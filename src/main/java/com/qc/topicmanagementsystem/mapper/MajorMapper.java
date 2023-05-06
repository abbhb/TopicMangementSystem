package com.qc.topicmanagementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.topicmanagementsystem.pojo.Major;
import com.qc.topicmanagementsystem.pojo.Major;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MajorMapper extends BaseMapper<Major> {

    /**
     * 添加学院
     * @param major
     * @return
     */
    int addMajor(Major major);


    /**
     * 更新学院
     * @param major
     * @return
     */
    int updataMajor(Major major);

    /**
     * 查询所有学院
     * @return
     */
    List<Major> listMajor();

    /**
     * 通过id获取学院
     * @param id
     * @return
     */
    Major getOneMajorById(@Param("id") Long id);

    /**
     * 通过id删除
     * @param id
     * @return
     */
    int deleteMajor(@Param("id") Long id);

}
