package com.albenw.util.excel.converter;

import com.albenw.util.excel.base.converter.CellConverter;
import com.albenw.util.excel.exception.ConvertException;

/**
 * @author alben.wong
 * @date 2018/2/2.
 */
public class NameConverter implements CellConverter {

    @Override
    public Object convertIn(Object value) throws ConvertException {
        return null;
    }

    @Override
    public Object convertOut(Object value) throws ConvertException {
        String origin = (String) value;
        return origin + "_after";
    }
}
