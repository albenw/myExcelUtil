package com.alben.util.excel.exception;

/**
 * @author alben.wong
 * @date 2018/2/2.
 */
public class ExcelUtilException extends Exception{
    public ExcelUtilException(){
        super();
    }

    public ExcelUtilException(String messaage){
        super(messaage);
    }

    public ExcelUtilException(Throwable throwable){
        super(throwable);
    }

    public ExcelUtilException(String message, Throwable throwable){
        super(message, throwable);
    }
}
