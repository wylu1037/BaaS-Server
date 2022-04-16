package com.zkjg.baas.task.manager;

import com.zkjg.baas.task.bean.JobEntity;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

/**
 * @author wylu
 * @date 2022/4/15 下午 10:52
 */
public interface JobManager {

    void restartAllJobs() throws SchedulerException;

    void shutdownAllJobs();

    String refreshJob(Long id) throws SchedulerException;

    String modifyJob(JobEntity jobEntity);

    void pauseJob(String jobName, String jobGroup);

    void resumeJob(String jobName, String jobGroup);

    void deleteJob(String jobName, String jobGroup);

    /**
     * 获取JobDetail的标识JobKey
     *
     * @param jobEntity
     * @return
     */
    JobKey getJobKey(JobEntity jobEntity);
}
