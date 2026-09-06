package com.template.system.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 借出单-主对象 sys_loan_header
 *
 * @author template
 * @date 2025-10-04
 */
public class SysLoanHeader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 借出单据号 */
    @Excel(name = "借出单据号")
    private String code;

    /** 借货人 */
    @Excel(name = "借货人")
    private String borrower;

    /** 借货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loanDate;

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
    public void setBorrower(String borrower)
    {
        this.borrower = borrower;
    }

    public String getBorrower()
    {
        return borrower;
    }
    public void setLoanDate(Date loanDate)
    {
        this.loanDate = loanDate;
    }

    public Date getLoanDate()
    {
        return loanDate;
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
            .append("borrower", getBorrower())
            .append("loanDate", getLoanDate())
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
