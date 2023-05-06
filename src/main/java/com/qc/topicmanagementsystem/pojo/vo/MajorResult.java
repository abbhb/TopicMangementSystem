package com.qc.topicmanagementsystem.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MajorResult implements Serializable {
    /**
     * 专业id
     */
    private String id;

    /**
     * 专业名称
     */
    private String name;

    /**
     * 所属学院
     */
    private String collegeId;
}
