package com.qc.topicmanagementsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.topicmanagementsystem.pojo.College;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollegeMapper extends BaseMapper<College> {
    /**
     * 添加学院
     * @param college
     * @return
     */
    int addCollege(College college);


    /**
     * 更新学院
     * @param college
     * @return
     */
    int updataCollege(College college);

    /**
     * 查询所有学院
     * @return
     */
    List<College> listCollege();

    /**
     * 通过id获取学院
     * @param id
     * @return
     */
    College getOneCollegeById(@Param("id") Long id);

    /**
     * 通过id删除
     * @param id
     * @return
     */
    int deleteCollege(@Param("id") Long id);
}
