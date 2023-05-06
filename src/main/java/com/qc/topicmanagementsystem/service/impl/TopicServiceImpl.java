package com.qc.topicmanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.topicmanagementsystem.common.CustomException;
import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.mapper.ChooseTopicStudentMapper;
import com.qc.topicmanagementsystem.mapper.TopicMapper;
import com.qc.topicmanagementsystem.pojo.ChooseTopicStudent;
import com.qc.topicmanagementsystem.pojo.Topic;
import com.qc.topicmanagementsystem.pojo.TopicStudent;
import com.qc.topicmanagementsystem.pojo.User;
import com.qc.topicmanagementsystem.pojo.dto.TopicAndUserDTO;
import com.qc.topicmanagementsystem.pojo.dto.TopicDTO;
import com.qc.topicmanagementsystem.pojo.dto.TopicRDTO;
import com.qc.topicmanagementsystem.pojo.dto.TopicStudentDTO;
import com.qc.topicmanagementsystem.pojo.vo.TopicResult;
import com.qc.topicmanagementsystem.service.ChooseTopicStudentService;
import com.qc.topicmanagementsystem.service.TopicService;
import com.qc.topicmanagementsystem.utils.SnowflakeIdWorker;
import com.qc.topicmanagementsystem.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {
    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private ChooseTopicStudentMapper chooseTopicStudentMapper;

    @Autowired
    private ChooseTopicStudentService chooseTopicStudentService;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public R<String> addTopic(TopicDTO topicDTO) {
        if (StringUtils.isEmpty(topicDTO.getTitle())){
            throw new CustomException("无题目");
        }
        if (topicDTO.getTeacher()==null){
            throw new CustomException("请选择指导教师");
        }

        long id = snowflakeIdWorker.nextId();

        Topic topic = new Topic();
        topic.setId(id);
        topic.setTeacher(topicDTO.getTeacher());
        topic.setTitle(topicDTO.getTitle());
        //申报成功默认未审核
        topic.setStatus(1);
        int i = topicMapper.addTopic(topic);
        if (i==0){
            throw new CustomException("异常");
        }
        topicDTO.setId(id);
        boolean isT = chooseTopicStudentService.addChooseTopicStudent(topicDTO);
        if (!isT){
            throw new CustomException("异常");
        }
        return R.success("申报成功");
    }

    @Override
    public R<List<TopicRDTO>> adminList() {
        List<TopicRDTO> topicRDTOS = new ArrayList<>();
        List<TopicAndUserDTO> topicAndUserDTOS = topicMapper.admin();

        for (TopicAndUserDTO topicAndUserDTO:
                topicAndUserDTOS) {
            TopicRDTO topicRDTO = new TopicRDTO();
            topicRDTO.setId(topicAndUserDTO.getId());

            topicRDTO.setTitle(topicAndUserDTO.getTitle());
            topicRDTO.setTeacherName(topicAndUserDTO.getTeacherName());
            topicRDTO.setTeacher(topicAndUserDTO.getTeacher());
            topicRDTO.setStatus(topicAndUserDTO.getStatus());
            topicRDTO.setTopicStudents(chooseTopicStudentMapper.topicStudent(topicAndUserDTO.getId()));
            topicRDTOS.add(topicRDTO);
        }

        log.info("{}",topicRDTOS);

        return R.success(topicRDTOS);
    }

    @Override
    public R<List<TopicRDTO>> selfList() {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        Long userId = currentUser.getId();
        List<TopicRDTO> topicRDTOS = new ArrayList<>();

        List<ChooseTopicStudent> chooseTopicStudents = chooseTopicStudentMapper.selfList(userId);
        for (ChooseTopicStudent c:
                chooseTopicStudents) {
            TopicRDTO topicRDTO = new TopicRDTO();
            topicRDTO.setId(c.getTopicId());
            TopicAndUserDTO topicAndUserDTOS = topicMapper.self(c.getTopicId());

            topicRDTO.setTitle(topicAndUserDTOS.getTitle());
            topicRDTO.setTeacherName(topicAndUserDTOS.getTeacherName());
            topicRDTO.setTeacher(topicAndUserDTOS.getTeacher());
            topicRDTO.setStatus(topicAndUserDTOS.getStatus());
            topicRDTO.setTopicStudents(chooseTopicStudentMapper.topicStudent(c.getTopicId()));
            topicRDTOS.add(topicRDTO);
        }

        log.info("{}",topicRDTOS);

        return R.success(topicRDTOS);
    }

    /**
     * 要求只能状态为0或者1的时候进行编辑
     * @param topicDTO
     * @return
     */
    @Override
    public R<String> updataSelfTopic(TopicDTO topicDTO) {
        if (StringUtils.isEmpty(topicDTO.getTitle())){
            throw new CustomException("无题目");
        }
        if (topicDTO.getTeacher()==null){
            throw new CustomException("请选择指导教师");
        }



        Topic byId = super.getById(topicDTO.getId());
        if (!byId.getStatus().equals(1)&&!byId.getStatus().equals(0)&&!byId.getStatus().equals(4)){
            throw new CustomException("编辑操作只能在为审核前或者驳回后");
        }
        Topic topic = new Topic();
        if(byId.getStatus().equals(4)){
            topic.setStatus(1);//驳回后再次提交审核
        }

        topic.setTeacher(topicDTO.getTeacher());
        topic.setTitle(topicDTO.getTitle());
        int i = topicMapper.updataTopic(topic);
        boolean isT = chooseTopicStudentService.updataChooseTopicStudent(topicDTO);

        return R.success("编辑成功");
    }

    @Override
    public R<String> deleteTopic(Topic topic) {

        User currentUser = ThreadLocalUtil.getCurrentUser();
        LambdaQueryWrapper<ChooseTopicStudent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChooseTopicStudent::getTopicId,topic.getId());
        lambdaQueryWrapper.eq(ChooseTopicStudent::getStudentId,currentUser.getId());
        ChooseTopicStudent one = chooseTopicStudentService.getOne(lambdaQueryWrapper);

        if (!one.getIsTeamEader().equals(1)){
            throw new CustomException("只能由组长操作");
        }


        int i = topicMapper.deleteTopic(topic.getId());
        //虽然表里做了级联删除还是删一下把
        int i1 = chooseTopicStudentMapper.deleteByTopicId(topic.getId());
        if (i!=0){
            return R.success("删除成功");
        }
        return null;
    }

    @Override
    public R<List<TopicRDTO>> selfTeacher() {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        Long userId = currentUser.getId();
        List<TopicRDTO> topicRDTOS = new ArrayList<>();

        List<Topic> topicList = topicMapper.teacher(userId);
        for (Topic t:
                topicList) {
            TopicRDTO topicRDTO = new TopicRDTO();
            topicRDTO.setId(t.getId());
            topicRDTO.setTitle(t.getTitle());
            topicRDTO.setStatus(t.getStatus());
            topicRDTO.setTeacher(t.getTeacher());
            List<TopicStudentDTO> topicStudents = chooseTopicStudentMapper.topicStudent(t.getId());
            topicRDTO.setTopicStudents(topicStudents);
            topicRDTOS.add(topicRDTO);
        }

        log.info("{}",topicRDTOS);

        return R.success(topicRDTOS);
    }

    @Override
    public R<String> teaherUpdata(TopicDTO topicDTO) {
        if (topicDTO.getId()==null||topicDTO.getStatus()==null){
            return R.error("参数异常");
        }
        Topic byId = super.getById(topicDTO.getId());
        if (byId.getStatus().equals(topicDTO.getStatus())){
            return R.error("更新失败");
        }
        Topic topic = new Topic();
        topic.setId(topicDTO.getId());
        topic.setStatus(topicDTO.getStatus());
        int i = topicMapper.updataTopicStatus(topic);
        return R.success("更新成功");

    }


}
