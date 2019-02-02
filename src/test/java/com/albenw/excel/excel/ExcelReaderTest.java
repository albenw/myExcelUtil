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
    public void readDomTest(){

    }

    @Test
    public void read03DomTest(){

    }

    @Test
    public void read07SaxTest() throws Exception {
        ExcelReader.<User>newBuilder().targetClass(User.class).parserType(ParserTypeEnum.DOM).addListener(new ReadEventListener<User>() {
            @Override
            public void readRow(User rowData) {
                log.info("readRow={}", rowData);
            }

            @Override
            public void readBatch(List<User> rowDatas) {

            }

            @Override
            public void readFinished() {

            }
        }).build().read(in07);
    }

    @Test
    public void read03SaxTest(){

    }

}
