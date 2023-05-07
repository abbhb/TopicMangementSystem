package com.qc.topicmanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.pojo.User;
import com.qc.topicmanagementsystem.pojo.vo.UserResult;
import com.qc.topicmanagementsystem.pojo.vo.UserResult2;

import java.util.List;

public interface UserService extends IService<User> {
    UserResult login(String username, String password);

    UserResult loginbytgc(String tgc);

    R<String> logout(String tgcInRequest);

    R<UserResult> updataForUserSelf(User user);

    R<UserResult> changePassword(String password, String newpassword, String checknewpassword);

    R<List<UserResult2>> listAdmin(Integer pageNums, Integer pageSize);

    R<String> addUser(User user);

    R<String> updataUser(User user);

    R<String> deleteUser(Long id);

    R<List<UserResult>> listTeahcer();
}
