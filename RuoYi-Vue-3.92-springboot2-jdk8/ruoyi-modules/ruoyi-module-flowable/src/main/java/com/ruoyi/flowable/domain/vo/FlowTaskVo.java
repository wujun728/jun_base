package com.ruoyi.flowable.domain.vo;

import java.util.List;
import java.util.Map;

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
public class FlowTaskVo {

    @ApiModelProperty("浠诲姟Id")
    private String taskId;

    @ApiModelProperty("鐢ㄦ埛Id")
    private String userId;

    @ApiModelProperty("浠诲姟鎰忚")
    private String comment;

    @ApiModelProperty("娴佺▼瀹炰緥Id")
    private String instanceId;

    @ApiModelProperty("鑺傜偣")
    private String targetKey;

    private String deploymentId;
    @ApiModelProperty("娴佺▼鐜妭瀹氫箟ID")
    private String defId;

    @ApiModelProperty("瀛愭墽琛屾祦ID")
    private String currentChildExecutionId;

    @ApiModelProperty("瀛愭墽琛屾祦鏄惁宸叉墽琛?)
    private Boolean flag;

    @ApiModelProperty("娴佺▼鍙橀噺淇℃伅")
    private Map<String, Object> variables;

    @ApiModelProperty("瀹℃壒浜?)
    private String assignee;

    @ApiModelProperty("鍊欓€変汉")
    private List<String> candidateUsers;

    @ApiModelProperty("瀹℃壒缁?)
    private List<String> candidateGroups;
}
