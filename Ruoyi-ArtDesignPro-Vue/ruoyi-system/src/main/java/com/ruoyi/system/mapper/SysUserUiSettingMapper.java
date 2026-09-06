package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysUserUiSetting;

/**
 * 用户界面偏好 数据层
 */
public interface SysUserUiSettingMapper
{
    public List<SysUserUiSetting> selectByUserId(Long userId);

    public int upsert(SysUserUiSetting setting);
}
