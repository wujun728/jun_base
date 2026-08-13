package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

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

@Data
@TableName("sys_data_rule")
@SuppressWarnings("serial")
public class SysDataRule extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private String id; //id
    @TableField(value = "resource_code")
    private String resourceCode;  //资源编号
    @TableField(value = "scope_name")
    private String scopeName;  //数据权限名称
    @TableField(value = "scope_field")
    private String scopeField;  //数据权限字段
    @TableField(value = "scope_class")
    private String scopeClass;  //数据权限类名
    @TableField(value = "scope_column")
    private String scopeColumn;  //数据权限字段
    @TableField(value = "table_name")
    private String tableName;  //数据权限关联表名
    @TableField(value = "scope_type")
    private String scopeType;  //数据权限类型
    @TableField(value = "scope_value")
    private String scopeValue;  //数据权限值域
    @TableField(value = "user_column")
    private String userColumn;  //用户表对应字段
    @TableField(value = "user_entity_field")
    private String userEntityField;  //用户实体类对应字段

    @TableField(exist = false)
    private boolean select;  //是否选择
}
