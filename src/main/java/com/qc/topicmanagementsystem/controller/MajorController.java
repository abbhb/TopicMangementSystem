package com.qc.topicmanagementsystem.controller;

import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.common.annotation.NeedLogin;
import com.qc.topicmanagementsystem.common.annotation.PermissionCheck;
import com.qc.topicmanagementsystem.pojo.Major;
import com.qc.topicmanagementsystem.pojo.vo.MajorResult;
import com.qc.topicmanagementsystem.service.MajorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/major")
public class MajorController {
    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    @NeedLogin
    @PermissionCheck("2")
    @PostMapping("/add")
    public R<String> addMajor(@RequestBody Major major) {
        if (major==null){
            return R.error("数据异常");
        }
        return majorService.addMajor(major);
    }
    @NeedLogin
    @GetMapping ("/list")
    public R<List<MajorResult>> listMajor() {
        return majorService.listMajor();
    }

    @NeedLogin
    @PermissionCheck("2")
    @DeleteMapping("/delete")
    public R<String> deleteMajor(@RequestBody Major major) {
        if (major==null){
            return R.error("数据异常");
        }
        return majorService.deleteMajor(major);
    }

    @NeedLogin
    @PermissionCheck("2")
    @PutMapping("/updata")
    public R<String> updataMajor(@RequestBody Major major) {
        if (major==null){
            return R.error("数据异常");
        }
        return majorService.updataMajor(major);
    }

}
