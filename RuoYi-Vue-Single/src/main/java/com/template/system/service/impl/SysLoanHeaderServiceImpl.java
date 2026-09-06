package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysLoanHeaderMapper;
import com.template.system.entity.SysLoanHeader;
import com.template.system.service.ISysLoanHeaderService;

/**
 * 借出单-主Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysLoanHeaderServiceImpl implements ISysLoanHeaderService
{
    @Autowired
    private SysLoanHeaderMapper sysLoanHeaderMapper;

    /**
     * 查询借出单-主
     *
     * @param id 借出单-主主键
     * @return 借出单-主
     */
    @Override
    public SysLoanHeader selectSysLoanHeaderById(Long id)
    {
        return sysLoanHeaderMapper.selectSysLoanHeaderById(id);
    }

    /**
     * 查询借出单-主列表
     *
     * @param sysLoanHeader 借出单-主
     * @return 借出单-主
     */
    @Override
    public List<SysLoanHeader> selectSysLoanHeaderList(SysLoanHeader sysLoanHeader)
    {
        return sysLoanHeaderMapper.selectSysLoanHeaderList(sysLoanHeader);
    }

    /**
     * 新增借出单-主
     *
     * @param sysLoanHeader 借出单-主
     * @return 结果
     */
    @Override
    public int insertSysLoanHeader(SysLoanHeader sysLoanHeader)
    {
        sysLoanHeader.setCreateTime(DateUtils.getNowDate());
        return sysLoanHeaderMapper.insertSysLoanHeader(sysLoanHeader);
    }

    /**
     * 修改借出单-主
     *
     * @param sysLoanHeader 借出单-主
     * @return 结果
     */
    @Override
    public int updateSysLoanHeader(SysLoanHeader sysLoanHeader)
    {
        sysLoanHeader.setUpdateTime(DateUtils.getNowDate());
        return sysLoanHeaderMapper.updateSysLoanHeader(sysLoanHeader);
    }

    /**
     * 批量删除借出单-主
     *
     * @param ids 需要删除的借出单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysLoanHeaderByIds(Long[] ids)
    {
        return sysLoanHeaderMapper.deleteSysLoanHeaderByIds(ids);
    }

    /**
     * 删除借出单-主信息
     *
     * @param id 借出单-主主键
     * @return 结果
     */
    @Override
    public int deleteSysLoanHeaderById(Long id)
    {
        return sysLoanHeaderMapper.deleteSysLoanHeaderById(id);
    }
}
