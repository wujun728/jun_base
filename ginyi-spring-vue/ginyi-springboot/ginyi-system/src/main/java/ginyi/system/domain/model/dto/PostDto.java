package ginyi.system.domain.model.dto;

import ginyi.common.swagger.AddGroup;
import ginyi.common.swagger.UpdateGroup;
import ginyi.system.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("系统岗位参数")
public class PostDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("岗位id")
    @NotNull(groups = UpdateGroup.class, message = "岗位id不能为空")
    private Long postId;

    /**
     * 根据岗位ids获取用户列表专用
     */
    @ApiModelProperty("岗位id（根据岗位ids获取用户列表专用）")
    private List<Long> postIds;

    @ApiModelProperty("岗位编码")
    @NotBlank(groups = {UpdateGroup.class, AddGroup.class}, message = "岗位编码不能为空")
    private String postCode;

    @ApiModelProperty("岗位名称")
    @NotBlank(groups = {UpdateGroup.class, AddGroup.class}, message = "岗位名称不能为空")
    private String postName;

    @ApiModelProperty("岗位排序")
    @NotNull(groups = {UpdateGroup.class, AddGroup.class}, message = "岗位顺序不能为空")
    private Integer sort;

    @ApiModelProperty("状态（0正常 1停用）")
    @Size(min = 0, max = 1, message = "状态不合法")
    @NotBlank(groups = {UpdateGroup.class, AddGroup.class}, message = "岗位状态不能为空")
    private String status;

    @ApiModelProperty("创建时间，开始时间")
    private Date beginTime;

    @ApiModelProperty("创建时间，结束时间")
    private Date endTime;

}
