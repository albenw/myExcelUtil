package com.albenw.excel.base.converter;

import com.albenw.excel.exception.ExcelException;

/**
 * @author alben.wong
 * @date 2017/10/11.
 */
public interface CellConverter<E, F> {

    /**
     * 导入时转换
     * @return
     * @throws ExcelException
     */
    F convertIn(E value) throws ExcelException;

    /**
     * 导出时转换
     * @param value
     * @return
     * @throws ExcelException
     */
    E convertOut(F value) throws ExcelException;

}
