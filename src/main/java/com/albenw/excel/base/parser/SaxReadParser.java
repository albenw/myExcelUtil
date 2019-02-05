package com.albenw.excel.base.parser;

import com.albenw.excel.base.context.ReaderContext;

import java.io.InputStream;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
public abstract class SaxReadParser<T> implements ReadParser{

    @Override
    public void execute(InputStream in, ReaderContext context) throws Exception{
    }

}
