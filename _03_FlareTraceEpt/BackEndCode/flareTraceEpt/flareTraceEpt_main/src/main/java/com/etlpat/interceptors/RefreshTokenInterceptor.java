package com.etlpat.interceptors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.etlpat.dto.UserDTO;
import com.etlpat.utils.RedisConstants;
import com.etlpat.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;


// springBoot的拦截器
//
// (1)添加拦截器的步骤
//  1.创建拦截器类
//      ①创建一个拦截器类，使其实现HandlerInterceptor接口。
//      ②重写该类的handle拦截方法；返回true表示放行，false表示中断。
//  2.在配置类中注册拦截器
//      ①在web配置类的addInterceptors方法中，添加要注册的拦截器。
//      ②配置拦截路径（可以指定要 拦截/排除 的路径）。
//      ③配置拦截器的执行顺序。
//


@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 前置拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {// 若token为空（未登录）
            return true;
        }

        // 2.从redis中获取token对应的用户信息map
        String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(tokenKey);
        if (userMap == null || userMap.isEmpty()) {// 若用户信息为空（token错误/用户登录信息过期）
            return true;
        }

        // 3.若用户存在（已登录），则将user存入ThreadLocal中
        // TODO 注意：只有用户已经登录时，ThreadLocal中才存在数据！
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        UserHolder.saveUser(userDTO);
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);// 刷新user信息的有效期
        return true;
    }


    // 后置拦截器
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户信息，避免内存泄漏
        UserHolder.removeUser();
    }
}
