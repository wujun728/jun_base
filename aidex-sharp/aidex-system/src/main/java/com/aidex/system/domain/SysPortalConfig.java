package com.aidex.system.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import lombok.Data;
import com.aidex.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.aidex.common.annotation.Excel;
/**
 * 多栏目门户配置对象 sys_portal_config
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPortalConfig extends BaseEntity<SysPortalConfig>
{
    private static final long serialVersionUID = 1L;

    /** 小页名称 */
    @Excel(name = "名称")
    @NotBlank(message = "名称不允许为空")
    private String name;

    /** 小页编码 */
    @Excel(name = "编码")
    private String code;

    /** 应用范围 */
    @Excel(name = "应用范围", dictType = "sys_yes_no")
    private String applicationRange;

    /** 是否默认 */
    @Excel(name = "是否默认", dictType = "sys_yes_no")
    private String isDefault;

    /** 资源 */
    @Excel(name = "资源")
    private String resourceId;
    private String resourceName;

    /** 系统定义ID区分系统定义和用户自定义 */
    private String systemDefinedId;

    /** 配置信息 */
    private String content;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;
    /**
     * 保存方式sys系统定义user用户自定义
     */
    private String saveType;

    /** 状态 */
    private String status;

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setApplicationRange(String applicationRange) 
    {
        this.applicationRange = applicationRange;
    }

    public String getApplicationRange() 
    {
        return applicationRange;
    }

    public void setIsDefault(String isDefault) 
    {
        this.isDefault = isDefault;
    }

    public String getIsDefault() 
    {
        return isDefault;
    }

    public void setResourceId(String resourceId) 
    {
        this.resourceId = resourceId;
    }

    public String getResourceId() 
    {
        return resourceId;
    }

    public void setSystemDefinedId(String systemDefinedId) 
    {
        this.systemDefinedId = systemDefinedId;
    }

    public String getSystemDefinedId() 
    {
        return systemDefinedId;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
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
            .append("name", getName())
            .append("code", getCode())
            .append("applicationRange", getApplicationRange())
            .append("isDefault", getIsDefault())
            .append("resourceId", getResourceId())
            .append("systemDefinedId", getSystemDefinedId())
            .append("content", getContent())
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
