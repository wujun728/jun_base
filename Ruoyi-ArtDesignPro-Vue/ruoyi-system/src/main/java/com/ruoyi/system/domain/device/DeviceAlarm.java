package com.ruoyi.system.domain.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报警器 device_alarm
 */
public class DeviceAlarm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "报警器ID")
    private Long alarmId;

    @Excel(name = "报警器名称")
    private String alarmName;

    @Excel(name = "报警器编码")
    private String alarmCode;

    @Excel(name = "报警器类型")
    private String alarmType;

    @Excel(name = "IP地址")
    private String ipAddr;

    @Excel(name = "安装位置")
    private String installPosition;

    @Excel(name = "溜井ID")
    private Long chuteId;

    @Excel(name = "溜井名称")
    private String chuteName;

    @Excel(name = "部门ID")
    private Long deptId;

    @Excel(name = "部门名称")
    private String deptName;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    private String delFlag;

    public Long getAlarmId()
    {
        return alarmId;
    }

    public void setAlarmId(Long alarmId)
    {
        this.alarmId = alarmId;
    }

    @NotBlank(message = "报警器名称不能为空")
    @Size(max = 100, message = "报警器名称不能超过100个字符")
    public String getAlarmName()
    {
        return alarmName;
    }

    public void setAlarmName(String alarmName)
    {
        this.alarmName = alarmName;
    }

    @NotBlank(message = "报警器编码不能为空")
    @Size(max = 64, message = "报警器编码不能超过64个字符")
    public String getAlarmCode()
    {
        return alarmCode;
    }

    public void setAlarmCode(String alarmCode)
    {
        this.alarmCode = alarmCode;
    }

    public String getAlarmType()
    {
        return alarmType;
    }

    public void setAlarmType(String alarmType)
    {
        this.alarmType = alarmType;
    }

    @NotBlank(message = "IP地址不能为空")
    @Size(max = 128, message = "IP地址不能超过128个字符")
    public String getIpAddr()
    {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr)
    {
        this.ipAddr = ipAddr;
    }

    public String getInstallPosition()
    {
        return installPosition;
    }

    public void setInstallPosition(String installPosition)
    {
        this.installPosition = installPosition;
    }

    @NotNull(message = "对应溜井不能为空")
    public Long getChuteId()
    {
        return chuteId;
    }

    public void setChuteId(Long chuteId)
    {
        this.chuteId = chuteId;
    }

    public String getChuteName()
    {
        return chuteName;
    }

    public void setChuteName(String chuteName)
    {
        this.chuteName = chuteName;
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
