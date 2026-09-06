package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysStockCountHeaderMapper;
import com.template.system.entity.SysStockCountHeader;
import com.template.system.service.ISysStockCountHeaderService;

/**
 * 盘点单-主Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysStockCountHeaderServiceImpl implements ISysStockCountHeaderService
{
    @Autowired
    private SysStockCountHeaderMapper sysStockCountHeaderMapper;

    /**
     * 查询盘点单-主
     *
     * @param id 盘点单-主主键
     * @return 盘点单-主
     */
    @Override
    public SysStockCountHeader selectSysStockCountHeaderById(Long id)
    {
        return sysStockCountHeaderMapper.selectSysStockCountHeaderById(id);
    }

    /**
     * 查询盘点单-主列表
     *
     * @param sysStockCountHeader 盘点单-主
     * @return 盘点单-主
     */
    @Override
    public List<SysStockCountHeader> selectSysStockCountHeaderList(SysStockCountHeader sysStockCountHeader)
    {
        return sysStockCountHeaderMapper.selectSysStockCountHeaderList(sysStockCountHeader);
    }

    /**
     * 新增盘点单-主
     *
     * @param sysStockCountHeader 盘点单-主
     * @return 结果
     */
    @Override
    public int insertSysStockCountHeader(SysStockCountHeader sysStockCountHeader)
    {
        sysStockCountHeader.setCreateTime(DateUtils.getNowDate());
        return sysStockCountHeaderMapper.insertSysStockCountHeader(sysStockCountHeader);
    }

    /**
     * 修改盘点单-主
     *
     * @param sysStockCountHeader 盘点单-主
     * @return 结果
     */
    @Override
    public int updateSysStockCountHeader(SysStockCountHeader sysStockCountHeader)
    {
        sysStockCountHeader.setUpdateTime(DateUtils.getNowDate());
        return sysStockCountHeaderMapper.updateSysStockCountHeader(sysStockCountHeader);
    }

    /**
     * 批量删除盘点单-主
     *
     * @param ids 需要删除的盘点单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysStockCountHeaderByIds(Long[] ids)
    {
        return sysStockCountHeaderMapper.deleteSysStockCountHeaderByIds(ids);
    }

    /**
     * 删除盘点单-主信息
     *
     * @param id 盘点单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysStockCountHeaderById(Long id)
    {
        return sysStockCountHeaderMapper.deleteSysStockCountHeaderById(id);
    }
}
