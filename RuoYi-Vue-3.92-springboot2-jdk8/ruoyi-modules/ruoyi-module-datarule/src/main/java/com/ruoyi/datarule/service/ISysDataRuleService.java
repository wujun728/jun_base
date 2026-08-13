package com.ruoyi.datarule.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.SysDataRule;
import com.ruoyi.datarule.domain.SysRoleDataRule;

import java.util.Collection;
import java.util.List;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package sys
 * @title: 数据权限表控制器
 * @description: 数据权限表控制器
 * @author: 未知
 * @date: 2019-11-29 06:05:01
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */
public interface ISysDataRuleService extends IService<SysDataRule> {

    List<SysRoleDataRule> roleDataList(QueryWrapper<SysRoleDataRule> roleDataRuleEntityWrapper);

    void removeRoleDataRule(QueryWrapper<SysRoleDataRule> roleDataRuleEntityWrapper);

    void insertBatchRoleDataRule(List<SysRoleDataRule> roleDataRuleList);

}
