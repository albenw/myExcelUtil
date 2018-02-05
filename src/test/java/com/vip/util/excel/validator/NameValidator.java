package com.vip.util.excel.validator;

import com.vip.util.excel.base.ValidateResult;
import com.vip.util.excel.base.validator.CellValidator;
import com.vip.util.excel.exception.ValidateException;

/**
 * @author alben.wong
 * @date 2018/2/2.
 */
public class NameValidator extends CellValidator{
    @Override
    public ValidateResult validate(Object cellValue) throws ValidateException {
        String name = (String) cellValue;
        if(name.indexOf("_1") > 0){
            return ValidateResult.buildFailWithMessage("名字->" + name + ", 有错");
        }
        return ValidateResult.buildSuccess();
    }
}
