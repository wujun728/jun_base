package com.ruoyi.pay.service;

import java.util.List;

import com.ruoyi.pay.domain.PayOrder;

/**
 * 订单Service接口
 * 
 * @author geek
 * @date 2024-06-11
 */
public interface IPayOrderService 
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
     * 批量删除订单
     * 
     * @param orderIds 需要删除的订单主键集合
     * @return 结果
     */
    public int deletePayOrderByOrderIds(Long[] orderIds);

    
    /**
     * 删除订单信息
     * 
     * @param orderId 订单主键
     * @return 结果
     */
    public int deletePayOrderByOrderNumber(String orderNumber);

    /**
     * 删除订单信息
     * 
     * @param orderId 订单主键
     * @return 结果
     */
    public int deletePayOrderByOrderId(Long orderId);
    public int updateStatus(String orderNumber, String orderStatus);
}
