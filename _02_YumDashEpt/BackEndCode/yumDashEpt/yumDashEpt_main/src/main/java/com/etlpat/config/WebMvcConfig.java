package com.etlpat.config;

import com.etlpat.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

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


    // 扩展mvc框架的消息转换器
    // （自定义前后端交互时，对象数据的序列化/反序列化方式！！！）
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用Jackson将java对象转化为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将以上消息转换器对象，添加到mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }
}
