package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysReturnHeaderMapper;
import com.template.system.entity.SysReturnHeader;
import com.template.system.service.ISysReturnHeaderService;

/**
 * 退库单-主Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysReturnHeaderServiceImpl implements ISysReturnHeaderService
{
    @Autowired
    private SysReturnHeaderMapper sysReturnHeaderMapper;

    /**
     * 查询退库单-主
     *
     * @param id 退库单-主主键
     * @return 退库单-主
     */
    @Override
    public SysReturnHeader selectSysReturnHeaderById(Long id)
    {
        return sysReturnHeaderMapper.selectSysReturnHeaderById(id);
    }

    /**
     * 查询退库单-主列表
     *
     * @param sysReturnHeader 退库单-主
     * @return 退库单-主
     */
    @Override
    public List<SysReturnHeader> selectSysReturnHeaderList(SysReturnHeader sysReturnHeader)
    {
        return sysReturnHeaderMapper.selectSysReturnHeaderList(sysReturnHeader);
    }

    /**
     * 新增退库单-主
     *
     * @param sysReturnHeader 退库单-主
     * @return 结果
     */
    @Override
    public int insertSysReturnHeader(SysReturnHeader sysReturnHeader)
    {
        sysReturnHeader.setCreateTime(DateUtils.getNowDate());
        return sysReturnHeaderMapper.insertSysReturnHeader(sysReturnHeader);
    }

    /**
     * 修改退库单-主
     *
     * @param sysReturnHeader 退库单-主
     * @return 结果
     */
    @Override
    public int updateSysReturnHeader(SysReturnHeader sysReturnHeader)
    {
        sysReturnHeader.setUpdateTime(DateUtils.getNowDate());
        return sysReturnHeaderMapper.updateSysReturnHeader(sysReturnHeader);
    }

    /**
     * 批量删除退库单-主
     *
     * @param ids 需要删除的退库单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysReturnHeaderByIds(Long[] ids)
    {
        return sysReturnHeaderMapper.deleteSysReturnHeaderByIds(ids);
    }

    /**
     * 删除退库单-主信息
     *
     * @param id 退库单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysReturnHeaderById(Long id)
    {
        return sysReturnHeaderMapper.deleteSysReturnHeaderById(id);
    }
}
