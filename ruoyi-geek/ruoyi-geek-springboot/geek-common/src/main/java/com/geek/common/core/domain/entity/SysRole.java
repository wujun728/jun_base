package com.geek.common.core.domain.entity;

import java.util.List;
import java.util.Set;

import com.geek.common.annotation.Excel;
import com.geek.common.annotation.Excel.ColumnType;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色表 sys_role
 *
 * @author geek
 */
@Table("sys_role")
@Schema(title = "角色表")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Id
    @Schema(title = "角色ID")
    @Excel(name = "角色序号", cellType = ColumnType.NUMERIC)
    private Long roleId;

    /** 角色名称 */
    @Schema(title = "角色名称")
    @Excel(name = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    /** 角色权限 */
    @Schema(title = "角色权限")
    @Excel(name = "角色权限")
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    private String roleKey;

    /** 角色排序 */
    @Schema(title = "角色排序")
    @Excel(name = "角色排序")
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;

    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限） */
    @Schema(title = "数据范围", description = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    @Excel(name = "数据范围", readConverterExp = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private String dataScope;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    @Schema(title = "菜单树选择项是否关联显示", description = "0：父子不互相关联显示 1：父子互相关联显示")
    private boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    @Schema(title = "部门树选择项是否关联显示", description = "0：父子不互相关联显示 1：父子互相关联显示 ")
    private boolean deptCheckStrictly;

    /** 角色状态（0正常 1停用） */
    @Schema(title = "角色状态", description = "0正常 1停用")
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Schema(title = "删除标志", description = "0代表存在 2代表删除")
    private Integer delFlag;

    /** 用户是否存在此角色标识 默认不存在 */
    @Schema(title = "用户是否存在此角色标识", description = "默认不存在")
    @Column(ignore = true)
    private boolean flag = false;

    /** 菜单组 */
    @Schema(title = "菜单组")
    @RelationManyToMany(joinTable = "sys_role_menu", selfField = "roleId", joinSelfColumn = "role_id", targetField = "menuId", joinTargetColumn = "menu_id", targetTable = "sys_menu", valueField = "menuId")
    private List<Long> menuIds;

    /** 部门组（数据权限） */
    @Schema(title = "部门组", description = "数据权限")
    @RelationManyToMany(joinTable = "sys_role_dept", selfField = "roleId", joinSelfColumn = "role_id", targetField = "deptId", joinTargetColumn = "dept_id", targetTable = "sys_dept", valueField = "deptId")
    private List<Long> deptIds;

    /** 角色菜单权限 */
    @Schema(title = "角色菜单权限")
    @Column(ignore = true)
    private Set<String> permissions;

    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

    public SysRole() {

    }

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }
}
