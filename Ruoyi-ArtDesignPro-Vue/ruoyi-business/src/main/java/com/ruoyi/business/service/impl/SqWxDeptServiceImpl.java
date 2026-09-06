package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqWxDept;
import com.ruoyi.business.mapper.SqWxDeptMapper;
import com.ruoyi.business.service.ISqWxDeptService;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqWxDeptServiceImpl implements ISqWxDeptService
{
    @Autowired
    private SqWxDeptMapper sqWxDeptMapper;

    @Override
    public SqWxDept selectSqWxDeptById(Long deptId)
    {
        return sqWxDeptMapper.selectSqWxDeptById(deptId);
    }

    @Override
    public List<SqWxDept> selectSqWxDeptList(SqWxDept dept)
    {
        return sqWxDeptMapper.selectSqWxDeptList(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqWxDept(SqWxDept dept)
    {
        if (StringUtils.isBlank(dept.getEnabled()))
        {
            dept.setEnabled("true");
        }
        int rows = sqWxDeptMapper.insertSqWxDept(dept);
        refreshParentCount(dept.getPid());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqWxDept(SqWxDept dept)
    {
        SqWxDept old = sqWxDeptMapper.selectSqWxDeptById(dept.getDeptId());
        int rows = sqWxDeptMapper.updateSqWxDept(dept);
        refreshParentCount(dept.getPid());
        if (old != null && old.getPid() != null && !old.getPid().equals(dept.getPid()))
        {
            refreshParentCount(old.getPid());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqWxDeptByIds(Long[] deptIds)
    {
        for (Long deptId : deptIds)
        {
            if (sqWxDeptMapper.countSqWxDeptByPid(deptId) > 0)
            {
                throw new IllegalStateException("存在下级企业微信部门，不能删除");
            }
        }
        Long parentId = deptIds.length == 1 ? selectParentId(deptIds[0]) : null;
        int rows = sqWxDeptMapper.deleteSqWxDeptByIds(deptIds);
        refreshParentCount(parentId);
        return rows;
    }

    private Long selectParentId(Long deptId)
    {
        SqWxDept dept = sqWxDeptMapper.selectSqWxDeptById(deptId);
        return dept == null ? null : dept.getPid();
    }

    private void refreshParentCount(Long parentId)
    {
        if (parentId != null)
        {
            sqWxDeptMapper.updateSubCount(parentId);
        }
    }
}
