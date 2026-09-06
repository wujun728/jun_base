package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ溜井卸矿报表对象 a_mdispatch
 */
public class SqDispatchReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;
    @Excel(name = "溜井名称")
    private String lname;
    @Excel(name = "所属公司")
    private String company;
    @Excel(name = "报表日期")
    private String theday;
    @Excel(name = "8点班大卡")
    private String bigc;
    @Excel(name = "8点班小卡")
    private String bigck;
    @Excel(name = "4点班大卡")
    private String bigf;
    @Excel(name = "4点班小卡")
    private String smailc;
    @Excel(name = "0点班大卡")
    private String smailck;
    @Excel(name = "0点班小卡")
    private String smailf;
    private String bigcRen;
    private String bigckRen;
    private String bigfRen;
    private String smailcRen;
    private String smailckRen;
    private String smailfRen;
    private String bigcKuang;
    private String bigcFei;
    private String bigckKuang;
    private String bigckFei;
    private String bigfKuang;
    private String bigfFei;
    private String smailcKuang;
    private String smailcFei;
    private String smailckKuang;
    private String smailckFei;
    private String smailfKuang;
    private String smailfFei;
    private String beginTime;
    private String endTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getTheday() { return theday; }
    public void setTheday(String theday) { this.theday = theday; }
    public String getBigc() { return bigc; }
    public void setBigc(String bigc) { this.bigc = bigc; }
    public String getBigck() { return bigck; }
    public void setBigck(String bigck) { this.bigck = bigck; }
    public String getBigf() { return bigf; }
    public void setBigf(String bigf) { this.bigf = bigf; }
    public String getSmailc() { return smailc; }
    public void setSmailc(String smailc) { this.smailc = smailc; }
    public String getSmailck() { return smailck; }
    public void setSmailck(String smailck) { this.smailck = smailck; }
    public String getSmailf() { return smailf; }
    public void setSmailf(String smailf) { this.smailf = smailf; }
    public String getBigcRen() { return bigcRen; }
    public void setBigcRen(String bigcRen) { this.bigcRen = bigcRen; }
    public String getBigckRen() { return bigckRen; }
    public void setBigckRen(String bigckRen) { this.bigckRen = bigckRen; }
    public String getBigfRen() { return bigfRen; }
    public void setBigfRen(String bigfRen) { this.bigfRen = bigfRen; }
    public String getSmailcRen() { return smailcRen; }
    public void setSmailcRen(String smailcRen) { this.smailcRen = smailcRen; }
    public String getSmailckRen() { return smailckRen; }
    public void setSmailckRen(String smailckRen) { this.smailckRen = smailckRen; }
    public String getSmailfRen() { return smailfRen; }
    public void setSmailfRen(String smailfRen) { this.smailfRen = smailfRen; }
    public String getBigcKuang() { return bigcKuang; }
    public void setBigcKuang(String bigcKuang) { this.bigcKuang = bigcKuang; }
    public String getBigcFei() { return bigcFei; }
    public void setBigcFei(String bigcFei) { this.bigcFei = bigcFei; }
    public String getBigckKuang() { return bigckKuang; }
    public void setBigckKuang(String bigckKuang) { this.bigckKuang = bigckKuang; }
    public String getBigckFei() { return bigckFei; }
    public void setBigckFei(String bigckFei) { this.bigckFei = bigckFei; }
    public String getBigfKuang() { return bigfKuang; }
    public void setBigfKuang(String bigfKuang) { this.bigfKuang = bigfKuang; }
    public String getBigfFei() { return bigfFei; }
    public void setBigfFei(String bigfFei) { this.bigfFei = bigfFei; }
    public String getSmailcKuang() { return smailcKuang; }
    public void setSmailcKuang(String smailcKuang) { this.smailcKuang = smailcKuang; }
    public String getSmailcFei() { return smailcFei; }
    public void setSmailcFei(String smailcFei) { this.smailcFei = smailcFei; }
    public String getSmailckKuang() { return smailckKuang; }
    public void setSmailckKuang(String smailckKuang) { this.smailckKuang = smailckKuang; }
    public String getSmailckFei() { return smailckFei; }
    public void setSmailckFei(String smailckFei) { this.smailckFei = smailckFei; }
    public String getSmailfKuang() { return smailfKuang; }
    public void setSmailfKuang(String smailfKuang) { this.smailfKuang = smailfKuang; }
    public String getSmailfFei() { return smailfFei; }
    public void setSmailfFei(String smailfFei) { this.smailfFei = smailfFei; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
