package com.vip.util.excel;

import com.vip.util.excel.annotation.ExportColumn;
import com.vip.util.excel.annotation.ExportSheet;
import com.vip.util.excel.annotation.ImportColumn;
import com.vip.util.excel.annotation.ImportSheet;
import com.vip.util.excel.base.*;
import com.vip.util.excel.base.converter.CellConverter;
import com.vip.util.excel.base.validator.CellValidator;
import com.vip.util.excel.base.validator.RowValidator;
import com.vip.util.excel.exception.ExcelUtilException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author alben.wong
 * @Date 2017/10/11.
 */
@Slf4j
public final class ExcelUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    private static Map<String, CellConverter> cellConverterCache = new ConcurrentHashMap<>();
    private static Map<String ,CellValidator> cellValidatorCache = new ConcurrentHashMap<>();
    private static Map<String, RowValidator> rowValidatorCache = new ConcurrentHashMap<>();
    private static volatile ExcelUtil excelUtil = null;

    /**
     * 导出excel模板路径
     */
    private static final String EXCEL_TEMPLATE_PATH = "excelTemplate/";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private ExcelUtil(){}

    /**
     * excel导入
     * @param clazz
     * @param inputStream
     * @param <T>
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> ImportResult<T> importFrom(Class clazz, InputStream inputStream) throws Exception{

        ImportResult<T> importResult = new ImportResult<T>();
        Workbook workbook = WorkbookFactory.create(inputStream);
        //TODO 只导入到第一个sheet ?
        Sheet sheet = workbook.getSheetAt(0);
        ImportSheet importSheet = (ImportSheet)clazz.getAnnotation(ImportSheet.class);
        if(importSheet == null){
            throw new ExcelUtilException("class ->" + clazz.getName() + ", 缺少'ImportSheet'注解");
        }
        int startRow = importSheet.startRow();
        List<IndexingField> indexingFields = sortImportFieldByIndex(clazz);
        Row headRow = sheet.getRow(startRow);
        for (int i = startRow; i < sheet.getLastRowNum() + 1; i++){
            Row row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            T t = (T) clazz.newInstance();
            ImportLog importLog = new ImportLog();
            for (int j = 0; j < headRow.getLastCellNum(); j++){
                Cell cell = row.getCell(j);
                //截取掉多余的列
                if( j >= indexingFields.size() || cell == null){
                    continue;
                }
                Object cellValue = getCellValue(cell);
                Object value;
                Field field = indexingFields.get(j).getField();
                ImportColumn importColumn = this.getImportColumn(field);
                try {
                    //进行转换
                    CellConverter cellConverter = getCellConverter(importColumn.converter());
                    value = cellConverter.convertIn(cellValue);
                    //进行验证
                    CellValidator validator = this.getCellValidator(importColumn.validator());
                    ValidateResult validateResult = validator.validate(value);
                    if(!validateResult.isSuccess()){
                        importLog.setHasError(Boolean.TRUE);
                        importLog.appendLogMessage(validateResult.getMessage()).appendLogMessage(";");
                        //验证失败不进行值转换了
                        continue;
                    }
                } catch (Exception e) {
                    throw new ExcelUtilException(e.getMessage(), e);
                }
                this.transform(t, field, cell);
            }
            //hibernate-validator 验证
            if(importSheet.useHibernateValidator()){
                String errorMessage = ValidationUtils.getValidationMessage(ValidationUtils.validate(t));
                if(StringUtils.isNotBlank(errorMessage)){
                    importLog.appendLogMessage(errorMessage);
                }
            }
            //行数据验证
            RowValidator rowValidator = this.getRowValidator(importSheet.rowValidator());
            ValidateResult rowValidResult = rowValidator.validate(t);
            if(!rowValidResult.isSuccess()){
                importLog.setHasError(Boolean.TRUE);
                importLog.appendLogMessage(rowValidResult.getMessage()).appendLogMessage(";");
            }
            rowValidator.validate(t);
            importResult.getData().add(t);
            importLog.setRowData(t);
            importLog.setRowNum(i + 1);
            importResult.getLogs().add(importLog);
            if(importSheet.validAll() && Boolean.TRUE.equals(importLog.getHasError())){
                importResult.setSuccess(false);
                break;
            }
        }
        return importResult;
    }

    /**
     * 导出到excel
     * @param list
     * @param os
     * @param clazz
     * @param <T>
     * @throws Exception
     */
    public <T> void exportTo(List<T> list, OutputStream os, Class<T> clazz) throws Exception{
        ExportSheet exportSheet = this.getExportSheet(clazz);
        if(exportSheet == null){
            throw new ExcelUtilException("缺少ExportSheet注解");
        }
        Workbook workbook = getWorkbook(exportSheet.excelFileType());
        List<IndexingField> indexingFields = sortExportFieldByIndex(clazz);
        if(CollectionUtils.isEmpty(indexingFields)){
            throw new ExcelUtilException("没有需要导出的列");
        }
        String sheetName = exportSheet.sheetName();
        //TODO 只导出第一个sheet
        Sheet sheet = workbook.createSheet(sheetName);
        String templateFileName = getTemplateName(clazz);
        if(StringUtils.isNotBlank(templateFileName)){
            //TODO 可复制目标模板的格式样式
        }
        createHeaderRow(workbook, sheet, indexingFields);
        createDataRow(list, sheet, exportSheet.startRow(), indexingFields);
        writeToStream(workbook, os);

    }

    private void createHeaderRow(Workbook workbook , Sheet sheet, List<IndexingField> indexingFields){
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        Font font = workbook.createFont();
        cellStyle.setFont(font);
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < indexingFields.size(); i++){
            IndexingField indexingField = indexingFields.get(i);
            Field field = indexingField.getField();
            ExportColumn exportColumn = this.getExportColumn(field);
            if(exportColumn == null){
                continue;
            }
            String headerName = exportColumn.headerName();
            Cell cell = headerRow.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(headerName == null ? String.valueOf(i) : headerName);
            cell.setCellStyle(cellStyle);
        }
    }

    private <T> void createDataRow(List<T> list, Sheet sheet, int startRow, List<IndexingField> indexingFields)
            throws IllegalAccessException,NoSuchFieldException, ExcelUtilException{
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        for(int rowNum = 0; rowNum < list.size(); rowNum++){
            Row row = sheet.createRow(rowNum + startRow);
            T data = list.get(rowNum);
            for(int filedIndex = 0; filedIndex < indexingFields.size(); filedIndex++){
                IndexingField indexingField = indexingFields.get(filedIndex);
                Field field = indexingField.getField();
                ExportColumn exportColumn = this.getExportColumn(field);
                String fieldType = field.getGenericType().getTypeName();
                field.setAccessible(true);
                String result = "";
                CellType cellType = CellType.STRING;
                Object fieldValue = field.get(data);
                //进行转换
                try{
                    CellConverter cellConverter = this.getCellConverter(exportColumn.converter());
                    fieldValue = cellConverter.convertOut(fieldValue);
                }catch (Exception e){
                    throw new ExcelUtilException(e.getMessage(), e);
                }
                if ("java.lang.String".equalsIgnoreCase(fieldType)) {
                    result = StringUtils.trimToEmpty((String)fieldValue);
                } else if ("java.util.Date".equalsIgnoreCase(fieldType)){
                    Date date = (Date)fieldValue;
                    String format = exportColumn.format();
                    if(StringUtils.isBlank(format)){
                        format = DEFAULT_DATE_FORMAT;
                    }
                    if(date != null){
                        result = DateFormatUtils.format(date, format);
                    }
                } else if ("java.lang.Integer".equalsIgnoreCase(fieldType) || "int".equalsIgnoreCase(fieldType)){
                    Integer intValue = (Integer)fieldValue;
                    if(intValue != null){
                        result = String.valueOf(intValue);
                    }
                } else if("java.lang.Float".equalsIgnoreCase(fieldType) || "float".equalsIgnoreCase(fieldType)
                        || "java.lang.Double".equalsIgnoreCase(fieldType) || "double".equalsIgnoreCase(fieldType)){
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(fieldValue));
                    String format = exportColumn.format();
                    if(bigDecimal != null){
                        result = String.valueOf(bigDecimal.doubleValue());
                        if(StringUtils.isNotBlank(format)){
                            try{
                                result = String.format(format, bigDecimal.doubleValue());
                            }catch (IllegalFormatException e){
                                log.error(e.getMessage());
                                result = "";
                            }
                        }
                    }
                } else {
                    Object obj = field.get(data);
                    if(obj != null){
                        result = String.valueOf(obj);
                    }
                }
                Cell cell = row.createCell(filedIndex);
                cell.setCellType(cellType);
                cell.setCellValue(result);
            }
        }
    }


    public void writeToStream(Workbook workbook, OutputStream os) throws IOException{
        BufferedOutputStream bos = null;
        try{
            bos = new BufferedOutputStream(os);
            workbook.write(bos);
            bos.flush();
        }catch (IOException e){
            LOGGER.error("导出异常");
            throw  e;
        }finally {
            try {
                if(os != null) {
                    os.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭流异常");
            }
        }
    }

    /**
     * 根据annotation的 index 排序字段
     *
     * @param clazz
     * @return
     */
    private List<IndexingField> sortExportFieldByIndex(Class<?> clazz){
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<IndexingField> fields = new ArrayList<>();
        List<IndexingField> annoNullFields = new ArrayList<>();
        for (Field field : fieldsArr) {
            ExportColumn ec = this.getExportColumn(field);
            if (ec == null) {
                continue;
            }
            int index = ec.index();
            fields.add(new IndexingField(field, index));
        }
        fields.addAll(annoNullFields);
        sortByProperties(fields, true, false, "index");
        return fields;
    }

    /**
     * 根据annotation的 index 排序字段
     *
     * @param clazz
     * @return
     */
    private List<IndexingField> sortImportFieldByIndex(Class<?> clazz){
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<IndexingField> fields = new ArrayList<IndexingField>();
        List<IndexingField> annoNullFields = new ArrayList<IndexingField>();
        for (Field field : fieldsArr) {
            ImportColumn ec = this.getImportColumn(field);
            if (ec == null) {
                continue;
            }
            int index = ec.index();
            fields.add(new IndexingField(field, index));
        }
        fields.addAll(annoNullFields);
        sortByProperties(fields, true, false, "index");
        return fields;
    }


    @SuppressWarnings("unchecked")
    private void sortByProperties(List<? extends Object> list, boolean isNullHigh,
                                         boolean isReversed, String... props) {
        if (CollectionUtils.isNotEmpty(list)) {
            Comparator<?> typeComp = ComparableComparator.getInstance();
            if (isNullHigh == true) {
                typeComp = ComparatorUtils.nullHighComparator(typeComp);
            } else {
                typeComp = ComparatorUtils.nullLowComparator(typeComp);
            }
            if (isReversed) {
                typeComp = ComparatorUtils.reversedComparator(typeComp);
            }

            List<Object> sortCols = new ArrayList<Object>();

            if (props != null) {
                for (String prop : props) {
                    sortCols.add(new BeanComparator(prop, typeComp));
                }
            }
            if (sortCols.size() > 0) {
                Comparator<Object> sortChain = new ComparatorChain(sortCols);
                Collections.sort(list, sortChain);
            }
        }
    }

    /**
     * 获取单元格值
     *
     * @param cell
     * @return
     */
    private Object getCellValue(Cell cell) {
        if (cell == null
                || (cell.getCellTypeEnum() == CellType.STRING && StringUtils.isBlank(cell
                .getStringCellValue()))) {
            return null;
        }
        cell.getCellTypeEnum();
        CellType cellType = cell.getCellTypeEnum();
        if(cellType.equals(CellType.BLANK)){
            return null;
        }else if(cellType.equals(CellType.BOOLEAN)){
            return cell.getBooleanCellValue();
        }else if(cellType.equals(CellType.ERROR)){
            return cell.getErrorCellValue();
        }else if(cellType.equals(CellType.FORMULA)){
            return cell.getNumericCellValue();
        }else if(cellType.equals(CellType.NUMERIC)){
            return cell.getNumericCellValue();
        }else if(cellType.equals(CellType.STRING)){
            return cell.getStringCellValue();
        }else{
            return null;
        }
    }

    private CellConverter getCellConverter(Class<? extends CellConverter> clazz)
            throws IllegalAccessException, InstantiationException{
        CellConverter cellConverter = cellConverterCache.get(clazz.getName());
        if(cellConverter == null){
            cellConverter = clazz.newInstance();
            cellConverterCache.put(clazz.getName(), cellConverter);
        }
        return cellConverter;
    }

    private CellValidator getCellValidator(Class<? extends CellValidator> clazz) throws InstantiationException, IllegalAccessException{
        CellValidator validator = cellValidatorCache.get(clazz.getName());
        if(validator == null){
            validator = clazz.newInstance();
            cellValidatorCache.put(clazz.getName(), validator);
        }
        return validator;
    }

    private RowValidator getRowValidator(Class<? extends RowValidator> clazz) throws InstantiationException, IllegalAccessException{
        RowValidator rowValidator = rowValidatorCache.get(clazz);
        if(rowValidator == null){
            rowValidator = clazz.newInstance();
            rowValidatorCache.put(clazz.getName(), rowValidator);
        }
        return rowValidator;
    }

    private Object getAndFormatValue(Object value, Field field, String format){
        String fieldType = field.getGenericType().getTypeName();
        if("java.lang.String".equals(fieldType)){
            value = String.valueOf(value);
        }
        else if("int".equals(fieldType) || "java.lang.Integer".equals(fieldType)){
            value = Integer.valueOf(String.valueOf(value));
        }
        else if("long".equals(fieldType)|| "java.lang.Long".equals(fieldType)){
            value = Long.valueOf(String.valueOf(value));
        }
        else if("float".equals(fieldType) || "java.lang.Float".equals(fieldType)){
            String tmp = new DecimalFormat(format).format(Double.valueOf(String.valueOf(value)));
            value = Float.valueOf(tmp);
        }else if("double".equals(fieldType) || "java.lang.Double".equals(fieldType)){
            String tmp = new DecimalFormat(format).format(Double.valueOf(String.valueOf(value)));
            value = Double.valueOf(tmp);
        }
        else if("java.util.Date".equals(fieldType)){
            String dateStr = String.valueOf(value);
            try {
                Date date = new SimpleDateFormat(format).parse(dateStr);
                value = date;
            } catch (ParseException e) {
                LOGGER.error("初始化日期异常 -> value = " + dateStr, e);
                value = null;
            }
        }
        return value;
    }

    public <T> Class getGenericType(List<T> list){
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0).getClass();
        }
        try{
            Type type = list.getClass().getGenericSuperclass();
            Type firstType = type;
            if(firstType instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType)firstType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for(Type actualTypeArgument: actualTypeArguments) {
                    System.out.println(actualTypeArgument);
                }
                return (Class) parameterizedType.getActualTypeArguments()[0];
            }
        }catch (Exception e){
            LOGGER.error("获取泛型异常",e);
            return null;
        }
        return null;
    }

    private Workbook getWorkbook(ExcelFileEnum excelFileEnum){
        Workbook workbook = null;
        if(ExcelFileEnum.XLS.equals(excelFileEnum)){
            workbook = new HSSFWorkbook();
        }
        if(ExcelFileEnum.XLSX.equals(excelFileEnum)){
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    private String getTemplateName(Class clazz){
        String templateName = null;
        ExportSheet exportSheet = (ExportSheet) clazz.getAnnotation(ExportSheet.class);
        if(exportSheet != null){
            templateName = exportSheet.useTemplateHeaderStyle();
        }
        return templateName;
    }

    public void copyHeaderStyle(Sheet sheet, String templateName) throws IOException, InvalidFormatException{
        InputStream is = ExcelUtil.class.getClassLoader().getResourceAsStream(EXCEL_TEMPLATE_PATH + templateName);
        Workbook templateWorkBook = WorkbookFactory.create(is);
        POIUtil.copySheet(sheet.getWorkbook(), sheet, templateWorkBook.getSheetAt(0), true);
    }

    private Object transform(Object obj, final Field field, final Cell cell)
            throws NoSuchFieldException, IllegalAccessException, ParseException{
        field.setAccessible(true);
        String type = field.getGenericType().getTypeName();
        if("java.lang.String".equals(type)){
            cell.setCellType(CellType.STRING);
            String value = StringUtils.trimToEmpty(cell.getStringCellValue());
            field.set(obj,value);
        }
        else if("int".equals(type) || "java.lang.Integer".equals(type)){
            cell.setCellType(CellType.STRING);
            String cellValue = StringUtils.trimToNull(cell.getStringCellValue());
            if(cellValue != null){
                Integer intValue = Integer.valueOf(cellValue);
                field.set(obj, intValue);
            }
        }
        else if("long".equals(type)||"java.lang.Long".equals(type)){
            cell.setCellType(CellType.STRING);
            String value = StringUtils.trimToNull(cell.getStringCellValue());
            if(value != null){
                Long longValue = Long.valueOf(value);
                field.set(obj, longValue);
            }
        }
        else if("java.util.Date".equals(type)){
            Date date = getDate(cell);
            field.set(obj, date);
        }else if("java.lang.Double".equals(type) || "double".equals(type)){
            cell.setCellType(CellType.STRING);
            String value = StringUtils.trimToNull(cell.getStringCellValue());
            if(value != null){
                Double doubleValue = Double.valueOf(value);
                field.set(obj, doubleValue);
            }
        }else if("java.lang.Flaot".equals(type) || "float".equals(type)){
            cell.setCellType(CellType.STRING);
            String value = StringUtils.trimToNull(cell.getStringCellValue());
            if(value != null){
                Float floatValue = Float.valueOf(value);
                field.set(obj, floatValue);
            }
        }else{
            log.error("不支持的数据类型, type->{}", type);
        }
        return obj;
    }

    private Date getDate(Cell cell){
        CellType cellType = cell.getCellTypeEnum();
        try{
            if(CellType.NUMERIC.equals(cellType)){
                if(DateUtil.isCellDateFormatted(cell)){
                    return DateUtil.getJavaDate(cell.getNumericCellValue());
                }
            }else if(CellType.STRING.equals(cellType) || CellType.FORMULA.equals(cellType)){
                String value = cell.getStringCellValue();
                return DateUtils.parseDate(value, DEFAULT_DATE_FORMAT);
            }else{

            }
        }catch (ParseException e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static ExcelUtil getInstance(){
        if(excelUtil == null){
            synchronized (ExcelUtil.class){
                if(excelUtil == null){
                    excelUtil = new ExcelUtil();
                }
            }
        }
        return excelUtil;
    }

    private ExportSheet getExportSheet(Class clazz) throws ExcelUtilException{
        ExportSheet anno = (ExportSheet)clazz.getAnnotation(ExportSheet.class);
        if(anno == null){
            throw new ExcelUtilException("class -> " + clazz.getName() + ", 缺少 'ExportSheet' 注解 ");
        }
        return anno;
    }

    private ExportColumn getExportColumn(Field field){
        ExportColumn anno = field.getAnnotation(ExportColumn.class);
        return anno;
    }

    private ImportColumn getImportColumn(Field field){
        ImportColumn anno = field.getAnnotation(ImportColumn.class);
        return anno;
    }


}
