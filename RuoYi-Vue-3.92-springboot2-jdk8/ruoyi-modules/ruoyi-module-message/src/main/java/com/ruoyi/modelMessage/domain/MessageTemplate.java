package com.ruoyi.modelMessage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 模版管理对象 message_template
 * 
 * @author ruoyi
 * @date 2024-12-31
 */
@Schema(description = "模版管理对象")
public class MessageTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 主键 */
    @Schema(title = "主键")
    private Long templateId;

    /** 模版名称 */
    @Schema(title = "模版名称")
    @Excel(name = "模版名称")
    private String templateName;

    /** 模版CODE */
    @Schema(title = "模版CODE")
    @Excel(name = "模版CODE")
    private String templateCode;

    /** 模版类型 */
    @Schema(title = "模版类型")
    @Excel(name = "模版类型")
    private String templateType;

    /** 模版内容 */
    @Schema(title = "模版内容")
    @Excel(name = "模版内容")
    private String templateContent;

    /** 变量属性 */
    @Schema(title = "变量属性")
    @Excel(name = "变量属性")
    private String templateVariable;
    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }


    public void setTemplateName(String templateName) 
    {
        this.templateName = templateName;
    }

    public String getTemplateName() 
    {
        return templateName;
    }


    public void setTemplateCode(String templateCode) 
    {
        this.templateCode = templateCode;
    }

    public String getTemplateCode() 
    {
        return templateCode;
    }


    public void setTemplateType(String templateType) 
    {
        this.templateType = templateType;
    }

    public String getTemplateType() 
    {
        return templateType;
    }


    public void setTemplateContent(String templateContent) 
    {
        this.templateContent = templateContent;
    }

    public String getTemplateContent() 
    {
        return templateContent;
    }


    public void setTemplateVariable(String templateVariable) 
    {
        this.templateVariable = templateVariable;
    }

    public String getTemplateVariable() 
    {
        return templateVariable;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("templateId", getTemplateId())
            .append("templateName", getTemplateName())
            .append("templateCode", getTemplateCode())
            .append("templateType", getTemplateType())
            .append("templateContent", getTemplateContent())
            .append("templateVariable", getTemplateVariable())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
