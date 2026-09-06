package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ车辆管理对象 a_cars
 */
public class SqTransportCar extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;

    @Excel(name = "车辆")
    private String cars;

    @Excel(name = "车辆编号")
    private String carsNum;

    @Excel(name = "车辆型号")
    private String carsXh;

    @Excel(name = "所属工区")
    private String mine;

    @Excel(name = "所属工区")
    private String mineLabel;

    @Excel(name = "车辆所属")
    private String carsComp;

    @Excel(name = "车辆所属")
    private String carsCompLabel;

    @Excel(name = "车辆载重")
    private String carsW;

    @Excel(name = "运输管理")
    private String teamid;

    @Excel(name = "运输管理")
    private String teamName;

    private String createUser;

    private String createDept;

    private String updateUser;

    private String status;

    private String isDeleted;

    @Excel(name = "车辆类型")
    private String ctype;

    @Excel(name = "色条")
    private String color;

    @Excel(name = "RFID标签")
    private String rfid;

    @Excel(name = "满载体积")
    private String volume;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCars() { return cars; }
    public void setCars(String cars) { this.cars = cars; }
    public String getCarsNum() { return carsNum; }
    public void setCarsNum(String carsNum) { this.carsNum = carsNum; }
    public String getCarsXh() { return carsXh; }
    public void setCarsXh(String carsXh) { this.carsXh = carsXh; }
    public String getMine() { return mine; }
    public void setMine(String mine) { this.mine = mine; }
    public String getMineLabel() { return mineLabel; }
    public void setMineLabel(String mineLabel) { this.mineLabel = mineLabel; }
    public String getCarsComp() { return carsComp; }
    public void setCarsComp(String carsComp) { this.carsComp = carsComp; }
    public String getCarsCompLabel() { return carsCompLabel; }
    public void setCarsCompLabel(String carsCompLabel) { this.carsCompLabel = carsCompLabel; }
    public String getCarsW() { return carsW; }
    public void setCarsW(String carsW) { this.carsW = carsW; }
    public String getTeamid() { return teamid; }
    public void setTeamid(String teamid) { this.teamid = teamid; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getCreateDept() { return createDept; }
    public void setCreateDept(String createDept) { this.createDept = createDept; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIsDeleted() { return isDeleted; }
    public void setIsDeleted(String isDeleted) { this.isDeleted = isDeleted; }
    public String getCtype() { return ctype; }
    public void setCtype(String ctype) { this.ctype = ctype; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getRfid() { return rfid; }
    public void setRfid(String rfid) { this.rfid = rfid; }
    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }
}
