package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ RFID阅读器对象 yn_rfid_ydq
 */
public class SqRfidReader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;

    @Excel(name = "位置")
    private String location;

    @Excel(name = "状态")
    private String status;

    @Excel(name = "备注")
    private String mark;

    @Excel(name = "名称")
    private String name;

    @Excel(name = "IP地址")
    private String ip;

    @Excel(name = "用户名")
    private String username;

    private String psw;

    @Excel(name = "端口")
    private String port;

    private String lUserid;

    @Excel(name = "关联溜井")
    private String tname;

    @Excel(name = "关联溜井")
    private String tnameLabel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMark() { return mark; }
    public void setMark(String mark) { this.mark = mark; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPsw() { return psw; }
    public void setPsw(String psw) { this.psw = psw; }
    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }
    public String getLUserid() { return lUserid; }
    public void setLUserid(String lUserid) { this.lUserid = lUserid; }
    public String getTname() { return tname; }
    public void setTname(String tname) { this.tname = tname; }
    public String getTnameLabel() { return tnameLabel; }
    public void setTnameLabel(String tnameLabel) { this.tnameLabel = tnameLabel; }
}
