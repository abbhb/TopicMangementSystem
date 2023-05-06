package com.qc.topicmanagementsystem.pojo.dto;

import com.qc.topicmanagementsystem.pojo.Topic;
import com.qc.topicmanagementsystem.pojo.TopicStudent;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * dto类
 * 此处继承的是public的方法
 */
@Data
public class TopicDTO extends Topic implements Serializable {

    private List<TopicStudent> topicStudents;



}
