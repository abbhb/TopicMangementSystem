package com.qc.topicmanagementsystem.controller;

import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.common.annotation.NeedLogin;
import com.qc.topicmanagementsystem.common.annotation.PermissionCheck;
import com.qc.topicmanagementsystem.pojo.Major;
import com.qc.topicmanagementsystem.pojo.Topic;
import com.qc.topicmanagementsystem.pojo.dto.TopicDTO;
import com.qc.topicmanagementsystem.pojo.dto.TopicRDTO;
import com.qc.topicmanagementsystem.pojo.vo.TopicResult;
import com.qc.topicmanagementsystem.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @NeedLogin
    @PostMapping("/add")
    public R<String> addTopic(@RequestBody TopicDTO topicDTO) {
        if (topicDTO==null){
            return R.error("数据异常");
        }
        return topicService.addTopic(topicDTO);
    }

    @NeedLogin
    @GetMapping("/selfList")
    public R<List<TopicRDTO>> selfList() {
        return topicService.selfList();
    }

    @NeedLogin
    @PermissionCheck("2")
    @GetMapping("/admin")
    public R<List<TopicRDTO>> adminList() {
        return topicService.adminList();
    }

    @NeedLogin
    @PermissionCheck("3")
    @GetMapping("/selfTeacher")
    public R<List<TopicRDTO>> selfTeacher() {
        return topicService.selfTeacher();
    }
    @NeedLogin
    @PutMapping("/updataSelf")
    public R<String> updataSelf(@RequestBody TopicDTO topicDTO){
        if (topicDTO==null){
            return R.error("数据异常");
        }
        return topicService.updataSelfTopic(topicDTO);
    }

    @NeedLogin
    @DeleteMapping("/deleteSelf")
    public R<String> deleteTopic(@RequestBody Topic topic) {
        if (topic==null){
            return R.error("数据异常");
        }
        return topicService.deleteTopic(topic);
    }
    @NeedLogin
    @PermissionCheck("3")
    @PutMapping("/teaherUpdata")
    public R<String> teaherUpdata(@RequestBody TopicDTO topicDTO) {
        if (topicDTO==null){
            return R.error("数据异常");
        }
        return topicService.teaherUpdata(topicDTO);
    }



}
