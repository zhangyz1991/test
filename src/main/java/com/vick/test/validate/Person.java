package com.vick.test.validate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author Vick
 * @date 2020/9/23
 */
public class Person {
    @NotNull
    public String name;
    /**
     * @Positive 不能用于String类型字段
     */
    @NotNull
    @Min(0)
    @Positive(message = "年龄必须为正数")
    public Integer age;
    /*@NotNull
    @Min(0)
    @Positive
    public String age;*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    /*public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }*/
}
