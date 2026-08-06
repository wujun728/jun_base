package com.ruoyi.flowable.domain.vo;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>流程任务<p>
 *
 * @author Tony
 * @date 2021-04-03
 */
@Data
@Schema(description = "工作流任务相关--请求参数")
public class FlowTaskVo {

    @Schema(title = "任务Id")
    private String taskId;

    @Schema(title = "用户Id")
    private String userId;

    @Schema(title = "任务意见")
    private String comment;

    @Schema(title = "流程实例Id")
    private String instanceId;

    @Schema(title = "节点")
    private String targetKey;

    private String deploymentId;
    @Schema(title = "流程环节定义ID")
    private String defId;

    @Schema(title = "子执行流ID")
    private String currentChildExecutionId;

    @Schema(title = "子执行流是否已执行")
    private Boolean flag;

    @Schema(title = "流程变量信息")
    private Map<String, Object> variables;

    @Schema(title = "审批人")
    private String assignee;

    @Schema(title = "候选人")
    private List<String> candidateUsers;

    @Schema(title = "审批组")
    private List<String> candidateGroups;
}
