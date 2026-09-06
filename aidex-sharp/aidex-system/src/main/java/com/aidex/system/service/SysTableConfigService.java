package com.aidex.system.service;

import com.aidex.common.core.service.BaseService;
import com.aidex.system.domain.SysTableConfig;

/**
 * 个性化配置Service接口
 *
 * @date 2021-03-31
 */
public interface SysTableConfigService extends BaseService<SysTableConfig>
{
    /**
     * 批量删除个性化配置
     *
     * @param tableKeys 需要删除的个性化配置ID
     * @return 结果
     */
    public int deleteSysTableConfigByIds(String[] tableKeys);

    public SysTableConfig getInfoByTableKey(String tableKey);

    /**
     * 从数据库加载数据
     * @param tableKey
     * @return
     */
    public SysTableConfig getInfoByTableKeyFormDb(String tableKey);


}
