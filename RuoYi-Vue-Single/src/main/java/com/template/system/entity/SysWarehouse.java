package com.template.system.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 仓库对象 sys_warehouse
 *
 * @author template
 * @date 2025-10-04
 */
public class SysWarehouse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String name;

    /** 说明 */
    @Excel(name = "说明")
    private String description;

    /** 仓库可容纳数量 */
    @Excel(name = "仓库可容纳数量")
    private BigDecimal capacityQty;

    /** 仓库地址 */
    @Excel(name = "仓库地址")
    private String address;

    /** 负责人 */
    @Excel(name = "负责人")
    private String managerName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setCapacityQty(BigDecimal capacityQty)
    {
        this.capacityQty = capacityQty;
    }

    public BigDecimal getCapacityQty()
    {
        return capacityQty;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    public String getManagerName()
    {
        return managerName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("description", getDescription())
            .append("capacityQty", getCapacityQty())
            .append("address", getAddress())
            .append("managerName", getManagerName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
