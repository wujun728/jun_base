package com.geek.system.domain;

import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色和部门关联 sys_role_dept
 * 
 * @author geek
 */
@Schema(title = "角色和部门关联")
@Table("sys_role_dept")
@Data
public class SysRoleDept {
    /** 角色ID */
    @Schema(title = "角色ID")
    private Long roleId;

    /** 部门ID */
    @Schema(title = "部门ID")
    private Long deptId;
}
