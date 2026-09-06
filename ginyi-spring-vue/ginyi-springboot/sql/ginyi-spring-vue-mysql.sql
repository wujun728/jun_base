/*
 Navicat Premium Data Transfer

 Source Server         : MySQL8.0
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : ginyi-spring-vue

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 16/09/2023 15:27:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2022-12-03 07:21:33', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2022-12-03 07:21:33', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2022-12-03 07:21:33', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2022-12-03 07:21:33', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-12-03 07:21:33', '', NULL, '是否开启注册用户功能（true开启，false关闭）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', 'Ginyi', '2023-05-28 20:39:45', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', 'Ginyi', '2023-02-17 22:32:20', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-03 07:21:32', 'Ginyi', '2023-05-28 18:01:25', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 123 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2022-12-03 07:21:33', '', '2022-12-07 08:22:56', '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2022-12-03 07:21:33', '', '2022-12-07 08:22:56', '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2022-12-03 07:21:33', '', '2022-12-07 08:22:56', '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由name，用于跳转',
  `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2039 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 'system', 1, 'system', NULL, '', 1, 0, '0', 'M', '0', '0', '', 'AccessibilitySharp', 'admin', '2022-12-03 07:21:33', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 'sysmonitor', 2, 'sysmonitor', NULL, '', 1, 0, '0', 'M', '0', '0', '', 'PersonOutline', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-02-23 21:14:26', '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 'systools', 3, 'systools', NULL, '', 1, 0, '0', 'M', '0', '0', '', 'BuildSharp', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-02-26 17:01:21', '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 'user', 1, 'user', 'pages/system/user/index', '', 1, 0, '0', 'C', '0', '0', 'system:user:list', 'Duplicate', 'admin', '2022-12-03 07:21:33', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 'role', 2, 'role', 'pages/system/role/index', '', 1, 0, '0', 'C', '0', '0', 'system:role:list', 'FishSharp', 'admin', '2022-12-03 07:21:33', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 'menu', 3, 'menu', 'pages/system/menu/index', '', 1, 0, '0', 'C', '0', '0', 'system:menu:list', 'Heart', 'admin', '2022-12-03 07:21:33', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 'department', 4, 'department', 'pages/system/department/index', '', 1, 0, '0', 'C', '0', '0', 'system:dept:list', 'LayersSharp', 'admin', '2022-12-03 07:21:33', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 'position', 5, 'position', 'pages/system/position/index', '', 1, 0, '0', 'C', '0', '0', 'system:post:list', 'PawSharp', 'admin', '2022-12-03 07:21:33', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 'dict', 6, 'dict', 'pages/system/dict/index', '', 1, 0, '0', 'C', '0', '0', 'system:dict:list', 'LogoElectron', 'admin', '2022-12-03 07:21:33', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 'params', 7, 'params', 'pages/system/params/index', '', 1, 0, '0', 'C', '0', '0', 'system:config:list', 'LogoWechat', 'admin', '2022-12-03 07:21:33', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 'notice', 8, 'notice', 'pages/system/notice/index', '', 1, 0, '0', 'C', '0', '0', 'system:notice:list', 'MailUnreadOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 'log', 9, 'log', '', '', 1, 0, '0', 'M', '0', '0', '', 'Mic', 'admin', '2022-12-03 07:21:33', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 'online', 1, 'online', 'pages/monitor/online/index', '', 1, 0, '0', 'C', '0', '0', 'monitor:online:list', 'Disc', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-02-23 21:14:13', '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 'task', 2, 'task', 'pages/monitor/task/index', '', 1, 0, '0', 'C', '0', '0', 'monitor:job:list', 'ExtensionPuzzleSharp', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-02-26 16:59:34', '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 'data', 3, 'data', 'pages/monitor/data/index', '', 1, 0, '0', 'C', '0', '0', 'monitor:druid:list', 'FilterCircle', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-06-30 14:02:06', '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 'service', 4, 'service', 'pages/monitor/service/index', '', 1, 0, '0', 'C', '0', '0', 'monitor:server:list', 'FootballSharp', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-06-30 14:02:07', '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 'cache', 5, 'cache', 'pages/monitor/cache/index', '', 1, 0, '0', 'C', '0', '0', 'monitor:cache:list', 'Leaf', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-06-30 14:02:08', '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 'cacheList', 6, 'cacheList', 'pages/monitor/cacheList/index', '', 1, 0, '0', 'C', '0', '0', 'monitor:cache:list', 'LogoOctocat', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-06-30 17:39:42', '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 'code', 2, 'code', 'pages/systools/code/index', '', 1, 0, '0', 'C', '0', '0', 'tool:gen:list', 'Mail', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-02-26 17:05:52', '代码生成菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 'operationLog', 1, 'operationLog', 'pages/system/log/operation/index', '', 1, 0, '0', 'C', '0', '0', 'monitor:operlog:list', 'Notifications', 'admin', '2022-12-03 07:21:33', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 'loginLog', 2, 'loginLog', 'pages/system/log/login/index', '', 1, 0, '0', 'C', '0', '0', 'monitor:loginlog:list', 'Planet', 'admin', '2022-12-03 07:21:33', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, NULL, 1, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:user:list', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, NULL, 2, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:user:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, NULL, 3, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:user:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, NULL, 4, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:user:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, NULL, 5, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:user:export', 'PersonOutline', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-02-19 15:38:58', '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, NULL, 6, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:user:import', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, NULL, 7, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:user:resetPwd', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, NULL, 1, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:role:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, NULL, 2, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:role:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, NULL, 3, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:role:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, NULL, 4, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:role:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, NULL, 5, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:role:export', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, NULL, 1, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:menu:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, NULL, 2, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:menu:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, NULL, 3, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:menu:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, NULL, 4, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:menu:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, NULL, 1, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:dept:list', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, NULL, 2, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:dept:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, NULL, 3, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:dept:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, NULL, 4, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:dept:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, NULL, 1, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:post:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, NULL, 2, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:post:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, NULL, 3, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:post:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, NULL, 4, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:post:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, NULL, 5, '', '', '', 1, 0, '0', 'F', '1', '0', 'system:post:export', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, NULL, 1, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:dict:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, NULL, 2, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:dict:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, NULL, 3, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:dict:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, NULL, 4, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:dict:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, NULL, 5, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:dict:export', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, NULL, 1, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:config:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, NULL, 2, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:config:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, NULL, 3, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:config:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, NULL, 4, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:config:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, NULL, 5, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:config:export', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, NULL, 1, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:notice:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, NULL, 2, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:notice:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, NULL, 3, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:notice:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, NULL, 4, '#', '', '', 1, 0, '0', 'F', '1', '0', 'system:notice:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, NULL, 1, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:operlog:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, NULL, 2, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:operlog:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, NULL, 3, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:operlog:export', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, NULL, 1, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:loginlog:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, NULL, 2, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:loginlog:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, NULL, 3, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:loginlog:export', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, NULL, 4, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:loginlog:unlock', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, NULL, 1, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:online:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, NULL, 2, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:online:batchLogout', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, NULL, 3, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:online:forceLogout', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, NULL, 1, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:job:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, NULL, 2, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:job:add', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, NULL, 3, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:job:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, NULL, 4, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:job:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, NULL, 5, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:job:changeStatus', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, NULL, 6, '#', '', '', 1, 0, '0', 'F', '1', '0', 'monitor:job:export', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, NULL, 1, '#', '', '', 1, 0, '0', 'F', '1', '0', 'tool:gen:query', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, NULL, 2, '#', '', '', 1, 0, '0', 'F', '1', '0', 'tool:gen:edit', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, NULL, 3, '#', '', '', 1, 0, '0', 'F', '1', '0', 'tool:gen:remove', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, NULL, 4, '#', '', '', 1, 0, '0', 'F', '1', '0', 'tool:gen:import', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, NULL, 5, '#', '', '', 1, 0, '0', 'F', '1', '0', 'tool:gen:preview', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, NULL, 6, '#', '', '', 1, 0, '0', 'F', '1', '0', 'tool:gen:code', 'PersonOutline', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2031, '订单管理', 0, 'order', 4, 'order', '', NULL, 1, 0, '0', 'M', '0', '0', '', 'BarChartSharp', 'Ginyi', '2023-02-28 21:19:38', 'Ginyi', '2023-02-28 21:20:28', '');
INSERT INTO `sys_menu` VALUES (2032, '订单列表', 2031, 'orderList', 1, 'orderList', 'pages/order/index', NULL, 1, 0, '0', 'C', '0', '0', 'order:list', 'Card', 'Ginyi', '2023-02-28 21:22:53', 'Ginyi', '2023-02-28 21:28:47', '');
INSERT INTO `sys_menu` VALUES (2033, '商品管理', 0, 'product', 5, 'product', NULL, NULL, 1, 0, '0', 'M', '0', '0', NULL, 'Cart', 'Ginyi', '2023-02-28 21:35:33', 'Ginyi', '2023-05-28 20:39:38', '');
INSERT INTO `sys_menu` VALUES (2034, '商品列表', 2033, 'productList', 1, 'productList', 'pages/product/index', NULL, 1, 0, '0', 'C', '0', '0', 'product:list', 'Documents', 'Ginyi', '2023-02-28 21:37:50', 'Ginyi', '2023-05-28 18:01:16', '');
INSERT INTO `sys_menu` VALUES (2035, '查看缓存详情', 114, NULL, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'monitor:cache:details', '#', 'Ginyi', '2023-06-30 17:35:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2036, '删除缓存', 114, NULL, 2, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'monitor:cache:remove', '#', 'Ginyi', '2023-06-30 17:37:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2037, '已阅公告', 107, NULL, 5, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'system:notice:haveRead', '#', 'Ginyi', '2023-07-09 00:31:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2038, '公告查询（管理员）', 107, NULL, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'system:notice:query:admin', '#', 'Ginyi', '2023-07-09 00:32:29', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（0通知 1公告）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告内容',
  `user_ids` json NOT NULL COMMENT '通知的用户',
  `user_read_ids` json NULL COMMENT '已阅的用户',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', '新版本内容', '[1, 2]', '[1, 2]', '0', 'admin', '2022-12-03 07:21:33', '', NULL, NULL);
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', '维护内容', '[1, 2]', '[2]', '0', 'admin', '2022-12-03 07:21:33', '', NULL, NULL);
INSERT INTO `sys_notice` VALUES (14, '测试标题', '1', '测试标题内容', '[1]', NULL, '0', 'admin', '2023-07-02 18:42:14', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '');
INSERT INTO `sys_post` VALUES (9, 'test', '测试测试', 5, '0', '0', 'ginyi', '2022-12-31 18:23:34', 'Ginyi', '2023-06-28 22:04:13', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 113 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-12-03 07:21:33', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '系统管理员', 'system', 1, '2', 1, 1, '0', '0', 'admin', '2022-12-03 07:21:33', 'Ginyi', '2023-07-09 00:40:25', '专门管理系统的');
INSERT INTO `sys_role` VALUES (111, '订单管理员', 'order', 0, '1', 1, 1, '0', '0', 'Ginyi', '2023-02-26 16:05:39', 'Ginyi', '2023-02-28 21:33:53', '专门管理订单的');
INSERT INTO `sys_role` VALUES (112, '商品管理员', 'conduct', 2, '1', 1, 1, '0', '0', 'Ginyi', '2023-02-26 16:06:28', 'Ginyi', '2023-05-28 20:39:27', '专门管理商品的');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

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
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` json NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, '[1, 100, 101, 102, 103, 104, 105, 106, 107, 108, 500, 501, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 2, 109, 1046, 1047, 1048, 110, 1049, 1050, 1051, 1052, 1053, 1054, 111, 112, 113, 114, 3, 116, 1055, 1056, 1057, 1058, 1059, 1060, 2035, 2036, 2037, 2038]');
INSERT INTO `sys_role_menu` VALUES (111, '[2031, 2032]');
INSERT INTO `sys_role_menu` VALUES (112, '[2033, 2034]');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phone_number` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$0zqV6SsWmLkjBroBlzgnVO/mNRNTjbJrrCbUhRPOKZa9vyLnyJAR6', '0', '0', '192.168.0.101', '2023-07-09 01:06:17', 'admin', '2022-12-03 07:21:32', 'Ginyi', '2023-07-09 01:06:17', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'Ginyi', '(´･_･`)', '00', 'Ginyi@aliyun.com', '15666666666', '1', '', '$2a$10$0zqV6SsWmLkjBroBlzgnVO/mNRNTjbJrrCbUhRPOKZa9vyLnyJAR6', '0', '0', '192.168.0.103', '2023-09-16 15:14:49', 'admin', '2022-12-03 07:21:32', 'Ginyi', '2023-09-16 15:14:49', '测试员');
INSERT INTO `sys_user` VALUES (133, 101, 'order', '订单管理员', '00', '', '13800138000', '0', '', '$2a$10$0zqV6SsWmLkjBroBlzgnVO/mNRNTjbJrrCbUhRPOKZa9vyLnyJAR6', '0', '0', '127.0.0.1', '2023-03-02 23:06:23', 'ginyi', '2022-12-23 07:40:55', 'Ginyi', '2023-03-02 23:06:23', NULL);
INSERT INTO `sys_user` VALUES (134, 104, 'product', '商品管理员', '00', '', '13800138000', '0', '', '$2a$10$0zqV6SsWmLkjBroBlzgnVO/mNRNTjbJrrCbUhRPOKZa9vyLnyJAR6', '0', '0', '127.0.0.1', '2023-05-27 22:38:58', 'ginyi', '2022-12-23 07:41:02', 'Ginyi', '2023-05-28 17:04:05', NULL);
INSERT INTO `sys_user` VALUES (138, 106, 'tony', '测试用户3', '00', '', '13800138000', '0', '', '', '0', '0', '', NULL, 'ginyi', '2022-12-23 07:41:28', 'admin', '2023-03-03 21:50:14', '没有初始化密码，不能登录');
INSERT INTO `sys_user` VALUES (139, 109, 'lucy', '测试用户4', '00', '', '13800138000', '0', '', '', '1', '0', '', NULL, 'ginyi', '2022-12-23 13:31:22', 'Ginyi', '2023-07-09 13:20:26', '没有初始化密码，不能登录');
INSERT INTO `sys_user` VALUES (140, 101, 'carry', '测试用户5', '00', '', '', '0', '', '', '0', '0', '', NULL, 'ginyi', '2022-12-23 21:35:13', 'Ginyi', '2023-05-28 20:35:40', '没有初始化密码，不能登录');
INSERT INTO `sys_user` VALUES (145, 109, 'test6', '测试用户6', '00', '', '', '0', '', '', '0', '0', '', NULL, 'Ginyi', '2023-02-26 16:40:51', 'Ginyi', '2023-07-09 13:20:24', '没有初始化密码，不能登录');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` json NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, '[1]');
INSERT INTO `sys_user_post` VALUES (2, '[2]');
INSERT INTO `sys_user_post` VALUES (133, '[4]');
INSERT INTO `sys_user_post` VALUES (134, '[4]');
INSERT INTO `sys_user_post` VALUES (138, '[2, 4]');
INSERT INTO `sys_user_post` VALUES (139, '[1, 2]');
INSERT INTO `sys_user_post` VALUES (140, '[2, 9]');
INSERT INTO `sys_user_post` VALUES (145, '[9]');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` json NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, '[1]');
INSERT INTO `sys_user_role` VALUES (2, '[2]');
INSERT INTO `sys_user_role` VALUES (133, '[111, 112]');
INSERT INTO `sys_user_role` VALUES (134, '[112]');
INSERT INTO `sys_user_role` VALUES (138, '[111]');
INSERT INTO `sys_user_role` VALUES (139, '[112]');
INSERT INTO `sys_user_role` VALUES (140, '[111]');
INSERT INTO `sys_user_role` VALUES (145, '[112]');

SET FOREIGN_KEY_CHECKS = 1;
