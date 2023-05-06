package com.qc.topicmanagementsystem.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 本系统暂时假设题目自拟，由用户自己申报教师审核
 * 选题关系表
 */
@Data
public class ChooseTopicStudent extends TopicStudent implements Serializable {
    /**
     * 项编号
     */
    private Long id;


    /**
     * 毕设题目编号
     */
    private Long topicId;


}
