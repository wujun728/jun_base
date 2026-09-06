package ginyi.system.domain.model.vo;

import ginyi.system.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("系统角色返回数据")
public class RoleVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色权限字符串")
    private String roleKey;

    @ApiModelProperty("角色排序")
    private Integer sort;

    @ApiModelProperty("角色状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("角色菜单权限")
    private List<Long> permissions;
}
