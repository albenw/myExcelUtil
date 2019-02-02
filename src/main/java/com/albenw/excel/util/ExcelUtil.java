package com.albenw.excel.util;

import com.albenw.excel.annotation.ExportField;
import com.albenw.excel.annotation.ExportSheet;
import com.albenw.excel.base.IndexingField;
import com.albenw.util.excel.base.*;
import com.albenw.excel.base.constant.ExcelTypeEnum;
import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.base.converter.ConverterHelper;
import com.albenw.excel.exception.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author alben.wong
 * @Date 2017/10/11.
 */
@Slf4j
public final class ExcelUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    private static volatile ExcelUtil excelUtil = null;

    private ExcelUtil(){}

    /**
     * 导出到excel
     * @param list
     * @param os
     * @param clazz
     * @param <T>
     * @throws Exception
     */
    public <T> void exportTo(List<T> list, OutputStream os, Class<T> clazz) throws Throwable{
        ExportSheet exportSheet = AnnotationUtil.getExportSheet(clazz);
        if(exportSheet == null){
            throw new ParseException("缺少ExportSheet注解");
        }
        Workbook workbook = getWorkbook(exportSheet.excelFileType());
        List<IndexingField> indexingFields = CollectionUtil.sortExportFieldByIndex(clazz);
        if(CollectionUtil.isEmpty(indexingFields)){
            throw new ParseException("没有需要导出的列");
        }
        String sheetName = exportSheet.sheetName();
        //TODO 只导出第一个sheet
        Sheet sheet = workbook.createSheet(sheetName);
        String templateFileName = getTemplateName(clazz);
        if(StringUtil.isNotBlank(templateFileName)){
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
            ExportField exportColumn = AnnotationUtil.getExportColumn(field);
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
            throws Throwable{
        if(CollectionUtil.isEmpty(list)){
            return;
        }
        for(int rowNum = 0; rowNum < list.size(); rowNum++){
            Row row = sheet.createRow(rowNum + startRow);
            T data = list.get(rowNum);
            for(int filedIndex = 0; filedIndex < indexingFields.size(); filedIndex++){
                IndexingField indexingField = indexingFields.get(filedIndex);
                Field field = indexingField.getField();
                ExportField exportColumn = AnnotationUtil.getExportColumn(field);
                String fieldType = field.getGenericType().getTypeName();
                field.setAccessible(true);
                String result = "";
                CellType cellType = CellType.STRING;
                Object fieldValue = field.get(data);
                //进行转换
                try{
                    CellConverter cellConverter = ConverterHelper.getCellConverter(exportColumn.converter());
                    fieldValue = cellConverter.convertOut(fieldValue);
                }catch (Exception e){
                    throw new ParseException(e.getMessage(), e);
                }
                if ("java.lang.String".equalsIgnoreCase(fieldType)) {
                    result = StringUtil.trimToEmpty((String)fieldValue);
                } else if ("java.util.Date".equalsIgnoreCase(fieldType)){
                    Date date = (Date)fieldValue;
                    String format = exportColumn.format();
                    if(StringUtil.isBlank(format)){
                        format = com.albenw.excel.util.DateUtil.DEFAULT_DATE_FORMAT;
                    }
                    if(date != null){
                        result = DateUtil.format(date, format);
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
                        if(StringUtil.isNotBlank(format)){
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
            } catch (java.text.ParseException e) {
                LOGGER.error("初始化日期异常 -> value = " + dateStr, e);
                value = null;
            }
        }
        return value;
    }

    public <T> Class getGenericType(List<T> list){
        if(CollectionUtil.isNotEmpty(list)){
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

    private Workbook getWorkbook(ExcelTypeEnum excelFileEnum){
        Workbook workbook = null;
        if(ExcelTypeEnum.XLS.equals(excelFileEnum)){
            workbook = new HSSFWorkbook();
        }
        if(ExcelTypeEnum.XLSX.equals(excelFileEnum)){
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

}
