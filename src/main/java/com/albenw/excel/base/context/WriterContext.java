package com.albenw.excel.base.context;

import com.albenw.excel.base.IndexingField;
import com.albenw.excel.base.constant.ExcelTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.OutputStream;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-02-05.
 */
@Getter @Setter
public class WriterContext<T> {

    private Class<T> targetClass;
    private ExcelTypeEnum excelType;
    private String sheetName;
    private OutputStream out;
    private List<IndexingField> fields;

    public WriterContext(Class<T> targetClass, ExcelTypeEnum excelType, String sheetName,
                         OutputStream out, List<IndexingField> fields){
        this.targetClass = targetClass;
        this.excelType = excelType;
        this.sheetName = sheetName;
        this.out = out;
        this.fields = fields;
    }
}
