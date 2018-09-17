package com.albenw.util.excel.annotation;

import com.albenw.util.excel.base.converter.CellConverter;
import com.albenw.util.excel.base.converter.DefaultCellConverter;
import com.albenw.util.excel.base.validator.CellValidator;
import com.albenw.util.excel.base.validator.DefaultCellValidator;

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
public @interface ImportColumn {

    /**
     * 按照排列进行导入
     * 注：只表示排列，不表示固定位置
     * @return
     */
    int index() default 1;

    /**
     * 导入时进行列验证
     * @return
     */
    Class<? extends CellValidator> validator() default DefaultCellValidator.class;

    /**
     * 对字段进行转换
     * 导入时使用 convertIn 方法进行转换
     * @return
     */
    Class<? extends CellConverter> converter() default DefaultCellConverter.class;

}
