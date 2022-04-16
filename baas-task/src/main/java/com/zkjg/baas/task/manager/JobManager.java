package com.zkjg.baas.task.manager;

import com.zkjg.baas.task.bean.JobEntity;
import com.zkjg.baas.task.bean.req.ModifyJobReq;
import javax.validation.constraints.NotNull;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;

/**
 * @author wylu
 * @date 2022/4/15 下午 10:52
 */
public interface JobManager {

    void restartAllJobs() throws SchedulerException;

    void shutdownAllJobs() throws SchedulerException;

    String refreshJob(@NotNull Long id) throws SchedulerException;

    String modifyJob(@Validated ModifyJobReq reqParams);

    void pauseJob(@NotNull String jobName, @NotNull String jobGroup) throws SchedulerException;

    void resumeJob(@NotNull String jobName, @NotNull String jobGroup) throws SchedulerException;

    void deleteJob(@NotNull String jobName, @NotNull String jobGroup) throws SchedulerException;

    /**
     * 获取JobDetail的标识JobKey
     *
     * @param jobEntity
     * @return
     */
    JobKey getJobKey(@NotNull JobEntity jobEntity);
}
