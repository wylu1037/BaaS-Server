package com.zkjg.baas.task.manager.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.common.util.DateUtils;
import com.zkjg.baas.task.api.ITaskService;
import com.zkjg.baas.task.bean.JobEntity;
import com.zkjg.baas.task.job.BaseJob;
import com.zkjg.baas.task.manager.JobManager;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author wylu
 * @date 2022/4/15 下午 10:53
 */
@Slf4j
@Service
public class JobManagerImpl implements JobManager {

    @Reference
    private ITaskService iTaskService;

    // 构造器注入
    SchedulerFactoryBean schedulerFactoryBean;
    List<BaseJob> jobList;

    public JobManagerImpl(SchedulerFactoryBean schedulerFactoryBean, List<BaseJob> jobList) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.jobList = jobList;
    }

    @Override
    public void restartAllJobs() throws SchedulerException {
        log.info("[JobManagerImpl] restartAllJobs() start execute");
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());

        // 暂停所有
        scheduler.pauseJobs(GroupMatcher.anyGroup());
        for (JobKey jobKey : set) {
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
            scheduler.deleteJob(jobKey);
        }

        List<JobEntity> list = iTaskService.loadTasks();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (JobEntity jobEntity : list) {
            JobDataMap jobDataMap = this.assembleJobDataMap(jobEntity);
            JobKey jobKey = this.getJobKey(jobEntity);
            jobList.forEach(baseJob -> {
                if (!baseJob.acceptGroup().equals(jobEntity.getGroupName())) {
                    return;
                }

                JobDetail jobDetail;
                try {
                    jobDetail = this.getJobDetail(baseJob.getClass(), jobKey, jobEntity.getRemark(), jobDataMap);

                    if (jobEntity.getEnableFlag() == 1) {
                        scheduler.scheduleJob(jobDetail, this.getTrigger(jobEntity));
                    } else {
                        log.info("");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public void shutdownAllJobs() {

    }

    @Override
    public String refreshJob(Long id) throws SchedulerException {
        log.info("[JobManagerImpl] refreshJob() called with: id = {}", id);
        String result = "FAILURE";
        BaseResult<JobEntity> baseResult = iTaskService.findJobEntityById(id);
        if (!baseResult.isSuccess()) {
            return result;
        }

        JobEntity jobEntity = baseResult.getData();
        JobKey jobKey = this.getJobKey(jobEntity);

        // 调度中心
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(jobKey);
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
        scheduler.deleteJob(jobKey);

        JobDataMap map = this.assembleJobDataMap(jobEntity);
        for (BaseJob baseJob : jobList) {
            if (!baseJob.acceptGroup().equals(jobEntity.getGroupName())) {
                return result;
            }
            JobDetail jobDetail;
            try {
                jobDetail = this.getJobDetail(baseJob.getClass(), jobKey, jobEntity.getRemark(), map);
                if (jobEntity.getEnableFlag() == 1) {
                    scheduler.scheduleJob(jobDetail, this.getTrigger(jobEntity));
                    result = "SUCCESS";
                } else {
                    result = "FAILURE";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("[JobManagerImpl] refreshJob() return result = {}", result);
        return result;
    }

    /**
     * 组装JobDetail的参数数据
     *
     * @param jobEntity job实体
     * @return
     */
    private JobDataMap assembleJobDataMap(JobEntity jobEntity) {
        JobDataMap map = new JobDataMap();
        map.put("id", jobEntity.getId());
        map.put("name", jobEntity.getName());
        map.put("groupName", jobEntity.getGroupName());
        map.put("chainId", jobEntity.getChainId());
        map.put("nodeId", jobEntity.getNodeId());
        map.put("cron", jobEntity.getCron());
        map.put("params", jobEntity.getParams());
        map.put("enableFlag", jobEntity.getEnableFlag());
        map.put("remark", jobEntity.getRemark());
        map.put("createTime", DateUtils.format(jobEntity.getCreateTime()));
        map.put("updateTime", DateUtils.format(jobEntity.getUpdateTime()));
        return map;
    }

    private JobDetail getJobDetail(Class<? extends Job> clazz, JobKey jobKey, String jobDescription, JobDataMap map)
        throws InstantiationException, IllegalAccessException {
        return JobBuilder.newJob(clazz.newInstance().getClass())
            .withIdentity(jobKey)
            .withDescription(jobDescription)
            .setJobData(map)
            .storeDurably()
            .build();
    }

    private Trigger getTrigger(JobEntity job) {
        return TriggerBuilder.newTrigger()
            .withIdentity(String.valueOf(job.getId()), job.getGroupName())
            .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
            .build();
    }

    @Override
    public String modifyJob(JobEntity jobEntity) {
        return null;
    }

    @Override
    public void pauseJob(String jobName, String jobGroup) {

    }

    @Override
    public void resumeJob(String jobName, String jobGroup) {

    }

    @Override
    public void deleteJob(String jobName, String jobGroup) {

    }

    @Override
    public JobKey getJobKey(JobEntity jobEntity) {
        return JobKey.jobKey(String.valueOf(jobEntity.getId()), jobEntity.getGroupName());
    }
}
