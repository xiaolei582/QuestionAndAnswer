package com.panshi.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author huangxiaolei
 */
//@Component
public class CrossDomain extends HandlerInterceptorAdapter  {

    private static StringRedisTemplate redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(redisTemplate == null){
            redisTemplate = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()).getBean(StringRedisTemplate.class);
        }
        String[] split = request.getRequestURI().split("/");

        //获取redis中的缓存
        String currentLimiting = redisTemplate.opsForValue().get("currentLimiting");
        //判断用户是否在*秒之前访问过
        String  user = "user:"+split[3]+":"+split[4];
        if(currentLimiting != null && currentLimiting.equals(user)){
            System.out.println(user);
            response.setHeader("Content-type","text/html;charset=utf-8");
            response.getWriter().print("正在处理");
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String[] split = request.getRequestURI().split("/");
        String  user = "user:"+split[3]+":"+split[4];
        //保存请求后的用户信息
        redisTemplate.opsForValue().set("currentLimiting",user,10, TimeUnit.SECONDS);
    }
}
