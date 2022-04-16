package com.zkjg.baas.task.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wylu
 * @date 2022/4/15 下午 10:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "job_entity")
public class JobEntity implements Serializable {

    private static final long serialVersionUID = 5465808653036518727L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String groupName;

    private Long operatorId;

    private Long chainId;

    private Long nodeId;

    private String cron;

    private String params;

    private Integer enableFlag;

    private String remark;
    
    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
}
