package com.ruoyi.flowable.domain.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 宸ヤ綔娴佷换鍔?
 * <p>
 *
 * @author Tony
 * @date 2021-04-03
 */
@Getter
@Setter
@Api(tags = "宸ヤ綔娴佷换鍔＄浉鍏?杩斿洖鍙傛暟")
public class FlowTaskDto implements Serializable {

    @ApiModelProperty("浠诲姟缂栧彿")
    private String taskId;

    @ApiModelProperty("浠诲姟鎵ц缂栧彿")
    private String executionId;

    @ApiModelProperty("浠诲姟鍚嶇О")
    private String taskName;

    @ApiModelProperty("浠诲姟Key")
    private String taskDefKey;

    @ApiModelProperty("浠诲姟鎵ц浜篒d")
    private Long assigneeId;

    @ApiModelProperty("閮ㄩ棬鍚嶇О")
    private String deptName;

    @ApiModelProperty("娴佺▼鍙戣捣浜洪儴闂ㄥ悕绉?)
    private String startDeptName;

    @ApiModelProperty("浠诲姟鎵ц浜哄悕绉?)
    private String assigneeName;
    @ApiModelProperty("浠诲姟鎵ц浜洪儴闂?)
    private String assigneeDeptName;;

    @ApiModelProperty("娴佺▼鍙戣捣浜篒d")
    private String startUserId;

    @ApiModelProperty("娴佺▼鍙戣捣浜哄悕绉?)
    private String startUserName;

    @ApiModelProperty("娴佺▼绫诲瀷")
    private String category;

    @ApiModelProperty("娴佺▼鍙橀噺淇℃伅")
    private Object variables;

    @ApiModelProperty("灞€閮ㄥ彉閲忎俊鎭?)
    private Object taskLocalVars;

    @ApiModelProperty("娴佺▼閮ㄧ讲缂栧彿")
    private String deployId;

    @ApiModelProperty("娴佺▼ID")
    private String procDefId;

    @ApiModelProperty("娴佺▼key")
    private String procDefKey;

    @ApiModelProperty("娴佺▼瀹氫箟鍚嶇О")
    private String procDefName;

    @ApiModelProperty("娴佺▼瀹氫箟鍐呯疆浣跨敤鐗堟湰")
    private int procDefVersion;

    @ApiModelProperty("娴佺▼瀹炰緥ID")
    private String procInsId;

    @ApiModelProperty("鍘嗗彶娴佺▼瀹炰緥ID")
    private String hisProcInsId;

    @ApiModelProperty("浠诲姟鑰楁椂")
    private String duration;

    @ApiModelProperty("浠诲姟鎰忚")
    private FlowCommentDto comment;

    @ApiModelProperty("鍊欓€夋墽琛屼汉")
    private String candidate;

    @ApiModelProperty("浠诲姟鍒涘缓鏃堕棿")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("浠诲姟瀹屾垚鏃堕棿")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

}
