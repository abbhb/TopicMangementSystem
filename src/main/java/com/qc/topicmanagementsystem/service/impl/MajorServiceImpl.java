package com.qc.topicmanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.topicmanagementsystem.common.CustomException;
import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.mapper.MajorMapper;
import com.qc.topicmanagementsystem.pojo.Major;
import com.qc.topicmanagementsystem.pojo.vo.MajorResult;
import com.qc.topicmanagementsystem.service.MajorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
    @Autowired
    private MajorMapper majorMapper;


    @Transactional
    @Override
    public R<String> addMajor(Major major) {
        int i = majorMapper.addMajor(major);
        if (i!=0){
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    @Override
    public R<List<MajorResult>> listMajor() {
        List<Major> majors = majorMapper.listMajor();
        if (majors==null){
            throw new CustomException("业务异常");
        }
        List<MajorResult> results = new ArrayList<>();
        for (Major c:
                majors) {
            MajorResult majorResult = new MajorResult();
            majorResult.setId(String.valueOf(c.getId()));
            majorResult.setName(c.getName());
            majorResult.setCollegeId(String.valueOf(c.getCollegeId()));
            results.add(majorResult);
        }
        return R.success(results);
    }

    @Override
    public Major getMajorById(Long id) {
        Major oneMajorById = majorMapper.getOneMajorById(id);
        return oneMajorById;
    }

    @Transactional
    @Override
    public R<String> deleteMajor(Major major) {
        int i = majorMapper.deleteMajor(major.getId());
        if (i!=0){
            return R.success("删除成功");
        }
        return null;
    }

    @Transactional
    @Override
    public R<String> updataMajor(Major major) {
        log.info("{}",major);
        int i = majorMapper.updataMajor(major);
        log.info("{}",i);
        if (i!=0){
            return R.success("更新成功");
        }
        return null;
    }
}
