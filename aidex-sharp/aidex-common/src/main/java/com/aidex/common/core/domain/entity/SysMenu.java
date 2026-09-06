package com.aidex.common.core.domain.entity;

import com.aidex.common.core.domain.BaseTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 菜单权限表 sys_menu
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseTreeEntity<SysMenu>
{
    private static final long serialVersionUID = 1L;

    /** 菜单名称 */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    @NotBlank(message = "菜单编码不能为空")
    @Size(min = 0, max = 50, message = "菜单编码长度不能超过50个字符")
    private String menuCode;

    /** 父菜单名称 */
    private String parentName;

    /** 路由地址 */
    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
    private String path;

    /** 组件路径 */
    @Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
    private String component;

    /** 是否为外链（0是 1否） */
    private String isFrame;

    /** 是否缓存（0缓存 1不缓存） */
    private String isCache;

    /** 类型（M目录 C菜单 F按钮） */
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    /** 显示状态（0显示 1隐藏） */
    private String visible;
    
    /** 菜单状态（0显示 1隐藏） */
    private String status;

    /** 权限字符串 */
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;

    /** 菜单图标 */
    private String icon;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("menuCode", getMenuName())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("treeSort", getTreeSort())
            .append("path", getPath())
            .append("component", getComponent())
            .append("isFrame", getIsFrame())
            .append("IsCache", getIsCache())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("status ", getStatus())
            .append("perms", getPerms())
            .append("icon", getIcon())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
