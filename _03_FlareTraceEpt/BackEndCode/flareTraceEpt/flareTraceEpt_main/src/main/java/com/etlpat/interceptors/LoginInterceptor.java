package com.etlpat.interceptors;

import com.etlpat.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


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
public class LoginInterceptor implements HandlerInterceptor {

    // 前置拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 注意：经过上一层拦截器后，若用户已登录，则ThreadLocal中存放了用户信息；
        //      相反，若ThreadLocal中不存在任何数据，则表示用户登录失败。
        if (UserHolder.getUser() == null) {// 若登录失败
            response.setStatus(401);
            return false;
        }
        return true;
    }

}
