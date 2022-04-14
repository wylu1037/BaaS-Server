package com.zkjg.baas.user;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author luwenyang
 * @date 2022/3/28 上午 10:13
 */
@EnableNacosConfig
@SpringBootApplication
// 指定扫描路径
@EnableDubbo(scanBasePackages = "com.zkjg.baas.user.service")
@MapperScan("com.zkjg.baas.user.mapper")
@ComponentScan(basePackages = {"com.zkjg.baas.user", "com.zkjg.baas.common.config"})
public class BaasUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaasUserApplication.class, args);
    }
}
