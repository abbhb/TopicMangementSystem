package com.qc.topicmanagementsystem.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 若直接使用该注解将失效，必须搭配NeedToken使用
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionCheck {
    String value() default "4";//默认所有用户为学生权限
}
