package com.pjy.nchu2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.pjy.nchu2.mapper")
public class Nchu2Application {

    public static void main(String[] args) {
        SpringApplication.run(Nchu2Application.class, args);
    }

}
