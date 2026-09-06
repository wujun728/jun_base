package com.template.system.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 盘点单-明细对象 sys_stock_count_line
 *
 * @author template
 * @date 2025-10-04
 */
public class SysStockCountLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 盘点单ID */
    @Excel(name = "盘点单ID")
    private Long countId;

    /** 行号 */
    @Excel(name = "行号")
    private Long lineNo;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品编号(冗余) */
    @Excel(name = "产品编号(冗余)")
    private String productCode;

    /** 名称(冗余) */
    @Excel(name = "名称(冗余)")
    private String productName;

    /** 简拼(冗余) */
    @Excel(name = "简拼(冗余)")
    private String shortName;

    /** 仓库ID */
    @Excel(name = "仓库ID")
    private Long warehouseId;

    /** 当前库存数量 */
    @Excel(name = "当前库存数量")
    private BigDecimal qtyOnHand;

    /** 实际库存数量 */
    @Excel(name = "实际库存数量")
    private BigDecimal qtyActual;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCountId(Long countId)
    {
        this.countId = countId;
    }

    public Long getCountId()
    {
        return countId;
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
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getShortName()
    {
        return shortName;
    }
    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public Long getWarehouseId()
    {
        return warehouseId;
    }
    public void setQtyOnHand(BigDecimal qtyOnHand)
    {
        this.qtyOnHand = qtyOnHand;
    }

    public BigDecimal getQtyOnHand()
    {
        return qtyOnHand;
    }
    public void setQtyActual(BigDecimal qtyActual)
    {
        this.qtyActual = qtyActual;
    }

    public BigDecimal getQtyActual()
    {
        return qtyActual;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("countId", getCountId())
            .append("lineNo", getLineNo())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("shortName", getShortName())
            .append("warehouseId", getWarehouseId())
            .append("qtyOnHand", getQtyOnHand())
            .append("qtyActual", getQtyActual())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
