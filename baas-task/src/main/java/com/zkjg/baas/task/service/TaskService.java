package com.zkjg.baas.task.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.common.constant.HttpStatus;
import com.zkjg.baas.common.util.GsonUtils;
import com.zkjg.baas.task.api.ITaskService;
import com.zkjg.baas.task.bean.JobEntity;
import com.zkjg.baas.task.bean.req.ModifyJobReq;
import com.zkjg.baas.task.constant.JobStatus;
import com.zkjg.baas.task.manager.JobManager;
import com.zkjg.baas.task.mapper.JobEntityMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;

/**
 * @author wylu
 * @date 2022/4/15 下午 10:39
 */
@Slf4j
@Service(timeout = 20000, retries = -1)
public class TaskService implements ITaskService {

    // 依赖注入
    JobManager jobManager;
    JobEntityMapper jobEntityMapper;

    public TaskService(JobManager jobManager, JobEntityMapper jobEntityMapper) {
        this.jobManager = jobManager;
        this.jobEntityMapper = jobEntityMapper;
    }

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
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            jobEntityMapper.insert(jobEntity);

            Long jobId = jobEntity.getId();
            if (jobId == null) {
                log.error("新增定时任务异常");
                return baseResult;
            }

            String result = jobManager.refreshJob(jobId);
            if (result.equals("SUCCESS")) {
                baseResult.setData(jobId);
                baseResult.setCode(HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("[TaskService] addTask() occurred exception! Params: jobEntity = {}", jobEntity, e);
        }
        return baseResult;
    }

    @Override
    public BaseResult<JobEntity> findJobEntityById(Long id) {
        log.info("[TaskService] findJobEntityById() called with: id = {}", id);
        BaseResult<JobEntity> baseResult = new BaseResult<>();
        try {
            JobEntity jobEntity = jobEntityMapper.selectById(id);
            if (jobEntity != null) {
                baseResult.setCode(HttpStatus.OK);
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
        return jobEntityMapper.selectList(wrapper);
    }

    @Override
    public BaseResult<Object> deleteTask(Long id) {
        log.info("[TaskService] deleteTask() called with: id = {}", id);
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            LambdaUpdateWrapper<JobEntity> wrapper = Wrappers.<JobEntity>lambdaUpdate()
                .set(JobEntity::getDeleteFlag, 1)
                .eq(JobEntity::getId, id)
                .last("limit 1");
            int updateCount = jobEntityMapper.update(null, wrapper);
            if (updateCount > 0) {
                BaseResult<JobEntity> findResult = this.findJobEntityById(id);
                if (!findResult.isSuccess()) {
                    return baseResult;
                }
                JobEntity jobEntity = findResult.getData();
                jobManager.deleteJob(String.valueOf(jobEntity.getId()), jobEntity.getGroupName());
                baseResult.setCode(HttpStatus.OK);
            }
        } catch (Exception e) {
            baseResult.setMessage("删除任务失败");
            log.error("[TaskService] deleteTask() occurred exception! Params: id = {}", id, e);
        }
        return baseResult;
    }

    @Override
    public BaseResult<Object> pauseTask(Long id) {
        log.info("[TaskService] pauseTask() called with: id = {}", id);
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            BaseResult<JobEntity> findResult = findJobEntityById(id);
            if (findResult.isSuccess()) {
                int updateCount = jobEntityMapper.update(null, Wrappers.<JobEntity>lambdaUpdate()
                    .set(JobEntity::getEnableFlag, 0)
                    .eq(JobEntity::getId, id)
                    .last("limit 1")
                );
                if (updateCount > 0) {
                    JobEntity jobEntity = findResult.getData();
                    jobManager.pauseJob(String.valueOf(id), jobEntity.getGroupName());
                    baseResult.setCode(HttpStatus.OK);
                }
            }
        } catch (SchedulerException e) {
            log.error("[TaskService] pauseTask() occurred exception! Params: id = {}", id, e);
        }
        return baseResult;
    }

    @Override
    public BaseResult<Object> modifyTask(ModifyJobReq reqParams) {
        log.info("[TaskService] modifyTask() called with: reqParams = {}", reqParams);
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            String result = jobManager.modifyJob(reqParams);
            if (result != null && result.equals(JobStatus.SUCCESS)) {
                int updateCount = jobEntityMapper.update(null, Wrappers.<JobEntity>lambdaUpdate()
                    .set(JobEntity::getCron, reqParams.getCron())
                    .eq(JobEntity::getId, reqParams.getId())
                    .last("limit 1")
                );
                if (updateCount > 0) {
                    baseResult.setCode(HttpStatus.OK);
                    baseResult.setMessage("更新定时任务成功");
                }
            }
        } catch (Exception e) {
            log.error("[TaskService] modifyTask() occurred exception! Params: reqParams = {}", reqParams, e);
        }
        return baseResult;
    }

    @Override
    public BaseResult<Object> resumeTask(Long id) {
        log.info("[TaskService] resumeTask() called with: id = {}", id);
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            BaseResult<JobEntity> findResult = findJobEntityById(id);
            if (findResult.isSuccess()) {
                int updateCount = jobEntityMapper.update(null, Wrappers.<JobEntity>lambdaUpdate()
                    .set(JobEntity::getEnableFlag, 1)
                    .eq(JobEntity::getId, id)
                    .last("limit 1")
                );
                if (updateCount > 0) {
                    JobEntity jobEntity = findResult.getData();
                    jobManager.resumeJob(String.valueOf(id), jobEntity.getGroupName());
                    baseResult.setCode(HttpStatus.OK);
                }
            }
        } catch (SchedulerException e) {
            log.error("[TaskService] resumeTask() occurred exception! Params: id = {}", id, e);
        }
        return baseResult;
    }
}
