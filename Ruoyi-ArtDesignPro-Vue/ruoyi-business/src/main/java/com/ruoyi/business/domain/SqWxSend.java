package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class SqWxSend extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;
    @Excel(name = "报警类型")
    private Integer types;
    @Excel(name = "推送人员")
    private String sendUser;
    @Excel(name = "是否推送")
    private String isSend;
    @Excel(name = "报警等级")
    private String level;
    private String createUser;
    private String createDept;
    private String updateUser;
    @Excel(name = "所属单位")
    private String tname;
    @Excel(name = "推送名称")
    private String userName;
    private String deptId;
    private String deptLabel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getTypes() { return types; }
    public void setTypes(Integer types) { this.types = types; }
    public String getSendUser() { return sendUser; }
    public void setSendUser(String sendUser) { this.sendUser = sendUser; }
    public String getIsSend() { return isSend; }
    public void setIsSend(String isSend) { this.isSend = isSend; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getCreateDept() { return createDept; }
    public void setCreateDept(String createDept) { this.createDept = createDept; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getTname() { return tname; }
    public void setTname(String tname) { this.tname = tname; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getDeptId() { return deptId; }
    public void setDeptId(String deptId) { this.deptId = deptId; }
    public String getDeptLabel() { return deptLabel; }
    public void setDeptLabel(String deptLabel) { this.deptLabel = deptLabel; }
}
