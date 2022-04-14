package com.zkjg.baas.view.user.controller;

import com.zkjg.baas.user.bean.CloudMachineInfo;
import com.zkjg.baas.view.user.manager.CloudMachineInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luwenyang
 * @date 2022/4/13 下午 03:50
 */
@RestController
@RequestMapping(value = "/user/cloudMachine")
public class CloudMachineInfoController {

    @Autowired
    private CloudMachineInfoManager cloudMachineInfoManager;

    @GetMapping("getById")
    public CloudMachineInfo getByIdHandler(
        @RequestParam Long id) {
        return cloudMachineInfoManager.getById(id);
    }
}
