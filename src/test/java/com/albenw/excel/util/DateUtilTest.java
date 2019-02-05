package com.albenw.excel.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;

/**
 * @author alben.wong
 * @since 2019-01-31.
 */
@Slf4j
public class DateUtilTest {

    @Test
    public void formatTest(){
        String format = DateUtil.format(new Date(), DateUtil.DEFAULT_DATE_FORMAT);
        log.info("format={}", format);
    }

    @Test
    public void TryparseTest() throws Exception{
        Date parse = DateUtil.tryParse("2019-01-31 11:17:54");
        log.info("parse={}", parse);
        assert parse != null;
    }
}
