package com.aidex.system.domain;

import com.aidex.common.annotation.Excel;
import com.aidex.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

/**
 * 通知公告用户阅读对象 sys_notice_user_read
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNoticeUserRead extends BaseEntity<SysNoticeUserRead>
{
    private static final long serialVersionUID = 1L;

    /** 通知公告ID */
    @Excel(name = "通知公告ID")
    @NotBlank(message = "通知公告ID不允许为空")
    private String noticeId;

    /** 系统用户id */
    @Excel(name = "系统用户id")
    @NotBlank(message = "系统用户id不允许为空")
    private String userId;

    /** 是否已读，仅在制定通知接收人时使用 */
    @Excel(name = "是否已读，仅在制定通知接收人时使用")
    private String isRead;

    /** 状态 */
    @Excel(name = "状态")
    @NotBlank(message = "状态不允许为空")
    private String status;

    public void setNoticeId(String noticeId) 
    {
        this.noticeId = noticeId;
    }

    public String getNoticeId() 
    {
        return noticeId;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }

    public void setIsRead(String isRead) 
    {
        this.isRead = isRead;
    }

    public String getIsRead() 
    {
        return isRead;
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
            .append("noticeId", getNoticeId())
            .append("userId", getUserId())
            .append("isRead", getIsRead())
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
