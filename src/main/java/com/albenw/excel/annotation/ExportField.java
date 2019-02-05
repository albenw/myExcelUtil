package com.albenw.excel.annotation;

import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.base.converter.DefaultCellConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author alben.wong
 * @date 2017/10/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportField {

    /**
     * 列中文名，为空则使用字段名
     * @return
     */
    String headerName() default "";

    /**
     * 按照排列进行输出
     * 注：只表示相对顺序，不代表固定位置
     * @return
     */
    int index() default 1;

    /**
     * 对字段进行转换
     * 导出时使用 convertOut 方法进行转换
     * @return
     */
    Class<? extends CellConverter> converter() default DefaultCellConverter.class;


}
