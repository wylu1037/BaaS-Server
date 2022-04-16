package com.zkjg.baas.task.bean.req;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wylu
 * @date 2022/4/16 下午 12:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyJobReq implements Serializable {

    private static final long serialVersionUID = -9061505463115111993L;

    @NotNull(message = "the job id cannot be null")
    private Long id;

    @NotNull(message = "the job cron cannot be empty")
    private String cron;
}
