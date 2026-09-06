package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysTransferLineMapper;
import com.template.system.entity.SysTransferLine;
import com.template.system.service.ISysTransferLineService;

/**
 * 调拨单-明细Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysTransferLineServiceImpl implements ISysTransferLineService
{
    @Autowired
    private SysTransferLineMapper sysTransferLineMapper;

    /**
     * 查询调拨单-明细
     *
     * @param id 调拨单-明细主键
     * @return 调拨单-明细
     */
    @Override
    public SysTransferLine selectSysTransferLineById(Long id)
    {
        return sysTransferLineMapper.selectSysTransferLineById(id);
    }

    /**
     * 查询调拨单-明细列表
     *
     * @param sysTransferLine 调拨单-明细
     * @return 调拨单-明细
     */
    @Override
    public List<SysTransferLine> selectSysTransferLineList(SysTransferLine sysTransferLine)
    {
        return sysTransferLineMapper.selectSysTransferLineList(sysTransferLine);
    }

    /**
     * 新增调拨单-明细
     *
     * @param sysTransferLine 调拨单-明细
     * @return 结果
     */
    @Override
    public int insertSysTransferLine(SysTransferLine sysTransferLine)
    {
        sysTransferLine.setCreateTime(DateUtils.getNowDate());
        return sysTransferLineMapper.insertSysTransferLine(sysTransferLine);
    }

    /**
     * 修改调拨单-明细
     *
     * @param sysTransferLine 调拨单-明细
     * @return 结果
     */
    @Override
    public int updateSysTransferLine(SysTransferLine sysTransferLine)
    {
        sysTransferLine.setUpdateTime(DateUtils.getNowDate());
        return sysTransferLineMapper.updateSysTransferLine(sysTransferLine);
    }

    /**
     * 批量删除调拨单-明细
     *
     * @param ids 需要删除的调拨单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysTransferLineByIds(Long[] ids)
    {
        return sysTransferLineMapper.deleteSysTransferLineByIds(ids);
    }

    /**
     * 删除调拨单-明细信息
     *
     * @param id 调拨单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysTransferLineById(Long id)
    {
        return sysTransferLineMapper.deleteSysTransferLineById(id);
    }
}
