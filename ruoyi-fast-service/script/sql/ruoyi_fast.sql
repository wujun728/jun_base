SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL COMMENT '编号',
  `table_id` bigint(20) NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` bigint(20) NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-06-13 16:06:37', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-06-13 16:06:37', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-light', 'Y', 'admin', '2024-06-13 16:06:37', 'admin', '2024-07-16 11:25:33', '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2024-06-13 16:06:37', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2024-06-13 16:06:37', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (11, 'OSS预览列表资源开关', 'sys.oss.previewListResource', 'true', 'Y', 'admin', '2024-06-13 16:06:37', '', NULL, 'true:开启, false:关闭');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门状态（1正常 0停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（1代表存在 0代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '1', '0', 'admin', '2024-06-13 16:06:25', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0停用 1正常）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '1', 'sys_show_hide', '', 'primary', 'Y', '1', 'admin', '2024-06-13 16:06:36', 'admin', '2024-07-10 16:34:54', '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '0', 'sys_show_hide', '', 'danger', 'N', '1', 'admin', '2024-06-13 16:06:36', 'admin', '2024-07-10 16:35:07', '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '1', 'sys_normal_disable', '', 'primary', 'Y', '1', 'admin', '2024-06-13 16:06:36', 'admin', '2024-07-10 14:30:58', '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '0', 'sys_normal_disable', '', 'danger', 'N', '1', 'admin', '2024-06-13 16:06:36', 'admin', '2024-07-10 14:31:06', '停用状态');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '1', 'sys_notice_status', '', 'primary', 'Y', '1', 'admin', '2024-06-13 16:06:36', 'admin', '2024-07-10 17:24:35', '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '0', 'sys_notice_status', '', 'danger', 'N', '1', 'admin', '2024-06-13 16:06:36', 'admin', '2024-07-10 17:24:44', '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '1', 'admin', '2024-06-13 16:06:37', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '1', 'admin', '2024-06-13 16:06:37', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '1', 'admin', '2024-06-13 16:06:37', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '1', 'admin', '2024-06-13 16:06:37', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '1', 'admin', '2024-06-13 16:06:37', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '1', 'admin', '2024-06-13 16:06:37', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '1', 'admin', '2024-06-13 16:06:37', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (27, 1, '失败', '0', 'sys_common_status', '', 'danger', 'N', '1', 'admin', '2024-06-13 16:06:37', 'admin', '2024-07-15 10:50:52', '正常状态');
INSERT INTO `sys_dict_data` VALUES (28, 2, '成功', '1', 'sys_common_status', '', 'success', 'N', '1', 'admin', '2024-06-13 16:06:37', 'admin', '2024-07-15 10:51:05', '停用状态');
INSERT INTO `sys_dict_data` VALUES (29, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '其他操作');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（1正常 0停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '1', 'admin', '2024-06-13 16:06:35', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '1', 'admin', '2024-06-13 16:06:35', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '1', 'admin', '2024-06-13 16:06:35', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '1', 'admin', '2024-06-13 16:06:35', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '1', 'admin', '2024-06-13 16:06:35', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '1', 'admin', '2024-06-13 16:06:35', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '1', 'admin', '2024-06-13 16:06:35', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '1', 'admin', '2024-06-13 16:06:36', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(20) NOT NULL COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（1成功 0失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status`) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (1966403042944090114, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', '1', '登录成功', '2025-09-12 15:26:34');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(1) NULL DEFAULT 1 COMMENT '是否为外链（1是 0否）',
  `is_cache` int(1) NULL DEFAULT 0 COMMENT '是否缓存（1缓存 0不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '显示状态（1显示 0隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（1正常 0停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 11, 'system', NULL, '', 0, 0, 'M', '1', '1', '', 'system', 'admin', '2024-06-13 16:06:26', 'admin', '2024-07-04 14:58:14', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 12, 'monitor', NULL, '', 0, 0, 'M', '1', '1', '', 'monitor', 'admin', '2024-06-13 16:06:26', 'admin', '2024-07-04 14:58:29', '系统监控目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 0, 0, 'C', '1', '1', 'system:user:list', 'user', 'admin', '2024-06-13 16:06:26', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 0, 0, 'C', '1', '1', 'system:role:list', 'peoples', 'admin', '2024-06-13 16:06:26', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 0, 0, 'C', '1', '1', 'system:menu:list', 'tree-table', 'admin', '2024-06-13 16:06:26', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 0, 0, 'C', '1', '1', 'system:dept:list', 'tree', 'admin', '2024-06-13 16:06:26', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 0, 0, 'C', '1', '1', 'system:post:list', 'post', 'admin', '2024-06-13 16:06:26', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 0, 0, 'C', '1', '1', 'system:dict:list', 'dict', 'admin', '2024-06-13 16:06:26', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 0, 0, 'C', '1', '1', 'system:config:list', 'edit', 'admin', '2024-06-13 16:06:26', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 0, 0, 'C', '1', '1', 'system:notice:list', 'message', 'admin', '2024-06-13 16:06:26', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 0, 14, 'log', '', '', 0, 0, 'M', '1', '1', '', 'log', 'admin', '2024-06-13 16:06:27', 'admin', '2024-07-11 14:35:49', '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 0, 0, 'C', '1', '1', 'monitor:online:list', 'online', 'admin', '2024-06-13 16:06:27', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (112, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', 0, 0, 'C', '1', '1', 'monitor:cache:list', 'redis-list', 'admin', '2024-06-13 16:06:27', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', 0, 0, 'C', '1', '1', 'monitor:cache:list', 'redis', 'admin', '2024-06-13 16:06:27', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (115, '代码生成', 0, 13, 'gen', 'tool/gen/index', '', 0, 0, 'C', '1', '1', 'tool:gen:list', 'code', 'admin', '2024-06-13 16:06:27', 'admin', '2024-07-05 11:00:03', '代码生成菜单');
INSERT INTO `sys_menu` VALUES (118, '文件管理', 1, 10, 'oss', 'system/oss/index', '', 0, 0, 'C', '1', '1', 'system:oss:list', 'upload', 'admin', '2024-06-13 16:06:27', '', NULL, '文件管理菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', 0, 0, 'C', '1', '1', 'monitor:operlog:list', 'form', 'admin', '2024-06-13 16:06:27', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 0, 0, 'C', '1', '1', 'monitor:logininfor:list', 'logininfor', 'admin', '2024-06-13 16:06:27', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', '', 0, 0, 'F', '1', '1', 'system:user:query', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', '', 0, 0, 'F', '1', '1', 'system:user:add', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', '', 0, 0, 'F', '1', '1', 'system:user:edit', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', '', 0, 0, 'F', '1', '1', 'system:user:remove', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', '', 0, 0, 'F', '1', '1', 'system:user:export', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', '', 0, 0, 'F', '1', '1', 'system:user:import', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', '', 0, 0, 'F', '1', '1', 'system:user:resetPwd', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', '', 0, 0, 'F', '1', '1', 'system:role:query', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', '', 0, 0, 'F', '1', '1', 'system:role:add', '#', 'admin', '2024-06-13 16:06:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', '', 0, 0, 'F', '1', '1', 'system:role:edit', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', '', 0, 0, 'F', '1', '1', 'system:role:remove', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', '', 0, 0, 'F', '1', '1', 'system:role:export', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', '', 0, 0, 'F', '1', '1', 'system:menu:query', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', '', 0, 0, 'F', '1', '1', 'system:menu:add', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', '', 0, 0, 'F', '1', '1', 'system:menu:edit', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', '', 0, 0, 'F', '1', '1', 'system:menu:remove', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门查询', 103, 1, '', '', '', 0, 0, 'F', '1', '1', 'system:dept:query', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门新增', 103, 2, '', '', '', 0, 0, 'F', '1', '1', 'system:dept:add', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门修改', 103, 3, '', '', '', 0, 0, 'F', '1', '1', 'system:dept:edit', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '部门删除', 103, 4, '', '', '', 0, 0, 'F', '1', '1', 'system:dept:remove', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位查询', 104, 1, '', '', '', 0, 0, 'F', '1', '1', 'system:post:query', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位新增', 104, 2, '', '', '', 0, 0, 'F', '1', '1', 'system:post:add', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位修改', 104, 3, '', '', '', 0, 0, 'F', '1', '1', 'system:post:edit', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位删除', 104, 4, '', '', '', 0, 0, 'F', '1', '1', 'system:post:remove', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '岗位导出', 104, 5, '', '', '', 0, 0, 'F', '1', '1', 'system:post:export', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典查询', 105, 1, '#', '', '', 0, 0, 'F', '1', '1', 'system:dict:query', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典新增', 105, 2, '#', '', '', 0, 0, 'F', '1', '1', 'system:dict:add', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典修改', 105, 3, '#', '', '', 0, 0, 'F', '1', '1', 'system:dict:edit', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典删除', 105, 4, '#', '', '', 0, 0, 'F', '1', '1', 'system:dict:remove', '#', 'admin', '2024-06-13 16:06:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '字典导出', 105, 5, '#', '', '', 0, 0, 'F', '1', '1', 'system:dict:export', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数查询', 106, 1, '#', '', '', 0, 0, 'F', '1', '1', 'system:config:query', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数新增', 106, 2, '#', '', '', 0, 0, 'F', '1', '1', 'system:config:add', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数修改', 106, 3, '#', '', '', 0, 0, 'F', '1', '1', 'system:config:edit', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数删除', 106, 4, '#', '', '', 0, 0, 'F', '1', '1', 'system:config:remove', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '参数导出', 106, 5, '#', '', '', 0, 0, 'F', '1', '1', 'system:config:export', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告查询', 107, 1, '#', '', '', 0, 0, 'F', '1', '1', 'system:notice:query', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告新增', 107, 2, '#', '', '', 0, 0, 'F', '1', '1', 'system:notice:add', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告修改', 107, 3, '#', '', '', 0, 0, 'F', '1', '1', 'system:notice:edit', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '公告删除', 107, 4, '#', '', '', 0, 0, 'F', '1', '1', 'system:notice:remove', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作查询', 500, 1, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:operlog:query', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '操作删除', 500, 2, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:operlog:remove', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '日志导出', 500, 4, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:operlog:export', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录查询', 501, 1, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:logininfor:query', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '登录删除', 501, 2, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:logininfor:remove', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '日志导出', 501, 3, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:logininfor:export', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:online:query', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:online:batchLogout', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:online:forceLogout', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '账户解锁', 501, 4, '#', '', '', 0, 0, 'F', '1', '1', 'monitor:logininfor:unlock', '#', 'admin', '2024-06-13 16:06:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 115, 1, '#', '', '', 0, 0, 'F', '1', '1', 'tool:gen:query', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 115, 2, '#', '', '', 0, 0, 'F', '1', '1', 'tool:gen:edit', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 115, 3, '#', '', '', 0, 0, 'F', '1', '1', 'tool:gen:remove', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 115, 2, '#', '', '', 0, 0, 'F', '1', '1', 'tool:gen:import', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 115, 4, '#', '', '', 0, 0, 'F', '1', '1', 'tool:gen:preview', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 115, 5, '#', '', '', 0, 0, 'F', '1', '1', 'tool:gen:code', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1600, '文件查询', 118, 1, '#', '', '', 0, 0, 'F', '1', '1', 'system:oss:query', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1601, '文件上传', 118, 2, '#', '', '', 0, 0, 'F', '1', '1', 'system:oss:upload', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1602, '文件下载', 118, 3, '#', '', '', 0, 0, 'F', '1', '1', 'system:oss:download', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1603, '文件删除', 118, 4, '#', '', '', 0, 0, 'F', '1', '1', 'system:oss:remove', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1604, '配置添加', 118, 5, '#', '', '', 0, 0, 'F', '1', '1', 'system:oss:add', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1605, '配置编辑', 118, 6, '#', '', '', 0, 0, 'F', '1', '1', 'system:oss:edit', '#', 'admin', '2024-06-13 16:06:30', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` bigint(20) NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '公告状态（1正常 0关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '1', 'admin', '2024-06-13 16:06:38', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '1', 'admin', '2024-06-13 16:06:38', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NULL DEFAULT 0 COMMENT '操作状态（0异常 1正常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type`) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status`) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1820003099041587202, '操作日志', 9, 'com.ruoyi.system.controller.monitor.SysOperlogController.clean()', 'DELETE', 1, 'admin', '研发部门', '/monitor/operlog/clean', '127.0.0.1', '内网IP', '{}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 1, '', '2024-08-04 15:45:27');
INSERT INTO `sys_oper_log` VALUES (1820003134722531330, '登录日志', 9, 'com.ruoyi.system.controller.monitor.SysLogininforController.clean()', 'DELETE', 1, 'admin', '研发部门', '/monitor/logininfor/clean', '127.0.0.1', '内网IP', '{}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 1, '', '2024-08-04 15:45:36');
INSERT INTO `sys_oper_log` VALUES (1820004236931096578, '代码生成', 3, 'com.ruoyi.generator.controller.GenController.remove()', 'DELETE', 1, 'admin', '研发部门', '/tool/gen/1809075479210795009,1809075484260737026,1813054136052756481,1814196401338703873,1814223445313273858,1814223448240898049,1814223501781188609,1815297702604587009,1815297705465102337,1818122618072825858', '127.0.0.1', '内网IP', '{}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 1, '', '2024-08-04 15:49:59');
INSERT INTO `sys_oper_log` VALUES (1820004262881255425, '代码生成', 3, 'com.ruoyi.generator.controller.GenController.remove()', 'DELETE', 1, 'admin', '研发部门', '/tool/gen/1818840710792933377,1818840712697147393', '127.0.0.1', '内网IP', '{}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 1, '', '2024-08-04 15:50:05');
INSERT INTO `sys_oper_log` VALUES (1820019896939700226, '用户管理', 1, 'com.ruoyi.system.controller.system.SysUserController.add()', 'POST', 1, 'admin', '研发部门', '/system/user', '127.0.0.1', '内网IP', '{\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"userId\":\"1820019896092450818\",\"deptId\":100,\"userName\":\"test01\",\"nickName\":\"test01\",\"userType\":null,\"email\":null,\"phonenumber\":null,\"sex\":null,\"avatar\":null,\"status\":\"1\",\"delFlag\":null,\"loginIp\":null,\"loginDate\":null,\"remark\":null,\"dept\":null,\"roles\":null,\"roleIds\":[],\"postIds\":[],\"roleId\":null,\"admin\":false}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 1, '', '2024-08-04 16:52:12');

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `oss_id` bigint(20) NOT NULL COMMENT '对象存储主键',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原名',
  `file_suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件后缀名',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '上传人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新人',
  `service` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'minio' COMMENT '服务商',
  PRIMARY KEY (`oss_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OSS对象存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oss_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss_config`;
CREATE TABLE `sys_oss_config`  (
  `oss_config_id` bigint(20) NOT NULL COMMENT '主建',
  `config_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置key',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'accessKey',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '秘钥',
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '桶名称',
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '前缀',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '访问站点',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '自定义域名',
  `is_https` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '域',
  `access_policy` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '桶权限类型(0=private 1=public 2=custom)',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否默认（1=是,0=否）',
  `ext1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '扩展字段',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`oss_config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对象存储配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss_config
-- ----------------------------
INSERT INTO `sys_oss_config` VALUES (1, 'minio', 'ruoyi', 'ruoyi123', 'ruoyi', '', '127.0.0.1:9000', '', 'N', '', '1', '1', '', 'admin', '2024-06-13 16:06:38', 'admin', '2024-07-10 17:51:27', NULL);
INSERT INTO `sys_oss_config` VALUES (2, 'qiniu', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi', '', 's3-cn-north-1.qiniucs.com', '', 'N', '', '1', '0', '', 'admin', '2024-06-13 16:06:38', 'admin', '2024-06-13 16:06:38', NULL);
INSERT INTO `sys_oss_config` VALUES (3, 'aliyun', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi', '', 'oss-cn-beijing.aliyuncs.com', '', 'N', '', '1', '0', '', 'admin', '2024-06-13 16:06:38', 'admin', '2024-07-10 17:50:41', NULL);
INSERT INTO `sys_oss_config` VALUES (4, 'qcloud', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi-1250000000', '', 'cos.ap-beijing.myqcloud.com', '', 'N', 'ap-beijing', '1', '0', '', 'admin', '2024-06-13 16:06:38', 'admin', '2024-06-13 16:06:38', NULL);
INSERT INTO `sys_oss_config` VALUES (5, 'image', 'ruoyi', 'ruoyi123', 'ruoyi', 'image', '127.0.0.1:9000', '', 'N', '', '1', '0', '', 'admin', '2024-06-13 16:06:38', 'admin', '2024-06-13 16:06:38', NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（1正常 0停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '1', 'admin', '2024-06-13 16:06:25', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '1', 'admin', '2024-06-13 16:06:25', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '1', 'admin', '2024-06-13 16:06:25', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '1', 'admin', '2024-06-13 16:06:25', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（1正常 0停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（1代表存在 0代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '1', '0', 'admin', '2024-06-13 16:06:26', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '1', '0', 'admin', '2024-06-13 16:06:26', 'admin', '2024-07-10 17:13:05', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'sys_user' COMMENT '用户类型（sys_user系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '帐号状态（1正常 0停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '字节大叔', 'sys_user', 'crazyLionLi@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '1', '0', '127.0.0.1', '2025-09-12 15:26:34', 'admin', '2024-06-13 16:06:25', 'admin', '2025-09-12 15:26:34', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'lionli', '测试员', 'sys_user', '*******@qq.com', '15666666666', '1', '', '$2a$10$AkMsjUie8i3XN/NzflqmLO3KEQFiDWrT6tj8Ul/Xc30wD9zfRQAFG', '1', '0', '127.0.0.1', '2024-06-13 16:06:25', 'admin', '2024-06-13 16:06:25', 'admin', '2024-07-10 17:37:16', '测试员');
INSERT INTO `sys_user` VALUES (1820019896092450818, 100, 'test01', 'test01', 'sys_user', '', '', '0', '', '$2a$10$MKTcKwjtB11PH9fVsqZTgeaqwFiNjRa6M2Rur.kMIl/BvNH046fm.', '1', '0', '', NULL, 'admin', '2024-08-04 16:52:12', 'admin', '2024-08-04 16:52:12', NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
