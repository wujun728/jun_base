package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysApPrepaymentMapper;
import com.template.system.entity.SysApPrepayment;
import com.template.system.service.ISysApPrepaymentService;

/**
 * 采购预付款登记Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysApPrepaymentServiceImpl implements ISysApPrepaymentService
{
    @Autowired
    private SysApPrepaymentMapper sysApPrepaymentMapper;

    /**
     * 查询采购预付款登记
     *
     * @param id 采购预付款登记主键
     * @return 采购预付款登记
     */
    @Override
    public SysApPrepayment selectSysApPrepaymentById(Long id)
    {
        return sysApPrepaymentMapper.selectSysApPrepaymentById(id);
    }

    /**
     * 查询采购预付款登记列表
     *
     * @param sysApPrepayment 采购预付款登记
     * @return 采购预付款登记
     */
    @Override
    public List<SysApPrepayment> selectSysApPrepaymentList(SysApPrepayment sysApPrepayment)
    {
        return sysApPrepaymentMapper.selectSysApPrepaymentList(sysApPrepayment);
    }

    /**
     * 新增采购预付款登记
     *
     * @param sysApPrepayment 采购预付款登记
     * @return 结果
     */
    @Override
    public int insertSysApPrepayment(SysApPrepayment sysApPrepayment)
    {
        sysApPrepayment.setCreateTime(DateUtils.getNowDate());
        return sysApPrepaymentMapper.insertSysApPrepayment(sysApPrepayment);
    }

    /**
     * 修改采购预付款登记
     *
     * @param sysApPrepayment 采购预付款登记
     * @return 结果
     */
    @Override
    public int updateSysApPrepayment(SysApPrepayment sysApPrepayment)
    {
        sysApPrepayment.setUpdateTime(DateUtils.getNowDate());
        return sysApPrepaymentMapper.updateSysApPrepayment(sysApPrepayment);
    }

    /**
     * 批量删除采购预付款登记
     *
     * @param ids 需要删除的采购预付款登记主键
     * @return 结果
     */
    @Override
    public int deleteSysApPrepaymentByIds(Long[] ids)
    {
        return sysApPrepaymentMapper.deleteSysApPrepaymentByIds(ids);
    }

    /**
     * 删除采购预付款登记信息
     *
     * @param id 采购预付款登记主键
     * @return 结果
     */
    @Override
    public int deleteSysApPrepaymentById(Long id)
    {
        return sysApPrepaymentMapper.deleteSysApPrepaymentById(id);
    }
}
