package ginyi.system.domain.model.dto;

import ginyi.common.swagger.AddGroup;
import ginyi.common.swagger.UpdateGroup;
import ginyi.system.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("系统通知公告请求参数")
public class NoticeDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 通知的id
     */
    @ApiModelProperty("通知的id")
    @NotNull(groups = UpdateGroup.class, message = "通知id不能为空")
    private Long noticeId;

    /**
     * 通知标题
     */
    @ApiModelProperty("通知标题")
    @Size(max = 50, message = "通知标题长度不能超过50个字符")
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "通知标题不能为空")
    private String title;

    /**
     * 通知的类型（0通知，1公告）
     */
    @ApiModelProperty("通知的类型（0通知，1公告）")
    @NotEmpty(groups = {AddGroup.class, UpdateGroup.class}, message = "通知的类型不能为空")
    private String type;

    /**
     * 通知的内容
     */
    @ApiModelProperty("通知的内容")
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "通知的内容不能为空")
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
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "通知的状态不能为空")
    private String status;
}
