package com.ruoyi.pay.service;

import com.ruoyi.pay.domain.PayOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PayService {
    String payUrl(PayOrder payOrder);

    String notify(HttpServletRequest servletRequest, HttpServletResponse response, PayOrder payOrder, String type)
            throws Exception;

    PayOrder query(PayOrder payOrder);

    PayOrder refund(PayOrder payOrder);
}
