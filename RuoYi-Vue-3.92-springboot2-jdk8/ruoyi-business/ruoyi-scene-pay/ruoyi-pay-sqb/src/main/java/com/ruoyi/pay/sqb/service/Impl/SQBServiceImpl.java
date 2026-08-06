package com.ruoyi.pay.sqb.service.Impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.ruoyi.common.core.text.CharsetKit;
import com.ruoyi.common.exception.ServiceException;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.pay.domain.PayOrder;
import com.ruoyi.pay.service.IPayOrderService;
import com.ruoyi.pay.sqb.config.SqbConfig;
import com.ruoyi.pay.sqb.service.ISqbPayService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Slf4j
@Service("pay:service:sqb")
@ConditionalOnProperty(prefix = "pay.sqb", name = "enabled", havingValue = "true")
public class SQBServiceImpl implements ISqbPayService {
    @Autowired
    private SqbConfig sqbConfig;

    @Autowired
    private IPayOrderService payOrderService;

    /**
     * http POST 请求
     * 
     * @param url:请求地址
     * @param body:    body实体字符串
     * @param sign:签名
     * @param sn:      序列号
     * @return
     */
    public static JsonNode httpPost(String url, Object body, String sign, String sn)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String xmlRes = "{}";
        try {
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", sn + " " + sign);
            // 使用新的 HttpUtils.postJson 支持 headers
            xmlRes = HttpUtils.postJson(url, body, header);
        } catch (Exception e) {
        }
        return JSON.parseObject(xmlRes);
    }

    /**
     * 计算字符串的MD5值
     *
     * @param signStr:签名字符串
     * @return
     */
    private String getSign(String signStr) {
        try {
            String md5 = Md5Utils.encryptMd5(signStr);
            return md5;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 终端激活
     *
     * @param code:激活码
     * @return {terminal_sn:"$终端号",terminal_key:"$终端密钥"}
     */
    public JsonNode activate(String code, String deviceId, String clientSn, String name) {
        String url = sqbConfig.getApiDomain() + "/terminal/activate";
        ObjectNode params = JSON.createObjectNode();
        try {
            params.put("app_id", sqbConfig.getAppId()); // app_id，必填
            params.put("code", code); // 激活码，必填
            params.put("device_id", deviceId); // 客户方收银终端序列号，需保证同一app_id下唯一，必填。为方便识别，建议格式为“品牌名+门店编号+‘POS’+POS编号“
            params.put("client_sn", clientSn); // 客户方终端编号，一般客户方或系统给收银终端的编号，必填
            params.put("name", name); // 客户方终端名称，必填
            String sign = getSign(params.toString() + sqbConfig.getVendorKey());
            JsonNode retObj = httpPost(url, params.toString(), sign, sqbConfig.getVendorSn());
            String resCode = retObj.get("result_code").toString();
            if (!"200".equals(resCode)) {
                return null;
            }
            String responseStr = retObj.get("biz_response").toString();
            JsonNode terminal = JSON.parseObject(responseStr);
            if (terminal.get("terminal_sn") == null || terminal.get("terminal_key") == null) {
                return null;
            }
            return terminal;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 终端签到
     *
     * @return {terminal_sn:"$终端号",terminal_key:"$终端密钥"}
     */
    public JsonNode checkin() {
        String url = sqbConfig.getApiDomain() + "/terminal/checkin";
        ObjectNode params = JSON.createObjectNode();
        try {
            params.put("terminal_sn", sqbConfig.getTerminalSn());
            params.put("device_id", "HUISUAN001POS01");
            params.put("os_info", "Mac OS");
            params.put("sdk_version", "Java SDK v1.0");
            String sign = getSign(params.toString() + sqbConfig.getTerminalKey());
            JsonNode retObj = httpPost(url, params.toString(), sign, sqbConfig.getTerminalSn());
            String resCode = retObj.get("result_code").toString();
            if (!"200".equals(resCode)) {
                return null;
            }
            String responseStr = retObj.get("biz_response").toString();
            JsonNode terminal = JSON.parseObject(responseStr);
            if (terminal.get("terminal_sn") == null || terminal.get("terminal_key") == null) {
                return null;
            }
            return terminal;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 退款
     *
     * @return
     */
    public PayOrder refund(PayOrder payOrder) {
        String url = sqbConfig.getApiDomain() + "/upay/v2/refund";
        ObjectNode params = JSON.createObjectNode();
        try {
            params.put("terminal_sn", sqbConfig.getTerminalSn()); // 收钱吧终端ID
            params.put("client_sn", payOrder.getOrderNumber()); // 商户系统订单号,必须在商户系统内唯一；且长度不超过64字节
            params.put("refund_amount", payOrder.getTotalAmount()); // 退款金额
            params.put("refund_request_no", "1"); // 商户退款所需序列号,表明是第几次退款
            params.put("operator", "kay"); // 门店操作员

            String sign = getSign(params.toString() + sqbConfig.getTerminalKey());
            JsonNode retObj = httpPost(url, params, sign, sqbConfig.getTerminalSn());
            JsonNode bizResponse = retObj.get("biz_response");
            if ("REFUNDED".equals(bizResponse.get("order_status").asString())) {
                payOrderService.updateStatus(payOrder.getOrderNumber(), "已退款");
            } else {
                log.error("退款失败");
            }
            return payOrder;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询
     *
     * @return
     */
    @Override
    public PayOrder query(PayOrder payOrder) {
        String url = sqbConfig.getApiDomain() + "/upay/v2/query";
        ObjectNode params = JSON.createObjectNode();
        try {
            params.put("terminal_sn", sqbConfig.getTerminalSn()); // 终端号
            params.put("client_sn", payOrder.getOrderNumber()); // 商户系统订单号,必须在商户系统内唯一；且长度不超过64字节
            System.out.println(params.toString() + sqbConfig.getTerminalKey());
            String sign = getSign(params.toString() + sqbConfig.getTerminalKey());
            JsonNode retObj = httpPost(url, params, sign, sqbConfig.getTerminalSn());
            String resCode = retObj.get("result_code").toString();
            if (!"200".equals(resCode)) {
                throw new ServiceException("查询支付订单失败");
            } else {
                JsonNode response = retObj.get("biz_response");
                System.out.println(response);
            }
            return payOrder;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String payUrl(PayOrder payOrder) {
        return payUrl(payOrder, null);
    }

    public String payUrl(PayOrder payOrder, String notifyBaseUrl) {
        if (payOrder.getRemark() == null) {
            payOrder.setRemark("支付");
        }
        // sqbConfig.getNotifyUrl() + payOrder.getOrderNumber() + "/pay";
        StringBuilder notifyUrlBuilder = new StringBuilder();
        String orderNotifyUrl = notifyUrlBuilder.append(sqbConfig.getNotifyUrl())
                .append("/").append(payOrder.getOrderNumber())
                .append("/pay").toString();
        String param = "" +
                "client_sn=" + payOrder.getOrderNumber() +
                "&notify_url=" + orderNotifyUrl +
                "&operator=" + payOrder.getCreateBy() +
                "&return_url=" + "https://www.shouqianba.com/" +
                "&subject=" + payOrder.getRemark() +
                "&terminal_sn=" + sqbConfig.getTerminalSn() +
                "&total_amount=" + Long.valueOf(payOrder.getTotalAmount().toString());
        String urlParam;
        try {
            urlParam = "" +
                    "client_sn=" + payOrder.getOrderNumber() +
                    "&notify_url=" + URLEncoder.encode(orderNotifyUrl, CharsetKit.UTF_8) +
                    "&operator=" + URLEncoder.encode(payOrder.getCreateBy(), CharsetKit.UTF_8) +
                    "&return_url=" + "https://www.shouqianba.com/" +
                    "&subject=" + URLEncoder.encode(payOrder.getRemark(), CharsetKit.UTF_8) +
                    "&terminal_sn=" + sqbConfig.getTerminalSn() +
                    "&total_amount=" + Long.valueOf(payOrder.getTotalAmount().toString());

            String sign = getSign(param + "&key=" + sqbConfig.getTerminalKey());
            return "https://qr.shouqianba.com/gateway?" + urlParam + "&sign=" + sign.toUpperCase();
        } catch (Exception e) {
            throw new ServiceException("生成收钱吧支付链接失败");
        }
    }

    /**
     * 预下单
     *
     * @return
     */
    public String precreate(PayOrder payOrder, String sn, String payway) {
        String url = sqbConfig.getApiDomain() + "/upay/v2/precreate";
        ObjectNode params = JSON.createObjectNode();
        try {
            params.put("terminal_sn", sqbConfig.getTerminalSn()); // 收钱吧终端ID
            params.put("client_sn", payOrder.getOrderNumber()); // 商户系统订单号,必须在商户系统内唯一；且长度不超过32字节
            params.put("total_amount", payOrder.getTotalAmount()); // 交易总金额
            // params.put("payway", payway); // 支付方式
            params.put("subject", "无简介"); // 交易简介
            params.put("operator", SecurityUtils.getUsername()); // 门店操作员

            String sign = getSign(params.toString() + sqbConfig.getTerminalKey());
            JsonNode retObj = httpPost(url, params.toString(), sign, sqbConfig.getTerminalSn());
            return retObj.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 自动撤单
     *
     * @param terminal_sn:终端号
     * @param terminal_key:终端密钥
     * @return
     */
    public String cancel(String terminal_sn, String terminal_key) {
        String url = sqbConfig.getApiDomain() + "/upay/v2/cancel";
        ObjectNode params = JSON.createObjectNode();
        try {
            params.put("terminal_sn", terminal_sn); // 终端号
            params.put("client_sn", "18348290098298292838"); // 商户系统订单号,必须在商户系统内唯一；且长度不超过64字节

            String sign = getSign(params.toString() + terminal_key);
            JsonNode retObj = httpPost(url, params.toString(), sign, terminal_sn);

            return retObj.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateSign(String data, String sign) {
        try {
            // 使用SHA256WithRSA算法
            Signature signature = Signature.getInstance("SHA256WithRSA");

            // 获取公钥
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey localPublicKey = keyFactory
                    .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(sqbConfig.getPublicKey())));
            // 初始化验证过程
            signature.initVerify(localPublicKey);
            signature.update(data.getBytes());

            // 解码签名
            byte[] bytesSign = Base64.getDecoder().decode(sign);

            // 验证签名
            return signature.verify(bytesSign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String notify(HttpServletRequest request, HttpServletResponse response, PayOrder payOrder, String type) {
        try {
            String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            JsonNode jsonObject = JSON.parseObject(requestBody);
            String sign = request.getHeader("Authorization");
            if (!validateSign(requestBody, sign)) {
                throw new ServiceException("收钱吧支付回调验签失败");
            }
            System.out.println(jsonObject);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
    }

}
