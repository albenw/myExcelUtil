package com.albenw.excel.base.parser;

import com.albenw.excel.annotation.ImportField;
import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;
import com.albenw.excel.util.AnnotationUtil;
import com.albenw.excel.util.DateUtil;
import com.albenw.excel.util.PoiUtil;
import com.albenw.excel.base.IndexingField;
import com.albenw.excel.base.context.ReaderContext;
import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.base.converter.ConverterHelper;
import com.albenw.excel.base.listener.ReadEventListener;
import com.albenw.excel.util.CollectionUtil;
import com.albenw.excel.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
@Slf4j
public class DomReadParser extends AbstractReadParser {

    @Override
    public void execute(InputStream in, ReaderContext context) throws ExcelException, IOException, IllegalAccessException,
            InstantiationException{

        List allList = new LinkedList<>();
        List batchList = new LinkedList<>();
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheetAt(context.getSheetNum());
        List<IndexingField> fields = context.getFields();
        if(CollectionUtil.isEmpty(fields)){
            throw new ExcelException(ErrorCode.LACK_OF_ANNOTATION);
        }
        Row headRow = sheet.getRow(context.getStartRow());
        ReadEventListener listener = context.getListener();
        boolean isContinue = true;
        int batchCount = 0;
        for (int i = context.getStartRow(); i < sheet.getLastRowNum() + 1; i++){
            if(!isContinue){
                break;
            }
            Row row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            Object instance = context.getClazz().newInstance();
            for (int j = 0; j < headRow.getLastCellNum(); j++){
                Cell cell = row.getCell(j);
                //截取掉多余的列
                if( j >= fields.size() || cell == null){
                    continue;
                }
                Field field = fields.get(j).getField();
                ImportField importColumn = AnnotationUtil.getImportColumn(field);
                try {
                    //进行转换
                    CellConverter cellConverter = ConverterHelper.getCellConverter(importColumn.converter());
                    if(ConverterHelper.isDefaultCellConverter(cellConverter)){
                        PoiUtil.transformToField(instance, field, cell);
                    } else {
                        String cellStringValue = PoiUtil.getCellStringValue(cell);
                        Object value = ConverterHelper.getTypeValue(cellConverter, cellStringValue);
                        Object convertedValue = cellConverter.convertIn(value);
                        PoiUtil.setFieldValue(instance, field, convertedValue);
                    }
                } catch (Exception e) {
                    log.error("字段[{}]的converter异常", field.getName());
                    if(!listener.parseException(i, j, instance, e)){
                        isContinue = false;
                    }
                }
            }
            allList.add(instance);
            batchList.add(instance);
            if(!listener.readRow(instance)){
                isContinue = false;
            }
            batchCount++;
            if(batchCount >= context.getBatchSize()){
                if(!listener.readBatch(batchList)){
                    isContinue = false;
                }
                batchCount = 0;
                batchList = new LinkedList();
            }
        }
        if(batchList.size() > 0){
            listener.readBatch(batchList);
        }
        listener.readFinished();
    }

}
