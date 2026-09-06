package com.ruoyi.business.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业务示例对象 business_demo
 */
public class BusinessDemo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 示例ID */
    @Excel(name = "示例ID")
    private Long demoId;

    /** 示例名称 */
    @Excel(name = "示例名称")
    private String demoName;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getDemoId()
    {
        return demoId;
    }

    public void setDemoId(Long demoId)
    {
        this.demoId = demoId;
    }

    @NotBlank(message = "示例名称不能为空")
    @Size(max = 100, message = "示例名称不能超过100个字符")
    public String getDemoName()
    {
        return demoName;
    }

    public void setDemoName(String demoName)
    {
        this.demoName = demoName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
