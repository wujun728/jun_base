package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysApPaymentMapper;
import com.template.system.entity.SysApPayment;
import com.template.system.service.ISysApPaymentService;

/**
 * 采购付款登记Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysApPaymentServiceImpl implements ISysApPaymentService
{
    @Autowired
    private SysApPaymentMapper sysApPaymentMapper;

    /**
     * 查询采购付款登记
     *
     * @param id 采购付款登记主键
     * @return 采购付款登记
     */
    @Override
    public SysApPayment selectSysApPaymentById(Long id)
    {
        return sysApPaymentMapper.selectSysApPaymentById(id);
    }

    /**
     * 查询采购付款登记列表
     *
     * @param sysApPayment 采购付款登记
     * @return 采购付款登记
     */
    @Override
    public List<SysApPayment> selectSysApPaymentList(SysApPayment sysApPayment)
    {
        return sysApPaymentMapper.selectSysApPaymentList(sysApPayment);
    }

    /**
     * 新增采购付款登记
     *
     * @param sysApPayment 采购付款登记
     * @return 结果
     */
    @Override
    public int insertSysApPayment(SysApPayment sysApPayment)
    {
        sysApPayment.setCreateTime(DateUtils.getNowDate());
        return sysApPaymentMapper.insertSysApPayment(sysApPayment);
    }

    /**
     * 修改采购付款登记
     *
     * @param sysApPayment 采购付款登记
     * @return 结果
     */
    @Override
    public int updateSysApPayment(SysApPayment sysApPayment)
    {
        sysApPayment.setUpdateTime(DateUtils.getNowDate());
        return sysApPaymentMapper.updateSysApPayment(sysApPayment);
    }

    /**
     * 批量删除采购付款登记
     *
     * @param ids 需要删除的采购付款登记主键
     * @return 结果
     */
    @Override
    public int deleteSysApPaymentByIds(Long[] ids)
    {
        return sysApPaymentMapper.deleteSysApPaymentByIds(ids);
    }

    /**
     * 删除采购付款登记信息
     *
     * @param id 采购付款登记主键
     * @return 结果
     */
    @Override
    public int deleteSysApPaymentById(Long id)
    {
        return sysApPaymentMapper.deleteSysApPaymentById(id);
    }
}
