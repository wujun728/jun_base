package com.aidex.system.service;

import com.aidex.common.core.service.BaseService;
import com.aidex.system.domain.SysConfig;

/**
 * 参数配置 服务层
 *
 * @author ruoyi
 */
public interface SysConfigService extends BaseService<SysConfig> {




    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    public void deleteConfigByIds(String[] configIds);

    /**
     * 刷新缓存
     * @return 结果
     */
    public void refreshCache();

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数信息
     * @return 结果
     */
    public void checkConfigKeyUnique(SysConfig config);

    /**
     * 加载参数缓存数据
     */
    public void loadingConfigCache();
}
