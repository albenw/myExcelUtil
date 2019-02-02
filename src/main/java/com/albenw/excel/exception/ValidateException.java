package com.albenw.excel.exception;

/**
 * @Author alben.wong
 * @Date 2017/10/13.
 */
public class ValidateException extends ExcelException{

    public ValidateException() {
        super();
    }

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }

}
