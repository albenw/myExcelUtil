package com.albenw.util.excel.validator;

import com.albenw.util.excel.exception.ValidateException;
import com.albenw.util.excel.base.ValidateResult;
import com.albenw.util.excel.base.validator.CellValidator;

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
