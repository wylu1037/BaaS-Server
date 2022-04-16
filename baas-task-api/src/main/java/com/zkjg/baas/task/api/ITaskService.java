package com.zkjg.baas.task.api;

import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.task.bean.JobEntity;
import java.util.List;

/**
 * @author wylu
 * @date 2022/4/15 下午 10:38
 */
public interface ITaskService {

    public void initialize();

    /**
     * 增加定时任务
     *
     * @param jobEntity
     * @return
     */
    BaseResult<Object> addTask(JobEntity jobEntity);

    /**
     * 查找定时任务实体信息
     *
     * @param id
     * @return
     */
    BaseResult<JobEntity> findJobEntityById(Long id);

    List<JobEntity> loadTasks();
}
