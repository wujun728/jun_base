package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 单数据对象 form_data
 * 
 * @author ruoyi
 * @date 2025-05-12
 */
@Schema(description = "单数据对象")
public class FormData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 数据ID */
    @Schema(title = "数据ID")
    private Long dataId;

    /** 关联的表单ID */
    @Schema(title = "关联的表单ID")
    @Excel(name = "关联的表单ID")
    private Long formId;

    /** 表单版本（与模板表版本一致） */
    @Schema(title = "表单版本（与模板表版本一致）")
    @Excel(name = "表单版本", readConverterExp = "与=模板表版本一致")
    private String formVersion;

    /** 表单数据内容（JSON格式） */
    @Schema(title = "表单数据内容（JSON格式）")
    @Excel(name = "表单数据内容", readConverterExp = "J=SON格式")
    private String dataContent;

    /** 数据状态（draft, submitted, approved, rejected） */
    @Schema(title = "数据状态（draft, submitted, approved, rejected）")
    @Excel(name = "数据状态", readConverterExp = "d=raft,,s=ubmitted,,a=pproved,,r=ejected")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Schema(title = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /** 表单名称 */
    @Schema(title = "表单名称")
    @Excel(name = "表单名称")
    private String formName;

    /** 表单JSON Schema（vForm配置） */
    @Schema(title = "表单JSON Schema（vForm配置）")
    @Excel(name = "表单JSON Schema", readConverterExp = "v=Form配置")
    private String formSchema;

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Long getDataId() {
        return dataId;
    }

    public String getFormSchema() {
        return formSchema;
    }

    public void setFormSchema(String formSchema) {
        this.formSchema = formSchema;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormVersion(String formVersion) {
        this.formVersion = formVersion;
    }

    public String getFormVersion() {
        return formVersion;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormName() {
        return formName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("dataId", getDataId())
                .append("formId", getFormId())
                .append("formVersion", getFormVersion())
                .append("dataContent", getDataContent())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .append("formName", getFormName())
                .toString();
    }
}
