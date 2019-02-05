package com.albenw.excel.util;

import com.albenw.excel.annotation.ExportField;
import com.albenw.excel.base.IndexingField;
import com.albenw.excel.base.constant.ExcelTypeEnum;
import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.base.converter.ConverterHelper;
import com.albenw.excel.exception.ExcelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
                log.error("初始化日期异常 -> value = " + dateStr, e);
                value = null;
            }
        }
        return value;
    }

}
