package com.aidex.system.domain;

import com.aidex.common.annotation.Excel;
import com.aidex.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

/**
 * 常用菜单配置对象 sys_common_use_menu
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysCommonUseMenu extends BaseEntity<SysCommonUseMenu>
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    @Excel(name = "path")
    private String path;
    @Excel(name = "icon")
    private String icon;
    @Excel(name = "title")
    private String title;

    /** 对应菜单JSON */
    @Excel(name = "对应菜单JSON")
    private String menuObj;

    /** 状态（0正常 1删除 2停用） */
    @Excel(name = "状态", dictType = "sys_normal_disable")
    @NotBlank(message = "状态（0正常 1删除 2停用）不允许为空")
    private String status;
    /** 排序 */
    private Integer sort;

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }

    public void setMenuObj(String menuObj) 
    {
        this.menuObj = menuObj;
    }

    public String getMenuObj() 
    {
        return menuObj;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("path", getPath())
            .append("icon", getIcon())
            .append("title", getTitle())
            .append("menuObj", getMenuObj())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("id", getId())
            .append("createBy", getCreateBy())
            .append("createDept", getCreateDept())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("updateIp", getUpdateIp())
            .append("version", getVersion())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
