package com.aidex.system.mapper;

import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.system.domain.SysCommonUseMenu;
import com.aidex.system.domain.SysUserRole;

import java.util.List;

/**
 * 常用菜单配置Mapper接口
 * @author aidex
 * @date 2021-04-22
 */
public interface SysCommonUseMenuMapper extends BaseMapper<SysCommonUseMenu>
{
    /**
     * 批量删除常用菜单配置
     * @param ids 需要删除的数据ID
     * @return
     */
    public int deleteSysCommonUseMenuByIds(String[] ids);
    /**
     * 获取最大编号
     * @param sysCommonUseMenu
     * @return
     */
    public Integer findMaxSort(SysCommonUseMenu sysCommonUseMenu);

    public int batchEditSort(List<SysCommonUseMenu> sysCommonUseMenuList);

}
