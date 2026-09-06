package com.template.system.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 归还单-明细对象 sys_loan_return_line
 *
 * @author template
 * @date 2025-10-04
 */
public class SysLoanReturnLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 归还单ID */
    @Excel(name = "归还单ID")
    private Long loanReturnId;

    /** 行号 */
    @Excel(name = "行号")
    private Long lineNo;

    /** 借出单据号 */
    @Excel(name = "借出单据号")
    private String loanCode;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品编码(冗余) */
    @Excel(name = "产品编码(冗余)")
    private String productCode;

    /** 归还数量 */
    @Excel(name = "归还数量")
    private BigDecimal qty;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setLoanReturnId(Long loanReturnId)
    {
        this.loanReturnId = loanReturnId;
    }

    public Long getLoanReturnId()
    {
        return loanReturnId;
    }
    public void setLineNo(Long lineNo)
    {
        this.lineNo = lineNo;
    }

    public Long getLineNo()
    {
        return lineNo;
    }
    public void setLoanCode(String loanCode)
    {
        this.loanCode = loanCode;
    }

    public String getLoanCode()
    {
        return loanCode;
    }
    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getProductId()
    {
        return productId;
    }
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getProductCode()
    {
        return productCode;
    }
    public void setQty(BigDecimal qty)
    {
        this.qty = qty;
    }

    public BigDecimal getQty()
    {
        return qty;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("loanReturnId", getLoanReturnId())
            .append("lineNo", getLineNo())
            .append("loanCode", getLoanCode())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("qty", getQty())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
