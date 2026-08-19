package com.ruoyi.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 聊天会话详情对象 chat_session_detail
 *
 * @author ruoyi
 * @date 2025-03-04
 */
public class ChatSessionDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 聊天会话详细ID */
    private Long id;

    /** 聊天会话ID */
    @Excel(name = "聊天会话ID")
    private Long sessionId;

    /** 会话角色 user:用户 ai:AI */
    @Excel(name = "会话角色 user:用户 ai:AI")
    private String role;

    /** 聊天内容 */
    @Excel(name = "聊天内容")
    private String content;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setSessionId(Long sessionId)
    {
        this.sessionId = sessionId;
    }

    public Long getSessionId()
    {
        return sessionId;
    }
    public void setRole(String role)
    {
        this.role = role;
    }

    public String getRole()
    {
        return role;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sessionId", getSessionId())
            .append("role", getRole())
            .append("content", getContent())
            .append("createTime", getCreateTime())
            .toString();
    }
}
