package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysStockCountLineMapper;
import com.template.system.entity.SysStockCountLine;
import com.template.system.service.ISysStockCountLineService;

/**
 * 盘点单-明细Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysStockCountLineServiceImpl implements ISysStockCountLineService
{
    @Autowired
    private SysStockCountLineMapper sysStockCountLineMapper;

    /**
     * 查询盘点单-明细
     *
     * @param id 盘点单-明细主键
     * @return 盘点单-明细
     */
    @Override
    public SysStockCountLine selectSysStockCountLineById(Long id)
    {
        return sysStockCountLineMapper.selectSysStockCountLineById(id);
    }

    /**
     * 查询盘点单-明细列表
     *
     * @param sysStockCountLine 盘点单-明细
     * @return 盘点单-明细
     */
    @Override
    public List<SysStockCountLine> selectSysStockCountLineList(SysStockCountLine sysStockCountLine)
    {
        return sysStockCountLineMapper.selectSysStockCountLineList(sysStockCountLine);
    }

    /**
     * 新增盘点单-明细
     *
     * @param sysStockCountLine 盘点单-明细
     * @return 结果
     */
    @Override
    public int insertSysStockCountLine(SysStockCountLine sysStockCountLine)
    {
        sysStockCountLine.setCreateTime(DateUtils.getNowDate());
        return sysStockCountLineMapper.insertSysStockCountLine(sysStockCountLine);
    }

    /**
     * 修改盘点单-明细
     *
     * @param sysStockCountLine 盘点单-明细
     * @return 结果
     */
    @Override
    public int updateSysStockCountLine(SysStockCountLine sysStockCountLine)
    {
        sysStockCountLine.setUpdateTime(DateUtils.getNowDate());
        return sysStockCountLineMapper.updateSysStockCountLine(sysStockCountLine);
    }

    /**
     * 批量删除盘点单-明细
     *
     * @param ids 需要删除的盘点单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysStockCountLineByIds(Long[] ids)
    {
        return sysStockCountLineMapper.deleteSysStockCountLineByIds(ids);
    }

    /**
     * 删除盘点单-明细信息
     *
     * @param id 盘点单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysStockCountLineById(Long id)
    {
        return sysStockCountLineMapper.deleteSysStockCountLineById(id);
    }
}
