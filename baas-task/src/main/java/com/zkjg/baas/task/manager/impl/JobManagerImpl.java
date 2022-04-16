package com.zkjg.baas.task.manager.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.common.util.DateUtils;
import com.zkjg.baas.task.api.ITaskService;
import com.zkjg.baas.task.bean.JobEntity;
import com.zkjg.baas.task.bean.req.ModifyJobReq;
import com.zkjg.baas.task.constant.JobStatus;
import com.zkjg.baas.task.job.BaseJob;
import com.zkjg.baas.task.manager.JobManager;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
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
import org.springframework.validation.annotation.Validated;

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
        synchronized (log) {
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
    }

    @Override
    public void shutdownAllJobs() throws SchedulerException {
        synchronized (log) {
            Scheduler scheduler;
            scheduler = schedulerFactoryBean.getScheduler();
            Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
            scheduler.pauseJobs(GroupMatcher.anyGroup());
            for (JobKey jobKey : set) {
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                scheduler.deleteJob(jobKey);
            }
        }
    }

    @Override
    public String refreshJob(Long id) throws SchedulerException {
        log.info("[JobManagerImpl] refreshJob() called with: id = {}", id);
        synchronized (log) {
            String result = JobStatus.FAILURE;
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
                        result = JobStatus.SUCCESS;
                    }
                } catch (Exception e) {
                    result = JobStatus.EXCEPTION;
                    log.error("[JobManagerImpl] refreshJob() occurred exception! Params: id = {}", id, e);
                }
            }
            log.info("[JobManagerImpl] refreshJob() return result = {}", result);
            return result;
        }
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
        map.put("operatorId", jobEntity.getOperatorId());
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
    public String modifyJob(@Validated ModifyJobReq reqParams) {
        log.info("[JobManagerImpl] modifyJob() called with: reqParams = {}", reqParams);
        String cron = reqParams.getCron();
        if (!CronExpression.isValidExpression(cron)) {
            return "cron is invalid";
        }
        synchronized (log) {
            BaseResult<JobEntity> findResult = iTaskService.findJobEntityById(reqParams.getId());
            if (findResult.isSuccess() && findResult.getData().getEnableFlag() == 1) {
                try {
                    JobEntity jobEntity = findResult.getData();
                    JobKey jobKey = this.getJobKey(jobEntity);
                    TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());
                    Scheduler scheduler = schedulerFactoryBean.getScheduler();
                    CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    String oldCron = cronTrigger.getCronExpression();

                    if (!oldCron.equals(cron)) {
                        jobEntity.setCron(cron);
                        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                        CronTrigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity(jobKey.getName(), jobKey.getGroup())
                            .withSchedule(cronScheduleBuilder)
                            .usingJobData(this.assembleJobDataMap(jobEntity))
                            .build();

                        scheduler.rescheduleJob(triggerKey, trigger);
                        return JobStatus.SUCCESS;
                    }
                } catch (Exception e) {
                    log.error("[JobManagerImpl] modifyJob() occurred exception! Params: reqParams = {}", reqParams, e);
                    return JobStatus.EXCEPTION;
                }
            } else {
                return JobStatus.FAILURE;
            }
        }
        return null;
    }

    @Override
    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
    }

    @Override
    public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
    }

    @Override
    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        synchronized (log) {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.deleteJob(jobKey);
        }
    }

    @Override
    public JobKey getJobKey(JobEntity jobEntity) {
        return JobKey.jobKey(String.valueOf(jobEntity.getId()), jobEntity.getGroupName());
    }
}
