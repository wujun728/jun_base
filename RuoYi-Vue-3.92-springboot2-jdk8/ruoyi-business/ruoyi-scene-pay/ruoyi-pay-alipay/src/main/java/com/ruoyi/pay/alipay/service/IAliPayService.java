package com.ruoyi.pay.alipay.service;

import java.util.Map;

import com.ruoyi.pay.service.PayService;

public interface IAliPayService extends PayService {
    public void callback(Map<String, String> params);
}
