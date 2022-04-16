package com.zkjg.baas.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zkjg.baas.task.api.ITaskService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * quartz调度器初始化所有定时任务
 *
 * @author wylu
 * @date 2022/4/16 上午 10:05
 */
@Component
@Order(value = 1)
public class SchedulerRunner implements ApplicationRunner {

    @Reference
    private ITaskService iTaskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        iTaskService.initialize();
    }
}
