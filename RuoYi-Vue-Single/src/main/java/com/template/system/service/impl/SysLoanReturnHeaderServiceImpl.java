package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysLoanReturnHeaderMapper;
import com.template.system.entity.SysLoanReturnHeader;
import com.template.system.service.ISysLoanReturnHeaderService;

/**
 * 归还单-主Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysLoanReturnHeaderServiceImpl implements ISysLoanReturnHeaderService
{
    @Autowired
    private SysLoanReturnHeaderMapper sysLoanReturnHeaderMapper;

    /**
     * 查询归还单-主
     *
     * @param id 归还单-主主键
     * @return 归还单-主
     */
    @Override
    public SysLoanReturnHeader selectSysLoanReturnHeaderById(Long id)
    {
        return sysLoanReturnHeaderMapper.selectSysLoanReturnHeaderById(id);
    }

    /**
     * 查询归还单-主列表
     *
     * @param sysLoanReturnHeader 归还单-主
     * @return 归还单-主
     */
    @Override
    public List<SysLoanReturnHeader> selectSysLoanReturnHeaderList(SysLoanReturnHeader sysLoanReturnHeader)
    {
        return sysLoanReturnHeaderMapper.selectSysLoanReturnHeaderList(sysLoanReturnHeader);
    }

    /**
     * 新增归还单-主
     *
     * @param sysLoanReturnHeader 归还单-主
     * @return 结果
     */
    @Override
    public int insertSysLoanReturnHeader(SysLoanReturnHeader sysLoanReturnHeader)
    {
        sysLoanReturnHeader.setCreateTime(DateUtils.getNowDate());
        return sysLoanReturnHeaderMapper.insertSysLoanReturnHeader(sysLoanReturnHeader);
    }

    /**
     * 修改归还单-主
     *
     * @param sysLoanReturnHeader 归还单-主
     * @return 结果
     */
    @Override
    public int updateSysLoanReturnHeader(SysLoanReturnHeader sysLoanReturnHeader)
    {
        sysLoanReturnHeader.setUpdateTime(DateUtils.getNowDate());
        return sysLoanReturnHeaderMapper.updateSysLoanReturnHeader(sysLoanReturnHeader);
    }

    /**
     * 批量删除归还单-主
     *
     * @param ids 需要删除的归还单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysLoanReturnHeaderByIds(Long[] ids)
    {
        return sysLoanReturnHeaderMapper.deleteSysLoanReturnHeaderByIds(ids);
    }

    /**
     * 删除归还单-主信息
     *
     * @param id 归还单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysLoanReturnHeaderById(Long id)
    {
        return sysLoanReturnHeaderMapper.deleteSysLoanReturnHeaderById(id);
    }
}
