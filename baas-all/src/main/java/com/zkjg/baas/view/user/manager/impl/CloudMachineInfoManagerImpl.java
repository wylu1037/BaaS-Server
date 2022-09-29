package com.zkjg.baas.view.user.manager.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zkjg.baas.user.api.ICloudMachineInfoService;
import com.zkjg.baas.user.bean.CloudMachineInfo;
import com.zkjg.baas.view.user.manager.CloudMachineInfoManager;
import org.springframework.stereotype.Service;

/**
 * @author luwenyang
 * @date 2022/4/13 下午 04:56
 */
@Service
public class CloudMachineInfoManagerImpl implements CloudMachineInfoManager {

    @Reference
    private ICloudMachineInfoService cloudMachineInfoService;

    @Override
    public CloudMachineInfo getById(Long id) {
        return cloudMachineInfoService.getById(id);
    }
}
