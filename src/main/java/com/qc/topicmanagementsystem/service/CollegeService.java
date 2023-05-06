package com.qc.topicmanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.pojo.College;
import com.qc.topicmanagementsystem.pojo.vo.CollegeResult;

import java.util.List;


public interface CollegeService extends IService<College> {
    R<String> addCollege(College college);


    R<List<CollegeResult>> listCollege();

    College getCollegeById(Long id);

    R<String> deleteCollege(College college);

    R<String> updataCollege(College college);
}
