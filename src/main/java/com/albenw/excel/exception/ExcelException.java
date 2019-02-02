package com.albenw.excel.exception;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
public class ExcelException extends Exception{

    public ExcelException() {
        super();
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

}
