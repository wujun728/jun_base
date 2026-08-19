package com.ruoyi.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 知识库单栏对象 knowledge_base_line
 *
 * @author 欧阳日峰
 * @date 2025-06-25
 */
public class KnowledgeBaseLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 知识库表单栏 */
    private Long id;

    /** 知识库单栏名称 */
    @Excel(name = "知识库单栏名称")
    private String name;

    /** 知识库ID */
    @Excel(name = "知识库ID")
    private Long knowledgeBaseId;

    /** 知识库内容 */
    @Excel(name = "知识库内容")
    private String context;

    /** 状态 */
    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setKnowledgeBaseId(Long knowledgeBaseId)
    {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public Long getKnowledgeBaseId()
    {
        return knowledgeBaseId;
    }
    public void setContext(String context)
    {
        this.context = context;
    }

    public String getContext()
    {
        return context;
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
                .append("id", getId())
                .append("name", getName())
                .append("knowledgeBaseId", getKnowledgeBaseId())
                .append("context", getContext())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("status", getStatus())
                .toString();
    }
}
