package com.aidex.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.domain.entity.SysDept;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.exception.CustomException;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.framework.cache.DeptUtils;
import com.aidex.framework.cache.UserUtils;
import com.aidex.system.domain.SysPortalConfig;
import com.aidex.system.mapper.SysPortalConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aidex.system.mapper.SysPortletMapper;
import com.aidex.system.domain.SysPortlet;
import com.aidex.system.service.SysPortletService;
import org.springframework.transaction.annotation.Transactional;
import com.aidex.common.utils.NumberUtils;
import com.aidex.common.exception.BizException;
import org.springframework.util.CollectionUtils;

/**
 * 门户小页管理Service业务层处理
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-05-10
 */
@Service
@Transactional(readOnly = true)
public class SysPortletServiceImpl extends BaseServiceImpl<SysPortletMapper, SysPortlet> implements SysPortletService
{
    private static final Logger log = LoggerFactory.getLogger(SysPortletServiceImpl.class);

    @Autowired(required = false)
    private SysPortalConfigMapper sysPortalConfigMapper;

    /**
     * 获取单条数据
     * @param sysPortlet
     * @return
     */
    @Override
    public SysPortlet get(SysPortlet sysPortlet)
    {
        SysPortlet dto = super.get(sysPortlet);
                                                                                                                                                                                                                                                                                                                                                                                                                                return dto;
    }

    /**
     * 获取单条数据
     * @return
     */
    @Override
    public SysPortlet get(String id)
    {
        SysPortlet dto = super.get(id);
                                                                                                                                                                                                                                                                                                                                                                                                                                return dto;
    }


    /**
     * 查询门户小页管理列表
     * @param sysPortlet
     * @return
     */
    @Override
    public List<SysPortlet> findList(SysPortlet sysPortlet)
    {
        return super.findList(sysPortlet);
    }

    /**
     * 分页查询门户小页管理列表
     * @param sysPortlet
     * @return
     */
    @Override
    public PageInfo<SysPortlet> findPage(SysPortlet sysPortlet)
    {
        return super.findPage(sysPortlet);
    }

    /**
     * 保存门户小页管理
     * @param sysPortlet
     * @return
     */
    @Override
    public boolean save(SysPortlet sysPortlet)
    {
        checkCodeUnique(sysPortlet);
        return super.save(sysPortlet);
    }

    /**
     * 校验小页编码的唯一性
     * @param sysPortlet
     * @return
     */
    @Override
    public void checkCodeUnique(SysPortlet sysPortlet){
        SysPortlet sysPortletUnique = new SysPortlet();
        sysPortletUnique.setNotEqualId(sysPortlet.getId());
        sysPortletUnique.setCode(sysPortlet.getCode());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysPortletUnique))) {
            throw new BizException("小页编码已存在，请重新输入！");
        }
    }

    /**
     * 删除门户小页管理信息
     * @param sysPortlet
     * @return
     */
    @Override
    public boolean remove(SysPortlet sysPortlet)
    {
        return super.remove(sysPortlet);
    }

    /**
     * 批量删除门户小页管理
     * @param ids 需要删除的门户小页管理ID
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public int deleteSysPortletByIds(String[] ids)
    {
        return mapper.deleteSysPortletByIds(ids);
    }

    /**
     * 获取最大编号
     * @param sysPortlet
     * @return
     */
    @Override
    public int findMaxSort(SysPortlet sysPortlet)
    {
        return NumberUtils.nextOrder(mapper.findMaxSort(sysPortlet));
    }
    @Override
    public PageInfo<SysPortlet> findPageByRoleId(SysPortlet sysPortlet){
        PageDomain page = sysPortlet.getPage();
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.getOrderByColumn());
        return new PageInfo(mapper.findListByRoleId(sysPortlet));
    }
    @Override
    public List<SysPortlet> getPortletListByAuth(String portalConfigId){
        //portalConfigId不为null时表示系统设计门户小页，需要根据对应的sysPortalConfig数据判断可加载的小页集合，portalConfigId为null表示当前登录人设计个人首页，需要根据当前登录人角色加载小页集合
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<SysPortlet> sysPortletList = new ArrayList<SysPortlet>();
        SysPortlet querySysPortlet = new SysPortlet();
            if(StringUtils.isBlank(portalConfigId) || (StringUtils.isNotBlank(portalConfigId) &&"null".equals(portalConfigId))){
                if (loginUser.getUser().isAdmin())
                {
                    sysPortletList = super.findList(querySysPortlet);
                }else{
                    //用户设计小页，获取用户对应角色下可操作小页
                    querySysPortlet.setUserId(loginUser.getUser().getId());
                    sysPortletList =  mapper.getPortletListByUserId(querySysPortlet);
                }
            }else{
                if (loginUser.getUser().isAdmin())
                {
                    sysPortletList = super.findList(querySysPortlet);
                }else{
                    SysPortalConfig sysPortalConfig = sysPortalConfigMapper.get(portalConfigId);
                    if(sysPortalConfig != null){
                        if("S".equals(sysPortalConfig.getApplicationRange())){
                            //系统级
                            sysPortletList = super.findList(new SysPortlet());
                        }else if("R".equals(sysPortalConfig.getApplicationRange())){
                            //角色级
                            querySysPortlet.setRoleId(sysPortalConfig.getResourceId());
                            sysPortletList =  mapper.getPortletListByRoleId(querySysPortlet);
                        }else if("U".equals(sysPortalConfig.getApplicationRange())){
                            //用户级
                            querySysPortlet.setUserId(sysPortalConfig.getResourceId());
                            sysPortletList =  mapper.getPortletListByUserId(querySysPortlet);
                        }
                    }
                }
            }
        return sysPortletList;
    }
}
