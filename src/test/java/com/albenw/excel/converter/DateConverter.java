package com.albenw.excel.converter;

import com.albenw.excel.base.converter.AbstractCellConverter;
import com.albenw.excel.exception.ExcelException;
import com.albenw.excel.util.DateUtil;

import java.util.Date;

/**
 * @author alben.wong
 * @since 2019-02-05.
 */
public class DateConverter extends AbstractCellConverter<String, Date> {

    @Override
    public Date convertIn(String value) throws ExcelException {
        return null;
    }

    @Override
    public String convertOut(Date value) throws ExcelException {
        return DateUtil.format(value, DateUtil.DEFAULT_DATE_TIME_FORMAT);
    }
}
