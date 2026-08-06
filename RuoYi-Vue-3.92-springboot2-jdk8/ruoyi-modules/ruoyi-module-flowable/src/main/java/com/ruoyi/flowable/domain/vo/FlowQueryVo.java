package com.ruoyi.flowable.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Api;
import lombok.Data;

/**
 * <p>娴佺▼浠诲姟<p>
 *
 * @author Tony
 * @date 2021-04-03
 */
@Data
@Api(tags = "宸ヤ綔娴佷换鍔＄浉鍏?-璇锋眰鍙傛暟")
public class FlowQueryVo {

    @ApiModelProperty("娴佺▼鍚嶇О")
    private String name;

    @ApiModelProperty("寮€濮嬫椂闂?)
    private String startTime;

    @ApiModelProperty("缁撴潫鏃堕棿")
    private String endTime;

    @ApiModelProperty("褰撳墠椤电爜")
    private Integer pageNum;

    @ApiModelProperty("姣忛〉鏉℃暟")
    private Integer pageSize;

}
