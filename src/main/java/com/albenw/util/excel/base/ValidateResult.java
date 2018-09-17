package com.albenw.util.excel.base;

/**
 * @author alben.wong
 * @date 2017/10/13.
 */
public class ValidateResult {

    private boolean success;
    private String message;

    private ValidateResult(boolean success, String failMessage){
        this.success = success;
        this.message = failMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ValidateResult buildSuccess(){
        return new ValidateResult(true, "");
    }

    public static ValidateResult buildSuccessWithMessage(String message){
        return new ValidateResult(true, message);
    }

    public static ValidateResult buildFailWithMessage(String message){
        return new ValidateResult(false, message);
    }

}