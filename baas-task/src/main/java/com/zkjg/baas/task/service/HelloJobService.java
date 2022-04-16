package com.zkjg.baas.task.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.common.util.GsonUtils;
import com.zkjg.baas.task.api.IHelloJobService;
import com.zkjg.baas.task.api.ITaskService;
import com.zkjg.baas.task.bean.JobEntity;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wylu
 * @date 2022/4/16 上午 09:19
 */
@Slf4j
@Service(timeout = 20000, retries = -1)
public class HelloJobService implements IHelloJobService {

    ITaskService iTaskService;

    public HelloJobService(ITaskService iTaskService) {
        this.iTaskService = iTaskService;
    }

    @Override
    public BaseResult<String> sayHelloJob(String language, String author, String content) {
        log.info("[HelloJobService] sayHelloJob() called with: language = {}, author = {}, content = {}",
            language, author, content);
        BaseResult<String> baseResult = new BaseResult<>(0);

        String cron = "0/10 * * * * ?";
        Map<String, Object> map = new HashMap<>();
        map.put("language", language);
        map.put("author", author);
        map.put("content", content);
        String params = GsonUtils.toJson(map);

        JobEntity jobEntity = JobEntity.builder()
            .name("Hello World")
            .groupName("Test")
            .chainId(1L)
            .nodeId(1L)
            .cron(cron)
            .params(params)
            .enableFlag(1)
            .remark("测试Say Hello任务")
            .deleteFlag(0)
            .build();

        BaseResult<Object> addTaskResult = iTaskService.addTask(jobEntity);
        log.info("[HelloJobService] sayHelloJob() completed addTask, return result = {}",
            GsonUtils.toJson(addTaskResult));

        if (addTaskResult.isSuccess()) {
            baseResult.setCode(200);
            baseResult.setData(String.format("语言：%s, 作者：%s, 内容：%s", language, author, content));

        } else {
            baseResult.setMessage("添加任务失败");
        }
        log.info(">>>>>> " + baseResult.getData());
        return baseResult;
    }
}
