package com.template.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 供货商对象 sys_supplier
 *
 * @author template
 * @date 2025-10-04
 */
public class SysSupplier extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String name;

    /** 供应商名称简拼 */
    @Excel(name = "供应商名称简拼")
    private String shortName;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 邮政编码 */
    @Excel(name = "邮政编码")
    private String postalCode;

    /** 电话 */
    @Excel(name = "电话")
    private String phone;

    /** 传真 */
    @Excel(name = "传真")
    private String fax;

    /** 网址 */
    @Excel(name = "网址")
    private String website;

    /** 电子邮件 */
    @Excel(name = "电子邮件")
    private String email;

    /** 地区 */
    @Excel(name = "地区")
    private String region;

    /** 注册资金 */
    @Excel(name = "注册资金")
    private BigDecimal registeredCapital;

    /** 法人代表 */
    @Excel(name = "法人代表")
    private String legalPerson;

    /** 开户银行 */
    @Excel(name = "开户银行")
    private String bankName;

    /** 银行帐号 */
    @Excel(name = "银行帐号")
    private String bankAccount;

    /** 银行地址 */
    @Excel(name = "银行地址")
    private String bankAddress;

    /** 纳税号 */
    @Excel(name = "纳税号")
    private String taxNo;

    /** 初次合作日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "初次合作日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date firstCoopDate;

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
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getShortName()
    {
        return shortName;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getPostalCode()
    {
        return postalCode;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getFax()
    {
        return fax;
    }
    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getWebsite()
    {
        return website;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getRegion()
    {
        return region;
    }
    public void setRegisteredCapital(BigDecimal registeredCapital)
    {
        this.registeredCapital = registeredCapital;
    }

    public BigDecimal getRegisteredCapital()
    {
        return registeredCapital;
    }
    public void setLegalPerson(String legalPerson)
    {
        this.legalPerson = legalPerson;
    }

    public String getLegalPerson()
    {
        return legalPerson;
    }
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getBankName()
    {
        return bankName;
    }
    public void setBankAccount(String bankAccount)
    {
        this.bankAccount = bankAccount;
    }

    public String getBankAccount()
    {
        return bankAccount;
    }
    public void setBankAddress(String bankAddress)
    {
        this.bankAddress = bankAddress;
    }

    public String getBankAddress()
    {
        return bankAddress;
    }
    public void setTaxNo(String taxNo)
    {
        this.taxNo = taxNo;
    }

    public String getTaxNo()
    {
        return taxNo;
    }
    public void setFirstCoopDate(Date firstCoopDate)
    {
        this.firstCoopDate = firstCoopDate;
    }

    public Date getFirstCoopDate()
    {
        return firstCoopDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("shortName", getShortName())
            .append("address", getAddress())
            .append("postalCode", getPostalCode())
            .append("phone", getPhone())
            .append("fax", getFax())
            .append("website", getWebsite())
            .append("email", getEmail())
            .append("region", getRegion())
            .append("registeredCapital", getRegisteredCapital())
            .append("legalPerson", getLegalPerson())
            .append("bankName", getBankName())
            .append("bankAccount", getBankAccount())
            .append("bankAddress", getBankAddress())
            .append("taxNo", getTaxNo())
            .append("firstCoopDate", getFirstCoopDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
