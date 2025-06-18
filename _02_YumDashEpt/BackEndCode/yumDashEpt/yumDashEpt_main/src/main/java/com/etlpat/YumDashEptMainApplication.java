package com.etlpat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.etlpat.mapper")
@EnableTransactionManagement// 开启事务的支持
@ServletComponentScan
@SpringBootApplication
public class YumDashEptMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(YumDashEptMainApplication.class, args);
    }

}
