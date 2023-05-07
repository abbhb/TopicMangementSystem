package com.qc.topicmanagementsystem.controller;

import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.common.annotation.NeedLogin;
import com.qc.topicmanagementsystem.common.annotation.PermissionCheck;
import com.qc.topicmanagementsystem.pojo.College;
import com.qc.topicmanagementsystem.pojo.vo.CollegeResult;
import com.qc.topicmanagementsystem.service.CollegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/college")
public class CollegeController {
    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @NeedLogin
    @PermissionCheck("2")
    @PostMapping("/add")
    public R<String> addCollege(@RequestBody College college) {
        if (college==null){
            return R.error("数据异常");
        }
        return collegeService.addCollege(college);
    }
    @NeedLogin
    @GetMapping ("/list")
    public R<List<CollegeResult>> listCollege() {
        return collegeService.listCollege();
    }@NeedLogin

    @GetMapping ("/id")
    public R<College> getCollegeById(Long id) {
        return R.success(collegeService.getCollegeById(id));
    }

    @NeedLogin
    @PermissionCheck("2")
    @DeleteMapping("/delete")
    public R<String> deleteCollege(@RequestBody College college) {
        if (college==null){
            return R.error("数据异常");
        }
        return collegeService.deleteCollege(college);
    }

    @NeedLogin
    @PermissionCheck("2")
    @PutMapping("/updata")
    public R<String> updataCollege(@RequestBody College college) {
        if (college==null){
            return R.error("数据异常");
        }
        return collegeService.updataCollege(college);
    }

}
