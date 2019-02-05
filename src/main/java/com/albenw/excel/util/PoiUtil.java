package com.albenw.excel.util;

import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;

/**
 * @author alben.wong
 * @date 2017/11/16.
 */
@Slf4j
public class PoiUtil {

    public static void transformToField(Object instance, Field field, Cell cell) throws IllegalAccessException, ParseException, ExcelException{
        field.setAccessible(true);
        String type = field.getGenericType().getTypeName();
        if("java.lang.String".equals(type)){
            cell.setCellType(CellType.STRING);
            String stringCellValue = StringUtil.trimToEmpty(cell.getStringCellValue());
            field.set(instance, stringCellValue);
        }
        else if("int".equals(type) || "java.lang.Integer".equals(type)){
            cell.setCellType(CellType.STRING);
            String stringCellValue = StringUtil.trimToEmpty(cell.getStringCellValue());
            if(StringUtil.isNotBlank(stringCellValue)){
                Integer intValue = Integer.valueOf(stringCellValue);
                field.set(instance, intValue);
            }
        }
        else if("long".equals(type)||"java.lang.Long".equals(type)){
            cell.setCellType(CellType.STRING);
            String stringCellValue = StringUtil.trimToEmpty(cell.getStringCellValue());
            if(StringUtil.isNotBlank(stringCellValue)){
                Long longValue = Long.valueOf(stringCellValue);
                field.set(instance, longValue);
            }
        }
        else if("java.util.Date".equals(type)){
            Date date = tryParseDate(cell);
            field.set(instance, date);
        }else if("java.lang.Double".equals(type) || "double".equals(type)){
            cell.setCellType(CellType.STRING);
            String stringCellValue = StringUtil.trimToEmpty(cell.getStringCellValue());
            if(StringUtil.isNotBlank(stringCellValue)){
                Double doubleValue = Double.valueOf(stringCellValue);
                field.set(instance, doubleValue);
            }
        }else if("java.lang.Float".equals(type) || "float".equals(type)){
            cell.setCellType(CellType.STRING);
            String stringCellValue = StringUtil.trimToEmpty(cell.getStringCellValue());
            if(StringUtil.isNotBlank(stringCellValue)){
                Float floatValue = Float.valueOf(stringCellValue);
                field.set(instance, floatValue);
            }
        }else{
            throw new ExcelException(ErrorCode.UNSUPPORT_DATA_TYPE);
        }
    }

    public static void setFieldValue(Object instance, Field field, Object value) throws IllegalAccessException{
        field.setAccessible(true);
        field.set(instance, value);
    }

    public static String getCellStringValue(Cell cell){
        cell.setCellType(CellType.STRING);
        String stringCellValue = cell.getStringCellValue();
        return StringUtil.trimToEmpty(stringCellValue);
    }

    public static Date tryParseDate(Cell cell) throws ParseException {
        if(CellType.NUMERIC.equals(cell.getCellType())){
            return com.albenw.excel.util.DateUtil.tryParse(cell.toString());
        }
        if(CellType.STRING.equals(cell.getCellType())){
            return com.albenw.excel.util.DateUtil.tryParse(cell.getStringCellValue());
        }
        if(HSSFDateUtil.isCellDateFormatted(cell)){
            return cell.getDateCellValue();
        }
        return null;
    }

}
