package com.ruoyi.pay.mapper;

import java.util.List;

import com.ruoyi.pay.domain.PayInvoice;

/**
 * 发票Mapper接口
 * 
 * @author geek
 * @date 2024-06-11
 */
public interface PayInvoiceMapper 
{
    /**
     * 查询发票
     * 
     * @param invoiceId 发票主键
     * @return 发票
     */
    public PayInvoice selectPayInvoiceByInvoiceId(Long invoiceId);

    /**
     * 查询发票列表
     * 
     * @param payInvoice 发票
     * @return 发票集合
     */
    public List<PayInvoice> selectPayInvoiceList(PayInvoice payInvoice);

    /**
     * 新增发票
     * 
     * @param payInvoice 发票
     * @return 结果
     */
    public int insertPayInvoice(PayInvoice payInvoice);

    /**
     * 修改发票
     * 
     * @param payInvoice 发票
     * @return 结果
     */
    public int updatePayInvoice(PayInvoice payInvoice);

    /**
     * 删除发票
     * 
     * @param invoiceId 发票主键
     * @return 结果
     */
    public int deletePayInvoiceByInvoiceId(Long invoiceId);

    /**
     * 批量删除发票
     * 
     * @param invoiceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePayInvoiceByInvoiceIds(Long[] invoiceIds);
}
