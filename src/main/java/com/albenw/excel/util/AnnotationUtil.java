package com.albenw.excel.util;

import com.albenw.excel.annotation.ExportField;
import com.albenw.excel.annotation.ImportField;
import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;

import java.lang.reflect.Field;

/**
 * @author alben.wong
 * @date 2019-01-04.
 */
public class AnnotationUtil {

    public static ExportField getExportField(Field field){
        ExportField anno = field.getAnnotation(ExportField.class);
        return anno;
    }

    public static ImportField getImportField(Field field){
        ImportField anno = field.getAnnotation(ImportField.class);
        return anno;
    }

}
