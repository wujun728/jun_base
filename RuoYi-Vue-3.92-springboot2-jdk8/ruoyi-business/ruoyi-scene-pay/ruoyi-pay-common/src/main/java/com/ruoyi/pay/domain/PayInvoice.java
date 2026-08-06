package com.ruoyi.pay.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Api;

/**
 * 鍙戠エ瀵硅薄 pay_invoice
 * 
 * @author geek
 * @date 2024-06-11
 */
@Api(tags = "鍙戠エ瀵硅薄")
public class PayInvoice extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 鍙戠エid */
    @ApiModelProperty("鍙戠エid")
    private Long invoiceId;

    /** 璁㈠崟鍙?*/
    @ApiModelProperty("璁㈠崟鍙?)
    @Excel(name = "璁㈠崟鍙?)
    private String orderNumber;

    /** 鍙戠エ绫诲瀷 */
    @ApiModelProperty("鍙戠エ绫诲瀷")
    @Excel(name = "鍙戠エ绫诲瀷")
    private String invoiceType;

    /** 鍙戠エ鎶ご */
    @ApiModelProperty("鍙戠エ鎶ご")
    @Excel(name = "鍙戠エ鎶ご")
    private String invoiceHeader;

    /** 绾崇◣浜鸿瘑鍒彿 */
    @ApiModelProperty("绾崇◣浜鸿瘑鍒彿")
    @Excel(name = "绾崇◣浜鸿瘑鍒彿")
    private String invoiceNumber;

    /** 鏀剁エ浜烘墜鏈哄彿 */
    @ApiModelProperty("鏀剁エ浜烘墜鏈哄彿")
    @Excel(name = "鏀剁エ浜烘墜鏈哄彿")
    private String invoicePhone;

    /** 鏀剁エ浜洪偖绠?*/
    @ApiModelProperty("鏀剁エ浜洪偖绠?)
    @Excel(name = "鏀剁エ浜洪偖绠?)
    private String invoiceEmail;

    /** 鍙戠エ澶囨敞 */
    @ApiModelProperty("鍙戠エ澶囨敞")
    @Excel(name = "鍙戠エ澶囨敞")
    private String invoiceRemark;
    public void setInvoiceId(Long invoiceId) 
    {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceId() 
    {
        return invoiceId;
    }


    public void setOrderNumber(String orderNumber) 
    {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() 
    {
        return orderNumber;
    }


    public void setInvoiceType(String invoiceType) 
    {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() 
    {
        return invoiceType;
    }


    public void setInvoiceHeader(String invoiceHeader) 
    {
        this.invoiceHeader = invoiceHeader;
    }

    public String getInvoiceHeader() 
    {
        return invoiceHeader;
    }


    public void setInvoiceNumber(String invoiceNumber) 
    {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNumber() 
    {
        return invoiceNumber;
    }


    public void setInvoicePhone(String invoicePhone) 
    {
        this.invoicePhone = invoicePhone;
    }

    public String getInvoicePhone() 
    {
        return invoicePhone;
    }


    public void setInvoiceEmail(String invoiceEmail) 
    {
        this.invoiceEmail = invoiceEmail;
    }

    public String getInvoiceEmail() 
    {
        return invoiceEmail;
    }


    public void setInvoiceRemark(String invoiceRemark) 
    {
        this.invoiceRemark = invoiceRemark;
    }

    public String getInvoiceRemark() 
    {
        return invoiceRemark;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("invoiceId", getInvoiceId())
            .append("orderNumber", getOrderNumber())
            .append("invoiceType", getInvoiceType())
            .append("invoiceHeader", getInvoiceHeader())
            .append("invoiceNumber", getInvoiceNumber())
            .append("invoicePhone", getInvoicePhone())
            .append("invoiceEmail", getInvoiceEmail())
            .append("invoiceRemark", getInvoiceRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
