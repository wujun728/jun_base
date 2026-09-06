package com.template.system.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 采购进货单-主对象 sys_grn_header
 *
 * @author template
 * @date 2025-10-04
 */
public class SysGrnHeader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 进货单号 */
    @Excel(name = "进货单号")
    private String code;

    /** 供货商ID */
    @Excel(name = "供货商ID")
    private Long supplierId;

    /** 进货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "进货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receivedDate;

    /** 业务员 */
    @Excel(name = "业务员")
    private String salesman;

    /** 操作员 */
    @Excel(name = "操作员")
    private String operator;

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
    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }
    public void setReceivedDate(Date receivedDate)
    {
        this.receivedDate = receivedDate;
    }

    public Date getReceivedDate()
    {
        return receivedDate;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("supplierId", getSupplierId())
            .append("receivedDate", getReceivedDate())
            .append("salesman", getSalesman())
            .append("operator", getOperator())
            .append("approver", getApprover())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
