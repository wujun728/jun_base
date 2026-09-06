package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ运输管理对象 a_team
 */
public class SqTransportTeam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;

    @Excel(name = "所属单位")
    private String tname;

    @Excel(name = "所属单位")
    private String tnameLabel;

    @Excel(name = "所属中段")
    private String tnameuser;

    private String createUser;

    private String createDept;

    private String updateUser;

    private String status;

    private String isDeleted;

    @Excel(name = "所属工区")
    private String mine;

    @Excel(name = "所属工区")
    private String mineLabel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTname() { return tname; }
    public void setTname(String tname) { this.tname = tname; }
    public String getTnameLabel() { return tnameLabel; }
    public void setTnameLabel(String tnameLabel) { this.tnameLabel = tnameLabel; }
    public String getTnameuser() { return tnameuser; }
    public void setTnameuser(String tnameuser) { this.tnameuser = tnameuser; }
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
    public String getMine() { return mine; }
    public void setMine(String mine) { this.mine = mine; }
    public String getMineLabel() { return mineLabel; }
    public void setMineLabel(String mineLabel) { this.mineLabel = mineLabel; }
}
