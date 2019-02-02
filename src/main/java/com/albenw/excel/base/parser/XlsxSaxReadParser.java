package com.albenw.excel.base.parser;

import com.albenw.excel.base.context.ReaderContext;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.io.InputStream;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
public class XlsxSaxReadParser extends SaxReadParser implements XSSFSheetXMLHandler.SheetContentsHandler {

    @Override
    public void execute(InputStream in, ReaderContext context) {

    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {

    }

    @Override
    public void endSheet() {

    }

    @Override
    public void startRow(int i) {

    }

    @Override
    public void endRow(int i) {

    }

    @Override
    public void cell(String s, String s1, XSSFComment xssfComment) {

    }

}
