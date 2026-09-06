package com.aidex.system.domain;

import com.aidex.common.annotation.Excel;
import com.aidex.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

/**
 * 用户主题信息记录对象 sys_theme_config
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-04-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysThemeConfig extends BaseEntity<SysThemeConfig>
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID")
    @NotBlank(message = "用户ID不允许为空")
    private String userId;

    /** 对应主题JSON */
    @Excel(name = "对应主题JSON")
    private String themeObj;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 状态（0正常 1删除 2停用） */
    @Excel(name = "状态", dictType = "sys_normal_disable")
    @NotBlank(message = "状态（0正常 1删除 2停用）不允许为空")
    private String status;

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }

    public void setThemeObj(String themeObj) 
    {
        this.themeObj = themeObj;
    }

    public String getThemeObj() 
    {
        return themeObj;
    }

    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
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
            .append("themeObj", getThemeObj())
            .append("sort", getSort())
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
