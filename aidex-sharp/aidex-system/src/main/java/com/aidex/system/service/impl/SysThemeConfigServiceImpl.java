package com.aidex.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.utils.NumberUtils;
import com.aidex.system.domain.SysThemeConfig;
import com.aidex.system.mapper.SysThemeConfigMapper;
import com.aidex.system.service.SysThemeConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户主题信息记录Service业务层处理
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-04-29
 */
@Service
@Transactional(readOnly = true)
public class SysThemeConfigServiceImpl extends BaseServiceImpl<SysThemeConfigMapper, SysThemeConfig> implements SysThemeConfigService
{
    private static final Logger log = LoggerFactory.getLogger(SysThemeConfigServiceImpl.class);

        /**
     * 获取单条数据
     * @param sysThemeConfig
     * @return
     */
    @Override
    public SysThemeConfig get(SysThemeConfig sysThemeConfig)
    {
        SysThemeConfig dto = super.get(sysThemeConfig);
                                                                                                                                                                                                                                                                                                        return dto;
    }

    /**
     * 获取单条数据
     * @param sysThemeConfig
     * @return
     */
    @Override
    public SysThemeConfig get(String id)
    {
        SysThemeConfig dto = super.get(id);
                                                                                                                                                                                                                                                                                                        return dto;
    }


    /**
     * 查询用户主题信息记录列表
     * @param sysThemeConfig
     * @return
     */
    @Override
    public List<SysThemeConfig> findList(SysThemeConfig sysThemeConfig)
    {
        return super.findList(sysThemeConfig);
    }

    /**
     * 分页查询用户主题信息记录列表
     * @param sysThemeConfig
     * @return
     */
    @Override
    public PageInfo<SysThemeConfig> findPage(SysThemeConfig sysThemeConfig)
    {
        return super.findPage(sysThemeConfig);
    }

    /**
     * 保存用户主题信息记录
     * @param sysThemeConfig
     * @return
     */
    @Override
    public boolean save(SysThemeConfig sysThemeConfig)
    {
        return super.save(sysThemeConfig);
    }

    /**
     * 删除用户主题信息记录信息
     * @param sysThemeConfig
     * @return
     */
    @Override
    public boolean remove(SysThemeConfig sysThemeConfig)
    {
        return super.remove(sysThemeConfig);
    }

    /**
     * 批量删除用户主题信息记录
     * @param ids 需要删除的用户主题信息记录ID
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public int deleteSysThemeConfigByIds(String[] ids)
    {
        return mapper.deleteSysThemeConfigByIds(ids);
    }

    /**
     * 获取最大编号
     * @param sysThemeConfig
     * @return
     */
    @Override
    public int findMaxSort(SysThemeConfig sysThemeConfig)
    {
        return NumberUtils.nextOrder(mapper.findMaxSort(sysThemeConfig));
    }
}
