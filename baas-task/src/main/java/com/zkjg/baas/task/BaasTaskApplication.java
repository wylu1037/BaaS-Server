package com.zkjg.baas.task;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wylu
 * @date 2022/4/15 下午 12:40
 */
@EnableNacosConfig
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.zkjg.baas.task.service")
@MapperScan("com.zkjg.baas.task.mapper")
@ComponentScan(basePackages = {"com.zkjg.baas.task", "com.zkjg.baas.common.config"})
public class BaasTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaasTaskApplication.class, args);
    }
}
