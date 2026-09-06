package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ设备运行记录对象 yn_camera_record
 */
public class SqDeviceRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;

    @Excel(name = "通道ID")
    private String channelId;

    @Excel(name = "设备名称")
    private String lname;

    @Excel(name = "运行状态")
    private String isOpen;

    @Excel(name = "设备IP")
    private String carmip;

    @Excel(name = "设备类型")
    private String type;

    private String beginTime;

    private String endTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    public String getIsOpen() { return isOpen; }
    public void setIsOpen(String isOpen) { this.isOpen = isOpen; }
    public String getCarmip() { return carmip; }
    public void setCarmip(String carmip) { this.carmip = carmip; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
