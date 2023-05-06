package com.qc.topicmanagementsystem.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class College implements Serializable {
    /**
     * 学院编号
     */
    private Long id;

    /**
     * 学院名称
     */
    private String name;
}
