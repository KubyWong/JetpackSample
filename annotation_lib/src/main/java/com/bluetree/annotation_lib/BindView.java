package com.bluetree.annotation_lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)//声明注解作用域
@Retention(RetentionPolicy.SOURCE)//声明注解声明周期
public @interface BindView {
    int value();
}
