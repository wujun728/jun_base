package com.ruoyi.pay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.pay.domain.PayInvoice;
import com.ruoyi.pay.mapper.PayInvoiceMapper;
import com.ruoyi.pay.service.IPayInvoiceService;

/**
 * 发票Service业务层处理
 * 
 * @author geek
 * @date 2024-06-11
 */
@Service
public class PayInvoiceServiceImpl implements IPayInvoiceService 
{
    @Autowired
    private PayInvoiceMapper payInvoiceMapper;

    /**
     * 查询发票
     * 
     * @param invoiceId 发票主键
     * @return 发票
     */
    @Override
    public PayInvoice selectPayInvoiceByInvoiceId(Long invoiceId)
    {
        return payInvoiceMapper.selectPayInvoiceByInvoiceId(invoiceId);
    }

    /**
     * 查询发票列表
     * 
     * @param payInvoice 发票
     * @return 发票
     */
    @Override
    public List<PayInvoice> selectPayInvoiceList(PayInvoice payInvoice)
    {
        return payInvoiceMapper.selectPayInvoiceList(payInvoice);
    }

    /**
     * 新增发票
     * 
     * @param payInvoice 发票
     * @return 结果
     */
    @Override
    public int insertPayInvoice(PayInvoice payInvoice)
    {
        payInvoice.setCreateTime(DateUtils.getNowDate());
        return payInvoiceMapper.insertPayInvoice(payInvoice);
    }

    /**
     * 修改发票
     * 
     * @param payInvoice 发票
     * @return 结果
     */
    @Override
    public int updatePayInvoice(PayInvoice payInvoice)
    {
        payInvoice.setUpdateTime(DateUtils.getNowDate());
        return payInvoiceMapper.updatePayInvoice(payInvoice);
    }

    /**
     * 批量删除发票
     * 
     * @param invoiceIds 需要删除的发票主键
     * @return 结果
     */
    @Override
    public int deletePayInvoiceByInvoiceIds(Long[] invoiceIds)
    {
        return payInvoiceMapper.deletePayInvoiceByInvoiceIds(invoiceIds);
    }

    /**
     * 删除发票信息
     * 
     * @param invoiceId 发票主键
     * @return 结果
     */
    @Override
    public int deletePayInvoiceByInvoiceId(Long invoiceId)
    {
        return payInvoiceMapper.deletePayInvoiceByInvoiceId(invoiceId);
    }
}
