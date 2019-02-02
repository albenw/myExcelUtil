package com.albenw.excel.base.converter;

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

}
