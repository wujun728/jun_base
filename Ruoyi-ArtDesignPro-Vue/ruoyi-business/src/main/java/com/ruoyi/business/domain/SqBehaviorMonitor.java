package com.ruoyi.business.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ人员/斜井作业监测对象 yn_user / yn_user_xj
 */
public class SqBehaviorMonitor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;
    @Excel(name = "图片")
    private String picture;
    @Excel(name = "视频")
    private String video;
    @Excel(name = "备注")
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "警示时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date warningTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;
    @Excel(name = "指令")
    private String instruction;
    @Excel(name = "检测类型")
    private String type;
    @Excel(name = "设备名称")
    private String name;
    @Excel(name = "设备IP")
    private String ip;
    @Excel(name = "场景号")
    private String scene;
    @Excel(name = "状态")
    private String status;
    @Excel(name = "处理记录")
    private String handle;
    @Excel(name = "所属中段")
    private String section;
    private String beginTime;
    private String endTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getWarningTime() { return warningTime; }
    public void setWarningTime(Date warningTime) { this.warningTime = warningTime; }
    public Date getHandleTime() { return handleTime; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }
    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) { this.instruction = instruction; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getScene() { return scene; }
    public void setScene(String scene) { this.scene = scene; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getHandle() { return handle; }
    public void setHandle(String handle) { this.handle = handle; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
