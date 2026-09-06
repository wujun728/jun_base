package com.aidex.system.service.impl;

import com.aidex.framework.cache.TableConfigUtils;
import com.github.pagehelper.PageInfo;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.system.domain.SysTableConfig;
import com.aidex.system.mapper.SysTableConfigMapper;
import com.aidex.system.service.SysTableConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 个性化配置Service业务层处理
 * @date 2021-03-31
 */
@Service
public class SysTableConfigServiceImpl extends BaseServiceImpl<SysTableConfigMapper, SysTableConfig> implements SysTableConfigService
{

    /**
     * 获取单条数据
     * @param sysTableConfig
     * @return
     */
    @Override
    public SysTableConfig get(SysTableConfig sysTableConfig)
    {
        return super.get(sysTableConfig);
    }
    @Override
    public SysTableConfig getInfoByTableKey (String tableKey)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return TableConfigUtils.getTableConfig(loginUser.getUser().getId(),tableKey);
    }

    @Override
    public SysTableConfig getInfoByTableKeyFormDb (String tableKey)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysTableConfig sysTableConfig = new SysTableConfig();
        sysTableConfig.setTableKey(tableKey);
        sysTableConfig.setUserId(loginUser.getUser().getId());
        List<SysTableConfig> sysTableConfigList = mapper.getInfoByTableKey(sysTableConfig);
        if(!CollectionUtils.isEmpty(sysTableConfigList)){
            return sysTableConfigList.get(0);
        }else{
            return null;
        }
    }
    /**
     * 查询个性化配置列表
     * @param sysTableConfig
     * @return
     */
    @Override
    public List<SysTableConfig> findList(SysTableConfig sysTableConfig)
    {
        return super.findList(sysTableConfig);
    }

    /**
     * 分页查询个性化配置列表
     * @param sysTableConfig
     * @return 个性化配置
     */
    @Override
    public PageInfo<SysTableConfig> findPage(SysTableConfig sysTableConfig)
    {
        return super.findPage(sysTableConfig);
    }

    /**
     * 保存个性化配置
     * @param sysTableConfig
     * @return 结果
     */
    @Override
    public boolean save(SysTableConfig sysTableConfig)
    {
        boolean saveFlag  = false;
        try{
            saveFlag  = super.save(sysTableConfig);
            TableConfigUtils.setTableConfig(sysTableConfig);
        }catch (Exception e){
           //保存异常清除缓存
            TableConfigUtils.clearCacheByKey(sysTableConfig);
        }
        return saveFlag;
    }

    /**
     * 删除个性化配置信息
     * @param sysTableConfig
     * @return 结果
     */
    @Override
    public boolean remove(SysTableConfig sysTableConfig)
    {
        return super.remove(sysTableConfig);
    }

    /**
     * 批量删除个性化配置
     * @param ids 需要删除的个性化配置ID
     * @return 结果
     */
    @Transactional(readOnly = false)
    @Override
    public int deleteSysTableConfigByIds(String[] ids)
    {
        return mapper.deleteSysTableConfigByIds(ids);
    }
}