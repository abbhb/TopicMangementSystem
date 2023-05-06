package com.qc.topicmanagementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.topicmanagementsystem.pojo.Topic;
import com.qc.topicmanagementsystem.pojo.TopicStudent;
import com.qc.topicmanagementsystem.pojo.dto.TopicAndUserDTO;
import com.qc.topicmanagementsystem.pojo.dto.TopicRDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {

    /**
     * 添加课题
     * @param topic
     * @return
     */
   int addTopic(Topic topic);

    /**
     * 通过课程id查询相关信息
     * @param topicId
     * @return
     */
    TopicAndUserDTO self(@Param("id")Long topicId);

    /**
     * 教师查课题表
     * @param teacherId
     * @return
     */
    List<Topic> teacher(@Param("id")Long teacherId);

    int updataTopic(Topic topic);

    int updataTopicStatus(Topic topic);

    /**
     * 删除课题
     * @param topicId
     * @return
     */
    int deleteTopic(@Param("id")Long topicId);

    List<TopicAndUserDTO> admin();


}
