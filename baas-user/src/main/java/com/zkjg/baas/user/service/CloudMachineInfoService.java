package com.zkjg.baas.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zkjg.baas.user.api.ICloudMachineInfoService;
import com.zkjg.baas.user.bean.CloudMachineInfo;
import com.zkjg.baas.user.mapper.CloudMachineInfoMapper;
import javax.annotation.Resource;

/**
 * @author luwenyang
 * @date 2022/4/13 下午 03:31
 */
@Service(timeout = 20000, retries = -1)
public class CloudMachineInfoService implements ICloudMachineInfoService {

    @Resource(type = CloudMachineInfoMapper.class)
    private CloudMachineInfoMapper cloudMachineInfoMapper;

    @Override
    public CloudMachineInfo getById(Long id) {
        // 构建查询条件
        LambdaQueryWrapper<CloudMachineInfo> wrapper = Wrappers.<CloudMachineInfo>lambdaQuery()
            .select(CloudMachineInfo::getInstanceName, CloudMachineInfo::getInstanceName)
            .eq(CloudMachineInfo::getId, 1)
            .last("limit 1");
        return cloudMachineInfoMapper.selectOne(wrapper);
    }
}
