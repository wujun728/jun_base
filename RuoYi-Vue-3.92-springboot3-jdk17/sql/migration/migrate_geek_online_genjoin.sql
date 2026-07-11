-- =============================================
-- 迁移脚本: Geek项目精选功能 → 研发基座
-- 模块: 1.MyBatis在线接口 + 2.代码生成器关联表 + 3.文件管理
-- 日期: 2026-07-08
-- 说明: 基于Geek Entity实体类反推的表结构
-- =============================================

-- =============================================
-- 1. MyBatis在线接口表
-- =============================================
DROP TABLE IF EXISTS `online_mb`;

CREATE TABLE `online_mb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag` varchar(50) NOT NULL COMMENT 'MyBatis标签名(select/insert/update/delete)',
  `tag_id` varchar(100) NOT NULL COMMENT '标签ID(mapper.xml的id属性)',
  `parameter_type` varchar(255) DEFAULT NULL COMMENT '参数类型(Java全限定类名)',
  `result_map` varchar(100) DEFAULT NULL COMMENT '结果映射类型',
  `result_type` varchar(100) DEFAULT NULL COMMENT '响应类型',
  `sql_text` text COMMENT 'SQL语句体',
  `path` varchar(255) NOT NULL COMMENT '请求路径(以/开头)',
  `method` varchar(10) NOT NULL COMMENT 'HTTP请求方式(GET/POST/PUT/DELETE)',
  `actuator` varchar(50) DEFAULT NULL COMMENT '执行器类型',
  `user_id_flag` char(1) DEFAULT '0' COMMENT '是否自动注入userId(0否 1是)',
  `dept_id_flag` char(1) DEFAULT '0' COMMENT '是否自动注入deptId(0否 1是)',
  `permission_type` varchar(20) DEFAULT NULL COMMENT '鉴权类型(role/permission)',
  `permission_value` varchar(100) DEFAULT NULL COMMENT '鉴权值',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_path_method` (`path`, `method`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='MyBatis在线接口表';

-- =============================================
-- 2. 代码生成器关联表
-- =============================================
DROP TABLE IF EXISTS `gen_join`;

CREATE TABLE `gen_join` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_id` bigint(20) NOT NULL COMMENT '主表编号(关联gen_table.table_id)',
  `left_table_id` bigint(20) NOT NULL COMMENT '左表编号',
  `right_table_id` bigint(20) NOT NULL COMMENT '右表编号',
  `new_table_id` bigint(20) DEFAULT NULL COMMENT '新引入的表编号',
  `left_table_alias` varchar(50) DEFAULT NULL COMMENT '左表别名',
  `right_table_alias` varchar(50) DEFAULT NULL COMMENT '右表别名',
  `left_table_fk` bigint(20) DEFAULT NULL COMMENT '左表外键(关联gen_table_column.column_id)',
  `right_table_fk` bigint(20) DEFAULT NULL COMMENT '右表外键(关联gen_table_column.column_id)',
  `join_type` varchar(10) DEFAULT 'left' COMMENT '关联类型(left/right/inner)',
  `join_columns` varchar(2000) DEFAULT NULL COMMENT '关联字段列表(逗号分隔的column_id)',
  `order_num` int(11) DEFAULT '1' COMMENT '关联顺序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_table_id` (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='代码生成器关联表配置';

-- =============================================
-- 3. 文件管理表 (Geek: sys_file_info)
--    来源: com.geek.system.domain.SysFileInfo (MyBatis-Flex @Table)
-- =============================================
DROP TABLE IF EXISTS `sys_file_info`;

CREATE TABLE `sys_file_info` (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件主键',
  `file_name` varchar(255) DEFAULT NULL COMMENT '原始文件名',
  `file_path` varchar(500) DEFAULT NULL COMMENT '统一逻辑路径(以/开头)',
  `storage_type` varchar(50) DEFAULT NULL COMMENT '存储类型(local/minio/oss)',
  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型/后缀',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小(字节)',
  `md5` varchar(64) DEFAULT NULL COMMENT '文件MD5',
  `del_flag` int(1) DEFAULT '0' COMMENT '删除标志(0存在 2删除)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表';

-- =============================================
-- 4. 在线接口字典数据
-- =============================================

-- 字典类型
INSERT IGNORE INTO `sys_dict_type`(`dict_id`,`dict_name`,`dict_type`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES
(110,'MyBatis标签名','online_api_tag','0','admin',NOW(),'',NULL,'select/insert/update/delete'),
(111,'HTTP请求方式','online_api_method','0','admin',NOW(),'',NULL,'GET/POST/PUT/DELETE'),
(112,'响应类型','online_api_result','0','admin',NOW(),'',NULL,'响应结果类型'),
(113,'执行器类型','online_api_actuator','0','admin',NOW(),'',NULL,'执行器类型');

-- 字典数据: MyBatis标签名
INSERT IGNORE INTO `sys_dict_data`(`dict_sort`,`dict_label`,`dict_value`,`dict_type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES
(1,'select','select','online_api_tag','','primary','Y','0','admin',NOW(),'',NULL,'查询标签'),
(2,'insert','insert','online_api_tag','','success','N','0','admin',NOW(),'',NULL,'插入标签'),
(3,'update','update','online_api_tag','','warning','N','0','admin',NOW(),'',NULL,'更新标签'),
(4,'delete','delete','online_api_tag','','danger','N','0','admin',NOW(),'',NULL,'删除标签');

-- 字典数据: HTTP请求方式
INSERT IGNORE INTO `sys_dict_data`(`dict_sort`,`dict_label`,`dict_value`,`dict_type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES
(1,'GET','GET','online_api_method','','primary','Y','0','admin',NOW(),'',NULL,'GET请求'),
(2,'POST','POST','online_api_method','','success','N','0','admin',NOW(),'',NULL,'POST请求'),
(3,'PUT','PUT','online_api_method','','warning','N','0','admin',NOW(),'',NULL,'PUT请求'),
(4,'DELETE','DELETE','online_api_method','','danger','N','0','admin',NOW(),'',NULL,'DELETE请求');

-- =============================================
-- 5. 菜单配置参考 (可在后台菜单管理中配置)
-- =============================================
-- 在线接口菜单
--   菜单名称: 在线接口
--   父菜单: 系统工具(或自定义)
--   路由地址: onlineMb
--   组件路径: online/mb/index
--   权限标识: online:mb:list
--   菜单图标: code
--   按钮权限: online:mb:add, online:mb:edit, online:mb:remove, online:mb:export

-- =============================================
-- 迁移完成!
-- 表: online_mb(在线接口), gen_join(生成器关联表), sys_file_info(文件管理)
-- 字典: online_api_tag, online_api_method, online_api_result, online_api_actuator
-- =============================================
