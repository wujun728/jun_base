package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqWxDept;

public interface ISqWxDeptService
{
    public SqWxDept selectSqWxDeptById(Long deptId);
    public List<SqWxDept> selectSqWxDeptList(SqWxDept dept);
    public int insertSqWxDept(SqWxDept dept);
    public int updateSqWxDept(SqWxDept dept);
    public int deleteSqWxDeptByIds(Long[] deptIds);
}
