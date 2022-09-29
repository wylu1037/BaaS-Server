package com.zkjg.baas.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;

/**
 * @author luwenyang
 * @date 2022/4/13 下午 03:47
 */
@SpringBootApplication
@EnableDubbo
@EnableNacosConfig
public class BaasViewApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaasViewApplication.class, args);
    }
}
