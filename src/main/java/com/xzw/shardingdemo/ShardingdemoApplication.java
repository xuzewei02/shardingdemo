package com.xzw.shardingdemo;

import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = SpringBootConfiguration.class)//关键！！！被坑1天  不开启shardingsphere自动装配
public class ShardingdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingdemoApplication.class, args);
    }

}
