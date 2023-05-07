package com.qc.topicmanagementsystem.controller;

import com.qc.topicmanagementsystem.common.R;
import com.qc.topicmanagementsystem.common.annotation.NeedLogin;
import com.qc.topicmanagementsystem.common.annotation.PermissionCheck;
import com.qc.topicmanagementsystem.pojo.User;
import com.qc.topicmanagementsystem.pojo.vo.UserResult;
import com.qc.topicmanagementsystem.pojo.vo.UserResult2;
import com.qc.topicmanagementsystem.service.IRedisService;
import com.qc.topicmanagementsystem.service.UserService;
import com.qc.topicmanagementsystem.utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 方便调试就允许跨域了，不允许可以加nginx
 */
@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final IRedisService iRedisService;

    private final UserService userService;
    public UserController(IRedisService iRedisService, UserService userService) {
        this.iRedisService = iRedisService;
        this.userService = userService;
    }

    /**
     * 登录成功
     * 写入cookie，key为tgc，方便可能后续接入自己的CAS
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<UserResult> login(HttpServletResponse response, @RequestBody Map<String, Object> user) {
        /**
         * 对密码进行加密传输
         */
        String username = (String) user.get("username");//用户名可以是用户名也可以用邮箱
        String password = (String) user.get("password");
        log.info("{}", response.getStatus());
        UserResult userResult = userService.login(username, password);
        if (StringUtils.isEmpty(userResult.getTgc())) {
            return R.error("好奇怪，出错了!");
        }
        log.info("写入{}", userResult.getTgc());
        Cookie cookie = new Cookie("tgc", userResult.getTgc());
        cookie.setMaxAge(3 * 60 * 60); // 后面可以加入7天过期的功能
        cookie.setPath("/");
        response.addCookie(cookie);
        //正常登录平台
        return R.success(userResult);
    }

    /**
     * cookies里的token登录
     * @param request
     * @return
     */
    @NeedLogin
    @PostMapping("/loginbytgc")
    public R<UserResult> loginbytgc(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return R.error("好奇怪，出错了!");
        }
        String tgc = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("tgc")) {
                tgc = cookie.getValue();
                break;
            }
        }
        if (StringUtils.isEmpty(tgc)) {
            return R.error("好奇怪，出错了!");
        }
        UserResult userResult = userService.loginbytgc(tgc);
        if (StringUtils.isEmpty(userResult.getTgc())) {
            return R.error("好奇怪，出错了!");
        }

        return R.success(userResult);
    }

    @NeedLogin
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String tgcInRequest = JWTUtil.getTGCInRequest(request);
        Cookie cookie = new Cookie("tgc", "");
        cookie.setMaxAge(0); // 使其过期
        cookie.setPath("/");
        response.addCookie(cookie);
        return userService.logout(tgcInRequest);
    }

    /**
     * 更新操作(user)
     * 此Api只允许更新自己的信息
     * 独立掉 用户自己更新信息方便加权限注解
     *
     * @param user
     * @return
     */
    @NeedLogin
    @PutMapping("/self")
    public R<UserResult> updataForUserSelf(HttpServletRequest request, @RequestBody User user) {
        log.info("user = {}", user);

        if (user.getUsername() == null) {
            return R.error("更新失败");
        }
        if (user.getName() == null) {
            return R.error("更新失败");
        }
        if (user.getSex() == null) {
            return R.error("更新失败");
        }
        if (user.getAge() == null) {
            return R.error("更新失败");
        }
        if (user.getPhone() == null) {
            return R.error("更新失败");
        }
        String tgcInRequest = JWTUtil.getTGCInRequest(request);
        log.info("{}", tgcInRequest);
        if (StringUtils.isEmpty(tgcInRequest)) {
            return R.error("更新失败");
        }
        return userService.updataForUserSelf(user);
    }


    @NeedLogin
    @PutMapping("/changePassword")
    public R<UserResult> changePassword(@RequestBody Map<String, Object> user) {
        System.out.println("user = " + user);
        String password = (String) user.get("password");
        String newpassword = (String) user.get("newpassword");
        String checknewpassword = (String) user.get("checkpassword");
        return userService.changePassword(password, newpassword, checknewpassword);
    }

    @NeedLogin
    @PermissionCheck("2")
    @GetMapping("/list")
    public R<List<UserResult2>> list(Integer pageNums, Integer pageSize){
        if (pageNums==null||pageSize==null){
            return R.error("err");
        }
        return userService.listAdmin(pageNums,pageSize);
    }
    @NeedLogin
    @GetMapping("/listTeahcer")
    public R<List<UserResult>> listTeahcer(){
        return userService.listTeahcer();
    }


    @NeedLogin
    @PermissionCheck("2")
    @PostMapping("/addUser")
    public R<String> addUser(@RequestBody User user) {
        if (user==null){
            return R.error("数据异常");
        }
        return userService.addUser(user);
    }


    /**
     * 更新操作(admin)
     * 独立掉 用户自己更新信息方便加权限注解
     * @param user
     * @return
     */
    @NeedLogin
    @PermissionCheck("2")
    @PutMapping("/updataUser")
    public R<String> updataUser(@RequestBody User user) {
        log.info("user = {}", user);

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
        return userService.updataUser(user);
    }

    /**
     * 删除用户
     * @return
     */
    @NeedLogin
    @PermissionCheck("2")
    @DeleteMapping("/delete")
    public R<String> deleteUsers(@RequestBody User user) {
        if (user==null){
            return R.error("删除失败");
        }
        if (user.getId()==null){
            return R.error("删除失败");
        }
        return userService.deleteUser(user.getId());

    }
}
