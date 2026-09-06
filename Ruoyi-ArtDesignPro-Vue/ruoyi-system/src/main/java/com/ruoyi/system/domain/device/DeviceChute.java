package com.ruoyi.system.domain.device;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 溜井信息 device_chute
 */
public class DeviceChute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "溜井ID")
    private Long chuteId;

    @Excel(name = "溜井名称")
    private String chuteName;

    @Excel(name = "溜井编码")
    private String chuteCode;

    @Excel(name = "溜井高度")
    private BigDecimal chuteHeight;

    @Excel(name = "安装位置")
    private String location;

    @Excel(name = "部门ID")
    private Long deptId;

    @Excel(name = "部门名称")
    private String deptName;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    private String delFlag;

    public Long getChuteId()
    {
        return chuteId;
    }

    public void setChuteId(Long chuteId)
    {
        this.chuteId = chuteId;
    }

    @NotBlank(message = "溜井名称不能为空")
    @Size(max = 100, message = "溜井名称不能超过100个字符")
    public String getChuteName()
    {
        return chuteName;
    }

    public void setChuteName(String chuteName)
    {
        this.chuteName = chuteName;
    }

    @NotBlank(message = "溜井编码不能为空")
    @Size(max = 64, message = "溜井编码不能超过64个字符")
    public String getChuteCode()
    {
        return chuteCode;
    }

    public void setChuteCode(String chuteCode)
    {
        this.chuteCode = chuteCode;
    }

    @NotNull(message = "溜井高度不能为空")
    public BigDecimal getChuteHeight()
    {
        return chuteHeight;
    }

    public void setChuteHeight(BigDecimal chuteHeight)
    {
        this.chuteHeight = chuteHeight;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    @NotNull(message = "所属部门不能为空")
    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }
}
