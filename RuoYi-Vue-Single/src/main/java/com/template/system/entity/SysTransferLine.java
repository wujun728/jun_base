package com.template.system.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 调拨单-明细对象 sys_transfer_line
 *
 * @author template
 * @date 2025-10-04
 */
public class SysTransferLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 调拨单ID */
    @Excel(name = "调拨单ID")
    private Long transferId;

    /** 行号 */
    @Excel(name = "行号")
    private Long lineNo;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品编码(冗余) */
    @Excel(name = "产品编码(冗余)")
    private String productCode;

    /** 名称(冗余) */
    @Excel(name = "名称(冗余)")
    private String productName;

    /** 调拨数量 */
    @Excel(name = "调拨数量")
    private BigDecimal qty;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTransferId(Long transferId)
    {
        this.transferId = transferId;
    }

    public Long getTransferId()
    {
        return transferId;
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
    public void setQty(BigDecimal qty)
    {
        this.qty = qty;
    }

    public BigDecimal getQty()
    {
        return qty;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("transferId", getTransferId())
            .append("lineNo", getLineNo())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("qty", getQty())
            .append("price", getPrice())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
