package com.qc.topicmanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.topicmanagementsystem.pojo.ChooseTopicStudent;
import com.qc.topicmanagementsystem.pojo.dto.TopicDTO;

public interface ChooseTopicStudentService extends IService<ChooseTopicStudent> {
    boolean addChooseTopicStudent(TopicDTO topicDTO);

    boolean updataChooseTopicStudent(TopicDTO topicDTO);
}
