package com.vip.util.excel.annotation;

import com.vip.util.excel.base.converter.CellConverter;
import com.vip.util.excel.base.converter.DefaultCellConverter;

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
public @interface ExportColumn {

    /**
     * 列中文名，为空则使用字段名，导出时使用
     * @return
     */
    String headerName() default "";

    /**
     * 按照排列进行输出
     * 注：只表示排列，不表示固定位置
     * @return
     */
    int index() default 1;

    /**
     * 对字段进行转换
     * 导出时使用 convertOut 方法进行转换
     * @return
     */
    Class<? extends CellConverter> converter() default DefaultCellConverter.class;

    /**
     * 格式：根据字段的类型进行格式化，Float／Double 保留两位小数 - "%.2f"; Data 格式化 - "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    String format() default "";

}
