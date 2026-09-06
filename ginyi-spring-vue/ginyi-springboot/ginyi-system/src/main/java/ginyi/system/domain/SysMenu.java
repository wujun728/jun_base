package ginyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author ruoyi
 */
@Data
public class SysMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId
    @ApiModelProperty("菜单ID")
    private Long menuId;

    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String menuName;

    /**
     * 父菜单名称
     */
    @ApiModelProperty("父级菜单名称")
    @TableField(exist = false, select = false)
    private String parentName;

    /**
     * 父菜单ID
     */
    @ApiModelProperty("父级菜单ID")
    private Long parentId;

    /**
     * 路由name，用于路由跳转
     */
    @ApiModelProperty("路由名称")
    private String name;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    private Integer sort;

    /**
     * 路由地址
     */
    @ApiModelProperty("路由地址")
    @TableField(value = "path")
    private String path;

    /**
     * 组件路径
     */
    @ApiModelProperty("组件路径")
    private String component;

    /**
     * 路由参数
     */
    @ApiModelProperty("路由参数")
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    @ApiModelProperty("是否为外链（0是 1否）")
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @ApiModelProperty("是否缓存（0缓存 1不缓存）")
    private String isCache;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @ApiModelProperty("类型（M目录 C菜单 F按钮）")
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    @ApiModelProperty("显示状态（0显示 1隐藏）")
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @ApiModelProperty("菜单状态（0正常 1停用）")
    private String status;

    /**
     * 权限字符串
     */
    @ApiModelProperty("权限字符串")
    private String perms;

    /**
     * 菜单图标
     */
    @ApiModelProperty("菜单图标")
    private String icon;

    /**
     * 子菜单
     */
    @ApiModelProperty("子菜单")
    @TableField(exist = false, select = false)
    private List<SysMenu> children = new ArrayList<SysMenu>();

}
