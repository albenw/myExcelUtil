package com.albenw.excel.base.parser;

import com.albenw.excel.annotation.ImportField;
import com.albenw.excel.exception.ParseException;
import com.albenw.excel.util.AnnotationUtil;
import com.albenw.excel.util.PoiUtil;
import com.albenw.excel.base.IndexingField;
import com.albenw.excel.base.context.ReaderContext;
import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.base.converter.ConverterHelper;
import com.albenw.excel.base.listener.ReadEventListener;
import com.albenw.excel.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
@Slf4j
public class DomReadParser extends AbstractReadParser {

    @Override
    public void execute(InputStream in,  ReaderContext context) throws Exception{

        List list = new LinkedList<>();
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheetAt(context.getSheetNum());
        int startRow = context.getStartRow();
        List<IndexingField> indexingFields = CollectionUtil.sortImportFieldByIndex(context.getClazz());
        if(CollectionUtil.isEmpty(indexingFields)){
            throw new ParseException("目标类的字段缺少@ImportColumn注解");
        }
        Row headRow = sheet.getRow(startRow);
        ReadEventListener listener = context.getListener();
        for (int i = startRow; i < sheet.getLastRowNum() + 1; i++){
            Row row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            Object instance = context.getClazz().newInstance();
            for (int j = 0; j < headRow.getLastCellNum(); j++){
                Cell cell = row.getCell(j);
                //截取掉多余的列
                if( j >= indexingFields.size() || cell == null){
                    continue;
                }
                String cellStringValue = PoiUtil.getCellStringValue(cell);
                Field field = indexingFields.get(j).getField();
                ImportField importColumn = AnnotationUtil.getImportColumn(field);
                try {
                    //进行转换
                    CellConverter cellConverter = ConverterHelper.getCellConverter(importColumn.converter());
                    //获取范型参数类型
                    Object value = cellConverter.convertIn(cellStringValue);
                    if(value == null){
                        PoiUtil.transformToField(instance, field, cell);
                    }else{
                        PoiUtil.setFieldValue(instance, field, value);
                    }
                } catch (Exception e) {
                    log.error("字段[{}]的converter异常", field.getName());
                    throw new ParseException(e.getMessage(), e);
                }
            }
            list.add(instance);
            listener.readRow(instance);
        }
    }

}
