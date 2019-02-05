package com.albenw.excel.base.parser;

import com.albenw.excel.base.context.ReaderContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
@Setter
@Getter
public class XlsxSaxReadParser extends SaxReadParser {

    public XlsxSaxReadParser(){
    }

    @Override
    public void execute(InputStream in, ReaderContext context) throws IOException, OpenXML4JException, SAXException {
        OPCPackage xlsxPackage = OPCPackage.open(in);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage, false);
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int sheetCount = 0;
        while (iter.hasNext()) {
            try (InputStream stream = iter.next()) {
                if(sheetCount == context.getSheetNum()){
                    processSheet(styles, strings, new XlsxSaxReadHandler(context), stream);
                }
                sheetCount++;
            }
        }
    }

    public void processSheet(Styles styles, SharedStrings strings, XSSFSheetXMLHandler.SheetContentsHandler sheetHandler,
                             InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch(ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

}
