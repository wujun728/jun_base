package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysTransferHeaderMapper;
import com.template.system.entity.SysTransferHeader;
import com.template.system.service.ISysTransferHeaderService;

/**
 * 调拨单-主Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysTransferHeaderServiceImpl implements ISysTransferHeaderService
{
    @Autowired
    private SysTransferHeaderMapper sysTransferHeaderMapper;

    /**
     * 查询调拨单-主
     *
     * @param id 调拨单-主主键
     * @return 调拨单-主
     */
    @Override
    public SysTransferHeader selectSysTransferHeaderById(Long id)
    {
        return sysTransferHeaderMapper.selectSysTransferHeaderById(id);
    }

    /**
     * 查询调拨单-主列表
     *
     * @param sysTransferHeader 调拨单-主
     * @return 调拨单-主
     */
    @Override
    public List<SysTransferHeader> selectSysTransferHeaderList(SysTransferHeader sysTransferHeader)
    {
        return sysTransferHeaderMapper.selectSysTransferHeaderList(sysTransferHeader);
    }

    /**
     * 新增调拨单-主
     *
     * @param sysTransferHeader 调拨单-主
     * @return 结果
     */
    @Override
    public int insertSysTransferHeader(SysTransferHeader sysTransferHeader)
    {
        sysTransferHeader.setCreateTime(DateUtils.getNowDate());
        return sysTransferHeaderMapper.insertSysTransferHeader(sysTransferHeader);
    }

    /**
     * 修改调拨单-主
     *
     * @param sysTransferHeader 调拨单-主
     * @return 结果
     */
    @Override
    public int updateSysTransferHeader(SysTransferHeader sysTransferHeader)
    {
        sysTransferHeader.setUpdateTime(DateUtils.getNowDate());
        return sysTransferHeaderMapper.updateSysTransferHeader(sysTransferHeader);
    }

    /**
     * 批量删除调拨单-主
     *
     * @param ids 需要删除的调拨单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysTransferHeaderByIds(Long[] ids)
    {
        return sysTransferHeaderMapper.deleteSysTransferHeaderByIds(ids);
    }

    /**
     * 删除调拨单-主信息
     *
     * @param id 调拨单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysTransferHeaderById(Long id)
    {
        return sysTransferHeaderMapper.deleteSysTransferHeaderById(id);
    }
}
