package com.ruoyi.flowable.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Api;
import lombok.Data;

/**
 * <p>鍙€€鍥炶妭鐐?p>
 *
 * @author tony
 * @date 2022-04-23 11:01:52
 */
@Data
@Api(tags = "鍙€€鍥炶妭鐐?)
public class ReturnTaskNodeVo {

    @ApiModelProperty("浠诲姟Id")
    private String id;

    @ApiModelProperty("鐢ㄦ埛Id")
    private String name;

}
