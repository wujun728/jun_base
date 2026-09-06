package com.template.system.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 借出单-明细对象 sys_loan_line
 *
 * @author template
 * @date 2025-10-04
 */
public class SysLoanLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 借出单ID */
    @Excel(name = "借出单ID")
    private Long loanId;

    /** 行号 */
    @Excel(name = "行号")
    private Long lineNo;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品编码(冗余) */
    @Excel(name = "产品编码(冗余)")
    private String productCode;

    /** 产品名称(冗余) */
    @Excel(name = "产品名称(冗余)")
    private String productName;

    /** 产品规格 */
    @Excel(name = "产品规格")
    private String spec;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String unit;

    /** 产品数量 */
    @Excel(name = "产品数量")
    private BigDecimal qty;

    /** 已还数量 */
    @Excel(name = "已还数量")
    private BigDecimal qtyReturned;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setLoanId(Long loanId)
    {
        this.loanId = loanId;
    }

    public Long getLoanId()
    {
        return loanId;
    }
    public void setLineNo(Long lineNo)
    {
        this.lineNo = lineNo;
    }

    public Long getLineNo()
    {
        return lineNo;
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
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductName()
    {
        return productName;
    }
    public void setSpec(String spec)
    {
        this.spec = spec;
    }

    public String getSpec()
    {
        return spec;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setQty(BigDecimal qty)
    {
        this.qty = qty;
    }

    public BigDecimal getQty()
    {
        return qty;
    }
    public void setQtyReturned(BigDecimal qtyReturned)
    {
        this.qtyReturned = qtyReturned;
    }

    public BigDecimal getQtyReturned()
    {
        return qtyReturned;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("loanId", getLoanId())
            .append("lineNo", getLineNo())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("spec", getSpec())
            .append("unit", getUnit())
            .append("qty", getQty())
            .append("qtyReturned", getQtyReturned())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
