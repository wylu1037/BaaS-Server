package com.zkjg.baas.task.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zkjg.baas.base.bean.BaseResult;
import com.zkjg.baas.common.constant.HttpStatus;
import com.zkjg.baas.task.api.ITaskService;
import com.zkjg.baas.task.bean.JobEntity;
import com.zkjg.baas.task.bean.req.ModifyJobReq;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wylu
 * @date 2022/4/15 下午 09:25
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Reference
    private ITaskService iTaskService;

    @PostMapping("initialize")
    public BaseResult<Object> initializeHandler() {
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            iTaskService.initialize();
            baseResult.setCode(HttpStatus.OK);
            baseResult.setMessage("初始化所有任务成功");
        } catch (Exception e) {
            baseResult.setMessage("初始化所有任务失败");
        }
        return baseResult;
    }

    @PostMapping("add")
    public BaseResult<Object> addTaskHandler(
        @RequestBody JobEntity jobEntity) {
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            baseResult = iTaskService.addTask(jobEntity);
            baseResult.setMessage("新增任务成功");
        } catch (Exception e) {
            baseResult.setMessage("新增任务失败");
        }
        return baseResult;
    }

    @GetMapping("findAll")
    public BaseResult<List<JobEntity>> findAllTaskHandler() {
        BaseResult<List<JobEntity>> baseResult = new BaseResult<>();
        try {
            List<JobEntity> data = iTaskService.loadTasks();
            baseResult.setCode(HttpStatus.OK);
            baseResult.setData(data);
            baseResult.setMessage("查找所有任务成功");
        } catch (Exception e) {
            baseResult.setMessage("查找所有任务失败");
        }
        return baseResult;
    }

    @PostMapping("delete")
    public BaseResult<Object> deleteTaskHandler(@RequestParam Long id) {
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            baseResult = iTaskService.deleteTask(id);
            baseResult.setMessage("删除任务成功");
        } catch (Exception e) {
            baseResult.setMessage("删除任务失败");
        }
        return baseResult;
    }

    @PostMapping("pause")
    public BaseResult<Object> pauseTaskHandler(@RequestParam Long id) {
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            baseResult = iTaskService.pauseTask(id);
            baseResult.setMessage("暂停任务成功");
        } catch (Exception e) {
            baseResult.setMessage("暂停任务失败");
        }
        return baseResult;
    }

    @PostMapping("modify")
    public BaseResult<Object> modifyTaskHandler(@RequestBody ModifyJobReq reqParams) {
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            baseResult = iTaskService.modifyTask(reqParams);
            baseResult.setMessage("修改任务成功");
        } catch (Exception e) {
            baseResult.setMessage("修改任务失败");
        }
        return baseResult;
    }

    @PostMapping("resume")
    public BaseResult<Object> resumeTaskHandler(@RequestParam Long id) {
        BaseResult<Object> baseResult = new BaseResult<>();
        try {
            baseResult = iTaskService.resumeTask(id);
            baseResult.setMessage("恢复任务成功");
        } catch (Exception e) {
            baseResult.setMessage("恢复任务失败");
        }
        return baseResult;
    }
}
