package com.zkjg.baas.view;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author luwenyang
 * @date 2022/4/13 下午 03:47
 */
@SpringBootApplication
@EnableNacosConfig
// 指定Spring扫描路径
@EnableDubbo(scanBasePackages = "com.zkjg.baas.view.*.manager.impl")
@PropertySource("classpath:bootstrap.yml")
public class BaasViewApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaasViewApplication.class, args);
    }
}
