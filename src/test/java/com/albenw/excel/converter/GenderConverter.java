package com.albenw.excel.converter;

import com.albenw.excel.base.converter.AbstractCellConverter;
import com.albenw.excel.exception.ExcelException;

/**
 * @author alben.wong
 * @date 2018/2/2.
 */
public class GenderConverter extends AbstractCellConverter<String, Integer> {

    @Override
    public Integer convertIn(String value) throws ExcelException {
        if("男".equals(value)){
            return 1;
        }else if("女".equals(value)){
            return 2;
        }
        return 0;
    }

    @Override
    public String convertOut(Integer value) throws ExcelException {
        if(value == 1){
            return "男";
        } else if(value == 2){
            return "女";
        } else{
            return "";
        }
    }

}
