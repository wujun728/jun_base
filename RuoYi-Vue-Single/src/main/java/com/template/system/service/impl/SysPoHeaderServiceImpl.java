package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysPoHeaderMapper;
import com.template.system.entity.SysPoHeader;
import com.template.system.service.ISysPoHeaderService;

/**
 * 采购订单-主Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysPoHeaderServiceImpl implements ISysPoHeaderService
{
    @Autowired
    private SysPoHeaderMapper sysPoHeaderMapper;

    /**
     * 查询采购订单-主
     *
     * @param id 采购订单-主主键
     * @return 采购订单-主
     */
    @Override
    public SysPoHeader selectSysPoHeaderById(Long id)
    {
        return sysPoHeaderMapper.selectSysPoHeaderById(id);
    }

    /**
     * 查询采购订单-主列表
     *
     * @param sysPoHeader 采购订单-主
     * @return 采购订单-主
     */
    @Override
    public List<SysPoHeader> selectSysPoHeaderList(SysPoHeader sysPoHeader)
    {
        return sysPoHeaderMapper.selectSysPoHeaderList(sysPoHeader);
    }

    /**
     * 新增采购订单-主
     *
     * @param sysPoHeader 采购订单-主
     * @return 结果
     */
    @Override
    public int insertSysPoHeader(SysPoHeader sysPoHeader)
    {
        sysPoHeader.setCreateTime(DateUtils.getNowDate());
        return sysPoHeaderMapper.insertSysPoHeader(sysPoHeader);
    }

    /**
     * 修改采购订单-主
     *
     * @param sysPoHeader 采购订单-主
     * @return 结果
     */
    @Override
    public int updateSysPoHeader(SysPoHeader sysPoHeader)
    {
        sysPoHeader.setUpdateTime(DateUtils.getNowDate());
        return sysPoHeaderMapper.updateSysPoHeader(sysPoHeader);
    }

    /**
     * 批量删除采购订单-主
     *
     * @param ids 需要删除的采购订单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysPoHeaderByIds(Long[] ids)
    {
        return sysPoHeaderMapper.deleteSysPoHeaderByIds(ids);
    }

    /**
     * 删除采购订单-主信息
     *
     * @param id 采购订单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysPoHeaderById(Long id)
    {
        return sysPoHeaderMapper.deleteSysPoHeaderById(id);
    }
}
