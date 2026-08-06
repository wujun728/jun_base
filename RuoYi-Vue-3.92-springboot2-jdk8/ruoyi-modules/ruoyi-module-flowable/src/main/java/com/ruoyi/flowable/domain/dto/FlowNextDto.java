package com.ruoyi.flowable.domain.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 动态人员、组
 * @author Tony
 * @date 2021/4/17 22:59
 */
@Data
public class FlowNextDto implements Serializable {

    /**
     * 审批人类型
     */
    private String type;

    /**
     * 是否需要动态指定任务审批人
     */
    private String dataType;

    /**
     * 流程变量
     */
    private String vars;

}
