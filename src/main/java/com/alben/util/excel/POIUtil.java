package com.alben.util.excel;

import org.apache.poi.ss.usermodel.*;
import java.util.Iterator;

/**
 * @author alben.wong
 * @date 2017/11/16.
 */
public class POIUtil {
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
     *
     * @param srcCell
     * @param distCell
     * @param copyValueFlag
     *            true则连同cell的内容一起复制
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
        CellType srcCellType = srcCell.getCellTypeEnum();
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

}
