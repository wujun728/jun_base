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

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("系统菜单请求参数")
public class MenuDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单id")
    @NotNull(groups = UpdateGroup.class, message = "菜单id不能为空")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "菜单名称不能为空")
    private String menuName;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("排序（1升序，0降序）")
    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "排序不能为空")
    private Integer sort = 1;

    @ApiModelProperty("路由名称")
    @Size(max = 50, message = "路由名称长度不得超过50个字符")
    private String name;

    @ApiModelProperty("路由地址")
    @Size(max = 200, message = "路由地址长度不得超过200个字符")
    private String path;

    @ApiModelProperty("组件路径")
    @Size(max = 200, message = "组件路径长度不得超过200个字符")
    private String component;

    @ApiModelProperty("类型（M目录 C菜单 F按钮）")
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "菜单类型不能为空")
    private String menuType;

    @ApiModelProperty("显示状态（0显示 1隐藏）")
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "显示状态不能为空")
    private String visible;

    @ApiModelProperty("菜单状态（0正常 1停用）")
    @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "菜单状态不能为空")
    private String status;

    @ApiModelProperty("路由参数")
    private String query;

    @ApiModelProperty("是否为外链（0是 1否）")
    private String isFrame;

    @ApiModelProperty("是否缓存（0缓存 1不缓存）")
    private String isCache;

    @ApiModelProperty("权限字符串")
    private String perms;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("过滤按钮（0过滤 1不过滤）")
    private String filterButton;

}
