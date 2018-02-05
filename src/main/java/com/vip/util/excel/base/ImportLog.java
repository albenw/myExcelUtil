package com.vip.util.excel.base;

/**
 * @Author alben.wong
 * @Date 2017/10/11.
 */

/**
 * 每行的导入日志
 * @param <T>
 */
public class ImportLog<T> {

    /**
     * 实际第几行
     */
    private Integer rowNum;
    /**
     *
     */
    private T rowData;
    /**
     * 验证信息
     */
    private StringBuilder logMessage = new StringBuilder();
    /**
     * 这行导入验证是否有误
     */
    private Boolean hasError = Boolean.FALSE;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public T getRowData() {
        return rowData;
    }

    public void setRowData(T rowData) {
        this.rowData = rowData;
    }

    public String getLogMessage() {
        return logMessage.toString();
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public ImportLog appendLogMessage(String appendMessage){
        logMessage.append(appendMessage);
        return this;
    }

}
