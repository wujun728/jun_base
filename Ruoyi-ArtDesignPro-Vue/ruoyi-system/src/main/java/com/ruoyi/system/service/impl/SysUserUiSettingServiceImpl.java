package com.ruoyi.system.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.domain.SysUserUiSetting;
import com.ruoyi.system.mapper.SysUserUiSettingMapper;
import com.ruoyi.system.service.ISysUserUiSettingService;

/**
 * 用户界面偏好 服务层实现
 */
@Service
public class SysUserUiSettingServiceImpl implements ISysUserUiSettingService
{
    @Autowired
    private SysUserUiSettingMapper settingMapper;

    @Override
    public Map<String, String> selectByUserId(Long userId)
    {
        Map<String, String> values = new LinkedHashMap<>();
        settingMapper.selectByUserId(userId)
            .forEach(setting -> values.put(setting.getConfigKey(), setting.getConfigValue()));
        return values;
    }

    @Override
    @Transactional
    public void save(Long userId, String username, Map<String, String> values)
    {
        values.forEach((key, value) -> {
            SysUserUiSetting setting = new SysUserUiSetting();
            setting.setUserId(userId);
            setting.setConfigKey(key);
            setting.setConfigValue(value);
            setting.setUpdateBy(username);
            settingMapper.upsert(setting);
        });
    }
}
