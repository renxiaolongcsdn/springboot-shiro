package com.rxl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ren.xiaolong
 * @date 2022/4/18
 * @Description
 */
@SpringBootApplication
@MapperScan(basePackages = "com.rxl.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
