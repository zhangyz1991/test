package com.vick.test.validate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author Vick
 * @date 2020/9/23
 */
public class ValidateTest {

    public static void main(String[] args) {
        Person person = new Person();
        person.setAge(-1);
        // 1、使用【默认配置】得到一个校验工厂  这个配置可以来自于provider、SPI提供
        // 2、得到一个校验器
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        // 3、校验Java Bean（解析注解） 返回校验结果
        Validator validator = validatorFactory.getValidator();
        // 输出校验结果
        Set<ConstraintViolation<Person>> result = validator.validate(person);
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
    }
}
