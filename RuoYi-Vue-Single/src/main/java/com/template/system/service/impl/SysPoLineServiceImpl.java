package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysPoLineMapper;
import com.template.system.entity.SysPoLine;
import com.template.system.service.ISysPoLineService;

/**
 * 采购订单-明细Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysPoLineServiceImpl implements ISysPoLineService
{
    @Autowired
    private SysPoLineMapper sysPoLineMapper;

    /**
     * 查询采购订单-明细
     *
     * @param id 采购订单-明细主键
     * @return 采购订单-明细
     */
    @Override
    public SysPoLine selectSysPoLineById(Long id)
    {
        return sysPoLineMapper.selectSysPoLineById(id);
    }

    /**
     * 查询采购订单-明细列表
     *
     * @param sysPoLine 采购订单-明细
     * @return 采购订单-明细
     */
    @Override
    public List<SysPoLine> selectSysPoLineList(SysPoLine sysPoLine)
    {
        return sysPoLineMapper.selectSysPoLineList(sysPoLine);
    }

    /**
     * 新增采购订单-明细
     *
     * @param sysPoLine 采购订单-明细
     * @return 结果
     */
    @Override
    public int insertSysPoLine(SysPoLine sysPoLine)
    {
        sysPoLine.setCreateTime(DateUtils.getNowDate());
        return sysPoLineMapper.insertSysPoLine(sysPoLine);
    }

    /**
     * 修改采购订单-明细
     *
     * @param sysPoLine 采购订单-明细
     * @return 结果
     */
    @Override
    public int updateSysPoLine(SysPoLine sysPoLine)
    {
        sysPoLine.setUpdateTime(DateUtils.getNowDate());
        return sysPoLineMapper.updateSysPoLine(sysPoLine);
    }

    /**
     * 批量删除采购订单-明细
     *
     * @param ids 需要删除的采购订单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysPoLineByIds(Long[] ids)
    {
        return sysPoLineMapper.deleteSysPoLineByIds(ids);
    }

    /**
     * 删除采购订单-明细信息
     *
     * @param id 采购订单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysPoLineById(Long id)
    {
        return sysPoLineMapper.deleteSysPoLineById(id);
    }
}
