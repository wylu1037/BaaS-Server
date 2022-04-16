package com.zkjg.baas.task.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.common.util.GsonUtils;
import com.zkjg.baas.task.api.ITaskService;
import com.zkjg.baas.task.bean.JobEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wylu
 * @date 2022/4/16 上午 09:59
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestTaskService {

    @Reference
    private ITaskService iTaskService;

    @Test
    public void testFindJobEntityById() {
        BaseResult<JobEntity> baseResult = iTaskService.findJobEntityById(1L);
        System.out.println(GsonUtils.toJson(baseResult));
    }
}
