package com.alben.excel;

import com.alben.util.excel.ExcelUtil;
import com.alben.util.excel.POIUtil;
import com.alben.util.excel.base.ImportResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import com.alben.vo.User;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author alben.wong
 * @date 2017/11/16.
 */
@Slf4j
public class ExcelTest {

    private static String FILE_1 = "";
    private static String FILE_2 = "";
    private static String EXCEL_TEMPLATE = "";


    @Test
    public void fileTest() throws Exception{
        XSSFWorkbook toBook = new XSSFWorkbook();
        Sheet sheet = toBook.createSheet("sheet1");
        InputStream in = new FileInputStream(FILE_1);
        Workbook fromBook = WorkbookFactory.create(in);
        POIUtil.copySheet(sheet.getWorkbook(), fromBook.getSheetAt(0), toBook.getSheetAt(0), false);
        FileOutputStream fos = new FileOutputStream(FILE_2);
        ExcelUtil.getInstance().writeToStream(toBook, new BufferedOutputStream(fos));
    }

    @Test
    public void exportTest() throws Exception{
        Long start = System.currentTimeMillis();
        List users = new LinkedList<>();
        for(int i=0; i < 10; i++){
            User user = new User();
            user.setName("name_" + i);
            user.setPhoneNo("phone_"+i);
            user.setAddress("address_"+i);
            user.setAge(1000);
            user.setAsset(100.123);
            user.setBirthday(new Date());
            user.setEmail("123@mail.com");
            users.add(user);
        }
        OutputStream os = new BufferedOutputStream(new FileOutputStream(FILE_1));
        ExcelUtil.getInstance().exportTo(users, os, User.class);
        log.info("test run end === " + (System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void file2() throws Exception{
        InputStream is = ExcelTest.class.getClassLoader().getResourceAsStream(EXCEL_TEMPLATE);
        System.out.println(is.available());
    }

    @Test
    public void importTest() throws Exception{
        Long start = System.currentTimeMillis();
        InputStream is = new FileInputStream(FILE_1);
        ImportResult importResult = ExcelUtil.getInstance().importFrom(User.class, is);
        log.info("test run end === " + (System.currentTimeMillis() - start) / 1000);
        assert importResult != null;
    }

    @Test
    public void getGenericTypeTest(){
        LinkedList<User> list = new LinkedList<>();
        User user = new User();
        user.setName("name");
        user.setPhoneNo("phone");
        user.setAddress("address");
        user.setAge(1);
        user.setAsset(100.56D);
        user.setBirthday(new Date());
        list.add(user);

        System.out.println(list.getClass());
        System.out.println(list.getClass().getGenericSuperclass());
        Type mySuperClass = list.getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
        System.out.println(type.getTypeName());
    }

    @Test
    public void test1(){
        List list = new ArrayList<>();
        System.out.println(list.getClass());
        System.out.println(list.getClass().getGenericSuperclass());
        Type mySuperClass = list.getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
        System.out.println(type.getTypeName());
    }

}
