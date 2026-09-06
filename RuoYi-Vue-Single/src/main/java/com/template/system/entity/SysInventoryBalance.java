package com.template.system.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 库存现势(按仓库+产品)对象 sys_inventory_balance
 *
 * @author template
 * @date 2025-10-04
 */
public class SysInventoryBalance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 仓库ID */
    @Excel(name = "仓库ID")
    private Long warehouseId;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 当前库存数量 */
    @Excel(name = "当前库存数量")
    private BigDecimal qtyOnHand;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public Long getWarehouseId()
    {
        return warehouseId;
    }
    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getProductId()
    {
        return productId;
    }
    public void setQtyOnHand(BigDecimal qtyOnHand)
    {
        this.qtyOnHand = qtyOnHand;
    }

    public BigDecimal getQtyOnHand()
    {
        return qtyOnHand;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("warehouseId", getWarehouseId())
            .append("productId", getProductId())
            .append("qtyOnHand", getQtyOnHand())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
