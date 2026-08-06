package com.ruoyi.modelMessage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 消息管理对象 message_system
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
@Schema(description = "消息管理对象")
public class MessageSystem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(title = "主键")
    private Long messageId;

    /** 标题 */
    @Schema(title = "标题")
    @Excel(name = "标题")
    private String messageTitle;

    /** 消息内容 */
    @Schema(title = "消息内容")
    @Excel(name = "消息内容")
    private String messageContent;

    /** 消息状态(0未读 1已读) */
    @Schema(title = "消息状态(0未读 1已读)")
    @Excel(name = "消息状态(0未读 1已读)")
    private String messageStatus;

    /** 消息类型 */
    @Schema(title = "消息类型")
    @Excel(name = "消息类型")
    private String messageType;

     /** 消息类型 */
     @Schema(title = "接收人")
     @Excel(name = "接收人")
     private String messageRecipient; 

    /** 发送方式(0平台 1手机号 2 邮箱) */
    @Schema(title = "发送方式(0平台 1手机号 2 邮箱)")
    @Excel(name = "发送方式(0平台 1手机号 2 邮箱)")
    private String sendMode;

    /** 号码 */
    @Schema(title = "号码")
    @Excel(name = "号码")
    private String code;

    public String getSendMode() {
        return sendMode;
    }

    public void setSendMode(String sendMode) {
        this.sendMode = sendMode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }


    public void setMessageTitle(String messageTitle) 
    {
        this.messageTitle = messageTitle;
    }

    public String getMessageTitle() 
    {
        return messageTitle;
    }


    public void setMessageContent(String messageContent) 
    {
        this.messageContent = messageContent;
    }

    public String getMessageContent() 
    {
        return messageContent;
    }


    public void setMessageStatus(String messageStatus) 
    {
        this.messageStatus = messageStatus;
    }

    public String getMessageStatus() 
    {
        return messageStatus;
    }


    public void setMessageType(String messageType) 
    {
        this.messageType = messageType;
    }

    public String getMessageType() 
    {
        return messageType;
    }

    public String getMessageRecipient() {
        return messageRecipient;
    }

    public void setMessageRecipient(String messageRecipient) {
        this.messageRecipient = messageRecipient;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("messageId", getMessageId())
            .append("messageTitle", getMessageTitle())
            .append("messageContent", getMessageContent())
            .append("messageStatus", getMessageStatus())
            .append("messageType", getMessageType())
            .append("messageRecipient", getMessageRecipient())
            .append("sendMode", getSendMode())
            .append("code", getCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

    
}
