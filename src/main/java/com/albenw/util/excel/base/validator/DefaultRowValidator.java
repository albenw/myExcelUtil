package com.albenw.util.excel.base.validator;

import com.albenw.util.excel.base.ValidateResult;
import com.albenw.util.excel.exception.ValidateException;

/**
 * @author alben.wong
 * @date 2018/2/4.
 */
public class DefaultRowValidator extends RowValidator{
    @Override
    public ValidateResult validate(Object rowValue) throws ValidateException {
        return ValidateResult.buildSuccess();
    }
}
