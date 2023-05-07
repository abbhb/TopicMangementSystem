package com.qc.topicmanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.pojo.College;
import com.qc.topicmanagementsystem.pojo.Major;
import com.qc.topicmanagementsystem.pojo.vo.CollegeWithMajor;
import com.qc.topicmanagementsystem.pojo.vo.MajorResult;

import java.util.List;

public interface MajorService extends IService<Major> {
    R<String> addMajor(Major major);

    R<List<MajorResult>> listMajor();

    R<String> deleteMajor(Major major);

    R<String> updataMajor(Major major);

    Major getMajorById(Long id);

    R<List<CollegeWithMajor>> listMajorWithCollege();

    R<College> getCollegeIdByMajorId(Long id);
}
