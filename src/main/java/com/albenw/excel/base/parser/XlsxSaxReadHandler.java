package com.albenw.excel.base.parser;

import com.albenw.excel.annotation.ImportField;
import com.albenw.excel.base.IndexingField;
import com.albenw.excel.base.context.ReaderContext;
import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.base.converter.ConverterHelper;
import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;
import com.albenw.excel.util.AnnotationUtil;
import com.albenw.excel.util.CollectionUtil;
import com.albenw.excel.util.ReflectionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-02-04.
 */
@Getter @Setter
@Slf4j
public class XlsxSaxReadHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

    private boolean firstCellOfRow;
    private int currentRow = -1;
    private int currentCol = -1;
    private List<String> cellString;
    private ReaderContext context;
    private int batchCount = 0;
    private List<Object> datas = new ArrayList<>();
    private boolean notInterrupt = true;

    public XlsxSaxReadHandler(ReaderContext context){
        this.context = context;
    }

    @Override
    public void startRow(int rowNum) {
        if(!notInterrupt){
            return;
        }
        currentRow = rowNum;
        // Prepare for this row
        firstCellOfRow = true;
        currentCol = -1;
        cellString = initRowList(context.getFields().size());
    }

    @Override
    public void endRow(int rowNum) {
        if(!notInterrupt || currentRow < context.getStartRow()){
            return;
        }
        Object instance = null;
        try{
            instance = transformFromCellString(context.getClazz(), context.getFields(), cellString);
            datas.add(instance);
            this.batchCount++;
            this.notInterrupt = context.getListener().readRow(instance);
            if(this.batchCount >= context.getBatchSize()){
                this.notInterrupt = context.getListener().readBatch(datas);
                datas = new LinkedList<>();
                this.batchCount = 0;
            }
        }catch (ExcelException e){
            this.notInterrupt = context.getListener().parseException(currentRow + 1, currentCol + 1, instance, e);
        }
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        if(!notInterrupt || currentRow < context.getStartRow()){
            return;
        }
        if (firstCellOfRow) {
            firstCellOfRow = false;
        }

        // gracefully handle missing CellRef here in a similar way as XSSFCell does
        if(cellReference == null) {
            cellReference = new CellAddress(currentRow, currentCol).formatAsString();
        }

        // Did we miss any cells?
        int thisCol = (new CellReference(cellReference)).getCol();
        int missedCols = thisCol - currentCol - 1;
        for (int i = 0; i < missedCols; i++) {

        }
        currentCol = thisCol;
        cellString.set(currentCol, formattedValue);
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
        log.info("headerFooter");
    }

    @Override
    public void endSheet() {
        if(CollectionUtil.isNotEmpty(datas)){
            context.getListener().readBatch(datas);
            datas = null;
        }
    }

    private Object transformFromCellString(Class clazz, List<IndexingField> fields, List<String> cellString) throws ExcelException {
        try{
            Object instance = clazz.newInstance();
            for(int i = 0; i < fields.size(); i ++) {
                Field field = fields.get(i).getField();
                String formattedValue = cellString.get(i);
                ImportField importColumn = AnnotationUtil.getImportField(field);
                CellConverter cellConverter = ConverterHelper.getCellConverter(importColumn.converter());
                Object value = null;
                if(ConverterHelper.isDefaultCellConverter(cellConverter)){
                    value = ReflectionUtil.convertToTypeValueFromString(field.getType(), formattedValue);
                    ReflectionUtil.setFieldValue(field, instance, value);
                } else {
                    value = ConverterHelper.getConvertInTypeValue(cellConverter, formattedValue);
                    Object convertedValue = cellConverter.convertIn(value);
                    ReflectionUtil.setFieldValue(field, instance, convertedValue);
                }
            }
            return instance;
        }catch (InstantiationException| IllegalAccessException| ParseException e){
            throw new ExcelException(ErrorCode.FIELD_PARSE_ERROR);
        }
    }

    private List<String> initRowList(int size) {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < size; i++){
            list.add("");
        }
        return list;
    }

}
