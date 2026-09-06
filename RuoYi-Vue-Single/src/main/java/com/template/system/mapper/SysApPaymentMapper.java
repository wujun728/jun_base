package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysApPayment;

/**
 * 采购付款登记Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysApPaymentMapper 
{
    /**
     * 查询采购付款登记
     * 
     * @param id 采购付款登记主键
     * @return 采购付款登记
     */
    public SysApPayment selectSysApPaymentById(Long id);

    /**
     * 查询采购付款登记列表
     * 
     * @param sysApPayment 采购付款登记
     * @return 采购付款登记集合
     */
    public List<SysApPayment> selectSysApPaymentList(SysApPayment sysApPayment);

    /**
     * 新增采购付款登记
     * 
     * @param sysApPayment 采购付款登记
     * @return 结果
     */
    public int insertSysApPayment(SysApPayment sysApPayment);

    /**
     * 修改采购付款登记
     * 
     * @param sysApPayment 采购付款登记
     * @return 结果
     */
    public int updateSysApPayment(SysApPayment sysApPayment);

    /**
     * 删除采购付款登记
     * 
     * @param id 采购付款登记主键
     * @return 结果
     */
    public int deleteSysApPaymentById(Long id);

    /**
     * 批量删除采购付款登记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysApPaymentByIds(Long[] ids);
}
