package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysGrnHeaderMapper;
import com.template.system.entity.SysGrnHeader;
import com.template.system.service.ISysGrnHeaderService;

/**
 * 采购进货单-主Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysGrnHeaderServiceImpl implements ISysGrnHeaderService
{
    @Autowired
    private SysGrnHeaderMapper sysGrnHeaderMapper;

    /**
     * 查询采购进货单-主
     *
     * @param id 采购进货单-主主键
     * @return 采购进货单-主
     */
    @Override
    public SysGrnHeader selectSysGrnHeaderById(Long id)
    {
        return sysGrnHeaderMapper.selectSysGrnHeaderById(id);
    }

    /**
     * 查询采购进货单-主列表
     *
     * @param sysGrnHeader 采购进货单-主
     * @return 采购进货单-主
     */
    @Override
    public List<SysGrnHeader> selectSysGrnHeaderList(SysGrnHeader sysGrnHeader)
    {
        return sysGrnHeaderMapper.selectSysGrnHeaderList(sysGrnHeader);
    }

    /**
     * 新增采购进货单-主
     *
     * @param sysGrnHeader 采购进货单-主
     * @return 结果
     */
    @Override
    public int insertSysGrnHeader(SysGrnHeader sysGrnHeader)
    {
        sysGrnHeader.setCreateTime(DateUtils.getNowDate());
        return sysGrnHeaderMapper.insertSysGrnHeader(sysGrnHeader);
    }

    /**
     * 修改采购进货单-主
     *
     * @param sysGrnHeader 采购进货单-主
     * @return 结果
     */
    @Override
    public int updateSysGrnHeader(SysGrnHeader sysGrnHeader)
    {
        sysGrnHeader.setUpdateTime(DateUtils.getNowDate());
        return sysGrnHeaderMapper.updateSysGrnHeader(sysGrnHeader);
    }

    /**
     * 批量删除采购进货单-主
     *
     * @param ids 需要删除的采购进货单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysGrnHeaderByIds(Long[] ids)
    {
        return sysGrnHeaderMapper.deleteSysGrnHeaderByIds(ids);
    }

    /**
     * 删除采购进货单-主信息
     *
     * @param id 采购进货单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysGrnHeaderById(Long id)
    {
        return sysGrnHeaderMapper.deleteSysGrnHeaderById(id);
    }
}
