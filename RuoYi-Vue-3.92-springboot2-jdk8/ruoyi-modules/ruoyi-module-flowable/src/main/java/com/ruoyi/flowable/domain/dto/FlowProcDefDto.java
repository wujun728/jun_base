package com.ruoyi.flowable.domain.dto;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>娴佺▼瀹氫箟<p>
 *
 * @author Tony
 * @date 2021-04-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Api(tags = "娴佺▼瀹氫箟")
public class FlowProcDefDto implements Serializable {

    @ApiModelProperty("娴佺▼id")
    private String id;

    @ApiModelProperty("娴佺▼鍚嶇О")
    private String name;

    @ApiModelProperty("娴佺▼key")
    private String flowKey;

    @ApiModelProperty("娴佺▼鍒嗙被")
    private String category;

    @ApiModelProperty("閰嶇疆琛ㄥ崟鍚嶇О")
    private String formName;

    @ApiModelProperty("閰嶇疆琛ㄥ崟id")
    private Long formId;

    @ApiModelProperty("鐗堟湰")
    private int version;

    @ApiModelProperty("閮ㄧ讲ID")
    private String deploymentId;

    @ApiModelProperty("娴佺▼瀹氫箟鐘舵€? 1:婵€娲?, 2:涓")
    private int suspensionState;

    @ApiModelProperty("閮ㄧ讲鏃堕棿")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deploymentTime;

}
