package com.etlpat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@MapperScan("com.etlpat.mapper")
@EnableTransactionManagement// 开启事务的支持
@EnableAspectJAutoProxy(exposeProxy = true)// 暴露代理对象
@SpringBootApplication
public class FlareTraceEptMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlareTraceEptMainApplication.class, args);
    }

}
