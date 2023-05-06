package com.qc.topicmanagementsystem.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResult implements Serializable {

    private String id;

    private String majorId;
    private String majorName;
    private String collegeName;

    /**
     * 此处暂时用jwt代替，后续可能接入CAS认证
     */
    private String tgc;

    private String permissionName;

    private String name;

    private Integer status;
    /**
     * 身份编码
     * 数据库上设置了外键
     */
    private Integer permission;


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
