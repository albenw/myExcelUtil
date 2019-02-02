package com.albenw.excel.base.parser;

import com.albenw.excel.base.context.ReaderContext;

import java.io.InputStream;

/**
 * @author alben.wong
 * @since 2019-01-31.
 */
public interface ReadParser {

    void execute(InputStream in, ReaderContext context) throws Exception;
}
