package com.alben.util.excel.base.converter;

import com.alben.util.excel.exception.ConvertException;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
public class DefaultCellConverter implements CellConverter {
    
    @Override
    public Object convertIn(Object value) throws ConvertException {
        return value;
    }

    @Override
    public Object convertOut(Object value) throws ConvertException {
        return value;
    }
}