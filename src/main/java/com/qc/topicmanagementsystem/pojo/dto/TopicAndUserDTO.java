package com.qc.topicmanagementsystem.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TopicAndUserDTO implements Serializable {
    /**
     * 题目id
     */
    private Long id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目状态
     * 0为未申报状态（草稿）
     * 1为申报未审核状态
     * 2为审核通过状态
     * 3为结题状态
     * 4为驳回状态
     */
    private Integer status;

    /**
     * 指导老师ID
     */
    private Long teacher;

    /**
     * 这里为teacherName 勿改与xml对应
     */
    private String teacherName;

}
