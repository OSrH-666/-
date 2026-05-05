package com.example.unionsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.unionsystem.mapper")
public class UnionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnionSystemApplication.class, args);
    }
}