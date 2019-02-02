package com.albenw.excel.base.reader;

import com.albenw.excel.base.constant.ParserTypeEnum;
import com.albenw.excel.base.context.ReaderContext;
import com.albenw.excel.base.listener.ReadEventListener;
import com.albenw.excel.base.parser.DomParserWrapper;
import com.albenw.excel.base.parser.ParserWrapper;
import com.albenw.excel.base.parser.ReadParser;
import com.albenw.excel.base.parser.SaxParserWrapper;
import com.albenw.excel.exception.ExcelException;
import com.albenw.excel.exception.ParseException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author alben.wong
 * @since 2019-01-31.
 */
@Getter
@Setter
@Slf4j
public class ExcelReader {

    private ParserWrapper parserWrapper;
    private ReaderContext context;

    public void read(InputStream in) throws Exception{
        ReadParser readParser = parserWrapper.createReadParser(in, context);
        readParser.execute(in, context);
    }

    private <T> ExcelReader(Class<T> targetClass, ParserTypeEnum parserType, Integer sheetNum, Integer startRow, ReadEventListener listener){
        this.context = new ReaderContext(targetClass, parserType, sheetNum, startRow, listener);
        switch (parserType){
            case DOM:
                parserWrapper = new DomParserWrapper();
                break;
            case SAX:
                parserWrapper = new SaxParserWrapper();
                break;
            default:
                parserWrapper = null;
        }
    }

    public static <T> Builder<T> newBuilder() {
        return new Builder();
    }

    @Setter
    @Getter
    public static class Builder<T>{
        private Class<T> targetClass;
        private Integer sheetNum;
        private ParserTypeEnum parserType;
        private ReadEventListener listener;
        private Integer batchSize;
        private Integer startRow;

        public Builder<T> targetClass(Class<T> clz) {
            this.setTargetClass(clz);
            return this;
        }

        public Builder<T> sheetNum(int sheetNum){
            this.setSheetNum(sheetNum);
            return this;
        }

        public Builder<T> parserType(ParserTypeEnum parserType){
            this.parserType = parserType;
            return this;
        }

        public Builder<T> startRow(Integer startRow){
            this.startRow = startRow;
            return this;
        }

        public Builder<T> addListener(ReadEventListener<T> listener){
            this.listener = listener;
            return this;
        }

        public ExcelReader build() throws ExcelException {
            checkBuildParameter();
            return new ExcelReader(this.targetClass, this.parserType, this.sheetNum, this.startRow, this.listener);
        }

        private void checkBuildParameter() throws ExcelException {
            //默认第一个sheet
            if(this.sheetNum == null){
                this.sheetNum = 0;
            }
            //默认第二行开始
            if(this.startRow == null){
                this.startRow = 1;
            }
            if(targetClass == null){
                throw new ParseException("缺少targetClass参数");
            }
            if(parserType == null){
                throw new ParseException("缺少parserType参数");
            }
            if(ParserTypeEnum.SAX.equals(parserType) && this.listener == null){
                throw new ParseException("SAX模式下必须listener");
            }
        }
    }

}
