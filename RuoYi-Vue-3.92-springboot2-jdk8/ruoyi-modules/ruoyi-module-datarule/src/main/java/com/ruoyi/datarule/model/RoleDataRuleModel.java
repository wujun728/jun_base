package com.ruoyi.datarule.model;

import lombok.Data;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package sys
 * @title: 角色数据权限关联表控制器
 * @description: 角色数据权限关联表控制器
 * @author: 未知
 * @date: 2019-11-29 03:21:37
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@Data
@SuppressWarnings("serial")
public class RoleDataRuleModel implements Serializable {

    private String id; //id
    private Integer dataRuleCategory;  //权限类型(1:数据权限、2:接口权限)
    private String dataRuleId;  //权限id
    private String roleId;  //角色id

}
