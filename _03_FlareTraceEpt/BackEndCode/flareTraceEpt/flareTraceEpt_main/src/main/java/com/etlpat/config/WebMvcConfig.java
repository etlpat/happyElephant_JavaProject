package com.etlpat.config;

import com.etlpat.interceptors.LoginInterceptor;
import com.etlpat.interceptors.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// web配置类，可以在其内部注册拦截器
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;


    // 该方法用于注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {// 参数为注册器
        // 添加刷新token拦截器
        registry.addInterceptor(refreshTokenInterceptor)
                .order(0);// 设置执行顺序（小的优先）

        // 添加登录拦截器
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(// 指定不需要拦截的路径
                        "/user/code",
                        "/user/login",
                        "/blog/hot",
                        "/shop/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/voucher/**"
                ).order(1);
    }

}
