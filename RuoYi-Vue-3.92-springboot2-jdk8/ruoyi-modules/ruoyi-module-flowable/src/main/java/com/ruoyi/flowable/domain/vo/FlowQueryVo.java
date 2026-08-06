package com.ruoyi.flowable.domain.vo;

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
public class FlowQueryVo {

    @Schema(title = "流程名称")
    private String name;

    @Schema(title = "开始时间")
    private String startTime;

    @Schema(title = "结束时间")
    private String endTime;

    @Schema(title = "当前页码")
    private Integer pageNum;

    @Schema(title = "每页条数")
    private Integer pageSize;


}
