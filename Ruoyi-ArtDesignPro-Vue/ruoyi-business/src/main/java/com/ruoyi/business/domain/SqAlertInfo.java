package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ报警类别对象 a_alert_info
 */
public class SqAlertInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;
    @Excel(name = "声音文件")
    private String voice;
    @Excel(name = "对应溜井")
    private String name;
    @Excel(name = "检测类型")
    private Integer types;
    @Excel(name = "报警状态")
    private String status;
    private String createUser;
    private String createDept;
    private String updateUser;
    private String isDeleted;
    @Excel(name = "报警等级")
    private String level;
    @Excel(name = "报警语音文字")
    private String text;
    @Excel(name = "是否播报")
    private String isopen;
    @Excel(name = "是否推送")
    private String isSend;
    @Excel(name = "推送人员")
    private String sendUser;
    @Excel(name = "接收日志")
    private String isReceive;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVoice() { return voice; }
    public void setVoice(String voice) { this.voice = voice; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getTypes() { return types; }
    public void setTypes(Integer types) { this.types = types; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getCreateDept() { return createDept; }
    public void setCreateDept(String createDept) { this.createDept = createDept; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getIsDeleted() { return isDeleted; }
    public void setIsDeleted(String isDeleted) { this.isDeleted = isDeleted; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getIsopen() { return isopen; }
    public void setIsopen(String isopen) { this.isopen = isopen; }
    public String getIsSend() { return isSend; }
    public void setIsSend(String isSend) { this.isSend = isSend; }
    public String getSendUser() { return sendUser; }
    public void setSendUser(String sendUser) { this.sendUser = sendUser; }
    public String getIsReceive() { return isReceive; }
    public void setIsReceive(String isReceive) { this.isReceive = isReceive; }
}
