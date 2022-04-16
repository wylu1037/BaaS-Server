package com.zkjg.baas.task.job;

import com.zkjg.baas.common.util.GsonUtils;
import com.zkjg.baas.task.constant.GroupEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * quartz中的JobDetail，JobDataMap是JobDetail对象的一部分，用来给Job实例增加属性或配置
 *
 * @author wylu
 * @date 2022/4/15 下午 10:00
 */
@Slf4j
@Component
@DisallowConcurrentExecution //不允许并发执行
public class HelloJob implements BaseJob {

    @Override
    public String acceptGroup() {
        return GroupEnum.HELLO.getName();
    }

    @Override
    public void execute(JobExecutionContext context) {
        // 获取JobDataMap context.getMergedJobDataMap();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        // 调用接口
        log.info("[HelloJob] execute() called with: jobDataMap = {}", GsonUtils.toJson(jobDataMap));
    }
}
