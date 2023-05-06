package com.qc.topicmanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.topicmanagementsystem.common.CustomException;
import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.mapper.CollegeMapper;
import com.qc.topicmanagementsystem.pojo.College;
import com.qc.topicmanagementsystem.pojo.vo.CollegeResult;
import com.qc.topicmanagementsystem.service.CollegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {
    @Autowired
    private CollegeMapper collegeMapper;


    @Transactional
    @Override
    public R<String> addCollege(College college) {
        int i = collegeMapper.addCollege(college);
        if (i!=0){
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    @Override
    public R<List<CollegeResult>> listCollege() {
        List<College> colleges = collegeMapper.listCollege();
        if (colleges==null){
            throw new CustomException("业务异常");
        }
        List<CollegeResult> results = new ArrayList<>();
        for (College c:
                colleges) {
            CollegeResult collegeResult = new CollegeResult();
            collegeResult.setId(String.valueOf(c.getId()));
            collegeResult.setName(c.getName());
            results.add(collegeResult);
        }
        return R.success(results);
    }

    @Override
    public College getCollegeById(Long id) {
        College oneCollegeById = collegeMapper.getOneCollegeById(id);
        return oneCollegeById;
    }

    @Transactional
    @Override
    public R<String> deleteCollege(College college) {
        int i = collegeMapper.deleteCollege(college.getId());
        if (i!=0){
            return R.success("删除成功");
        }
        return null;
    }

    @Transactional
    @Override
    public R<String> updataCollege(College college) {
        log.info("{}",college);
        int i = collegeMapper.updataCollege(college);
        log.info("{}",i);
        if (i!=0){
            return R.success("更新成功");
        }
        return null;
    }

}

