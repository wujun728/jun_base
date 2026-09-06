package com.template.system.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.template.framework.annotation.Excel;
import com.template.framework.core.domain.BaseEntity;

/**
 * 调拨单-主对象 sys_transfer_header
 *
 * @author template
 * @date 2025-10-04
 */
public class SysTransferHeader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 调拨单据号 */
    @Excel(name = "调拨单据号")
    private String code;

    /** 目标仓库 */
    @Excel(name = "目标仓库")
    private Long dstWarehouseId;

    /** 源仓库 */
    @Excel(name = "源仓库")
    private Long srcWarehouseId;

    /** 调拨日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "调拨日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transferDate;

    /** 调拨人 */
    @Excel(name = "调拨人")
    private String transferBy;

    /** 操作员 */
    @Excel(name = "操作员")
    private String operator;

    /** 审核人 */
    @Excel(name = "审核人")
    private String approver;

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
    public void setDstWarehouseId(Long dstWarehouseId)
    {
        this.dstWarehouseId = dstWarehouseId;
    }

    public Long getDstWarehouseId()
    {
        return dstWarehouseId;
    }
    public void setSrcWarehouseId(Long srcWarehouseId)
    {
        this.srcWarehouseId = srcWarehouseId;
    }

    public Long getSrcWarehouseId()
    {
        return srcWarehouseId;
    }
    public void setTransferDate(Date transferDate)
    {
        this.transferDate = transferDate;
    }

    public Date getTransferDate()
    {
        return transferDate;
    }
    public void setTransferBy(String transferBy)
    {
        this.transferBy = transferBy;
    }

    public String getTransferBy()
    {
        return transferBy;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
    public void setApprover(String approver)
    {
        this.approver = approver;
    }

    public String getApprover()
    {
        return approver;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("dstWarehouseId", getDstWarehouseId())
            .append("srcWarehouseId", getSrcWarehouseId())
            .append("transferDate", getTransferDate())
            .append("transferBy", getTransferBy())
            .append("operator", getOperator())
            .append("approver", getApprover())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
