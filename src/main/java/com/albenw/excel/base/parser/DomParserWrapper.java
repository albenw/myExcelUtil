package com.albenw.excel.base.parser;

import com.albenw.excel.base.context.ReaderContext;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
@Setter
@Getter
public class DomParserWrapper<T> implements ParserWrapper{

    @Override
    public ReadParser createReadParser(InputStream in, ReaderContext context) {
        return new DomReadParser();
    }

}
