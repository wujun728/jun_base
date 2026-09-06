package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysReturnLineMapper;
import com.template.system.entity.SysReturnLine;
import com.template.system.service.ISysReturnLineService;

/**
 * 退库单-明细Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysReturnLineServiceImpl implements ISysReturnLineService
{
    @Autowired
    private SysReturnLineMapper sysReturnLineMapper;

    /**
     * 查询退库单-明细
     *
     * @param id 退库单-明细主键
     * @return 退库单-明细
     */
    @Override
    public SysReturnLine selectSysReturnLineById(Long id)
    {
        return sysReturnLineMapper.selectSysReturnLineById(id);
    }

    /**
     * 查询退库单-明细列表
     *
     * @param sysReturnLine 退库单-明细
     * @return 退库单-明细
     */
    @Override
    public List<SysReturnLine> selectSysReturnLineList(SysReturnLine sysReturnLine)
    {
        return sysReturnLineMapper.selectSysReturnLineList(sysReturnLine);
    }

    /**
     * 新增退库单-明细
     *
     * @param sysReturnLine 退库单-明细
     * @return 结果
     */
    @Override
    public int insertSysReturnLine(SysReturnLine sysReturnLine)
    {
        sysReturnLine.setCreateTime(DateUtils.getNowDate());
        return sysReturnLineMapper.insertSysReturnLine(sysReturnLine);
    }

    /**
     * 修改退库单-明细
     *
     * @param sysReturnLine 退库单-明细
     * @return 结果
     */
    @Override
    public int updateSysReturnLine(SysReturnLine sysReturnLine)
    {
        sysReturnLine.setUpdateTime(DateUtils.getNowDate());
        return sysReturnLineMapper.updateSysReturnLine(sysReturnLine);
    }

    /**
     * 批量删除退库单-明细
     *
     * @param ids 需要删除的退库单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysReturnLineByIds(Long[] ids)
    {
        return sysReturnLineMapper.deleteSysReturnLineByIds(ids);
    }

    /**
     * 删除退库单-明细信息
     *
     * @param id 退库单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysReturnLineById(Long id)
    {
        return sysReturnLineMapper.deleteSysReturnLineById(id);
    }
}
