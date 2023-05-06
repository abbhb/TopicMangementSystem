package com.qc.topicmanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.pojo.Topic;
import com.qc.topicmanagementsystem.pojo.dto.TopicDTO;
import com.qc.topicmanagementsystem.pojo.dto.TopicRDTO;
import com.qc.topicmanagementsystem.pojo.vo.TopicResult;

import java.util.List;


public interface TopicService extends IService<Topic> {
    R<String> addTopic(TopicDTO topicDTO);

    R<List<TopicRDTO>> selfList();

    R<String> updataSelfTopic(TopicDTO topicDTO);

    R<String> deleteTopic(Topic topic);

    R<List<TopicRDTO>> selfTeacher();

    R<String> teaherUpdata(TopicDTO topicDTO);

    R<List<TopicRDTO>> adminList();
}
