package com.albenw.excel.converter;

import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.exception.ConvertException;

/**
 * @author alben.wong
 * @date 2018/2/2.
 */
public class NameConverter implements CellConverter<Integer, String> {

    @Override
    public String convertIn(Integer value) throws ConvertException {
        return null;
    }

    @Override
    public Integer convertOut(String value) throws ConvertException {
        String origin = (String) value;
        return 1;
    }
}
