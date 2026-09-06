package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class SqWxUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "用户ID")
    private Long userId;
    private Long deptName;
    private Long deptId;
    @Excel(name = "部门")
    private String deptLabel;
    @Excel(name = "账号")
    private String username;
    @Excel(name = "姓名")
    private String nickName;
    private String createBy;
    private String updateBy;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getDeptName() { return deptName; }
    public void setDeptName(Long deptName) { this.deptName = deptName; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getDeptLabel() { return deptLabel; }
    public void setDeptLabel(String deptLabel) { this.deptLabel = deptLabel; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}
