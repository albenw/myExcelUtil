package com.alben.util.excel.base.validator;

import com.alben.util.excel.base.ValidateResult;
import com.alben.util.excel.exception.ValidateException;

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
