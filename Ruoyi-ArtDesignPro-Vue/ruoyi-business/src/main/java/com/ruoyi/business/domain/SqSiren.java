package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ电子围栏对象 yn_siren
 */
public class SqSiren extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;
    @Excel(name = "位置")
    private String location;
    @Excel(name = "IP地址")
    private String ip;
    @Excel(name = "是否发送")
    private String flag;
    @Excel(name = "备注")
    private String mark;
    @Excel(name = "关联摄像头")
    private String cameras;
    @Excel(name = "音量")
    private String volume;
    @Excel(name = "报警")
    private String isWarning;
    @Excel(name = "设备名称")
    private String name;
    @Excel(name = "状态")
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
    public String getMark() { return mark; }
    public void setMark(String mark) { this.mark = mark; }
    public String getCameras() { return cameras; }
    public void setCameras(String cameras) { this.cameras = cameras; }
    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }
    public String getIsWarning() { return isWarning; }
    public void setIsWarning(String isWarning) { this.isWarning = isWarning; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
