package com.qc.topicmanagementsystem.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 需要保证一个组只能有一个组长
 */
@Data
public class TopicStudent implements Serializable {
    /**
     * 学生编号
     */
    private Long studentId;

    /**
     * 是否为组长
     * 1为是
     * 0为不是
     */
    private Integer isTeamEader;


}
