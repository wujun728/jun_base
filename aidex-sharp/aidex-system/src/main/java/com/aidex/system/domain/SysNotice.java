package com.aidex.system.domain;

import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.utils.log.annotation.FieldRemark;
import com.aidex.common.utils.log.annotation.LogField;
import com.aidex.common.xss.Xss;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 通知公告表 sys_notice
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNotice extends BaseEntity<SysNotice>
{
    private static final long serialVersionUID = 1L;

    /** 公告标题 */
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    @LogField
    @FieldRemark(field = "noticeTitle", name = "公告标题")
    @Xss(message = "公告标题不能包含脚本字符")
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    @FieldRemark(field = "noticeType", name = "公告类型")
    private String noticeType;

    /** 公告内容 */
    private String noticeContent;
    private String noticeContentHtml;

    /** 公告状态（0正常 1关闭） */
    @FieldRemark(field = "status", name = "公告状态")
    private String status;

    private String createByName;
    private String userId;
    private String isRead;//消息是否已读

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("noticeTitle", getNoticeTitle())
            .append("noticeType", getNoticeType())
            .append("noticeContent", getNoticeContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
