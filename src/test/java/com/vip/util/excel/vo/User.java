package com.vip.util.excel.vo;

import com.alben.util.excel.annotation.*;
import com.vip.util.excel.annotation.ImportColumn;
import com.vip.util.excel.base.ExcelFileEnum;
import com.vip.util.excel.validator.AddressValidator;
import com.vip.util.excel.validator.NameValidator;
import com.vip.util.excel.annotation.ExportColumn;
import com.vip.util.excel.annotation.ExportSheet;
import com.vip.util.excel.annotation.ImportSheet;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
@ExportSheet(excelFileType = ExcelFileEnum.XLSX, startRow = 1, sheetName = "xxxoo", useTemplateHeaderStyle = "123")
@ImportSheet(validAll = false, useHibernateValidator = true)
@Getter @Setter
public class User {

    @ExportColumn(index = 1, headerName = "名字")
    @ImportColumn(index = 1, validator = NameValidator.class)
    @NotBlank @Length(min = 5, max = 30)
    private String name;

    @ExportColumn(index = 2, headerName = "年纪")
    @ImportColumn(index = 2)
    @Min(18) @Max(60)
    private Integer age;

    @ExportColumn(index = 3, headerName = "电子邮件")
    @ImportColumn(index = 3)
    @Email
    private String email;

    @ExportColumn(index = 4, headerName = "地址")
    @ImportColumn(index = 4, validator = AddressValidator.class)
    @NotBlank(message = "地址不能为空")
    private String address;

    @ExportColumn(index = 5, headerName = "电话")
    @ImportColumn(index = 5)
    private String phoneNo;

    @ExportColumn(index = 6, headerName = "生日",format = "yyyy-MM-dd")
    @ImportColumn(index = 6)
    private Date birthday;

//    @ExportColumn(index = 7, headerName = "资产", format = "%.2f")
//    @ImportColumn(index = 7)
    private Double asset;

}
