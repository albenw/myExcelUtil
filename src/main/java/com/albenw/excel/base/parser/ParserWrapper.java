package com.albenw.excel.base.parser;

import com.albenw.excel.base.context.ReaderContext;

import java.io.InputStream;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
public interface ParserWrapper {

    ReadParser createReadParser(InputStream in, ReaderContext context);

}
