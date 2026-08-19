package com.ruoyi.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 知识库列表对象 knowledge_base_list
 * 
 * @author 欧阳日峰
 * @date 2025-04-10
 */
public class KnowledgeBaseList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 知识库ID */
    private Long id;

    /** 知识库名称 */
    @Excel(name = "知识库名称")
    private String knowledgeBaseName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setKnowledgeBaseName(String knowledgeBaseName) 
    {
        this.knowledgeBaseName = knowledgeBaseName;
    }

    public String getKnowledgeBaseName() 
    {
        return knowledgeBaseName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("knowledgeBaseName", getKnowledgeBaseName())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
