package com.ruoyi.datarule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @date: 2019-12-03 13:03:38
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("sys_role_data_rule")
@SuppressWarnings("serial")
public class SysRoleDataRule implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private String id; //id
    @TableField(value = "data_rule_category")
    private Integer scopeCategory;  //权限类型(1:数据权限、2:接口权限)
    @TableField(value = "data_rule_id")
    private String scopeId;  //权限id
    @TableField(value = "role_id")
    private String roleId;  //角色id
    @TableField(exist = false)
    private String ids;

}
