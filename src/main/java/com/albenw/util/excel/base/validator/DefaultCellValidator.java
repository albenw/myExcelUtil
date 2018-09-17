package com.albenw.util.excel.base.validator;

import com.albenw.util.excel.exception.ValidateException;
import com.albenw.util.excel.base.ValidateResult;

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
