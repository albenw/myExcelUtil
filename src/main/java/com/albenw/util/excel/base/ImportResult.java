package com.albenw.util.excel.base;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author alben.wong
 * @Date 2017/10/11.
 */
public class ImportResult<T> {

    private List<ImportLog<T>> logs;
    private List<T> data;
    private boolean success = true;

    public ImportResult(){
        this.logs = new LinkedList<>();
        this.data = new LinkedList<>();
    }

    public List<ImportLog<T>> getLogs() {
        return logs;
    }

    public void setLogs(List<ImportLog<T>> logs) {
        this.logs = logs;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
