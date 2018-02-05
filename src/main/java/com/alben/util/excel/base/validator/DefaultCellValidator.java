package com.alben.util.excel.base.validator;

import com.alben.util.excel.base.ValidateResult;
import com.alben.util.excel.base.validator.CellValidator;
import com.alben.util.excel.exception.ValidateException;

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
