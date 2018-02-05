package com.alben.util.excel.annotation;

import com.alben.util.excel.base.ExcelFileEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExportSheet {

    /**
     * 从第几行导出，默认是第二行
     * @return
     */
    int startRow() default 1;
    /**
     * 导出时 sheet 的名字
     * @return
     */
    String sheetName() default "mySheet";

    /**
     * 导出excel文件类型
     * @return
     */
    ExcelFileEnum excelFileType() default ExcelFileEnum.XLSX;

    /**
     * 使用模板的头样式，值为文件名，放在 classpath 下的 excelTemplate 目录下
     * @return
     */
    String useTemplateHeaderStyle() default "";

}
