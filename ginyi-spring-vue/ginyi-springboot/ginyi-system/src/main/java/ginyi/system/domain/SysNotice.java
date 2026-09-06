package ginyi.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysNotice extends BaseEntity {

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
     * 通知的用户
     */
    @ApiModelProperty("通知的用户")
    private List<Long> userIds;

    /**
     * 已读的用户
     */
    @ApiModelProperty("已读的用户")
    private List<Long> userReadIds;

    /**
     * 通知的状态（0正常，1关闭）
     */
    @ApiModelProperty("通知的状态（0正常，1关闭）")
    private String status;
}
