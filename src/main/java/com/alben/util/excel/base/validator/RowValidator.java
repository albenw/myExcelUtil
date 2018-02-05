package com.alben.util.excel.base.validator;

import com.alben.util.excel.base.ValidateResult;
import com.alben.util.excel.exception.ValidateException;

/**
 * @author alben.wong
 * @date 2018/2/4.
 */
public abstract class RowValidator implements Validator{
    /**
     * 验证行
     * @param rowValue
     * @return
     * @throws ValidateException
     */
    @Override
    public abstract ValidateResult validate(Object rowValue) throws ValidateException;
    
}
