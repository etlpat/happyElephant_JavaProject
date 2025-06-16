package com.etlpat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    // 配置静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将浏览器的"/backend/**"访问路径，映射到resources文件夹下的"/backend/"路径
        registry.addResourceHandler("/backend/**")
                .addResourceLocations("classpath:/backend/");

        // 将浏览器的"/front/**"访问路径，映射到resources文件夹下的"/front/"路径
        registry.addResourceHandler("/front/**")
                .addResourceLocations("classpath:/front/");
    }
}
