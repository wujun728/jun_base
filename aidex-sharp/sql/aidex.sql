/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50711
Source Host           : localhost:3306
Source Database       : aidex_zs

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2021-10-08 22:02:34
*/

SET FOREIGN_KEY_CHECKS=0;  

-- ----------------------------
-- Table structure for `base_table`
-- ----------------------------
DROP TABLE IF EXISTS `base_table`;
CREATE TABLE `base_table` (
  `id` varchar(64) NOT NULL,
  `sort` int(4) NOT NULL COMMENT '排序',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1 停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `extend_s1` varchar(200) DEFAULT NULL COMMENT '备用字符串1',
  `extend_s2` varchar(200) DEFAULT NULL COMMENT '备用字符串2',
  `extend_s3` varchar(200) DEFAULT NULL COMMENT '备用字符串3',
  `extend_s4` varchar(200) DEFAULT NULL COMMENT '备用字符串4',
  `extend_i1` int(11) DEFAULT NULL COMMENT '备用整型1',
  `extend_i2` int(11) DEFAULT NULL COMMENT '备用整型2',
  `extend_i3` int(11) DEFAULT NULL COMMENT '备用整型3',
  `extend_i4` int(11) DEFAULT NULL COMMENT '备用整型4',
  `extend_d1` datetime DEFAULT NULL COMMENT '备用日期1',
  `extend_d2` datetime DEFAULT NULL COMMENT '备用日期2',
  `extend_d3` datetime DEFAULT NULL COMMENT '备用日期3',
  `extend_d4` datetime DEFAULT NULL COMMENT '备用日期3',
  `extend_f1` double(11,2) DEFAULT NULL COMMENT '备用浮点1',
  `extend_f2` double(11,2) DEFAULT NULL COMMENT '备用浮点2',
  `extend_f3` double(11,2) DEFAULT NULL COMMENT '备用浮点3',
  `extend_f4` double(11,2) DEFAULT NULL COMMENT '备用浮点4',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='基础表';

-- ----------------------------
-- Records of base_table
-- ----------------------------

-- ----------------------------
-- Table structure for `base_tree_table`
-- ----------------------------
DROP TABLE IF EXISTS `base_tree_table`;
CREATE TABLE `base_tree_table` (
  `id` varchar(64) NOT NULL,
  `parent_id` varchar(32) DEFAULT '0' COMMENT '父id',
  `parent_ids` varchar(500) DEFAULT NULL COMMENT '父id集合',
  `tree_sort` int(11) DEFAULT '0' COMMENT '排序',
  `tree_sorts` varchar(500) DEFAULT NULL COMMENT '排序集合',
  `tree_level` int(11) DEFAULT NULL COMMENT '层级',
  `tree_leaf` char(1) DEFAULT NULL COMMENT '是否子节点（0是 1否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='树基础表';

-- ----------------------------
-- Records of base_tree_table
-- ----------------------------

-- ----------------------------
-- Table structure for `gen_config_template`
-- ----------------------------
DROP TABLE IF EXISTS `gen_config_template`;
CREATE TABLE `gen_config_template` (
  `id` varchar(64) NOT NULL,
  `template_name` varchar(50) NOT NULL COMMENT '模板名称',
  `function_author` varchar(100) NOT NULL COMMENT '作者',
  `function_author_email` varchar(100) NOT NULL COMMENT '邮箱',
  `workspace_path` varchar(200) DEFAULT NULL COMMENT '工作空间路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '模块名',
  `package_name` varchar(100) DEFAULT NULL COMMENT '模块包路径',
  `web_workspace_path` varchar(200) DEFAULT NULL COMMENT '前端工作空间路径',
  `template_default` varchar(10) NOT NULL COMMENT '是否默认',
  `sort` int(4) NOT NULL COMMENT '排序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1 停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板配置表';

-- ----------------------------
-- Records of gen_config_template
-- ----------------------------

-- ----------------------------
-- Table structure for `gen_table`
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `workspace_path` varchar(200) DEFAULT NULL COMMENT '工作空间',
  `module_name` varchar(30) DEFAULT NULL COMMENT '模块名',
  `package_name` varchar(100) DEFAULT NULL COMMENT '包路径',
  `business_name` varchar(30) DEFAULT NULL COMMENT '业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '作者',
  `function_author_email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `web_workspace_path` varchar(200) DEFAULT NULL COMMENT '前端工作空间路径',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `options` varchar(4000) DEFAULT NULL COMMENT '扩展选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='代码生成业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for `gen_table_column`
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(10) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(10) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(10) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(10) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(10) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(10) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(10) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `is_unique` char(10) DEFAULT NULL COMMENT '是否唯一性',
  `is_log` char(10) DEFAULT NULL COMMENT '是否记录日志',
  `is_column_sort` varchar(10) DEFAULT NULL COMMENT '是否排序',
  `is_new_row` char(10) DEFAULT NULL COMMENT '新行',
  `col_span` int(10) DEFAULT 2 COMMENT '列数',
  `align_type` varchar(10) DEFAULT NULL COMMENT '对齐方式',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `col_check` varchar(100) DEFAULT NULL COMMENT '字段校验',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8 COMMENT='代码生成业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_attach`
-- ----------------------------
DROP TABLE IF EXISTS `sys_attach`;
CREATE TABLE `sys_attach` (
  `id` varchar(64) NOT NULL,
  `attach_name` varchar(200) DEFAULT NULL COMMENT '附件名称',
  `file_business_id` varchar(50) DEFAULT NULL COMMENT '数据对应ID',
  `file_business_table_name` varchar(50) DEFAULT NULL COMMENT '关联业务表',
  `file_body` longblob COMMENT '附件内容',
  `attach_type` varchar(10) DEFAULT NULL COMMENT '附件类型',
  `attach_size` decimal(20,0) DEFAULT NULL COMMENT '附件大小',
  `file_url` text COMMENT '上传附件路径',
  `file_business_table_field` varchar(100) DEFAULT NULL COMMENT '附件所关联字段名称',
  `attach_category` varchar(32) DEFAULT NULL COMMENT '附件类别',
  `save_type` varchar(50) DEFAULT NULL COMMENT '保存方式',
  `element_id` varchar(100) DEFAULT NULL COMMENT '页面区块ID',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1 停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `extend_s1` varchar(200) DEFAULT NULL COMMENT '备用字符串1',
  `extend_s2` varchar(200) DEFAULT NULL COMMENT '备用字符串2',
  `extend_s3` varchar(200) DEFAULT NULL COMMENT '备用字符串3',
  `extend_s4` varchar(200) DEFAULT NULL COMMENT '备用字符串4',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx1` (`file_business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='附件信息表';

-- ----------------------------
-- Records of sys_attach
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_common_use_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_use_menu`;
CREATE TABLE `sys_common_use_menu` (
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `path` varchar(100) DEFAULT NULL COMMENT '路劲',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `menu_obj` varchar(200) DEFAULT NULL COMMENT '对应菜单JSON',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `sort` int(4) NOT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx1` (`user_id`,`del_flag`,`path`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='常用菜单配置表';

-- ----------------------------
-- Records of sys_common_use_menu
-- ----------------------------
INSERT INTO `sys_common_use_menu` VALUES ('18dcfb72c207429d886abbda75f25b88', '1', '/monitor/druid', 'dashboard', '数据监控', '{\"path\":\"/monitor/druid\",\"iconStr\":\"dashboard\",\"title\":\"数据监控\",\"remark\":\"数据监控菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '210', '1', '100', '2021-07-06 16:43:41', '1', '2021-07-06 16:43:41', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('391b5ea23f144970b5d786eac7f46aca', '1', '/sysSetting/config', 'code', '参数设置', '{\"path\":\"/sysSetting/config\",\"iconStr\":\"code\",\"title\":\"参数设置\",\"remark\":\"参数设置菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '280', '1', '100', '2021-07-06 16:54:20', '1', '2021-07-06 16:54:20', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('4f8ef1709b2a4f52ade1d798a5e934f6', '1', '/monitor/online', 'dot-chart', '在线用户', '{\"path\":\"/monitor/online\",\"iconStr\":\"dot-chart\",\"title\":\"在线用户\",\"remark\":\"在线用户菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '190', '1', '100', '2021-07-06 16:43:40', '1', '2021-07-06 16:43:40', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('704c8f7a018149498e5a742182708aec', '1', '/auth/role', 'contacts', '角色管理', '{\"path\":\"/auth/role\",\"iconStr\":\"contacts\",\"title\":\"角色管理\",\"remark\":\"维护平台各角色数据以及权限分配\",\"id\":\"\",\"isCommonUse\":false}', '0', '250', '1', '100', '2021-07-06 16:52:22', '1', '2021-07-06 16:52:22', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('76e270b147504620979af3483c1fd7bd', '1', '/auth/sysAuth', 'api', '集中授权', '{\"path\":\"/auth/sysAuth\",\"iconStr\":\"api\",\"title\":\"集中授权\",\"remark\":null,\"id\":\"\",\"isCommonUse\":false}', '0', '260', '1', '100', '2021-07-06 16:54:18', '1', '2021-07-06 16:54:18', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('998f5c24b4e643cca698cd0726163fe8', '1', '/org/post', 'idcard', '岗位管理', '{\"path\":\"/org/post\",\"iconStr\":\"idcard\",\"title\":\"岗位管理\",\"remark\":\"岗位管理菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '240', '1', '100', '2021-07-06 16:52:22', '1', '2021-07-06 16:52:22', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('b367cde7ce2d4954822b314990906360', '1', '/sysApp/iconSelect', 'shopping', '图标选择', '{\"path\":\"/sysApp/iconSelect\",\"iconStr\":\"shopping\",\"title\":\"图标选择\",\"remark\":\"\",\"id\":\"\",\"isCommonUse\":false}', '0', '300', '1', '100', '2021-07-06 16:54:22', '1', '2021-07-06 16:54:22', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('b463caf0037d424893d80d2616ed071d', '1', '/monitor/job', 'bar-chart', '定时任务', '{\"path\":\"/monitor/job\",\"iconStr\":\"bar-chart\",\"title\":\"定时任务\",\"remark\":\"定时任务菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '200', '1', '100', '2021-07-06 16:43:40', '1', '2021-07-06 16:43:40', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('d609d6c03a5544bc85aa84c1a524b7c5', '1', '/sysApp/notice', 'notification', '通知公告', '{\"path\":\"/sysApp/notice\",\"iconStr\":\"notification\",\"title\":\"通知公告\",\"remark\":\"通知公告菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '290', '1', '100', '2021-07-06 16:54:21', '1', '2021-07-06 16:54:21', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('da60c299e4da42f7b229329016d7eda2', '1', '/monitor/cache', 'box-plot', '缓存监控', '{\"path\":\"/monitor/cache\",\"iconStr\":\"box-plot\",\"title\":\"缓存监控\",\"remark\":\"缓存监控菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '230', '1', '100', '2021-07-06 16:43:44', '1', '2021-07-06 16:43:44', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('f3d9d32d1ffa44dab32d97010eb58f6b', '1', '/sysSetting/menu', 'bars', '菜单管理', '{\"path\":\"/sysSetting/menu\",\"iconStr\":\"bars\",\"title\":\"菜单管理\",\"remark\":\"平台所有菜单维护\",\"id\":\"\",\"isCommonUse\":false}', '0', '180', '1', '100', '2021-07-06 16:31:00', '1', '2021-07-06 16:31:00', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('f9f5ec6f9d4c4e5dab43ac26934ac76d', '1', '/sysSetting/dict', 'read', '字典管理', '{\"path\":\"/sysSetting/dict\",\"iconStr\":\"read\",\"title\":\"字典管理\",\"remark\":\"字典管理菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '270', '1', '100', '2021-07-06 16:54:19', '1', '2021-07-06 16:54:19', '127.0.0.1', null, '1', '0');
INSERT INTO `sys_common_use_menu` VALUES ('fe93fdb435c44211b72baabd44f4b4d6', '1', '/monitor/server', 'pie-chart', '服务监控', '{\"path\":\"/monitor/server\",\"iconStr\":\"pie-chart\",\"title\":\"服务监控\",\"remark\":\"服务监控菜单\",\"id\":\"\",\"isCommonUse\":false}', '0', '220', '1', '100', '2021-07-06 16:43:42', '1', '2021-07-06 16:43:42', '127.0.0.1', null, '1', '0');

-- ----------------------------
-- Table structure for `sys_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES ('主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2021-01-30 13:27:43', '1', '2021-07-06 15:13:33', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', '1', '4', '0', '127.0.0.1', '1');
INSERT INTO `sys_config` VALUES ('用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2021-01-30 13:27:43', '', null, '初始化密码 123456', '2', '1', '0', null, '1');
INSERT INTO `sys_config` VALUES ('系统下载方式', 'sys.upload.save.type', 'Disk', 'Y', 'admin', '2021-03-12 17:43:10', '1', '2021-09-05 12:10:32', 'Disk', '2345678935345', '6', '0', '127.0.0.1', '1');
INSERT INTO `sys_config` VALUES ('主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2021-01-30 13:27:43', '1', '2021-08-06 11:39:33', '深色主题theme-dark，浅色主题theme-light   ', '3', '5', '0', '127.0.0.1', '1');
INSERT INTO `sys_config` VALUES ('用户是否可注册开关', 'sys.account.registerUser', 'true', 'Y', '1', '2021-09-29 23:51:21', '1', '2021-09-29 23:51:21', '是否开启验证码功能（true开启，false关闭）', 'b3158f15cbed4a3d93e43f19d5fb5893', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_config` VALUES ('验证码开关', 'sys.captcha.onOff', 'true', 'Y', '1', '2021-09-29 23:51:21', '1', '2021-09-29 23:51:21', '是否开启验证码功能（true开启，false关闭）', 'b3158f15cbed4a3d93e43f29d5fb5893', '1', '0', '127.0.0.1', '100');

-- ----------------------------
-- Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` varchar(64) NOT NULL,
  `dept_code` varchar(500) DEFAULT NULL COMMENT '部门编号',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `dept_full_name` varchar(200) DEFAULT NULL COMMENT '全称',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `dept_type` varchar(10) DEFAULT NULL COMMENT '机构类型',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邮政编码',
  `dept_pinyin` varchar(500) DEFAULT NULL COMMENT '部门拼音',
  `parent_id` varchar(32) DEFAULT '0' COMMENT '父id',
  `parent_ids` varchar(500) DEFAULT NULL COMMENT '父id集合',
  `tree_sort` int(11) DEFAULT '0' COMMENT '排序',
  `tree_sorts` varchar(500) DEFAULT NULL COMMENT '排序集合',
  `tree_level` int(11) DEFAULT NULL COMMENT '层级',
  `tree_leaf` char(1) DEFAULT NULL COMMENT '是否子节点（0是 1否）',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('100', '100', '集团', '集团', '管理员', '', '', 'org', null, null, 'jituan,jt', '0', '100', '10', '000010', '1', 'n', '0', 'admin', null, '2021-01-30 13:28:35', '1', '2021-09-05 10:07:57', '127.0.0.1', null, '7', '0');
INSERT INTO `sys_dept` VALUES ('102', '102', '子公司', '子公司', '管理员', '', '', 'company', null, null, 'zigongsi,zgs', '100', '100/102', '10', '000010/000010', '2', 'n', '0', 'admin', null, '2021-01-30 13:28:35', '1', '2021-09-05 10:07:48', '127.0.0.1', '111', '33', '0');
INSERT INTO `sys_dept` VALUES ('f96f514133a241fd8bffd9dd899de8ea', 'manager', '市场部', '市场部', null, null, null, 'dept', null, null, 'shichangbu,scb', '102', '100/102/f96f514133a241fd8bffd9dd899de8ea', '10', '000010/000010/000010', '3', 'y', '0', '1', '40f40750917349249f3a7d4593e3e886', '2021-07-26 18:20:38', '1', '2021-09-04 21:54:25', '127.0.0.1', null, '3', '0');

-- ----------------------------
-- Table structure for `sys_dict_data`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) DEFAULT NULL COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dict_data_idx` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES ('1', '10', '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2021-01-30 13:27:43', '1', '2021-03-24 11:38:41', '性别男', '1', '1', '0', '127.0.0.1', null);
INSERT INTO `sys_dict_data` VALUES ('10', '10', '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '默认分组', '10', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('11', '20', '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '系统分组', '11', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('12', '10', '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '系统默认是', '12', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('13', '20', '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '系统默认否', '13', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('14', '10', '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '通知', '14', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('15', '20', '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '公告', '15', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('16', '10', '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '正常状态', '16', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('17', '20', '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '关闭状态', '17', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('18', '10', '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '新增操作', '18', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('19', '20', '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '修改操作', '19', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('2', '20', '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '性别女', '2', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('20', '30', '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '删除操作', '20', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('27', '100', '查询', '10', 'sys_oper_type', null, null, null, '0', '1', '2021-10-04 22:37:38', '1', '2021-10-04 22:49:32', '查询', '205c0b4c-e4d2-44b1-b70b-0d231b07012d', '3', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('21', '40', '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '授权操作', '21', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('22', '50', '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '导出操作', '22', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('23', '60', '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '导入操作', '23', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('24', '70', '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '强退操作', '24', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('25', '80', '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '生成操作', '25', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('26', '90', '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '清空操作', '26', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('27', '10', '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '正常状态', '27', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('28', '20', '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '停用状态', '28', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('3', '30', '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '性别未知', '3', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '10', '系统', 'S', 'sys_portal_range', null, null, null, '1', '1', '2021-05-11 10:15:31', '1', '2021-06-21 15:49:10', null, '311f9aef2e354baf962a39b52602b7ad', '21', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '10', '有效', '0', 'sys_is_valid', null, null, null, '0', '1', '2021-05-27 10:10:48', '1', '2021-05-27 10:10:48', null, '36315b7a36a94a56ae1822b49da8da50', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '10', '后台用户', '1', 'sys_user_type', null, null, null, '0', '1', '2021-03-01 11:41:30', '1', '2021-03-01 11:41:35', null, '38ca19b3b24242b48c6d8302bb6f1a67', '2', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES (null, '10', '部分成功', 'sucess_fail', 'sys_upload_file_status', null, null, null, '0', '1', '2021-07-29 17:54:49', '1', '2021-07-29 17:54:49', null, '3e6d5a3380834e92af742b0ef3a3dbb1', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('4', '10', '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '显示菜单', '4', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('5', '20', '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '隐藏菜单', '5', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '20', '失败', 'fail', 'sys_upload_file_status', null, null, null, '0', '1', '2021-07-29 17:54:28', '1', '2021-07-29 17:54:28', null, '5f04bcbfde0049ddb2a17441976a64b2', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('6', '10', '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '正常状态', '6', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '200', '开启', '20', 'is_active', null, null, null, '0', '1', '2021-08-12 16:16:16', '1', '2021-08-12 16:16:16', null, '60e4ea6677b54a2ea98310538f597b12', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '30', '处理中', 'processing', 'sys_upload_file_status', null, null, null, '0', '1', '2021-07-29 21:41:04', '1', '2021-07-29 21:41:04', null, '68e4aeacb4534486b253c7e1d91b4f0c', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('7', '20', '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '停用状态', '7', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '10', '等待下载', 'waiting', 'sys_download_file_status', null, null, null, '0', '1', '2021-07-18 18:59:48', '1', '2021-07-18 18:59:48', null, '728d9405e43b4ac6953ef0f37fe59d5f', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('8', '10', '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '正常状态', '8', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '20', '角色', 'R', 'sys_portal_range', null, null, null, '0', '1', '2021-05-11 10:15:48', '1', '2021-05-11 10:15:48', null, '86adc81770e34600ab906d68b927c110', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '20', '导出失败', 'fail', 'sys_download_file_status', null, null, null, '0', '1', '2021-07-18 15:05:22', '1', '2021-07-18 15:05:22', null, '888a580d4c914e9a9a59021b149301c4', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('9', '20', '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '停用状态', '9', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '20', '前台用户', '2', 'sys_user_type', null, null, null, '0', '1', '2021-03-01 11:41:46', '1', '2021-03-01 11:41:46', null, '9f6424fbc8154850ba41076616855d63', '1', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES (null, '10', '机构', 'org', 'sys_dept_type', null, null, null, '0', '1', '2021-02-24 18:35:41', '1', '2021-02-25 15:22:42', null, 'a2c5e3f94ce44007adb448de721bbc49', '5', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES (null, '30', '文件已过期', 'expired', 'sys_download_file_status', null, null, null, '0', '1', '2021-07-18 15:05:46', '1', '2021-07-18 15:05:46', null, 'a8a02ea692f246c4be64b0f341291c5b', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('28', '110', '校验', '11', 'sys_oper_type', null, null, null, '0', '1', '2021-10-04 22:40:50', '1', '2021-10-04 22:50:02', '校验', 'ab9bbed6-9395-420f-9fa2-c9be42550017', '2', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '20', '公司', 'company', 'sys_dept_type', null, null, null, '0', '1', '2021-02-24 11:45:32', '1', '2021-02-24 14:06:42', null, 'c226f8276bbd41ce9bb53bff9f38e6db', '3', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES (null, '20', '无效', '1', 'sys_is_valid', null, null, null, '0', '1', '2021-05-27 10:10:58', '1', '2021-05-27 10:10:58', null, 'c4f7b956f6074cde80ea2d0520aaae15', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '30', '部门', 'dept', 'sys_dept_type', null, null, null, '0', '1', '2021-03-02 10:14:09', '1', '2021-03-02 10:14:09', null, 'cc8564ca5919410e8ee71145ef652ccd', '1', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES (null, '40', '导出成功', 'success', 'sys_download_file_status', null, null, null, '0', '1', '2021-07-18 15:05:11', '1', '2021-07-18 15:05:11', null, 'd057ba5be59947bda3275e604ad758d0', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '100', '关闭', '10', 'is_active', null, null, null, '0', '1', '2021-08-12 16:16:05', '1', '2021-08-12 16:16:05', null, 'd6a6dd3180e2497eb34b29c5b29ead15', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '30', '用户', 'U', 'sys_portal_range', null, null, null, '0', '1', '2021-05-11 10:16:00', '1', '2021-05-11 10:16:00', null, 'dae2f9bc8046495182fa320b9efd05b2', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '40', '成功', 'success', 'sys_upload_file_status', null, null, null, '0', '1', '2021-07-29 17:54:13', '1', '2021-07-29 17:54:13', null, 'f5b21638f0af411fa642f78811b9650d', '1', '0', '127.0.0.1', '100');

-- ----------------------------
-- Table structure for `sys_dict_type`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` varchar(64) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('1', '用户性别', 'sys_user_sex', '0', 'admin', '2021-01-30 13:27:43', '1', '2021-03-24 11:38:47', '用户性别列表', '1', '0', '127.0.0.1', null);
INSERT INTO `sys_dict_type` VALUES ('10', '系统状态', 'sys_common_status', '0', 'admin', '2021-01-30 13:27:43', '', null, '登录状态列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('17a5aedeee3f4ed0a3a7d0d84e5b37d2', '门户应用范围', 'sys_portal_range', '0', '1', '2021-05-11 10:15:05', '1', '2021-05-11 10:15:05', null, '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_type` VALUES ('2', '菜单状态', 'sys_show_hide', '0', 'admin', '2021-01-30 13:27:43', '', null, '菜单状态列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('3', '系统开关', 'sys_normal_disable', '0', 'admin', '2021-01-30 13:27:43', '', null, '系统开关列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('4', '任务状态', 'sys_job_status', '0', 'admin', '2021-01-30 13:27:43', '', null, '任务状态列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('5', '任务分组', 'sys_job_group', '0', 'admin', '2021-01-30 13:27:43', '', null, '任务分组列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('6', '系统是否', 'sys_yes_no', '0', 'admin', '2021-01-30 13:27:43', '', null, '系统是否列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('6a944ab30cdb4bcda6380db18515e111', '导出文件状态', 'sys_download_file_status', '0', '1', '2021-07-18 15:03:42', '1', '2021-07-18 15:03:42', null, '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_type` VALUES ('7', '通知类型', 'sys_notice_type', '0', 'admin', '2021-01-30 13:27:43', '', null, '通知类型列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('79aaacc0f8424b75b4b2dd1809b698c6', '是否有效', 'sys_is_valid', '0', '1', '2021-05-27 10:10:26', '1', '2021-05-27 10:10:26', null, '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_type` VALUES ('8', '通知状态', 'sys_notice_status', '0', 'admin', '2021-01-30 13:27:43', '', null, '通知状态列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('9', '操作类型', 'sys_oper_type', '0', 'admin', '2021-01-30 13:27:43', '', null, '操作类型列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('a2dce0cac0ce4d539ca0c9f4ee2893b1', '是否启动', 'is_active', '0', '1', '2021-08-12 16:15:39', '1', '2021-08-12 16:15:39', null, '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_type` VALUES ('aa0c5c14dca441e49c5d98c620429cda', '用户类型', 'sys_user_type', '0', '1', '2021-03-01 11:41:07', '1', '2021-03-01 11:41:07', null, '1', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_type` VALUES ('c3c50d4b965640b19a5e73597922ba20', '部门类型', 'sys_dept_type', '0', '1', '2021-02-25 17:11:16', '1', '2021-02-25 17:11:16', '1', '1', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_type` VALUES ('f90f3ffd1e474160a6cbeaf88cef2647', '导入文件状态', 'sys_upload_file_status', '0', '1', '2021-07-29 17:50:27', '1', '2021-07-29 17:53:35', null, '2', '0', '127.0.0.1', '100');

-- ----------------------------
-- Table structure for `sys_download_files`
-- ----------------------------
DROP TABLE IF EXISTS `sys_download_files`;
CREATE TABLE `sys_download_files` (
  `id` varchar(64) NOT NULL,
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名',
  `table_name` varchar(100) DEFAULT NULL COMMENT '类型',
  `file_type` varchar(10) DEFAULT NULL COMMENT '文件类型',
  `file_url` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `file_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `download_user` varchar(100) DEFAULT NULL COMMENT '下载用户',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名称',
  `dto_value` text COMMENT '对象值',
  `dto` text COMMENT '对象',
  `service_bean` varchar(1000) DEFAULT NULL COMMENT '所属Bean对象',
  `status` char(100) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1 停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `batch_count` int(11) DEFAULT NULL COMMENT '总处理数',
  `success_count` int(11) DEFAULT NULL COMMENT '已处理变更',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='导出记录表';

-- ----------------------------
-- Records of sys_download_files
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_job`
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES ('1', '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2021-01-30 13:27:43', '', '2021-06-09 09:58:47', '');
INSERT INTO `sys_job` VALUES ('2', '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2021-01-30 13:27:43', '', '2021-06-09 10:01:44', '');
INSERT INTO `sys_job` VALUES ('3', '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2021-01-30 13:27:43', '', '2021-06-09 11:15:02', '');
INSERT INTO `sys_job` VALUES ('4', '系统定时清理日志', 'DEFAULT', 'systemRemoveLogTask.removeLog(\'10\')', '0 0 1 * * ? *', '1', '1', '0', 'admin', '2021-07-06 09:56:16', 'admin', '2021-07-06 10:48:29', '每天凌晨一点清除日志，保留10天');

-- ----------------------------
-- Table structure for `sys_job_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_login_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统访问记录';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `menu_code` varchar(500) DEFAULT NULL COMMENT '菜单编码',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` varchar(64) DEFAULT '0' COMMENT '父菜单ID',
  `sort` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `is_frame` int(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `parent_ids` varchar(500) DEFAULT NULL COMMENT '父id集合',
  `tree_sort` int(11) DEFAULT '0' COMMENT '排序',
  `tree_sorts` varchar(500) DEFAULT NULL COMMENT '排序集合',
  `tree_level` int(11) DEFAULT NULL COMMENT '层级',
  `tree_leaf` char(1) DEFAULT NULL COMMENT '是否子节点（0是 1否）',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单信息表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('0a122b6b4ba046ea803d28e4ba04424c', 'sysPortletRemove', '门户小页管理删除', '2cd6e3f489bc44cd978b5f7d7772fa00', '10', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortlet:remove', '#', '1', '2021-05-10 17:09:43', '1', '2021-05-10 17:09:43', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/2cd6e3f489bc44cd978b5f7d7772fa00/0a122b6b4ba046ea803d28e4ba04424c', '40', '000010/000050/000030/40', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('0f69d2222aee4cd58975cc444c319171', 'sysPortletQuery', '门户小页管理查询', '2cd6e3f489bc44cd978b5f7d7772fa00', '10', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortlet:query', '#', '1', '2021-05-10 17:09:43', '1', '2021-05-10 17:09:43', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/2cd6e3f489bc44cd978b5f7d7772fa00/0f69d2222aee4cd58975cc444c319171', '10', '000010/000050/000030/10', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('1', 'system_manage', '系统管理', '0', '11', 'system', '', '1', '0', 'M', '0', '0', '', 'setting', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:33:36', '系统管理目录', '1', '10', '000010', '1', 'n', '', '127.0.0.1', '27', '0');
INSERT INTO `sys_menu` VALUES ('100', 'user-manager', '用户管理', '8f00f7f7d3b44aaba1041bc8fdf6c470', '1', 'user', 'system/user/SysUserIndex', '1', '0', 'C', '0', '0', 'system:user:list', 'team', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:42:04', '主要维护平台相关用户信息', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/100', '10', '000010/000010/000010', '3', 'n', '', '127.0.0.1', '18', '0');
INSERT INTO `sys_menu` VALUES ('1001', 'userQuery', '用户查询', '100', '1', '', '', '1', '0', 'F', '0', '0', 'system:user:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:43:32', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/100/1001', '10', '000010/000010/000010/000010', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1002', 'userAdd', '用户新增', '100', '2', '', '', '1', '0', 'F', '0', '0', 'system:user:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:43:42', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/100/1002', '20', '000010/000010/000010/000020', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1003', 'userEdit', '用户修改', '100', '3', '', '', '1', '0', 'F', '0', '0', 'system:user:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:43:56', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/100/1003', '30', '000010/000010/000010/000030', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1004', 'userRemove', '用户删除', '100', '4', '', '', '1', '0', 'F', '0', '0', 'system:user:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:44:06', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/100/1004', '40', '000010/000010/000010/000040', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1005', 'userExport', '用户导出', '100', '5', '', '', '1', '0', 'F', '0', '0', 'system:user:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:44:20', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/100/1005', '50', '000010/000010/000010/000050', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1006', 'userImport', '用户导入', '100', '6', '', '', '1', '0', 'F', '0', '0', 'system:user:import', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:45:01', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/100/1006', '0', '000010/000010/000010/000000', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1007', 'userReetPwd', '重置密码', '100', '7', '', '', '1', '0', 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:44:37', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/100/1007', '0', '000010/000010/000010/000000', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1008', 'roleQuery', '角色查询', '101', '1', '', '', '1', '0', 'F', '0', '0', 'system:role:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:48:38', '', '1/ed0919f0a42e4a07993622037df86a33/101/1008', '10', '000010/000020/000010/000010', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1009', 'roleAdd', '角色新增', '101', '2', '', '', '1', '0', 'F', '0', '0', 'system:role:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:48:55', '', '1/ed0919f0a42e4a07993622037df86a33/101/1009', '20', '000010/000020/000010/000020', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('101', 'role-manager', '角色管理', 'ed0919f0a42e4a07993622037df86a33', '2', 'role', 'system/role/QueryList', '1', '0', 'C', '0', '0', '', 'contacts', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:49:36', '维护平台各角色数据以及权限分配.', '1/ed0919f0a42e4a07993622037df86a33/101', '10', '000010/000020/000010', '3', 'n', '', '127.0.0.1', '17', '0');
INSERT INTO `sys_menu` VALUES ('1010', 'roleEdit', '角色修改', '101', '3', '', '', '1', '0', 'F', '0', '0', 'system:role:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:03', '', '1/ed0919f0a42e4a07993622037df86a33/101/1010', '30', '000010/000020/000010/000030', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1011', 'roleRemove', '角色删除', '101', '4', '', '', '1', '0', 'F', '0', '0', 'system:role:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:11', '', '1/ed0919f0a42e4a07993622037df86a33/101/1011', '40', '000010/000020/000010/000040', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1012', 'roleExport', '角色导出', '101', '5', '', '', '1', '0', 'F', '0', '0', 'system:role:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:18', '', '1/ed0919f0a42e4a07993622037df86a33/101/1012', '50', '000010/000020/000010/000050', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1013', 'menuQuery', '菜单查询', '102', '1', '', '', '1', '0', 'F', '0', '0', 'system:menu:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:37', '', '1/2ea5441cad6d47679b9029b6ac2d291f/102/1013', '10', '000010/000030/000010/000010', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1014', 'menuAdd', '菜单新增', '102', '2', '', '', '1', '0', 'F', '0', '0', 'system:menu:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:45', '', '1/2ea5441cad6d47679b9029b6ac2d291f/102/1014', '20', '000010/000030/000010/000020', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1015', 'menuEdit', '菜单修改', '102', '3', '', '', '1', '0', 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:50:00', '', '1/2ea5441cad6d47679b9029b6ac2d291f/102/1015', '30', '000010/000030/000010/000030', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1016', 'menuRemove', '菜单删除', '102', '4', '', '', '1', '0', 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:50:08', '', '1/2ea5441cad6d47679b9029b6ac2d291f/102/1016', '40', '000010/000030/000010/000040', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1017', 'deptQuery', '部门查询', '103', '1', '', '', '1', '0', 'F', '0', '0', 'system:dept:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:43:41', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/103/1017', '30', '000010/000010/000020/000030', '4', 'y', '', '127.0.0.1', '10', '0');
INSERT INTO `sys_menu` VALUES ('1018', 'deptAdd', '部门新增', '103', '2', '', '', '1', '0', 'F', '0', '0', 'system:dept:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:43:51', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/103/1018', '40', '000010/000010/000020/000040', '4', 'y', '', '127.0.0.1', '10', '0');
INSERT INTO `sys_menu` VALUES ('1019', 'deptEdit', '部门修改', '103', '3', '', '', '1', '0', 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:43:20', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/103/1019', '10', '000010/000010/000020/000010', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('102', 'menu-manager', '菜单管理', '2ea5441cad6d47679b9029b6ac2d291f', '3', 'menu', 'system/menu/MenuIndex', '1', '0', 'C', '0', '0', 'system:menu:list', 'bars', 'admin', '2021-01-30 13:27:43', '1', '2021-06-08 10:08:35', '平台所有菜单维护', '1/2ea5441cad6d47679b9029b6ac2d291f/102', '10', '000010/000030/000010', '3', 'n', '', '127.0.0.1', '14', '0');
INSERT INTO `sys_menu` VALUES ('1020', 'deptRemove', '部门删除', '103', '4', '', '', '1', '0', 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:43:31', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/103/1020', '20', '000010/000010/000020/000020', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1021', 'postQuery', '岗位查询', '104', '1', '', '', '1', '0', 'F', '0', '0', 'system:post:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:44:29', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/104/1021', '10', '000010/000010/000030/000010', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1022', 'postAdd', '岗位新增', '104', '2', '', '', '1', '0', 'F', '0', '0', 'system:post:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:44:40', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/104/1022', '20', '000010/000010/000030/000020', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1023', 'postEdit', '岗位修改', '104', '3', '', '', '1', '0', 'F', '0', '0', 'system:post:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:45:00', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/104/1023', '30', '000010/000010/000030/000030', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1024', 'postRemove', '岗位删除', '104', '4', '', '', '1', '0', 'F', '0', '0', 'system:post:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:45:36', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/104/1024', '40', '000010/000010/000030/000040', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1025', 'postExport', '岗位导出', '104', '5', '', '', '1', '0', 'F', '0', '0', 'system:post:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:45:48', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/104/1025', '50', '000010/000010/000030/000050', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1026', 'dictQuery', '字典查询', '105', '1', '#', '', '1', '0', 'F', '0', '0', 'system:dict:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:11:42', '', '1/2ea5441cad6d47679b9029b6ac2d291f/105/1026', '10', '000010/000030/000020/000010', '4', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1027', 'dictAdd', '字典新增', '105', '2', '#', '', '1', '0', 'F', '0', '0', 'system:dict:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:08', '', '1/2ea5441cad6d47679b9029b6ac2d291f/105/1027', '20', '000010/000030/000020/000020', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1028', 'dictEdit', '字典修改', '105', '3', '#', '', '1', '0', 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:16', '', '1/2ea5441cad6d47679b9029b6ac2d291f/105/1028', '30', '000010/000030/000020/000030', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1029', 'dictRemove', '字典删除', '105', '4', '#', '', '1', '0', 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:22', '', '1/2ea5441cad6d47679b9029b6ac2d291f/105/1029', '40', '000010/000030/000020/000040', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('103', 'dept-manager', '部门管理', '8f00f7f7d3b44aaba1041bc8fdf6c470', '4', 'dept', 'system/dept/SysDeptIndex', '1', '0', 'C', '0', '0', 'system:dept:list', 'cluster', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:42:58', '部门管理菜单', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/103', '20', '000010/000010/000020', '3', 'n', '', '127.0.0.1', '13', '0');
INSERT INTO `sys_menu` VALUES ('1030', 'dictExport', '字典导出', '105', '5', '#', '', '1', '0', 'F', '0', '0', 'system:dict:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:28', '', '1/2ea5441cad6d47679b9029b6ac2d291f/105/1030', '50', '000010/000030/000020/000050', '4', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1031', 'configQuery', '参数查询', '106', '1', '#', '', '1', '0', 'F', '0', '0', 'system:config:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:37', '', '1/2ea5441cad6d47679b9029b6ac2d291f/106/1031', '10', '000010/000030/000030/000010', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1032', 'configAdd', '参数新增', '106', '2', '#', '', '1', '0', 'F', '0', '0', 'system:config:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:44', '', '1/2ea5441cad6d47679b9029b6ac2d291f/106/1032', '20', '000010/000030/000030/000020', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1033', 'configEdit', '参数修改', '106', '3', '#', '', '1', '0', 'F', '0', '0', 'system:config:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:50', '', '1/2ea5441cad6d47679b9029b6ac2d291f/106/1033', '30', '000010/000030/000030/000030', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1034', 'configRemove', '参数删除', '106', '4', '#', '', '1', '0', 'F', '0', '0', 'system:config:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:57', '', '1/2ea5441cad6d47679b9029b6ac2d291f/106/1034', '40', '000010/000030/000030/000040', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1035', 'configExport', '参数导出', '106', '5', '#', '', '1', '0', 'F', '0', '0', 'system:config:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:04', '', '1/2ea5441cad6d47679b9029b6ac2d291f/106/1035', '50', '000010/000030/000030/000050', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1036', 'notice-query', '公告查询', '107', '1', '#', '', '1', '0', 'F', '0', '0', 'system:notice:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:17', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/107/1036', '10', '000010/000050/000010/000010', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1037', 'noticeAdd', '公告新增', '107', '2', '#', '', '1', '0', 'F', '0', '0', 'system:notice:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:23', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/107/1037', '20', '000010/000050/000010/000020', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1038', 'noticeEdit', '公告修改', '107', '3', '#', '', '1', '0', 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:29', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/107/1038', '30', '000010/000050/000010/000030', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1039', 'noticeRemove', '公告删除', '107', '4', '#', '', '1', '0', 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:36', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/107/1039', '40', '000010/000050/000010/000040', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('104', 'post-manager', '岗位管理', '8f00f7f7d3b44aaba1041bc8fdf6c470', '5', 'post', 'system/post/PostIndex', '1', '0', 'C', '0', '0', 'system:post:list', 'idcard', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:44:09', '岗位管理菜单', '1/8f00f7f7d3b44aaba1041bc8fdf6c470/104', '30', '000010/000010/000030', '3', 'n', '', '127.0.0.1', '14', '0');
INSERT INTO `sys_menu` VALUES ('1040', 'operateLogQuery', '操作查询', '500', '1', '#', '', '1', '0', 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:05', '', '1/108/500/1040', '10', '000010/000040/000010/000010', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1041', 'operateLogRemove', '操作删除', '500', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:13', '', '1/108/500/1041', '20', '000010/000040/000010/000020', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1042', 'operateLoExport', '日志导出', '500', '4', '#', '', '1', '0', 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:20', '', '1/108/500/1042', '30', '000010/000040/000010/000030', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1043', 'loginLogQuery', '登录查询', '501', '1', '#', '', '1', '0', 'F', '0', '0', 'monitor:loginLog:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:30', '', '1/108/501/1043', '10', '000010/000040/000020/000010', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1044', 'loginLogRemove', '登录删除', '501', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:loginLog:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:36', '', '1/108/501/1044', '20', '000010/000040/000020/000020', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1099', 'loginLogUnlock', '账户解锁', '501', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:loginLog:unlock', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:36', '', '1/108/501/1044', '20', '000010/000040/000020/000020', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1045', 'loginLogExport', '日志导出', '501', '3', '#', '', '1', '0', 'F', '0', '0', 'monitor:loginLog:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:43', '', '1/108/501/1045', '30', '000010/000040/000020/000030', '4', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1046', 'onlineQuery', '在线查询', '109', '1', '#', '', '1', '0', 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:15:40', '', '2/109/1046', '10', '000020/000010/000010', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1047', 'onlineBatchLogout', '批量强退', '109', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:15:47', '', '2/109/1047', '20', '000020/000010/000020', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1048', 'onlineLogout', '单条强退', '109', '3', '#', '', '1', '0', 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:15:54', '', '2/109/1048', '30', '000020/000010/000030', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1049', 'jobQuery', '任务查询', '110', '1', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:06', '', '2/110/1049', '10', '000020/000020/000010', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('105', 'dict-manager', '字典管理', '2ea5441cad6d47679b9029b6ac2d291f', '6', 'dict', 'system/dict/DictIndex', '1', '0', 'C', '0', '0', 'system:dict:list', 'read', 'admin', '2021-01-30 13:27:43', '1', '2021-06-08 09:53:17', '字典管理菜单', '1/2ea5441cad6d47679b9029b6ac2d291f/105', '20', '000010/000030/000020', '3', 'n', '', '127.0.0.1', '12', '0');
INSERT INTO `sys_menu` VALUES ('1050', 'jobAdd', '任务新增', '110', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:17', '', '2/110/1050', '20', '000020/000020/000020', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1051', 'jobEdit', '任务修改', '110', '3', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:25', '', '2/110/1051', '30', '000020/000020/000030', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1052', 'jobRemove', '任务删除', '110', '4', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:32', '', '2/110/1052', '40', '000020/000020/000040', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1053', 'jobChangeStatus', '状态修改', '110', '5', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:42', '', '2/110/1053', '50', '000020/000020/000050', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1054', 'jobExport', '任务导出', '110', '7', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:49', '', '2/110/1054', '60', '000020/000020/000060', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1055', 'genQuery', '生成查询', '115', '1', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:08', '', '3/115/1055', '10', '000030/000020/000010', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1056', 'genEdit', '生成修改', '115', '2', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:14', '', '3/115/1056', '20', '000030/000020/000020', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1057', 'genRemove', '生成删除', '115', '3', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:23', '', '3/115/1057', '30', '000030/000020/000030', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1058', 'genImport', '导入代码', '115', '2', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:30', '', '3/115/1058', '40', '000030/000020/000040', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1059', 'genPreview', '预览代码', '115', '4', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:38', '', '3/115/1059', '50', '000030/000020/000050', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('106', 'config-manager', '参数设置', '2ea5441cad6d47679b9029b6ac2d291f', '7', 'config', 'system/config/ConfigIndex', '1', '0', 'C', '0', '0', 'system:config:list', 'code', 'admin', '2021-01-30 13:27:43', '1', '2021-06-08 09:53:33', '参数设置菜单', '1/2ea5441cad6d47679b9029b6ac2d291f/106', '30', '000010/000030/000030', '3', 'n', '', '127.0.0.1', '12', '0');
INSERT INTO `sys_menu` VALUES ('1060', 'gen-code', '生成代码', '115', '5', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:45', '', '3/115/1060', '60', '000030/000020/000060', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('107', 'notice-manager', '通知公告', 'd8a45047891d4a28a5a3faf96f2b8d97', '8', 'notice', 'system/notice/NoticeIndex', '1', '0', 'C', '0', '0', 'system:notice:list', 'notification', 'admin', '2021-01-30 13:27:43', '1', '2021-06-08 10:05:31', '通知公告菜单', '1/d8a45047891d4a28a5a3faf96f2b8d97/107', '10', '000010/000050/000010', '3', 'n', '', '127.0.0.1', '11', '0');
INSERT INTO `sys_menu` VALUES ('108', 'system-log', '日志管理', '1', '9', 'log', '', '1', '0', 'M', '0', '0', '', 'copy', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:41:19', '日志管理菜单', '1/108', '40', '000010/000040', '2', 'n', '', '127.0.0.1', '11', '0');
INSERT INTO `sys_menu` VALUES ('109', 'online-user', '在线用户', '2', '1', 'online', 'monitor/online/index', '1', '0', 'C', '0', '0', 'monitor:online:list', 'dot-chart', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:38:59', '在线用户菜单', '2/109', '10', '000020/000010', '2', 'n', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('110', 'job-manager', '定时任务', '2', '2', 'job', 'monitor/job/index', '1', '0', 'C', '0', '0', 'monitor:job:list', 'bar-chart', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:39:25', '定时任务菜单', '2/110', '20', '000020/000020', '2', 'n', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('111', 'druid-monitor', '数据监控', '2', '3', 'druid', 'monitor/druid/index', '1', '0', 'C', '0', '0', 'monitor:druid:list', 'dashboard', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:52:49', '数据监控菜单', '2/111', '30', '000020/000030', '2', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('112', 'server-monitor', '服务监控', '2', '4', 'server', 'monitor/server/index', '1', '0', 'C', '0', '0', 'monitor:server:list', 'pie-chart', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:41:17', '服务监控菜单', '2/112', '40', '000020/000040', '2', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('113', 'cache-monitor', '缓存监控', '2', '5', 'cache', 'monitor/cache/index', '1', '0', 'C', '0', '0', 'monitor:cache:list', 'box-plot', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:41:35', '缓存监控菜单', '2/113', '50', '000020/000050', '2', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('114', 'form-generate', '表单构建', '3', '1', 'build', 'tool/build/index', '1', '0', 'C', '0', '0', 'tool:build:list', 'bars', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:41:50', '表单构建菜单', '3/114', '10', '000030/000010', '2', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('115', 'code-gen', '代码生成', '3', '2', 'gen', 'tool/gen/index', '1', '0', 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:17:26', '代码生成菜单', '3/115', '20', '000030/000020', '2', 'n', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('116', 'saggger-manager', '系统接口', '3', '3', 'swagger', 'tool/swagger/index', '1', '0', 'C', '0', '0', 'tool:swagger:list', 'api', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:42:53', '系统接口菜单', '3/116', '30', '000030/000030', '2', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1d43e70d8a734644aa0d062dc230bc4f', 'testHttp', '测试外链', '2007', '0', 'http://localhost:8000/', '', '0', '0', 'C', '0', '0', '', 'compress', '1', '2021-05-06 16:02:42', '1', '2021-05-06 16:06:25', '', '2007/1d43e70d8a734644aa0d062dc230bc4f', '150', '000050/000150', '2', 'y', '100', '127.0.0.1', '4', '0');
INSERT INTO `sys_menu` VALUES ('1f89001d84ba48dcb41abc13cccc67e4', 'auth-center', '菜单授权', 'ed0919f0a42e4a07993622037df86a33', '0', 'sysAuth', 'system/role/SysRoleAuth', '1', '0', 'C', '0', '0', '', 'api', '1', '2021-06-04 13:44:30', '1', '2021-09-13 08:52:05', '', '1/ed0919f0a42e4a07993622037df86a33/1f89001d84ba48dcb41abc13cccc67e4', '30', '000010/000020/000030', '3', 'y', '100', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('2', 'system_monitor', '系统监控', '0', '2', 'monitor', '', '1', '0', 'M', '0', '0', '', 'fund', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:45:13', '系统监控目录', '2', '20', '000020', '1', 'n', '', '127.0.0.1', '11', '0');
INSERT INTO `sys_menu` VALUES ('2004', '2004', '报表看板', '2007', '4', 'echart', 'demo/chart/echartDashBoard', '1', '0', 'C', '0', '0', '', 'monitor', 'admin', '2021-02-01 11:15:22', '1', '2021-03-26 15:21:57', '', '2007/2004', '10', '000050/000010', '2', 'y', '', '127.0.0.1', '11', '0');
INSERT INTO `sys_menu` VALUES ('2007', 'sys_demo', '示例中心', '0', '6', 'demo', '', '1', '0', 'M', '0', '0', '', 'pie-chart', 'admin', '2021-02-02 09:30:51', '1', '2021-09-04 16:20:47', '', '2007', '50', '000050', '1', 'n', '', '127.0.0.1', '18', '0');
INSERT INTO `sys_menu` VALUES ('2008', '2008', '选人页', '2007', '1', 'selectUser', 'demo/selectUser/selectUser', '1', '0', 'C', '0', '0', '', 'peoples', 'admin', '2021-02-02 09:31:52', '1', '2021-03-26 15:22:03', '', '2007/2008', '20', '000050/000020', '2', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('2012', 'system-icon-select', '图标选择', 'd8a45047891d4a28a5a3faf96f2b8d97', '10', 'iconSelect', 'system/iconSelect/IconSelect', '1', '0', 'C', '0', '0', '', 'shopping', 'admin', '2021-02-06 15:04:26', '1', '2021-06-08 10:05:54', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/2012', '20', '000010/000050/000020', '3', 'y', '', '127.0.0.1', '10', '0');
INSERT INTO `sys_menu` VALUES ('2018', '2018', '选部门', '2007', '5', 'selectDept', 'demo/selectDept/selectDept', '1', '0', 'C', '0', '0', '', 'tree', 'admin', '2021-03-01 10:42:25', '1', '2021-03-26 15:22:18', '', '2007/2018', '30', '000050/000030', '2', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('2bb61f0f958c400faf97f37d35a1b7d6', 'sysPortalConfigEdit', '多栏目门户配置修改', 'e70d56d0bec74c4aba2105854ce5608f', '30', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortalConfig:edit', '#', '1', '2021-05-10 18:20:28', '1', '2021-05-10 18:20:28', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/e70d56d0bec74c4aba2105854ce5608f/2bb61f0f958c400faf97f37d35a1b7d6', '30', '000010/000050/000040/30', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('2cd6e3f489bc44cd978b5f7d7772fa00', 'sysPortlet', '工作台小页', 'd8a45047891d4a28a5a3faf96f2b8d97', '100', 'sysPortlet', 'system/sysportlet/index', '1', '0', 'C', '0', '0', 'system:sysPortlet:list', 'color', '1', '2021-05-10 17:09:43', '1', '2021-09-13 08:53:15', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/2cd6e3f489bc44cd978b5f7d7772fa00', '30', '000010/000050/000030', '3', 'n', '', '127.0.0.1', '4', '0');
INSERT INTO `sys_menu` VALUES ('2e8945e8fe7140deb3375664de4b0e9d', 'sysUploadFilesQuery', '导入记录查询', '4e3d48641bcf44f18a59a5ba35315f28', '10', '#', '', '1', '0', 'F', '0', '0', 'system:sysUploadFiles:query', '#', '1', '2021-07-29 18:11:06', '1', '2021-07-29 18:11:06', '', 'd54485baef564e40a4b40249a937734b/4e3d48641bcf44f18a59a5ba35315f28/2e8945e8fe7140deb3375664de4b0e9d', '10', '000040/000010/10', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('2ea5441cad6d47679b9029b6ac2d291f', 'system-setting', '系统设置', '1', '0', 'sysSetting', '', '1', '0', 'M', '0', '0', '', 'setting', '1', '2021-06-08 09:52:35', '1', '2021-09-04 10:40:59', '', '1/2ea5441cad6d47679b9029b6ac2d291f', '30', '000010/000030', '2', 'n', '100', '127.0.0.1', '5', '0');
INSERT INTO `sys_menu` VALUES ('3', 'system_tool', '系统工具', '0', '3', 'tool', '', '1', '0', 'M', '0', '0', '', 'appstore', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:34:25', '系统工具目录', '3', '30', '000030', '1', 'n', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('330d1940a6a64dc7adcfa03fc9f57d25', 'simcard_oper_log', '卡操作日志查询', '76262a407245454492ccb8b100976ae9', '0', '', '', '1', '0', 'F', '0', '0', 'm2mSimcardOperLogQuery', '', '1', '2021-08-02 09:05:32', '1', '2021-08-02 09:05:32', '', '2dc9dcc092134fb18d15cb4966100b39/76262a407245454492ccb8b100976ae9/330d1940a6a64dc7adcfa03fc9f57d25', '20', '000210/000030/000020', '3', 'y', '100', '127.0.0.1', '1', '0');
INSERT INTO `sys_menu` VALUES ('4e3d48641bcf44f18a59a5ba35315f28', 'sysUploadFiles', '上传中心', 'd54485baef564e40a4b40249a937734b', '130', 'sysUploadFiles', 'system/sysuploadfiles/index', '1', '0', 'C', '0', '0', 'system:sysUploadFiles:list', 'upload', '1', '2021-07-29 18:11:06', '1', '2021-08-26 18:04:40', '', 'd54485baef564e40a4b40249a937734b/4e3d48641bcf44f18a59a5ba35315f28', '10', '000040/000010', '2', 'n', '', '127.0.0.1', '4', '0');
INSERT INTO `sys_menu` VALUES ('500', 'sys-operate-log', '操作日志', '108', '1', 'operlog', 'monitor/operlog/OperlogIndex', '1', '0', 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2021-01-30 13:27:43', '1', '2021-04-02 10:06:43', '操作日志菜单', '1/108/500', '10', '000010/000040/000010', '3', 'n', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('501', 'sys-login-log', '登录日志', '108', '2', 'loginLog', 'monitor/loginlog/LoginLogIndex', '1', '0', 'C', '0', '0', 'monitor:loginLog:list', 'loginLog', 'admin', '2021-01-30 13:27:43', '1', '2021-04-02 10:07:05', '登录日志菜单', '1/108/501', '20', '000010/000040/000020', '3', 'n', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('55f1fb9461b1476eb2bec8541b84d22e', 'sysDownloadFilesQuery', '导出记录查询', 'e16303c3ce5e4a03b045a71501cb301e', '10', '#', '', '1', '0', 'F', '0', '0', 'system:sysDownloadFiles:query', '#', '1', '2021-07-27 11:28:17', '1', '2021-07-27 11:28:17', '', 'd54485baef564e40a4b40249a937734b/e16303c3ce5e4a03b045a71501cb301e/55f1fb9461b1476eb2bec8541b84d22e', '10', '000040/000020/10', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('56b65ea40f9b4385991383fd766b5c06', 'simcard-query', 'SIM卡查看', '76262a407245454492ccb8b100976ae9', '0', '', '', '1', '0', 'F', '0', '0', 'iot:m2mSimcard:query', '', '1', '2021-08-02 09:04:36', '1', '2021-08-02 09:04:36', '', '2dc9dcc092134fb18d15cb4966100b39/76262a407245454492ccb8b100976ae9/56b65ea40f9b4385991383fd766b5c06', '10', '000210/000030/000010', '3', 'y', '100', '127.0.0.1', '1', '0');
INSERT INTO `sys_menu` VALUES ('652e86f6905d478e924f0641ebe88874', 'simcard-list', '卡列表查询', '76262a407245454492ccb8b100976ae9', '0', '', '', '1', '0', 'F', '0', '0', 'iot:m2mSimcard:list', '', '1', '2021-08-02 13:52:45', '1', '2021-08-02 13:52:45', '', '2dc9dcc092134fb18d15cb4966100b39/76262a407245454492ccb8b100976ae9/652e86f6905d478e924f0641ebe88874', '30', '000210/000030/000030', '3', 'y', '100', '127.0.0.1', '1', '0');
INSERT INTO `sys_menu` VALUES ('67c8d43a175040a5874439d1516308da', 'sys-job-log', '调度日志', '108', '0', 'joblog', 'monitor/job/log', '1', '0', 'C', '0', '0', '', 'bug', '1', '2021-06-18 16:39:15', '1', '2021-06-18 16:42:49', '', '1/108/67c8d43a175040a5874439d1516308da', '30', '000010/000040/000030', '3', 'y', '100', '127.0.0.1', '5', '0');
INSERT INTO `sys_menu` VALUES ('6c5f7c21b8554629824ba898f20fba78', 'avue-ui', '可视化大屏', 'efa0099fd27f47008b4c881f27d93ac3', '0', ' http://localhost:8081/', '', '0', '0', 'C', '0', '0', '', 'monitor', '1', '2021-05-07 09:05:06', '1', '2021-09-04 16:31:35', '', 'efa0099fd27f47008b4c881f27d93ac3/6c5f7c21b8554629824ba898f20fba78', '10', '000060/000010', '2', 'y', '100', '127.0.0.1', '2', '0');
INSERT INTO `sys_menu` VALUES ('759d7e551ed148adbf1152f8fc55fe34', 'sysDownloadFilesExport', '导出记录导出', 'e16303c3ce5e4a03b045a71501cb301e', '50', '#', '', '1', '0', 'F', '0', '0', 'system:sysDownloadFiles:export', '#', '1', '2021-07-27 11:28:17', '1', '2021-07-27 11:28:17', '', 'd54485baef564e40a4b40249a937734b/e16303c3ce5e4a03b045a71501cb301e/759d7e551ed148adbf1152f8fc55fe34', '50', '000040/000020/50', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('7e00c054a0d34baea9dbcec52f239d25', 'cache-manager', '缓存列表', '2', '0', 'cacheList', 'monitor/cache/indexCacheList', '1', '0', 'C', '0', '0', '', 'dashboardNew', '1', '2021-08-05 14:51:44', '1', '2021-08-05 14:53:53', null, '2/7e00c054a0d34baea9dbcec52f239d25', '55', '000020/000055', '2', 'y', '100', '127.0.0.1', '2', '0');
INSERT INTO `sys_menu` VALUES ('841b8c3b06b1496da99c541629a7ec90', 'sysPortletAdd', '门户小页管理新增', '2cd6e3f489bc44cd978b5f7d7772fa00', '20', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortlet:add', '#', '1', '2021-05-10 17:09:43', '1', '2021-05-10 17:09:43', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/2cd6e3f489bc44cd978b5f7d7772fa00/841b8c3b06b1496da99c541629a7ec90', '20', '000010/000050/000030/20', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('8f00f7f7d3b44aaba1041bc8fdf6c470', 'org-manager', '组织管理', '1', '0', 'org', '', '1', '0', 'M', '0', '0', '', 'appstore', '1', '2021-06-08 09:31:04', '1', '2021-09-04 10:39:56', '', '1/8f00f7f7d3b44aaba1041bc8fdf6c470', '10', '000010/000010', '2', 'n', '100', '127.0.0.1', '4', '0');
INSERT INTO `sys_menu` VALUES ('91c141056bd24d3595f418883a4aed0b', 'sysDownloadFilesRemove', '导出记录删除', 'e16303c3ce5e4a03b045a71501cb301e', '10', '#', '', '1', '0', 'F', '0', '0', 'system:sysDownloadFiles:remove', '#', '1', '2021-07-27 11:28:17', '1', '2021-07-27 11:28:17', '', 'd54485baef564e40a4b40249a937734b/e16303c3ce5e4a03b045a71501cb301e/91c141056bd24d3595f418883a4aed0b', '40', '000040/000020/40', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('95329865e4c74ac89867ce4e9e693efd', 'sysUploadFilesRemove', '导入记录删除', '4e3d48641bcf44f18a59a5ba35315f28', '10', '#', '', '1', '0', 'F', '0', '0', 'system:sysUploadFiles:remove', '#', '1', '2021-07-29 18:11:06', '1', '2021-07-29 18:11:06', '', 'd54485baef564e40a4b40249a937734b/4e3d48641bcf44f18a59a5ba35315f28/95329865e4c74ac89867ce4e9e693efd', '40', '000040/000010/40', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('992a9b58c99a427ebfcbb7533f55a2cb', 'sysPortletEdit', '门户小页管理修改', '2cd6e3f489bc44cd978b5f7d7772fa00', '30', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortlet:edit', '#', '1', '2021-05-10 17:09:43', '1', '2021-05-10 17:09:43', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/2cd6e3f489bc44cd978b5f7d7772fa00/992a9b58c99a427ebfcbb7533f55a2cb', '30', '000010/000050/000030/30', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('aa7e8d7ac1544c1f8ebfb6afa16351f9', 'sysPortalConfigAdd', '多栏目门户配置新增', 'e70d56d0bec74c4aba2105854ce5608f', '20', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortalConfig:add', '#', '1', '2021-05-10 18:20:28', '1', '2021-05-10 18:20:28', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/e70d56d0bec74c4aba2105854ce5608f/aa7e8d7ac1544c1f8ebfb6afa16351f9', '20', '000010/000050/000040/20', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('b3418a3b67a9481bb833b8674e4250fc', 'sysDownloadFilesAdd', '导出记录新增', 'e16303c3ce5e4a03b045a71501cb301e', '20', '#', '', '1', '0', 'F', '0', '0', 'system:sysDownloadFiles:add', '#', '1', '2021-07-27 11:28:17', '1', '2021-07-27 11:28:17', '', 'd54485baef564e40a4b40249a937734b/e16303c3ce5e4a03b045a71501cb301e/b3418a3b67a9481bb833b8674e4250fc', '20', '000040/000020/20', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('b61000137c134a7393ded9b69512e1f0', 'sysUploadFilesEdit', '导入记录修改', '4e3d48641bcf44f18a59a5ba35315f28', '30', '#', '', '1', '0', 'F', '0', '0', 'system:sysUploadFiles:edit', '#', '1', '2021-07-29 18:11:06', '1', '2021-07-29 18:11:06', '', 'd54485baef564e40a4b40249a937734b/4e3d48641bcf44f18a59a5ba35315f28/b61000137c134a7393ded9b69512e1f0', '30', '000040/000010/30', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('b882b12ce42645039ed8af35377e4a75', 'sysDownloadFilesEdit', '导出记录修改', 'e16303c3ce5e4a03b045a71501cb301e', '30', '#', '', '1', '0', 'F', '0', '0', 'system:sysDownloadFiles:edit', '#', '1', '2021-07-27 11:28:17', '1', '2021-07-27 11:28:17', '', 'd54485baef564e40a4b40249a937734b/e16303c3ce5e4a03b045a71501cb301e/b882b12ce42645039ed8af35377e4a75', '30', '000040/000020/30', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('bad0110ab2cf471b8858b09a52c184fd', 'sysUploadFilesAdd', '导入记录新增', '4e3d48641bcf44f18a59a5ba35315f28', '20', '#', '', '1', '0', 'F', '0', '0', 'system:sysUploadFiles:add', '#', '1', '2021-07-29 18:11:06', '1', '2021-07-29 18:11:06', '', 'd54485baef564e40a4b40249a937734b/4e3d48641bcf44f18a59a5ba35315f28/bad0110ab2cf471b8858b09a52c184fd', '20', '000040/000010/20', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('c2b336b9b41d47388115c397ca077e2f', 'sysPortalConfigQuery', '多栏目门户配置查询', 'e70d56d0bec74c4aba2105854ce5608f', '10', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortalConfig:query', '#', '1', '2021-05-10 18:20:28', '1', '2021-05-10 18:20:28', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/e70d56d0bec74c4aba2105854ce5608f/c2b336b9b41d47388115c397ca077e2f', '10', '000010/000050/000040/10', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('c9d88c5c57494bfcac8137ffd0aa3121', 'genconfig-template', '模板配置', '3', '0', 'template', 'tool/gen/genconfigtemplate/index', '1', '0', 'C', '0', '0', '', 'picture', '1', '2021-03-07 12:28:59', '1', '2021-04-23 10:43:18', '', '3/c9d88c5c57494bfcac8137ffd0aa3121', '40', '000030/000040', '2', 'y', '100', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('d54485baef564e40a4b40249a937734b', 'file-center', '文件中心', '0', '0', 'fileCenter', null, '1', '0', 'M', '0', '0', '', 'tool', '1', '2021-08-04 14:15:20', '1', '2021-09-04 10:35:14', null, 'd54485baef564e40a4b40249a937734b', '40', '000040', '1', 'n', '100', '127.0.0.1', '4', '0');
INSERT INTO `sys_menu` VALUES ('d87a284bb7e34ae6997e6ab86b7254a5', 'sysUploadFilesExport', '导入记录导出', '4e3d48641bcf44f18a59a5ba35315f28', '50', '#', '', '1', '0', 'F', '0', '0', 'system:sysUploadFiles:export', '#', '1', '2021-07-29 18:11:06', '1', '2021-07-29 18:11:06', '', 'd54485baef564e40a4b40249a937734b/4e3d48641bcf44f18a59a5ba35315f28/d87a284bb7e34ae6997e6ab86b7254a5', '50', '000040/000010/50', '3', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('d8a45047891d4a28a5a3faf96f2b8d97', 'system-app', '系统应用', '1', '0', 'sysApp', '', '1', '0', 'M', '0', '0', '', 'laptop', '1', '2021-06-08 10:05:01', '1', '2021-09-04 10:41:12', '', '1/d8a45047891d4a28a5a3faf96f2b8d97', '50', '000010/000050', '2', 'n', '100', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('e16303c3ce5e4a03b045a71501cb301e', 'sysDownloadFiles', '下载中心', 'd54485baef564e40a4b40249a937734b', '50', 'sysDownloadFiles', 'system/sysdownloadfiles/index', '1', '0', 'C', '0', '0', 'system:sysDownloadFiles:list', 'download', '1', '2021-07-27 11:28:16', '1', '2021-08-26 18:06:48', '', 'd54485baef564e40a4b40249a937734b/e16303c3ce5e4a03b045a71501cb301e', '20', '000040/000020', '2', 'n', '', '127.0.0.1', '3', '0');
INSERT INTO `sys_menu` VALUES ('e70d56d0bec74c4aba2105854ce5608f', 'sysPortalConfig', '工作台配置', 'd8a45047891d4a28a5a3faf96f2b8d97', '110', 'sysPortalConfig', 'system/sysportalconfig/index', '1', '0', 'C', '0', '0', 'system:sysPortalConfig:list', 'dict', '1', '2021-05-10 18:20:28', '1', '2021-06-18 17:50:07', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/e70d56d0bec74c4aba2105854ce5608f', '40', '000010/000050/000040', '3', 'n', '', '127.0.0.1', '2', '0');
INSERT INTO `sys_menu` VALUES ('ed0919f0a42e4a07993622037df86a33', 'auth-manager', '权限管理', '1', '0', 'auth', '', '1', '0', 'M', '0', '0', '', 'solution', '1', '2021-06-08 09:49:56', '1', '2021-09-04 10:40:46', '', '1/ed0919f0a42e4a07993622037df86a33', '20', '000010/000020', '2', 'n', '100', '127.0.0.1', '3', '0');
INSERT INTO `sys_menu` VALUES ('efa0099fd27f47008b4c881f27d93ac3', 'system_chart', '可视化大屏', '0', '0', 'vuedata', '', '1', '0', 'M', '1', '0', '', 'dashboardNew', '1', '2021-05-06 18:25:31', '1', '2021-09-04 10:38:23', '', 'efa0099fd27f47008b4c881f27d93ac3', '60', '000060', '1', 'n', '100', '127.0.0.1', '10', '0');
INSERT INTO `sys_menu` VALUES ('f099257fce5b46519b4c05234e3a3db0', 'sysPortletExport', '门户小页管理查询', '2cd6e3f489bc44cd978b5f7d7772fa00', '50', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortlet:export', '#', '1', '2021-05-10 17:09:43', '1', '2021-05-10 17:09:43', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/2cd6e3f489bc44cd978b5f7d7772fa00/f099257fce5b46519b4c05234e3a3db0', '50', '000010/000050/000030/50', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('fa6c148d7bec4c31aa46b7a73d575af3', 'admin-oper', '管理员操作权限', 'ed9087f850894d21bb41044c8b95d415', '0', '', '', '1', '0', 'F', '0', '0', 'company:m2mCompany:admin', '', '1', '2021-07-27 09:08:52', '1', '2021-08-02 09:05:49', '', '51634d802b134c88834cebe9547c599f/ed9087f850894d21bb41044c8b95d415/fa6c148d7bec4c31aa46b7a73d575af3', '60', '000200/000010/000060', '3', 'y', '100', '127.0.0.1', '3', '0');
INSERT INTO `sys_menu` VALUES ('fae936ec1cec465d90fe413cf0508221', 'sysPortalConfigRemove', '多栏目门户配置删除', 'e70d56d0bec74c4aba2105854ce5608f', '10', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortalConfig:remove', '#', '1', '2021-05-10 18:20:28', '1', '2021-05-10 18:20:28', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/e70d56d0bec74c4aba2105854ce5608f/fae936ec1cec465d90fe413cf0508221', '40', '000010/000050/000040/40', '4', 'y', '', '127.0.0.1', '0', '0');
INSERT INTO `sys_menu` VALUES ('fbeaa5ca1cd54c6fbcde247f0be6250e', 'sysPortalConfigExport', '多栏目门户配置查询', 'e70d56d0bec74c4aba2105854ce5608f', '50', '#', '', '1', '0', 'F', '0', '0', 'system:sysPortalConfig:export', '#', '1', '2021-05-10 18:20:28', '1', '2021-05-10 18:20:28', '', '1/d8a45047891d4a28a5a3faf96f2b8d97/e70d56d0bec74c4aba2105854ce5608f/fbeaa5ca1cd54c6fbcde247f0be6250e', '50', '000010/000050/000040/50', '4', 'y', '', '127.0.0.1', '0', '0');

-- ----------------------------
-- Table structure for `sys_notice`
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `notice_content_html` longblob COMMENT '公告内容HTML',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_notice_user_read`
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_user_read`;
CREATE TABLE `sys_notice_user_read` (
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `notice_id` varchar(64) NOT NULL COMMENT '通知公告ID',
  `user_id` varchar(64) NOT NULL COMMENT '系统用户id',
  `is_read` varchar(10) DEFAULT NULL COMMENT '是否已读（0未读，1已读），仅在制定通知接收人时使用',
  `status` char(1) NOT NULL COMMENT '状态',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index2` (`notice_id`,`user_id`,`is_read`,`status`,`del_flag`),
  KEY `index1` (`notice_id`,`user_id`,`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知公告用户阅读表';

-- ----------------------------
-- Records of sys_notice_user_read
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_oper_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` text COMMENT '返回结果集',
  `log_content` text COMMENT '日志变更内容',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `oper_id` bigint(20) DEFAULT NULL COMMENT '任务ID',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `form_id` varchar(64) DEFAULT NULL COMMENT '表单数据ID',
  `take_up_time` int(11) DEFAULT NULL COMMENT '耗时',
  PRIMARY KEY (`id`),
  KEY `idx_formId` (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志记录';

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES ('61136fba-b615-4ba4-81ce-3f210188a1a9', '菜单管理', '3', 'com.aidex.web.controller.system.SysMenuController.remove()', 'DELETE', '1', 'admin', null, '/system/menu/a18e0a829b3a400ea41095d9160b4434', '127.0.0.1', '内网IP', '{menuId=a18e0a829b3a400ea41095d9160b4434}', '{\"msg\":\"操作成功\",\"code\":200}', null, '0', null, '2021-10-05 10:50:24', null, null, '0', null, '43');
INSERT INTO `sys_oper_log` VALUES ('915f6f23-85e1-4ab6-bdc3-faaf86d61345', '菜单管理', '3', 'com.aidex.web.controller.system.SysMenuController.remove()', 'DELETE', '1', 'admin', null, '/system/menu/4f1974f240104888af3f0fad2db62d21', '127.0.0.1', '内网IP', '{menuId=4f1974f240104888af3f0fad2db62d21}', '{\"msg\":\"操作成功\",\"code\":200}', null, '0', null, '2021-10-05 10:50:28', null, null, '0', null, '23');
INSERT INTO `sys_oper_log` VALUES ('9974d119-5e2f-4f74-bf47-fb4f1777cee1', '参数管理', '9', 'com.aidex.web.controller.system.SysConfigController.refreshCache()', 'DELETE', '1', 'admin', null, '/system/config/refreshCache', '127.0.0.1', '内网IP', '{}', '{\"code\":200,\"msg\":\"操作成功\"}', null, '0', null, '2021-10-05 10:48:59', null, null, '0', null, '27');
INSERT INTO `sys_oper_log` VALUES ('b56500d9-e9b8-478b-9eaf-544511301800', '菜单管理', '3', 'com.aidex.web.controller.system.SysMenuController.remove()', 'DELETE', '1', 'admin', null, '/system/menu/8d38416a1b60446a80c53d2f2934fbf9', '127.0.0.1', '内网IP', '{menuId=8d38416a1b60446a80c53d2f2934fbf9}', '{\"msg\":\"操作成功\",\"code\":200}', null, '0', null, '2021-10-05 10:50:15', null, null, '0', null, '23');
INSERT INTO `sys_oper_log` VALUES ('ba5eff53-53da-457b-ae13-dd83fca49cf0', '菜单管理', '3', 'com.aidex.web.controller.system.SysMenuController.remove()', 'DELETE', '1', 'admin', null, '/system/menu/952abc4a55cd44fd9b696883ecf92590', '127.0.0.1', '内网IP', '{menuId=952abc4a55cd44fd9b696883ecf92590}', '{\"msg\":\"操作成功\",\"code\":200}', null, '0', null, '2021-10-05 10:50:13', null, null, '0', null, '23');
INSERT INTO `sys_oper_log` VALUES ('bb946840-ba46-4bc8-b3b8-0c13fa745332', '个人信息', '2', 'com.aidex.web.controller.system.SysProfileController.updatePwd()', 'PUT', '1', 'admin', null, '/system/user/profile/updatePwd', '127.0.0.1', '内网IP', '123admin123 admin123', '{\"msg\":\"操作成功\",\"code\":200}', null, '0', null, '2021-10-05 13:45:52', null, null, '0', null, '488');
INSERT INTO `sys_oper_log` VALUES ('de46873c-e323-48ac-a592-a447bb8a96c5', '菜单管理', '3', 'com.aidex.web.controller.system.SysMenuController.remove()', 'DELETE', '1', 'admin', null, '/system/menu/5883b75c2f64424eb86b06497c6e294c', '127.0.0.1', '内网IP', '{menuId=5883b75c2f64424eb86b06497c6e294c}', '{\"msg\":\"操作成功\",\"code\":200}', null, '0', null, '2021-10-05 10:50:11', null, null, '0', null, '76');
INSERT INTO `sys_oper_log` VALUES ('f5fe36c4-3c7b-4ab4-bc4a-fab690015287', '菜单管理', '3', 'com.aidex.web.controller.system.SysMenuController.remove()', 'DELETE', '1', 'admin', null, '/system/menu/ddcb2a640e4940ad99401928d4742eef', '127.0.0.1', '内网IP', '{menuId=ddcb2a640e4940ad99401928d4742eef}', '{\"msg\":\"操作成功\",\"code\":200}', null, '0', null, '2021-10-05 10:50:17', null, null, '0', null, '26');

-- ----------------------------
-- Table structure for `sys_portal_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_portal_config`;
CREATE TABLE `sys_portal_config` (
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `name` varchar(200) NOT NULL COMMENT '小页名称',
  `code` varchar(64) NOT NULL COMMENT '小页编码',
  `application_range` varchar(20) DEFAULT NULL COMMENT '应用范围(S系统,R角色,U用户)',
  `is_default` varchar(10) DEFAULT NULL COMMENT '是否默认',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '资源ID(角色ID或者用户ID)',
  `system_defined_id` varchar(64) DEFAULT NULL COMMENT '系统定义ID区分系统定义和用户自定义',
  `content` text COMMENT '配置信息',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` char(1) NOT NULL COMMENT '状态',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作台配置表';

-- ----------------------------
-- Records of sys_portal_config
-- ----------------------------
INSERT INTO `sys_portal_config` VALUES ('73c217ba0fb24945a8faef74eb10d302', '工作台', '6c297eb4651940edbb45c87c75be00d7', 'U', 'Y', '1', 'app1', '[{\"id\":\"4ae60dd1debe462096698e1da993317a\",\"x\":0,\"y\":0,\"w\":3,\"h\":262,\"i\":\"4ae60dd1debe462096698e1da993317a\",\"key\":\"kuaijierukou\",\"isShowTitle\":\"N\",\"isAllowDrag\":false,\"name\":\"快捷入口\",\"type\":\"smallPage\",\"url\":\"dashboard/portal/CommonUse\",\"options\":\"{\\\"titleRequired\\\":true,\\\"moreUrl\\\":\\\"\\\",\\\"refresh\\\":1}\",\"moved\":false},{\"id\":\"fd7290c27f644234b38d18faf5d75783\",\"x\":0,\"y\":262,\"w\":3,\"h\":1370,\"i\":\"fd7290c27f644234b38d18faf5d75783\",\"key\":\"todo\",\"isShowTitle\":\"N\",\"isAllowDrag\":true,\"name\":\"待办小页\",\"type\":\"smallPage\",\"url\":\"dashboard/portal/ToDo\",\"options\":\"{\\\"titleRequired\\\":true,\\\"moreUrl\\\":\\\"\\\",\\\"refresh\\\":1}\",\"moved\":false}]', null, '0', '1', '100', '2021-09-01 16:25:30', '1', '2021-09-04 21:44:35', '127.0.0.1', null, '5', '0');

-- ----------------------------
-- Table structure for `sys_portlet`
-- ----------------------------
DROP TABLE IF EXISTS `sys_portlet`;
CREATE TABLE `sys_portlet` (
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `name` varchar(200) NOT NULL COMMENT '小页名称',
  `code` varchar(64) NOT NULL COMMENT '小页编码',
  `url` varchar(200) DEFAULT NULL COMMENT '小页ULRL',
  `refresh_rate` varchar(10) DEFAULT NULL COMMENT '刷新频率',
  `show_title` varchar(10) DEFAULT NULL COMMENT '是否显示标题',
  `is_allow_drag` varchar(10) DEFAULT NULL COMMENT '是否允许拖拽',
  `x_grid_number` varchar(10) DEFAULT NULL COMMENT '横向栅格数',
  `y_grid_number` varchar(10) DEFAULT NULL COMMENT '纵向栅格数',
  `sort` int(11) DEFAULT NULL COMMENT '排序号',
  `status` char(1) NOT NULL COMMENT '状态',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_config_idx1` (`name`,`code`,`del_flag`),
  KEY `table_config_idx` (`name`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作台小页管理表';

-- ----------------------------
-- Records of sys_portlet
-- ----------------------------
INSERT INTO `sys_portlet` VALUES ('48ab0929410c4458b8a0fb1f6e1517a6', '项目金额总览', 'test1', 'demo/portLetCards/AmountStatistics', null, 'N', 'Y', '2', '100', '10', '0', '1', '100', '2021-05-10 17:16:13', '1', '2021-05-24 09:21:04', '127.0.0.1', null, '10', '0');
INSERT INTO `sys_portlet` VALUES ('4ae60dd1debe462096698e1da993317a', '快捷入口', 'kuaijierukou', 'dashboard/portal/CommonUse', null, 'N', 'N', '3', '300', '90', '0', '1', '100', '2021-05-20 16:48:19', '1', '2021-05-27 17:43:55', '127.0.0.1', null, '3', '0');
INSERT INTO `sys_portlet` VALUES ('9460f48c10574f3f8dc41e91a9e7d852', '统计图-饼图', 'test8', 'demo/chart/pie-chart', null, 'N', 'Y', '2', '100', '80', '0', '1', '100', '2021-05-13 17:24:35', '1', '2021-05-18 10:44:30', '127.0.0.1', null, '2', '0');
INSERT INTO `sys_portlet` VALUES ('9e84a390d85045b3b3d50bb71096d5f0', '当月任务完成情况总览', 'test3', 'demo/portLetCards/TaskCompletionStatistics', null, 'N', 'Y', '2', '100', '30', '0', '1', '100', '2021-05-13 16:21:28', '1', '2021-05-19 16:54:09', '127.0.0.1', null, '4', '0');
INSERT INTO `sys_portlet` VALUES ('c42a1e2bc13e4a8ebdc640f137b71afb', '统计图-折线图', 'test7', 'demo/chart/line-chart', null, 'N', 'Y', '2', '100', '70', '0', '1', '100', '2021-05-13 17:23:23', '1', '2021-05-18 10:44:23', '127.0.0.1', null, '4', '0');
INSERT INTO `sys_portlet` VALUES ('ce6b3d01fd44400c9f919ede36b0dfd4', '统计图-面积图', 'test6', 'demo/chart/area-chart', null, 'N', 'Y', '2', '100', '60', '0', '1', '100', '2021-05-13 17:19:13', '1', '2021-05-18 10:44:16', '127.0.0.1', null, '6', '0');
INSERT INTO `sys_portlet` VALUES ('edbe13d789dc46efa17af9e8348cd08a', '访问量', 'test4', 'demo/portLetCards/TrafficStatistics', null, 'N', 'Y', '1', '100', '40', '0', '1', '100', '2021-05-13 16:23:29', '1', '2021-05-18 10:44:01', '127.0.0.1', null, '3', '0');
INSERT INTO `sys_portlet` VALUES ('f2d7ae2c79b748f28c1d1b9a75490887', '仪表盘', 'test5', 'demo/portLetCards/DashboardChart', null, 'N', 'Y', '1', '100', '50', '0', '1', '100', '2021-05-13 16:43:16', '1', '2021-05-18 10:44:06', '127.0.0.1', null, '2', '0');
INSERT INTO `sys_portlet` VALUES ('f3fe47b17c53437cb5c672d29ea432ae', '当月任务总数统计', 'test2', 'demo/portLetCards/TaskTotalStatistics', null, 'N', 'Y', '1', '100', '20', '0', '1', '100', '2021-05-13 16:15:06', '1', '2021-05-18 10:43:51', '127.0.0.1', null, '4', '0');
INSERT INTO `sys_portlet` VALUES ('fd7290c27f644234b38d18faf5d75783', '待办小页', 'todo', 'dashboard/portal/ToDo', null, 'N', 'Y', '3', '300', '100', '0', '1', '100', '2021-05-20 18:00:35', '1', '2021-05-20 18:28:47', '127.0.0.1', null, '4', '0');

-- ----------------------------
-- Table structure for `sys_post`
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES ('1', 'ceo', '董事长', '10', '0', 'admin', null, '2021-01-30 13:27:43', '1', '2021-09-05 10:11:01', '127.0.0.1', '1', '1', '0');
INSERT INTO `sys_post` VALUES ('2', 'se', '项目经理', '20', '0', 'admin', null, '2021-01-30 13:27:43', '1', '2021-09-05 10:10:50', '127.0.0.1', '1', '1', '0');
INSERT INTO `sys_post` VALUES ('3', 'hr', '人力资源', '30', '0', 'admin', null, '2021-01-30 13:27:43', '1', '2021-09-05 10:11:12', '127.0.0.1', '1', '1', '0');
INSERT INTO `sys_post` VALUES ('4', 'user', '普通员工', '40', '0', 'admin', null, '2021-01-30 13:27:43', '1', '2021-09-05 10:11:16', '127.0.0.1', '1', '1', '0');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `sort` int(11) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', 'admin', '1', '1', '1', '1', '0', '0', 'admin', null, '2021-01-30 13:27:43', '1', '2021-07-13 10:16:52', '超级管理员', '127.0.0.1', '3');
INSERT INTO `sys_role` VALUES ('1d65a356f35d455bbb2a3a614737b0f7', '项目管理员', 'projectAdmin', '3', '2', '1', '1', '0', '0', '1', '100', '2021-06-03 18:39:55', '1', '2021-07-13 11:47:03', null, '127.0.0.1', '14');
INSERT INTO `sys_role` VALUES ('2', '系统管理员', 'sysAdmin', '2', '2', '1', '1', '0', '0', 'admin', null, '2021-01-30 13:27:43', '1', '2021-07-13 11:46:35', '普通角色', '127.0.0.1', '7');
INSERT INTO `sys_role` VALUES ('c6676868d24e43098e4724014a096800', '一般用户', 'ordinaryUser', '50', '2', '0', '0', '0', '0', '1', '100', '2021-07-16 14:49:26', '1', '2021-07-17 12:46:50', null, '127.0.0.1', '3');

-- ----------------------------
-- Table structure for `sys_role_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  `dept_id` varchar(64) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色和部门关联表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES ('1d65a356f35d455bbb2a3a614737b0f7', '40f40750917349249f3a7d4593e3e886');
INSERT INTO `sys_role_dept` VALUES ('2', '40f40750917349249f3a7d4593e3e886');

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  `menu_id` varchar(64) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_table_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_table_config`;
CREATE TABLE `sys_table_config` (
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `table_key` varchar(200) NOT NULL COMMENT '表唯一性编码',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `space` varchar(100) DEFAULT NULL COMMENT '间距',
  `columns` varchar(4000) DEFAULT NULL COMMENT '对应列集合JSON',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_config_idx1` (`table_key`,`user_id`,`del_flag`),
  KEY `table_config_idx` (`table_key`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表格个性化配置表';

-- ----------------------------
-- Records of sys_table_config
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_theme_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_theme_config`;
CREATE TABLE `sys_theme_config` (
  `id` varchar(64) NOT NULL COMMENT '岗位ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `theme_obj` varchar(1000) DEFAULT NULL COMMENT '对应主题JSON',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `sort` int(4) NOT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx1` (`user_id`,`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户主题信息记录表';

-- ----------------------------
-- Records of sys_theme_config
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_upload_files`
-- ----------------------------
DROP TABLE IF EXISTS `sys_upload_files`;
CREATE TABLE `sys_upload_files` (
  `id` varchar(64) NOT NULL,
  `upload_code` varchar(100) DEFAULT NULL COMMENT '操作ID',
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名',
  `file_type` varchar(10) DEFAULT NULL COMMENT '文件类型',
  `file_url` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `batch_content` varchar(4000) DEFAULT NULL COMMENT '更新内容',
  `batch_type` varchar(20) DEFAULT NULL COMMENT '操作类型',
  `batch_value` varchar(100) DEFAULT NULL COMMENT '目标值',
  `batch_value_name` varchar(100) DEFAULT NULL COMMENT '目标值中文显示',
  `file_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upload_user` varchar(100) DEFAULT NULL COMMENT '下载用户',
  `dto` varchar(100) DEFAULT NULL COMMENT 'DTO对象',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名称',
  `service_bean` varchar(1000) DEFAULT NULL COMMENT '所属Bean对象',
  `batch_count` int(11) DEFAULT NULL COMMENT '总处理数',
  `success_count` int(11) DEFAULT NULL COMMENT '已处理变更',
  `success_file_url` varchar(100) DEFAULT NULL COMMENT '已处理变更文件路径',
  `fail_count` int(11) DEFAULT NULL COMMENT '失败变更',
  `fail_file_url` varchar(100) DEFAULT NULL COMMENT '失败变更文件路径',
  `progress` varchar(100) DEFAULT NULL COMMENT '进度',
  `options` varchar(1000) DEFAULT NULL COMMENT '扩展选项',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `status` char(100) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='导入记录表';

-- ----------------------------
-- Records of sys_upload_files
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '用户ID',
  `dept_id` varchar(50) DEFAULT NULL COMMENT '部门ID',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `name_en` varchar(50) DEFAULT NULL COMMENT '英文名',
  `no` varchar(50) DEFAULT NULL COMMENT '用户编号',
  `user_name` varchar(30) NOT NULL COMMENT '登陆名称',
  `nick_name` varchar(30) DEFAULT NULL COMMENT '别称',
  `user_type` varchar(2) DEFAULT '0' COMMENT '前后台类型标识 0:后台用户 1:前台用户',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `nation` varchar(100) DEFAULT NULL COMMENT '民族',
  `birth_address` varchar(100) DEFAULT NULL COMMENT '籍贯',
  `polity` varchar(50) DEFAULT NULL COMMENT '政治面貌',
  `title` varchar(100) DEFAULT NULL COMMENT '职称',
  `office_tel` varchar(50) DEFAULT NULL COMMENT '办公电话',
  `fax` varchar(50) DEFAULT NULL COMMENT '传真号',
  `work_space` varchar(100) DEFAULT NULL COMMENT '工作地点',
  `sort` int(11) DEFAULT NULL COMMENT '排序号',
  `user_pinyin` varchar(500) DEFAULT NULL COMMENT '用户姓名全拼和简拼',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_dept` varchar(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_ip` varchar(128) DEFAULT NULL COMMENT '更新IP',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('01085bb3d1174d119d5b35877378a366', '100', 'test', null, 'test', 'test', 'sdfsd', '2', '334444@qq.com', '15155558888', '0', null, '$2a$10$etMMI1O1HTWJ9W8//vuRdOMF2xxbG4rZQkRgAGZDGOtOpHSiGwEGq', null, null, null, null, null, null, null, null, '10', 'test,test', '0', '0', null, null, '1', '100', '2021-04-07 14:41:30', '1', '2021-09-05 10:37:08', '127.0.0.1', null, '10');
INSERT INTO `sys_user` VALUES ('1', '100', '管理员', null, 'admin', 'admin', '管理员', '1', '11223344@qq.com', '15355558888', '0', '/profile\\avatar\\2021/07/28\\fce3dc16-5f76-41d8-8ad7-fa5ade9668ef.jpeg', '$2a$10$pKFKKWAQz5lNS7uLqNZYveEkNSgDQEOThN/0SJ/HmwFeyQfJwhX96', null, null, null, null, null, '029-03456751111', null, null, '1', 'admin,admin', '0', '0', '127.0.0.1', '2021-10-05 13:46:04', 'admin', null, '2021-01-30 13:27:43', '1', '2021-08-17 11:18:23', '10.216.70.100', '管理员', '32');

-- ----------------------------
-- Table structure for `sys_user_post`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `post_id` varchar(64) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES ('01085bb3d1174d119d5b35877378a366', '2');
INSERT INTO `sys_user_post` VALUES ('01085bb3d1174d119d5b35877378a366', '3');
INSERT INTO `sys_user_post` VALUES ('01085bb3d1174d119d5b35877378a366', '4');
INSERT INTO `sys_user_post` VALUES ('1', '1');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `idx` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('01085bb3d1174d119d5b35877378a366', '2');
