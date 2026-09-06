package com.template.system.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 采购进货单-明细对象 sys_grn_line
 *
 * @author template
 * @date 2025-10-04
 */
public class SysGrnLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 进货单ID */
    @Excel(name = "进货单ID")
    private Long grnId;

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

    /** 简拼(冗余) */
    @Excel(name = "简拼(冗余)")
    private String shortName;

    /** 规格 */
    @Excel(name = "规格")
    private String spec;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String unit;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal qty;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 厂家 */
    @Excel(name = "厂家")
    private String manufacturer;

    /** 仓库ID */
    @Excel(name = "仓库ID")
    private Long warehouseId;

    /** 货位 */
    @Excel(name = "货位")
    private String location;

    /** 税率(%) */
    @Excel(name = "税率(%)")
    private BigDecimal taxRate;

    /** 关联采购订单明细ID */
    @Excel(name = "关联采购订单明细ID")
    private Long poLineId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setGrnId(Long grnId)
    {
        this.grnId = grnId;
    }

    public Long getGrnId()
    {
        return grnId;
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
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }
    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public Long getWarehouseId()
    {
        return warehouseId;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }
    public void setTaxRate(BigDecimal taxRate)
    {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxRate()
    {
        return taxRate;
    }
    public void setPoLineId(Long poLineId)
    {
        this.poLineId = poLineId;
    }

    public Long getPoLineId()
    {
        return poLineId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("grnId", getGrnId())
            .append("lineNo", getLineNo())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("shortName", getShortName())
            .append("spec", getSpec())
            .append("unit", getUnit())
            .append("qty", getQty())
            .append("price", getPrice())
            .append("manufacturer", getManufacturer())
            .append("warehouseId", getWarehouseId())
            .append("location", getLocation())
            .append("taxRate", getTaxRate())
            .append("poLineId", getPoLineId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
