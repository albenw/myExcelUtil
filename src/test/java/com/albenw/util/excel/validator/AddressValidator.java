package com.albenw.util.excel.validator;

import com.albenw.util.excel.exception.ValidateException;
import com.albenw.util.excel.base.ValidateResult;
import com.albenw.util.excel.base.validator.CellValidator;

/**
 * @author alben.wong
 * @date 2018/2/2.
 */
public class AddressValidator extends CellValidator {
    @Override
    public ValidateResult validate(Object cellValue) throws ValidateException {
        String address = (String) cellValue;
        if(address.indexOf("_2") > 0){
            return ValidateResult.buildFailWithMessage("错误的地址->" + address);
        }
        return ValidateResult.buildSuccess();
    }
}
