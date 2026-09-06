package com.ruoyi.business.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ料位一览对象 yn_logs_level
 */
public class SqLevelLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;
    @Excel(name = "时间点")
    private Integer times;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", dateFormat = "yyyy-MM-dd")
    private Date dates;
    @Excel(name = "溜井点")
    private String lname;
    @Excel(name = "溜井名称")
    private String chuteName;
    @Excel(name = "车数")
    private Integer cars;
    @Excel(name = "料位高度")
    private BigDecimal heights;
    private String beginTime;
    private String endTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getTimes() { return times; }
    public void setTimes(Integer times) { this.times = times; }
    public Date getDates() { return dates; }
    public void setDates(Date dates) { this.dates = dates; }
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    public String getChuteName() { return chuteName; }
    public void setChuteName(String chuteName) { this.chuteName = chuteName; }
    public Integer getCars() { return cars; }
    public void setCars(Integer cars) { this.cars = cars; }
    public BigDecimal getHeights() { return heights; }
    public void setHeights(BigDecimal heights) { this.heights = heights; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
