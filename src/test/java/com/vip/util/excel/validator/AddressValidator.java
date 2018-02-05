package com.vip.util.excel.validator;

import com.vip.util.excel.base.ValidateResult;
import com.vip.util.excel.base.validator.CellValidator;
import com.vip.util.excel.exception.ValidateException;

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
