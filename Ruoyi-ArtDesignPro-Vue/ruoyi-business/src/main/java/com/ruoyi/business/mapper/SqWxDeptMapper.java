package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqWxDept;

public interface SqWxDeptMapper
{
    public SqWxDept selectSqWxDeptById(Long deptId);
    public List<SqWxDept> selectSqWxDeptList(SqWxDept dept);
    public int insertSqWxDept(SqWxDept dept);
    public int updateSqWxDept(SqWxDept dept);
    public int deleteSqWxDeptByIds(Long[] deptIds);
    public int countSqWxDeptByPid(Long pid);
    public int updateSubCount(Long deptId);
}
