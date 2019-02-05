package com.albenw.excel.base.parser;

import com.albenw.excel.base.context.ReaderContext;
import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;

import java.io.InputStream;

/**
 * @author alben.wong
 * @since 2019-02-01.
 */
@Setter
@Getter @Slf4j
public class SaxParserWrapper implements ParserWrapper{

    @Override
    public ReadParser createReadParser(InputStream in, ReaderContext context) {
        //判断03还是07
        try {
            if (FileMagic.valueOf(in) == FileMagic.OLE2) {
                return null;
            } else if (FileMagic.valueOf(in) == FileMagic.OOXML) {
                return new XlsxSaxReadParser();
            } else {
                throw new ExcelException(ErrorCode.UNSUPPORT_FILE_TYPE_ERROR);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
