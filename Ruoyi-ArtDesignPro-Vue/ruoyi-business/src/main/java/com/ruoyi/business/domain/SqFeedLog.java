package com.ruoyi.business.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ入料记录对象 a_cars_logs / a_cars_logs_his
 */
public class SqFeedLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;

    @Excel(name = "溜井")
    private String name;

    @Excel(name = "溜井名称")
    private String chuteName;

    @Excel(name = "车辆编号")
    private String cars;

    @Excel(name = "是否满车")
    private String fulls;

    @Excel(name = "巨石")
    private String bigs;

    @Excel(name = "异物")
    private String yiwu;

    @Excel(name = "矿种")
    private String shitype;

    @Excel(name = "视频")
    private String videopath;

    @Excel(name = "图片")
    private String imgpath;

    private String imgpath1;

    @Excel(name = "逗留时间")
    private String timecha;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdate;

    private String isDeleted;

    private String createUser;

    private String createDept;

    private String updateUser;

    private String status;

    private String ctype;

    @Excel(name = "所属公司")
    private String company;

    @Excel(name = "人")
    private String person;

    @Excel(name = "卡车")
    private String forkcar;

    private String water;

    private String mud;

    private String safeline;

    @Excel(name = "班次")
    private String shift;

    @Excel(name = "组")
    private String groups;

    @Excel(name = "值班人")
    private String persons;

    @Excel(name = "装载率")
    private String rate;

    private String beginTime;

    private String endTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getChuteName() { return chuteName; }
    public void setChuteName(String chuteName) { this.chuteName = chuteName; }
    public String getCars() { return cars; }
    public void setCars(String cars) { this.cars = cars; }
    public String getFulls() { return fulls; }
    public void setFulls(String fulls) { this.fulls = fulls; }
    public String getBigs() { return bigs; }
    public void setBigs(String bigs) { this.bigs = bigs; }
    public String getYiwu() { return yiwu; }
    public void setYiwu(String yiwu) { this.yiwu = yiwu; }
    public String getShitype() { return shitype; }
    public void setShitype(String shitype) { this.shitype = shitype; }
    public String getVideopath() { return videopath; }
    public void setVideopath(String videopath) { this.videopath = videopath; }
    public String getImgpath() { return imgpath; }
    public void setImgpath(String imgpath) { this.imgpath = imgpath; }
    public String getImgpath1() { return imgpath1; }
    public void setImgpath1(String imgpath1) { this.imgpath1 = imgpath1; }
    public String getTimecha() { return timecha; }
    public void setTimecha(String timecha) { this.timecha = timecha; }
    public Date getCreatedate() { return createdate; }
    public void setCreatedate(Date createdate) { this.createdate = createdate; }
    public String getIsDeleted() { return isDeleted; }
    public void setIsDeleted(String isDeleted) { this.isDeleted = isDeleted; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getCreateDept() { return createDept; }
    public void setCreateDept(String createDept) { this.createDept = createDept; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCtype() { return ctype; }
    public void setCtype(String ctype) { this.ctype = ctype; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getPerson() { return person; }
    public void setPerson(String person) { this.person = person; }
    public String getForkcar() { return forkcar; }
    public void setForkcar(String forkcar) { this.forkcar = forkcar; }
    public String getWater() { return water; }
    public void setWater(String water) { this.water = water; }
    public String getMud() { return mud; }
    public void setMud(String mud) { this.mud = mud; }
    public String getSafeline() { return safeline; }
    public void setSafeline(String safeline) { this.safeline = safeline; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    public String getGroups() { return groups; }
    public void setGroups(String groups) { this.groups = groups; }
    public String getPersons() { return persons; }
    public void setPersons(String persons) { this.persons = persons; }
    public String getRate() { return rate; }
    public void setRate(String rate) { this.rate = rate; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
