package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysLoanReturnLineMapper;
import com.template.system.entity.SysLoanReturnLine;
import com.template.system.service.ISysLoanReturnLineService;

/**
 * 归还单-明细Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysLoanReturnLineServiceImpl implements ISysLoanReturnLineService
{
    @Autowired
    private SysLoanReturnLineMapper sysLoanReturnLineMapper;

    /**
     * 查询归还单-明细
     *
     * @param id 归还单-明细主键
     * @return 归还单-明细
     */
    @Override
    public SysLoanReturnLine selectSysLoanReturnLineById(Long id)
    {
        return sysLoanReturnLineMapper.selectSysLoanReturnLineById(id);
    }

    /**
     * 查询归还单-明细列表
     *
     * @param sysLoanReturnLine 归还单-明细
     * @return 归还单-明细
     */
    @Override
    public List<SysLoanReturnLine> selectSysLoanReturnLineList(SysLoanReturnLine sysLoanReturnLine)
    {
        return sysLoanReturnLineMapper.selectSysLoanReturnLineList(sysLoanReturnLine);
    }

    /**
     * 新增归还单-明细
     *
     * @param sysLoanReturnLine 归还单-明细
     * @return 结果
     */
    @Override
    public int insertSysLoanReturnLine(SysLoanReturnLine sysLoanReturnLine)
    {
        sysLoanReturnLine.setCreateTime(DateUtils.getNowDate());
        return sysLoanReturnLineMapper.insertSysLoanReturnLine(sysLoanReturnLine);
    }

    /**
     * 修改归还单-明细
     *
     * @param sysLoanReturnLine 归还单-明细
     * @return 结果
     */
    @Override
    public int updateSysLoanReturnLine(SysLoanReturnLine sysLoanReturnLine)
    {
        sysLoanReturnLine.setUpdateTime(DateUtils.getNowDate());
        return sysLoanReturnLineMapper.updateSysLoanReturnLine(sysLoanReturnLine);
    }

    /**
     * 批量删除归还单-明细
     *
     * @param ids 需要删除的归还单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysLoanReturnLineByIds(Long[] ids)
    {
        return sysLoanReturnLineMapper.deleteSysLoanReturnLineByIds(ids);
    }

    /**
     * 删除归还单-明细信息
     *
     * @param id 归还单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysLoanReturnLineById(Long id)
    {
        return sysLoanReturnLineMapper.deleteSysLoanReturnLineById(id);
    }
}
