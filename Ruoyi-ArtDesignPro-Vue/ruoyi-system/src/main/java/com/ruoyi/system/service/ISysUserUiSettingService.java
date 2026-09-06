package com.ruoyi.system.service;

import java.util.Map;

/**
 * 用户界面偏好 服务层
 */
public interface ISysUserUiSettingService
{
    public Map<String, String> selectByUserId(Long userId);

    public void save(Long userId, String username, Map<String, String> values);
}
