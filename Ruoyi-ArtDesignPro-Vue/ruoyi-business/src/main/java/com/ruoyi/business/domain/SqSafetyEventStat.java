package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ安全事件统计对象 a_alert
 */
public class SqSafetyEventStat extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "统计日期")
    private String statDate;
    @Excel(name = "报警类型")
    private String status;
    @Excel(name = "处理状态")
    private String state;
    @Excel(name = "事件数量")
    private Long eventCount;
    private String beginTime;
    private String endTime;

    public String getStatDate() { return statDate; }
    public void setStatDate(String statDate) { this.statDate = statDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public Long getEventCount() { return eventCount; }
    public void setEventCount(Long eventCount) { this.eventCount = eventCount; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
