package com.aidex.system.service;

import com.github.pagehelper.PageInfo;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.service.BaseService;
import com.aidex.system.domain.SysPortlet;
import com.aidex.system.mapper.SysPortalConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 门户小页管理Service接口
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-05-10
 */
public interface SysPortletService extends BaseService<SysPortlet>
{
    /**
     * 批量删除门户小页管理
     * @param ids 需要删除的门户小页管理ID
     * @return
     */
    public int deleteSysPortletByIds(String[] ids);

    /**
    * 获取最大编号
    * @param sysPortlet
    * @return
    */
    public int findMaxSort(SysPortlet sysPortlet);

    /**
    * 校验小页编码的唯一性
    * @param sysPortlet
    */
    public void checkCodeUnique(SysPortlet sysPortlet);


    public PageInfo<SysPortlet> findPageByRoleId(SysPortlet sysPortlet);

    public List<SysPortlet> getPortletListByAuth(String portalConfigId);

}
