package com.qc.topicmanagementsystem.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qc.topicmanagementsystem.common.Code;
import com.qc.topicmanagementsystem.common.CustomException;
import com.qc.topicmanagementsystem.common.MyString;
import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.mapper.UserMapper;
import com.qc.topicmanagementsystem.pojo.Permission;
import com.qc.topicmanagementsystem.pojo.User;
import com.qc.topicmanagementsystem.pojo.vo.UserResult;
import com.qc.topicmanagementsystem.service.IRedisService;
import com.qc.topicmanagementsystem.service.UserService;
import com.qc.topicmanagementsystem.utils.JWTUtil;
import com.qc.topicmanagementsystem.utils.PageUtil;
import com.qc.topicmanagementsystem.utils.RandomName;
import com.qc.topicmanagementsystem.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private IRedisService iRedisService;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserResult login(String username, String password) {
        if (username==null||username.equals("")){
            throw new CustomException("用户名不存在");
        }
        if (password==null||password.equals("")){
            throw new CustomException("密码不存在");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User one = super.getOne(queryWrapper);
        if (one==null){
            throw new CustomException("用户名或密码错误");
        }
        if (!password.equals(one.getPassword())){//前端传入的明文密码加上后端的盐，处理后跟库中密码比对，一样登陆成功
            throw new CustomException("用户名或密码错误");
        }
        if(one.getStatus() == 0){
            throw new CustomException("账号已禁用!");
        }
        Permission permission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(one.getPermission()));
        //生成TGC和TGT ----  通过此方式登录说明该用户在此设备上的此浏览器没有登录过
        String uuid = RandomName.getUUID();
        String token = JWTUtil.getToken(String.valueOf(one.getId()), String.valueOf(one.getPermission()),uuid );
        iRedisService.addToken(uuid,one,3*3600L);//token作为value，id是不允许更改的
        //15过期的st,防止网络缓慢
        UserResult userResult = new UserResult();
        userResult.setId(String.valueOf(one.getId()));
        userResult.setName(one.getName());
        userResult.setUsername(one.getUsername());
        userResult.setPermission(one.getPermission());
        userResult.setAge(one.getAge());
        userResult.setStatus(one.getStatus());
        userResult.setPermissionName(permission.getName());
        userResult.setPhone(one.getPhone());
        userResult.setSex(one.getSex());
        userResult.setTgc(token);
        return userResult;
    }

    @Override
    public UserResult loginbytgc(String tgc) {
        if (StringUtils.isEmpty(tgc)){
            throw new CustomException("出错了");
        }
        User one = ThreadLocalUtil.getCurrentUser();
        if (one==null){
            throw new CustomException("需要认证");
        }
        if(one.getStatus() == 0){
            throw new CustomException("账号已禁用!");
        }
        Permission permission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(one.getPermission()));
        //15过期的st,防止网络缓慢
        UserResult userResult = new UserResult();
        userResult.setId(String.valueOf(one.getId()));
        userResult.setName(one.getName());
        userResult.setAge(one.getAge());
        userResult.setTgc(tgc);
        userResult.setUsername(one.getUsername());
        userResult.setPermission(one.getPermission());
        userResult.setPermissionName(permission.getName());
        userResult.setPhone(one.getPhone());
        userResult.setSex(one.getSex());
        userResult.setStatus(one.getStatus());
        return userResult;
    }

    @Override
    public R<String> logout(String tgcInRequest) {
        if (StringUtils.isEmpty(tgcInRequest)) {
            return R.error(Code.DEL_TOKEN,"登陆过期");
        }
        DecodedJWT decodedJWT = JWTUtil.deToken(tgcInRequest);
        String uuid = decodedJWT.getClaim("uuid").asString();
        iRedisService.del(uuid);
        return R.error(Code.DEL_TOKEN,"登陆过期");
    }

    @Transactional
    @Override
    public R<UserResult> updataForUserSelf(User user) {
        if (user.getAge()==null){
            throw new CustomException("请填写年龄");
        }
        if (user.getPhone()==null){
            throw new CustomException("请填写手机号");
        }
        User currentUser = ThreadLocalUtil.getCurrentUser();
        if (currentUser==null){
            throw new CustomException("更新失败");
        }
        user.setPassword(null);
        user.setPermission(null);
        user.setMajorId(null);
        user.setStatus(null);
        user.setUsername(null);

        user.setId(currentUser.getId());
        boolean b = super.updateById(user);
        if (b){
            return R.success("更新成功");
        }

        return R.error("更新失败");
    }


    /**
     * 此接口必须登录后才能调用
     * 否则出现id为null
     * @param password
     * @param newpassword
     * @param checknewpassword
     * @return
     */
    @Transactional
    @Override
    public R<UserResult> changePassword(String password, String newpassword, String checknewpassword) {
        if (StringUtils.isEmpty(password)){
            return  R.error("请输入原密码");
        }
        if (StringUtils.isEmpty(newpassword)){
            return R.error("请输入新密码");
        }
        if (StringUtils.isEmpty(checknewpassword)){
            return R.error("请输入确认密码");
        }
        if (!newpassword.equals(checknewpassword)){
            return R.error("两次密码不一致!");
        }
        User user = new User();
        User currentUser = ThreadLocalUtil.getCurrentUser();
        if (currentUser==null){
            throw new CustomException("需鉴权");
        }
        user.setId(currentUser.getId());
        user.setPassword(newpassword);
        boolean b = super.updateById(user);
        if (b){
            return R.success("修改成功,下次登录生效");
        }
        return R.error("err");
    }

    @Override
    public R<List<UserResult>> listAdmin(Integer pageNums, Integer pageSize) {
        Integer integer = userMapper.selectSize();
        if (integer==null){
            return R.error("查询失败");
        }
        PageUtil pageUtil = new PageUtil(pageSize,integer,pageNums);
        List<User> users = userMapper.selectUserByPage(pageUtil);
        if (users==null){
            return R.error("查询失败");
        }
        List<UserResult> userResults = new ArrayList<>();
        for (User user:
                users) {
            UserResult userResult = new UserResult();
            userResult.setId(String.valueOf(user.getId()));
            userResult.setAge(user.getAge());
            userResult.setName(user.getName());
            userResult.setPermission(user.getPermission());
            Permission permission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(user.getPermission()));
            userResult.setPermissionName(permission.getName());
            userResult.setStatus(user.getStatus());
            userResult.setSex(user.getSex());
            userResult.setPhone(user.getPhone());
            userResult.setUsername(user.getUsername());
            userResult.setMajorName(null);
            userResult.setCollegeName(null);
            userResults.add(userResult);
        }
        return R.success(userResults);
    }

    @Override
    @Transactional
    public R<String> addUser(User user) {
        log.info("{}",user);

        if (StringUtils.isEmpty(user.getName())){
            throw new CustomException("参数异常");
        }
        if (StringUtils.isEmpty(user.getUsername())){
            throw new CustomException("参数异常");
        }
        if (StringUtils.isEmpty(user.getPassword())){
            throw new CustomException("参数异常");

        }
        if (StringUtils.isEmpty(user.getPhone())){
            throw new CustomException("参数异常");
        }
        if (user.getId()==null){
            throw new CustomException("参数异常");
        }
        if (user.getStatus()==null){
            throw new CustomException("参数异常");
        }
        if (user.getMajorId()==null){
            throw new CustomException("参数异常");
        }
        if (user.getPhone()==null){
            throw new CustomException("参数异常");
        }
        if (StringUtils.isEmpty(user.getSex())){
            throw new CustomException("参数异常");
        }
        int i = userMapper.addUser(user);
        if (i!=0){
            return R.success("插入成功");
        }
        return R.error("插入失败");
    }

    @Transactional
    @Override
    public R<String> updataUser(User user) {
        if (user.getId() == null) {
            return R.error("更新失败");
        }
        if (user.getUsername() == null) {
            return R.error("更新失败");
        }
        if (user.getName() == null) {
            return R.error("更新失败");
        }
        if (user.getSex() == null) {
            return R.error("更新失败");
        }
        if (user.getPhone() == null) {
            return R.error("更新失败");
        }
        if (user.getStatus() == null) {
            return R.error("更新失败");
        }
        if (user.getPermission() == null) {
            return R.error("更新失败");
        }
        user.setPassword(null);
        User currentUser = ThreadLocalUtil.getCurrentUser();
        if (currentUser==null){
            throw new CustomException("需要认证");
        }
        Permission userPermission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(user.getPermission()));
        Permission myPermission = (Permission) iRedisService.getHash(MyString.permission_key, String.valueOf(currentUser.getPermission()));
        if (myPermission.getWeight()<=userPermission.getWeight()){
            throw new CustomException("权限不足");
        }
        int i = userMapper.updataUser(user);
        if (i!=0){
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }

    @Transactional
    @Override
    public R<String> deleteUser(Long id) {
        if (id==null){
            throw new CustomException("请传入id");
        }
        int i = userMapper.deleteUser(id);
        if (i!=0){
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    @Override
    public R<List<UserResult>> listTeahcer() {
        List<User> users = userMapper.selectTeacher();
        if (users==null) {
            throw new CustomException("查询失败");
        }
        List<UserResult> userResults = new ArrayList<>();
        for (User user:
             users) {
            UserResult userResult = new UserResult();
            userResult.setId(String.valueOf(user.getId()));
            userResult.setName(user.getName());
            userResults.add(userResult);
        }
        return R.success(userResults);
    }
}
