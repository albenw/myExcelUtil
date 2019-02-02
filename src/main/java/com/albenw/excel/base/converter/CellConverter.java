package com.albenw.excel.base.converter;
import com.albenw.excel.exception.ConvertException;

/**
 * @author alben.wong
 * @date 2017/10/11.
 */
public interface CellConverter<E, F> {

    /**
     * 导入时转换
     * @return
     * @throws ConvertException
     */
    F convertIn(E value) throws ConvertException;

    /**
     * 导出时转换
     * @param value
     * @return
     * @throws ConvertException
     */
    E convertOut(F value) throws ConvertException;

}
