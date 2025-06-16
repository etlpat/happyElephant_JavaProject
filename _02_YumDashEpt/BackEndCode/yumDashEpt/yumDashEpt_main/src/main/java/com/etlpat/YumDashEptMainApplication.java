package com.etlpat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan("com.etlpat.mapper")
@ServletComponentScan
@SpringBootApplication
public class YumDashEptMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(YumDashEptMainApplication.class, args);
    }

}
