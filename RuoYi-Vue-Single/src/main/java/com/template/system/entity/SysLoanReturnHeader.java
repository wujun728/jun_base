package com.template.system.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 归还单-主对象 sys_loan_return_header
 *
 * @author template
 * @date 2025-10-04
 */
public class SysLoanReturnHeader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 归还单据号 */
    @Excel(name = "归还单据号")
    private String code;

    /** 还货人 */
    @Excel(name = "还货人")
    private String returner;

    /** 还货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "还货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnDate;

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
    public void setReturner(String returner)
    {
        this.returner = returner;
    }

    public String getReturner()
    {
        return returner;
    }
    public void setReturnDate(Date returnDate)
    {
        this.returnDate = returnDate;
    }

    public Date getReturnDate()
    {
        return returnDate;
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
            .append("returner", getReturner())
            .append("returnDate", getReturnDate())
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
