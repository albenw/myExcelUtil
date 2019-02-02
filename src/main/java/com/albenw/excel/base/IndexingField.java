package com.albenw.excel.base;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * @Author alben.wong
 * @Date 2017/10/11.
 */
@Setter
@Getter
public class IndexingField {

    private Field field;
    private Integer index;

    public IndexingField(Field field, Integer index){
        this.field = field;
        this.index = index;
    }

}
