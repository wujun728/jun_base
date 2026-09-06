package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysGrnLineMapper;
import com.template.system.entity.SysGrnLine;
import com.template.system.service.ISysGrnLineService;

/**
 * 采购进货单-明细Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysGrnLineServiceImpl implements ISysGrnLineService
{
    @Autowired
    private SysGrnLineMapper sysGrnLineMapper;

    /**
     * 查询采购进货单-明细
     *
     * @param id 采购进货单-明细主键
     * @return 采购进货单-明细
     */
    @Override
    public SysGrnLine selectSysGrnLineById(Long id)
    {
        return sysGrnLineMapper.selectSysGrnLineById(id);
    }

    /**
     * 查询采购进货单-明细列表
     *
     * @param sysGrnLine 采购进货单-明细
     * @return 采购进货单-明细
     */
    @Override
    public List<SysGrnLine> selectSysGrnLineList(SysGrnLine sysGrnLine)
    {
        return sysGrnLineMapper.selectSysGrnLineList(sysGrnLine);
    }

    /**
     * 新增采购进货单-明细
     *
     * @param sysGrnLine 采购进货单-明细
     * @return 结果
     */
    @Override
    public int insertSysGrnLine(SysGrnLine sysGrnLine)
    {
        sysGrnLine.setCreateTime(DateUtils.getNowDate());
        return sysGrnLineMapper.insertSysGrnLine(sysGrnLine);
    }

    /**
     * 修改采购进货单-明细
     *
     * @param sysGrnLine 采购进货单-明细
     * @return 结果
     */
    @Override
    public int updateSysGrnLine(SysGrnLine sysGrnLine)
    {
        sysGrnLine.setUpdateTime(DateUtils.getNowDate());
        return sysGrnLineMapper.updateSysGrnLine(sysGrnLine);
    }

    /**
     * 批量删除采购进货单-明细
     *
     * @param ids 需要删除的采购进货单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysGrnLineByIds(Long[] ids)
    {
        return sysGrnLineMapper.deleteSysGrnLineByIds(ids);
    }

    /**
     * 删除采购进货单-明细信息
     *
     * @param id 采购进货单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysGrnLineById(Long id)
    {
        return sysGrnLineMapper.deleteSysGrnLineById(id);
    }
}
