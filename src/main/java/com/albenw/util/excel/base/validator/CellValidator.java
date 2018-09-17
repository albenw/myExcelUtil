package com.albenw.util.excel.base.validator;

import com.albenw.util.excel.exception.ValidateException;
import com.albenw.util.excel.base.ValidateResult;

/**
 * @author alben.wong
 * @date 2017/10/13.
 */
public abstract class CellValidator implements Validator {

    /**
     * 验证列
     * @param cellValue
     * @return
     * @throws ValidateException
     */
    @Override
    public abstract ValidateResult validate(Object cellValue) throws ValidateException;

}
