package com.etlpat.config;

import com.etlpat.interceptors.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


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


// web配置类，可以在其内部注册拦截器
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JWTInterceptor jwtInterceptor;// jwt令牌拦截器


    // 该方法用于注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {// 参数为注册器
        registry.addInterceptor(jwtInterceptor)// 添加jwt令牌拦截器
                .excludePathPatterns("/user/register", "/user/login");// 注册和登录方法不拦截
    }
}
