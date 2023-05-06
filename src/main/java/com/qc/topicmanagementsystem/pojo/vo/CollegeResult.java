package com.qc.topicmanagementsystem.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CollegeResult implements Serializable {
    /**
     * 学院编号
     */
    private String id;

    /**
     * 学院名称
     */
    private String name;
}
