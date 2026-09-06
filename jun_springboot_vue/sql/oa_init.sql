SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oa_admin
-- ----------------------------
DROP TABLE IF EXISTS `oa_admin`;
CREATE TABLE `oa_admin`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `userid` varchar(100) NOT NULL DEFAULT '' COMMENT '企业微信userid',
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '登录用户名',
  `pwd` varchar(100) NOT NULL DEFAULT '' COMMENT '登录密码',
  `salt` varchar(100) NOT NULL DEFAULT '' COMMENT '密码盐',
  `reg_pwd` varchar(100) NOT NULL DEFAULT '' COMMENT '初始密码',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '员工姓名',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '电子邮箱',
  `mobile` bigint(11) NOT NULL DEFAULT 0 COMMENT '手机号码',
  `sex` int(1) NOT NULL DEFAULT 0 COMMENT '性别:1男,2女',
  `nickname` varchar(100) NOT NULL DEFAULT '' COMMENT '别名',
  `thumb` varchar(255) NOT NULL COMMENT '头像',
  `theme` varchar(50) NOT NULL DEFAULT 'white' COMMENT '系统主题',
  `did` int(11) NOT NULL DEFAULT 0 COMMENT '主部门id',
  `pid` int(11) NOT NULL DEFAULT 0 COMMENT '上级主管id',
  `position_id` int(11) NOT NULL DEFAULT 0 COMMENT '职位id',
  `position_name` int(11) NOT NULL DEFAULT 0 COMMENT '职务',
  `position_rank` int(11) NOT NULL DEFAULT 0 COMMENT '职级',
  `type` int(1) NOT NULL DEFAULT 0 COMMENT '员工类型:0未设置,1正式,2试用,3实习',
  `is_staff` int(1) NOT NULL DEFAULT 1 COMMENT '身份类型:1企业员工,2劳务派遣,3兼职员工',
  `job_number` varchar(255) NOT NULL DEFAULT '' COMMENT '工号',
  `birthday` varchar(255) NOT NULL DEFAULT '' COMMENT '生日',
  `age` int(11) NOT NULL DEFAULT 0 COMMENT '年龄',
  `work_date` varchar(255) NOT NULL DEFAULT '' COMMENT '开始工作时间',
  `work_location` int(11) NOT NULL DEFAULT 0 COMMENT '工作地点',
  `native_place` varchar(255) NOT NULL DEFAULT '' COMMENT '籍贯',
  `nation` varchar(255) NOT NULL DEFAULT '' COMMENT '民族',
  `home_address` varchar(255) NOT NULL DEFAULT '' COMMENT '家庭地址',
  `current_address` varchar(255) NOT NULL DEFAULT '' COMMENT '现居地址',
  `contact` varchar(255) NOT NULL DEFAULT '' COMMENT '紧急联系人',
  `contact_mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '紧急联系人电话',
  `resident_type` int(1) NOT NULL DEFAULT 0 COMMENT '户口性质:1农村户口,2城镇户口',
  `resident_place` varchar(255) NOT NULL DEFAULT '' COMMENT '户口所在地',
  `graduate_school` varchar(255) NOT NULL DEFAULT '' COMMENT '毕业学校',
  `graduate_day` varchar(255) NOT NULL DEFAULT '' COMMENT '毕业日期',
  `political` int(1) NOT NULL DEFAULT 1 COMMENT '政治面貌:1中共党员,2团员',
  `marital_status` int(1) NOT NULL DEFAULT 1 COMMENT '婚姻状况:1未婚,2已婚,3离异',
  `idcard` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证',  
  `education` varchar(255) NOT NULL DEFAULT '' COMMENT '学位',
  `speciality` varchar(255) NOT NULL DEFAULT '' COMMENT '专业',
  `social_account` varchar(255) NOT NULL DEFAULT '' COMMENT '社保账号',
  `medical_account` varchar(255) NOT NULL DEFAULT '' COMMENT '医保账号',
  `provident_account` varchar(255) NOT NULL DEFAULT '' COMMENT '公积金账号',
  `bank_account` varchar(255) NOT NULL DEFAULT '' COMMENT '银行卡号',
  `bank_info` varchar(255) NOT NULL DEFAULT '' COMMENT '开户行',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '档案附件',
  `desc` mediumtext  NULL COMMENT '员工个人简介',
  `is_hide` int(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏联系方式:0否,1是',
  `entry_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '员工入职日期',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '注册时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新信息时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `last_login_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '最后登录时间',
  `login_num` int(11) NOT NULL DEFAULT 0 COMMENT '登录次数',
  `last_login_ip` varchar(64) NOT NULL DEFAULT '' COMMENT '最后登录IP',
  `is_lock` int(1) NOT NULL DEFAULT 0 COMMENT '是否锁屏:1是0否',
  `auth_did` int(2) NOT NULL DEFAULT 0 COMMENT '数据权限类型:0仅自己关联的数据,1所属主部门的数据,2所属次部门的数据,3所属主次部门的数据,4所属主部门及其子部门数据,5所属次部门及其子部门数据,6所属主次部门及其子部门数据,7所属主部门所在顶级部门及其子部门数据,8所属次部门所在顶级部门及其子部门数据,9所属主次部门所在顶级部门及其子部门数据,10所有部门数据',
  `auth_dids` varchar(500) NOT NULL DEFAULT '' COMMENT '可见部门数据',
  `son_dids` varchar(500) NOT NULL DEFAULT '' COMMENT '可见子部门数据',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态：-1待入职,0禁止登录,1正常,2离职',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工表';

-- ----------------------------
-- Table structure for oa_admin_profiles
-- ----------------------------
DROP TABLE IF EXISTS `oa_admin_profiles`;
CREATE TABLE `oa_admin_profiles`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '员工ID',
  `types` int(1) NOT NULL DEFAULT 0 COMMENT '类型:1教育经历/2工作经历/3相关证书/4计算机技能/5语言能力',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '院校/培训机构/公司名称/证书名称/技能名称/语言名称/',
  `start_time` varchar(255) NOT NULL DEFAULT '' COMMENT '开始时间',
  `end_time` varchar(255) NOT NULL DEFAULT '' COMMENT '结束时间',
  `speciality` varchar(50) NOT NULL DEFAULT '' COMMENT '所学专业',
  `education` varchar(50) NOT NULL DEFAULT '' COMMENT '所获学历',
  `authority` varchar(50) NOT NULL DEFAULT '' COMMENT '颁发机构',
  `position` varchar(50) NOT NULL DEFAULT '' COMMENT '职位',
  `know` int(11) NOT NULL DEFAULT 0 COMMENT '熟悉程度',
  `remark` mediumtext  NULL COMMENT '备注说明',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工档案表';

-- ----------------------------
-- Table structure for oa_labor_contract
-- ----------------------------
DROP TABLE IF EXISTS `oa_labor_contract`;
CREATE TABLE `oa_labor_contract`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `renewal_pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '续签母合同',
  `change_pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '变更母合同',
  `uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '员工ID',
  `cate` int(1) NOT NULL DEFAULT 1 COMMENT '合同类别:1劳动合同,2劳务合同,3保密协议',
  `types` int(1) NOT NULL DEFAULT 1 COMMENT '合同类型:1新签合同,2续签合同,2变更合同',
  `enterprise_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联企业主体ID',
  `properties` int(1) NOT NULL DEFAULT 1 COMMENT '合同属性:1初级职称,2中级职称,3高级职称',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '合同编号',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '合同名称',
  `sign_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '签订时间',
  `start_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '生效时间',
  `end_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '失效时间',
  `secure_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '解除时间',
  `trial_months` int(5) NOT NULL DEFAULT 0 COMMENT '试用月数',
  `trial_end_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '试用结束时间',
  `trial_salary` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '试用工资',
  `worker_salary` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '转正工资',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '合同状态:1正常,2已到期,3已解除',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件',
  `remark` mediumtext  NULL COMMENT '备注说明',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人ID',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工合同表';

-- ----------------------------
-- Table structure for oa_admin_log
-- ----------------------------
DROP TABLE IF EXISTS `oa_admin_log`;
CREATE TABLE `oa_admin_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `type` varchar(80) NOT NULL DEFAULT '' COMMENT '操作类型',
  `action` varchar(80) NOT NULL DEFAULT '' COMMENT '操作动作',
  `subject` varchar(80) NOT NULL DEFAULT '' COMMENT '操作主体',
  `module` varchar(32) NOT NULL DEFAULT '' COMMENT '模块',
  `controller` varchar(32) NOT NULL DEFAULT '' COMMENT '控制器',
  `function` varchar(32) NOT NULL DEFAULT '' COMMENT '方法',
  `ip` varchar(64) NOT NULL DEFAULT '' COMMENT '登录ip',
  `param_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作数据id',
  `param` mediumtext  NULL COMMENT '参数json格式',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工操作日志表';


-- ----------------------------
-- Table structure for oa_admin_log_count
-- ----------------------------
DROP TABLE IF EXISTS `oa_admin_log_count`;
CREATE TABLE `oa_admin_log_count`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int(4) NOT NULL COMMENT '年份',
  `date` date NOT NULL COMMENT '日期',
  `num` int(11) NOT NULL DEFAULT 1 COMMENT '统计数',
  `create_time` bigint(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '员工操作日志统计表';

-- ----------------------------
-- Table structure for oa_admin_module
-- ----------------------------
DROP TABLE IF EXISTS `oa_admin_module`;
CREATE TABLE `oa_admin_module`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '模块名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '模块目录，唯一，字母',
  `desc` varchar(500) NOT NULL DEFAULT '' COMMENT '模块功能描述',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '状态:1系统模块,2普通模块,3自定义模块',
  `sourse` int(2) NOT NULL DEFAULT 1 COMMENT '来源:1官方,2第三方',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:0禁用,1正常',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '功能模块表';

-- ----------------------------
-- Records of oa_admin_module
-- ----------------------------
INSERT INTO `oa_admin_module` VALUES (1, '系统模块', 'home','', 1, 1,1, 1639562910, 0);
INSERT INTO `oa_admin_module` VALUES (2, '人事模块', 'user','', 1, 1,1, 1639562910, 0);
INSERT INTO `oa_admin_module` VALUES (3, '行政模块', 'adm','', 1, 1,1, 1639562910, 0);
INSERT INTO `oa_admin_module` VALUES (4, '办公模块', 'office','', 1, 1,1, 1639562910, 0);
INSERT INTO `oa_admin_module` VALUES (5, '财务模块', 'finance','', 1, 1,1, 1639562910, 0);
INSERT INTO `oa_admin_module` VALUES (6, '客户模块', 'customer','', 2, 1,1, 1639562910, 0);
INSERT INTO `oa_admin_module` VALUES (7, '合同模块', 'contract','', 2, 1,1, 1656142368, 0);
INSERT INTO `oa_admin_module` VALUES (8, '项目模块', 'project','', 2, 1,1, 1656142368, 0);
INSERT INTO `oa_admin_module` VALUES (9, '网盘模块', 'disk','', 2, 1,1, 1656143065, 0);

-- ----------------------------
-- Table structure for oa_admin_rule
-- ----------------------------
DROP TABLE IF EXISTS `oa_admin_rule`;
CREATE TABLE `oa_admin_rule`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父id',
  `src` varchar(255) NOT NULL DEFAULT '' COMMENT 'url链接',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '日志操作名称',
  `module` varchar(255) NOT NULL DEFAULT '' COMMENT '所属模块',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `menu` int(1) NOT NULL DEFAULT 0 COMMENT '是否是菜单,1是,2不是',
  `sort` int(11) NOT NULL DEFAULT 1 COMMENT '越小越靠前',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态,0禁用,1正常',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '菜单及权限表';

-- ----------------------------
-- Records of oa_admin_rule
-- ----------------------------
INSERT INTO `oa_admin_rule` VALUES (1, 0, '', '系统管理', '系统管理', 'home', 'icon-jichupeizhi', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (2, 0, '', '基础数据', '基础数据', 'base', 'icon-hetongshezhi', 1, 2, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (3, 0, '', '人事管理', '人事管理', 'user', 'icon-renshishezhi', 1, 3, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (4, 0, '', '行政办公', '行政办公', 'adm', 'icon-banjiguanli', 1, 4, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (5, 0, '', '个人办公', '个人办公', 'office', 'icon-kaoshijihua', 1, 5, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (6, 0, '', '财务管理', '财务管理', 'finance', 'icon-yuangongtidian', 1, 6, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (7, 0, '', '客户管理', '客户管理', 'customer', 'icon-kehuguanli', 1, 7, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (8, 0, '', '合同管理', '合同管理', 'contract', 'icon-hetongyidong', 1, 8, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (9, 0, '', '项目管理', '项目管理', 'project', 'icon-xiangmuguanli', 1, 9, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (10, 0, '', '知识网盘', '知识网盘', 'disk', 'icon-tikuguanli', 1, 10, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (11, 1, 'home/conf/index', '系统配置', '系统配置', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (12, 11, 'home/conf/add', '新建/编辑', '配置项', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (13, 11, 'home/conf/delete', '删除', '配置项', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (14, 11, 'home/conf/edit', '编辑', '配置详情', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (15, 1, 'home/module/index', '功能模块', '功能模块', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (16, 15, 'home/module/add', '新建/编辑', '功能模块', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (17, 15, 'home/module/del', '删除', '功能模块', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (18, 15, 'home/module/recovery', '恢复', '功能模块', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (19, 15, 'home/module/install', '安装', '功能模块', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (20, 1, 'home/dataauth/index', '模块配置', '模块配置', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (21, 20, 'home/dataauth/edit', '编辑', '模块配置', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (22, 1, 'home/rule/index', '功能节点', '功能节点', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (23, 22, 'home/rule/add', '新建/编辑', '功能节点', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (24, 22, 'home/rule/delete', '删除', '功能节点', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (25, 1, 'mobile/bar/datalist', '移动端配置', '移动端配置', 'home', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (26, 25, 'mobile/bar/datalist', 'BAR菜单', 'BAR菜单', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (27, 26, 'mobile/bar/add', '新建/编辑', 'BAR菜单', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (28, 26, 'mobile/bar/set', '设置', 'BAR菜单', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (29, 25, 'mobile/types/datalist', '工作台菜单类型', '工作台菜单类型', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (30, 29, 'mobile/types/add', '新建/编辑', '工作台菜单类型', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (31, 29, 'mobile/types/del', '删除', '工作台菜单类型', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (32, 29, 'mobile/types/set', '设置', '工作台菜单类型', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (33, 25, 'mobile/menu/datalist', '工作台菜单', '工作台菜单', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (34, 33, 'mobile/menu/add', '新建/编辑', '工作台菜单', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (35, 33, 'mobile/menu/del', '删除', '工作台菜单', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (36, 33, 'mobile/menu/set', '设置', '工作台菜单', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (37, 1, 'home/role/index', '角色权限', '角色权限', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (38, 37, 'home/role/add', '新建/编辑', '角色权限', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (39, 37, 'home/role/delete', '删除', '角色权限', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (40, 1, 'home/log/index', '操作日志', '操作日志', 'home', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (41, 1, 'home/files/index', '附件管理','附件管理', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (42, 41, 'home/files/edit', '编辑附件','附件', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (43, 41, 'home/files/move', '移动附件','附件', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (44, 41, 'home/files/delete', '删除附件','附件', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (45, 41, 'home/files/get_group', '附件分组','附件分组', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (46, 41, 'home/files/add_group', '新建/编辑','附件分组', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (47, 41, 'home/files/del_group', '删除附件分组','附件分组', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (48, 1, 'home/database/database', '备份数据', '数据备份', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (49, 48, 'home/database/backup', '备份数据表', '数据', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (50, 48, 'home/database/optimize', '优化数据表', '数据表', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (51, 48, 'home/database/repair', '修复数据表', '数据表', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (52, 1, 'home/database/backuplist', '还原数据', '数据还原', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (53, 52, 'home/database/import', '还原数据表', '数据', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (54, 52, 'home/database/downfile', '下载备份数据', '备份数据', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (55, 52, 'home/database/del', '删除备份数据', '备份数据', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (56, 1, 'home/task/index', '定时任务', '定时任务', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (57, 56, 'home/task/add', '新建/编辑', '定时任务', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (58, 56, 'home/task/delete', '删除', '定时任务', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (59, 2, '', '公共模块', '公共模块', 'home', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (60, 59, 'home/template/datalist', '消息模板', '消息模板', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (61, 60, 'home/template/add', '新建/编辑', '消息模板', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (62, 60, 'home/template/set', '设置', '消息模板', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (63, 60, 'home/template/view', '查看', '消息模板', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (64, 59, 'adm/flow/modulelist', '审批模块', '审批模块', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (65, 64, 'adm/flow/module_add', '新建/编辑', '审批模块', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (66, 64, 'adm/flow/module_check', '设置', '审批模块', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (67, 59, 'adm/flow/catelist', '审批类型', '审批类型', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (68, 67, 'adm/flow/cate_add', '新建/编辑', '审批类型', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (69, 67, 'adm/flow/cate_check', '设置', '审批类型', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (70, 59, 'adm/flow/datalist', '审批流程', '审批流程', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (71, 70, 'adm/flow/add', '新建/编辑', '审批流程', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (72, 70, 'adm/flow/del', '删除', '审批流程', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (73, 70, 'adm/flow/check', '设置', '审批流程', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (74, 59, 'home/cate/enterprise', '企业主体', '企业主体', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (75, 74, 'home/cate/enterprise_add', '新建/编辑', '企业主体', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (76, 74, 'home/cate/enterprise_check', '设置', '企业主体', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (77, 59, 'home/area/datalist', '全国省市', '全国省市', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (78, 77, 'home/area/add', '新建/编辑', '全国省市', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (79, 77, 'home/area/set', '设置', '全国省市', 'home', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (80, 2, '', '人事模块', '人事模块', 'user', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (81, 80, 'user/rewardscate/datalist', '奖罚项目', '奖罚项目', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (82, 81, 'user/rewardscate/add', '新建/编辑', '奖罚项目', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (83, 81, 'user/rewardscate/set', '设置', '奖罚项目', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (84, 80, 'user/carecate/datalist', '关怀项目', '关怀项目', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (85, 84, 'user/carecate/add', '新建/编辑', '关怀项目', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (86, 84, 'user/carecate/set', '设置', '关怀项目', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (87, 80, 'user/basic/datalist', '常规数据', '常规数据', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (88, 87, 'user/basic/add', '新建/编辑', '常规数据', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (89, 87, 'user/basic/set', '设置', '常规数据', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (90, 3, 'user/department/index', '部门架构', '部门', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (91, 90, 'user/department/add', '新建/编辑', '部门', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (92, 90, 'user/department/delete', '删除', '部门', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (93, 3, 'user/position/index', '岗位职称', '岗位职称', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (94, 93, 'user/position/add', '新建/编辑', '岗位职称', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (95, 93, 'user/position/delete', '删除', '岗位职称', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (96, 93, 'user/position/view', '查看', '岗位职称', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (97, 3, 'user/user/index', '企业员工', '员工', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (98, 97, 'user/user/add', '新建/编辑', '员工', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (99, 97, 'user/user/view', '查看', '员工信息', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (100, 97, 'user/user/set', '设置', '员工状态', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (101, 97, 'user/user/reset_psw', '重设密码', '员工密码', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (102, 97, 'user/user/del', '删除', '员工', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (103, 3, 'user/files/datalist', '员工档案', '员工档案', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (104, 103, 'user/files/add', '编辑', '员工档案', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (105, 103, 'user/files/view', '查看', '员工档案', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (106, 3, 'user/personal/change', '人事调动', '人事调动', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (107, 106, 'user/personal/change_add', '新建/编辑', '人事调动', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (108, 3, 'user/personal/leave', '离职申请', '离职申请', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (109, 108, 'user/personal/leave_add', '新建/编辑', '离职申请', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (110, 108, 'user/personal/leave_delete', '删除', '离职申请', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (111, 108, 'user/personal/leave_view', '查看', '离职申请', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (112, 3, 'user/rewards/datalist', '奖罚管理', '奖罚管理', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (113, 112, 'user/rewards/add', '新建/编辑', '奖罚管理', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (114, 112, 'user/rewards/view', '查看', '奖罚管理', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (115, 112, 'user/rewards/del', '删除', '奖罚管理', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (116, 3, 'user/care/datalist', '员工关怀', '员工关怀', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (117, 116, 'user/care/add', '新建/编辑', '员工关怀', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (118, 116, 'user/care/view', '查看', '员工关怀', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (119, 116, 'user/care/del', '删除', '员工关怀', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (120, 3, 'user/laborcontract/datalist', '员工合同', '员工合同', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (121, 120, 'user/laborcontract/add', '新建/编辑', '员工合同', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (122, 120, 'user/laborcontract/add_renewal', '续签', '员工合同', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (123, 120, 'user/laborcontract/add_change', '变更', '员工合同', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (124, 120, 'user/laborcontract/view', '查看', '员工合同', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (125, 120, 'user/laborcontract/del', '删除', '员工合同', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (126, 120, 'user/laborcontract/set', '设置', '员工合同', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (127, 2, '', '行政模块', '行政模块', 'adm', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (128, 127, 'adm/propertycate/datalist', '资产分类', '资产分类', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (129, 128, 'adm/propertycate/add', '新建/编辑', '资产分类', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (130, 128, 'adm/propertycate/del', '删除', '资产分类', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (131, 127, 'adm/propertybrand/datalist', '资产品牌', '资产品牌', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (132, 131, 'adm/propertybrand/add', '新建/编辑', '资产品牌', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (133, 131, 'adm/propertybrand/check', '设置', '资产品牌', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (134, 127, 'adm/propertyunit/datalist', '资产单位', '资产单位', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (135, 134, 'adm/propertyunit/add', '新建/编辑', '资产单位', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (136, 134, 'adm/propertyunit/check', '设置', '资产单位', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (137, 127, 'adm/sealcate/datalist', '印章管理', '印章', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (138, 137, 'adm/sealcate/add', '新建/编辑', '印章', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (139, 137, 'adm/sealcate/check', '设置', '印章', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (140, 127, 'adm/basic/datalist', '常规数据', '常规数据', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (141, 140, 'adm/basic/add', '新建/编辑', '常规数据', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (142, 140, 'adm/basic/set', '设置', '常规数据', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (143, 4, '', '固定资产', '固定资产', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (144, 143, 'adm/property/datalist', '资产信息', '固定资产', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (145, 144, 'adm/property/add', '新建/编辑', '固定资产', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (146, 144, 'adm/property/check', '设置', '固定资产', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (147, 144, 'adm/property/view', '查看', '固定资产', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (148, 143, 'adm/property/repair_list', '报修记录', '资产报修记录', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (149, 148, 'adm/property/repair_add', '新建/编辑', '资产报修记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (150, 148, 'adm/property/repair_view', '查看', '资产报修记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (151, 148, 'adm/property/repair_del', '删除', '资产报修记录', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (152, 4, '', '车辆管理', '车辆', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (153, 152, 'adm/car/datalist', '车辆信息', '车辆', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (154, 153, 'adm/car/add', '新建/编辑', '车辆', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (155, 153, 'adm/car/check', '设置', '车辆', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (156, 153, 'adm/car/view', '查看', '车辆', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (157, 152, 'adm/car/repair_list', '车辆维修', '车辆维修记录', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (158, 157, 'adm/car/repair_add', '新建/编辑', '车辆维修记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (159, 157, 'adm/car/repair_view', '查看', '车辆维修记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (160, 157, 'adm/car/repair_del', '删除', '车辆维修记录', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (161, 152, 'adm/car/protect_list', '车辆保养', '车辆保养记录', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (162, 161, 'adm/car/protect_add', '新建/编辑', '车辆保养记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (163, 161, 'adm/car/protect_view', '查看', '车辆保养记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (164, 161, 'adm/car/protect_del', '删除', '车辆保养记录', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (165, 152, 'adm/car/mileage_list', '车辆里程', '车辆里程记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (166, 165, 'adm/car/mileage_add', '新建/编辑', '车辆里程记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (167, 165, 'adm/car/mileage_del', '删除', '车辆里程记录', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (168, 152, 'adm/car/fee_list', '车辆费用', '车辆费用记录', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (169, 168, 'adm/car/fee_add', '新建/编辑', '车辆费用记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (170, 168, 'adm/car/fee_view', '查看', '车辆费用记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (171, 168, 'adm/car/fee_del', '删除', '车辆费用记录', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (172, 4, '', '会议管理', '会议管理', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (173, 172, 'adm/meeting/room', '会议室管理', '会议室', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (174, 173, 'adm/meeting/room_add', '新建/编辑', '会议室', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (175, 173, 'adm/meeting/room_view', '查看', '会议纪要', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (176, 173, 'adm/meeting/room_check', '设置', '会议室', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (177, 172, 'adm/meeting/records', '会议记录', '会议记录', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (178, 177, 'adm/meeting/records_add', '新建/编辑', '会议记录', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (179, 177, 'adm/meeting/records_view', '查看', '会议纪要', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (180, 177, 'adm/meeting/records_del', '删除', '会议纪要', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (181, 4, '', '公文管理', '公文管理', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (182, 181, 'adm/official/datalist', '公文列表', '公文管理', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (183, 182, 'adm/official/add', '新建/编辑', '公文管理', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (184, 182, 'adm/official/view', '查看', '公文管理', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (185, 182, 'adm/official/del', '删除', '公文管理', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (186, 181, 'adm/official/pending', '待审公文', '公文管理', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (187, 181, 'adm/official/reviewed', '已审公文', '公文管理', 'adm', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (188, 4, '', '用章管理', '用章管理', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (189, 188, 'adm/seal/datalist', '用章申请', '用章申请', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (190, 189, 'adm/seal/add', '新建/编辑', '用章申请', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (191, 189, 'adm/seal/view', '查看', '用章申请', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (192, 189, 'adm/seal/del', '删除', '用章申请', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (193, 188, 'adm/seal/record', '用章记录', '用章记录', 'adm', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (194, 127, 'adm/notecate/datalist', '公告类型', '公告类型', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (195, 194, 'adm/notecate/add', '新建/编辑', '公告类型', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (196, 194, 'adm/notecate/set', '设置', '公告类型', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (197, 4, 'adm/note/datalist', '公告列表', '公告', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (198, 197, 'adm/note/add', '新建/编辑', '公告', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (199, 197, 'adm/note/del', '删除', '公告', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (200, 197, 'adm/note/view', '查看', '公告', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (201, 4, 'adm/news/datalist', '公司新闻', '公司新闻', 'adm', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (202, 201, 'adm/news/add', '新建/编辑', '公司新闻', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (203, 201, 'adm/news/del', '删除', '公司新闻', 'adm', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (204, 201, 'adm/news/view', '查看', '公司新闻', 'adm', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (205, 5, 'oa/plan/datalist', '日程安排', '日程安排', 'oa', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (206, 205, 'oa/plan/add', '新建/编辑', '日程安排', 'oa', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (207, 205, 'oa/plan/view', '查看', '日程安排', 'oa', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (208, 205, 'oa/plan/del', '删除', '日程安排', 'oa', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (209, 5, 'oa/plan/calendar', '日程日历', '日程安排', 'oa', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (210, 5, 'oa/schedule/datalist', '工作记录', '工作记录', 'oa', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (211, 210, 'oa/schedule/add', '新建/编辑', '工作记录', 'oa', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (212, 210, 'oa/schedule/view', '查看', '工作记录', 'oa', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (213, 210, 'oa/schedule/del', '删除', '工作记录', 'oa', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (214, 5, 'oa/schedule/calendar', '工作日历', '工作日历', 'oa', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (215, 5, 'oa/work/datalist', '工作汇报', '工作汇报', 'oa', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (216, 215, 'oa/work/add', '新建/编辑', '工作汇报', 'oa', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (217, 215, 'oa/work/send', '发送', '工作汇报', 'oa', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (218, 215, 'oa/work/view', '查看', '工作汇报', 'oa', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (219, 215, 'oa/work/del', '删除', '工作汇报', 'oa', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (220, 5, 'oa/note/datalist', '公告通知', '公告通知', 'oa', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (221, 220, 'oa/note/view', '查看', '公告通知', 'oa', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (222, 5, 'oa/news/datalist', '公司新闻', '公司新闻', 'oa', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (223, 222, 'oa/news/view', '查看', '公司新闻', 'oa', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (224, 5, 'oa/meeting/datalist', '会议纪要', '会议纪要', 'oa', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (225, 224, 'oa/meeting/view', '查看', '会议纪要', 'oa', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (226, 2, '', '财务模块', '财务模块', 'finance', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (227, 226, 'finance/expensecate/datalist', '报销类型', '报销类型', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (228, 227, 'finance/expensecate/add', '新建/编辑', '报销类型', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (229, 227, 'finance/expensecate/set', '设置', '报销类型', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (230, 226, 'finance/costcate/datalist', '费用类型', '费用类型', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (231, 230, 'finance/costcate/add', '新建/编辑', '费用类型', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (233, 230, 'finance/costcate/set', '设置', '费用类型', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (234, 6, 'finance/expense/datalist', '报销管理', '报销', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (235, 234, 'finance/expense/add', '新建/编辑', '报销', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (236, 234, 'finance/expense/del', '删除', '报销', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (237, 234, 'finance/expense/view', '查看', '报销', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (238, 6, 'finance/invoice/datalist', '开票管理', '发票', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (239, 238, 'finance/invoice/add', '新建/编辑', '发票', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (240, 238, 'finance/invoice/del', '删除', '发票', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (241, 238, 'finance/invoice/view', '查看', '发票', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (242, 6, 'finance/ticket/datalist', '收票管理', '发票', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (243, 242, 'finance/ticket/add', '新建/编辑', '发票', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (244, 242, 'finance/ticket/del', '删除', '发票', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (245, 242, 'finance/ticket/view', '查看', '发票', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (246, 6, 'finance/income/datalist', '回款管理', '回款记录', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (247, 246, 'finance/income/add', '新建/编辑', '回款记录', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (248, 246, 'finance/income/view', '查看', '回款记录', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (249, 246, 'finance/income/del', '删除', '回款记录', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (250, 6, 'finance/invoice/datalist_a', '无发票回款', '无发票回款', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (251, 250, 'finance/invoice/add_a', '新建/编辑', '无发票回款', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (252, 250, 'finance/invoice/del_a', '删除', '无发票回款', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (253, 250, 'finance/invoice/view_a', '查看', '无发票回款', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (254, 6, 'finance/payment/datalist', '付款管理', '付款记录', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (255, 254, 'finance/payment/add', '新建/编辑', '付款记录', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (256, 254, 'finance/payment/view', '查看', '付款记录', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (257, 254, 'finance/payment/del', '删除', '付款记录', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (258, 6, 'finance/ticket/datalist_a', '无发票付款', '无发票付款', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (259, 258, 'finance/ticket/add_a', '新建/编辑', '无发票付款', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (260, 258, 'finance/ticket/del_a', '删除', '无发票付款', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (261, 258, 'finance/ticket/view_a', '查看', '无发票付款', 'finance', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (262, 6, '', '财务统计', '财务统计', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (263, 262, 'finance/expense/record', '报销记录', '报销记录', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (264, 262, 'finance/invoice/record', '开票记录', '开票记录', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (265, 262, 'finance/ticket/record', '收票记录', '收票记录', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (266, 262, 'finance/income/record', '回款记录', '回款记录', 'finance', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (267, 262, 'finance/payment/record', '付款记录', '付款记录', 'finance', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (268, 2, '', '客户模块', '客户模块', 'customer', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (269, 268, 'customer/industry/datalist', '行业类型', '行业类型', 'home', '', 1, 0, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (270, 269, 'customer/industry/add', '新建/编辑', '行业类型', 'home', '', 2, 0, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (271, 269, 'customer/industry/set', '设置', '行业类型', 'home', '', 2, 0, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (272, 268, 'customer/grade/datalist', '客户等级', '客户等级', 'customer', '', 1, 0, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (273, 272, 'customer/grade/add', '新建/编辑', '客户等级', 'customer', '', 2, 0, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (274, 272, 'customer/grade/set', '设置', '客户等级', 'customer', '', 2,0, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (275, 268, 'customer/source/datalist', '客户渠道', '客户渠道', 'customer', '', 1, 0, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (276, 275, 'customer/source/add', '新建/编辑', '客户渠道', 'customer', '', 2, 0, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (277, 275, 'customer/source/set', '设置', '客户渠道', 'customer', '', 2,0, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (278, 268, 'customer/basic/datalist', '常规数据', '常规数据', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (279, 278, 'customer/basic/add', '新建/编辑', '常规数据', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (280, 278, 'customer/basic/set', '设置', '常规数据', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (281, 7, 'customer/customer/datalist', '客户列表', '客户列表', 'customer', '', 1, 0, 1, 1556143065, 0);
INSERT INTO `oa_admin_rule` VALUES (282, 281, 'customer/customer/add', '新建/编辑', '客户', 'customer', '', 2, 0, 1, 1556143065, 0);
INSERT INTO `oa_admin_rule` VALUES (283, 281, 'customer/customer/view', '查看', '客户', 'customer', '', 2, 0, 1, 1556143065, 0);
INSERT INTO `oa_admin_rule` VALUES (284, 281, 'customer/customer/del', '删除', '客户', 'customer', '', 2, 0, 1, 1556143065, 0);

INSERT INTO `oa_admin_rule` VALUES (285, 7, 'customer/index/rush', '抢 客 宝', '抢客宝', 'customer', '', 1, 0, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (286, 7, 'customer/index/sea', '公海客户', '客户', 'customer', '', 1, 0, 1, 1556143065, 0);
INSERT INTO `oa_admin_rule` VALUES (287, 286, 'customer/index/to_get', '获取', '客户', 'customer', '', 2, 0, 1, 1556143065, 0);
INSERT INTO `oa_admin_rule` VALUES (288, 286, 'customer/index/to_divide', '分配客户', '客户', 'customer', '', 2, 0, 1, 1556143065, 0);
INSERT INTO `oa_admin_rule` VALUES (289, 286, 'customer/index/to_sea', '转入公海', '客户', 'customer', '', 2, 0, 1, 1556143065, 0);
INSERT INTO `oa_admin_rule` VALUES (290, 286, 'customer/index/to_trash', '转入废弃池', '客户', 'customer', '', 2, 0, 1, 1556143065, 0);
INSERT INTO `oa_admin_rule` VALUES (291, 286, 'customer/index/to_revert', '恢复客户', '客户', 'customer', '', 2, 0, 1, 1556143065, 0);

INSERT INTO `oa_admin_rule` VALUES (292, 7, 'customer/index/trash', '废弃客户', '客户', 'customer', '', 1, 0, 1, 1556143065, 0);

INSERT INTO `oa_admin_rule` VALUES (293, 7, 'customer/contact/datalist', '客户联系人', '联系人', 'customer', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (294, 286, 'customer/contact/add', '新建/编辑', '联系人', 'customer', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (295, 286, 'customer/contact/del', '删除', '联系人', 'customer', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (296, 286, 'customer/contact/view', '查看', '客户联系人', 'customer', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (297, 7, 'customer/chance/datalist', '机会线索', '机会线索', 'customer', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (298, 297, 'customer/chance/add', '新建/编辑', '机会线索', 'customer', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (299, 297, 'customer/chance/view', '查看', '机会线索', 'customer', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (300, 297, 'customer/chance/del', '删除', '机会线索', 'customer', '', 2, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (301, 7, 'customer/trace/datalist', '跟进记录', '跟进记录', 'customer', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (302, 301, 'customer/trace/add', '新建/编辑', '跟进记录', 'customer', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (303, 301, 'customer/trace/view', '查看', '跟进记录', 'customer', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (304, 301, 'customer/trace/del', '删除', '跟进记录', 'customer', '', 2, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (305, 2, '', '合同模块', '合同模块', 'contract', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (306, 305, 'contract/cate/datalist', '合同分类', '合同分类', 'contract', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (307, 306, 'contract/cate/add', '新建/编辑', '合同分类', 'contract', '', 2, 1, 1, 0, 1656143065);
INSERT INTO `oa_admin_rule` VALUES (308, 306, 'contract/cate/set', '设置', '合同分类', 'contract', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (309, 305, 'contract/productcate/datalist', '产品分类', '产品分类', 'contract', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (310, 309, 'contract/productcate/add', '新建/编辑', '产品分类', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (311, 309, 'contract/productcate/del', '删除', '产品分类', 'contract', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (312, 305, 'contract/product/datalist', '产品列表', '产品', 'contract', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (313, 312, 'contract/product/add', '新建/编辑', '产品', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (314, 312, 'contract/product/view', '查看', '产品', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (315, 312, 'contract/product/del', '删除', '产品', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (316, 312, 'contract/product/set', '设置', '产品', 'contract', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (317, 305, 'contract/services/datalist', '服务内容', '服务内容', 'contract', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (318, 317, 'contract/services/add', '新建/编辑', '服务内容', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (319, 317, 'contract/services/set', '设置', '服务内容', 'contract', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (320, 305, 'contract/supplier/datalist', '供应商列表', '供应商', 'contract', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (321, 320, 'contract/supplier/add', '新建/编辑', '供应商', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (322, 320, 'contract/supplier/set', '设置', '供应商', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (323, 320, 'contract/supplier/view', '查看', '供应商', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (324, 320, 'contract/supplier/del', '删除', '供应商', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (325, 320, 'contract/supplier/contact_add', '新建/编辑', '供应商联系人', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (326, 320, 'contract/supplier/contact_del', '删除', '供应商联系人', 'contract', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (327, 305, 'contract/purchasedcate/datalist', '采购品分类', '采购品分类', 'contract', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (328, 327, 'contract/purchasedcate/add', '新建/编辑', '采购品分类', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (329, 327, 'contract/purchasedcate/del', '删除', '采购品分类', 'contract', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (330, 305, 'contract/purchased/datalist', '采购品列表', '采购品', 'contract', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (331, 330, 'contract/purchased/add', '新建/编辑', '采购品', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (332, 330, 'contract/purchased/view', '查看', '采购品', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (333, 330, 'contract/purchased/del', '删除', '采购品', 'contract', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (334, 330, 'contract/purchased/set', '设置', '采购品', 'contract', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (335, 8, 'contract/contract/datalist', '销售合同', '销售合同', 'contract', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (336, 335, 'contract/contract/add', '新建/编辑', '合同', 'contract', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (337, 335, 'contract/contract/view', '查看', '合同', 'contract', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (338, 335, 'contract/contract/del', '删除', '合同', 'contract', '', 2, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (339, 8, 'contract/purchase/datalist', '采购合同', '采购合同', 'contract', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (340, 339, 'contract/purchase/add', '新建/编辑', '合同', 'contract', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (341, 339, 'contract/purchase/view', '查看', '合同', 'contract', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (342, 339, 'contract/purchase/del', '删除', '合同', 'contract', '', 2, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (343, 8, 'contract/contract/archivelist', '合同归档', '合同归档', 'contract', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (344, 343, 'contract/purchase/archivelist', '采购合同归档', '采购合同归档', 'contract', '', 2, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (345, 8, 'contract/contract/stoplist', '中止合同', '中止合同', 'contract', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (346, 345, 'contract/purchase/stoplist', '中止采购合同', '中止采购合同', 'contract', '', 2, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (347, 8, 'contract/contract/voidlist', '作废合同', '作废合同', 'contract', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (348, 347, 'contract/purchase/voidlist', '作废合同归档', '作废采购合同', 'contract', '', 2, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (349, 2, '', '项目模块', '项目模块', 'project', '', 1, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (350, 349, 'project/step/datalist', '项目阶段', '项目阶段', 'project', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (351, 350, 'project/step/add', '新建/编辑', '项目阶段', 'project', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (352, 350, 'project/step/set', '设置', '项目阶段', 'project', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (353, 349, 'project/cate/datalist', '项目分类', '项目分类', 'project', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (354, 353, 'project/cate/add', '新建/编辑', '项目分类', 'project', '', 2, 1, 1, 0, 1656143065);
INSERT INTO `oa_admin_rule` VALUES (355, 353, 'project/cate/set', '设置', '项目分类', 'project', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (356, 349, 'project/work/datalist', '工作类别', '工作类别', 'project', '', 1, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (357, 356, 'project/work/add', '新建/编辑', '工作类别', 'project', '', 2, 1, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (358, 356, 'project/work/set', '设置', '工作类别', 'project', '', 2, 1, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (359, 9, 'project/index/datalist', '项目列表', '项目', 'project', '', 1, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (360, 359, 'project/index/add', '新建', '项目', 'project', '', 2, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (361, 359, 'project/index/edit', '编辑', '项目', 'project', '', 2, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (362, 359, 'project/index/view', '查看', '项目', 'project', '', 2, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (363, 359, 'project/index/del', '删除', '项目', 'project', '', 2, 0, 1, 1656142368, 0);

INSERT INTO `oa_admin_rule` VALUES (364, 9, 'project/task/datalist', '任务列表', '任务', 'project', '', 1, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (365, 364, 'project/task/add', '新建', '任务', 'project', '', 2, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (366, 364, 'project/task/edit', '编辑', '任务', 'project', '', 2, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (367, 364, 'project/task/view', '查看', '任务', 'project', '', 2, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (368, 364, 'project/task/del', '删除', '任务', 'project', '', 2, 0, 1, 1656142368, 0);

INSERT INTO `oa_admin_rule` VALUES (369, 9, 'project/task/hour', '任务工时', '工时', 'project', '', 1, 0, 1, 1656142368, 0);

INSERT INTO `oa_admin_rule` VALUES (370, 9, 'project/document/datalist', '文档列表', '文档', 'project', '', 1, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (371, 370, 'project/document/add', '新建/编辑', '文档', 'project', '', 2, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (372, 370, 'project/document/view', '查看', '文档', 'project', '', 2, 0, 1, 1656142368, 0);
INSERT INTO `oa_admin_rule` VALUES (373, 370, 'project/document/delete', '删除', '文档', 'project', '', 2, 0, 1, 1656142368, 0);

INSERT INTO `oa_admin_rule` VALUES (374, 10, 'disk/index/datalist', '我的空间', '我的空间', 'disk', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (375, 374, 'disk/index/add_upload', '新增', '文件', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (376, 374, 'disk/index/add_folder', '新增', '文件夹', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (377, 374, 'disk/index/add_article', '新增/编辑', '在线文档', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (378, 374, 'disk/index/view_article', '查看', '在线文档', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (379, 374, 'disk/index/del', '删除', '文件/文件夹/在线文档', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (380, 374, 'disk/index/rename', '重命名', '文件', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (381, 374, 'disk/index/move', '移动', '文件', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (382, 374, 'disk/index/star', '标星', '文件', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (383, 374, 'disk/index/unstar', '取消标星', '文件', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (384, 374, 'disk/index/back', '还原', '文件', 'disk', '', 2, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (385, 374, 'disk/index/clear', '清除', '文件', 'disk', '', 2, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (386, 10, 'disk/index/sharelist', '共享空间', '共享空间', 'disk', '', 1, 0, 1, 1656143065, 0);
INSERT INTO `oa_admin_rule` VALUES (387, 386, 'disk/index/add_group', '新建/编辑','共享空间', 'disk', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (388, 386, 'disk/index/del_group', '删除','共享空间', 'disk', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (389, 10, 'disk/index/clearlist', '回 收 站', '回收站文件', 'disk', '', 1, 0, 1, 1656143065, 0);

INSERT INTO `oa_admin_rule` VALUES (390, 6, 'finance/loan/datalist', '借支管理', '借支', 'finance', '', 1, 0, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (391, 390, 'finance/loan/add', '新建/编辑', '借支', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (392, 390, 'finance/loan/del', '删除', '借支', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (393, 390, 'finance/loan/view', '查看', '借支', 'finance', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (394, 262, 'finance/loan/record', '借支记录', '借支记录', 'finance', '', 1, 0, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (395, 3, 'user/talent/datalist', '入职申请', '入职申请', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (396, 395, 'user/talent/add', '新增/编辑', '入职申请', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (397, 395, 'user/talent/view', '查看', '入职申请', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (398, 395, 'user/talent/del', '删除', '入职申请', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (399, 395, 'user/talent/set', '入职', '新员工', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (400, 3, 'user/blacklist/datalist', '人员黑名单', '黑名单', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (401, 400, 'user/blacklist/add', '编辑', '黑名单', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (402, 400, 'user/blacklist/del', '删除', '黑名单', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (403, 106, 'user/personal/change_view', '查看', '人事调动', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (404, 106, 'user/personal/change_delete', '删除', '人事调动', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (405, 172, 'adm/meeting/datalist', '会议室预定', '会议室预定', 'user', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (406, 405, 'adm/meeting/add', '新增/编辑', '会议室预定', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (407, 405, 'adm/meeting/view', '查看', '会议室预定', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (408, 405, 'adm/meeting/del', '删除', '会议室预定', 'user', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (409, 405, 'adm/meeting/room_use', '会议室使用情况', '会议室预定', 'user', '', 2, 1, 1, 0, 0);

INSERT INTO `oa_admin_rule` VALUES (410, 59, 'home/cate/links', '办公工具', '办公工具', 'home', '', 1, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (411, 410, 'home/cate/links_add', '新建/编辑', '办公工具', 'home', '', 2, 1, 1, 0, 0);
INSERT INTO `oa_admin_rule` VALUES (412, 410, 'home/cate/links_del', '删除', '办公工具', 'home', '', 2, 1, 1, 0, 0);

-- ----------------------------
-- Table structure for oa_admin_group
-- ----------------------------
DROP TABLE IF EXISTS `oa_admin_group`;
CREATE TABLE `oa_admin_group`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '',
  `status` int(1) NOT NULL DEFAULT 1,
  `rules` mediumtext  NULL COMMENT '用户组拥有的规则id',
  `layouts` mediumtext  NULL COMMENT '首页展示模块',
  `mobile_bar` mediumtext  NULL COMMENT '手机端Bar',
  `mobile_menu` mediumtext  NULL COMMENT '手机端工作台',
  `desc` mediumtext  NULL COMMENT '备注',
  `create_time` bigint(11) NOT NULL DEFAULT 0,
  `update_time` bigint(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工权限分组表';

-- ----------------------------
-- Records of oa_admin_group
-- ----------------------------
INSERT INTO `oa_admin_group` VALUES (1, '超级权限角色', 1, '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,364,365,366,367,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,384,385,386,387,388,389,390,391,392,393,394,395,396,397,398,399,400,401,402,403,404,405,406,407,408,409,410,411,412', '1,2,3,4,5,6,7,8,9,10,11,12,13','1,2,3,4,5','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,5,17,18,19,20','超级权限角色，拥有系统的最高权限，主要用于系统初始化数据而设，不可修改，不可删除。', 0, 0);
INSERT INTO `oa_admin_group` VALUES (2, '管理岗角色', 1, '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,364,365,366,367,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,384,385,386,387,388,389,390,391,392,393,394,395,396,397,398,399,400,401,402,403','1,2,3,4,5,6,7,8,9,10,11,12,13','1,2,3,4,5','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,5,17,18,19,20', '管理岗角色权限，可根据公司的具体需求调整。', 0, 0);
INSERT INTO `oa_admin_group` VALUES (3, '业务岗角色', 1, '4,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,197,198,199,200,201,202,203,204,5,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,6,390,391,392,393,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,394,263,264,265,266,267,7,281,282,283,284,285,286,287,288,289,290,291,294,295,296,292,293,297,298,299,300,301,302,303,304,8,335,336,337,338,339,340,341,342,343,344,345,346,347,348,9,359,360,361,362,363,364,365,366,367,368,369,370,371,372,373,10,374,375,376,377,378,379,380,381,382,383,384,385,386,387,388,389','1,2,3,4,5,6,7,8,9,10,11,12,13','1,2,3,4,5','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,5,17,18,19,20', '业务岗角色权限，可根据公司的具体需求调整。', 0, 0);

-- ----------------------------
-- Table structure for oa_data_auth
-- ----------------------------
DROP TABLE IF EXISTS `oa_data_auth`;
CREATE TABLE `oa_data_auth`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '权限名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '权限标识唯一，字母',
  `desc` mediumtext  NULL COMMENT '备注描述',
  `module` varchar(255) NOT NULL DEFAULT '' COMMENT '所属模块，唯一，字母',
  `uids` mediumtext  NULL COMMENT '权限用户，1,2,3',
  `conf_1` mediumtext  NULL COMMENT '配置字段1，可作为预配置内容',
  `conf_2` mediumtext  NULL COMMENT '配置字段2，可作为预配置内容',
  `conf_3` mediumtext  NULL COMMENT '配置字段3，可作为预配置内容',
  `conf_4` mediumtext  NULL COMMENT '配置字段4，可作为预配置内容',
  `conf_5` mediumtext  NULL COMMENT '配置字段5，可作为预配置内容',
  `conf_6` mediumtext  NULL COMMENT '配置字段6，可作为预配置内容',
  `conf_7` mediumtext  NULL COMMENT '配置字段7，可作为预配置内容',
  `conf_8` mediumtext  NULL COMMENT '配置字段8，可作为预配置内容',
  `conf_9` mediumtext  NULL COMMENT '配置字段9，可作为预配置内容',
  `conf_10` mediumtext  NULL COMMENT '配置字段9，可作为预配置内容',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '数据权限表';

-- ----------------------------
-- Records of  oa_data_auth
-- ----------------------------
INSERT INTO `oa_data_auth` VALUES (1, '日常办公', 'office_admin', '日常办公相关数据权限配置。', 'office', '', '', '3', '', '', '', '', '', '', '', '', 1656143065, 1724830718);
INSERT INTO `oa_data_auth` VALUES (2, '财务模块', 'finance_admin', '财务到账相关数据权限配置。', 'finance', '', '', '', '', '', '', '', '', '', '', '', 1656143065, 0);
INSERT INTO `oa_data_auth` VALUES (3, '客户模块', 'customer_admin', '客户模块相关数据权限配置。', 'customer', '', '', '10', '1000', '', '', '', '', '', '', '', 1656143065, 1724830738);
INSERT INTO `oa_data_auth` VALUES (4, '合同模块', 'contract_admin', '合同模块相关数据权限配置。', 'contract', '', '', '1', '1', '1', '1', '1', '1', '', '', '30', 1656143065, 1724830772);
INSERT INTO `oa_data_auth` VALUES (5, '项目模块', 'project_admin', '项目模块相关数据权限配置。', 'project', '', '', '', '', '', '', '', '', '', '', '3', 1656143065, 0);
INSERT INTO `oa_data_auth` VALUES (6, '网盘模块', 'disk_admin', '网盘模块相关数据权限配置。', 'disk', '', '', '', '', '', '', '', '', '', '', '', 1656143065, 0);

-- ----------------------------
-- Table structure for oa_config
-- ----------------------------
DROP TABLE IF EXISTS `oa_config`;
CREATE TABLE `oa_config`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '配置名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '配置标识',
  `content` mediumtext  NULL COMMENT '配置内容',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '系统配置表';

-- ----------------------------
-- Records of oa_config
-- ----------------------------
INSERT INTO `oa_config`VALUES (1, '信息配置', 'web', 'a:10:{s:2:\"id\";s:1:\"1\";s:11:\"admin_title\";s:8:\"勾股OA\";s:6:\"domain\";s:24:\"https://www.gougucms.com\";s:3:\"icp\";s:21:\"粤ICP备xxxxxxx号-1\";s:4:\"logo\";s:31:\"/static/home/images/syslogo.png\";s:4:\"file\";s:0:\"\";s:10:\"small_logo\";s:37:\"/static/home/images/syslogo_small.png\";s:5:\"beian\";s:27:\"粤公网安备xxxxxxx号-1\";s:8:\"keywords\";s:8:\"勾股OA\";s:4:\"desc\";s:565:\"勾股办公是一款基于ThinkPHP8 + Layui + MySql打造的，简单实用的开源免费的企业办公系统框架。系统集成了系统设置、基础数据、人事管理、消息管理、审批管理、行政办公、个人办公、客户管理、合同管理、项目管理、财务管理、知识网盘等模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，帮助开发者简单高效降低二次开发成本，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。\";}', 1, 1612514630, 1724823769);
INSERT INTO `oa_config`VALUES (2, '系统配置', 'system', 'a:8:{s:9:\"menu_mode\";s:9:\"classical\";s:19:\"upload_max_filesize\";s:2:\"50\";s:9:\"msg_sound\";s:1:\"1\";s:9:\"watermark\";s:1:\"1\";s:6:\"qrcode\";s:1:\"2\";s:7:\"version\";s:5:\"5.0.2\";s:9:\"copyright\";s:24:\"© 2021-2025 gouguoa.com\";s:2:\"id\";s:1:\"2\";}', 1, 1612514630, 1724824879);
INSERT INTO `oa_config`VALUES (3, '邮箱配置', 'email', 'a:8:{s:2:\"id\";s:1:\"2\";s:4:\"smtp\";s:11:\"smtp.qq.com\";s:9:\"smtp_port\";s:3:\"465\";s:9:\"smtp_user\";s:15:\"gougucms@qq.com\";s:8:\"smtp_pwd\";s:6:\"123456\";s:4:\"from\";s:24:\"勾股CMS系统管理员\";s:5:\"email\";s:18:\"admin@gougucms.com\";s:8:\"template\";s:485:\"<p>勾股办公是一款基于ThinkPHP8 + Layui + MySql打造的，简单实用的开源免费的企业办公系统框架。系统集成了系统设置、人事管理模块、消息管理模块、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，帮助开发者简单高效降低二次开发成本，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。</p>\";}', 1, 1612521657, 1637075205);
INSERT INTO `oa_config`VALUES (4, 'Api Token配置', 'token', 'a:5:{s:2:\"id\";s:1:\"3\";s:3:\"iss\";s:15:\"oa.gougucms.com\";s:3:\"aud\";s:7:\"gouguoa\";s:7:\"secrect\";s:7:\"GOUGUOA\";s:7:\"exptime\";s:4:\"3600\";}', 1, 1627313142, 1638010233);
INSERT INTO `oa_config`VALUES (5, '其他配置', 'other', 'a:3:{s:2:\"id\";s:1:\"5\";s:6:\"author\";s:15:\"勾股工作室\";s:7:\"version\";s:13:\"v1.2025.06.06\";}', 1, 1613725791, 1724824410);

-- ----------------------------
-- Table structure for oa_timing_task
-- ----------------------------
DROP TABLE IF EXISTS `oa_timing_task`;
CREATE TABLE `oa_timing_task`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '任务名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '任务标识，唯一',
  `link` varchar(255) NOT NULL DEFAULT '' COMMENT '执行链接',
  `types` int(2) NOT NULL DEFAULT 3 COMMENT '状态:1系统任务,2普通任务,3自定义任务',
  `desc` varchar(500) NOT NULL DEFAULT '' COMMENT '任务描述',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '定时任务表';

-- ----------------------------
-- Table structure for oa_department
-- ----------------------------
DROP TABLE IF EXISTS `oa_department`;
CREATE TABLE `oa_department`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '部门名称',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级部门id',
  `leader_ids` varchar(500) NULL DEFAULT '' COMMENT '部门负责人ids',
  `phone` varchar(60) NOT NULL DEFAULT '' COMMENT '部门联系电话',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '部门组织';

-- ----------------------------
-- Records of oa_department
-- ----------------------------
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (1, '董事会', 0);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (2, '人事部', 1);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (3, '财务部', 1);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (4, '市场部', 1);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (5, '销售部', 1);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (6, '技术部', 1);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (7, '客服部', 1);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (8, '销售一部', 5);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (9, '销售二部', 5);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (10, '销售三部', 5);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (11, '产品部', 6);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (12, '设计部', 6);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (13, '研发部', 6);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (14, '客服一部', 7);
INSERT INTO `oa_department`(`id`, `title`, `pid`) VALUES (15, '客服二部', 7);

-- ----------------------------
-- Table structure for oa_department_admin
-- ----------------------------
DROP TABLE IF EXISTS `oa_department_admin`;
CREATE TABLE `oa_department_admin`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '员工ID',
  `department_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '部门ID',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '次要部门人员关联表';

-- ----------------------------
-- Table structure for oa_talent
-- ----------------------------
DROP TABLE IF EXISTS `oa_talent`;
CREATE TABLE `oa_talent`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '姓名',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '电子邮箱',
  `mobile` bigint(11) NOT NULL DEFAULT 0 COMMENT '手机号码',
  `sex` int(255) NOT NULL DEFAULT 0 COMMENT '性别:1男,2女',
  `to_did` int(11) NOT NULL DEFAULT 0 COMMENT '所属部门',
  `to_dids` varchar(500) NOT NULL DEFAULT '' COMMENT '次部门',
  `thumb` varchar(255) NOT NULL COMMENT '头像',
  `position_id` int(11) NOT NULL DEFAULT 0 COMMENT '职位id',
  `type` int(1) NOT NULL DEFAULT 0 COMMENT '员工类型:0未设置,1正式,2试用,3实习',
  `position_name` int(11) NOT NULL DEFAULT 0 COMMENT '应聘职务',
  `position_rank` int(11) NOT NULL DEFAULT 0 COMMENT '应聘职级',
  `job_number` varchar(255) NOT NULL DEFAULT '' COMMENT '工号',
  `birthday` varchar(255) NOT NULL DEFAULT '' COMMENT '生日',
  `pid` int(11) NOT NULL DEFAULT 0 COMMENT '上级领导',
  `work_date` varchar(255) NOT NULL DEFAULT '' COMMENT '开始工作时间',
  `work_location` int(11) NOT NULL DEFAULT 0 COMMENT '工作地点',
  `native_place` varchar(255) NOT NULL DEFAULT '' COMMENT '籍贯',
  `nation` varchar(255) NOT NULL DEFAULT '' COMMENT '民族',
  `home_address` varchar(255) NOT NULL DEFAULT '' COMMENT '家庭地址',
  `current_address` varchar(255) NOT NULL DEFAULT '' COMMENT '现居地址',
  `contact` varchar(255) NOT NULL DEFAULT '' COMMENT '紧急联系人',
  `contact_mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '紧急联系人电话',
  `resident_type` int(1) NOT NULL DEFAULT 0 COMMENT '户口性质:1农村户口,2城镇户口',
  `resident_place` varchar(255) NOT NULL DEFAULT '' COMMENT '户口所在地',
  `graduate_school` varchar(255) NOT NULL DEFAULT '' COMMENT '毕业学校',
  `graduate_day` varchar(255) NOT NULL DEFAULT '毕业日期',
  `political` int(1) NOT NULL DEFAULT 1 COMMENT '政治面貌:1中共党员,2团员',
  `marital_status` int(1) NOT NULL DEFAULT 1 COMMENT '婚姻状况:1未婚,2已婚,3离异',
  `idcard` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证',
  `education` varchar(255) NOT NULL DEFAULT '' COMMENT '学位',
  `speciality` varchar(255) NOT NULL DEFAULT '' COMMENT '专业',
  `bank_account` varchar(255) NOT NULL DEFAULT '' COMMENT '银行卡号',
  `social_account` varchar(255) NOT NULL DEFAULT '' COMMENT '社保账号',
  `salary` int(11) NOT NULL DEFAULT 0 COMMENT '期望薪资',
  `salary_remark` varchar(255) NOT NULL DEFAULT '' COMMENT '薪资备注',
  `reference_name` varchar(255) NOT NULL DEFAULT '' COMMENT '推荐人姓名',
  `reference_rel` varchar(255) NOT NULL DEFAULT '' COMMENT '推荐人关系',
  `reference_mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '推荐人联系方式',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '档案附件',
  `desc` mediumtext  NULL COMMENT '个人简介',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '入职评语',
  `entry_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '入职时间',
  `is_staff` int(1) NOT NULL DEFAULT 1 COMMENT '身份类型:1普通员工,2劳务派遣',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:1正常,2已入职',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `did` int(11) NOT NULL DEFAULT 0 COMMENT '创建部门',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '申请时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新信息时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `talent_id` int(11) NOT NULL DEFAULT 0 COMMENT '入职申请id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '入职申请表';

-- ----------------------------
-- Table structure for oa_personal_quit
-- ----------------------------
DROP TABLE IF EXISTS `oa_personal_quit`;
CREATE TABLE `oa_personal_quit`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `lead_admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级领导',
  `connect_id` int(11) NOT NULL DEFAULT 0 COMMENT '资料交接人',
  `connect_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '资料交接时间',
  `connect_uids` varchar(100) NOT NULL DEFAULT '' COMMENT '参与交接人,可多个',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '档案附件',
  `quit_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '离职时间',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:1未交接,2已交接离职',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注信息',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人所在部门',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '离职申请表';

-- ----------------------------
-- Table structure for oa_department_change
-- ----------------------------
DROP TABLE IF EXISTS `oa_department_change`;
CREATE TABLE `oa_department_change`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '员工ID',
  `from_did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '原部门id',
  `to_did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '调到部门id',
  `connect_id` int(11) NOT NULL DEFAULT 0 COMMENT '资料交接人',
  `connect_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '资料交接时间',
  `connect_uids` varchar(100) NOT NULL DEFAULT '' COMMENT '参与交接人,可多个',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '档案附件',
  `move_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '调动时间',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:1未调动,2已交接调动',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注信息',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人所在部门',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '人事调动申请表';

-- ----------------------------
-- Table structure for oa_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `oa_blacklist`;
CREATE TABLE `oa_blacklist`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '姓名',
  `mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '手机号码',
  `idcard` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证',
  `remark` text NULL COMMENT '备注信息',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '申请时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新信息时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '黑名单表';

-- ----------------------------
-- Table structure for oa_file_group
-- ----------------------------
DROP TABLE IF EXISTS `oa_file_group`;
CREATE TABLE `oa_file_group`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '分组名',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '文件分组表';

-- ----------------------------
-- Table structure for oa_file
-- ----------------------------
DROP TABLE IF EXISTS `oa_file`;
CREATE TABLE `oa_file`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(20) NOT NULL DEFAULT '' COMMENT '所属模块',
  `sha1` varchar(60) NOT NULL COMMENT 'sha1',
  `md5` varchar(60) NOT NULL COMMENT 'md5',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '原始文件名',
  `filename` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名',
  `filepath` varchar(255) NOT NULL DEFAULT '' COMMENT '文件路径+文件名',
  `thumbpath` varchar(255) NOT NULL DEFAULT '' COMMENT '缩略图路径',
  `filesize` int(10) NOT NULL DEFAULT 0 COMMENT '文件大小',
  `fileext` varchar(10) NOT NULL DEFAULT '' COMMENT '文件后缀',
  `mimetype` varchar(100) NOT NULL DEFAULT '' COMMENT '文件类型',
  `group_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '文件分组ID',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上传会员ID',
  `uploadip` varchar(64) NOT NULL DEFAULT '' COMMENT '上传IP',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未审核1已审核-1不通过',
  `create_time` bigint(11) NOT NULL DEFAULT 0,
  `admin_id` int(11) NOT NULL COMMENT '审核者id',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `audit_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '审核时间',
  `action` varchar(50) NOT NULL DEFAULT '' COMMENT '来源模块功能',
  `use` varchar(255) NULL DEFAULT NULL COMMENT '用处',
  `download` int(11) NOT NULL DEFAULT 0 COMMENT '下载量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '文件表';

-- ----------------------------
-- Records of oa_file
-- ----------------------------
INSERT INTO `oa_file` VALUES (1, 'admin', '5125347886f07f48f7003825660117039eb8784f', '563e5e8f48e607ed54461796b0cb4844', 'f95982689eb222b84e999122a50b3780.jpg.jpg', 'f95982689eb222b84e999122a50b3780.jpg', 'https://blog.gougucms.com/storage/202202/f95982689eb222b84e999122a50b3780.jpg','', 62609, 'jpg', 'image/jpeg', 0, 1, '127.0.0.1', 1, 1645057433, 1, 0, 1645057433, 'upload', 'thumb', 0);
INSERT INTO `oa_file` VALUES (2, 'admin', '5125347886f07f48f7003825660117039eb8784f', '563e5e8f48e607ed54461796b0cb4844', 'e729477de18e3be7e7eb4ec7fe2f821e.jpg', 'e729477de18e3be7e7eb4ec7fe2f821e.jpg', 'https://blog.gougucms.com/storage/202202/e729477de18e3be7e7eb4ec7fe2f821e.jpg','', 62609, 'jpg', 'image/jpeg', 0, 1, '127.0.0.1', 1, 1645057433, 1, 0, 1645057433, 'upload', 'thumb', 0);
INSERT INTO `oa_file` VALUES (3, 'admin', '5125347886f07f48f7003825660117039eb8784f', '563e5e8f48e607ed54461796b0cb4844', '1193f7a1585b9f6e8a97ae17718018b3.jpg', 'images/1193f7a1585b9f6e8a97ae17718018b3.jpg', 'https://blog.gougucms.com/storage/202204/1193f7a1585b9f6e8a97ae17718018b3.jpg','', 62609, 'jpg', 'image/jpeg', 0, 1, '127.0.0.1', 1, 1645057433, 1, 0, 1645057433, 'upload', 'thumb', 0);
INSERT INTO `oa_file` VALUES (4, 'admin', '5125347886f07f48f7003825660117039eb8784f', '563e5e8f48e607ed54461796b0cb4844', '0f22a5ba4797b2fa22049ea73e6f779c.jpg', 'images/0f22a5ba4797b2fa22049ea73e6f779c.jpg', 'https://blog.gougucms.com/storage/202202/0f22a5ba4797b2fa22049ea73e6f779c.jpg','', 62609, 'jpg', 'image/jpeg', 0, 1, '127.0.0.1', 1, 1645057433, 1, 0, 1645057433, 'upload', 'thumb', 0);

-- ----------------------------
-- Table structure for oa_enterprise
-- ----------------------------
DROP TABLE IF EXISTS `oa_enterprise`;
CREATE TABLE `oa_enterprise`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '企业名称',
  `city` varchar(60) NOT NULL DEFAULT '' COMMENT '所在城市',
  `bank` varchar(60) NOT NULL DEFAULT '' COMMENT '开户银行',
  `bank_sn` varchar(60) NOT NULL DEFAULT '' COMMENT '银行帐号',
  `tax_num` varchar(100) NOT NULL DEFAULT '' COMMENT '纳税人识别号',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '开票电话',
  `address` varchar(200) NOT NULL DEFAULT '' COMMENT '开票地址',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注说明',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '企业主体';

-- ----------------------------
-- Records of oa_enterprise
-- ----------------------------
INSERT INTO `oa_enterprise` VALUES (1, '勾股信息科技有限公司','广州','','','','','','', 1, 1638006751, 0);

-- ----------------------------
-- Table structure for oa_template
-- ----------------------------
DROP TABLE IF EXISTS `oa_template`;
CREATE TABLE `oa_template`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '消息模板名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '权限标识唯一，字母',
  `types` tinyint(1) NOT NULL DEFAULT 1 COMMENT '类型:1普通消息,2审批消息',
  `check_types` int(11) NOT NULL DEFAULT 0 COMMENT '审批类型:0',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注描述，使用场景等',
  `msg_link` varchar(255) NOT NULL DEFAULT '' COMMENT '消息模板链接(审批申请)',
  `msg_title_0` varchar(255) NOT NULL DEFAULT '' COMMENT '消息模板标题(审批申请发审批人)',
  `msg_content_0` varchar(500) NOT NULL DEFAULT '' COMMENT '消息模板内容(审批申请发审批人)',
  `msg_title_1` varchar(255) NOT NULL DEFAULT '' COMMENT '消息模板标题(审批通过发申请人)',
  `msg_content_1` varchar(500) NOT NULL DEFAULT '' COMMENT '消息模板内容(审批通过发申请人)',
  `msg_title_2` varchar(255) NOT NULL DEFAULT '' COMMENT '消息模板标题(审批拒绝发申请人)',
  `msg_content_2` varchar(500) NOT NULL DEFAULT '' COMMENT '消息模板内容(审批拒绝发申请人)',
  `msg_title_3` varchar(255) NOT NULL DEFAULT '' COMMENT '消息模板标题(审批通过发抄送人)',
  `msg_content_3` varchar(500) NOT NULL DEFAULT '' COMMENT '消息模板内容(审批通过发抄送人)',
  `email_link` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱消息模板链接',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT  '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '消息模板表';

-- ----------------------------
-- Records of oa_template
-- ----------------------------
INSERT INTO `oa_template` VALUES (1, '公告通知', 'note', 1, 0, '', '/oa/note/view/id/{action_id}', '{from_user}发了一个新『公告』，请及时查看', '您有一个新公告：{title}。', '', '', '', '', '', '', '', 1, 1, 1733312491, 1733314809, 0);
INSERT INTO `oa_template` VALUES (2, '请假审批', 'leaves', 2, 0, '', '/home/leaves/view/id/{action_id}', '{from_user}提交了一个『请假申请』，请及时审批', '您有一个新的『请假申请』需要处理。', '您提交的『请假申请』已被审批通过。', '您在{create_time}提交的『请假申请』已于{date}被审批通过。', '您提交的『请假申请』已被驳回拒绝。', '您在{create_time}提交的『请假申请』已于{date}被驳回拒绝。', '{from_user}提交的『请假审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『请假审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733312616, 1733314814, 0);
INSERT INTO `oa_template` VALUES (3, '出差审批', 'trips', 2, 0, '', '/home/trips/view/id/{action_id}', '{from_user}提交了一个『出差申请』，请及时审批', '您有一个新的『出差申请』需要处理。', '您提交的『出差申请』已被审批通过。', '您在{create_time}提交的『出差申请』已于{date}被审批通过。', '您提交的『出差申请』已被驳回拒绝。', '您在{create_time}提交的『出差申请』已于{date}被驳回拒绝。', '{from_user}提交的『出差审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『出差审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733312725, 1733314819, 0);
INSERT INTO `oa_template` VALUES (4, '外出审批', 'outs', 2, 0, '', '/home/outs/view/id/{action_id}', '{from_user}提交了一个『外出申请』，请及时审批', '您有一个新的『外出申请』需要处理。', '您提交的『外出申请』已被审批通过。', '您在{create_time}提交的『外出申请』已于{date}被审批通过。', '您提交的『外出申请』已被驳回拒绝。', '您在{create_time}提交的『外出申请』已于{date}被驳回拒绝。', '{from_user}提交的『外出审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『外出审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733312801, 1733314824, 0);
INSERT INTO `oa_template` VALUES (5, '加班审批', 'overtimes', 2, 0, '', '/home/overtimes/view/id/{action_id}', '{from_user}提交了一个『加班申请』，请及时审批', '您有一个新的『加班申请』需要处理。', '您提交的『加班申请』已被审批通过。', '您在{create_time}提交的『加班申请』已于{date}被审批通过。', '您提交的『加班申请』已被驳回拒绝。', '您在{create_time}提交的『加班申请』已于{date}被驳回拒绝。', '{from_user}提交的『加班审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『加班审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733312801, 1733314828, 0);
INSERT INTO `oa_template` VALUES (6, '用章审批', 'seal', 2, 0, '', '/adm/seal/view/id/{action_id}', '{from_user}提交了一个『用章申请』，请及时审批', '您有一个新的『用章申请』需要处理。', '您提交的『公文申请』已被审批通过。', '您在{create_time}提交的『公文申请』已于{date}被审批通过。', '您提交的『用章申请』已被驳回拒绝。', '您在{create_time}提交的『用章申请』已于{date}被驳回拒绝。', '{from_user}提交的『用章审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『用章审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733313018, 1733314834, 0);
INSERT INTO `oa_template` VALUES (7, '公文审批', 'official', 2, 0, '', '/adm/official/view/id/{action_id}', '{from_user}提交了一个『公文申请』，请及时审批', '您有一个新的『公文申请』需要处理。', '您提交的『公文申请』已被审批通过', '您在{create_time}提交的『公文申请』已于{date}被审批通过。', '您提交的『公文申请』已被驳回拒绝', '您在{create_time}提交的『公文申请』已于{date}被驳回拒绝。', '{from_user}提交的『公文审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『公文审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733313078, 1733313262, 0);
INSERT INTO `oa_template` VALUES (8, '报销审批', 'expense', 2, 0, '', '/finance/expense/view/id/{action_id}', '{from_user}提交了一个『报销申请』，请及时审批', '您有一个新的『报销申请』需要处理。', '您提交的『报销申请』已被审批通过', '您在{create_time}提交的『报销申请』已于{date}被审批通过。', '您提交的『报销申请』已被驳回拒绝', '您在{create_time}提交的『报销申请』已于{date}被驳回拒绝。', '{from_user}提交的『报销审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『报销审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733313169, 1733313253, 0);
INSERT INTO `oa_template` VALUES (9, '发票审批', 'invoice', 2, 0, '', '/finance/invoice/view/id/{action_id}', '{from_user}提交了一个『发票申请』，请及时审批', '您有一个新的『发票申请』需要处理。', '您提交的『发票申请』已被审批通过', '您在{create_time}提交的『发票申请』已于{date}被审批通过。', '您提交的『发票申请』已被驳回拒绝', '您在{create_time}提交的『发票申请』已于{date}被驳回拒绝。', '{from_user}提交的『发票审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『发票审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733313245, 0, 0);
INSERT INTO `oa_template` VALUES (10, '收票审批', 'ticket', 2, 0, '', '/finance/ticket/view/id/{action_id}', '{from_user}提交了一个『收票申请』，请及时审批', '您有一个新的『收票申请』需要处理。', '您提交的『收票申请』已被审批通过', '您在{create_time}提交的『收票申请』已于{date}被审批通过。', '您提交的『收票申请』已被驳回拒绝', '您在{create_time}提交的『收票申请』已于{date}被驳回拒绝。', '{from_user}提交的『收票审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『收票审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733313341, 0, 0);
INSERT INTO `oa_template` VALUES (11, '无发票回款审批', 'invoicea', 2, 0, '', '/finance/invoice/view_a/id/{action_id}', '{from_user}提交了一个『无发票回款申请』，请及时审批', '您有一个新的『无发票回款申请』需要处理。', '您提交的『无发票回款申请』已被审批通过', '您在{create_time}提交的『无发票回款申请』已于{date}被审批通过。', '您提交的『无发票回款申请』已被驳回拒绝', '您在{create_time}提交的『无发票回款申请』已于{date}被驳回拒绝。', '{from_user}提交的『无发票回款审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『无发票回款审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733314549, 0, 0);
INSERT INTO `oa_template` VALUES (12, '无发票付款审批', 'ticketa', 2, 0, '', '/finance/ticket/view_a/id/{action_id}', '{from_user}提交了一个『无发票付款申请』，请及时审批', '您有一个新的『无发票付款申请』需要处理。', '您提交的『无发票付款申请』已被审批通过', '您在{create_time}提交的『无发票付款申请』已于{date}被审批通过。', '您提交的『无发票付款申请』已被驳回拒绝', '您在{create_time}提交的『无发票付款申请』已于{date}被驳回拒绝。', '{from_user}提交的『无发票付款审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『无发票付款审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733314607, 0, 0);
INSERT INTO `oa_template` VALUES (13, '销售合同审批', 'contract', 2, 0, '', '/contract/contract/view/id/{action_id}', '{from_user}提交了一个『销售合同审批』，请及时审批', '您有一个新的『销售合同审批』需要处理。', '您提交的『销售合同审批』已被审批通过', '您在{create_time}提交的『销售合同审批』已于{date}被审批通过。', '您提交的『销售合同审批』已被驳回拒绝', '您在{create_time}提交的『销售合同审批』已于{date}被驳回拒绝。', '{from_user}提交的『销售合同审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『销售合同审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733314701, 0, 0);
INSERT INTO `oa_template` VALUES (14, '采购合同审批', 'purchase', 2, 0, '', '/contract/purchase/view/id/{action_id}', '{from_user}提交了一个『采购合同审批』，请及时审批', '您有一个新的『采购合同审批』需要处理。', '您提交的『采购合同审批』已被审批通过', '您在{create_time}提交的『采购合同审批』已于{date}被审批通过。', '您提交的『采购合同审批』已被驳回拒绝', '您在{create_time}提交的『采购合同审批』已于{date}被驳回拒绝。', '{from_user}提交的『采购合同审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『采购合同审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733314789, 1753284351, 0);
INSERT INTO `oa_template` VALUES (15, '借支审批', 'loan', 2, 0, '', '/finance/loan/view/id/{action_id}', '{from_user}提交了一个『借支审批』，请及时审批', '您有一个新的『借支审批』需要处理。', '您提交的『借支审批』已被审批通过', '您在{create_time}提交的『借支审批』已于{date}被审批通过。', '您提交的『借支审批』已被驳回拒绝', '您在{create_time}提交的『借支审批』已于{date}被驳回拒绝。', '{from_user}提交的『借支审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『借支审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733314789, 1753284351, 0);
INSERT INTO `oa_template` VALUES (16, '入职审批', 'talent', 2, 0, '', '/user/talent/view/id/{action_id}', '{from_user}提交了一个『入职审批』，请及时审批', '您有一个新的『入职审批』需要处理。', '您提交的『入职审批』已被审批通过', '您在{create_time}提交的『入职审批』已于{date}被审批通过。', '您提交的『入职审批』已被驳回拒绝', '您在{create_time}提交的『入职审批』已于{date}被驳回拒绝。', '{from_user}提交的『入职审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『入职审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733314789, 1753284351, 0);
INSERT INTO `oa_template` VALUES (17, '离职审批', 'personal_quit', 2, 0, '', '/user/personal/leave_view/id/{action_id}', '{from_user}提交了一个『离职审批』，请及时审批', '您有一个新的『离职审批』需要处理。', '您提交的『离职审批』已被审批通过', '您在{create_time}提交的『离职审批』已于{date}被审批通过。', '您提交的『离职审批』已被驳回拒绝', '您在{create_time}提交的『离职审批』已于{date}被驳回拒绝。', '{from_user}提交的『离职审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『离职审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733314789, 1753284351, 0);
INSERT INTO `oa_template` VALUES (18, '人事调动审批', 'department_change', 2, 0, '', '/user/personal/change_view/id/{action_id}', '{from_user}提交了一个『人事调动审批』，请及时审批', '您有一个新的『人事调动审批』需要处理。', '您提交的『人事调动审批』已被审批通过', '您在{create_time}提交的『人事调动审批』已于{date}被审批通过。', '您提交的『人事调动审批』已被驳回拒绝', '您在{create_time}提交的『人事调动审批』已于{date}被驳回拒绝。', '{from_user}提交的『人事调动审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『人事调动审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1733314789, 1753284351, 0);
INSERT INTO `oa_template` VALUES (19, '会议室预定审批', 'meeting_order', 2, 0, '', '/adm/meeting/view/id/{action_id}', '{from_user}提交了一个『会议室预定审批』，请及时审批', '您有一个新的『会议室预定审批』需要处理。', '您提交的『会议室预定审批』已被审批通过', '您在{create_time}提交的『会议室预定审批』已于{date}被审批通过。', '您提交的『会议室预定审批』已被驳回拒绝', '您在{create_time}提交的『会议室预定审批』已于{date}被驳回拒绝。', '{from_user}提交的『会议室预定审批』已被审批通过并抄送给你', '{from_user}在{create_time}提交的『会议室预定审批』已被审批通过并抄送给你，请及时查看详情。', '', 1, 1, 1758550842, 1758582833, 0);
INSERT INTO `oa_template` VALUES (20, '工作汇报通知', 'work', 1, 0, '', '/oa/work/view/id/{action_id}', '{from_user}给您发了一份『工作汇报』，请及时查看', '您有一份新的工作汇报待查看。', '', '', '', '', '', '', '', 1, 1, 1760576534, 1760577087, 0);

-- ----------------------------
-- Table structure for oa_industry
-- ----------------------------
DROP TABLE IF EXISTS `oa_industry`;
CREATE TABLE `oa_industry`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '行业名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '行业';

-- ----------------------------
-- Records of oa_industry
-- ----------------------------
INSERT INTO `oa_industry` VALUES (1, '工业品企业', 1, 1637987189, 0);
INSERT INTO `oa_industry` VALUES (2, '互联网企业', 1, 1637987199, 0);
INSERT INTO `oa_industry` VALUES (3, '服务行业', 1, 1637987199, 0);
INSERT INTO `oa_industry` VALUES (4, '消费品企业', 1, 1637987199, 0);
INSERT INTO `oa_industry` VALUES (5, '原材料企业', 1, 1637987199, 0);
INSERT INTO `oa_industry` VALUES (6, '农业企业', 1, 1637987199, 0);
INSERT INTO `oa_industry` VALUES (7, '科技企业', 1, 1637987199, 0);
INSERT INTO `oa_industry` VALUES (8, '其他行业', 1, 1637987199, 0);

-- ----------------------------
-- Table structure for oa_rewards_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_rewards_cate`;
CREATE TABLE `oa_rewards_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '奖罚项目名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '奖罚项目';

-- ----------------------------
-- Records of oa_rewards_cate
-- ----------------------------
INSERT INTO `oa_rewards_cate` VALUES (1, '生日福利', 1, 1637987189, 0);
INSERT INTO `oa_rewards_cate` VALUES (2, '节日福利', 1, 1637987199, 0);
INSERT INTO `oa_rewards_cate` VALUES (3, '迟到扣款', 1, 1638088518, 0);
INSERT INTO `oa_rewards_cate` VALUES (4, '全勤奖励', 1, 1637987199, 0);
INSERT INTO `oa_rewards_cate` VALUES (5, '表现优秀', 1, 1637987199, 0);
INSERT INTO `oa_rewards_cate` VALUES (6, '违规操作', 1, 1637987199, 0);

-- ----------------------------
-- Table structure for oa_rewards
-- ----------------------------
DROP TABLE IF EXISTS `oa_rewards`;
CREATE TABLE `oa_rewards`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL DEFAULT 0 COMMENT '员工ID',
  `types` int(1) NOT NULL DEFAULT 0 COMMENT '奖罚类型:1奖励2惩罚',
  `rewards_cate` int(11) NOT NULL DEFAULT 0 COMMENT '奖罚项目',
  `rewards_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '奖罚日期',
  `cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  `thing` varchar(255) NOT NULL DEFAULT '' COMMENT '物品',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:1未执行2已执行',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件',
  `remark` mediumtext  NULL COMMENT '备注说明',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT  '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工奖罚表';

-- ----------------------------
-- Table structure for oa_basic_user
-- ----------------------------
DROP TABLE IF EXISTS `oa_basic_user`;
CREATE TABLE `oa_basic_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `types` varchar(100) NOT NULL DEFAULT '' COMMENT '数据类型:1职务,2职级,3看后期增加',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '人事模块常规数据';

-- ----------------------------
-- Records of oa_basic_user
-- ----------------------------
INSERT INTO `oa_basic_user` VALUES (1, '2', '初级', 1, 1733384661, 0);
INSERT INTO `oa_basic_user` VALUES (2, '2', '中级', 1, 1733384671, 0);
INSERT INTO `oa_basic_user` VALUES (3, '2', '高级', 1, 1733384675, 0);
INSERT INTO `oa_basic_user` VALUES (4, '1', '总经理', 1, 1733384651, 0);
INSERT INTO `oa_basic_user` VALUES (5, '1', '工程师', 1, 1733384651, 0);

-- ----------------------------
-- Table structure for oa_care_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_care_cate`;
CREATE TABLE `oa_care_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '关怀项目名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '关怀项目';

-- ----------------------------
-- Records of oa_care_cate
-- ----------------------------
INSERT INTO `oa_care_cate` VALUES (1, '礼品', 1, 1637987189, 0);
INSERT INTO `oa_care_cate` VALUES (2, '节日', 1, 1637987199, 0);
INSERT INTO `oa_care_cate` VALUES (3, '生日', 1, 1638088518, 0);
INSERT INTO `oa_care_cate` VALUES (4, '其他', 1, 1637987199, 0);

-- ----------------------------
-- Table structure for oa_care
-- ----------------------------
DROP TABLE IF EXISTS `oa_care`;
CREATE TABLE `oa_care`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL DEFAULT 0 COMMENT '员工ID',
  `care_cate` int(11) NOT NULL DEFAULT 0 COMMENT '关怀项目',
  `care_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '关怀日期',
  `vacation` int(11) NOT NULL DEFAULT 0 COMMENT '休假天数',
  `cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  `thing` varchar(255) NOT NULL DEFAULT '' COMMENT '物品',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:1未执行2已执行',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件',
  `remark` mediumtext  NULL COMMENT '备注说明',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT  '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工关怀表';

-- ----------------------------
-- Table structure for oa_flow_module
-- ----------------------------
DROP TABLE IF EXISTS `oa_flow_module`;
CREATE TABLE `oa_flow_module`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '审批模块名称',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '预设字段，图标',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `department_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '应用部门ID（空为全部）1,2,3',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '审批模块';

-- ----------------------------
-- Records of oa_flow_module
-- ----------------------------
INSERT INTO `oa_flow_module` VALUES (1, '假勤', '', 0, '', 1, 1723277295, 0);
INSERT INTO `oa_flow_module` VALUES (2, '行政', '', 0, '', 1, 1723277311, 0);
INSERT INTO `oa_flow_module` VALUES (3, '业务', '', 0, '', 1, 1723277351, 0);
INSERT INTO `oa_flow_module` VALUES (4, '财务', '', 0, '', 1, 1723277356, 0);
INSERT INTO `oa_flow_module` VALUES (5, '人事', '', 0, '', 1, 1723277368, 0);
INSERT INTO `oa_flow_module` VALUES (6, '其他', '', 0, '', 1, 1723277374, 0);

-- ----------------------------
-- Table structure for oa_flow_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_flow_cate`;
CREATE TABLE `oa_flow_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '审批类型名称',
  `name` varchar(100) NOT NULL COMMENT '审批类型标识,唯一',
  `module_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联审批模块id',
  `check_table` varchar(100) NOT NULL DEFAULT '' COMMENT '关联数据库表名',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `department_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '应用部门ID（空为全部）1,2,3',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `is_copy` int(11) NOT NULL DEFAULT 1 COMMENT '是否支持抄送人',
  `is_file` int(11) NOT NULL DEFAULT 0 COMMENT '审批过程是否支持上传附件',
  `is_export` int(11) NOT NULL DEFAULT 0 COMMENT '审批通过后是否支持导出PDF打印',
  `is_back` int(11) NOT NULL DEFAULT 1 COMMENT '是否支持撤回',
  `is_reversed` int(11) NOT NULL DEFAULT 0 COMMENT '是否支持反确认',
  `form` tinyint(1) NOT NULL DEFAULT 1 COMMENT '预设字段，表单模式：1固定表单,2自定义表单',
  `add_url` varchar(255) NOT NULL DEFAULT '' COMMENT '新建链接：固定表单模式必填',
  `view_url` varchar(255) NOT NULL DEFAULT '' COMMENT '查看链接：固定表单模式必填',
  `form_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '表单id：自定义表单模式必填',
  `is_list` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否列表页显示：0不显示 1显示',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `template_id` int(11) NOT NULL DEFAULT 0 COMMENT '审批消息模板id',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '审批类型';

-- ----------------------------
-- Records of oa_flow_cate
-- ----------------------------
INSERT INTO `oa_flow_cate` VALUES (1, '请假', 'leaves', 1, 'leaves', 'icon-kechengziyuanguanli', '', 0, 1, 0, 0, 1, 0, 1, '/home/leaves/add', '/home/leaves/view', 0, 1, 1, 2, 1723604674, 0);
INSERT INTO `oa_flow_cate` VALUES (2, '出差', 'trips', 1, 'trips', 'icon-jiaoshiguanli', '', 0, 1, 0, 0, 1, 0, 1, '/home/trips/add', '/home/trips/view', 0, 1, 1, 3, 1723799422, 0);
INSERT INTO `oa_flow_cate` VALUES (3, '外出', 'outs', 1, 'outs', 'icon-tuiguangguanli', '', 0, 1, 0, 0, 1, 0, 1, '/home/outs/add', '/home/outs/view', 0, 1, 1, 4, 1723800336, 0);
INSERT INTO `oa_flow_cate` VALUES (4, '加班', 'overtimes', 1, 'overtimes', 'icon-xueshengchengji', '', 0, 1, 0, 0, 1, 0, 1, '/home/overtimes/add', '/home/overtimes/view', 0, 1, 1, 5, 1723800393, 0);
INSERT INTO `oa_flow_cate` VALUES (5, '用章', 'seal', 2, 'seal', 'icon-shenpishezhi', '', 0, 1, 0, 0, 1, 0, 1, '/adm/seal/add', '/adm/seal/view', 0, 1, 1, 6, 1723469451, 0);
INSERT INTO `oa_flow_cate` VALUES (6, '公文', 'official_docs', 2, 'official_docs', 'icon-lunwenguanli', '', 0, 1, 0, 0, 1, 0, 1, '/adm/official/add', '/adm/official/view', 0, 0, 1, 7, 1723469614, 0);
INSERT INTO `oa_flow_cate` VALUES (7, '报销', 'expense', 4, 'expense', 'icon-jizhang', '', 0, 1, 0, 0, 1, 0, 1, '/finance/expense/add', '/finance/expense/view', 0, 1, 1, 8, 1723469732, 0);
INSERT INTO `oa_flow_cate` VALUES (8, '发票', 'invoice', 4, 'invoice', 'icon-duizhangdan', '', 0, 1, 0, 0, 1, 0, 1, '/finance/invoice/add', '/finance/invoice/view', 0, 1, 1, 9, 1723469814, 0);
INSERT INTO `oa_flow_cate` VALUES (9, '收票', 'ticket', 4, 'ticket', 'icon-yingjiaoqingdan', '', 0, 1, 0, 0, 1, 0, 1, '/finance/ticket/add', '/finance/ticket/view', 0, 1, 1, 10, 1724749856, 0);
INSERT INTO `oa_flow_cate` VALUES (10, '无发票回款', 'invoicea', 4, 'invoice', 'icon-shoufeipeizhi', '', 0, 1, 0, 0, 1, 0, 1, '/finance/invoice/add_a', '/finance/invoice/view_a', 0, 1, 1, 11, 1725856435, 0);
INSERT INTO `oa_flow_cate` VALUES (11, '无发票付款', 'ticketa', 4, 'ticket', 'icon-bulujiesuan', '', 0, 1, 0, 0, 1, 0, 1, '/finance/ticket/add_a', '/finance/ticket/view_a', 0, 1, 1, 12, 1725856613, 0);
INSERT INTO `oa_flow_cate` VALUES (12, '销售合同', 'contract', 3, 'contract', 'icon-hetongguanli', '', 0, 0, 0, 0, 1, 0, 1, '/contract/contract/add', '/contract/contract/view', 0, 1, 1, 13, 1723469917, 0);
INSERT INTO `oa_flow_cate` VALUES (13, '采购合同', 'purchase', 3, 'purchase', 'icon-dianshang', '', 0, 0, 0, 0, 1, 0, 1, '/contract/purchase/add', '/contract/purchase/view', 0, 1, 1, 14, 1723470017, 0);
INSERT INTO `oa_flow_cate` VALUES (14, '借支', 'loan', 4, 'loan', 'icon-zhangbuguanli', '', 0, 1, 0, 1, 1, 0, 1, '/finance/loan/add', '/finance/loan/view', 0, 1, 1, 15, 1723470017, 0);
INSERT INTO `oa_flow_cate` VALUES (15, '入职', 'talent', 5, 'talent', 'icon-yuangongdaoru', '', 0, 1, 0, 1, 1, 0, 1, '/user/talent/add', '/user/talent/view', 0, 1, 1, 16, 1729490152, 0);
INSERT INTO `oa_flow_cate` VALUES (16, '离职', 'personal_quit', 5, 'personal_quit', 'icon-yuangongtongji2', '', 0, 1, 0, 1, 1, 0, 1, '/user/personal/leave_add', '/user/personal/leave_view', 0, 1, 1, 17, 1729490152, 0);
INSERT INTO `oa_flow_cate` VALUES (17, '人事调动', 'department_change', 5, 'department_change', 'icon-yuangongbiandong', '', 0, 1, 0, 1, 1, 0, 1, '/user/personal/change_add', '/user/personal/change_view', 0, 1, 1, 18, 1729490152, 0);
INSERT INTO `oa_flow_cate` VALUES (18, '会议室预定审批', 'meeting_order', 2, 'meeting_order', 'icon-xuetangguanli', '', 0, 1, 0, 1, 1, 0, 1, '/adm/meeting/add', '/adm/meeting/view', 0, 1, 1, 19, 1758544152, 1758582877);


-- ----------------------------
-- Table structure for oa_flow
-- ----------------------------
DROP TABLE IF EXISTS `oa_flow`;
CREATE TABLE `oa_flow`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '审批流程名称',
  `cate_id` tinyint(11) NOT NULL DEFAULT 0 COMMENT '关联审批类型id',
  `check_type` tinyint(4) NOT NULL COMMENT '1自由审批流,2固定审批流,3固定可回退的审批流,4固定条件审批流',
  `department_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '应用部门ID（0为全部）1,2,3',
  `copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID',
  `flow_list` varchar(1000) NULL DEFAULT '' COMMENT '流程数据序列化',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 1启用，0禁用',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '流程说明',
  `admin_id` int(11) NOT NULL COMMENT '创建人ID',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '审批流程表';

-- ----------------------------
-- Records of oa_flow_cate
-- ----------------------------
INSERT INTO `oa_flow` VALUES (1, '请假审批', 1, 1, '', '', '', 1, '', 1, 1723791655, 0, 0);
INSERT INTO `oa_flow` VALUES (2, '出差审批', 2, 1, '', '', '', 1, '', 1, 1723799665, 0, 0);
INSERT INTO `oa_flow` VALUES (3, '外出审批', 3, 1, '', '', '', 1, '', 1, 1723800434, 0, 0);
INSERT INTO `oa_flow` VALUES (4, '加班审批', 4, 1, '', '', '', 1, '', 1, 1723800446, 0, 0);
INSERT INTO `oa_flow` VALUES (5, '用章审批', 5, 1, '', '', '', 1, '', 1, 1723470400, 0, 0);
INSERT INTO `oa_flow` VALUES (6, '公文审批', 6, 1, '', '', '', 1, '', 1, 1723470419, 0, 0);
INSERT INTO `oa_flow` VALUES (7, '报销审批', 7, 1, '', '', '', 1, '', 1, 1723470468, 0, 0);
INSERT INTO `oa_flow` VALUES (8, '发票审批', 8, 1, '', '', '', 1, '', 1, 1723470482, 0, 0);
INSERT INTO `oa_flow` VALUES (9, '收票审批', 9, 1, '', '', '', 1, '', 1, 1723470482, 0, 0);
INSERT INTO `oa_flow` VALUES (10, '无发票回款', 10, 1, '', '', '', 1, '', 1, 1725935073, 0, 0);
INSERT INTO `oa_flow` VALUES (11, '无发票付款', 11, 1, '', '', '', 1, '', 1, 1725935159, 0, 0);
INSERT INTO `oa_flow` VALUES (12, '销售合同审批', 12, 1, '', '', '', 1, '', 1, 1723470490, 0, 0);
INSERT INTO `oa_flow` VALUES (13, '采购合同审批', 13, 1, '', '', '', 1, '', 1, 1723470501, 0, 0);
INSERT INTO `oa_flow` VALUES (14, '借支审批', 14, 1, '', '', '', 1, '', 1, 1723470501, 0, 0);
INSERT INTO `oa_flow` VALUES (15, '入职审批', 15, 1, '', '', '', 1, '', 1, 1723470501, 0, 0);
INSERT INTO `oa_flow` VALUES (16, '离职审批', 16, 1, '', '', '', 1, '', 1, 1723470501, 0, 0);
INSERT INTO `oa_flow` VALUES (17, '离职审批', 17, 1, '', '', '', 1, '', 1, 1723470501, 0, 0);
INSERT INTO `oa_flow` VALUES (18, '会议室预定审批', 18, 1, '', '', '', 1, '', 1, 1758550919, 0, 0);

-- ----------------------------
-- Table structure for oa_flow_step
-- ----------------------------
DROP TABLE IF EXISTS `oa_flow_step`;
CREATE TABLE `oa_flow_step`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_name` varchar(255) NOT NULL DEFAULT '' COMMENT '流程步骤名称',
  `action_id` int(11) NOT NULL COMMENT '审批内容ID',
  `flow_id` int(11) NOT NULL COMMENT '审批流程id',
  `check_role` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审批角色:0自由指定,1当前部门负责人,2上一级部门负责人,3指定职位,4指定用户,5可回退审批',
  `check_position_id` int(11) NOT NULL DEFAULT 0 COMMENT '审批角色id',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '审批人ids(1,2,3)',
  `check_types` tinyint(4) NOT NULL DEFAULT 1 COMMENT '审批方式:1会签2或签',
  `sort` tinyint(4) NOT NULL DEFAULT 0 COMMENT '排序ID',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '审批步骤表';

-- ----------------------------
-- Table structure for oa_flow_record
-- ----------------------------
DROP TABLE IF EXISTS `oa_flow_record`;
CREATE TABLE `oa_flow_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_id` int(11) NOT NULL DEFAULT 0 COMMENT '审批内容ID',
  `check_table` varchar(255) NOT NULL DEFAULT '审批数据表',
  `flow_id` int(11) NOT NULL COMMENT '审批模版流程id',
  `step_id` int(11) NOT NULL DEFAULT 0 COMMENT '审批步骤ID',
  `check_files` varchar(500) NOT NULL DEFAULT '' COMMENT '审批附件',
  `check_uid` int(11) NOT NULL DEFAULT 0 COMMENT '审批人ID',
  `check_time` bigint(11) NOT NULL COMMENT '审批时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审批状态:0发起,1通过,2拒绝,3撤销',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT '审批意见',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '审批记录表';

-- ----------------------------
-- Table structure for oa_cost_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_cost_cate`;
CREATE TABLE `oa_cost_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '费用类型名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '费用类型';

-- ----------------------------
-- Records of oa_cost_cate
-- ----------------------------
INSERT INTO `oa_cost_cate` VALUES (1, '差旅费', 1, 1639898199, 0,0);
INSERT INTO `oa_cost_cate` VALUES (2, '办公费', 1, 1639898434, 0,0);
INSERT INTO `oa_cost_cate` VALUES (3, '招待费', 1, 1639898564, 0,0);
INSERT INTO `oa_cost_cate` VALUES (4, '交通费', 1, 1639898564, 0,0);
INSERT INTO `oa_cost_cate` VALUES (5, '通讯费', 1, 1639898564, 0,0);
INSERT INTO `oa_cost_cate` VALUES (6, '其他', 1, 1639898564, 0,0);

-- ----------------------------
-- Table structure for oa_seal_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_seal_cate`;
CREATE TABLE `oa_seal_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '印章名称',
  `dids` varchar(255) NOT NULL DEFAULT '' COMMENT '应用部门',
  `keep_uid` int(11) NOT NULL DEFAULT 0 COMMENT '保管人',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '用途简述',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '印章类型';

INSERT INTO `oa_seal_cate` VALUES (1, '合同章','', 2, 1, '', 1733385406, 0);

-- ----------------------------
-- Table structure for oa_meeting_room
-- ----------------------------
DROP TABLE IF EXISTS `oa_meeting_room`;
CREATE TABLE `oa_meeting_room`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '会议室名称',
  `keep_uid` int(11) NOT NULL DEFAULT 0 COMMENT '会议室管理员',
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '地址楼层',
  `device` varchar(255) NOT NULL DEFAULT '' COMMENT '会议室设备',
  `num` int(10) NOT NULL DEFAULT 10 COMMENT '可容纳人数',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '会议室描述',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '会议室';

-- ----------------------------
-- Table structure for oa_meeting_records
-- ----------------------------
DROP TABLE IF EXISTS `oa_meeting_records`;
CREATE TABLE `oa_meeting_records`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(225) NULL DEFAULT NULL COMMENT '会议主题',
  `meeting_date` int(11) NOT NULL DEFAULT 0 COMMENT '会议时间',
  `room_id` int(11) NOT NULL DEFAULT 0 COMMENT '会议室',
  `did` int(11) NOT NULL DEFAULT 0 COMMENT '主办部门',
  `content` text NOT NULL COMMENT '会议内容',
  `plans` text NOT NULL COMMENT '下一步实施计划',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件',
  `join_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '与会人员',
  `sign_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '签到人员',
  `share_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '共享给谁',
  `anchor_id` int(11) NOT NULL DEFAULT 0 COMMENT '主持人id',
  `recorder_id` int(11) NOT NULL DEFAULT 0 COMMENT '记录人id',
  `remarks` text NOT NULL COMMENT '备注内容',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '发布人id',
  `create_time` bigint(11) NOT NULL DEFAULT 0,
  `update_time` bigint(11) NOT NULL DEFAULT 0,
  `delete_time` bigint(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '会议纪要';

-- ----------------------------
-- Table structure for oa_car
-- ----------------------------
DROP TABLE IF EXISTS `oa_car`;
CREATE TABLE `oa_car`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '车辆名称',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '车辆牌号',
  `oil` varchar(100) NOT NULL DEFAULT '' COMMENT '油耗',
  `mileage` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '开始里程数',
  `seats` int(11) NOT NULL DEFAULT 5 COMMENT '座位数',
  `color` varchar(100) NOT NULL DEFAULT '' COMMENT '车身颜色',
  `vin` varchar(100) NOT NULL DEFAULT '' COMMENT '车架号',
  `engine` varchar(100) NOT NULL DEFAULT '' COMMENT '发动机号',
  `buy_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '购买日期',
  `price` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '购买价格',
  `thumb` int(11) NOT NULL DEFAULT 5 COMMENT '车辆照片',
  `types` int(11) NOT NULL DEFAULT 5 COMMENT '车辆类型:1轿车,2面包车,3越野车,4吉普车,5巴士,6工具车,7卡车,8其他',
  `driver` int(11) NOT NULL DEFAULT 0 COMMENT '驾驶员',
  `insure_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '保险到期时间',
  `review_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '年审到期时间',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ID，如:1,2,3',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '当前状态:1可用,停用,维修,报废',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '车辆管理';

-- ----------------------------
-- Table structure for oa_car_repair
-- ----------------------------
DROP TABLE IF EXISTS `oa_car_repair`;
CREATE TABLE `oa_car_repair`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `types` int(1) NOT NULL DEFAULT 1 COMMENT '类型:1维修,2保养',
  `car_id` int(11) NOT NULL DEFAULT 0 COMMENT '车id',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '维修(保养)地点',
  `content` varchar(1000) NULL DEFAULT '' COMMENT '维修(保养)原因&内容',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '维修(保养)金额',
  `handled` int(11) NOT NULL DEFAULT 0 COMMENT '经手人',
  `repair_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '维修(保养)时间',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ID，如:1,2,3',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注信息',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '车辆维修(保养)记录';

-- ----------------------------
-- Table structure for oa_car_mileage
-- ----------------------------
DROP TABLE IF EXISTS `oa_car_mileage`;
CREATE TABLE `oa_car_mileage`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `car_id` int(11) NOT NULL DEFAULT 0 COMMENT '车id',
  `mileage` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '里程数',
  `handled` int(11) NOT NULL DEFAULT 0 COMMENT '经手人',
  `mileage_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '里程月份',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注信息',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '车辆里程明细';

-- ----------------------------
-- Table structure for oa_car_fee
-- ----------------------------
DROP TABLE IF EXISTS `oa_car_fee`;
CREATE TABLE `oa_car_fee`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '费用主题',
  `types` int(1) NOT NULL DEFAULT 1 COMMENT '费用类型id',
  `car_id` int(11) NOT NULL DEFAULT 0 COMMENT '车id',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  `fee_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '费用日期',
  `handled` int(11) NOT NULL DEFAULT 0 COMMENT '经手人',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ID，如:1,2,3',
  `content` varchar(1000) NULL DEFAULT '' COMMENT '费用内容',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '车辆费用明细';

-- ----------------------------
-- Table structure for oa_expense_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_cate`;
CREATE TABLE `oa_expense_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '报销类型名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '报销类型';

-- ----------------------------
-- Records of oa_expense_cate
-- ----------------------------
INSERT INTO `oa_expense_cate` VALUES (1, '交通费', 1, 1637987189, 0,0);
INSERT INTO `oa_expense_cate` VALUES (2, '住宿费', 1, 1637987199, 0,0);
INSERT INTO `oa_expense_cate` VALUES (3, '餐补费', 1, 1638088518, 0,0);
INSERT INTO `oa_expense_cate` VALUES (4, '招待费', 1, 1637987199, 0,0);
INSERT INTO `oa_expense_cate` VALUES (5, '汽油费', 1, 1637987199, 0,0);
INSERT INTO `oa_expense_cate` VALUES (6, '其他费', 1, 1637987199, 0,0);

-- ----------------------------
-- Table structure for oa_loan
-- ----------------------------
DROP TABLE IF EXISTS `oa_loan`;
CREATE TABLE `oa_loan`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `subject_id` int(11)  NOT NULL DEFAULT 0 COMMENT '借支企业主体',
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '借支编码',
  `title` varchar(500) NOT NULL DEFAULT '' COMMENT '借款主题',
  `cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '借支金额',
  `types` tinyint(4) NOT NULL DEFAULT 1 COMMENT '借支类型：1日常备用金,2项目预支款',
  `loan_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预期借支日期',
  `plan_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预计还款日期',
  `content` varchar(1000) NULL DEFAULT '' COMMENT '借支理由',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '借支人',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '借支部门ID',
  `balance_cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '已冲账金额',
  `balance_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '冲账状态 0待冲账,1部分冲账,2已冲账',
  `project_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联项目ID',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ID，如:1,2,3',
  `pay_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '打款状态 0待打款,1已打款',
  `pay_admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '打款人ID',
  `pay_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最后打款时间',
  `back_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '还款状态 0待还款,1已还款',
  `back_admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '还款操作人ID',
  `back_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最后还款时间',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '借支表';

-- ----------------------------
-- Table structure for oa_expense
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense`;
CREATE TABLE `oa_expense`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `subject_id` int(11)  NOT NULL DEFAULT 0 COMMENT '报销企业主体',
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '报销编码',
  `cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '报销总金额',
  `income_month` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '入账月份',
  `expense_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '原始单据日期',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '报销人',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '报销部门ID',
  `loan_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联借支ID',
  `balance_cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '冲账金额',
  `pay_amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '需打款金额',
  `project_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联项目ID',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ID，如:1,2,3',
  `pay_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '打款状态 0待打款,1已打款',
  `pay_admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '打款人ID',
  `pay_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最后打款时间',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '报销表';

-- ----------------------------
-- Table structure for oa_expense_interfix
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_interfix`;
CREATE TABLE `oa_expense_interfix`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `exid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '报销ID',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  `cate_id` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '报销类型ID',
  `remarks` mediumtext  NULL COMMENT '备注',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '登记人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '报销关联数据表';

-- ----------------------------
-- Table structure for oa_invoice
-- ----------------------------
DROP TABLE IF EXISTS `oa_invoice`;
CREATE TABLE `oa_invoice`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '发票号码',
  `customer_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联客户ID',
  `contract_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联合同协议ID',
  `project_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联项目ID',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '发票金额',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票申请部门',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票申请人',
  `open_status` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '开票状态：0未开票 1已开票 2已作废',
  `open_admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票开具人',
  `open_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票开具时间',
  `delivery` varchar(100) NOT NULL DEFAULT '' COMMENT '快递单号',
  `types` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '抬头类型：1企业2个人',
  `invoice_type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '发票类型：1增值税专用发票,2普通发票,3专用发票',
  `invoice_subject` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联发票主体ID',
  `invoice_title` varchar(100) NOT NULL DEFAULT '' COMMENT '开票抬头',
  `invoice_tax` varchar(100) NOT NULL DEFAULT '' COMMENT '纳税人识别号',
  `invoice_phone` varchar(100) NOT NULL DEFAULT '' COMMENT '电话号码',
  `invoice_address` varchar(100) NOT NULL DEFAULT '' COMMENT '地址',
  `invoice_bank` varchar(100) NOT NULL DEFAULT '' COMMENT '开户银行',
  `invoice_account` varchar(100) NOT NULL DEFAULT '' COMMENT '银行账号',
  `invoice_banking` varchar(100) NOT NULL DEFAULT '' COMMENT '银行营业网点',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ID，如:1,2,3',
  `other_file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '其他附件ID，如:1,2,3',
  `remark` mediumtext  NULL COMMENT '备注',
  `enter_amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '已到账金额',
  `enter_status` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '回款状态：0未回款 1部分回款 2全部回款',
  `enter_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '最新回款时间',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '发票表';

-- ----------------------------
-- Table structure for oa_invoice_income
-- ----------------------------
DROP TABLE IF EXISTS `oa_invoice_income`;
CREATE TABLE `oa_invoice_income`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `invoice_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票ID',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '到账金额',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '到账登记人',
  `enter_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '到账时间',
  `remarks` mediumtext  NULL COMMENT '备注',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1正常 6作废',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '发票到账记录表';

-- ----------------------------
-- Table structure for oa_ticket
-- ----------------------------
DROP TABLE IF EXISTS `oa_ticket`;
CREATE TABLE `oa_ticket`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '发票号码',
  `supplier_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联供应商ID',
  `purchase_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联采购合同协议ID',
  `customer_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联客户ID',
  `project_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联项目ID',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '发票金额',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票接受部门',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票接受人',
  `open_status` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '开票状态：1正常 2已作废',
  `open_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票开具时间',
  `invoice_type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '发票类型：1增值税专用发票,2普通发票,3专用发票',
  `invoice_subject` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联发票主体ID',
  `invoice_title` varchar(100) NOT NULL DEFAULT '' COMMENT '开票抬头',
  `invoice_tax` varchar(100) NOT NULL DEFAULT '' COMMENT '纳税人识别号',
  `invoice_phone` varchar(100) NOT NULL DEFAULT '' COMMENT '电话号码',
  `invoice_address` varchar(100) NOT NULL DEFAULT '' COMMENT '地址',
  `invoice_bank` varchar(100) NOT NULL DEFAULT '' COMMENT '开户银行',
  `invoice_account` varchar(100) NOT NULL DEFAULT '' COMMENT '银行账号',
  `invoice_banking` varchar(100) NOT NULL DEFAULT '' COMMENT '银行营业网点',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ID，如:1,2,3',
  `other_file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '其他附件ID，如:1,2,3',
  `remark` mediumtext  NULL COMMENT '备注',
  `pay_amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '已付款金额',
  `cash_type` int(11) UNSIGNED NULL DEFAULT 1 COMMENT '付款类型：1银行,2现金,3支付宝,4微信,5汇票,6支票,7托收,8其他',
  `pay_status` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '付款状态：0未付款 1部分付款 2全部付款',
  `pay_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '最新回款时间',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '收票表';

-- ----------------------------
-- Table structure for oa_ticket_payment
-- ----------------------------
DROP TABLE IF EXISTS `oa_ticket_payment`;
CREATE TABLE `oa_ticket_payment`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ticket_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发票ID',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '付款金额',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '付款登记人',
  `pay_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '到账时间',
  `remarks` mediumtext  NULL COMMENT '备注',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1正常 6作废',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '收票付款记录表';

-- ----------------------------
-- Table structure for oa_third_message
-- ----------------------------
DROP TABLE IF EXISTS `oa_third_message`;
CREATE TABLE `oa_third_message`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `msgtype` varchar(100) NOT NULL DEFAULT '' COMMENT '消息类型,email,weixin,mobile',
  `types` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '接收人类型：1人员,2部门,3岗位,4全部',
  `uids` varchar(500) NOT NULL DEFAULT '' COMMENT '人员ids',
  `dids` varchar(500) NOT NULL DEFAULT '' COMMENT '部门ids',
  `pids` varchar(500) NOT NULL DEFAULT '' COMMENT '岗位ids',
  `content` mediumtext NULL COMMENT '消息内容',
  `send_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '发送日期',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '第三方发消息表';

-- ----------------------------
-- Table structure for oa_message
-- ----------------------------
DROP TABLE IF EXISTS `oa_message`;
CREATE TABLE `oa_message`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '消息主题',
  `template` varchar(100) NOT NULL DEFAULT '' COMMENT '消息模板,默认是空私人消息,其他则在配置文件查看消息模板',
  `content` mediumtext  NULL COMMENT '消息内容',
  `file_ids` mediumtext  NULL COMMENT '消息附件',
  `from_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送人id，0为系统消息',
  `types` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '接收人类型：1人员,2部门,3岗位,4全部',
  `uids` varchar(500) NOT NULL DEFAULT '' COMMENT '人员ids',
  `dids` varchar(500) NOT NULL DEFAULT '' COMMENT '部门ids',
  `pids` varchar(500) NOT NULL DEFAULT '' COMMENT '岗位ids',
  `copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '操送人员ids',
  `msg_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '转发、回复关联消息id',
  `is_draft` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否是草稿：1正常消息 2草稿消息',
  `send_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '发送日期',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `clear_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '清除时间',
  `action_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作模块数据的id（针对系统消息）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '发消息表';

-- ----------------------------
-- Table structure for oa_msg
-- ----------------------------
DROP TABLE IF EXISTS `oa_msg`;
CREATE TABLE `oa_msg`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '消息主题',
  `template` varchar(100) NOT NULL DEFAULT '' COMMENT '消息模板,默认是私人文本消息,其他则在配置文件查看消息模板',
  `content` mediumtext  NULL COMMENT '消息内容',
  `file_ids` mediumtext  NULL COMMENT '消息附件',
  `from_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送人id，0为系统消息',
  `to_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '接收人id',
  `message_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '来源发件消息id,0为系统消息',
  `msg_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '转发、回复关联消息id',
  `is_star` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否星标信息：1是 0不是',
  `read_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '阅读时间',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `clear_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '清除时间',
  `action_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作模块数据的id（针对系统消息）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '消息表';

-- ----------------------------
-- Table structure for oa_note_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_note_cate`;
CREATE TABLE `oa_note_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT 0 COMMENT '父类ID',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '排序',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1可用-1禁用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '公告分类';

-- ----------------------------
-- Records of oa_note_cate
-- ----------------------------
INSERT INTO `oa_note_cate` VALUES (1, 0, 1, '普通公告',1, 1637984265, 1637984299,0);
INSERT INTO `oa_note_cate` VALUES (2, 0, 2, '紧急公告',1, 1637984283, 1637984310,0);

-- ----------------------------
-- Table structure for oa_note
-- ----------------------------
DROP TABLE IF EXISTS `oa_note`;
CREATE TABLE `oa_note`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `cate_id` int(11) NOT NULL DEFAULT 0 COMMENT '公告分类ID',
  `sourse` tinyint(1) NOT NULL DEFAULT 1 COMMENT '发布平台:1PC,2手机',
  `title` varchar(225) NULL DEFAULT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '公告内容',
  `src` varchar(100) NULL DEFAULT NULL COMMENT '关联链接',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1可用-1禁用',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件',
  `role_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '查看权限，0所有人,1部门,2人员',
  `role_dids` varchar(500) NOT NULL DEFAULT '' COMMENT '可查看部门',
  `role_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '可查看用户',
  `start_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '展示开始时间',
  `end_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '展示结束时间',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '发布人id',
  `create_time` bigint(11) NOT NULL DEFAULT 0,
  `update_time` bigint(11) NOT NULL DEFAULT 0,
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '公告表';

-- ----------------------------
-- Records of oa_note
-- ----------------------------
INSERT INTO `oa_note` VALUES (1, 1,1, '欢迎使用勾股OA办公系统', '<p>欢迎使用勾股OA办公系统，勾股办公是一款简单实用的开源免费的企业办公系统。系统集成了系统设置、人事管理、行政管理、消息管理、日常办公、财务管理、客户管理、项目管理、合同管理、知识管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，帮助开发者简单高效降低二次开发成本，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。</p>', 'https://oa.gougucms.com', 1, 2,'',1,'','', 1635696000, 1924876800,1, 1637984962, 1637984975,0);
INSERT INTO `oa_note` VALUES (2, 1,1, '勾股OA支持定制开发', '<p>欢迎使用勾股OA办公系统，勾股办公是一款简单实用的开源免费的企业办公系统。系统集成了系统设置、人事管理、行政管理、消息管理、日常办公、财务管理、客户管理、项目管理、合同管理、知识管理等基础模块。</p><p>勾股OA开源发布，同时我们也支持功能定制开发，价格优惠，定制开发系统功能更贴近自身需求，欢迎够沟通合作。</p><p>合作联系微信号“hdm588”，业务合作、功能定制加微信时请备注。</p>', 'https://oa.gougucms.com', 1, 2,'',1,'','', 1635696000, 1924876800,1, 1637984962, 1637984975,0);
INSERT INTO `oa_note` VALUES (3, 1,1, '勾股DEV——研发管理与团队协作的工具', '<p>勾股DEV是一款专为IT行业研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。</p><p>项目体验地址：https://www.gougucms.com/home/pages/detail/s/gougudev.html</p><p>项目开源地址：https://gitee.com/gouguopen/dev</p><p>勾股DEV开源发布，同时我们也支持功能定制开发，价格优惠，定制开发系统功能更贴近自身需求，欢迎够沟通合作。</p><p>合作联系微信号“hdm588”，业务合作、功能定制加微信时请备注。</p>', 'https://dev.gougucms.com', 1, 2,'',1,'','', 1635696000, 1924876800,1, 1637984962, 1637984975,0);

-- ----------------------------
-- Table structure for oa_position
-- ----------------------------
DROP TABLE IF EXISTS `oa_position`;
CREATE TABLE `oa_position`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '岗位名称',
  `work_price` int(10) NOT NULL DEFAULT 0 COMMENT '工时单价',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '岗位职称';

-- ----------------------------
-- Records of oa_position
-- ----------------------------
INSERT INTO `oa_position` VALUES (1, '超级岗位', 1000, '超级岗位，不能轻易修改权限', 1, 0, 0);
INSERT INTO `oa_position` VALUES (2, '人事总监', 1000, '人事部的最大领导', 1, 0, 0);
INSERT INTO `oa_position` VALUES (3, '普通员工', 500, '普通员工', 1, 0, 0);

-- ----------------------------
-- Table structure for oa_position_group
-- ----------------------------
DROP TABLE IF EXISTS `oa_position_group`;
CREATE TABLE `oa_position_group`  (
  `pid` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '岗位id',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  UNIQUE INDEX `pid_group_id`(`pid`, `group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '权限分组和岗位的关联表';

-- ----------------------------
-- Records of oa_position_group
-- ----------------------------
INSERT INTO `oa_position_group` VALUES (1, 1, 1635755739, 0);
INSERT INTO `oa_position_group` VALUES (2, 2, 1638007427, 0);
INSERT INTO `oa_position_group` VALUES (3, 3, 1638007427, 0);

-- ----------------------------
-- Table structure for oa_plan
-- ----------------------------
DROP TABLE IF EXISTS `oa_plan`;
CREATE TABLE `oa_plan`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '工作安排主题',
  `type` varchar(100) NOT NULL DEFAULT '' COMMENT '日程优先级',
  `cid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预设字段:关联工作内容类型ID',
  `cmid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预设字段:关联客户ID',
  `ptid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预设字段:关联项目ID',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联创建员工ID',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属部门',
  `start_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '开始时间',
  `end_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '结束时间',
  `remind_type` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '提醒类型',
  `remind_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '提醒时间',
  `remark` text NOT NULL COMMENT '描述',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '日程安排';

-- ----------------------------
-- Table structure for oa_schedule
-- ----------------------------
DROP TABLE IF EXISTS `oa_schedule`;
CREATE TABLE `oa_schedule`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '工作记录主题',
  `cid` int(11) UNSIGNED NOT NULL DEFAULT 1 COMMENT '预设字段:关联工作内容类型ID',
  `cmid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预设字段:关联客户ID',
  `ptid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预设字段:关联项目ID',
  `tid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预设字段:关联任务ID',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联创建员工ID',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属部门',
  `start_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '开始时间',
  `end_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '结束时间',
  `labor_time` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '工时',
  `labor_type` int(1) NOT NULL DEFAULT 0 COMMENT '工作类型:1案头2外勤',
  `remark` text NOT NULL COMMENT '描述',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '工作记录';

-- ----------------------------
-- Table structure for oa_approve
-- ----------------------------
DROP TABLE IF EXISTS `oa_approve`;
CREATE TABLE `oa_approve`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '审批标识',
  `content` text NOT NULL COMMENT '内容/说明/理由',
  `types` varchar(255) NOT NULL DEFAULT '' COMMENT '审批类型',  
  `str_1` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串1',
  `str_2` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串2',
  `str_3` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串3',
  `str_4` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串4',
  `str_5` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串5',
  `str_6` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串6',
  `str_7` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串7',
  `str_8` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串8',
  `str_9` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串9',
  `str_10` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串10',
  `str_11` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串11',
  `str_12` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串12',
  `str_13` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串13',
  `str_14` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串14',
  `str_15` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串15',
  `str_16` varchar(255) NOT NULL DEFAULT '' COMMENT '字符串16',
  `int_1` int(11) NOT NULL DEFAULT 0 COMMENT '整数1',
  `int_2` int(11) NOT NULL DEFAULT 0 COMMENT '整数2',
  `int_3` int(11) NOT NULL DEFAULT 0 COMMENT '整数3',
  `int_4` int(11) NOT NULL DEFAULT 0 COMMENT '整数4',
  `int_5` int(11) NOT NULL DEFAULT 0 COMMENT '整数5',
  `int_6` int(11) NOT NULL DEFAULT 0 COMMENT '整数6',
  `int_7` int(11) NOT NULL DEFAULT 0 COMMENT '整数7',
  `int_8` int(11) NOT NULL DEFAULT 0 COMMENT '整数8',
  `int_9` int(11) NOT NULL DEFAULT 0 COMMENT '整数9',
  `int_10` int(11) NOT NULL DEFAULT 0 COMMENT '整数10',
  `float_1` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数1(2位小数)',
  `float_2` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数2(2位小数)',
  `float_3` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数3(2位小数)',
  `float_4` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数4(2位小数)',
  `float_5` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数5(2位小数)',
  `float_6` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数6(2位小数)',
  `float_7` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数7(2位小数)',
  `float_8` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数8(2位小数)',
  `float_9` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数9(2位小数)',
  `float_10` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '浮点数10(2位小数)',  
  `time_1` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间1',
  `time_2` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间2',
  `time_3` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间3',
  `time_4` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间4',
  `time_5` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间5',
  `time_6` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间6',
  `time_7` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间7',
  `time_8` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间8',
  `time_9` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间9',
  `time_10` bigint(11) NOT NULL DEFAULT 0 COMMENT '时间10',
  `file_1` varchar(500) NOT NULL DEFAULT '' COMMENT '附件1，如:1,2,3',
  `file_2` varchar(500) NOT NULL DEFAULT '' COMMENT '附件2，如:1,2,3',
  `file_3` varchar(500) NOT NULL DEFAULT '' COMMENT '附件3，如:1,2,3',
  `file_4` varchar(500) NOT NULL DEFAULT '' COMMENT '附件4，如:1,2,3',
  `file_5` varchar(500) NOT NULL DEFAULT '' COMMENT '附件5，如:1,2,3',
  `file_6` varchar(500) NOT NULL DEFAULT '' COMMENT '附件6，如:1,2,3',
  `text_1` text NOT NULL COMMENT '文本1',
  `text_2` text NOT NULL COMMENT '文本2',
  `text_3` text NOT NULL COMMENT '文本3',
  `text_4` text NOT NULL COMMENT '文本4',
  `text_5` text NOT NULL COMMENT '文本5',
  `text_6` text NOT NULL COMMENT '文本6',  
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `admin_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建人ID',
  `did` int(11) NOT NULL DEFAULT '0' COMMENT '创建人部门ID',
  `create_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '日常审批表';

-- ----------------------------
-- Table structure for oa_attendance
-- ----------------------------
DROP TABLE IF EXISTS `oa_attendance`;
CREATE TABLE `oa_attendance`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL COMMENT '员工ID',
  `did` int(11) NOT NULL COMMENT '创建人部门ID',
  `day_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '考勤日期',
  `time_in_1` bigint(11) NOT NULL DEFAULT '0' COMMENT '第一次打卡上班时间',
  `time_in_2` bigint(11) NOT NULL DEFAULT '0' COMMENT '第二次打卡上班时间',
  `time_out_1` bigint(11) NOT NULL DEFAULT '0' COMMENT '第一次打卡下班时间',
  `time_out_2` bigint(11) NOT NULL DEFAULT '0' COMMENT '第二次打卡下班时间',
  `late_min` int(10) NOT NULL DEFAULT '0' COMMENT '迟到分钟数',
  `leave_min` int(10) NOT NULL DEFAULT '0' COMMENT '早退分钟数',
  `create_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '日常考勤表';

-- ----------------------------
-- Table structure for oa_leaves
-- ----------------------------
DROP TABLE IF EXISTS `oa_leaves`;
CREATE TABLE `oa_leaves`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `types` int(2) NOT NULL DEFAULT '0' COMMENT '请假类型:1事假,2年假,3调休假,4病假,5婚假,6丧假,7产假,8陪产假,9其他',
  `start_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '开始日期',
  `end_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '结束日期',
  `start_span` int(10) NOT NULL DEFAULT '0' COMMENT '时间段:1上午,2下午',
  `end_span` int(10) NOT NULL DEFAULT '0' COMMENT '时间段:1上午,2下午',
  `duration` decimal(10, 1) NOT NULL DEFAULT 0.0 COMMENT '时长(工作日)',
  `reason` text NOT NULL COMMENT '请假原因',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件，如:1,2,3',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `admin_id` int(11) NOT NULL COMMENT '创建人ID',
  `did` int(11) NOT NULL COMMENT '创建人部门ID',
  `create_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '请假表';

-- ----------------------------
-- Table structure for oa_trips
-- ----------------------------
DROP TABLE IF EXISTS `oa_trips`;
CREATE TABLE `oa_trips`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '开始日期',
  `end_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '结束日期',
  `start_span` int(10) NOT NULL DEFAULT '0' COMMENT '时间段:1上午,2下午',
  `end_span` int(10) NOT NULL DEFAULT '0' COMMENT '时间段:1上午,2下午',
  `duration` decimal(10, 1) NOT NULL DEFAULT 0.0 COMMENT '时长(工作日)',
  `reason` text NOT NULL COMMENT '出差原因',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件，如:1,2,3',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `admin_id` int(11) NOT NULL COMMENT '创建人ID',
  `did` int(11) NOT NULL COMMENT '创建人部门ID',
  `create_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '出差表';

-- ----------------------------
-- Table structure for oa_outs
-- ----------------------------
DROP TABLE IF EXISTS `oa_outs`;
CREATE TABLE `oa_outs`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '开始日期',
  `end_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '结束日期',
  `start_span` int(10) NOT NULL DEFAULT '0' COMMENT '时间段:1上午,2下午',
  `end_span` int(10) NOT NULL DEFAULT '0' COMMENT '时间段:1上午,2下午',
  `duration` decimal(10, 1) NOT NULL DEFAULT 0.0 COMMENT '时长(工作日)',
  `reason` text NOT NULL COMMENT '出差原因',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件，如:1,2,3',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `admin_id` int(11) NOT NULL COMMENT '创建人ID',
  `did` int(11) NOT NULL COMMENT '创建人部门ID',
  `create_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '外出表';

-- ----------------------------
-- Table structure for oa_overtimes
-- ----------------------------
DROP TABLE IF EXISTS `oa_overtimes`;
CREATE TABLE `oa_overtimes`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '开始日期',
  `end_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '结束日期',
  `start_span` int(10) NOT NULL DEFAULT '0' COMMENT '时间段:1上午,2下午',
  `end_span` int(10) NOT NULL DEFAULT '0' COMMENT '时间段:1上午,2下午',
  `duration` decimal(10, 1) NOT NULL DEFAULT 0.0 COMMENT '时长(工作日)',
  `reason` text NOT NULL COMMENT '出差原因',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件，如:1,2,3',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `admin_id` int(11) NOT NULL COMMENT '创建人ID',
  `did` int(11) NOT NULL COMMENT '创建人部门ID',
  `create_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '加班表';

-- ----------------------------
-- Table structure for oa_work
-- ----------------------------
DROP TABLE IF EXISTS `oa_work`;
CREATE TABLE `oa_work`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `types` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '类型：1 日报 2周报 3月报',
  `start_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '开始日期',
  `end_date` bigint(11) NOT NULL DEFAULT '0' COMMENT '结束日期',
  `to_uids` mediumtext  NULL COMMENT '接受人员ID',
  `works` mediumtext  NULL COMMENT '汇报工作内容',
  `plans` mediumtext  NULL COMMENT '计划工作内容',
  `remark` mediumtext  NULL COMMENT '其他事项',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件，如:1,2,3',
  `send_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '发送时间',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人id',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '汇报工作表';

-- ----------------------------
-- Table structure for oa_work_record
-- ----------------------------
DROP TABLE IF EXISTS `oa_work_record`;
CREATE TABLE `oa_work_record`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `work_id` int(11) UNSIGNED NOT NULL COMMENT '汇报工作id',
  `from_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送人id',
  `to_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '接收人id',
  `send_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '发送日期',
  `read_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '阅读时间',
  `delete_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '汇报工作发送记录表';

-- ----------------------------
-- Table structure for oa_customer_grade
-- ----------------------------
DROP TABLE IF EXISTS `oa_customer_grade`;
CREATE TABLE `oa_customer_grade`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '客户等级名称',
  `sort` int(01) NOT NULL DEFAULT 0 COMMENT '排序，越大越靠前',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态: 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '客户等级';

-- ----------------------------
-- Records of oa_customer_grade
-- ----------------------------
INSERT INTO `oa_customer_grade` VALUES (1, '普通客户',0, 1, 1637987189, 0, 0);
INSERT INTO `oa_customer_grade` VALUES (2, 'VIP客户',0, 1, 1637987199, 0, 0);

-- ----------------------------
-- Table structure for oa_customer_source
-- ----------------------------
DROP TABLE IF EXISTS `oa_customer_source`;
CREATE TABLE `oa_customer_source`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '客户渠道名称',
  `sort` int(01) NOT NULL DEFAULT 0 COMMENT '排序，越大越靠前',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '客户来源';

-- ----------------------------
-- Records of oa_customer_source
-- ----------------------------
INSERT INTO `oa_customer_source` VALUES (1, '独立开发',0, 1, 1637987189, 0, 0);
INSERT INTO `oa_customer_source` VALUES (2, '微信公众号',0, 1, 1637987199, 0, 0);
INSERT INTO `oa_customer_source` VALUES (3, '今日头条',0, 1, 1637987199, 0, 0);
INSERT INTO `oa_customer_source` VALUES (4, '百度搜索',0, 1, 1637987199, 0, 0);
INSERT INTO `oa_customer_source` VALUES (5, '销售活动',0, 1, 1637987199, 0, 0);
INSERT INTO `oa_customer_source` VALUES (6, '电话来访',0, 1, 1637987199, 0, 0);
INSERT INTO `oa_customer_source` VALUES (7, '客户介绍',0, 1, 1637987199, 0, 0);
INSERT INTO `oa_customer_source` VALUES (8, '其他来源',0, 1, 1637987199, 0, 0);

-- ----------------------------
-- Table structure for oa_basic_customer
-- ----------------------------
DROP TABLE IF EXISTS `oa_basic_customer`;
CREATE TABLE `oa_basic_customer`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `types` varchar(100) NOT NULL DEFAULT '' COMMENT '数据类型:1客户状态,2客户意向,3跟进方式,4销售阶段，5看后期增加',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '客户常规数据';

-- ----------------------------
-- Records of oa_basic_customer
-- ----------------------------
INSERT INTO `oa_basic_customer` VALUES (1, '1', '新进客户', 1, 1732606239, 0);
INSERT INTO `oa_basic_customer` VALUES (2, '1', '跟进客户', 1, 1732606269, 0);
INSERT INTO `oa_basic_customer` VALUES (3, '1', '正式客户', 1, 1732606279, 0);
INSERT INTO `oa_basic_customer` VALUES (4, '1', '流失客户', 1, 1732606303, 0);
INSERT INTO `oa_basic_customer` VALUES (5, '1', '成交客户', 1, 1732606311, 0);
INSERT INTO `oa_basic_customer` VALUES (6, '2', '意向不明', 1, 1732606331, 0);
INSERT INTO `oa_basic_customer` VALUES (7, '2', '意向一般', 1, 1732606346, 0);
INSERT INTO `oa_basic_customer` VALUES (8, '2', '意向强烈', 1, 1732606354, 0);
INSERT INTO `oa_basic_customer` VALUES (9, '3', '电话', 1, 1732606405, 0);
INSERT INTO `oa_basic_customer` VALUES (10, '3', '微信', 1, 1732606409, 0);
INSERT INTO `oa_basic_customer` VALUES (11, '3', '上门', 1, 1732606413, 0);
INSERT INTO `oa_basic_customer` VALUES (12, '3', '其他', 1, 1732606418, 0);
INSERT INTO `oa_basic_customer` VALUES (13, '4', '立项评估', 1, 1732606467, 0);
INSERT INTO `oa_basic_customer` VALUES (14, '4', '初期沟通', 1, 1732606475, 0);
INSERT INTO `oa_basic_customer` VALUES (15, '4', '需求分析', 1, 1732606483, 0);
INSERT INTO `oa_basic_customer` VALUES (16, '4', '商务谈判', 1, 1732606490, 0);
INSERT INTO `oa_basic_customer` VALUES (17, '4', '方案制定', 1, 1732606499, 0);
INSERT INTO `oa_basic_customer` VALUES (18, '4', '合同签订', 1, 1732606506, 0);
INSERT INTO `oa_basic_customer` VALUES (19, '4', '丢单失单', 1, 1732607018, 0);

-- ----------------------------
-- Table structure for oa_customer
-- ----------------------------
DROP TABLE IF EXISTS `oa_customer`;
CREATE TABLE `oa_customer`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '客户名称',
  `source_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户来源id',
  `grade_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户等级id',
  `industry_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属行业id',
  `services_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户意向id',
  `provinceid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '省份id',
  `cityid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '城市id',
  `distid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '区县id',
  `townid` bigint(20) NOT NULL DEFAULT 0 COMMENT '城镇id',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '客户联系地址',
  `customer_status` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户状态：0未设置',
  `intent_status` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '意向状态：0未设置',
  `contact_first` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '第一联系人id',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '录入人',
  `belong_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属人',
  `belong_did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属部门',
  `belong_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '获取时间',
  `distribute_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最新分配时间',
  `follow_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最新跟进时间',
  `next_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '下次跟进时间',
  `discard_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '废弃时间',
  `share_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '共享人员，如:1,2,3',
  `content` mediumtext  NULL COMMENT '客户描述',
  `market` mediumtext  NULL COMMENT '主要经营业务',
  `remark` mediumtext  NULL COMMENT '备注信息',
  `tax_bank` varchar(100) NOT NULL DEFAULT '' COMMENT '开户银行',
  `tax_banksn` varchar(100) NOT NULL DEFAULT '' COMMENT '银行帐号',
  `tax_num` varchar(100) NOT NULL DEFAULT '' COMMENT '纳税人识别号',
  `tax_mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '开票电话',
  `tax_address` varchar(200) NOT NULL DEFAULT '' COMMENT '开票地址',
  `is_lock` tinyint(1) NOT NULL DEFAULT 0 COMMENT '锁定状态：0未锁,1已锁',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '客户表';

-- ----------------------------
-- Table structure for oa_customer_trace
-- ----------------------------
DROP TABLE IF EXISTS `oa_customer_trace`;
CREATE TABLE `oa_customer_trace`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `cid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户ID',
  `contact_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '联系人id',
  `chance_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '销售机会id',
  `types` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '跟进方式',
  `stage` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前阶段',
  `content` mediumtext NULL COMMENT '跟进内容',
  `follow_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '跟进时间',
  `next_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '下次跟进时间',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '共享人员，如:1,2,3',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '客户跟进记录表';

-- ----------------------------
-- Table structure for oa_customer_contact
-- ----------------------------
DROP TABLE IF EXISTS `oa_customer_contact`;
CREATE TABLE `oa_customer_contact`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `cid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户ID',
  `is_default` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否是第一联系人',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '姓名',
  `sex` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户性别:0未知,1男,2女',
  `mobile` char(20) NOT NULL DEFAULT '' COMMENT '手机号码',
  `qq` char(20) NOT NULL DEFAULT '' COMMENT 'QQ号',
  `wechat` char(20) NOT NULL DEFAULT '' COMMENT '微信号',
  `email` char(100) NOT NULL DEFAULT '' COMMENT '邮件地址',
  `nickname` varchar(50) NOT NULL DEFAULT '' COMMENT '称谓',
  `department` varchar(50) NOT NULL DEFAULT '' COMMENT '部门',
  `position` varchar(50) NOT NULL DEFAULT '' COMMENT '职位',
  `birthday` varchar(50) NOT NULL DEFAULT '' COMMENT '生日',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '家庭住址',
  `family` mediumtext NULL COMMENT '家庭成员',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '客户联系人表';

-- ----------------------------
-- Table structure for oa_customer_chance
-- ----------------------------
DROP TABLE IF EXISTS `oa_customer_chance`;
CREATE TABLE `oa_customer_chance`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '销售机会主题',
  `cid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户ID',
  `contact_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '联系人id',
  `services_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '需求服务id',
  `stage` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前阶段',
  `content` mediumtext  NULL COMMENT '需求描述',
  `discovery_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发现时间',
  `expected_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预计签单时间',
  `expected_amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '预计签单金额',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `belong_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属人',
  `assist_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '协助人员，如:1,2,3',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '客户线索机会表';

-- ----------------------------
-- Table structure for oa_customer_file
-- ----------------------------
DROP TABLE IF EXISTS `oa_customer_file`;
CREATE TABLE `oa_customer_file`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) UNSIGNED NOT NULL COMMENT '关联客户id',
  `file_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '相关联附件id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '客户附件关联表';

-- ----------------------------
-- Table structure for oa_property_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_property_cate`;
CREATE TABLE `oa_property_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '分类名称',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类id',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '分类说明',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '资产分类';

-- ----------------------------
-- Records of oa_property_cate
-- ----------------------------
INSERT INTO `oa_property_cate` VALUES (1, '计算机', 0, 0, '', 1, 0, 1733384708, 1733385234);
INSERT INTO `oa_property_cate` VALUES (2, '网络设备', 0, 0, '', 1, 0, 1733385274, 0);

-- ----------------------------
-- Table structure for oa_property_unit
-- ----------------------------
DROP TABLE IF EXISTS `oa_property_unit`;
CREATE TABLE `oa_property_unit`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '单位名称',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '描述',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '资产单位';

-- ----------------------------
-- Records of oa_property_unit
-- ----------------------------
INSERT INTO `oa_property_unit` VALUES (1, '台', 0, '', 1, 1733385300, 0);
INSERT INTO `oa_property_unit` VALUES (2, '只', 0, '', 1, 1733385307, 0);
INSERT INTO `oa_property_unit` VALUES (3, '个', 0, '', 1, 1733385313, 0);
INSERT INTO `oa_property_unit` VALUES (4, '瓶', 0, '', 1, 1733385321, 0);
INSERT INTO `oa_property_unit` VALUES (5, '盒', 0, '', 1, 1733385328, 0);
INSERT INTO `oa_property_unit` VALUES (6, '箱', 0, '', 1, 1733385333, 0);

-- ----------------------------
-- Table structure for oa_property_brand
-- ----------------------------
DROP TABLE IF EXISTS `oa_property_brand`;
CREATE TABLE `oa_property_brand`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '品牌名称',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '描述',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '资产品牌';
-- ----------------------------
-- Records of oa_property_brand
-- ----------------------------
INSERT INTO `oa_property_brand` VALUES (1, '品牌一', 0, '', 1, 1733385289, 0);
INSERT INTO `oa_property_brand` VALUES (2, '品牌二', 0, '', 1, 1733385289, 0);

-- ----------------------------
-- Table structure for oa_property
-- ----------------------------
DROP TABLE IF EXISTS `oa_property`;
CREATE TABLE `oa_property`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '编号',
  `thumb` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缩略图',
  `cate_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '资产分类id',
  `brand_id`int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '品牌名称',
  `unit_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单位',
  `quality_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '质保到期日期',
  `buy_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '购进日期',
  `price` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '价格',
  `rate` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '年折旧率',
  `model` varchar(255) NOT NULL DEFAULT '' COMMENT '规格型号',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '所放位置',
  `user_dids` varchar(255) NOT NULL DEFAULT '' COMMENT '使用部门',
  `user_ids`  varchar(255) NOT NULL DEFAULT '' COMMENT '使用人员',
  `content` mediumtext  NULL COMMENT '资产描述',
  `file_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '资产附件ids,如:1,2,3',
  `source` tinyint(1) NOT NULL DEFAULT 1 COMMENT '来源：1采购,2赠与,3自产,4其他',
  `purchase_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '采购单ID',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0闲置,1在用,2维修,3报废,4丢失',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_id` int(11) NOT NULL DEFAULT 0 COMMENT '编辑人',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '资产表';

-- ----------------------------
-- Table structure for oa_property_repair
-- ----------------------------
DROP TABLE IF EXISTS `oa_property_repair`;
CREATE TABLE `oa_property_repair`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `property_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '资产id',
  `repair_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '维修日期',
  `cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '维修费用',
  `content` mediumtext  NULL COMMENT '维修原因',
  `file_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '附件ids,如:1,2,3',
  `director_id` int(11) NOT NULL DEFAULT 0 COMMENT '跟进人',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '资产维修记录表';

-- ----------------------------
-- Table structure for oa_basic_adm
-- ----------------------------
DROP TABLE IF EXISTS `oa_basic_adm`;
CREATE TABLE `oa_basic_adm`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `types` varchar(100) NOT NULL DEFAULT '' COMMENT '数据类型:1车辆费用类型,2',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '行政模块常规数据';

-- ----------------------------
-- Records of oa_cost_cate
-- ----------------------------
INSERT INTO `oa_basic_adm` VALUES (1, '1', '燃油费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (2, '1', '停车费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (3, '1', '洗车费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (4, '1', '保养费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (5, '1', '维修费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (6, '1', '过路费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (7, '1', '过桥费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (8, '1', '养路费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (9, '1', '保险费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (10, '1', '年检费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (11, '1', '违章费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (12, '1', '其他费', 1, 1706840194, 0);
INSERT INTO `oa_basic_adm` VALUES (13, '2', '电子屏', 1, 1758584244, 0);
INSERT INTO `oa_basic_adm` VALUES (14, '2', '投影背景', 1, 1758584263, 0);
INSERT INTO `oa_basic_adm` VALUES (15, '2', '电脑', 1, 1758584272, 0);
INSERT INTO `oa_basic_adm` VALUES (16, '2', '视频', 1, 1758584287, 1758584291);
INSERT INTO `oa_basic_adm` VALUES (17, '2', '购买水果', 1, 1758584305, 0);
INSERT INTO `oa_basic_adm` VALUES (18, '2', '订餐', 1, 1758584312, 0);

-- ----------------------------
-- Table structure for oa_official_docs
-- ----------------------------
DROP TABLE IF EXISTS `oa_official_docs`;
CREATE TABLE `oa_official_docs`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '公文主题',
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '公文编号',
  `secrets` int(2) NOT NULL DEFAULT 1 COMMENT '密级程度:1公开,2秘密,3机密',
  `urgency` int(2) NOT NULL DEFAULT 1 COMMENT '紧急程度:1普通,2紧急,3加急',
  `send_uids` varchar(255) NOT NULL DEFAULT '' COMMENT '主送uid',
  `copy_uids` varchar(255) NOT NULL DEFAULT '' COMMENT '抄送uid',
  `share_uids` varchar(255) NOT NULL DEFAULT '' COMMENT '分享查阅uid',
  `content` mediumtext NULL COMMENT '公文内容',
  `file_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '附件ids,如:1,2,3',
  `draft_uid` int(11) NOT NULL DEFAULT 0 COMMENT '拟稿人',
  `did` int(11) NOT NULL DEFAULT 0 COMMENT '拟稿部门',
  `draft_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '拟稿日期',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '公文表';

-- ----------------------------
-- Table structure for oa_seal
-- ----------------------------
DROP TABLE IF EXISTS `oa_seal`;
CREATE TABLE `oa_seal`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '用章申请主题',
  `seal_cate_id` int(11) NOT NULL DEFAULT 0 COMMENT '印章类型', 
  `content` mediumtext NULL COMMENT '盖章内容',
  `file_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '附件ids,如:1,2,3',
  `did` int(11) NOT NULL DEFAULT 0 COMMENT '用印部门',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT '盖章次数',
  `use_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '预期用印日期',
  `is_borrow` int(1) NOT NULL DEFAULT 0 COMMENT '印章是否外借:0,1',
  `start_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '印章借用日期',
  `end_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '结束借用日期',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态:0待使用,1已使用(已外借),2已结束归还',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '用章申请表';

-- ----------------------------
-- Table structure for oa_news
-- ----------------------------
DROP TABLE IF EXISTS `oa_news`;
CREATE TABLE `oa_news`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(225) NULL DEFAULT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '新闻内容',
  `src` varchar(100) NULL DEFAULT NULL COMMENT '关联链接',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '发布人id',
  `create_time` bigint(11) NOT NULL DEFAULT 0,
  `update_time` bigint(11) NOT NULL DEFAULT 0,
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '公司新闻表';


-- ----------------------------
-- Table structure for oa_product_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_product_cate`;
CREATE TABLE `oa_product_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '分类名称',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类id',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '分类说明',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '产品分类';

-- ----------------------------
-- Records of oa_product_cate
-- ----------------------------
INSERT INTO `oa_product_cate` VALUES (1, '产品分类一', 0, 0, '', 1, 0, 1733385454, 0);
INSERT INTO `oa_product_cate` VALUES (2, '产品分类二', 0, 0, '', 1, 0, 1733385467, 0);

-- ----------------------------
-- Table structure for oa_product
-- ----------------------------
DROP TABLE IF EXISTS `oa_product`;
CREATE TABLE `oa_product`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '产品名称',
  `cate_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品分类id',
  `thumb` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缩略图id',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '产品编码',
  `barcode` varchar(255) NOT NULL DEFAULT '' COMMENT '条形码',
  `unit` varchar(100) NOT NULL DEFAULT '' COMMENT '单位',
  `specs` varchar(100) NOT NULL DEFAULT '' COMMENT '规格',
  `brand` varchar(100) NOT NULL DEFAULT '' COMMENT '品牌',
  `producer` varchar(100) NOT NULL DEFAULT '' COMMENT '生产商',
  `base_price` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '成本价',
  `purchase_price` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '采购价',
  `sale_price` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '销售价',
  `content` text NULL COMMENT '产品描述',
  `album_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '产品相册ids,如:1,2,3',
  `file_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '产品附件ids,如:1,2,3',
  `stock` int(11) NOT NULL DEFAULT 0 COMMENT '库存',
  `is_object` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否是实物,1是2不是',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '产品表';

-- ----------------------------
-- Table structure for oa_services
-- ----------------------------
DROP TABLE IF EXISTS `oa_services`;
CREATE TABLE `oa_services`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '服务名称',
  `cate_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '服务分类id',
  `price` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '服务费用',
  `content` text NULL COMMENT '服务描述',
  `sort` int(10) NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '服务表';

-- ----------------------------
-- Records of oa_services
-- ----------------------------
INSERT INTO `oa_services` VALUES (1, '定制服务', 0, 999.00, NULL, 0, 1, 1733385487, 1733385493, 0);
INSERT INTO `oa_services` VALUES (2, '咨询服务', 0, 99.00, NULL, 0, 1, 1733385500, 0, 0);

-- ----------------------------
-- Table structure for oa_supplier
-- ----------------------------
DROP TABLE IF EXISTS `oa_supplier`;
CREATE TABLE `oa_supplier`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商名称',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商编号',
  `phone` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商电话',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商邮箱',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商联系地址',
  `file_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '附件ids,如:1,2,3',
  `products` varchar(500) NOT NULL DEFAULT '' COMMENT '供应商商品',
  `content` text NULL COMMENT '供应商描述',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '供应商状态：0禁用,1启用',
  `tax_num` varchar(100) NOT NULL DEFAULT '' COMMENT '纳税人识别号',
  `tax_mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '开票电话',
  `tax_address` varchar(200) NOT NULL DEFAULT '' COMMENT '开票地址',
  `tax_bank` varchar(60) NOT NULL DEFAULT '' COMMENT '开户银行',
  `tax_banksn` varchar(60) NOT NULL DEFAULT '' COMMENT '银行帐号',
  `file_license_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '营业执照附件，如:1,2,3',
  `file_idcard_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '身份证附件，如:1,2,3',
  `file_bankcard_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '银行卡附件，如:1,2,3',
  `file_openbank_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '开户行附件，如:1,2,3',
  `tax_rate` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '税率',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '录入人',
  `sort` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '供应商表';

-- ----------------------------
-- Table structure for oa_supplier_contact
-- ----------------------------
DROP TABLE IF EXISTS `oa_supplier_contact`;
CREATE TABLE `oa_supplier_contact`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '供应商ID',
  `is_default` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否是第一联系人',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '姓名',
  `sex` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户性别:0未知,1男,2女',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号码',
  `qq` varchar(20) NOT NULL DEFAULT '' COMMENT 'QQ号',
  `wechat` varchar(100) NOT NULL DEFAULT '' COMMENT '微信号',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮件地址',
  `nickname` varchar(50) NOT NULL DEFAULT '' COMMENT '称谓',
  `department` varchar(50) NOT NULL DEFAULT '' COMMENT '部门',
  `position` varchar(50) NOT NULL DEFAULT '' COMMENT '职务',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` int(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '供应商联系人表';

-- ----------------------------
-- Table structure for oa_purchased_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_purchased_cate`;
CREATE TABLE `oa_purchased_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '分类名称',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类id',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '分类说明',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '采购品分类';

-- ----------------------------
-- Records of oa_purchased_cate
-- ----------------------------
INSERT INTO `oa_purchased_cate` VALUES (1, '采购品分类一', 0, 0, '', 1, 0, 1733385535, 0);
INSERT INTO `oa_purchased_cate` VALUES (2, '采购品分类二', 0, 0, '', 1, 0, 1733385542, 0);

-- ----------------------------
-- Table structure for oa_purchased
-- ----------------------------
DROP TABLE IF EXISTS `oa_purchased`;
CREATE TABLE `oa_purchased`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '采购品名称',
  `cate_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '采购分类id',
  `thumb` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缩略图id',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '产品编码',
  `barcode` varchar(255) NOT NULL DEFAULT '' COMMENT '条形码',
  `unit` varchar(100) NOT NULL DEFAULT '' COMMENT '单位',
  `specs` varchar(100) NOT NULL DEFAULT '' COMMENT '规格',
  `brand` varchar(100) NOT NULL DEFAULT '' COMMENT '品牌',
  `producer` varchar(100) NOT NULL DEFAULT '' COMMENT '生产商',
  `purchase_price` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '采购价',
  `sale_price` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '销售价',
  `content` text NULL COMMENT '商品描述',
  `album_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '采购品相册ids,如:1,2,3',
  `file_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '采购品附件ids,如:1,2,3',
  `stock` int(11) NOT NULL DEFAULT 0 COMMENT '库存',
  `is_object` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否是实物,1是2不是',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '采购品表';


-- ----------------------------
-- Table structure for oa_contract_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_contract_cate`;
CREATE TABLE `oa_contract_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '合同类别名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '合同类别';

-- ----------------------------
-- Records of oa_contract_cate
-- ----------------------------
INSERT INTO `oa_contract_cate` VALUES (1, '合同分类一', 1, 1637987189, 0,0);
INSERT INTO `oa_contract_cate` VALUES (2, '合同分类二', 1, 1637987199, 0,0);

-- ----------------------------
-- Table structure for oa_contract
-- ----------------------------
DROP TABLE IF EXISTS `oa_contract`;
CREATE TABLE `oa_contract`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父协议id',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '合同编号',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '合同名称',
  `cate_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类id',
  `types` tinyint(1) NOT NULL DEFAULT 1 COMMENT '合同性质:1普通合同2商品合同3服务合同',
  `subject_id` varchar(255) NOT NULL DEFAULT '' COMMENT '签约主体',
  `customer_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联客户ID,预设数据',
  `chance_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '销售机会id',
  `customer` varchar(255) NOT NULL DEFAULT '' COMMENT '客户名称',
  `contact_name` varchar(255) NOT NULL DEFAULT '' COMMENT '客户代表',
  `contact_mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '客户电话',
  `contact_address` varchar(255) NOT NULL DEFAULT '' COMMENT '客户地址',
  `start_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同开始时间',
  `end_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同结束时间',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `prepared_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同制定人',
  `sign_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同签订人',
  `keeper_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同保管人', 
  `share_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '共享人员，如:1,2,3',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件，如:1,2,3',
  `seal_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '盖章合同附件，如:1,2,3',
  `sign_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同签订时间',
  `did` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同所属部门',
  `cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '合同金额',
  `content` mediumtext  NULL COMMENT '合同内容',
  `is_tax` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否含税：0未含税,1含税',
  `tax` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '税点',
  `stop_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '中止人',
  `stop_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '中止时间',
  `stop_remark` mediumtext  NULL COMMENT '中止备注信息',
  `void_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '作废人',
  `void_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '作废时间',
  `void_remark` mediumtext  NULL COMMENT '作废备注信息',
  `archive_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '归档人',
  `archive_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '归档时间',
  `remark` mediumtext  NULL COMMENT '备注信息',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '销售合同表';

-- ----------------------------
-- Table structure for oa_purchase
-- ----------------------------
DROP TABLE IF EXISTS `oa_purchase`;
CREATE TABLE `oa_purchase`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父协议id',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '合同编号',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '合同名称',
  `cate_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类id',
  `types` tinyint(1) NOT NULL DEFAULT 1 COMMENT '合同性质:1普通采购2物品采购3服务采购',
  `subject_id` varchar(255) NOT NULL DEFAULT '' COMMENT '签约主体',
  `supplier_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联供应商ID',
  `supplier` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商名称',
  `contact_name` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商代表',
  `contact_mobile` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商电话',
  `contact_address` varchar(255) NOT NULL DEFAULT '' COMMENT '供应商地址',
  `start_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同开始时间',
  `end_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同结束时间',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `prepared_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同制定人',
  `sign_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同签订人',
  `keeper_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同保管人', 
  `share_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '共享人员，如:1,2,3',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件，如:1,2,3',
  `seal_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '盖章合同附件，如:1,2,3',
  `sign_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同签订时间',
  `did` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '合同所属部门',
  `cost` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '合同金额',
  `content` mediumtext  NULL COMMENT '合同内容',
  `stop_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '中止人',
  `stop_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '中止时间',
  `stop_remark` mediumtext  NULL COMMENT '中止备注信息',
  `void_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '作废人',
  `void_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '作废时间',
  `void_remark` mediumtext  NULL COMMENT '作废备注信息',
  `archive_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '归档人',
  `archive_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '归档时间',
  `remark` mediumtext  NULL COMMENT '备注信息',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '采购合同表';

-- ----------------------------
-- Table structure for oa_step
-- ----------------------------
DROP TABLE IF EXISTS `oa_step`;
CREATE TABLE `oa_step`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '步骤名称',
  `sort` int(10) NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '步骤表';

-- ----------------------------
-- Records of oa_step
-- ----------------------------
INSERT INTO `oa_step` VALUES (1, '立项阶段', 0,1, 1637987189, 0,0);
INSERT INTO `oa_step` VALUES (2, '规划阶段', 0,1, 1637987189, 0,0);
INSERT INTO `oa_step` VALUES (3, '执行阶段', 0,1, 1637987189, 0,0);
INSERT INTO `oa_step` VALUES (4, '监控与控制阶段', 0,1, 1637987189, 0,0);
INSERT INTO `oa_step` VALUES (5, '收尾阶段', 0,1, 1637987189, 0,0);

-- ----------------------------
-- Table structure for oa_work_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_work_cate`;
CREATE TABLE `oa_work_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '工作类型名称',
  `sort` int(10) NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '工作类型';

-- ----------------------------
-- Records of oa_work_cate
-- ----------------------------
INSERT INTO `oa_work_cate` VALUES (1, '其他', 0,1, 1637987189, 0);
INSERT INTO `oa_work_cate` VALUES (2, '方案策划',0, 1, 1637987199, 0);
INSERT INTO `oa_work_cate` VALUES (3, '撰写文档',0, 1, 1637987199, 0);
INSERT INTO `oa_work_cate` VALUES (4, '需求调研',0, 1, 1637987199, 0);
INSERT INTO `oa_work_cate` VALUES (5, '需求沟通',0, 1, 1637987199, 0);
INSERT INTO `oa_work_cate` VALUES (6, '参加会议',0, 1, 1637987199, 0);
INSERT INTO `oa_work_cate` VALUES (7, '拜访客户',0, 1, 1637987199, 0);
INSERT INTO `oa_work_cate` VALUES (8, '接待客户',0, 1, 1637987199, 0);

-- ----------------------------
-- Table structure for oa_project_cate
-- ----------------------------
DROP TABLE IF EXISTS `oa_project_cate`;
CREATE TABLE `oa_project_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '项目类别名称',
  `sort` int(10) NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '项目类别';

-- ----------------------------
-- Records of oa_work_cate
-- ----------------------------
INSERT INTO `oa_project_cate` VALUES (1, '项目分类一', 0, 1, 1733385561, 0, 0);
INSERT INTO `oa_project_cate` VALUES (2, '项目分类二', 0, 1, 1733385567, 0, 0);

-- ----------------------------
-- Table structure for oa_project
-- ----------------------------
DROP TABLE IF EXISTS `oa_project`;
CREATE TABLE `oa_project`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '项目名称',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '项目编号',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '项目金额',
  `cate_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类ID',
  `customer_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联客户ID',
  `contract_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联合同协议ID',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `director_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目负责人',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目所属部门',
  `start_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目开始时间',
  `end_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目结束时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0未设置,1未开始,2进行中,3已完成,4已关闭',
  `content` mediumtext  NULL COMMENT '项目描述',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '项目表';

-- ----------------------------
-- Table structure for oa_project_step
-- ----------------------------
DROP TABLE IF EXISTS `oa_project_step`;
CREATE TABLE `oa_project_step`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT '关联ID',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '阶段名称',
  `director_uid` int(11) NOT NULL DEFAULT 0 COMMENT '阶段负责人ID',
  `uids` varchar(500) NOT NULL DEFAULT '' COMMENT '阶段成员ID (使用逗号隔开) 1,2,3',
  `sort` tinyint(4) NOT NULL DEFAULT 0 COMMENT '排序ID',
  `is_current` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是当前阶段',
  `start_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '开始时间',
  `end_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '结束时间',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '阶段说明',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '项目阶段步骤表';

-- ----------------------------
-- Table structure for oa_project_step_record
-- ----------------------------
DROP TABLE IF EXISTS `oa_project_step_record`;
CREATE TABLE `oa_project_step_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联项目ID',
  `step_id` int(11) NOT NULL DEFAULT 0 COMMENT '阶段步骤ID',
  `check_uid` int(11) NOT NULL DEFAULT 0 COMMENT '确认人ID',
  `check_time` bigint(11) NOT NULL COMMENT '确认时间',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '1审核通过2审核拒绝3撤销',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT '操作意见',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '项目阶段步骤记录表';

-- ----------------------------
-- Table structure for oa_project_user
-- ----------------------------
DROP TABLE IF EXISTS `oa_project_user`;
CREATE TABLE `oa_project_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目成员id',
  `project_id` int(11) UNSIGNED NOT NULL COMMENT '关联项目id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '移除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '项目成员表';

-- ----------------------------
-- Table structure for oa_project_task
-- ----------------------------
DROP TABLE IF EXISTS `oa_project_task`;
CREATE TABLE `oa_project_task`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '主题',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父级任务id',
  `before_task` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '前置任务id',
  `project_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联项目id',
  `work_id` int(1) NOT NULL DEFAULT 0 COMMENT '关联工作类型',
  `step_id` int(1) NOT NULL DEFAULT 0 COMMENT '关联项目阶段',
  `plan_hours` decimal(10, 1) NOT NULL DEFAULT 0.00 COMMENT '预估工时',
  `end_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预计结束时间',
  `over_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '实际结束时间',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `director_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '指派给(负责人)',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属部门',
  `assist_admin_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '协助人员，如:1,2,3',
  `priority` tinyint(1) NOT NULL DEFAULT 1 COMMENT '优先级:1低,2中,3高,4紧急',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '任务状态：1待办的,2进行中,3已完成,4已拒绝,5已关闭',
  `done_ratio` int(2) NOT NULL DEFAULT 0 COMMENT '完成进度：0,20,40,50,60,80',
  `content` mediumtext  NULL COMMENT '任务描述',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '项目任务表';

-- ----------------------------
-- Table structure for oa_project_document
-- ----------------------------
DROP TABLE IF EXISTS `oa_project_document`;
CREATE TABLE `oa_project_document`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `project_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联项目id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属部门',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ids',
  `content` mediumtext  NULL COMMENT '文档内容',
  `md_content` mediumtext  NULL COMMENT 'markdown文档内容',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '项目文档表';

-- ----------------------------
-- Table structure for oa_project_file
-- ----------------------------
DROP TABLE IF EXISTS `oa_project_file`;
CREATE TABLE `oa_project_file`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '模块',
  `topic_id` int(11) UNSIGNED NOT NULL COMMENT '关联主题id',
  `file_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '相关联附件id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '项目附件关联表';

-- ----------------------------
-- Table structure for oa_disk
-- ----------------------------
DROP TABLE IF EXISTS `oa_disk`;
CREATE TABLE `oa_disk`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所在文件夹目录ID',
  `did` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属部门',
  `types` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '类型:0文件,1在线文档,2文件夹',
  `action_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '相关联id',
  `group_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分享空间id',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '文件名称',
  `file_ext` varchar(200) NOT NULL DEFAULT '' COMMENT '文件后缀名称',
  `file_size` bigint(11) NOT NULL DEFAULT 0 COMMENT '文件大小',
  `is_star` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '重要与否',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `clear_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '清除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '网盘文件表';

-- ----------------------------
-- Table structure for oa_disk_group
-- ----------------------------
DROP TABLE IF EXISTS `oa_disk_group`;
CREATE TABLE `oa_disk_group`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '分享空间名称',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `director_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '管理人员',
  `group_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '群组成员',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '网盘分享空间表';

-- ----------------------------
-- Records of oa_disk_group
-- ----------------------------
INSERT INTO `oa_disk_group` VALUES (1000, '入职培训', 1, '', '', 1749030795, 1749031338, 0);
INSERT INTO `oa_disk_group` VALUES (1001, '项目文件', 1, '', '', 1749030795, 1749031338, 0);
INSERT INTO `oa_disk_group` VALUES (1002, '常用软件', 1, '', '', 1749030795, 1749031338, 0);
INSERT INTO `oa_disk_group` VALUES (1003, '团建照片', 1, '', '', 1749030795, 1749031338, 0);


-- ----------------------------
-- Table structure for oa_article
-- ----------------------------
DROP TABLE IF EXISTS `oa_article`;
CREATE TABLE `oa_article`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '文档标题',
  `origin_url` varchar(255) NOT NULL DEFAULT '' COMMENT '来源地址',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件',
  `content` text NOT NULL COMMENT '文章内容',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '作者',
  `create_time` bigint(11) NOT NULL DEFAULT 0,
  `update_time` bigint(11) NOT NULL DEFAULT 0,
  `delete_time` bigint(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '文档表';

-- ----------------------------
-- Table structure for oa_edit_log
-- ----------------------------
DROP TABLE IF EXISTS `oa_edit_log`;
CREATE TABLE `oa_edit_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '标识',
  `field` varchar(100) NOT NULL DEFAULT '' COMMENT '字段',
  `action_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联id',
  `second_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '第二关联id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作人',
  `old_content` mediumtext  NULL COMMENT '修改前的内容',
  `new_content` mediumtext  NULL COMMENT '修改后的内容',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '修改记录表';

-- ----------------------------
-- Table structure for oa_comment
-- ----------------------------
DROP TABLE IF EXISTS `oa_comment`;
CREATE TABLE `oa_comment`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '模块',
  `topic_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联主题id',
  `pid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '回复内容id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `to_uids` varchar(100) NOT NULL DEFAULT '' COMMENT '@用户id',
  `content` mediumtext  NULL COMMENT '评论内容',
  `md_content` mediumtext  NULL COMMENT 'markdown评论内容',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '评论表';

-- ----------------------------
-- Table structure for oa_comment_read
-- ----------------------------
DROP TABLE IF EXISTS `oa_comment_read`;
CREATE TABLE `oa_comment_read`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `comment_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联评论id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '已读人',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '评论已读回执表';


-- ----------------------------
-- Table structure for oa_mobile_bar
-- ----------------------------
DROP TABLE IF EXISTS `oa_mobile_bar`;
CREATE TABLE `oa_mobile_bar`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '链接',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '手机端Bar表';

-- ----------------------------
-- Records of oa_mobile_bar
-- ----------------------------
INSERT INTO `oa_mobile_bar` VALUES (1, '工作台', 'icon-gongzuotai1', '/qiye/index/index', 0, 1, 1723277311, 1733147213);
INSERT INTO `oa_mobile_bar` VALUES (2, '客户', 'icon-kehu', '/qiye/customer/index', 0, 1, 1723277311, 1733147238);
INSERT INTO `oa_mobile_bar` VALUES (3, '合同', 'icon-hetong2', '/qiye/contract/index', 0, 1, 1723277351, 1733147266);
INSERT INTO `oa_mobile_bar` VALUES (4, '项目', 'icon-xiangmu1', '/qiye/project/index', 0, 1, 1723277356, 1733147298);
INSERT INTO `oa_mobile_bar` VALUES (5, '消息', 'icon-xiaoxi1', '/qiye/msg/index', 0, 1, 1723277368, 1733147331);

-- ----------------------------
-- Table structure for oa_mobile_types
-- ----------------------------
DROP TABLE IF EXISTS `oa_mobile_types`;
CREATE TABLE `oa_mobile_types`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `bgcolor` varchar(255) NOT NULL DEFAULT '' COMMENT '背景颜色',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '手机端工作台菜单类型表';

-- ----------------------------
-- Records of oa_mobile_types
-- ----------------------------
INSERT INTO `oa_mobile_types` VALUES (1, '办公审批', '', 'blue', 0, 1, 1723277311, 0);
INSERT INTO `oa_mobile_types` VALUES (2, '效率工具', '', 'green', 0, 1, 1723277311, 0);
INSERT INTO `oa_mobile_types` VALUES (3, '内部管理', '', 'yellow', 0, 1, 1723277351, 0);
INSERT INTO `oa_mobile_types` VALUES (4, '财务管理', '', 'purple', 0, 1, 1723277356, 0);
INSERT INTO `oa_mobile_types` VALUES (5, '人事管理', '', 'red', 0, 1, 1754539327,0);

-- ----------------------------
-- Table structure for oa_mobile_menu
-- ----------------------------
DROP TABLE IF EXISTS `oa_mobile_menu`;
CREATE TABLE `oa_mobile_menu`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `bgcolor` varchar(255) NOT NULL DEFAULT '' COMMENT '背景颜色',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '链接',
  `types` int(11) NOT NULL DEFAULT 0 COMMENT '关联菜单类型',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序：越大越靠前',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '手机端菜单表';

-- ----------------------------
-- Records of oa_mobile_menu
-- ----------------------------
INSERT INTO `oa_mobile_menu` VALUES (1, '审批申请', 'icon-shenpi','blue', '/qiye/approve/apply', 1, 0, 1, 1733146294, 1733152760, 0);
INSERT INTO `oa_mobile_menu` VALUES (2, '我申请的', 'icon-wodeshenpi1','blue', '/qiye/approve/mylist', 1, 0, 1, 1733152749, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (3, '我处理的', 'icon-chulishenpi','blue', '/qiye/approve/checklist', 1, 0, 1, 1733152798, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (4, '抄送给我', 'icon-chaosong','blue', '/qiye/approve/copylist', 1, 0, 1, 1733152823, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (5, '日程安排', 'icon-richeng','green', '/qiye/index/calendar', 2, 0, 1, 1733152855, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (6, '工作记录', 'icon-jilu','green', '/qiye/index/schedule', 2, 0, 1, 1733152878, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (7, '工作汇报', 'icon-huibao','green', '/qiye/index/work', 2, 0, 1, 1733152906, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (8, '公告通知', 'icon-gonggaotongzhi','yellow', '/qiye/index/note', 3, 0, 1, 1733152965, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (9, '公司新闻', 'icon-gongsixinwen','yellow', '/qiye/index/news', 3, 0, 1, 1733152993, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (10, '会议纪要', 'icon-huiyijiyao','yellow', '/qiye/index/meeting', 3, 0, 1, 1733152993, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (11, '报销管理', 'icon-a-baoxiao3','purple', '/qiye/finance/expense', 4, 0, 1, 1733153019, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (12, '开票管理', 'icon-kaipiao','purple', '/qiye/finance/invoice', 4, 0, 1, 1733153047, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (13, '收票管理', 'icon-shoupiao','purple', '/qiye/finance/ticket', 4, 0, 1, 1733153077, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (14, '回款管理', 'icon-huikuan','purple', '/qiye/finance/income', 4, 0, 1, 1733153106, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (15, '付款管理', 'icon-fukuan','purple', '/qiye/finance/payment', 4, 0, 1, 1733153131, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (16, '借支管理', 'icon-a-baoxiao2','purple', '/qiye/finance/loan', 4, 0, 1, 1733153131, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (17, '企业人员', 'icon-kehu', 'cyan', '/qiye/index/admin', 5, 0, 1, 1754539422, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (18, '入职管理', 'icon-wodeshenpi1', 'cyan', '/qiye/approve/talentlist', 5, 0, 1, 1754539501, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (19, '离职管理', 'icon-zuofeibaoming', 'cyan', '/qiye/approve/leavelist', 5, 0, 1, 1754539544, 0, 0);
INSERT INTO `oa_mobile_menu` VALUES (20, '人事调动', 'icon-qiangke', 'cyan', '/qiye/approve/changelist', 5, 0, 1, 1754539578, 0, 0);

-- ----------------------------
-- Table structure for oa_meeting_order
-- ----------------------------
DROP TABLE IF EXISTS `oa_meeting_order`;
CREATE TABLE `oa_meeting_order`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(225) NULL DEFAULT NULL COMMENT '会议主题',
  `start_date` int(11) NOT NULL DEFAULT 0 COMMENT '会议开始时间',
  `end_date` int(11) NOT NULL DEFAULT 0 COMMENT '会议结束时间',
  `requirements` varchar(500) NOT NULL DEFAULT '' COMMENT '会议需求',
  `room_id` int(11) NOT NULL DEFAULT 0 COMMENT '会议室',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT '人数',
  `remark` varchar(225) NULL DEFAULT NULL COMMENT '备注信息',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '相关附件',
  `join_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '与会人员',
  `anchor_id` int(11) NOT NULL DEFAULT 0 COMMENT '主持人id',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '发布人id',
  `did` int(11) NOT NULL DEFAULT 0 COMMENT '主办部门',
  `create_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '申请时间',
  `check_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态:0待审核,1审核中,2审核通过,3审核不通过,4撤销审核',
  `check_flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核流程id',
  `check_step_sort` int(11) NOT NULL DEFAULT 0 COMMENT '当前审批步骤',
  `check_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '当前审批人ID，如:1,2,3',
  `check_last_uid` varchar(500) NOT NULL DEFAULT '' COMMENT '上一审批人',
  `check_history_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '历史审批人ID，如:1,2,3',
  `check_copy_uids` varchar(500) NOT NULL DEFAULT '' COMMENT '抄送人ID，如:1,2,3',
  `check_time` bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核通过时间',
  `update_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '更新信息时间',
  `delete_time` bigint(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '会议室预定';

-- ----------------------------
-- Table structure for oa_links
-- ----------------------------
DROP TABLE IF EXISTS `oa_links`;
CREATE TABLE `oa_links`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '小工具名称',
  `logo` int(11) NOT NULL DEFAULT 0 COMMENT 'logo',
  `url` varchar(255) NULL DEFAULT NULL COMMENT '链接',
  `target` int(1) NOT NULL DEFAULT 1 COMMENT '是否新窗口打开，1是,0否',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  `delete_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '办公工具';

-- ----------------------------
-- Records of oa_links
-- ----------------------------
INSERT INTO `oa_links` VALUES (1, 'DeepSeek', 0, 'https://chat.deepseek.com/', 1, 0, 1757759036, 1760144979, 0);
INSERT INTO `oa_links` VALUES (2, '豆包', 0, 'https://www.doubao.com/chat/', 1, 0, 1757759226, 1760141689, 0);
INSERT INTO `oa_links` VALUES (3, '文心一言', 0, 'https://yiyan.baidu.com/', 1, 0, 1760141394, 0, 0);
INSERT INTO `oa_links` VALUES (4, '通义千问', 0, 'https://www.tongyi.com/', 1, 0, 1760141705, 0, 0);
INSERT INTO `oa_links` VALUES (5, 'AIPPT', 0, 'https://www.aippt.cn/', 1, 0, 1760144331, 0, 0);
INSERT INTO `oa_links` VALUES (6, 'ProcessOn', 0, 'https://www.processon.com/', 1, 0, 1760144601, 0, 0);