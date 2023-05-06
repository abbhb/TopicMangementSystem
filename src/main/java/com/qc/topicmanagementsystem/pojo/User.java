package com.qc.topicmanagementsystem.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 */
@Data
public class User implements Serializable {
    /**
     * 用户id 学号
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String name;

    private Integer status;
    /**
     * 身份编码
     * 数据库上设置了外键
     */
    private Integer permission;
    /**
     * 专业id
     */
    private Long majorId;

    /**
     * 用户名（登录时使用）
     */
    private String username;
    /**
     * 密码，时间有限不加盐不加密了
     */
    private String password;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别
     */
    private String sex;
    /**
     * 手机号
     */
    private String phone;
}
