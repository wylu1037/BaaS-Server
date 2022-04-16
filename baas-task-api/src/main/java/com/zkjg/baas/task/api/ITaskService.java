package com.zkjg.baas.task.api;

import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.task.bean.JobEntity;
import com.zkjg.baas.task.bean.req.ModifyJobReq;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * @author wylu
 * @date 2022/4/15 下午 10:38
 */
public interface ITaskService {

    /**
     * 初始化：重启所有任务
     */
    public void initialize();

    /**
     * 增加定时任务
     *
     * @param jobEntity
     * @return
     */
    BaseResult<Object> addTask(@NotNull JobEntity jobEntity);

    /**
     * 查找定时任务实体信息
     *
     * @param id
     * @return
     */
    BaseResult<JobEntity> findJobEntityById(@NotNull Long id);

    /**
     * 加载所有任务
     *
     * @return
     */
    List<JobEntity> loadTasks();

    /**
     * 删除指定任务
     *
     * @param id 任务id
     * @return
     */
    BaseResult<Object> deleteTask(@NotNull Long id);

    BaseResult<Object> pauseTask(@NotNull Long id);

    BaseResult<Object> modifyTask(@NotNull ModifyJobReq reqParams);

    BaseResult<Object> resumeTask(@NotNull Long id);
}
