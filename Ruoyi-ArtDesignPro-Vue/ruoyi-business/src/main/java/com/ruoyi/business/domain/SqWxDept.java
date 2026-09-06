package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class SqWxDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "部门ID")
    private Long deptId;
    private Long pid;
    @Excel(name = "子部门数")
    private String subCount;
    @Excel(name = "部门名称")
    private String name;
    @Excel(name = "排序")
    private String deptSort;
    @Excel(name = "状态")
    private String enabled;
    @Excel(name = "组织类型")
    private String type;
    private String createBy;
    private String updateBy;
    private String parentName;

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Long getPid() { return pid; }
    public void setPid(Long pid) { this.pid = pid; }
    public String getSubCount() { return subCount; }
    public void setSubCount(String subCount) { this.subCount = subCount; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDeptSort() { return deptSort; }
    public void setDeptSort(String deptSort) { this.deptSort = deptSort; }
    public String getEnabled() { return enabled; }
    public void setEnabled(String enabled) { this.enabled = enabled; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
}
