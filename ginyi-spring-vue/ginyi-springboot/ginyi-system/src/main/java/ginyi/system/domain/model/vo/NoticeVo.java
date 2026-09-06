package ginyi.system.domain.model.vo;

import ginyi.system.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("系统通知公告返回数据")
public class NoticeVo extends BaseEntity {
    /**
     * 通知的id
     */
    @ApiModelProperty("通知的id")
    private Long noticeId;

    /**
     * 通知标题
     */
    @ApiModelProperty("通知标题")
    private String title;

    /**
     * 通知的类型（0通知，1公告）
     */
    @ApiModelProperty("通知的类型（0通知，1公告）")
    private String type;

    /**
     * 通知的内容
     */
    @ApiModelProperty("通知的内容")
    private String content;

    /**
     * 是否已读
     */
    @ApiModelProperty("是否已读")
    private boolean haveRead;

    /**
     * 通知的状态（0正常，1关闭）
     */
    @ApiModelProperty("通知的状态（0正常，1关闭）")
    private String status;
}
