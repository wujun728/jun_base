package com.aidex.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.utils.NumberUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.system.domain.SysCommonUseMenu;
import com.aidex.system.mapper.SysCommonUseMenuMapper;
import com.aidex.system.service.SysCommonUseMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 常用菜单配置Service业务层处理
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-04-22
 */
@Service
@Transactional(readOnly = true)
public class SysCommonUseMenuServiceImpl extends BaseServiceImpl<SysCommonUseMenuMapper, SysCommonUseMenu> implements SysCommonUseMenuService
{
    private static final Logger log = LoggerFactory.getLogger(SysCommonUseMenuServiceImpl.class);
    /**
     * 获取单条数据
     * @param sysCommonUseMenu
     * @return
     */
    @Override
    public SysCommonUseMenu get(SysCommonUseMenu sysCommonUseMenu)
    {
        return super.get(sysCommonUseMenu);
    }

    /**
     * 查询常用菜单配置列表
     * @param sysCommonUseMenu
     * @return
     */
    @Override
    public List<SysCommonUseMenu> findList(SysCommonUseMenu sysCommonUseMenu)
    {
        return super.findList(sysCommonUseMenu);
    }

    /**
     * 分页查询常用菜单配置列表
     * @param sysCommonUseMenu
     * @return
     */
    @Override
    public PageInfo<SysCommonUseMenu> findPage(SysCommonUseMenu sysCommonUseMenu)
    {
        return super.findPage(sysCommonUseMenu);
    }

    /**
     * 保存常用菜单配置
     * @param sysCommonUseMenu
     * @return
     */
    @Override
    public boolean save(SysCommonUseMenu sysCommonUseMenu)
    {
      if(StringUtils.isBlank(sysCommonUseMenu.getId())){
         int maxSort = this.findMaxSort(sysCommonUseMenu);
         sysCommonUseMenu.setSort(maxSort);
       }
        return super.save(sysCommonUseMenu);
    }

    /**
     * 删除常用菜单配置信息
     * @param sysCommonUseMenu
     * @return
     */
    @Override
    public boolean remove(SysCommonUseMenu sysCommonUseMenu)
    {
        return super.remove(sysCommonUseMenu);
    }

    /**
     * 批量删除常用菜单配置
     * @param ids 需要删除的常用菜单配置ID
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public int deleteSysCommonUseMenuByIds(String[] ids)
    {
        return mapper.deleteSysCommonUseMenuByIds(ids);
    }

    /**
     * 获取最大编号
     * @return
     */
    public int findMaxSort(SysCommonUseMenu sysCommonUseMenu)
    {
        return NumberUtils.nextOrder(mapper.findMaxSort(sysCommonUseMenu));
    }


    @Override
    @Transactional(readOnly = false)
    public void editSort(List<SysCommonUseMenu> sysCommonUseMenuList){
        if(!CollectionUtils.isEmpty(sysCommonUseMenuList)){
            int initSort = 10;
            for (int i=0;i< sysCommonUseMenuList.size();i++) {
                SysCommonUseMenu sysCommonUseMenu = sysCommonUseMenuList.get(i);
                sysCommonUseMenu.setSort((i+1)*initSort);
            }
            mapper.batchEditSort(sysCommonUseMenuList);
        }
    }
}
