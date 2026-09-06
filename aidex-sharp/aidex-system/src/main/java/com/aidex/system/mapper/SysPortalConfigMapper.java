package com.aidex.system.mapper;

import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.system.domain.SysPortalConfig;

import java.util.List;

/**
 * 多栏目门户配置Mapper接口
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-05-11
 */
public interface SysPortalConfigMapper extends BaseMapper<SysPortalConfig>
{
    /**
     * 批量删除多栏目门户配置
     * @param ids 需要删除的数据ID
     * @return
     */
    public int deleteSysPortalConfigByIds(String[] ids);

    /**
     * 获取最大编号
     * @param sysPortalConfig
     * @return
     */
    public Integer findMaxSort(SysPortalConfig sysPortalConfig);

    /**
     * 获取当前登陆人可以看到的所有首页
     * @param sysPortalConfig
     * @return
     */
    public List<SysPortalConfig> findUserConfigList(SysPortalConfig sysPortalConfig);

    public void updateNotDefaultByUser(SysPortalConfig sysPortalConfig);

    /**
     * 获取模板列表
     * @param sysPortalConfig
     * @return
     */
    public List<SysPortalConfig> getPortalTemplateList(SysPortalConfig sysPortalConfig);
}
