package com.zkjg.baas.task.job;

import org.quartz.Job;

/**
 * @author wylu
 * @date 2022/4/16 上午 12:13
 */
public interface BaseJob extends Job {

    /**
     * 定时任务分组，与表job_entity的groupName字段值保持一致
     *
     * @return
     */
    String acceptGroup();
}
