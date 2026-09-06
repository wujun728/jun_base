package com.geek.system.domain;

import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色和菜单关联 sys_role_menu
 * 
 * @author geek
 */
@Schema(title = "角色和菜单关联")
@Data
@Table("sys_role_menu")
public class SysRoleMenu {
    /** 角色ID */
    @Schema(title = "角色ID")
    private Long roleId;

    /** 菜单ID */
    @Schema(title = "菜单ID")
    private Long menuId;

}
