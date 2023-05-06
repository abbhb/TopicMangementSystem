package com.qc.topicmanagementsystem.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 专业
 */
@Data
public class Major implements Serializable {
    /**
     * 专业id
     */
    private Long id;

    /**
     * 专业名称
     */
    private String name;

    /**
     * 所属学院
     */
    private Long collegeId;

}
