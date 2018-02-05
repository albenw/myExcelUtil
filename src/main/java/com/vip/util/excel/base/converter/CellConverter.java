package com.vip.util.excel.base.converter;
import com.vip.util.excel.exception.ConvertException;

/**
 * @author alben.wong
 * @date 2017/10/11.
 */
public interface CellConverter {

    /**
     * 导入时转换
     * @param value
     * @return
     * @throws ConvertException
     */
    Object convertIn(Object value) throws ConvertException;

    /**
     * 导出时转换
     * @param value
     * @return
     * @throws ConvertException
     */
    Object convertOut(Object value) throws ConvertException;

}
