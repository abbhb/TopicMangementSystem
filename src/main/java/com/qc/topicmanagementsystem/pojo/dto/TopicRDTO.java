package com.qc.topicmanagementsystem.pojo.dto;

import com.qc.topicmanagementsystem.pojo.TopicStudent;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TopicRDTO implements Serializable {
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

    private String teacherName;

    private List<TopicStudentDTO> topicStudents;

}
