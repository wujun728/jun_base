package com.ruoyi.pay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.pay.domain.PayOrder;
import com.ruoyi.pay.mapper.PayOrderMapper;
import com.ruoyi.pay.service.IPayOrderService;

/**
 * 订单Service业务层处理
 * 
 * @author geek
 * @date 2024-06-11
 */
@Service
public class PayOrderServiceImpl implements IPayOrderService {
    @Autowired
    private PayOrderMapper payOrderMapper;

    /**
     * 查询订单
     * 
     * @param orderId 订单主键
     * @return 订单
     */
    @Override
    public PayOrder selectPayOrderByOrderId(Long orderId) {
        return payOrderMapper.selectPayOrderByOrderId(orderId);
    }

    /**
     * 查询订单
     * 
     * @param orderNumber 订单号
     * @return 订单集合
     */
    @Override
    public PayOrder selectPayOrderByOrderNumber(String orderNumber) {
        PayOrder payOrder = payOrderMapper.selectPayOrderByOrderNumber(orderNumber);
        if (payOrder == null) {
            throw new ServiceException("订单不存在，请检查订单号是否正确");
        } else {
            return payOrder;
        }
    }

    /**
     * 查询订单列表
     * 
     * @param payOrder 订单
     * @return 订单
     */
    @Override
    public List<PayOrder> selectPayOrderList(PayOrder payOrder) {
        return payOrderMapper.selectPayOrderList(payOrder);
    }

    /**
     * 新增订单
     * 
     * @param payOrder 订单
     * @return 结果
     */
    @Override
    public int insertPayOrder(PayOrder payOrder) {
        return payOrderMapper.insertPayOrder(payOrder);
    }

    /**
     * 修改订单
     * 
     * @param payOrder 订单
     * @return 结果
     */
    @Override
    public int updatePayOrder(PayOrder payOrder) {
        payOrder.setUpdateTime(DateUtils.getNowDate());
        return payOrderMapper.updatePayOrder(payOrder);
    }

    /**
     * 批量删除订单
     * 
     * @param orderIds 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deletePayOrderByOrderIds(Long[] orderIds) {
        return payOrderMapper.deletePayOrderByOrderIds(orderIds);
    }

    /**
     * 删除订单信息
     * 
     * @param orderId 订单主键
     * @return 结果
     */
    @Override
    public int deletePayOrderByOrderId(Long orderId) {
        return payOrderMapper.deletePayOrderByOrderId(orderId);
    }

    /**
     * 删除订单信息
     * 
     * @param orderIds 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deletePayOrderByOrderNumber(String orderNumber) {
        return payOrderMapper.deletePayOrderByOrderNumber(orderNumber);
    }

    @Override
    public int updateStatus(String orderNumber, String orderStatus) {
        return payOrderMapper.updateStatus(orderNumber, orderStatus);
    }
}
