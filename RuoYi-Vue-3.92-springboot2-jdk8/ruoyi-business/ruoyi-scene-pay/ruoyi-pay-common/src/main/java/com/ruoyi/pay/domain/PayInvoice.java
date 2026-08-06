package com.ruoyi.pay.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 发票对象 pay_invoice
 * 
 * @author geek
 * @date 2024-06-11
 */
@Schema(description = "发票对象")
public class PayInvoice extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 发票id */
    @Schema(title = "发票id")
    private Long invoiceId;

    /** 订单号 */
    @Schema(title = "订单号")
    @Excel(name = "订单号")
    private String orderNumber;

    /** 发票类型 */
    @Schema(title = "发票类型")
    @Excel(name = "发票类型")
    private String invoiceType;

    /** 发票抬头 */
    @Schema(title = "发票抬头")
    @Excel(name = "发票抬头")
    private String invoiceHeader;

    /** 纳税人识别号 */
    @Schema(title = "纳税人识别号")
    @Excel(name = "纳税人识别号")
    private String invoiceNumber;

    /** 收票人手机号 */
    @Schema(title = "收票人手机号")
    @Excel(name = "收票人手机号")
    private String invoicePhone;

    /** 收票人邮箱 */
    @Schema(title = "收票人邮箱")
    @Excel(name = "收票人邮箱")
    private String invoiceEmail;

    /** 发票备注 */
    @Schema(title = "发票备注")
    @Excel(name = "发票备注")
    private String invoiceRemark;
    public void setInvoiceId(Long invoiceId) 
    {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceId() 
    {
        return invoiceId;
    }


    public void setOrderNumber(String orderNumber) 
    {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() 
    {
        return orderNumber;
    }


    public void setInvoiceType(String invoiceType) 
    {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() 
    {
        return invoiceType;
    }


    public void setInvoiceHeader(String invoiceHeader) 
    {
        this.invoiceHeader = invoiceHeader;
    }

    public String getInvoiceHeader() 
    {
        return invoiceHeader;
    }


    public void setInvoiceNumber(String invoiceNumber) 
    {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNumber() 
    {
        return invoiceNumber;
    }


    public void setInvoicePhone(String invoicePhone) 
    {
        this.invoicePhone = invoicePhone;
    }

    public String getInvoicePhone() 
    {
        return invoicePhone;
    }


    public void setInvoiceEmail(String invoiceEmail) 
    {
        this.invoiceEmail = invoiceEmail;
    }

    public String getInvoiceEmail() 
    {
        return invoiceEmail;
    }


    public void setInvoiceRemark(String invoiceRemark) 
    {
        this.invoiceRemark = invoiceRemark;
    }

    public String getInvoiceRemark() 
    {
        return invoiceRemark;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("invoiceId", getInvoiceId())
            .append("orderNumber", getOrderNumber())
            .append("invoiceType", getInvoiceType())
            .append("invoiceHeader", getInvoiceHeader())
            .append("invoiceNumber", getInvoiceNumber())
            .append("invoicePhone", getInvoicePhone())
            .append("invoiceEmail", getInvoiceEmail())
            .append("invoiceRemark", getInvoiceRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
