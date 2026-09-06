package com.template.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 采购预付款登记对象 sys_ap_prepayment
 *
 * @author template
 * @date 2025-10-04
 */
public class SysApPrepayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 预付款单据号 */
    @Excel(name = "预付款单据号")
    private String code;

    /** 原始单据号 */
    @Excel(name = "原始单据号")
    private String originCode;

    /** 供货商ID */
    @Excel(name = "供货商ID")
    private Long supplierId;

    /** 付款方式 */
    @Excel(name = "付款方式")
    private String payMethod;

    /** 付款金额 */
    @Excel(name = "付款金额")
    private BigDecimal amount;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payDate;

    /** 业务员 */
    @Excel(name = "业务员")
    private String salesman;

    /** 操作员 */
    @Excel(name = "操作员")
    private String operator;

    /** 付款单据号(冲抵来源) */
    @Excel(name = "付款单据号(冲抵来源)")
    private String paymentVoucherCode;

    /** 审核人 */
    @Excel(name = "审核人")
    private String approver;

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
    public void setOriginCode(String originCode)
    {
        this.originCode = originCode;
    }

    public String getOriginCode()
    {
        return originCode;
    }
    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }
    public void setPayMethod(String payMethod)
    {
        this.payMethod = payMethod;
    }

    public String getPayMethod()
    {
        return payMethod;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setPayDate(Date payDate)
    {
        this.payDate = payDate;
    }

    public Date getPayDate()
    {
        return payDate;
    }
    public void setSalesman(String salesman)
    {
        this.salesman = salesman;
    }

    public String getSalesman()
    {
        return salesman;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
    public void setPaymentVoucherCode(String paymentVoucherCode)
    {
        this.paymentVoucherCode = paymentVoucherCode;
    }

    public String getPaymentVoucherCode()
    {
        return paymentVoucherCode;
    }
    public void setApprover(String approver)
    {
        this.approver = approver;
    }

    public String getApprover()
    {
        return approver;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("originCode", getOriginCode())
            .append("supplierId", getSupplierId())
            .append("payMethod", getPayMethod())
            .append("amount", getAmount())
            .append("payDate", getPayDate())
            .append("salesman", getSalesman())
            .append("operator", getOperator())
            .append("paymentVoucherCode", getPaymentVoucherCode())
            .append("approver", getApprover())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
