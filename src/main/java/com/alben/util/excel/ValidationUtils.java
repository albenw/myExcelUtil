package com.alben.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * @author alben.wong
 * @date 2017/11/27.
 */
@Slf4j
public class ValidationUtils {

    private static Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(false)
            .buildValidatorFactory()
            .getValidator();

    public static <T> Set<ConstraintViolation<T>> validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        return constraintViolations;
    }

    public static <T> String getValidationMessage(Set<ConstraintViolation<T>> constraintViolations){
        StringBuilder sb = new StringBuilder();
        if (constraintViolations.size() > 0) {
            Iterator iterator = constraintViolations.iterator();
            while(iterator.hasNext()){
                ConstraintViolation constraintViolation = (ConstraintViolation)iterator.next();
                String message = constraintViolation.getMessage();
                if(StringUtils.isNotBlank(message)){
                    sb.append(message).append(";");
                }
            }
        }
        return sb.toString();
    }

}
