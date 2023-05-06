package com.qc.topicmanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.topicmanagementsystem.common.CustomException;
import com.qc.topicmanagementsystem.mapper.ChooseTopicStudentMapper;
import com.qc.topicmanagementsystem.pojo.ChooseTopicStudent;
import com.qc.topicmanagementsystem.pojo.TopicStudent;
import com.qc.topicmanagementsystem.pojo.User;
import com.qc.topicmanagementsystem.pojo.dto.TopicDTO;
import com.qc.topicmanagementsystem.service.ChooseTopicStudentService;
import com.qc.topicmanagementsystem.utils.SnowflakeIdWorker;
import com.qc.topicmanagementsystem.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 本项目删除课题时不用手动删除关系表，做了外键约束级联删除
 */

@Transactional
@Service
@Slf4j
public class ChooseTopicStudentServiceImpl extends ServiceImpl<ChooseTopicStudentMapper, ChooseTopicStudent> implements ChooseTopicStudentService {

    @Autowired
    private ChooseTopicStudentMapper chooseTopicStudentMapper;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public boolean addChooseTopicStudent(TopicDTO topicDTO) {
        if (topicDTO.getTopicStudents()==null){
            throw new CustomException("无对象");
        }
        if (topicDTO.getId()==null){
            throw new CustomException("无对象");
        }
        int flag = 0;
        for (TopicStudent topicStudent:
                topicDTO.getTopicStudents()) {
            if (topicStudent.getStudentId()==null){
                throw new CustomException("异常");
            }
            if (topicStudent.getIsTeamEader()==1&&flag==1){
                throw new CustomException("禁止一组多组长");
            }
            if (topicStudent.getIsTeamEader()==1&&flag!=1){
                flag = 1;
            }
        }
        if (flag==0){
            throw new CustomException("无组长");
        }
        for (TopicStudent topicStudent:
                topicDTO.getTopicStudents()) {
            ChooseTopicStudent chooseTopicStudent = new ChooseTopicStudent();
            chooseTopicStudent.setTopicId(topicDTO.getId());
            chooseTopicStudent.setStudentId(topicStudent.getStudentId());
            if (topicStudent.getIsTeamEader()==1){
                chooseTopicStudent.setIsTeamEader(1);
            }else {
                chooseTopicStudent.setIsTeamEader(0);
            }
            chooseTopicStudent.setId(snowflakeIdWorker.nextId());
            int i = chooseTopicStudentMapper.add(chooseTopicStudent);
            if (i==0){
                throw new CustomException("选题关系出错");
            }
        }
        return true;
    }

    @Override
    public boolean updataChooseTopicStudent(TopicDTO topicDTO) {
        if (topicDTO.getTopicStudents()==null){
            throw new CustomException("无对象");
        }
        if (topicDTO.getId()==null){
            throw new CustomException("无对象");
        }

        User currentUser = ThreadLocalUtil.getCurrentUser();
        LambdaQueryWrapper<ChooseTopicStudent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChooseTopicStudent::getTopicId,topicDTO.getId());
        lambdaQueryWrapper.eq(ChooseTopicStudent::getStudentId,currentUser.getId());
        ChooseTopicStudent one = super.getOne(lambdaQueryWrapper);

        if (!one.getIsTeamEader().equals(1)){
            throw new CustomException("只能由组长操作");
        }

        int flag = 0;
        for (TopicStudent topicStudent:
                topicDTO.getTopicStudents()) {
            if (topicStudent.getStudentId()==null){
                throw new CustomException("异常");
            }
            if (topicStudent.getIsTeamEader()==1&&flag==1){
                throw new CustomException("禁止一组多组长");
            }
            if (topicStudent.getIsTeamEader()==1&&flag!=1){
                flag = 1;
            }
        }
        if (flag==0){
            throw new CustomException("无组长");
        }

        //先全删掉
        int i1 = chooseTopicStudentMapper.deleteByTopicId(topicDTO.getId());

        //新建
        for (TopicStudent topicStudent:
                topicDTO.getTopicStudents()) {
            ChooseTopicStudent chooseTopicStudent = new ChooseTopicStudent();
            chooseTopicStudent.setTopicId(topicDTO.getId());
            chooseTopicStudent.setStudentId(topicStudent.getStudentId());
            if (topicStudent.getIsTeamEader()==1){
                chooseTopicStudent.setIsTeamEader(1);
            }else {
                chooseTopicStudent.setIsTeamEader(0);
            }
            chooseTopicStudent.setId(snowflakeIdWorker.nextId());
            int i = chooseTopicStudentMapper.add(chooseTopicStudent);
            if (i==0){
                throw new CustomException("选题关系出错");
            }
        }
        return true;
    }
}
