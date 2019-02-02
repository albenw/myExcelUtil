package com.albenw.excel.exception;

/**
 * @Author alben.wong
 * @Date 2017/10/11.
 */
public class ConvertException extends ExcelException{

    public ConvertException() {
        super();
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertException(Throwable cause) {
        super(cause);
    }

}
