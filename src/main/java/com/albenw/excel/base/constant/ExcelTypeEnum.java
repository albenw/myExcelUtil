package com.albenw.excel.base.constant;

/**
 * @author alben.wong
 * @date 2017/11/15.
 */
public enum ExcelTypeEnum {
    //03版本excel
    XLS("xls"),
    //07版本excel
    XLSX("xlsx");

    private String value;

    ExcelTypeEnum(String value){
        this.value = value;
    }

}
