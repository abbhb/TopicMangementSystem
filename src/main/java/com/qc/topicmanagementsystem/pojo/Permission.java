package com.qc.topicmanagementsystem.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 身份编码
 */
@Data
public class Permission implements Serializable {
    /**
     * 身份编码id
     */
    private Integer id;

    /**
     * 身份编码名称
     */
    private String name;

    /**
     * 权重
     * 权重大的能管理小的
     */
    private Integer weight;
}
