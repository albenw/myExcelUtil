package com.albenw.excel.parser;

import com.albenw.excel.base.context.ReaderContext;
import com.albenw.excel.base.listener.ReadEventListener;
import com.albenw.excel.base.parser.XlsxSaxReadParser;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-02-04.
 */
public class XlsxSaxReadParserTest {

    InputStream in07 = Thread.currentThread().getContextClassLoader().getResourceAsStream("excel2007.xlsx");

    @Test
    public void executeTest() throws Exception{

    }
}
