package com.zkjg.baas.task.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zkjg.baas.task.api.IHelloJobService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wylu
 * @date 2022/4/16 上午 09:49
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHelloService {

    @Reference
    private IHelloJobService iHelloJobService;

    @Test
    public void testSayHello() {
        iHelloJobService.sayHelloJob("java", "James Gosling", "java是世界上最好的语言！");
    }
}
