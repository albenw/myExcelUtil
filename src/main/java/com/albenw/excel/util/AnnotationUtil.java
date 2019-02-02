package com.albenw.excel.util;

import com.albenw.excel.annotation.ExportField;
import com.albenw.excel.annotation.ExportSheet;
import com.albenw.excel.annotation.ImportField;
import com.albenw.excel.exception.ParseException;

import java.lang.reflect.Field;

/**
 * @author alben.wong
 * @date 2019-01-04.
 */
public class AnnotationUtil {


    public static ExportSheet getExportSheet(Class clazz) throws ParseException {
        ExportSheet anno = (ExportSheet)clazz.getAnnotation(ExportSheet.class);
        if(anno == null){
            throw new ParseException("class -> " + clazz.getName() + ", 缺少 'ExportSheet' 注解 ");
        }
        return anno;
    }

    public static ExportField getExportColumn(Field field){
        ExportField anno = field.getAnnotation(ExportField.class);
        return anno;
    }

    public static ImportField getImportColumn(Field field){
        ImportField anno = field.getAnnotation(ImportField.class);
        return anno;
    }

}
