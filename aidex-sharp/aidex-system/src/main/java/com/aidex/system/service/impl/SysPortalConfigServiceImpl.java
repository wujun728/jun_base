package com.aidex.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.exception.BizException;
import com.aidex.common.utils.NumberUtils;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.bean.BeanUtils;
import com.aidex.common.utils.uuid.IdUtils;
import com.aidex.system.domain.SysPortalConfig;
import com.aidex.system.mapper.SysPortalConfigMapper;
import com.aidex.system.service.SysPortalConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多栏目门户配置Service业务层处理
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-05-11
 */
@Service
@Transactional(readOnly = true)
public class SysPortalConfigServiceImpl extends BaseServiceImpl<SysPortalConfigMapper, SysPortalConfig> implements SysPortalConfigService
{
    private static final Logger log = LoggerFactory.getLogger(SysPortalConfigServiceImpl.class);

        /**
     * 获取单条数据
     * @param sysPortalConfig
     * @return
     */
    @Override
    public SysPortalConfig get(SysPortalConfig sysPortalConfig)
    {
        SysPortalConfig dto = super.get(sysPortalConfig);
                                                                                                                                                                                                                                                                                                                                                                                                            return dto;
    }

    /**
     * 获取单条数据
     * @param id
     * @return
     */
    @Override
    public SysPortalConfig get(String id)
    {
        SysPortalConfig dto = super.get(id);
                                                                                                                                                                                                                                                                                                                                                                                                            return dto;
    }


    /**
     * 查询多栏目门户配置列表
     * @param sysPortalConfig
     * @return
     */
    @Override
    public List<SysPortalConfig> findList(SysPortalConfig sysPortalConfig)
    {
        return super.findList(sysPortalConfig);
    }

    /**
     * 分页查询多栏目门户配置列表
     * @param sysPortalConfig
     * @return
     */
    @Override
    public PageInfo<SysPortalConfig> findPage(SysPortalConfig sysPortalConfig)
    {
        return super.findPage(sysPortalConfig);
    }

    /**
     * 保存多栏目门户配置
     * @param sysPortalConfig
     * @return
     */
    @Override
    public boolean save(SysPortalConfig sysPortalConfig)
    {
        checkCodeUnique(sysPortalConfig);
        return super.save(sysPortalConfig);
    }

    /**
     * 校验小页编码的唯一性
     * @param sysPortalConfig
     * @return
     */
    @Override
    public void checkCodeUnique(SysPortalConfig sysPortalConfig){
        SysPortalConfig sysPortalConfigUnique = new SysPortalConfig();
        sysPortalConfigUnique.setNotEqualId(sysPortalConfig.getId());
        sysPortalConfigUnique.setCode(sysPortalConfig.getCode());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysPortalConfigUnique))) {
            throw new BizException("小页编码已存在，请重新输入！");
        }
    }

    /**
     * 删除多栏目门户配置信息
     * @param sysPortalConfig
     * @return
     */
    @Override
    public boolean remove(SysPortalConfig sysPortalConfig)
    {
        return super.remove(sysPortalConfig);
    }

    /**
     * 批量删除多栏目门户配置
     * @param ids 需要删除的多栏目门户配置ID
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public int deleteSysPortalConfigByIds(String[] ids)
    {
        return mapper.deleteSysPortalConfigByIds(ids);
    }

    /**
     * 获取最大编号
     * @param sysPortalConfig
     * @return
     */
    @Override
    public int findMaxSort(SysPortalConfig sysPortalConfig)
    {
        return NumberUtils.nextOrder(mapper.findMaxSort(sysPortalConfig));
    }

    /**
     * 获取当前登陆人可以看到的所有首页
     * @param user
     * @return
     */
    @Override
    public Map<String,Object> findUserConfigList(SysUser user){
        Map<String,Object> resultMap = new HashMap<String,Object>(2);
        SysPortalConfig sysPortalConfig = new SysPortalConfig();
        sysPortalConfig.setResourceId(user.getId());
        List<SysPortalConfig> sysPortalConfigList = mapper.findUserConfigList(sysPortalConfig);
        List<SysPortalConfig> defaultUserConfig = new ArrayList<SysPortalConfig>();
        List<SysPortalConfig> defaultRoleConfig = new ArrayList<SysPortalConfig>();
        List<SysPortalConfig> defaultSystemConfig = new ArrayList<SysPortalConfig>();
        SysPortalConfig defaultSysPortalConfig = null;
        if(!CollectionUtils.isEmpty(sysPortalConfigList)){
            for(SysPortalConfig obj : sysPortalConfigList){
                if("U".equals(obj.getApplicationRange()) && "Y".equals(obj.getIsDefault())){
                    defaultUserConfig.add(obj);
                }
                if("R".equals(obj.getApplicationRange()) && "Y".equals(obj.getIsDefault())){
                    defaultRoleConfig.add(obj);
                }
                if("S".equals(obj.getApplicationRange()) && "Y".equals(obj.getIsDefault())){
                    defaultSystemConfig.add(obj);
                }
            }
            defaultUserConfig.addAll(defaultRoleConfig);
            defaultUserConfig.addAll(defaultSystemConfig);
            if(!CollectionUtils.isEmpty(defaultUserConfig)){
                defaultSysPortalConfig = defaultUserConfig.get(0);
            }else{
                defaultSysPortalConfig = sysPortalConfigList.get(0);
            }
        }
        resultMap.put("default",defaultSysPortalConfig);
        resultMap.put("portalList",sysPortalConfigList);
        return resultMap;
    }
    @Transactional(readOnly = false)
    @Override
    public SysPortalConfig insertOrUpdateSysPortalConfig(SysPortalConfig sysPortalConfig){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isEmpty(sysPortalConfig.getId())) {
            if(StringUtils.isEmpty(sysPortalConfig.getCode())){
                sysPortalConfig.setCode(IdUtils.randomUUID());
            }
            if(StringUtils.isNotBlank(sysPortalConfig.getSaveType()) && "user".equals(sysPortalConfig.getSaveType())){
               //用户添加时需要设定默认数据
                sysPortalConfig.setApplicationRange("U");
                sysPortalConfig.setResourceId(loginUser.getUser().getId());
                sysPortalConfig.setIsDefault("Y");
            }
            //新增
            super.save(sysPortalConfig);
        }else{
            //编辑
            if(StringUtils.isNotBlank(sysPortalConfig.getSaveType()) && "user".equals(sysPortalConfig.getSaveType()) && !"U".equals(sysPortalConfig.getApplicationRange())){
                //当用户编辑一个应用范围不是用户的门户小页时需要将该小页内容copy一份作为用户自定义页面，原有的系统或角色自定义页面不变
                sysPortalConfig.setId("");
                sysPortalConfig.setApplicationRange("U");
                sysPortalConfig.setResourceId(loginUser.getUser().getId());
            }
            super.save(sysPortalConfig);
        }
      return sysPortalConfig;
    }

    /**
     * 设置当前小页为用户默认小页
     * @param sysPortalConfig
     */
    @Transactional(readOnly = false)
    @Override
    public void updateDefaultPortalConfig(SysPortalConfig sysPortalConfig){
        mapper.updateNotDefaultByUser(sysPortalConfig);
    }

    /**
     * 获取模板列表
     * @param user
     * @return
     */
    @Override
    public List<SysPortalConfig> getPortalTemplateList(SysUser user){
        SysPortalConfig sysPortalConfig = new SysPortalConfig();
        sysPortalConfig.setResourceId(user.getId());
        List<SysPortalConfig> sysPortalConfigList = mapper.getPortalTemplateList(sysPortalConfig);
        return sysPortalConfigList;
    }
}
