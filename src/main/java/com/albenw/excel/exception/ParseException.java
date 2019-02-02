package com.albenw.excel.exception;

/**
 * @author alben.wong
 * @date 2018/2/2.
 */
public class ParseException extends ExcelException {
    public ParseException(){
        super();
    }

    public ParseException(String messaage){
        super(messaage);
    }

    public ParseException(Throwable throwable){
        super(throwable);
    }

    public ParseException(String message, Throwable throwable){
        super(message, throwable);
    }
}
