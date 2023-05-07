package com.qc.topicmanagementsystem.pojo.vo;

import com.qc.topicmanagementsystem.pojo.Major;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CollegeWithMajor implements Serializable {
    /**
     * 学院id
     */
    private Long id;

    /**
     * 学院名称
     */
    private String name;

    private List<Major> majorList;

}
