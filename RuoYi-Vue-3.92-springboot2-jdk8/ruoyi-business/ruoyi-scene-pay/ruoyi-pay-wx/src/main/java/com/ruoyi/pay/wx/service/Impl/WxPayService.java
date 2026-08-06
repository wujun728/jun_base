package com.ruoyi.pay.wx.service.Impl;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.ruoyi.pay.domain.PayOrder;
import com.ruoyi.pay.service.IPayOrderService;
import com.ruoyi.pay.wx.config.WxPayConfig;
import com.ruoyi.pay.wx.service.IWxPayService;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service("pay:service:wechat")
@ConditionalOnProperty(prefix = "pay.wechat", name = "enabled", havingValue = "true")
public class WxPayService implements IWxPayService {

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired
    private NativePayService nativePayService;

    @Autowired
    private WxPayConfig wxPayAppConfig;

    @Autowired
    private NotificationParser notificationParser;

    @Autowired
    private RefundService refundService;

    @Override
    public String payUrl(PayOrder payOrder) {
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(Integer.parseInt(payOrder.getTotalAmount()));
        request.setAmount(amount);
        request.setAppid(wxPayAppConfig.getAppId());
        request.setMchid(wxPayAppConfig.getMerchantId());
        request.setDescription(payOrder.getOrderContent());
        StringBuilder notifyUrlBuilder = new StringBuilder();
        String orderNotifyUrl = notifyUrlBuilder.append(wxPayAppConfig.getNotifyUrl())
                .append("/").append(payOrder.getOrderNumber())
                .append("/pay").toString();
        request.setNotifyUrl(orderNotifyUrl);
        request.setOutTradeNo(payOrder.getOrderNumber());
        PrepayResponse response = nativePayService.prepay(request);
        return response.getCodeUrl();
    }

    private String getTradeState(Transaction.TradeStateEnum tradeState) {
        return switch (tradeState) {
            case SUCCESS -> "已支付";
            case REFUND -> "已退款";
            case NOTPAY -> "未支付";
            case CLOSED -> "已关闭";
            case REVOKED -> "已撤销";
            case USERPAYING -> "用户支付中";
            case PAYERROR -> "支付错误";
            case ACCEPT -> "已接收待支付";
            default -> "未知状态" + tradeState;
        };
    }

    @Override
    public String notify(HttpServletRequest request, HttpServletResponse response, PayOrder payOrder, String type) {
        String timeStamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String signature = request.getHeader("Wechatpay-Signature");
        String certSn = request.getHeader("Wechatpay-Serial");
        try {
            String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(certSn)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timeStamp)
                    .body(requestBody)
                    .build();
            if (type.equals("pay")) {
                Transaction transaction = notificationParser.parse(requestParam, Transaction.class);
                // String orderNumber = transaction.getOutTradeNo();
                payOrder.setOrderStatus(getTradeState(transaction.getTradeState()));
                payOrder.setThirdNumber(transaction.getTransactionId());
                payOrder.setActualAmount(transaction.getAmount().getTotal().toString());
                payOrderService.updatePayOrder(payOrder);
                return "success";

            } else if (type.equals("refund")) {
                RefundNotification transaction = notificationParser.parse(requestParam, RefundNotification.class);
                // String orderNumber = transaction.getOutTradeNo();
                payOrder.setOrderStatus(switch (transaction.getRefundStatus()) {
                    case SUCCESS -> "已退款";
                    case PROCESSING -> "退款处理中";
                    case CLOSED -> "退款关闭";
                    case ABNORMAL -> "退款异常";
                    default -> "未知状态" + transaction.getRefundStatus();
                });
                payOrderService.updatePayOrder(payOrder);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

    }

    @Override
    public PayOrder query(PayOrder payOrder) {
        QueryOrderByIdRequest queryRequest = new QueryOrderByIdRequest();
        queryRequest.setMchid(wxPayAppConfig.getMerchantId());
        queryRequest.setTransactionId(payOrder.getThirdNumber());
        try {
            Transaction result = nativePayService.queryOrderById(queryRequest);
            payOrder.setActualAmount(result.getAmount().getTotal().toString());
            payOrder.setOrderStatus(getTradeState(result.getTradeState()));
            payOrderService.updatePayOrder(payOrder);
        } catch (ServiceException e) {
            System.out.printf("code=[%s], message=[%s]\n", e.getErrorCode(), e.getErrorMessage());
            System.out.printf("reponse body=[%s]\n", e.getResponseBody());
        }
        return payOrder;
    }

    @Override
    public PayOrder refund(PayOrder payOrder) {
        CreateRequest request = new CreateRequest();
        request.setTransactionId(payOrder.getThirdNumber());
        request.setOutRefundNo(payOrder.getOrderNumber());
        request.setOutTradeNo(payOrder.getOrderNumber());
        AmountReq amount = new AmountReq();
        amount.setCurrency("CNY");
        amount.setTotal(Long.valueOf(payOrder.getTotalAmount()));
        amount.setRefund(Long.valueOf(payOrder.getTotalAmount()));
        request.setAmount(amount);
        StringBuilder notifyUrlBuilder = new StringBuilder();
        String orderNotifyUrl = notifyUrlBuilder.append(wxPayAppConfig.getNotifyUrl())
                .append("/").append(payOrder.getOrderNumber())
                .append("/refund").toString();
        request.setNotifyUrl(orderNotifyUrl);
        Refund refund = refundService.create(request);
        String status = switch (refund.getStatus()) {
            case SUCCESS -> "已退款";
            case PROCESSING -> "退款处理中";
            case CLOSED -> "退款关闭";
            case ABNORMAL -> "退款异常";
            default -> "未知状态" + refund.getStatus();
        };
        payOrder.setOrderStatus(status);
        payOrderService.updateStatus(payOrder.getOrderNumber(), status);
        return payOrder;
    }

}
