package com.ruoyi.flowable.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>可退回节点<p>
 *
 * @author tony
 * @date 2022-04-23 11:01:52
 */
@Data
@Schema(description = "可退回节点")
public class ReturnTaskNodeVo {

    @Schema(title = "任务Id")
    private String id;

    @Schema(title = "用户Id")
    private String name;

}
