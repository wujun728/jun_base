package com.ruoyi.system.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户界面偏好表 sys_user_ui_setting
 */
public class SysUserUiSetting implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String configKey;

    private String configValue;

    private String updateBy;

    private Date updateTime;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
}
