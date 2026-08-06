package com.ruoyi.pay.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.pay.domain.PayOrder;
import com.ruoyi.pay.service.IPayOrderService;
import com.ruoyi.pay.service.PayService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "支付业务")
@RequestMapping("/pay")
@RestController
public class PayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired(required = false)
    private Map<String, PayService> payServiceMap; // alipay wechat sqb

    @PostConstruct
    public void init() {
        if (payServiceMap == null) {
            payServiceMap = new HashMap<>();
            logger.warn("请注意，没有加载任何支付服务");
        } else {
            payServiceMap.forEach((k, v) -> {
                logger.info("已加载支付服务 {}", k);
            });
        }
    }

    @Operation(summary = "获取支付链接", description = "也不一定是链接，比如支付宝是一串表单代码")
    @Parameters({
            @Parameter(name = "channel", description = "支付方式", required = true),
            @Parameter(name = "orderNumber", description = "订单号", required = true)
    })
    @GetMapping("/{channel}/url/{orderNumber}")
    public R<String> url(@PathVariable String channel, @PathVariable String orderNumber) throws Exception {
        PayService payService = payServiceMap.get("pay:service:" + channel);
        PayOrder payOrder = payOrderService.selectPayOrderByOrderNumber(orderNumber);
        return R.ok(payService.payUrl(payOrder));
    }

    @Anonymous
    @Operation(summary = "支付/退款回调")
    @Parameters({
            @Parameter(name = "channel", description = "支付方式", required = true),
            @Parameter(name = "type", description = "通知类型", required = false)
    })
    @RequestMapping({
            "/{channel}/notify",
            "/{channel}/notify/{orderNumber}",
            "/{channel}/notify/{orderNumber}/{type}"
    })
    public String notify(
            @PathVariable String channel,
            @PathVariable(name = "orderNumber", required = false) String orderNumber,
            @PathVariable(name = "type", required = false) String type,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        logger.info("notify type:{} channel: {}, orderNumber: {}", type, channel, orderNumber);
        if (type == null) {
            type = "pay";
        }
        PayOrder payOrder = null;
        if (!StringUtils.isEmpty(orderNumber)) {
            payOrder = payOrderService.selectPayOrderByOrderNumber(orderNumber);
            payOrder.setPayType(channel);
        }
        PayService payService = payServiceMap.get("pay:service:" + channel);
        return payService.notify(request, response, payOrder, type);
    }

    @Operation(summary = "查询支付状态")
    @Parameters({
            @Parameter(name = "channel", description = "支付方式", required = true),
            @Parameter(name = "orderNumber", description = "订单号", required = true)
    })
    @PostMapping("/query/{orderNumber}")
    public R<PayOrder> query(@PathVariable(name = "orderNumber") String orderNumber)
            throws Exception {
        PayOrder payOrder = payOrderService.selectPayOrderByOrderNumber(orderNumber);
        String channel = payOrder.getPayType();
        if (StringUtils.isEmpty(channel)) {
            return R.ok(payOrder);
        }
        PayService payService = payServiceMap.get("pay:service:" + channel);
        return R.ok(payService.query(payOrder));
    }

    @Operation(summary = "退款")
    @PostMapping("/refund/{orderNumber}")
    @Parameters({
            @Parameter(name = "channel", description = "支付方式", required = true),
    })
    public AjaxResult refund(@PathVariable(name = "orderNumber") String orderNumber) {
        PayOrder payOrder = payOrderService.selectPayOrderByOrderNumber(orderNumber);
        String channel = payOrder.getPayType();
        if (StringUtils.isEmpty(channel)) {
            return error("该订单尚未支付，无法退款");
        }
        PayService payService = payServiceMap.get("pay:service:" + channel);
        return success(payService.refund(payOrder));
    }
}
