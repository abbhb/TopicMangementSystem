package com.qc.topicmanagementsystem.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResult2 extends UserResult implements Serializable {

    private String collegeName;

    private Long collegeId;
}
