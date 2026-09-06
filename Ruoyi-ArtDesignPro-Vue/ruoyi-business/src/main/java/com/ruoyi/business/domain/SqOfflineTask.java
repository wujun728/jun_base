package com.ruoyi.business.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ溜井离线任务对象 yn_task
 */
public class SqOfflineTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;
    @Excel(name = "任务名称")
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @Excel(name = "溜井")
    private String lname;
    @Excel(name = "溜井名称")
    private String chuteName;
    @Excel(name = "任务通道号")
    private String channel;
    @Excel(name = "运行状态")
    private String status;
    private String beginTime;
    private String searchEndTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    public String getChuteName() { return chuteName; }
    public void setChuteName(String chuteName) { this.chuteName = chuteName; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getSearchEndTime() { return searchEndTime; }
    public void setSearchEndTime(String searchEndTime) { this.searchEndTime = searchEndTime; }
}
