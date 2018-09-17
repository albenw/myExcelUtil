package com.albenw.util.excel.annotation;

import com.albenw.util.excel.base.validator.DefaultRowValidator;
import com.albenw.util.excel.base.validator.RowValidator;

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
public @interface ImportSheet {

    /**
     * 导入时，开始读取行，默认是从第二行开始
     * @return
     */
    int startRow() default 1;

    /**
     * 是否检验全部数据: true-只要某一行验证出错，就立即返回;
     * 为 false 时, ImportResult.success 总为 true
     * @return
     */
    boolean validAll() default false;

    /**
     * 是否支持在实体类上使用 hibernate-validator 注解
     * @return
     */
    boolean useHibernateValidator() default false;

    /**
     * 导入时进行行验证
     * @return
     */
    Class<? extends RowValidator> rowValidator() default DefaultRowValidator.class;

}
