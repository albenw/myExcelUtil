package com.vip.util.excel.base.validator;

import com.vip.util.excel.exception.ValidateException;
import com.vip.util.excel.base.ValidateResult;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
public class DefaultCellValidator extends CellValidator {

    @Override
    public ValidateResult validate(Object cellValue) throws ValidateException {
        return ValidateResult.buildSuccess();
    }
}
