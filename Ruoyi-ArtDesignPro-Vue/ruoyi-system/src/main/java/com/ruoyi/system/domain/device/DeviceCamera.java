package com.ruoyi.system.domain.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 摄像头 device_camera
 */
public class DeviceCamera extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    public static final String MASKED_PASSWORD = "******";

    @Excel(name = "摄像头ID")
    private Long cameraId;

    @Excel(name = "摄像头名称")
    private String cameraName;

    @Excel(name = "IP地址")
    private String ipAddr;

    @Excel(name = "用户名")
    private String username;

    private String password;

    @Excel(name = "RTSP流地址")
    private String rtspUrl;

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

    public Long getCameraId()
    {
        return cameraId;
    }

    public void setCameraId(Long cameraId)
    {
        this.cameraId = cameraId;
    }

    @NotBlank(message = "摄像头名称不能为空")
    @Size(max = 100, message = "摄像头名称不能超过100个字符")
    public String getCameraName()
    {
        return cameraName;
    }

    public void setCameraName(String cameraName)
    {
        this.cameraName = cameraName;
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

    @NotBlank(message = "用户名不能为空")
    @Size(max = 64, message = "用户名不能超过64个字符")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @NotBlank(message = "RTSP流地址不能为空")
    @Size(max = 500, message = "RTSP流地址不能超过500个字符")
    public String getRtspUrl()
    {
        return rtspUrl;
    }

    public void setRtspUrl(String rtspUrl)
    {
        this.rtspUrl = rtspUrl;
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
