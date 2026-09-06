package com.template.system.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 采购订单-主对象 sys_po_header
 *
 * @author template
 * @date 2025-10-04
 */
public class SysPoHeader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String code;

    /** 供货商ID */
    @Excel(name = "供货商ID")
    private Long supplierId;

    /** 订货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 交货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    /** 业务员 */
    @Excel(name = "业务员")
    private String salesman;

    /** 操作员 */
    @Excel(name = "操作员")
    private String operator;

    /** 审核人 */
    @Excel(name = "审核人")
    private String approver;

    /** 作废人 */
    @Excel(name = "作废人")
    private String voidBy;

    /** 是否作废(0否,1是) */
    @Excel(name = "是否作废(0否,1是)")
    private Integer isVoid;

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
    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }
    public void setDeliveryDate(Date deliveryDate)
    {
        this.deliveryDate = deliveryDate;
    }

    public Date getDeliveryDate()
    {
        return deliveryDate;
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
    public void setApprover(String approver)
    {
        this.approver = approver;
    }

    public String getApprover()
    {
        return approver;
    }
    public void setVoidBy(String voidBy)
    {
        this.voidBy = voidBy;
    }

    public String getVoidBy()
    {
        return voidBy;
    }
    public void setIsVoid(Integer isVoid)
    {
        this.isVoid = isVoid;
    }

    public Integer getIsVoid()
    {
        return isVoid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("supplierId", getSupplierId())
            .append("orderDate", getOrderDate())
            .append("deliveryDate", getDeliveryDate())
            .append("salesman", getSalesman())
            .append("operator", getOperator())
            .append("approver", getApprover())
            .append("voidBy", getVoidBy())
            .append("isVoid", getIsVoid())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
