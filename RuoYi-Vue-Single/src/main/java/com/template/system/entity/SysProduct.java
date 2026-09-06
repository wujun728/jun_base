package com.template.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 商品对象 sys_product
 *
 * @author template
 * @date 2025-10-04
 */
public class SysProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String code;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String name;

    /** 简拼 */
    @Excel(name = "简拼")
    private String shortName;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String unit;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specModel;

    /** 厂家 */
    @Excel(name = "厂家")
    private String manufacturer;

    /** 公开报价 */
    @Excel(name = "公开报价")
    private BigDecimal pricePublic;

    /** 成本价 */
    @Excel(name = "成本价")
    private BigDecimal priceCost;

    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

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
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getShortName()
    {
        return shortName;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setSpecModel(String specModel)
    {
        this.specModel = specModel;
    }

    public String getSpecModel()
    {
        return specModel;
    }
    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }
    public void setPricePublic(BigDecimal pricePublic)
    {
        this.pricePublic = pricePublic;
    }

    public BigDecimal getPricePublic()
    {
        return pricePublic;
    }
    public void setPriceCost(BigDecimal priceCost)
    {
        this.priceCost = priceCost;
    }

    public BigDecimal getPriceCost()
    {
        return priceCost;
    }
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("shortName", getShortName())
            .append("unit", getUnit())
            .append("specModel", getSpecModel())
            .append("manufacturer", getManufacturer())
            .append("pricePublic", getPricePublic())
            .append("priceCost", getPriceCost())
            .append("updateDate", getUpdateDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
