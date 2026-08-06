package com.ruoyi.pay.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 订单对象 pay_order
 * 
 * @author geek
 * @date 2024-06-11
 */
@Schema(description = "订单对象")
public class PayOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    @Schema(title = "订单ID")
    private Long orderId;

    /** 订单号 */
    @Schema(title = "商户订单号")
    @Excel(name = "商户订单号")
    private String orderNumber;

    /** 第三方订单号 */
    @Schema(title = "第三方订单号")
    @Excel(name = "第三方订单号")
    private String thirdNumber;

    /** 订单状态 */
    @Schema(title = "订单状态")
    @Excel(name = "订单状态")
    private String orderStatus;

    /** 订单总金额 */
    @Schema(title = "订单总金额")
    @Excel(name = "订单总金额")
    private String totalAmount;

    /** 实际支付金额 */
    @Schema(title = "实际支付金额")
    @Excel(name = "实际支付金额")
    private String actualAmount;

    /** 订单内容 */
    @Schema(title = "订单内容")
    @Excel(name = "订单内容")
    private String orderContent;

    /** 负载信息 */
    @Schema(title = "负载信息")
    @Excel(name = "负载信息")
    private String orderMessage;

    /** 支付方式 */
    @Schema(title = "支付方式")
    @Excel(name = "支付方式")
    private String payType;

    /** 支付时间 */
    @Schema(title = "支付时间")
    @Excel(name = "支付时间")
    private java.util.Date payTime;

    /** 支付人 */
    @Schema(title = "支付人")
    @Excel(name = "支付人")
    private String payBy;

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setThirdNumber(String thirdNumber) {
        this.thirdNumber = thirdNumber;
    }

    public String getThirdNumber() {
        return thirdNumber;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderMessage(String orderMessage) {
        this.orderMessage = orderMessage;
    }

    public String getOrderMessage() {
        return orderMessage;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayTime(java.util.Date payTime) {
        this.payTime = payTime;
    }

    public java.util.Date getPayTime() {
        return payTime;
    }

    public void setPayBy(String payBy) {
        this.payBy = payBy;
    }

    public String getPayBy() {
        return payBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("orderId", getOrderId())
                .append("orderNumber", getOrderNumber())
                .append("orderStatus", getOrderStatus())
                .append("totalAmount", getTotalAmount())
                .append("actualAmount", getActualAmount())
                .append("orderContent", getOrderContent())
                .append("orderMessage", getOrderMessage())
                .append("payType", getPayType())
                .append("payTime", getPayTime())
                .append("payBy", getPayBy())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
