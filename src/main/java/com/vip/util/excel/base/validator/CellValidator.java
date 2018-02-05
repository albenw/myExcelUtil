package com.vip.util.excel.base.validator;

import com.vip.util.excel.base.ValidateResult;
import com.vip.util.excel.exception.ValidateException;

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
