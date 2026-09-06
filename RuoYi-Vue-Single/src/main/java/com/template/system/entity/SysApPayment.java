package com.template.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 采购付款登记对象 sys_ap_payment
 *
 * @author template
 * @date 2025-10-04
 */
public class SysApPayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 付款单据号 */
    @Excel(name = "付款单据号")
    private String code;

    /** 供货商ID */
    @Excel(name = "供货商ID")
    private Long supplierId;

    /** 业务员 */
    @Excel(name = "业务员")
    private String salesman;

    /** 付款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "付款日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payDate;

    /** 审核人 */
    @Excel(name = "审核人")
    private String approver;

    /** 收款人 */
    @Excel(name = "收款人")
    private String receiver;

    /** 票据号码 */
    @Excel(name = "票据号码")
    private String billNo;

    /** 票据金额 */
    @Excel(name = "票据金额")
    private BigDecimal billAmount;

    /** 发票号码 */
    @Excel(name = "发票号码")
    private String invoiceNo;

    /** 发票金额 */
    @Excel(name = "发票金额")
    private BigDecimal invoiceAmount;

    /** 操作员 */
    @Excel(name = "操作员")
    private String operator;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }
    public void setSalesman(String salesman)
    {
        this.salesman = salesman;
    }

    public String getSalesman()
    {
        return salesman;
    }
    public void setPayDate(Date payDate)
    {
        this.payDate = payDate;
    }

    public Date getPayDate()
    {
        return payDate;
    }
    public void setApprover(String approver)
    {
        this.approver = approver;
    }

    public String getApprover()
    {
        return approver;
    }
    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getReceiver()
    {
        return receiver;
    }
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }

    public String getBillNo()
    {
        return billNo;
    }
    public void setBillAmount(BigDecimal billAmount)
    {
        this.billAmount = billAmount;
    }

    public BigDecimal getBillAmount()
    {
        return billAmount;
    }
    public void setInvoiceNo(String invoiceNo)
    {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo()
    {
        return invoiceNo;
    }
    public void setInvoiceAmount(BigDecimal invoiceAmount)
    {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getInvoiceAmount()
    {
        return invoiceAmount;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("supplierId", getSupplierId())
            .append("salesman", getSalesman())
            .append("payDate", getPayDate())
            .append("approver", getApprover())
            .append("receiver", getReceiver())
            .append("billNo", getBillNo())
            .append("billAmount", getBillAmount())
            .append("invoiceNo", getInvoiceNo())
            .append("invoiceAmount", getInvoiceAmount())
            .append("operator", getOperator())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
