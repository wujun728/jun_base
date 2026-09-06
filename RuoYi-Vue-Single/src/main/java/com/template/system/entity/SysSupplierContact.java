package com.template.system.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 供货商联系人对象 sys_supplier_contact
 *
 * @author template
 * @date 2025-10-04
 */
public class SysSupplierContact extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 供应商ID */
    @Excel(name = "供应商ID")
    private Long supplierId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 职务 */
    @Excel(name = "职务")
    private String title;

    /** 性别 M/F */
    @Excel(name = "性别 M/F")
    private String gender;

    /** 合作开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合作开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startCoopDate;

    /** 电话1 */
    @Excel(name = "电话1")
    private String phone1;

    /** 电话2 */
    @Excel(name = "电话2")
    private String phone2;

    /** 移动电话 */
    @Excel(name = "移动电话")
    private String mobile;

    /** 电子邮件 */
    @Excel(name = "电子邮件")
    private String email;

    /** 传真 */
    @Excel(name = "传真")
    private String fax;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 爱好 */
    @Excel(name = "爱好")
    private String hobby;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }
    public void setStartCoopDate(Date startCoopDate)
    {
        this.startCoopDate = startCoopDate;
    }

    public Date getStartCoopDate()
    {
        return startCoopDate;
    }
    public void setPhone1(String phone1)
    {
        this.phone1 = phone1;
    }

    public String getPhone1()
    {
        return phone1;
    }
    public void setPhone2(String phone2)
    {
        this.phone2 = phone2;
    }

    public String getPhone2()
    {
        return phone2;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMobile()
    {
        return mobile;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getFax()
    {
        return fax;
    }
    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Date getBirthday()
    {
        return birthday;
    }
    public void setHobby(String hobby)
    {
        this.hobby = hobby;
    }

    public String getHobby()
    {
        return hobby;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("supplierId", getSupplierId())
            .append("name", getName())
            .append("title", getTitle())
            .append("gender", getGender())
            .append("startCoopDate", getStartCoopDate())
            .append("phone1", getPhone1())
            .append("phone2", getPhone2())
            .append("mobile", getMobile())
            .append("email", getEmail())
            .append("fax", getFax())
            .append("birthday", getBirthday())
            .append("hobby", getHobby())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
