package com.qc.topicmanagementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.topicmanagementsystem.pojo.ChooseTopicStudent;
import com.qc.topicmanagementsystem.pojo.TopicStudent;
import com.qc.topicmanagementsystem.pojo.dto.TopicStudentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关系表没必要修改，删了重写
 */
@Mapper
public interface ChooseTopicStudentMapper extends BaseMapper<ChooseTopicStudent> {

    /**
     * 添加选题关系
     * @param chooseTopicStudent
     * @return
     */
    int add(ChooseTopicStudent chooseTopicStudent);

    List<ChooseTopicStudent> selfList(@Param("id")Long userId);

    List<TopicStudentDTO> topicStudent(@Param("id")Long topicId);

    int deleteByTopicId(@Param("id")Long topicId);

}
