DROP TABLE IF EXISTS `base_table`;
CREATE TABLE `base_table` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
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
  `id` varchar(32) NOT NULL COMMENT '部门id',
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
  `id` varchar(50) NOT NULL COMMENT '主键ID',
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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='代码生成业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES ('17', 'gen_config_template', '模板配置表', null, null, 'GenConfigTemplate', 'crud', null, null, null, 'genConfigTemplate', '模板配置', null, null, null, '1', '{\"treeParentCode\":\"parentId\",\"disableEnableOption\":\"0\",\"attachOption\":\"0\",\"treeCode\":\"id\"}', 'admin', '2021-09-22 22:25:48', '', null, null);

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
  `is_new_row` char(10) DEFAULT NULL COMMENT '新行',
  `col_span` int(10) unsigned zerofill DEFAULT '0000000002' COMMENT '列数',
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
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8 COMMENT='代码生成业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

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
INSERT INTO `sys_config` VALUES ('主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2021-01-30 13:27:43', '1', '2021-08-06 11:39:33', '深色主题theme-dark，浅色主题theme-light   ', '3', '5', '0', '127.0.0.1', '1');
INSERT INTO `sys_config` VALUES ('用户是否可注册开关', 'sys.account.registerUser', 'true', 'Y', '1', '2021-09-29 23:51:21', '1', '2021-09-29 23:51:21', '是否开启验证码功能（true开启，false关闭）', 'b3158f15cbed4a3d93e43f19d5fb5893', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_config` VALUES ('验证码开关', 'sys.captcha.onOff', 'true', 'Y', '1', '2021-09-29 23:51:21', '1', '2021-09-29 23:51:21', '是否开启验证码功能（true开启，false关闭）', 'b3158f15cbed4a3d93e43f29d5fb5893', '1', '0', '127.0.0.1', '100');

-- ----------------------------
-- Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` varchar(32) NOT NULL COMMENT '部门id',
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
  `id` varchar(64) NOT NULL COMMENT 'ID',
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
INSERT INTO `sys_dict_data` VALUES ('2', '20', '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '性别女', '2', '1', '0', '1', null);
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
INSERT INTO `sys_dict_data` VALUES ('4', '10', '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '显示菜单', '4', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('5', '20', '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '隐藏菜单', '5', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('6', '10', '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '正常状态', '6', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '200', '开启', '20', 'is_active', null, null, null, '0', '1', '2021-08-12 16:16:16', '1', '2021-08-12 16:16:16', null, '60e4ea6677b54a2ea98310538f597b12', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('7', '20', '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '停用状态', '7', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES ('8', '10', '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2021-01-30 13:27:43', '', null, '正常状态', '8', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '20', '角色', 'R', 'sys_portal_range', null, null, null, '0', '1', '2021-05-11 10:15:48', '1', '2021-05-11 10:15:48', null, '86adc81770e34600ab906d68b927c110', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('9', '20', '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2021-01-30 13:27:43', '', null, '停用状态', '9', '1', '0', null, null);
INSERT INTO `sys_dict_data` VALUES (null, '20', '前台用户', '2', 'sys_user_type', null, null, null, '0', '1', '2021-03-01 11:41:46', '1', '2021-03-01 11:41:46', null, '9f6424fbc8154850ba41076616855d63', '1', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES (null, '10', '机构', 'org', 'sys_dept_type', null, null, null, '0', '1', '2021-02-24 18:35:41', '1', '2021-02-25 15:22:42', null, 'a2c5e3f94ce44007adb448de721bbc49', '5', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES ('28', '110', '校验', '11', 'sys_oper_type', null, null, null, '0', '1', '2021-10-04 22:40:50', '1', '2021-10-04 22:50:02', '校验', 'ab9bbed6-9395-420f-9fa2-c9be42550017', '2', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '20', '公司', 'company', 'sys_dept_type', null, null, null, '0', '1', '2021-02-24 11:45:32', '1', '2021-02-24 14:06:42', null, 'c226f8276bbd41ce9bb53bff9f38e6db', '3', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES (null, '20', '无效', '1', 'sys_is_valid', null, null, null, '0', '1', '2021-05-27 10:10:58', '1', '2021-05-27 10:10:58', null, 'c4f7b956f6074cde80ea2d0520aaae15', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '30', '部门', 'dept', 'sys_dept_type', null, null, null, '0', '1', '2021-03-02 10:14:09', '1', '2021-03-02 10:14:09', null, 'cc8564ca5919410e8ee71145ef652ccd', '1', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_data` VALUES (null, '100', '关闭', '10', 'is_active', null, null, null, '0', '1', '2021-08-12 16:16:05', '1', '2021-08-12 16:16:05', null, 'd6a6dd3180e2497eb34b29c5b29ead15', '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES (null, '30', '用户', 'U', 'sys_portal_range', null, null, null, '0', '1', '2021-05-11 10:16:00', '1', '2021-05-11 10:16:00', null, 'dae2f9bc8046495182fa320b9efd05b2', '1', '0', '127.0.0.1', '100');

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
INSERT INTO `sys_dict_type` VALUES ('7', '通知类型', 'sys_notice_type', '0', 'admin', '2021-01-30 13:27:43', '', null, '通知类型列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('79aaacc0f8424b75b4b2dd1809b698c6', '是否有效', 'sys_is_valid', '0', '1', '2021-05-27 10:10:26', '1', '2021-05-27 10:10:26', null, '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_type` VALUES ('8', '通知状态', 'sys_notice_status', '0', 'admin', '2021-01-30 13:27:43', '', null, '通知状态列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('9', '操作类型', 'sys_oper_type', '0', 'admin', '2021-01-30 13:27:43', '', null, '操作类型列表', '1', '0', null, null);
INSERT INTO `sys_dict_type` VALUES ('a2dce0cac0ce4d539ca0c9f4ee2893b1', '是否启动', 'is_active', '0', '1', '2021-08-12 16:15:39', '1', '2021-08-12 16:15:39', null, '1', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_type` VALUES ('aa0c5c14dca441e49c5d98c620429cda', '用户类型', 'sys_user_type', '0', '1', '2021-03-01 11:41:07', '1', '2021-03-01 11:41:07', null, '1', '0', '127.0.0.1', '103');
INSERT INTO `sys_dict_type` VALUES ('c3c50d4b965640b19a5e73597922ba20', '部门类型', 'sys_dept_type', '0', '1', '2021-02-25 17:11:16', '1', '2021-02-25 17:11:16', '1', '1', '0', '127.0.0.1', '103');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='定时任务调度日志表';

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
INSERT INTO `sys_menu` VALUES ('100', 'user-manager', '用户管理', '8f00f7f7d3b44aaba1041bc8fdf6c470', '1', 'user', 'system/user/SysUserIndex', '1', '0', 'C', '0', '0', 'system:user:list', 'team', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:42:04', '主要维护平台相关用户信息', '8f00f7f7d3b44aaba1041bc8fdf6c470/100', '10', '000010/000010', '2', 'n', '', '127.0.0.1', '18', '0');
INSERT INTO `sys_menu` VALUES ('1001', 'userQuery', '用户查询', '100', '1', '', '', '1', '0', 'F', '0', '0', 'system:user:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:43:32', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/100/1001', '10', '000010/000010/000010', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1002', 'userAdd', '用户新增', '100', '2', '', '', '1', '0', 'F', '0', '0', 'system:user:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:43:42', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/100/1002', '20', '000010/000010/000020', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1003', 'userEdit', '用户修改', '100', '3', '', '', '1', '0', 'F', '0', '0', 'system:user:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:43:56', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/100/1003', '30', '000010/000010/000030', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1004', 'userRemove', '用户删除', '100', '4', '', '', '1', '0', 'F', '0', '0', 'system:user:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:44:06', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/100/1004', '40', '000010/000010/000040', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1005', 'userExport', '用户导出', '100', '5', '', '', '1', '0', 'F', '0', '0', 'system:user:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:44:20', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/100/1005', '50', '000010/000010/000050', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1006', 'userImport', '用户导入', '100', '6', '', '', '1', '0', 'F', '0', '0', 'system:user:import', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:45:01', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/100/1006', '0', '000010/000010/000000', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1007', 'userReetPwd', '重置密码', '100', '7', '', '', '1', '0', 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:44:37', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/100/1007', '0', '000010/000010/000000', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1008', 'roleQuery', '角色查询', '101', '1', '', '', '1', '0', 'F', '0', '0', 'system:role:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:48:38', '', 'ed0919f0a42e4a07993622037df86a33/101/1008', '10', '000020/000010/000010', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1009', 'roleAdd', '角色新增', '101', '2', '', '', '1', '0', 'F', '0', '0', 'system:role:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:48:55', '', 'ed0919f0a42e4a07993622037df86a33/101/1009', '20', '000020/000010/000020', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('101', 'role-manager', '角色管理', 'ed0919f0a42e4a07993622037df86a33', '2', 'role', 'system/role/QueryList', '1', '0', 'C', '0', '0', '', 'contacts', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:49:36', '维护平台各角色数据以及权限分配.', 'ed0919f0a42e4a07993622037df86a33/101', '10', '000020/000010', '2', 'n', '', '127.0.0.1', '17', '0');
INSERT INTO `sys_menu` VALUES ('1010', 'roleEdit', '角色修改', '101', '3', '', '', '1', '0', 'F', '0', '0', 'system:role:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:03', '', 'ed0919f0a42e4a07993622037df86a33/101/1010', '30', '000020/000010/000030', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1011', 'roleRemove', '角色删除', '101', '4', '', '', '1', '0', 'F', '0', '0', 'system:role:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:11', '', 'ed0919f0a42e4a07993622037df86a33/101/1011', '40', '000020/000010/000040', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1012', 'roleExport', '角色导出', '101', '5', '', '', '1', '0', 'F', '0', '0', 'system:role:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:18', '', 'ed0919f0a42e4a07993622037df86a33/101/1012', '50', '000020/000010/000050', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1013', 'menuQuery', '菜单查询', '102', '1', '', '', '1', '0', 'F', '0', '0', 'system:menu:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:37', '', '2ea5441cad6d47679b9029b6ac2d291f/102/1013', '10', '000030/000010/000010', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1014', 'menuAdd', '菜单新增', '102', '2', '', '', '1', '0', 'F', '0', '0', 'system:menu:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:49:45', '', '2ea5441cad6d47679b9029b6ac2d291f/102/1014', '20', '000030/000010/000020', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1015', 'menuEdit', '菜单修改', '102', '3', '', '', '1', '0', 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:50:00', '', '2ea5441cad6d47679b9029b6ac2d291f/102/1015', '30', '000030/000010/000030', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1016', 'menuRemove', '菜单删除', '102', '4', '', '', '1', '0', 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 14:50:08', '', '2ea5441cad6d47679b9029b6ac2d291f/102/1016', '40', '000030/000010/000040', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1017', 'deptQuery', '部门查询', '103', '1', '', '', '1', '0', 'F', '0', '0', 'system:dept:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:43:41', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/103/1017', '30', '000010/000020/000030', '3', 'y', '', '127.0.0.1', '10', '0');
INSERT INTO `sys_menu` VALUES ('1018', 'deptAdd', '部门新增', '103', '2', '', '', '1', '0', 'F', '0', '0', 'system:dept:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:43:51', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/103/1018', '40', '000010/000020/000040', '3', 'y', '', '127.0.0.1', '10', '0');
INSERT INTO `sys_menu` VALUES ('1019', 'deptEdit', '部门修改', '103', '3', '', '', '1', '0', 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:43:20', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/103/1019', '10', '000010/000020/000010', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('102', 'menu-manager', '菜单管理', '2ea5441cad6d47679b9029b6ac2d291f', '3', 'menu', 'system/menu/MenuIndex', '1', '0', 'C', '0', '0', 'system:menu:list', 'bars', 'admin', '2021-01-30 13:27:43', '1', '2021-06-08 10:08:35', '平台所有菜单维护', '2ea5441cad6d47679b9029b6ac2d291f/102', '10', '000030/000010', '2', 'n', '', '127.0.0.1', '14', '0');
INSERT INTO `sys_menu` VALUES ('1020', 'deptRemove', '部门删除', '103', '4', '', '', '1', '0', 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:43:31', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/103/1020', '20', '000010/000020/000020', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1021', 'postQuery', '岗位查询', '104', '1', '', '', '1', '0', 'F', '0', '0', 'system:post:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:44:29', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/104/1021', '10', '000010/000030/000010', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1022', 'postAdd', '岗位新增', '104', '2', '', '', '1', '0', 'F', '0', '0', 'system:post:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:44:40', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/104/1022', '20', '000010/000030/000020', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1023', 'postEdit', '岗位修改', '104', '3', '', '', '1', '0', 'F', '0', '0', 'system:post:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:45:00', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/104/1023', '30', '000010/000030/000030', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1024', 'postRemove', '岗位删除', '104', '4', '', '', '1', '0', 'F', '0', '0', 'system:post:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:45:36', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/104/1024', '40', '000010/000030/000040', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1025', 'postExport', '岗位导出', '104', '5', '', '', '1', '0', 'F', '0', '0', 'system:post:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:45:48', '', '8f00f7f7d3b44aaba1041bc8fdf6c470/104/1025', '50', '000010/000030/000050', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1026', 'dictQuery', '字典查询', '105', '1', '#', '', '1', '0', 'F', '0', '0', 'system:dict:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:11:42', '', '2ea5441cad6d47679b9029b6ac2d291f/105/1026', '10', '000030/000020/000010', '3', 'y', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('1027', 'dictAdd', '字典新增', '105', '2', '#', '', '1', '0', 'F', '0', '0', 'system:dict:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:08', '', '2ea5441cad6d47679b9029b6ac2d291f/105/1027', '20', '000030/000020/000020', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1028', 'dictEdit', '字典修改', '105', '3', '#', '', '1', '0', 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:16', '', '2ea5441cad6d47679b9029b6ac2d291f/105/1028', '30', '000030/000020/000030', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1029', 'dictRemove', '字典删除', '105', '4', '#', '', '1', '0', 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:22', '', '2ea5441cad6d47679b9029b6ac2d291f/105/1029', '40', '000030/000020/000040', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('103', 'dept-manager', '部门管理', '8f00f7f7d3b44aaba1041bc8fdf6c470', '4', 'dept', 'system/dept/SysDeptIndex', '1', '0', 'C', '0', '0', 'system:dept:list', 'cluster', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:42:58', '部门管理菜单', '8f00f7f7d3b44aaba1041bc8fdf6c470/103', '20', '000010/000020', '2', 'n', '', '127.0.0.1', '13', '0');
INSERT INTO `sys_menu` VALUES ('1030', 'dictExport', '字典导出', '105', '5', '#', '', '1', '0', 'F', '0', '0', 'system:dict:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:28', '', '2ea5441cad6d47679b9029b6ac2d291f/105/1030', '50', '000030/000020/000050', '3', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('1031', 'configQuery', '参数查询', '106', '1', '#', '', '1', '0', 'F', '0', '0', 'system:config:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:37', '', '2ea5441cad6d47679b9029b6ac2d291f/106/1031', '10', '000030/000030/000010', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1032', 'configAdd', '参数新增', '106', '2', '#', '', '1', '0', 'F', '0', '0', 'system:config:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:44', '', '2ea5441cad6d47679b9029b6ac2d291f/106/1032', '20', '000030/000030/000020', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1033', 'configEdit', '参数修改', '106', '3', '#', '', '1', '0', 'F', '0', '0', 'system:config:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:50', '', '2ea5441cad6d47679b9029b6ac2d291f/106/1033', '30', '000030/000030/000030', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1034', 'configRemove', '参数删除', '106', '4', '#', '', '1', '0', 'F', '0', '0', 'system:config:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:12:57', '', '2ea5441cad6d47679b9029b6ac2d291f/106/1034', '40', '000030/000030/000040', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1035', 'configExport', '参数导出', '106', '5', '#', '', '1', '0', 'F', '0', '0', 'system:config:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:04', '', '2ea5441cad6d47679b9029b6ac2d291f/106/1035', '50', '000030/000030/000050', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1036', 'notice-query', '公告查询', '107', '1', '#', '', '1', '0', 'F', '0', '0', 'system:notice:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:17', '', 'd8a45047891d4a28a5a3faf96f2b8d97/107/1036', '10', '000090/000010/000010', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1037', 'noticeAdd', '公告新增', '107', '2', '#', '', '1', '0', 'F', '0', '0', 'system:notice:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:23', '', 'd8a45047891d4a28a5a3faf96f2b8d97/107/1037', '20', '000090/000010/000020', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1038', 'noticeEdit', '公告修改', '107', '3', '#', '', '1', '0', 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:29', '', 'd8a45047891d4a28a5a3faf96f2b8d97/107/1038', '30', '000090/000010/000030', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1039', 'noticeRemove', '公告删除', '107', '4', '#', '', '1', '0', 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:13:36', '', 'd8a45047891d4a28a5a3faf96f2b8d97/107/1039', '40', '000090/000010/000040', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('104', 'post-manager', '岗位管理', '8f00f7f7d3b44aaba1041bc8fdf6c470', '5', 'post', 'system/post/PostIndex', '1', '0', 'C', '0', '0', 'system:post:list', 'idcard', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 15:44:09', '岗位管理菜单', '8f00f7f7d3b44aaba1041bc8fdf6c470/104', '30', '000010/000030', '2', 'n', '', '127.0.0.1', '14', '0');
INSERT INTO `sys_menu` VALUES ('1040', 'operateLogQuery', '操作查询', '500', '1', '#', '', '1', '0', 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:05', '', '108/500/1040', '10', '000070/000010/000010', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1041', 'operateLogRemove', '操作删除', '500', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:13', '', '108/500/1041', '20', '000070/000010/000020', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1042', 'operateLoExport', '日志导出', '500', '4', '#', '', '1', '0', 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:20', '', '108/500/1042', '30', '000070/000010/000030', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1043', 'loginLogQuery', '登录查询', '501', '1', '#', '', '1', '0', 'F', '0', '0', 'monitor:loginLog:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:30', '', '108/501/1043', '10', '000070/000020/000010', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1044', 'loginLogRemove', '登录删除', '501', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:loginLog:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:36', '', '108/501/1044', '20', '000070/000020/000020', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1045', 'loginLogExport', '日志导出', '501', '3', '#', '', '1', '0', 'F', '0', '0', 'monitor:loginLog:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:14:43', '', '108/501/1045', '30', '000070/000020/000030', '3', 'y', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('1046', 'onlineQuery', '在线查询', '109', '1', '#', '', '1', '0', 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:15:40', '', '2/109/1046', '10', '000080/000010/000010', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1047', 'onlineBatchLogout', '批量强退', '109', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:15:47', '', '2/109/1047', '20', '000080/000010/000020', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1048', 'onlineLogout', '单条强退', '109', '3', '#', '', '1', '0', 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:15:54', '', '2/109/1048', '30', '000080/000010/000030', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1049', 'jobQuery', '任务查询', '110', '1', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:06', '', '2/110/1049', '10', '000080/000020/000010', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('105', 'dict-manager', '字典管理', '2ea5441cad6d47679b9029b6ac2d291f', '6', 'dict', 'system/dict/DictIndex', '1', '0', 'C', '0', '0', 'system:dict:list', 'read', 'admin', '2021-01-30 13:27:43', '1', '2021-06-08 09:53:17', '字典管理菜单', '2ea5441cad6d47679b9029b6ac2d291f/105', '20', '000030/000020', '2', 'n', '', '127.0.0.1', '12', '0');
INSERT INTO `sys_menu` VALUES ('1050', 'jobAdd', '任务新增', '110', '2', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:17', '', '2/110/1050', '20', '000080/000020/000020', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1051', 'jobEdit', '任务修改', '110', '3', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:25', '', '2/110/1051', '30', '000080/000020/000030', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1052', 'jobRemove', '任务删除', '110', '4', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:32', '', '2/110/1052', '40', '000080/000020/000040', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1053', 'jobChangeStatus', '状态修改', '110', '5', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:42', '', '2/110/1053', '50', '000080/000020/000050', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1054', 'jobExport', '任务导出', '110', '7', '#', '', '1', '0', 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:16:49', '', '2/110/1054', '60', '000080/000020/000060', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1055', 'genQuery', '生成查询', '115', '1', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:08', '', '3/115/1055', '10', '000030/000020/000010', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1056', 'genEdit', '生成修改', '115', '2', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:14', '', '3/115/1056', '20', '000030/000020/000020', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1057', 'genRemove', '生成删除', '115', '3', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:23', '', '3/115/1057', '30', '000030/000020/000030', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1058', 'genImport', '导入代码', '115', '2', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:30', '', '3/115/1058', '40', '000030/000020/000040', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1059', 'genPreview', '预览代码', '115', '4', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:38', '', '3/115/1059', '50', '000030/000020/000050', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('106', 'config-manager', '参数设置', '2ea5441cad6d47679b9029b6ac2d291f', '7', 'config', 'system/config/ConfigIndex', '1', '0', 'C', '0', '0', 'system:config:list', 'code', 'admin', '2021-01-30 13:27:43', '1', '2021-06-08 09:53:33', '参数设置菜单', '2ea5441cad6d47679b9029b6ac2d291f/106', '30', '000030/000030', '2', 'n', '', '127.0.0.1', '12', '0');
INSERT INTO `sys_menu` VALUES ('1060', 'gen-code', '生成代码', '115', '5', '#', '', '1', '0', 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:18:45', '', '3/115/1060', '60', '000030/000020/000060', '3', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('107', 'notice-manager', '通知公告', 'd8a45047891d4a28a5a3faf96f2b8d97', '8', 'notice', 'system/notice/NoticeIndex', '1', '0', 'C', '0', '0', 'system:notice:list', 'notification', 'admin', '2021-01-30 13:27:43', '1', '2021-06-08 10:05:31', '通知公告菜单', 'd8a45047891d4a28a5a3faf96f2b8d97/107', '10', '000090/000010', '2', 'n', '', '127.0.0.1', '11', '0');
INSERT INTO `sys_menu` VALUES ('108', 'system-log', '日志管理', '0', '9', 'log', '', '1', '0', 'M', '0', '0', '', 'copy', 'admin', '2021-01-30 13:27:43', '1', '2021-09-17 17:29:29', '日志管理菜单', '108', '70', '000070', '1', 'n', '', '127.0.0.1', '12', '0');
INSERT INTO `sys_menu` VALUES ('109', 'online-user', '在线用户', '2', '1', 'online', 'monitor/online/index', '1', '0', 'C', '0', '0', 'monitor:online:list', 'dot-chart', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:38:59', '在线用户菜单', '2/109', '10', '000080/000010', '2', 'n', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('110', 'job-manager', '定时任务', '2', '2', 'job', 'monitor/job/index', '1', '0', 'C', '0', '0', 'monitor:job:list', 'bar-chart', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:39:25', '定时任务菜单', '2/110', '20', '000080/000020', '2', 'n', '', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('111', 'druid-monitor', '数据监控', '2', '3', 'druid', 'monitor/druid/index', '1', '0', 'C', '0', '0', 'monitor:druid:list', 'dashboard', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:52:49', '数据监控菜单', '2/111', '30', '000080/000030', '2', 'y', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('112', 'server-monitor', '服务监控', '2', '4', 'server', 'monitor/server/index', '1', '0', 'C', '0', '0', 'monitor:server:list', 'pie-chart', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:41:17', '服务监控菜单', '2/112', '40', '000080/000040', '2', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('113', 'cache-monitor', '缓存监控', '2', '5', 'cache', 'monitor/cache/index', '1', '0', 'C', '0', '0', 'monitor:cache:list', 'box-plot', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:41:35', '缓存监控菜单', '2/113', '50', '000080/000050', '2', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('114', 'form-generate', '表单构建', '3', '1', 'build', 'tool/build/index', '1', '0', 'C', '0', '0', 'tool:build:list', 'bars', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:41:50', '表单构建菜单', '3/114', '10', '000030/000010', '2', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('115', 'code-gen', '代码生成', '3', '2', 'gen', 'tool/gen/index', '1', '0', 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2021-01-30 13:27:43', '1', '2021-03-26 15:17:26', '代码生成菜单', '3/115', '20', '000030/000020', '2', 'n', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('116', 'saggger-manager', '系统接口', '3', '3', 'swagger', 'tool/swagger/index', '1', '0', 'C', '0', '0', 'tool:swagger:list', 'api', 'admin', '2021-01-30 13:27:43', '1', '2021-04-23 10:42:53', '系统接口菜单', '3/116', '30', '000030/000030', '2', 'y', '', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('1f89001d84ba48dcb41abc13cccc67e4', 'auth-center', '菜单授权', 'ed0919f0a42e4a07993622037df86a33', '0', 'sysAuth', 'system/role/SysRoleAuth', '1', '0', 'C', '0', '0', '', 'api', '1', '2021-06-04 13:44:30', '1', '2021-09-13 08:52:05', '', 'ed0919f0a42e4a07993622037df86a33/1f89001d84ba48dcb41abc13cccc67e4', '30', '000020/000030', '2', 'y', '100', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('2', 'system_monitor', '系统监控', '0', '2', 'monitor', '', '1', '0', 'M', '0', '0', '', 'fund', 'admin', '2021-01-30 13:27:43', '1', '2021-09-17 17:31:24', '系统监控目录', '2', '80', '000080', '1', 'n', '', '127.0.0.1', '12', '0');
INSERT INTO `sys_menu` VALUES ('2ea5441cad6d47679b9029b6ac2d291f', 'system-setting', '系统设置', '0', '0', 'sysSetting', '', '1', '0', 'M', '0', '0', '', 'setting', '1', '2021-06-08 09:52:35', '1', '2021-09-17 17:32:25', '', '2ea5441cad6d47679b9029b6ac2d291f', '30', '000030', '1', 'n', '100', '127.0.0.1', '10', '0');
INSERT INTO `sys_menu` VALUES ('3', 'system_tool', '系统工具', '0', '3', 'tool', '', '1', '0', 'M', '0', '0', '', 'appstore', 'admin', '2021-01-30 13:27:43', '1', '2021-09-04 10:34:25', '系统工具目录', '3', '30', '000030', '1', 'n', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('330d1940a6a64dc7adcfa03fc9f57d25', 'simcard_oper_log', '卡操作日志查询', '76262a407245454492ccb8b100976ae9', '0', '', '', '1', '0', 'F', '0', '0', 'm2mSimcardOperLogQuery', '', '1', '2021-08-02 09:05:32', '1', '2021-08-02 09:05:32', '', '2dc9dcc092134fb18d15cb4966100b39/76262a407245454492ccb8b100976ae9/330d1940a6a64dc7adcfa03fc9f57d25', '20', '000210/000030/000020', '3', 'y', '100', '127.0.0.1', '1', '0');
INSERT INTO `sys_menu` VALUES ('500', 'sys-operate-log', '操作日志', '108', '1', 'operlog', 'monitor/operlog/OperlogIndex', '1', '0', 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2021-01-30 13:27:43', '1', '2021-04-02 10:06:43', '操作日志菜单', '108/500', '10', '000070/000010', '2', 'n', '', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('501', 'sys-login-log', '登录日志', '108', '2', 'loginLog', 'monitor/loginlog/LoginLogIndex', '1', '0', 'C', '0', '0', 'monitor:loginLog:list', 'loginLog', 'admin', '2021-01-30 13:27:43', '1', '2021-04-02 10:07:05', '登录日志菜单', '108/501', '20', '000070/000020', '2', 'n', '', '127.0.0.1', '9', '0');
INSERT INTO `sys_menu` VALUES ('56b65ea40f9b4385991383fd766b5c06', 'simcard-query', 'SIM卡查看', '76262a407245454492ccb8b100976ae9', '0', '', '', '1', '0', 'F', '0', '0', 'iot:m2mSimcard:query', '', '1', '2021-08-02 09:04:36', '1', '2021-08-02 09:04:36', '', '2dc9dcc092134fb18d15cb4966100b39/76262a407245454492ccb8b100976ae9/56b65ea40f9b4385991383fd766b5c06', '10', '000210/000030/000010', '3', 'y', '100', '127.0.0.1', '1', '0');
INSERT INTO `sys_menu` VALUES ('652e86f6905d478e924f0641ebe88874', 'simcard-list', '卡列表查询', '76262a407245454492ccb8b100976ae9', '0', '', '', '1', '0', 'F', '0', '0', 'iot:m2mSimcard:list', '', '1', '2021-08-02 13:52:45', '1', '2021-08-02 13:52:45', '', '2dc9dcc092134fb18d15cb4966100b39/76262a407245454492ccb8b100976ae9/652e86f6905d478e924f0641ebe88874', '30', '000210/000030/000030', '3', 'y', '100', '127.0.0.1', '1', '0');
INSERT INTO `sys_menu` VALUES ('67c8d43a175040a5874439d1516308da', 'sys-job-log', '调度日志', '108', '0', 'joblog', 'monitor/job/log', '1', '0', 'C', '0', '0', '', 'bug', '1', '2021-06-18 16:39:15', '1', '2021-06-18 16:42:49', '', '108/67c8d43a175040a5874439d1516308da', '30', '000070/000030', '2', 'y', '100', '127.0.0.1', '5', '0');
INSERT INTO `sys_menu` VALUES ('7e00c054a0d34baea9dbcec52f239d25', 'cache-manager', '缓存列表', '2', '0', 'cacheList', 'monitor/cache/indexCacheList', '1', '0', 'C', '0', '0', '', 'dashboardNew', '1', '2021-08-05 14:51:44', '1', '2021-08-05 14:53:53', null, '2/7e00c054a0d34baea9dbcec52f239d25', '55', '000080/000055', '2', 'y', '100', '127.0.0.1', '2', '0');
INSERT INTO `sys_menu` VALUES ('8f00f7f7d3b44aaba1041bc8fdf6c470', 'org-manager', '组织管理', '0', '0', 'org', '', '1', '0', 'M', '0', '0', '', 'appstore', '1', '2021-06-08 09:31:04', '1', '2021-09-17 17:30:13', '', '8f00f7f7d3b44aaba1041bc8fdf6c470', '10', '000010', '1', 'n', '100', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('c9d88c5c57494bfcac8137ffd0aa3121', 'genconfig-template', '模板配置', '3', '0', 'template', 'tool/gen/genconfigtemplate/index', '1', '0', 'C', '0', '0', '', 'picture', '1', '2021-03-07 12:28:59', '1', '2021-04-23 10:43:18', '', '3/c9d88c5c57494bfcac8137ffd0aa3121', '40', '000030/000040', '2', 'y', '100', '127.0.0.1', '6', '0');
INSERT INTO `sys_menu` VALUES ('d8a45047891d4a28a5a3faf96f2b8d97', 'system-app', '系统应用', '0', '0', 'sysApp', '', '1', '0', 'M', '0', '0', '', 'laptop', '1', '2021-06-08 10:05:01', '1', '2021-09-17 17:29:53', '', 'd8a45047891d4a28a5a3faf96f2b8d97', '90', '000090', '1', 'n', '100', '127.0.0.1', '8', '0');
INSERT INTO `sys_menu` VALUES ('ed0919f0a42e4a07993622037df86a33', 'auth-manager', '权限管理', '0', '0', 'auth', '', '1', '0', 'M', '0', '0', '', 'solution', '1', '2021-06-08 09:49:56', '1', '2021-09-17 17:30:52', '', 'ed0919f0a42e4a07993622037df86a33', '20', '000020', '1', 'n', '100', '127.0.0.1', '7', '0');
INSERT INTO `sys_menu` VALUES ('fa6c148d7bec4c31aa46b7a73d575af3', 'admin-oper', '管理员操作权限', 'ed9087f850894d21bb41044c8b95d415', '0', '', '', '1', '0', 'F', '0', '0', 'company:m2mCompany:admin', '', '1', '2021-07-27 09:08:52', '1', '2021-08-02 09:05:49', '', '51634d802b134c88834cebe9547c599f/ed9087f850894d21bb41044c8b95d415/fa6c148d7bec4c31aa46b7a73d575af3', '60', '000200/000010/000060', '3', 'y', '100', '127.0.0.1', '3', '0');

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
INSERT INTO `sys_portal_config` VALUES ('73c217ba0fb24945a8faef74eb10d302', '首页', '6c297eb4651940edbb45c87c75be00d7', 'U', 'Y', '1', 'app1', '[{\"id\":\"4ae60dd1debe462096698e1da993317a\",\"x\":0,\"y\":0,\"w\":3,\"h\":262,\"i\":\"4ae60dd1debe462096698e1da993317a\",\"key\":\"kuaijierukou\",\"isShowTitle\":\"N\",\"isAllowDrag\":false,\"name\":\"快捷入口\",\"type\":\"smallPage\",\"url\":\"dashboard/portal/CommonUse\",\"options\":\"{\\\"titleRequired\\\":true,\\\"moreUrl\\\":\\\"\\\",\\\"refresh\\\":1}\",\"moved\":false},{\"id\":\"fd7290c27f644234b38d18faf5d75783\",\"x\":0,\"y\":262,\"w\":3,\"h\":1370,\"i\":\"fd7290c27f644234b38d18faf5d75783\",\"key\":\"todo\",\"isShowTitle\":\"N\",\"isAllowDrag\":true,\"name\":\"待办小页\",\"type\":\"smallPage\",\"url\":\"dashboard/portal/ToDo\",\"options\":\"{\\\"titleRequired\\\":true,\\\"moreUrl\\\":\\\"\\\",\\\"refresh\\\":1}\",\"moved\":false}]', null, '0', '1', '100', '2021-09-01 16:25:30', '1', '2021-09-04 21:44:35', '127.0.0.1', null, '5', '0');

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
INSERT INTO `sys_role` VALUES ('1d65a356f35d455bbb2a3a614737b0f7', '项目管理员', 'projectAdmin', '3', '2', '1', '1', '0', '0', '1', '100', '2021-06-03 18:39:55', '1', '2021-09-17 16:46:14', null, '127.0.0.1', '15');
INSERT INTO `sys_role` VALUES ('2', '系统管理员', 'sysAdmin', '2', '2', '1', '1', '0', '0', 'admin', null, '2021-01-30 13:27:43', '1', '2021-09-17 16:45:40', '普通角色', '127.0.0.1', '8');
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
INSERT INTO `sys_user` VALUES ('01085bb3d1174d119d5b35877378a366', '100', 'test', null, 'test', 'test', 'sdfsd', '2', '334444@qq.com', '', '0', null, '$2a$10$etMMI1O1HTWJ9W8//vuRdOMF2xxbG4rZQkRgAGZDGOtOpHSiGwEGq', null, null, null, null, null, null, null, null, '10', 'test,test', '0', '0', null, null, '1', '100', '2021-04-07 14:41:30', '1', '2021-09-05 10:37:08', '127.0.0.1', null, '10');
INSERT INTO `sys_user` VALUES ('1', '100', '管理员', null, 'admin', 'admin', '管理员', '1', '1125373330@qq.com', '1125373330', '0', '/profile\\avatar\\2021/07/28\\fce3dc16-5f76-41d8-8ad7-fa5ade9668ef.jpeg', '$2a$10$Es.n8SRDQ3F.RP4K7K5tk./z61StuD6ZTmdD7BaI4Q9J9xIvZQFD.', null, null, null, null, null, '029-03456751111', null, null, '1', 'admin,admin', '0', '0', '127.0.0.1', '2021-01-30 13:27:43', 'admin', null, '2021-01-30 13:27:43', '1', '2021-09-17 15:06:26', '127.0.0.1', '管理员', '33');

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
INSERT INTO `sys_user_post` VALUES ('c90b3091bc824efaae1dd43845b866fd', '1');

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
INSERT INTO `sys_user_role` VALUES ('c90b3091bc824efaae1dd43845b866fd', '2');