package com.albenw.excel.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
@Getter
@Setter
public class ExcelException extends Exception {

    private ErrorCode errorCode;

    public ExcelException() {
        super();
    }

    public ExcelException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        if(errorCode != null){
            builder.append("code=").append(errorCode.getCode()).append("msg=").append(errorCode.getMsg()).append(";");
        }
        return builder.toString() + super.getMessage();
    }
}
