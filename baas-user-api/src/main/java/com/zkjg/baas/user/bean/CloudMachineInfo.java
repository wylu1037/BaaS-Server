package com.zkjg.baas.user.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luwenyang
 * @date 2022/4/13 下午 03:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "云服务器持久化对象")
@TableName(value = "cloud_machine_info")
public class CloudMachineInfo implements Serializable {

    private static final long serialVersionUID = -1341916103185557836L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long accountId;

    private String serviceProvider;
    
    private String imageId;
    
    private String instanceId;
    
    private String instanceName;
    
    private String instanceType;
}
