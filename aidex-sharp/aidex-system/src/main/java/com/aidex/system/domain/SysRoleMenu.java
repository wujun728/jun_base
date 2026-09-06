package com.aidex.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 角色和菜单关联 sys_role_menu
 * 
 * @author ruoyi
 */
@Data
public class SysRoleMenu
{
    /** 角色ID */
    private String roleId;
    
    /** 菜单ID */
    private String menuId;
    /**
     * 角色对应的菜单集合
     */
    private List<String> menuIdList;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("menuId", getMenuId())
            .toString();
    }
}
