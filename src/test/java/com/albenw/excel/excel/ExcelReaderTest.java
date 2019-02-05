package com.albenw.excel.excel;

import com.albenw.excel.base.constant.ParserTypeEnum;
import com.albenw.excel.base.listener.ReadEventListener;
import com.albenw.excel.vo.User;
import com.albenw.excel.base.reader.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-02-02.
 */
@Slf4j
public class ExcelReaderTest {

    InputStream in07 = Thread.currentThread().getContextClassLoader().getResourceAsStream("excel2007.xlsx");

    @Test
    public void readDomTest() throws Exception{
        ExcelReader.<User>newBuilder().targetClass(User.class).parserType(ParserTypeEnum.DOM).addListener(new ReadEventListener<User>() {
            @Override
            public boolean readRow(User user) {
                log.info("readRow={}", user.toString());
                return true;
            }

            @Override
            public boolean readBatch(List<User> rowDatas) {
                log.info("readBatch");
                return true;
            }

            @Override
            public void readFinished() {
                log.info("readFinished");
            }

            @Override
            public boolean parseException(int rowNum, int colNum, User user, Exception e) {
                log.info("user={}", user.toString());
                log.info(e.getMessage());
                return true;
            }
        }).build().read(in07);
    }

    @Test
    public void read07SaxTest() throws Exception {
        ExcelReader.<User>newBuilder().targetClass(User.class).parserType(ParserTypeEnum.SAX).addListener(new ReadEventListener<User>() {
            @Override
            public boolean readRow(User user) {
                log.info("readRow={}", user.toString());
                return true;
            }

            @Override
            public boolean readBatch(List<User> rowDatas) {
                log.info("readBatch");
                return true;
            }

            @Override
            public void readFinished() {
                log.info("readFinished");
            }

            @Override
            public boolean parseException(int rowNum, int colNum, User user, Exception e) {
                log.info("user={}", user.toString());
                log.info(e.getMessage());
                return true;
            }
        }).build().read(in07);
    }

    @Test
    public void read03SaxTest(){

    }

}
