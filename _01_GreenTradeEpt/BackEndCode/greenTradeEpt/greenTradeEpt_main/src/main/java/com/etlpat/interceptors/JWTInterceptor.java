package com.etlpat.interceptors;

import com.etlpat.utils.JWTUtil;
import com.etlpat.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


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


// JWT拦截器：拦截请求方法，并验证其JWT令牌（不拦截登录注册）
@Component
public class JWTInterceptor implements HandlerInterceptor {// 拦截器方法，必须实现HandlerInterceptor接口
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    // (1)重写preHandle拦截方法（在Controller执行前拦截）
    // 功能：拦截全部浏览器请求，对其令牌进行验证。
    //      若验证成功，则将本次登录用户的信息存入ThreadLocal中。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的JWT令牌
        String token = request.getHeader("Authorization");

        // 验证令牌
        try {
            // ①验证令牌是否被篡改/过期
            Map<String, Object> userMap = JWTUtil.verifyToken(token);// 验证令牌

            // ②验证令牌是否和Redis中的令牌一致
            String redisToken = stringRedisTemplate.opsForValue().get("etlpat:greenTradeEpt:token:" + userMap.get("username"));
            if (redisToken == null || !redisToken.equals(token)) {
                throw new RuntimeException();
            }

            // 若令牌验证成功，则放行
            ThreadLocalUtil.set(userMap);// 【将本次登录的用户存入ThreadLocal中】
            return true;// 放行

        } catch (Exception e) {// 若报异常，表示令牌验证失败，则不放行
            response.setStatus(401);// 返回401状态码
            return false;// 不放行
        }
    }


    // (2)重写afterCompletion拦截方法（在Controller执行后拦截）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求关闭前，删除本次请求的登录用户信息
        ThreadLocalUtil.remove();// 移除ThreadLocal中用户数据，防止内存泄漏
    }

}
