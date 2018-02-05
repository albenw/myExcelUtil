package com.vip.util.excel.base;

import java.lang.reflect.Field;

/**
 * @Author alben.wong
 * @Date 2017/10/11.
 */

public class IndexingField {

    private Field field;
    private Integer index;

    public IndexingField(Field field, Integer index){
        this.field = field;
        this.index = index;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
