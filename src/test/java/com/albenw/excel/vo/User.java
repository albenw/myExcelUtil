package com.albenw.excel.vo;

import com.albenw.excel.annotation.ExportField;
import com.albenw.excel.annotation.ImportField;
import com.albenw.excel.converter.DateConverter;
import com.albenw.excel.converter.GenderConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
@Getter @Setter @ToString
public class User {

    @ExportField(index = 1, headerName = "名字")
    @ImportField(index = 1)
    private String name;

    @ExportField(index = 2, headerName = "性别", converter = GenderConverter.class)
    @ImportField(index = 2, converter = GenderConverter.class)
    private Integer gender;

    @ExportField(index = 3, headerName = "年纪")
    @ImportField(index = 3)
    private Integer age;

    @ExportField(index = 4, headerName = "电子邮件")
    @ImportField(index = 4)
    private String email;

    @ExportField(index = 5, headerName = "地址")
    @ImportField(index = 5)
    private String address;

    @ExportField(index = 6, headerName = "电话")
    @ImportField(index = 6)
    private String phoneNo;

    @ExportField(index = 7, headerName = "生日", converter = DateConverter.class)
    @ImportField(index = 7)
    private Date birthday;

    private Double asset;

}
