package com.albenw.excel.excel;

import com.albenw.excel.base.constant.ExcelTypeEnum;
import com.albenw.excel.base.writer.ExcelWriter;
import com.albenw.excel.vo.User;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.LinkedList;

/**
 * @author alben.wong
 * @since 2019-02-05.
 */
public class ExcelWriterTest {

    @Test
    public void writeTest() throws Exception{

        FileOutputStream outputStream = new FileOutputStream("/Users/alben.wong/Documents/writeTest.xlsx");

        ExcelWriter excelWriter = ExcelWriter
                .<User>newBuilder()
                .sheetName("123")
                .fileName("123")
                .targetClass(User.class)
                .excelType(ExcelTypeEnum.XLSX)
                .outputStream(outputStream)
                .build();

        LinkedList<User> list = new LinkedList<>();
        User user = new User();
        user.setName("name");
        user.setPhoneNo("phone");
        user.setAddress("address");
        user.setEmail("123@qq.com");
        user.setAge(1);
        user.setGender(1);
        user.setAsset(100.56D);
        user.setBirthday(new Date());
        list.add(user);
        excelWriter.write(list);
    }
}
