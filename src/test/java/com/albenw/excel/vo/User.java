package com.albenw.excel.vo;

import com.albenw.excel.annotation.ExportField;
import com.albenw.excel.annotation.ExportSheet;
import com.albenw.excel.annotation.ImportField;
import com.albenw.excel.base.constant.ExcelTypeEnum;
import com.albenw.excel.base.converter.CellConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
@ExportSheet(excelFileType = ExcelTypeEnum.XLSX, startRow = 1, sheetName = "xxxoo", useTemplateHeaderStyle = "123")
@Getter @Setter
@ToString
public class User {

    @ExportField(index = 1, headerName = "名字")
    @ImportField(index = 1)
    private String name;

    @ExportField(index = 2, headerName = "年纪")
    @ImportField(index = 2)
    private Integer age;

    @ExportField(index = 3, headerName = "电子邮件")
    @ImportField(index = 3)
    private String email;

    @ExportField(index = 4, headerName = "地址")
    @ImportField(index = 4)
    private String address;

    @ExportField(index = 5, headerName = "电话")
    @ImportField(index = 5)
    private String phoneNo;

    @ExportField(index = 6, headerName = "生日",format = "yyyy-MM-dd")
    @ImportField(index = 6, converter = CellConverter.class)
    private Date birthday;

    private Double asset;

}
