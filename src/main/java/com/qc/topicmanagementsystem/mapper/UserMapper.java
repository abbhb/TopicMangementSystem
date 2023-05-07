package com.qc.topicmanagementsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.topicmanagementsystem.pojo.User;
import com.qc.topicmanagementsystem.utils.PageUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户表总共有多少数据
     * @return
     */
    Integer selectSize();
    /**
     * 分页查询所有用户
     * @param pageUtil
     * @return
     */
    List<User> selectUserByPage(PageUtil pageUtil);


    /**
     * 添加用户 管理员
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 更新用户
     * @param user
     * @return
     */
    int updataUser(User user);

    /**
     * 删除用户
     * @param id
     * @return
     */
    int deleteUser(@Param("id") Long id);

    List<User> selectTeacher();

    User selectById(@Param("id") Long id);


    User selectUserByUsername(@Param("username") String username);
}
