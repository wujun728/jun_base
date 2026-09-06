package ginyi.system.domain.model.vo;

import ginyi.system.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("系统部门返回数据")
public class DeptVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("父级id")
    private Long parentId;

    private String ancestors;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("显示顺序")
    private Integer sort;

    @ApiModelProperty("部门负责人")
    private String leader;

    @ApiModelProperty("部门负责人电话")
    private String phone;

    @ApiModelProperty("部门负责人邮箱")
    private String email;

    @ApiModelProperty("状态，0正常，1停用")
    private String status;

}
