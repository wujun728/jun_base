package com.ruoyi.pay.mapper;

import java.util.List;

import com.ruoyi.pay.domain.PayOrder;

/**
 * 订单Mapper接口
 * 
 * @author Dftre
 * @date 2024-02-15
 */
public interface PayOrderMapper 
{
    /**
     * 查询订单
     * 
     * @param orderId 订单主键
     * @return 订单
     */
    public PayOrder selectPayOrderByOrderId(Long orderId);

    /**
     * 查询订单
     * 
     * @param orderNumber 订单号
     * @return 订单集合
     */
    public PayOrder selectPayOrderByOrderNumber(String orderNumber);

    /**
     * 查询订单列表
     * 
     * @param payOrder 订单
     * @return 订单集合
     */
    public List<PayOrder> selectPayOrderList(PayOrder payOrder);

    /**
     * 新增订单
     * 
     * @param payOrder 订单
     * @return 结果
     */
    public int insertPayOrder(PayOrder payOrder);

    /**
     * 修改订单
     * 
     * @param payOrder 订单
     * @return 结果
     */
    public int updatePayOrder(PayOrder payOrder);

    /**
     * 删除订单
     * 
     * @param orderId 订单主键
     * @return 结果
     */
    public int deletePayOrderByOrderId(Long orderId);

    /**
     * 批量删除订单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePayOrderByOrderIds(Long[] orderIds);

    public int deletePayOrderByOrderNumber(String orderNumber);
    public int updateStatus(String orderNumber, String orderStatus);
}
