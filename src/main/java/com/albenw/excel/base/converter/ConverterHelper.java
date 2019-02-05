package com.albenw.excel.base.converter;

import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;
import com.albenw.excel.util.DateUtil;
import com.albenw.excel.util.ReflectionUtil;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
public class ConverterHelper {

    private static Map<String, CellConverter> cellConverterCache = new ConcurrentHashMap<>();

    public static CellConverter getCellConverter(Class<? extends CellConverter> clazz)
            throws IllegalAccessException, InstantiationException{
        CellConverter cellConverter = cellConverterCache.get(clazz.getName());
        if(cellConverter == null){
            cellConverter = clazz.newInstance();
            cellConverterCache.put(clazz.getName(), cellConverter);
        }
        return cellConverter;
    }

    public static Object getConvertInTypeValue(CellConverter cellConverter, String cellStringValue) throws ExcelException, ParseException {
        Type type = getConvertInParameterGenericType(cellConverter);
        if(type == null){
            throw new ExcelException(ErrorCode.FIELD_PARSE_ERROR, String.format("converter=[%s]获取泛型失败", cellConverter.getClass().getName()));
        }
        return ReflectionUtil.convertToTypeValueFromString(type, cellStringValue);
    }

    public static Type getConvertInParameterGenericType(CellConverter cellConverter){
        return ReflectionUtil.getGenericSuperType(cellConverter, 0);
    }

    public static Type getConvertOutParameterGenericType(CellConverter cellConverter){
        return ReflectionUtil.getGenericSuperType(cellConverter, 1);
    }


    public static boolean isDefaultCellConverter(CellConverter cellConverter){
        Class<? extends CellConverter> converterClass = cellConverter.getClass();
        return DefaultCellConverter.class.equals(converterClass);
    }

}
