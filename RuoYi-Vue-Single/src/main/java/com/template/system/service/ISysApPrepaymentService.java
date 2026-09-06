package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysApPrepayment;

/**
 * 采购预付款登记Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysApPrepaymentService 
{
    /**
     * 查询采购预付款登记
     * 
     * @param id 采购预付款登记主键
     * @return 采购预付款登记
     */
    public SysApPrepayment selectSysApPrepaymentById(Long id);

    /**
     * 查询采购预付款登记列表
     * 
     * @param sysApPrepayment 采购预付款登记
     * @return 采购预付款登记集合
     */
    public List<SysApPrepayment> selectSysApPrepaymentList(SysApPrepayment sysApPrepayment);

    /**
     * 新增采购预付款登记
     * 
     * @param sysApPrepayment 采购预付款登记
     * @return 结果
     */
    public int insertSysApPrepayment(SysApPrepayment sysApPrepayment);

    /**
     * 修改采购预付款登记
     * 
     * @param sysApPrepayment 采购预付款登记
     * @return 结果
     */
    public int updateSysApPrepayment(SysApPrepayment sysApPrepayment);

    /**
     * 批量删除采购预付款登记
     * 
     * @param ids 需要删除的采购预付款登记主键集合
     * @return 结果
     */
    public int deleteSysApPrepaymentByIds(Long[] ids);

    /**
     * 删除采购预付款登记信息
     * 
     * @param id 采购预付款登记主键
     * @return 结果
     */
    public int deleteSysApPrepaymentById(Long id);
}
