package com.albenw.excel.base.converter;

import com.albenw.excel.exception.ExcelException;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
public class DefaultCellConverter extends AbstractCellConverter<Object, Object> {

    @Override
    public Object convertIn(Object value) throws ExcelException {
        return null;
    }

    @Override
    public Object convertOut(Object value) throws ExcelException {
        return null;
    }

}
