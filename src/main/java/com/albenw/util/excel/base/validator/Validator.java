package com.albenw.util.excel.base.validator;

import com.albenw.util.excel.base.ValidateResult;
import com.albenw.util.excel.exception.ValidateException;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
public interface Validator {

    /**
     * 验证
     * @param value
     * @return
     * @throws ValidateException
     */
    ValidateResult validate(Object value) throws ValidateException;

}
