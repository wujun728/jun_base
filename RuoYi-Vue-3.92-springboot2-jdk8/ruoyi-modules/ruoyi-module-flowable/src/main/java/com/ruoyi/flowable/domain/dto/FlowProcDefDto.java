package com.ruoyi.flowable.domain.dto;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>流程定义<p>
 *
 * @author Tony
 * @date 2021-04-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "流程定义")
public class FlowProcDefDto implements Serializable {

    @Schema(title = "流程id")
    private String id;

    @Schema(title = "流程名称")
    private String name;

    @Schema(title = "流程key")
    private String flowKey;

    @Schema(title = "流程分类")
    private String category;

    @Schema(title = "配置表单名称")
    private String formName;

    @Schema(title = "配置表单id")
    private Long formId;

    @Schema(title = "版本")
    private int version;

    @Schema(title = "部署ID")
    private String deploymentId;

    @Schema(title = "流程定义状态: 1:激活 , 2:中止")
    private int suspensionState;

    @Schema(title = "部署时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deploymentTime;


}
