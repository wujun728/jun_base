package com.ruoyi.pay.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Api;

/**
 * 璁㈠崟瀵硅薄 pay_order
 * 
 * @author geek
 * @date 2024-06-11
 */
@Api(tags = "璁㈠崟瀵硅薄")
public class PayOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 璁㈠崟ID */
    @ApiModelProperty("璁㈠崟ID")
    private Long orderId;

    /** 璁㈠崟鍙?*/
    @ApiModelProperty("鍟嗘埛璁㈠崟鍙?)
    @Excel(name = "鍟嗘埛璁㈠崟鍙?)
    private String orderNumber;

    /** 绗笁鏂硅鍗曞彿 */
    @ApiModelProperty("绗笁鏂硅鍗曞彿")
    @Excel(name = "绗笁鏂硅鍗曞彿")
    private String thirdNumber;

    /** 璁㈠崟鐘舵€?*/
    @ApiModelProperty("璁㈠崟鐘舵€?)
    @Excel(name = "璁㈠崟鐘舵€?)
    private String orderStatus;

    /** 璁㈠崟鎬婚噾棰?*/
    @ApiModelProperty("璁㈠崟鎬婚噾棰?)
    @Excel(name = "璁㈠崟鎬婚噾棰?)
    private String totalAmount;

    /** 瀹為檯鏀粯閲戦 */
    @ApiModelProperty("瀹為檯鏀粯閲戦")
    @Excel(name = "瀹為檯鏀粯閲戦")
    private String actualAmount;

    /** 璁㈠崟鍐呭 */
    @ApiModelProperty("璁㈠崟鍐呭")
    @Excel(name = "璁㈠崟鍐呭")
    private String orderContent;

    /** 璐熻浇淇℃伅 */
    @ApiModelProperty("璐熻浇淇℃伅")
    @Excel(name = "璐熻浇淇℃伅")
    private String orderMessage;

    /** 鏀粯鏂瑰紡 */
    @ApiModelProperty("鏀粯鏂瑰紡")
    @Excel(name = "鏀粯鏂瑰紡")
    private String payType;

    /** 鏀粯鏃堕棿 */
    @ApiModelProperty("鏀粯鏃堕棿")
    @Excel(name = "鏀粯鏃堕棿")
    private java.util.Date payTime;

    /** 鏀粯浜?*/
    @ApiModelProperty("鏀粯浜?)
    @Excel(name = "鏀粯浜?)
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
