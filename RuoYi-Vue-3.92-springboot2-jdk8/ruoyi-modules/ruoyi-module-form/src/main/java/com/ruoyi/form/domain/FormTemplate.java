package com.ruoyi.form.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 单模板对象 form_template
 * 
 * @author ruoyi
 * @date 2025-05-12
 */
@Schema(description = "单模板对象")
public class FormTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 表单ID */
    @Schema(title = "表单ID")
    private Long formId;

    /** 表单名称 */
    @Schema(title = "表单名称")
    @Excel(name = "表单名称")
    private String formName;

    /** 表单JSON Schema（vForm配置） */
    @Schema(title = "表单JSON Schema（vForm配置）")
    @Excel(name = "表单JSON Schema", readConverterExp = "v=Form配置")
    private String formSchema;

    /** 表单版本（语义化版本） */
    @Schema(title = "表单版本（语义化版本）")
    @Excel(name = "表单版本", readConverterExp = "语=义化版本")
    private String formVersion;

    /** 发布状态（0: 草稿, 1: 已发布, 2: 已停用） */
    @Schema(title = "发布状态（0: 草稿, 1: 已发布, 2: 已停用）")
    @Excel(name = "发布状态", readConverterExp = "0=:,草=稿,,1=:,已=发布,,2=:,已=停用")
    private String formStatus;

    /** 删除标志（0代表存在 2代表删除） */
    @Schema(title = "删除标志（0代表存在 2代表删除）")
    private String delFlag;
    public void setFormId(Long formId) 
    {
        this.formId = formId;
    }

    public Long getFormId() 
    {
        return formId;
    }


    public void setFormName(String formName) 
    {
        this.formName = formName;
    }

    public String getFormName() 
    {
        return formName;
    }


    public void setFormSchema(String formSchema) 
    {
        this.formSchema = formSchema;
    }

    public String getFormSchema() 
    {
        return formSchema;
    }


    public void setFormVersion(String formVersion) 
    {
        this.formVersion = formVersion;
    }

    public String getFormVersion() 
    {
        return formVersion;
    }


    public void setFormStatus(String formStatus) 
    {
        this.formStatus = formStatus;
    }

    public String getFormStatus() 
    {
        return formStatus;
    }


    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("formId", getFormId())
            .append("formName", getFormName())
            .append("formSchema", getFormSchema())
            .append("formVersion", getFormVersion())
            .append("formStatus", getFormStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
