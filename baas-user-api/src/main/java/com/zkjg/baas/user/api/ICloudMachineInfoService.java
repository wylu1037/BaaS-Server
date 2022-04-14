package com.zkjg.baas.user.api;

import com.zkjg.baas.user.bean.CloudMachineInfo;

/**
 * 云服务器信息接口
 *
 * @author luwenyang
 * @date 2022/4/13 下午 03:14
 */
public interface ICloudMachineInfoService {

    public CloudMachineInfo getById(Long id);
}
