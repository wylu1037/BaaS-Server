package com.zkjg.baas.task.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.common.util.GsonUtils;
import com.zkjg.baas.task.api.ITaskService;
import com.zkjg.baas.task.bean.JobEntity;
import com.zkjg.baas.task.manager.JobManager;
import com.zkjg.baas.task.mapper.JobMapper;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;

/**
 * @author wylu
 * @date 2022/4/15 下午 10:39
 */
@Slf4j
@Service(timeout = 20000, retries = -1)
public class TaskService implements ITaskService {

    @Resource(type = JobManager.class)
    private JobManager jobManager;

    @Resource(type = JobMapper.class)
    private JobMapper jobMapper;

    @Override
    public void initialize() {
        try {
            jobManager.restartAllJobs();
            log.info("[TaskService] initialize() success");
        } catch (SchedulerException e) {
            log.error("[TaskService] initialize() failure", e);
        }
    }

    @Override
    public BaseResult<Object> addTask(JobEntity jobEntity) {
        log.info("[TaskService] addTask() called with: jobEntity = {}", jobEntity);
        BaseResult<Object> baseResult = new BaseResult<>(0);
        try {
            jobMapper.insert(jobEntity);

            Long jobId = jobEntity.getId();
            if (jobId == null) {
                log.error("新增定时任务异常");
                return baseResult;
            }

            String result = jobManager.refreshJob(jobId);
            if (result.equals("SUCCESS")) {
                baseResult.setData(jobId);
                baseResult.setCode(200);
            }
        } catch (Exception e) {
            log.error("[TaskService] addTask() occurred exception! Params: jobEntity = {}", jobEntity, e);
        }
        return baseResult;
    }

    @Override
    public BaseResult<JobEntity> findJobEntityById(Long id) {
        log.info("[TaskService] findJobEntityById() called with: id = {}", id);
        BaseResult<JobEntity> baseResult = new BaseResult<>(0);
        try {
            JobEntity jobEntity = jobMapper.selectById(id);
            if (jobEntity != null) {
                baseResult.setCode(200);
                baseResult.setMessage("查找成功");
                baseResult.setData(jobEntity);
            }
        } catch (Exception e) {
            baseResult.setMessage("查找失败");
        }
        log.info("[TaskService] findJobEntityById() return result = {}", GsonUtils.toJson(baseResult));
        return baseResult;
    }

    @Override
    public List<JobEntity> loadTasks() {
        LambdaQueryWrapper<JobEntity> wrapper = Wrappers.<JobEntity>lambdaQuery()
            .eq(JobEntity::getEnableFlag, 1)
            .apply("deleteFlag = {0}", 0);
        return jobMapper.selectList(wrapper);
    }
}
