package com.template.system.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 退库单-明细对象 sys_return_line
 *
 * @author template
 * @date 2025-10-04
 */
public class SysReturnLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 退库单ID */
    @Excel(name = "退库单ID")
    private Long returnId;

    /** 行号 */
    @Excel(name = "行号")
    private Long lineNo;

    /** 出货单单据号(参考) */
    @Excel(name = "出货单单据号(参考)")
    private String refOutboundCode;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品编码(冗余) */
    @Excel(name = "产品编码(冗余)")
    private String productCode;

    /** 产品名称(冗余) */
    @Excel(name = "产品名称(冗余)")
    private String productName;

    /** 规格 */
    @Excel(name = "规格")
    private String spec;

    /** 退库数量 */
    @Excel(name = "退库数量")
    private BigDecimal qty;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String unit;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setReturnId(Long returnId)
    {
        this.returnId = returnId;
    }

    public Long getReturnId()
    {
        return returnId;
    }
    public void setLineNo(Long lineNo)
    {
        this.lineNo = lineNo;
    }

    public Long getLineNo()
    {
        return lineNo;
    }
    public void setRefOutboundCode(String refOutboundCode)
    {
        this.refOutboundCode = refOutboundCode;
    }

    public String getRefOutboundCode()
    {
        return refOutboundCode;
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
    public void setQty(BigDecimal qty)
    {
        this.qty = qty;
    }

    public BigDecimal getQty()
    {
        return qty;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("returnId", getReturnId())
            .append("lineNo", getLineNo())
            .append("refOutboundCode", getRefOutboundCode())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("spec", getSpec())
            .append("qty", getQty())
            .append("unit", getUnit())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
