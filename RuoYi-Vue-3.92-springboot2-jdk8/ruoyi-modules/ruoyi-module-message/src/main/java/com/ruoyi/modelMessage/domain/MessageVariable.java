package com.ruoyi.modelMessage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 变量管理对象 message_variable
 * 
 * @author ruoyi
 * @date 2024-12-31
 */
@Schema(description = "变量管理对象")
public class MessageVariable extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 主键 */
    @Schema(title = "主键")
    private Long variableId;

    /** 变量名称 */
    @Schema(title = "变量名称")
    @Excel(name = "变量名称")
    private String variableName;

    /** 变量类型 */
    @Schema(title = "变量类型")
    @Excel(name = "变量类型")
    private String variableType;

    /** 变量内容 */
    @Schema(title = "变量内容")
    @Excel(name = "变量内容")
    private String variableContent;
    public void setVariableId(Long variableId) 
    {
        this.variableId = variableId;
    }

    public MessageVariable(Long variableId, String variableName, String variableType, String variableContent) {
        this.variableId = variableId;
        this.variableName = variableName;
        this.variableType = variableType;
        this.variableContent = variableContent;
    }

    public Long getVariableId() 
    {
        return variableId;
    }


    public void setVariableName(String variableName) 
    {
        this.variableName = variableName;
    }

    public String getVariableName() 
    {
        return variableName;
    }


    public void setVariableType(String variableLength) 
    {
        this.variableType = variableLength;
    }

    public String getVariableType() 
    {
        return variableType;
    }


    public void setVariableContent(String variableContent) 
    {
        this.variableContent = variableContent;
    }

    public String getVariableContent() 
    {
        return variableContent;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("variableId", getVariableId())
            .append("variableName", getVariableName())
            .append("variableType", getVariableType())
            .append("variableContent", getVariableContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
