-- =============================================
-- 迁移脚本: 项目1(RuoYi-Vue-S) → 研发基座
-- 模块: 租户管理 + 数据权限
-- 日期: 2026-07-08
-- =============================================

-- =============================================
-- 1. 租户管理表 sys_tenant
-- =============================================
DROP TABLE IF EXISTS `sys_tenant`;

CREATE TABLE `sys_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `del_flag` char(1) DEFAULT NULL COMMENT '删除标识',
  `remark` text COMMENT '备注',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户标识',
  `contact` varchar(64) NOT NULL COMMENT '联系人',
  `phone` varchar(64) NOT NULL COMMENT '电话',
  `name` varchar(64) NOT NULL COMMENT '租户名称',
  `user_id` varchar(64) DEFAULT NULL COMMENT '管理员用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='租户管理';

-- =============================================
-- 2. 数据权限表 sys_data_rule
-- =============================================
DROP TABLE IF EXISTS `sys_data_rule`;

CREATE TABLE `sys_data_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_code` varchar(255) NOT NULL COMMENT '资源编号',
  `scope_name` varchar(255) NOT NULL COMMENT '数据权限名称',
  `scope_field` varchar(255) NOT NULL COMMENT '数据权限字段',
  `scope_class` varchar(500) NOT NULL COMMENT '数据权限类名(Mapper方法全路径)',
  `scope_column` varchar(255) NOT NULL COMMENT '数据权限列名',
  `scope_type` int(2) NOT NULL COMMENT '数据权限类型(1全部 2自定义 3本部门 4本部门及以下 5仅本人)',
  `scope_value` varchar(2000) DEFAULT NULL COMMENT '数据权限值域',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(2) DEFAULT NULL COMMENT '是否已删除',
  `table_name` varchar(64) DEFAULT NULL COMMENT '数据库表名',
  `user_column` varchar(64) DEFAULT NULL COMMENT '用户表对应字段',
  `tenant_id` varchar(64) NOT NULL DEFAULT '00000000' COMMENT '租户ID',
  `user_entity_field` varchar(64) DEFAULT NULL COMMENT 'user实体类对应字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='数据权限表';

-- =============================================
-- 3. 角色数据权限关联表 sys_role_data_rule
-- =============================================
DROP TABLE IF EXISTS `sys_role_data_rule`;

CREATE TABLE `sys_role_data_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_rule_category` int(2) DEFAULT NULL COMMENT '权限类型(1:数据权限 2:接口权限)',
  `data_rule_id` varchar(64) DEFAULT NULL COMMENT '权限ID',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='角色数据权限关联表';

-- =============================================
-- 4. 字典类型: 数据权限类型
-- =============================================
INSERT IGNORE INTO `sys_dict_type`(`dict_id`,`dict_name`,`dict_type`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`)
VALUES (100,'数据权限类型','data_rule_type','0','admin',NOW(),'',NULL,'数据权限类型:1全部 2自定义 3本部门 4本部门及以下 5仅本人');

-- =============================================
-- 5. 字典数据: 数据权限类型值
-- =============================================
INSERT IGNORE INTO `sys_dict_data`(`dict_sort`,`dict_label`,`dict_value`,`dict_type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES
(1,'全部数据权限','1','data_rule_type','','primary','Y','0','admin',NOW(),'',NULL,'全部数据权限'),
(2,'自定义数据权限','2','data_rule_type','','success','N','0','admin',NOW(),'',NULL,'自定义数据权限'),
(3,'本部门数据权限','3','data_rule_type','','info','N','0','admin',NOW(),'',NULL,'本部门数据权限'),
(4,'本部门及以下','4','data_rule_type','','warning','N','0','admin',NOW(),'',NULL,'本部门及以下数据权限'),
(5,'仅本人数据','5','data_rule_type','','danger','N','0','admin',NOW(),'',NULL,'仅本人数据');

-- =============================================
-- 6. 菜单配置参考 (通过后台菜单管理界面配置)
-- =============================================
-- 请在基座后台 "系统管理 → 菜单管理" 中添加以下菜单:

-- 父菜单: 系统管理 (parent_id=1, 请根据实际调整)
-- 
-- 【租户管理】
--   菜单名称: 租户管理
--   路由地址: tenant
--   组件路径: system/tenant/index
--   权限标识: system:tenant:list
--   菜单图标: peoples
--   按钮权限: system:tenant:query, system:tenant:add, system:tenant:edit, system:tenant:remove, system:tenant:export
--
-- 【数据权限】
--   菜单名称: 数据权限
--   路由地址: dataRule
--   组件路径: system/dataRule/index
--   权限标识: system:rule:list
--   菜单图标: tree
--   按钮权限: system:rule:query, system:rule:add, system:rule:edit, system:rule:remove, system:rule:export

-- =============================================
-- 迁移完成!
-- 表: sys_tenant, sys_data_rule, sys_role_data_rule
-- 字典: data_rule_type
-- =============================================
