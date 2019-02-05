package com.albenw.excel.util;

import com.albenw.excel.annotation.ExportField;
import com.albenw.excel.base.IndexingField;
import com.albenw.excel.base.constant.ExcelTypeEnum;
import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.base.converter.ConverterHelper;
import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-02-05.
 */
@Slf4j
public class WorkBookUtil {

    public static void createHeaderRow(Workbook workbook , Sheet sheet, List<IndexingField> indexingFields){
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        Font font = workbook.createFont();
        cellStyle.setFont(font);
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < indexingFields.size(); i++){
            Field field = indexingFields.get(i).getField();
            ExportField exportColumn = AnnotationUtil.getExportField(field);
            if(exportColumn == null){
                continue;
            }
            String headerName = exportColumn.headerName();
            Cell cell = headerRow.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(StringUtil.isBlank(headerName) ? String.valueOf(field.getName()) : headerName);
            cell.setCellStyle(cellStyle);
        }
    }

    public static  <T> void createDataRow(List<T> list, Sheet sheet, List<IndexingField> indexingFields) throws ExcelException{
        if(CollectionUtil.isEmpty(list)){
            return;
        }
        for(int rowNum = 0; rowNum < list.size(); rowNum++){
            Row row = sheet.createRow(rowNum + 1);
            T data = list.get(rowNum);
            for(int filedIndex = 0; filedIndex < indexingFields.size(); filedIndex++){
                try{
                    Field field = indexingFields.get(filedIndex).getField();
                    ExportField exportColumn = AnnotationUtil.getExportField(field);
                    field.setAccessible(true);
                    String result = "";
                    CellType cellType = CellType.STRING;
                    Object fieldValue = field.get(data);
                    //进行转换
                    CellConverter cellConverter = ConverterHelper.getCellConverter(exportColumn.converter());
                    if(ConverterHelper.isDefaultCellConverter(cellConverter)){
                        result = String.valueOf(fieldValue);
                    }else{
                        Object convertValue = cellConverter.convertOut(fieldValue);
                        result = String.valueOf(convertValue);
                    }
                    Cell cell = row.createCell(filedIndex);
                    cell.setCellType(cellType);
                    cell.setCellValue(result);
                }catch (IllegalAccessException| InstantiationException e){
                    throw new ExcelException(ErrorCode.FIELD_PARSE_ERROR, e.getMessage());
                }
            }
        }
    }

    public static Workbook getWorkbook(ExcelTypeEnum excelFileEnum) throws ExcelException{
        Workbook workbook;
        if(ExcelTypeEnum.XLS.equals(excelFileEnum)){
            workbook = new HSSFWorkbook();
        } else if(ExcelTypeEnum.XLSX.equals(excelFileEnum)){
            workbook = new XSSFWorkbook();
        } else{
            throw new ExcelException(ErrorCode.UNSUPPORT_FILE_TYPE_ERROR);
        }
        return workbook;
    }

}
