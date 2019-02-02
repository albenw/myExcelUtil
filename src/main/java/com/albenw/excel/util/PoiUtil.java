package com.albenw.excel.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;

/**
 * @author alben.wong
 * @date 2017/11/16.
 */
@Slf4j
public class PoiUtil {
    /**
     * 复制一个单元格样式到目的单元格样式
     * @param fromStyle
     * @param toStyle
     */
    public static void copyCellStyle(CellStyle fromStyle,
                                     CellStyle toStyle) {
        toStyle.setAlignment(fromStyle.getAlignmentEnum());
        //边框和边框颜色
        toStyle.setBorderBottom(fromStyle.getBorderBottomEnum());
        toStyle.setBorderLeft(fromStyle.getBorderLeftEnum());
        toStyle.setBorderRight(fromStyle.getBorderRightEnum());
        toStyle.setBorderTop(fromStyle.getBorderTopEnum());
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

        //背景和前景
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

        toStyle.setDataFormat(fromStyle.getDataFormat());
        toStyle.setFillPattern(fromStyle.getFillPatternEnum());
        toStyle.setHidden(fromStyle.getHidden());
        //首行缩进
        toStyle.setIndention(fromStyle.getIndention());
        toStyle.setLocked(fromStyle.getLocked());
        //旋转
        toStyle.setRotation(fromStyle.getRotation());
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignmentEnum());
        toStyle.setWrapText(fromStyle.getWrapText());

    }

    /**
     * Sheet复制
     * @param fromSheet
     * @param toSheet
     * @param copyValueFlag
     */
    public static void copySheet(Workbook wb, Sheet fromSheet, Sheet toSheet, boolean copyValueFlag) {
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
            Row tmpRow = (Row) rowIt.next();
            Row newRow = toSheet.createRow(tmpRow.getRowNum());
            //行复制
            copyRow(wb,tmpRow,newRow,copyValueFlag);
        }
    }

    /**
     * 行复制功能
     * @param fromRow
     * @param toRow
     */
    public static void copyRow(Workbook wb,Row fromRow,Row toRow,boolean copyValueFlag){
        for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
            Cell tmpCell = (Cell) cellIt.next();
            Cell newCell = toRow.createCell(tmpCell.getCellTypeEnum().ordinal());
            copyCell(wb,tmpCell, newCell, copyValueFlag);
        }
    }

    /**
     * 复制单元格
     * @param srcCell
     * @param distCell
     * @param copyValueFlag
     * true则连同cell的内容一起复制
     */
    public static void copyCell(Workbook wb, Cell srcCell, Cell distCell,
                                boolean copyValueFlag) {
        CellStyle newstyle = wb.createCellStyle();
        copyCellStyle(srcCell.getCellStyle(), newstyle);
//        distCell.setEncoding(srcCell.getEncoding());
        //样式
        distCell.setCellStyle(newstyle);
        //评论
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        // 不同数据类型处理
        CellType srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        if (copyValueFlag) {
            if (srcCellType == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(srcCell)) {
                    distCell.setCellValue(srcCell.getDateCellValue());
                } else {
                    distCell.setCellValue(srcCell.getNumericCellValue());
                }
            } else if (srcCellType == CellType.STRING) {
                distCell.setCellValue(srcCell.getRichStringCellValue());
            } else if (srcCellType == CellType.BLANK) {
                // nothing21
            } else if (srcCellType == CellType.BOOLEAN) {
                distCell.setCellValue(srcCell.getBooleanCellValue());
            } else if (srcCellType == CellType.ERROR) {
                distCell.setCellErrorValue(srcCell.getErrorCellValue());
            } else if (srcCellType == CellType.FORMULA) {
                distCell.setCellFormula(srcCell.getCellFormula());
            } else { // nothing29
            }
        }
    }

    public static void transformToField(Object instance, Field field, Cell cell) throws IllegalAccessException {
        field.setAccessible(true);
        String type = field.getGenericType().getTypeName();
        cell.setCellType(CellType.STRING);
        String stringCellValue = StringUtil.trimToEmpty(cell.getStringCellValue());
        if("java.lang.String".equals(type)){
            field.set(instance, stringCellValue);
        }
        else if("int".equals(type) || "java.lang.Integer".equals(type)){
            if(StringUtil.isNotBlank(stringCellValue)){
                Integer intValue = Integer.valueOf(stringCellValue);
                field.set(instance, intValue);
            }
        }
        else if("long".equals(type)||"java.lang.Long".equals(type)){
            if(StringUtil.isNotBlank(stringCellValue)){
                Long longValue = Long.valueOf(stringCellValue);
                field.set(instance, longValue);
            }
        }
        else if("java.util.Date".equals(type)){
            Date date = getDate(cell);
            field.set(instance, date);
        }else if("java.lang.Double".equals(type) || "double".equals(type)){
            if(StringUtil.isNotBlank(stringCellValue)){
                Double doubleValue = Double.valueOf(stringCellValue);
                field.set(instance, doubleValue);
            }
        }else if("java.lang.Float".equals(type) || "float".equals(type)){
            if(StringUtil.isNotBlank(stringCellValue)){
                Float floatValue = Float.valueOf(stringCellValue);
                field.set(instance, floatValue);
            }
        }else{
            log.error("不支持的数据类型, type->{}", type);
        }
    }

    public static void setFieldValue(Object instance, Field field, Object value) throws IllegalAccessException{
        field.setAccessible(true);
        field.set(instance, value);
    }

    public static String getCellStringValue(Cell cell){
        cell.setCellType(CellType.STRING);
        String stringCellValue = cell.getStringCellValue();
        return StringUtil.trimToEmpty(stringCellValue);
    }

    private static Date getDate(Cell cell){
        CellType cellType = cell.getCellType();
        try{
            if(CellType.STRING.equals(cellType) || CellType.FORMULA.equals(cellType)){
                String value = cell.getStringCellValue();
                return com.albenw.excel.util.DateUtil.parse(value, com.albenw.excel.util.DateUtil.DEFAULT_DATE_FORMAT);
            }else{
                log.error("解析日期异常");
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
