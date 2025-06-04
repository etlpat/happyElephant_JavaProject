package com.etlpat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.etlpat.mapper")
public class GreenTradeEptMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenTradeEptMainApplication.class, args);
    }

}
