package com.qc.topicmanagementsystem.common.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qc.topicmanagementsystem.common.annotation.NeedLogin;
import com.qc.topicmanagementsystem.pojo.User;
import com.qc.topicmanagementsystem.service.IRedisService;
import com.qc.topicmanagementsystem.service.UserService;

import com.qc.topicmanagementsystem.utils.JWTUtil;
import com.qc.topicmanagementsystem.utils.ThreadLocalUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private IRedisService iRedisService;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            System.out.println("OPTIONS请求，放行");
            return true;
        }
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.getAnnotation(NeedLogin.class) == null){
            //如果没有前置条件 需要登陆
            //权限校验直接通过
            return true;
        }
        //校验校验用户localstorage中携带的TGC 认证成功才给通过
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }
        String token = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("tgc")) {
                token = cookie.getValue();
                break;
            }
        }
        if (StringUtils.isEmpty(token)){
            return false;
        }
        DecodedJWT decodedJWT = JWTUtil.deToken(token);
        Claim uuid = decodedJWT.getClaim("uuid");
        Claim cid = decodedJWT.getClaim("id");

        User user = (User) iRedisService.getToken(uuid.asString());
        if (user==null){
            return false;
        }
        if (!user.getId().equals(Long.valueOf(cid.asString()))){
            return false;
        }
        Long userId = Long.valueOf(cid.asString());
        Integer userPermission = user.getPermission();
        User byId = userService.getById(Long.valueOf(userId));
        if (byId.getStatus()==0){
            //用户被封号;
            return false;
        }
        //到了这里已经是登录的了,现在在这里做个优化,如果此时快过期了，可以无感知更新下票的有效期(注意同步更新前端的票)
        if (iRedisService.getTokenTTL(uuid.asString())<1500L){
            iRedisService.setTokenTTL(uuid.asString(),3*3600L);
            Cookie cookie = new Cookie("tgc", token);
            cookie.setMaxAge(3 * 60 * 60); // 后面可以加入7天过期的功能,刷新cookie
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        ThreadLocalUtil.addCurrentUser(byId);
        //暂时直接过
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //防止内存泄露，对ThreadLocal里的对象进行清除
        ThreadLocalUtil.remove();
    }
}
