package com.alben.validator;

import com.alben.util.excel.ValidationUtils;
import com.alben.vo.TestVo;
import com.alben.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author alben.wong
 * @date 2017/11/27.
 */
@Slf4j
public class HibernateValidatorTest {

    @Test
    public void test(){
        User user = new User();
        user.setName("1231231231312");
        user.setPhoneNo("1231211111");
        user.setAge(1);
        ValidationUtils.validate(user);
    }

    @Test
    public void testNone(){
        TestVo t = new TestVo();
        t.setName("123");
        System.out.println(ValidationUtils.getValidationMessage(ValidationUtils.validate(t)));
    }

}
