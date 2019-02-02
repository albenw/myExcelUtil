package com.albenw.excel.base.context;

import com.albenw.excel.base.listener.ReadEventListener;
import com.albenw.excel.base.constant.ParserTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author alben.wong
 * @since 2019-01-31.
 */
@Getter
@Setter
@Slf4j
public class ReaderContext<T> {
    //配置属性
    private Class<T> clazz;
    private ParserTypeEnum parserType;
    private Integer sheetNum;
    private Integer startRow;
    private ReadEventListener listener;

    //上下文属性
    private int readCount;


    public ReaderContext(Class<T> clazz, ParserTypeEnum parserType, Integer sheetNum, Integer startRow, ReadEventListener listener){
        this.clazz = clazz;
        this.parserType = parserType;
        this.sheetNum = sheetNum;
        this.startRow = startRow;
        this.listener = listener;
    }

}
