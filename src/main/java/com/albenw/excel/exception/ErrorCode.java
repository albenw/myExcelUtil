package com.albenw.excel.exception;

import lombok.Getter;

/**
 * @author alben.wong
 * @since 2019-02-03.
 */
@Getter
public enum ErrorCode {

    //未知文件异常
    UNSUPPORT_FILE_TYPE_ERROR("unsupport_file_type_error","不支持文件"),
    //参数异常
    PARAMETER_ERROR("parameter_error","参数配置异常"),
    //字段解析异常
    FIELD_PARSE_ERROR("field_parse_error", "字段解析异常"),
    //不支持数据类型
    UNSUPPORT_DATA_TYPE("unsupport_data_type", "不支持数据类型"),
    //缺少注解
    LACK_OF_ANNOTATION("lack_of_annotation", "缺少注解"),
    //未知异常
    UNKNOWN_EXCEPTION("unknown_exception", "未知异常");

    private String code;
    private String msg;

    ErrorCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
