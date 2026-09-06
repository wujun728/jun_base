SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dev_admin
-- ----------------------------
DROP TABLE IF EXISTS `dev_admin`;
CREATE TABLE `dev_admin`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '登录用户名',
  `pwd` varchar(100) NOT NULL DEFAULT '' COMMENT '登录密码',
  `salt` varchar(100) NOT NULL DEFAULT '' COMMENT '密码盐',
  `reg_pwd` varchar(100) NOT NULL DEFAULT '' COMMENT '初始密码',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '员工姓名',
  `mobile` bigint(11) NOT NULL DEFAULT 0 COMMENT '手机号码',
  `sex` int(1) NOT NULL DEFAULT 0 COMMENT '性别1男,2女',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '电子邮箱',
  `nickname` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称',
  `thumb` varchar(255) NOT NULL COMMENT '头像',
  `did` int(11) NOT NULL DEFAULT 0 COMMENT '部门id',
  `position_id` int(11) NOT NULL DEFAULT 0 COMMENT '职位id',
  `type` int(1) NOT NULL DEFAULT 1 COMMENT '员工类型：0未设置,1正式,2试用,3实习',
  `desc` text NULL COMMENT '员工个人简介',
  `entry_time` int(11) NOT NULL DEFAULT 0 COMMENT '员工入职日期',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '注册时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新信息时间',
  `last_login_time` int(11) NOT NULL DEFAULT 0 COMMENT '最后登录时间',
  `login_num` int(11) NOT NULL DEFAULT 0 COMMENT '登录次数',
  `last_login_ip` varchar(64) NOT NULL DEFAULT '' COMMENT '最后登录IP',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除,0禁止登录,1正常,2离职',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工表';

-- ----------------------------
-- Table structure for dev_admin_rule
-- ----------------------------
DROP TABLE IF EXISTS `dev_admin_rule`;
CREATE TABLE `dev_admin_rule`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父id',
  `src` varchar(255) NOT NULL DEFAULT '' COMMENT 'url链接',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '日志操作名称',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `menu` int(1) NOT NULL DEFAULT 0 COMMENT '是否是菜单,1是,2不是',
  `sort` int(11) NOT NULL DEFAULT 1 COMMENT '越小越靠前',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态,0禁用,1正常',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '菜单及权限表';


-- ----------------------------
-- Records of dev_admin_rule
-- ----------------------------
INSERT INTO `dev_admin_rule` VALUES (1, 0, 'admin/setting/index', '系统', '系统管理', 'icon-jichupeizhi', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (2, 0, 'product/index/index', '产品', '产品管理', 'icon-xiaoshoupin', 1, 2, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (3, 0, 'project/index/index', '项目', '项目管理', 'icon-xiangmuguanli', 1, 3, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (4, 0, 'task/index/index', '任务', '任务管理', 'icon-lunwenguanli', 1, 4, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (5, 0, 'schedule/index/index', '工时', '工时管理', 'icon-renwuguanli', 1, 5, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (6, 0, 'knowledge/index/index', '知识库', '知识库', 'icon-shujiguanli', 1, 6, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (7, 1, '', '系统设置', '系统设置', 'icon-jichupeizhi', 1, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (8, 7, 'admin/conf/index', '系统配置', '系统配置', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (9, 8, 'admin/conf/add', '新建/编辑', '配置项', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (10, 8, 'admin/conf/delete', '删除', '配置项', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (11, 8, 'admin/conf/edit', '编辑', '配置详情', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (12, 7, 'admin/rule/index', '菜单节点', '菜单节点', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (13, 12, 'admin/rule/add', '新建/编辑', '菜单节点', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (14, 12, 'admin/rule/delete', '删除', '菜单节点', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (15, 7, 'admin/role/index', '权限角色', '权限角色', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (16, 15, 'admin/role/add', '新建/编辑', '权限角色', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (17, 15, 'admin/role/delete', '删除', '权限角色', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (18, 7, 'admin/log/index', '操作日志', '操作日志', '', 1, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (19, 7, 'admin/database/database', '数据安全', '数据备份', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (20, 19, 'admin/database/backup', '备份数据表', '数据', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (21, 19, 'admin/database/optimize', '优化数据表', '数据表', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (22, 19, 'admin/database/repair', '修复数据表', '数据表', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (23, 7, 'admin/database/backuplist', '还原数据', '数据还原', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (24, 23, 'admin/database/import', '还原数据表', '数据', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (25, 23, 'admin/database/downfile', '下载备份数据', '备份数据', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (26, 23, 'admin/database/del', '删除备份数据', '备份数据', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (27, 1, '', '基础数据', '基础数据', 'icon-jichushezhi', 1, 2, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (28, 27, 'admin/ncate/index', '公告类型', '公告类型', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (29, 28, 'admin/ncate/add', '新建/编辑', '公告类型', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (30, 28, 'admin/ncate/delete', '删除', '公告类型', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (31, 27, 'admin/wcate/index', '工作类型', '工作类型', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (32, 31, 'admin/wcate/add', '新建/编辑', '工作类型', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (33, 31, 'admin/wcate/check', '设置', '工作类型', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (34, 27, 'admin/tcate/index', '任务类型', '任务类型', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (35, 34, 'admin/tcate/add', '新建/编辑', '任务类型', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (36, 34, 'admin/tcate/check', '设置', '任务类型', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (37, 27, 'admin/kcate/index', '知识类型', '知识类型', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (38, 37, 'admin/kcate/add', '新建/编辑', '知识类型', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (39, 37, 'admin/kcate/delete', '删除', '知识类型', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (40, 1, '', '企业管理', '企业管理', 'icon-qiyeguanli', 1, 3, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (41, 40, 'admin/department/index', '部门架构', '部门', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (42, 41, 'admin/department/add', '新建/编辑', '部门', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (43, 41, 'admin/department/delete', '删除', '部门', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (44, 40, 'admin/position/index', '岗位职称', '岗位职称', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (45, 44, 'admin/position/add', '新建/编辑', '岗位职称', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (46, 44, 'admin/position/delete', '删除', '岗位职称', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (47, 44, 'admin/position/view', '查看', '岗位职称', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (48, 40, 'admin/user/index', '企业员工', '员工', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (49, 48, 'admin/user/add', '新建/编辑', '员工', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (50, 48, 'admin/user/view', '查看', '员工信息', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (51, 48, 'admin/user/set', '设置', '员工状态', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (52, 48, 'admin/user/reset', '重设密码', '员工密码', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (53, 40, 'admin/note/index', '企业公告', '公告', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (54, 54, 'admin/note/add', '新建/编辑', '公告', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (55, 54, 'admin/note/delete', '删除', '公告', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (56, 54, 'admin/note/view', '查看', '公告', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (57, 2, 'product/index/list', '产品列表', '产品', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (58, 57, 'product/index/add', '新建', '产品', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (59, 57, 'product/index/view', '查看', '产品', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (60, 57, 'product/index/delete', '删除', '产品', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (61, 3, 'project/index/list', '项目列表', '项目', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (62, 61, 'project/index/add', '新建', '项目', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (63, 61, 'project/index/edit', '编辑', '项目', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (64, 61, 'project/index/view', '查看', '项目', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (65, 61, 'project/index/delete', '删除', '项目', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (66, 4, 'task/index/list', '任务列表', '任务', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (67, 66, 'task/index/add', '新建', '任务', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (68, 66, 'task/index/delete', '删除', '任务', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (69, 66, 'task/index/view', '查看', '任务', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (70, 5, 'schedule/index/list', '工作记录列表', '工作记录', '', 1, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (71, 70, 'schedule/index/add', '新建/编辑', '工作记录', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (72, 70, 'schedule/index/delete', '删除', '工作记录', '', 2, 1, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (73, 70, 'schedule/index/view', '查看', '工作记录', '', 2, 1, 1, 0, 0);

INSERT INTO `dev_admin_rule` VALUES (74, 6, 'knowledge/index/index', '共享知识', '知识库', '', 1, 0, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (75, 6, 'knowledge/index/list', '个人知识', '知识库', '', 1, 0, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (76, 74, 'knowledge/index/add', '新建/编辑知识库', '知识库', '', 2, 0, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (77, 74, 'knowledge/index/delete', '删除知识库', '知识库', '', 2, 0, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (78, 74, 'knowledge/index/edit', '编辑知识库', '知识库', '', 2, 0, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (79, 74, 'knowledge/index/view', '知识库详情', '知识库', '', 1, 0, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (80, 74, 'knowledge/index/doc_add', '新建/编辑知识库文档', '知识库文档', '', 2, 0, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (81, 74, 'knowledge/index/doc_detail', '知识库文档详情', '知识库文档', '', 2, 0, 1, 0, 0);
INSERT INTO `dev_admin_rule` VALUES (82, 74, 'knowledge/index/doc_delete', '删除知识库文档', '知识库文档', '', 2, 0, 1, 0, 0);


-- ----------------------------
-- Table structure for dev_admin_group
-- ----------------------------
DROP TABLE IF EXISTS `dev_admin_group`;
CREATE TABLE `dev_admin_group`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '',
  `status` int(1) NOT NULL DEFAULT 1,
  `rules` varchar(1000) NULL DEFAULT '' COMMENT '用户组拥有的规则id， 多个规则\",\"隔开',
  `desc` text NULL COMMENT '备注',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工权限分组表';

-- ----------------------------
-- Records of cms_admin_group
-- ----------------------------
INSERT INTO `dev_admin_group` VALUES (1, '超级员工权限', 1, '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82', '超级员工权限，拥有系统的最高权限，不可修改', 0, 0);
INSERT INTO `dev_admin_group` VALUES (2, '人事角色权限', 1, '1,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,6,74,76,77,78,79,80,81,82,75', '人事部管理权限', 0, 0);
INSERT INTO `dev_admin_group` VALUES (3, '产品角色权限', 1, '2,57,58,59,60,3,61,62,63,64,65,4,66,67,68,69,5,70,71,72,73,6,74,76,77,78,79,80,81,82,75', '产品经理职位权限', 0, 0);
INSERT INTO `dev_admin_group` VALUES (4, '技术角色权限', 1, '2,57,58,59,60,3,61,62,63,64,65,4,66,67,68,69,5,70,71,72,73,6,74,76,77,78,79,80,81,82,75', '技术角色权限', 0, 0);

-- ----------------------------
-- Table structure for dev_department
-- ----------------------------
DROP TABLE IF EXISTS `dev_department`;
CREATE TABLE `dev_department`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '部门名称',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级部门id',
  `leader_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '部门负责人ID',
  `phone` varchar(60) NOT NULL DEFAULT '' COMMENT '部门联系电话',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '部门组织';

-- ----------------------------
-- Records of dev_department
-- ----------------------------
INSERT INTO `dev_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (1, '董事会', 0, 0, '13688888888');
INSERT INTO `dev_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (2, '人事部', 1, 0, '13688888887');
INSERT INTO `dev_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (3, '产品部', 1, 0, '13688888886');
INSERT INTO `dev_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (4, '设计部', 1, 0, '13688888885');
INSERT INTO `dev_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (5, '研发部', 1, 0, '13688888884');
INSERT INTO `dev_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (6, '测试部', 1, 0, '13688888883');

-- ----------------------------
-- Table structure for dev_position
-- ----------------------------
DROP TABLE IF EXISTS `dev_position`;
CREATE TABLE `dev_position`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '岗位名称',
  `work_price` int(10) NOT NULL DEFAULT 0 COMMENT '工时单价',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '岗位职称';

-- ----------------------------
-- Records of dev_position
-- ----------------------------
INSERT INTO `dev_position` VALUES (1, '超级岗位', 1000, '超级岗位，不能轻易修改权限', 1, 0, 0);
INSERT INTO `dev_position` VALUES (2, '人事总监', 1000, '根据公司经营战略，制定公司发展战略规划并负责规划公司人力资源的整体发展，制订与公司经营战略相匹配的人力资源政策，实施公司日常运营及管理。', 1, 0, 0);
INSERT INTO `dev_position` VALUES (3, '产品总监', 1000, '整体研究、策划、设计和完善公司的各个产品，主要产品线策略制定、实施及产品生命周期管理，负责产品部日常管理工作。', 1, 0, 0);
INSERT INTO `dev_position` VALUES (4, '技术总监', 1000, '领导技术团队开发各类产品，解决技术问题，管理不同的项目，排期交付。负责技术部日常管理工作。', 1, 0, 0);
INSERT INTO `dev_position` VALUES (5, '后端工程师', 800, '参与软件开发和维护过程中重大技术问题的解决，参与软件首次安装调试、数据割接、用户培训和项目推广。', 1, 0, 0);
INSERT INTO `dev_position` VALUES (6, '前端工程师', 800, '将产品UI设计稿实现成网站产品，涵盖用户PC端、移动端网页，处理视觉和交互问题。', 1, 0, 0);
INSERT INTO `dev_position` VALUES (7, 'UI设计师', 600, '对软件的人机交互、操作逻辑、界面美观的整体设计工作，包括高级网页设计、移动应用界面设计等。', 1, 0, 0);
INSERT INTO `dev_position` VALUES (8, '产品专员', 600, '需求收集，需求分析，需求落地，项目跟踪，项目上线，数据跟踪以及对业务人员进行培训，协助运营、销售、客服等开展工作。', 1, 0, 0);

-- ----------------------------
-- Table structure for dev_position_group
-- ----------------------------
DROP TABLE IF EXISTS `dev_position_group`;
CREATE TABLE `dev_position_group`  (
  `pid` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '岗位id',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  UNIQUE INDEX `pid_group_id`(`pid`, `group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '权限分组和岗位的关联表';

-- ----------------------------
-- Records of dev_position_group
-- ----------------------------
INSERT INTO `dev_position_group` VALUES (1, 1, 1652439152, 0);
INSERT INTO `dev_position_group` VALUES (3, 3, 1652439665, 0);
INSERT INTO `dev_position_group` VALUES (4, 4, 1652439679, 0);
INSERT INTO `dev_position_group` VALUES (2, 2, 1652439719, 0);
INSERT INTO `dev_position_group` VALUES (5, 4, 1652454970, 0);
INSERT INTO `dev_position_group` VALUES (6, 4, 1652455036, 0);
INSERT INTO `dev_position_group` VALUES (7, 4, 1652455100, 0);
INSERT INTO `dev_position_group` VALUES (8, 3, 1652455195, 0);

-- ----------------------------
-- Table structure for dev_config
-- ----------------------------
DROP TABLE IF EXISTS `dev_config`;
CREATE TABLE `dev_config`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '配置名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '配置标识',
  `content` text NULL COMMENT '配置内容',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COMMENT = '系统配置表';

-- ----------------------------
-- Records of dev_config
-- ----------------------------
INSERT INTO `dev_config` VALUES (1, '网站配置', 'web', 'a:13:{s:11:\"admin_title\";s:9:\"勾股DEV\";s:4:\"logo\";s:34:\"/static/home/images/login_logo.png\";s:4:\"file\";s:0:\"\";s:6:\"domain\";s:24:\"https://dev.gougucms.com\";s:5:\"title\";s:9:\"勾股DEV\";s:3:\"icp\";s:23:\"粤ICP备1xxxxxx11号-1\";s:5:\"beian\";s:29:\"粤公网安备1xxxxxx11号-1\";s:9:\"copyright\";s:36:\"© 2022 gougucms.com GPL-3.0 license\";s:7:\"version\";s:6:\"2.5.15\";s:8:\"keywords\";s:9:\"勾股DEV\";s:4:\"desc\";s:574:\"勾股DEV是一款专为IT研发团队打造的项目管理与团队协作的系统工具，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。勾股DEV的产品理念：通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。  \";s:4:\"code\";s:0:\"\";s:2:\"id\";s:1:\"1\";}', 1, 1612514630, 1652436221);
INSERT INTO `dev_config` VALUES (2, '邮箱配置', 'email', 'a:8:{s:4:\"smtp\";s:11:\"smtp.qq.com\";s:9:\"smtp_port\";s:3:\"465\";s:9:\"smtp_user\";s:15:\"gougucms@qq.com\";s:8:\"smtp_pwd\";s:6:\"123456\";s:4:\"from\";s:24:\"勾股CMS系统管理员\";s:5:\"email\";s:18:\"admin@gougucms.com\";s:8:\"template\";s:574:\"勾股DEV是一款专为IT研发团队打造的项目管理与团队协作的系统工具，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。勾股DEV的产品理念：通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。  \";s:2:\"id\";s:1:\"2\";}', 1, 1612521657, 1652436255);
INSERT INTO `dev_config` VALUES (3, 'Api Token配置', 'token', 'a:5:{s:3:\"iss\";s:16:\"dev.gougucms.com\";s:3:\"aud\";s:8:\"gougudev\";s:7:\"secrect\";s:8:\"GOUGUDEV\";s:7:\"exptime\";s:4:\"3600\";s:2:\"id\";s:1:\"3\";}', 1, 1627313142, 1650254285);
INSERT INTO `dev_config` VALUES (4, '其他配置', 'other', 'a:3:{s:6:\"author\";s:15:\"勾股工作室\";s:7:\"version\";s:13:\"v2.2022.05.15\";s:2:\"id\";s:1:\"4\";}', 1, 1613725791, 1652436278);


-- ----------------------------
-- Table structure for dev_admin_log
-- ----------------------------
DROP TABLE IF EXISTS `dev_admin_log`;
CREATE TABLE `dev_admin_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '姓名',
  `type` varchar(80) NOT NULL DEFAULT '' COMMENT '操作类型',
  `action` varchar(80) NOT NULL DEFAULT '' COMMENT '操作动作',
  `subject` varchar(80) NOT NULL DEFAULT '' COMMENT '操作主体',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '操作标题',
  `content` text NULL COMMENT '操作描述',
  `module` varchar(32) NOT NULL DEFAULT '' COMMENT '模块',
  `controller` varchar(32) NOT NULL DEFAULT '' COMMENT '控制器',
  `function` varchar(32) NOT NULL DEFAULT '' COMMENT '方法',
  `rule_menu` varchar(255) NOT NULL DEFAULT '' COMMENT '节点权限路径',
  `ip` varchar(64) NOT NULL DEFAULT '' COMMENT '登录ip',
  `param_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作数据id',
  `param` text NULL COMMENT '参数json格式',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0删除 1正常',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '员工操作日志表';

-- ----------------------------
-- Table structure for dev_file
-- ----------------------------
DROP TABLE IF EXISTS `dev_file`;
CREATE TABLE `dev_file`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(15) NOT NULL DEFAULT '' COMMENT '所属模块',
  `sha1` varchar(60) NOT NULL COMMENT 'sha1',
  `md5` varchar(60) NOT NULL COMMENT 'md5',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '原始文件名',
  `filename` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名',
  `filepath` varchar(255) NOT NULL DEFAULT '' COMMENT '文件路径+文件名',
  `filesize` int(10) NOT NULL DEFAULT 0 COMMENT '文件大小',
  `fileext` varchar(10) NOT NULL DEFAULT '' COMMENT '文件后缀',
  `mimetype` varchar(100) NOT NULL DEFAULT '' COMMENT '文件类型',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上传会员ID',
  `uploadip` varchar(15) NOT NULL DEFAULT '' COMMENT '上传IP',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未审核1已审核-1不通过',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `admin_id` int(11) NOT NULL COMMENT '审核者id',
  `audit_time` int(11) NOT NULL DEFAULT 0 COMMENT '审核时间',
  `action` varchar(50) NOT NULL DEFAULT '' COMMENT '来源模块功能',
  `use` varchar(255) NULL DEFAULT NULL COMMENT '用处',
  `download` int(11) NOT NULL DEFAULT 0 COMMENT '下载量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '文件表';

-- ----------------------------
-- Table structure for dev_message
-- ----------------------------
DROP TABLE IF EXISTS `dev_message`;
CREATE TABLE `dev_message`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '消息主题',
  `template` tinyint(2) NOT NULL DEFAULT 0 COMMENT '消息模板,用于前端拼接消息,0普通消息,1产品,2项目,3任务,4公告',
  `content` text NULL COMMENT '消息内容',
  `from_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送人id',
  `to_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '接收人id',
  `type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '阅览人类型：1 人员 2部门 3岗位 4全部',
  `type_user` text NULL COMMENT '人员ID或部门ID或角色ID，全员则为空',
  `send_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送日期',
  `read_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读时间',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '来源发件id',
  `fid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '转发或回复消息关联id',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1已删除消息 0垃圾消息 1正常消息',
  `is_draft` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否是草稿：1正常消息 2草稿消息',
  `delete_source` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '垃圾消息来源： 1已发消息 2草稿消息 3已收消息',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  `module_name` varchar(30) NOT NULL COMMENT '模块',
  `controller_name` varchar(30) NOT NULL COMMENT '控制器',
  `action_name` varchar(30) NOT NULL COMMENT '方法',
  `action_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作模块数据的id（针对系统消息）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '消息表';

-- ----------------------------
-- Table structure for dev_message_file_interfix
-- ----------------------------
DROP TABLE IF EXISTS `dev_message_file_interfix`;
CREATE TABLE `dev_message_file_interfix`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mid` int(11) UNSIGNED NOT NULL COMMENT '消息id',
  `file_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '相关联附件id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '消息关联的附件表';


-- ----------------------------
-- Table structure for dev_work_cate
-- ----------------------------
DROP TABLE IF EXISTS `dev_work_cate`;
CREATE TABLE `dev_work_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '工作类型名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '工作类型';

-- ----------------------------
-- Records of dev_work_cate
-- ----------------------------
INSERT INTO `dev_work_cate` VALUES (1, '其他', 1, 1637987189, 0);
INSERT INTO `dev_work_cate` VALUES (2, '产品原型', 1, 1637987199, 0);
INSERT INTO `dev_work_cate` VALUES (3, 'UI设计', 1, 1638088518, 0);
INSERT INTO `dev_work_cate` VALUES (4, '技术开发', 1, 1637987199, 0);
INSERT INTO `dev_work_cate` VALUES (5, '测试相关', 1, 1637987199, 0);
INSERT INTO `dev_work_cate` VALUES (6, '运维相关', 1, 1637987199, 0);
INSERT INTO `dev_work_cate` VALUES (7, '撰写文档', 1, 1637987199, 0);
INSERT INTO `dev_work_cate` VALUES (8, '需求调研', 1, 1637987199, 0);
INSERT INTO `dev_work_cate` VALUES (9, '需求沟通', 1, 1637987199, 0);
INSERT INTO `dev_work_cate` VALUES (10, '参加会议', 1, 1637987199, 0);

-- ----------------------------
-- Table structure for dev_task_cate
-- ----------------------------
DROP TABLE IF EXISTS `dev_task_cate`;
CREATE TABLE `dev_task_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '任务类型名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '任务类型';

-- ----------------------------
-- Records of dev_task_cate
-- ----------------------------
INSERT INTO `dev_task_cate` VALUES (1, '需求', 1, 1637987189, 0);
INSERT INTO `dev_task_cate` VALUES (2, '设计', 1, 1637987199, 0);
INSERT INTO `dev_task_cate` VALUES (3, '研发', 1, 1638088518, 0);
INSERT INTO `dev_task_cate` VALUES (4, '缺陷', 1, 1637987199, 0);

-- ----------------------------
-- Table structure for dev_note_cate
-- ----------------------------
DROP TABLE IF EXISTS `dev_note_cate`;
CREATE TABLE `dev_note_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT 0 COMMENT '父类ID',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '排序',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '公告分类';

-- ----------------------------
-- Records of dev_note_cate
-- ----------------------------
INSERT INTO `dev_note_cate` VALUES (1, 0, 1, '普通公告', 1637984265, 1637984299,1);
INSERT INTO `dev_note_cate` VALUES (2, 0, 2, '紧急公告', 1637984283, 1637984310,1);

-- ----------------------------
-- Table structure for dev_note
-- ----------------------------
DROP TABLE IF EXISTS `dev_note`;
CREATE TABLE `dev_note`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `cate_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联分类ID',
  `title` varchar(225) NULL DEFAULT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `md_content` text NOT NULL COMMENT 'markdown内容',
  `src` varchar(100) NULL DEFAULT NULL COMMENT '关联链接',
  `sort` int(11) NOT NULL DEFAULT 0,
  `read` int(11) NOT NULL DEFAULT 0,
  `admin_id` int(11) NOT NULL DEFAULT 0,
  `start_time` int(11) NOT NULL DEFAULT 0 COMMENT '展示开始时间',
  `end_time` int(11) NOT NULL DEFAULT 0 COMMENT '展示结束时间',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  `delete_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '公告';

-- ----------------------------
-- Records of dev_note
-- ----------------------------
INSERT INTO `dev_note` VALUES (1000, 1, '公司内部从5月份开始使用勾股DEV进行项目研发管理通知', '<p>经过公司开会讨论决定，公司技术部门从5月份开始使用勾股DEV进行项目研发管理。</p>\n<p>勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。</p>\n<p><strong>勾股DEV的产品理念：</strong><br>通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。</p>\n', '经过公司开会讨论决定，公司技术部门从5月份开始使用勾股DEV进行项目研发管理。\n\n勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。\n\n**勾股DEV的产品理念：**\n通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。', '', 1,0,1, 1651334400, 1756483200, 1652455674, 1652455991, 0);
INSERT INTO `dev_note` VALUES (1001, 1, '勾股DEV2.0于2022年5月15日正式对外开源发布', '<p>从勾股1.0版本到勾股DEV2.0版本，历时20多天的开发，终于2022年5月15日正式对外开源发布。</p>\n<p><strong>产品简介：</strong><br>勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。</p>\n<p><strong>产品理念：</strong><br>通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。</p>\n<p><strong>系统特点：</strong></p>\n<ul>\n<li>多产品支持，可添加多产品管理</li><li>多项目支持，可以多项目同时进行管理</li><li>可配置的用户角色控制，不同的角色可配置不同的操作权限</li><li>Wiki 形式的文档撰写，Mardown编辑器，工程师使用高效便捷</li><li>每个项目配置有需求、任务、Wiki文档、动态记录、互动评论、工作记录模块</li><li>任务时间跟踪机制，项目任务多状态流转，任务成果可见可控。</li><li>工时登记，团队精细化管理，可统计每个人每天在每个项目做了多少时间</li><li>任务安排，任务分配指定人，可设置负责人、多协同人员</li><li>员工的操作记录全覆盖跟踪</li></ul>\n', '从勾股1.0版本到勾股DEV2.0版本，历时20多天的开发，终于2022年5月15日正式对外开源发布。\n\n**产品简介：**\n勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。\n\n**产品理念：**\n通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。\n\n**系统特点：**\n- 多产品支持，可添加多产品管理\n- 多项目支持，可以多项目同时进行管理\n- 可配置的用户角色控制，不同的角色可配置不同的操作权限\n- Wiki 形式的文档撰写，Mardown编辑器，工程师使用高效便捷\n- 每个项目配置有需求、任务、Wiki文档、动态记录、互动评论、工作记录模块\n- 任务时间跟踪机制，项目任务多状态流转，任务成果可见可控。\n- 工时登记，团队精细化管理，可统计每个人每天在每个项目做了多少时间\n- 任务安排，任务分配指定人，可设置负责人、多协同人员\n- 员工的操作记录全覆盖跟踪', '', 2,0,1, 1652544000, 1756483200, 1652455899, 0, 0);

-- ----------------------------
-- Table structure for dev_schedule
-- ----------------------------
DROP TABLE IF EXISTS `dev_schedule`;
CREATE TABLE `dev_schedule`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '工作记录主题',
  `tid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联任务ID',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建员工ID',
  `start_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '开始时间',
  `end_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '结束时间',
  `labor_time` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '工时',
  `remark` text NOT NULL COMMENT '描述',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '工作记录';

-- ----------------------------
-- Table structure for dev_work
-- ----------------------------
DROP TABLE IF EXISTS `dev_work`;
CREATE TABLE `dev_work`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '类型：1 日报 2周报 3月报',
  `type_user` text NULL COMMENT '接受人员ID',
  `works` text NULL COMMENT '汇报工作内容',
  `plans` text NULL COMMENT '计划工作内容',
  `remark` text NULL COMMENT '其他事项',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人id',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '汇报工作表';

-- ----------------------------
-- Table structure for dev_work_file_interfix
-- ----------------------------
DROP TABLE IF EXISTS `dev_work_file_interfix`;
CREATE TABLE `dev_work_file_interfix`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `wid` int(11) UNSIGNED NOT NULL COMMENT '汇报工作id',
  `file_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '相关联附件id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '汇报工作关联的附件表';

-- ----------------------------
-- Table structure for dev_work_record
-- ----------------------------
DROP TABLE IF EXISTS `dev_work_record`;
CREATE TABLE `dev_work_record`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `wid` int(11) UNSIGNED NOT NULL COMMENT '汇报工作id',
  `from_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送人id',
  `to_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '接收人id',
  `send_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送日期',
  `read_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '汇报工作发送记录表';

-- ----------------------------
-- Table structure for dev_knowledge_cate
-- ----------------------------
DROP TABLE IF EXISTS `dev_knowledge_cate`;
CREATE TABLE `dev_knowledge_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT 0 COMMENT '父类ID',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '排序',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '分类标题',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '描述',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '知识库分类表';

-- ----------------------------
-- Records of dev_knowledge_cate
-- ----------------------------
INSERT INTO `dev_knowledge_cate` VALUES (1, 0, 0, '办公技巧', '', 1637984651, 0, 1);
INSERT INTO `dev_knowledge_cate` VALUES (2, 0, 0, '行业技能', '', 1637984739, 0, 1);

-- ----------------------------
-- Table structure for dev_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `dev_knowledge`;
CREATE TABLE `dev_knowledge`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '作者',
  `cate_id` int(11) NOT NULL DEFAULT 0 COMMENT '分类id',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `thumb` varchar(255) NOT NULL DEFAULT '' COMMENT '缩略图',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '描述',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '排序',
  `is_share` int(1) NOT NULL DEFAULT 1 COMMENT '是否公开:1是2否',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '知识库分类表';

-- ----------------------------
-- Records of dev_knowledge
-- ----------------------------
INSERT INTO `dev_knowledge` VALUES (1000, 1, 2, '勾股CMS在线文档', '', '勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。系统后台各管理模块，一目了然，操作简单；通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，极低门槛、开箱即用。', 0, 1, 1652498088, 1652499832, 0);
INSERT INTO `dev_knowledge` VALUES (1001, 1, 2, '勾股BLOG在线文档', '', '勾股博客是一款基于ThinkPHP6 + Layui + MySql打造的，简单实用的开源免费的博客系统。博客后台各管理模块，操作简单，博客前台具有简约，易用，访问统计，内存占用低等特点。博客除了基本的博客文章功能，还具备类似微博的分享简短实时个人动态信息的功能、及类似阿里语雀的文档功能，知识可按目录、章节归类整理分享。', 0, 1, 1652499703, 0, 0);
INSERT INTO `dev_knowledge` VALUES (1002, 1, 2, '勾股OA在线文档', '', '勾股OA是一款基于ThinkPHP6 + Layui + MySql打造的简单实用的开源免费的企业办公系统框架。可以帮助解决企业办公项目60%的重复工作，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。使用勾股OA可以简单快速地开发出企业级的Web应用系统。', 0, 1, 1652499886, 0, 0);
INSERT INTO `dev_knowledge` VALUES (1003, 1, 2, '勾股DEV在线文档', '', '勾股DEV是一款研发管理、团队协作的工具及解决方案，结合 「管理」+「协作」设计理念 ，管理团队的工作、项目和任务，贯穿“从需求提出到研发完成上线”整个产品研发全生命周期，促进产品、研发、测试、运维等产品研发过程中各角色的良好协作。', 0, 1, 1652500033, 0, 0);


-- ----------------------------
-- Table structure for dev_knowledge_doc
-- ----------------------------
DROP TABLE IF EXISTS `dev_knowledge_doc`;
CREATE TABLE `dev_knowledge_doc`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT 0 COMMENT '父章节',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ids',
  `type` int(1) NOT NULL DEFAULT 1 COMMENT '类型:1文档2章节3跳转链接',
  `knowledge_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联知识文件夹id',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '摘要',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '作者',
  `link` varchar(255) NOT NULL DEFAULT '' COMMENT '跳转地址',
  `content` text NULL COMMENT '内容',
  `md_content` text NULL COMMENT 'markdown内容',
  `read` int(11) NOT NULL DEFAULT 0 COMMENT '阅读量',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '知识库文档表';

-- ----------------------------
-- Records of dev_knowledge_doc
-- ----------------------------
INSERT INTO `dev_knowledge_doc` VALUES (1000, 0, '勾股CMS简介','', 1, 1000, '', 1, '', '<h2 id=\"h2--cms2-0\"><a name=\"📐 勾股CMS2.0\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>📐 勾股CMS2.0</h2><p><img src=\"https://www.gougucms.com/storage/image/slogo.jpg\" alt=\"输入图片说明\"></p>\n<h3 id=\"h3--\"><a name=\"✅ 相关链接\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✅ 相关链接</h3><ul>\n<li><p>Gitee：<a href=\"https://gitee.com/gougucms/gougucms.git\">https://gitee.com/gougucms/gougucms.git</a></p>\n</li><li><p><strong>项目会不定时进行更新，建议⭐star⭐和👁️watch👁️一份</strong></p>\n</li><li><p>演示地址：<a href=\"https://www.gougucms.com\">https://www.gougucms.com</a></p>\n</li><li>文档地址：<a href=\"https://blog.gougucms.com/home/book/detail/bid/1.html\">https://blog.gougucms.com/home/book/detail/bid/1.html</a></li><li>后台体验地址：<a href=\"https://www.gougucms.com/admin/index/index.html\">https://www.gougucms.com/admin/index/index.html</a></li><li>后台体验账号：gougucms     密码：gougucms</li><li>开发交流QQ群：24641076</li></ul>\n<h3 id=\"h3--\"><a name=\"📋 系统介绍\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>📋 系统介绍</h3><ul>\n<li>勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。</li><li>系统后台各管理模块，一目了然，操作简单；通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，极低门槛、开箱即用。</li><li>系统整合了经典富文本编辑器（ueditor）与现今流行的Mardown编辑器（editor.md）于自身，可在后台配置根据习惯切换不同的编辑器。</li><li>系统易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。</li><li>可以快速基于此系统进行ThinkPHP6的快速开发，免去每次都写一次后台基础的痛苦，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。</li><li>可去版权，真正意义的永久免费，可商用的后台开发框架。</li></ul>\n<h3 id=\"h3--\"><a name=\"✳️ 功能矩阵\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✳️ 功能矩阵</h3><p><img src=\"https://www.gougucms.com/storage/image/gougucms2.0.png\" alt=\"功能导图\" title=\"功能导图\"><br>系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限管理、功能管理（后台菜单管理）、导航设置、网站地图、轮播广告、TAG关键字管理、搜索关键字管理、文件上传、数据备份/还原、文章功能、商品功能、用户管理、用户操作日志、用户注册/登录、 API接口等。更多的个性化功能可以基于当前系统便捷做二次开发。</p>\n', '## 📐 勾股CMS2.0\n![输入图片说明](https://www.gougucms.com/storage/image/slogo.jpg)\n\n### ✅ 相关链接\n- Gitee：https://gitee.com/gougucms/gougucms.git\n\n- **项目会不定时进行更新，建议⭐star⭐和👁️watch👁️一份**\n\n- 演示地址：https://www.gougucms.com\n- 文档地址：[https://blog.gougucms.com/home/book/detail/bid/1.html](https://blog.gougucms.com/home/book/detail/bid/1.html)\n- 后台体验地址：[https://www.gougucms.com/admin/index/index.html](https://www.gougucms.com/admin/index/index.html)\n- 后台体验账号：gougucms     密码：gougucms\n- 开发交流QQ群：24641076\n\n### 📋 系统介绍\n\n- 勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。\n- 系统后台各管理模块，一目了然，操作简单；通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，极低门槛、开箱即用。\n- 系统整合了经典富文本编辑器（ueditor）与现今流行的Mardown编辑器（editor.md）于自身，可在后台配置根据习惯切换不同的编辑器。\n- 系统易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。\n- 可以快速基于此系统进行ThinkPHP6的快速开发，免去每次都写一次后台基础的痛苦，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。\n- 可去版权，真正意义的永久免费，可商用的后台开发框架。\n\n### ✳️ 功能矩阵\n\n![功能导图](https://www.gougucms.com/storage/image/gougucms2.0.png \"功能导图\")\n系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限管理、功能管理（后台菜单管理）、导航设置、网站地图、轮播广告、TAG关键字管理、搜索关键字管理、文件上传、数据备份/还原、文章功能、商品功能、用户管理、用户操作日志、用户注册/登录、 API接口等。更多的个性化功能可以基于当前系统便捷做二次开发。', 6, 0, 1652498154, 1652498733, 0);
INSERT INTO `dev_knowledge_doc` VALUES (1001, 0, '勾股BLOG简介','', 1, 1001, '', 1, '', '<h2 id=\"h2--blog\"><a name=\"📐 勾股BLOG\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>📐 勾股BLOG</h2><p><img src=\"https://blog.gougucms.com/storage/image/slogo.jpg\" alt=\"输入图片说明\"></p>\n<h3 id=\"h3--\"><a name=\"✅ 相关链接\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✅ 相关链接</h3><ul>\n<li>Gitee：<a href=\"https://gitee.com/gougucms/blog.git\">https://gitee.com/gougucms/blog.git</a></li><li><p><strong>项目会不定时进行更新，建议⭐star⭐和👁️watch👁️一份</strong></p>\n</li><li><p>演示地址：<a href=\"https://blog.gougucms.com\">https://blog.gougucms.com</a></p>\n</li><li>文档地址：<a href=\"https://blog.gougucms.com/home/book/detail/bid/1.html\">https://blog.gougucms.com/home/book/detail/bid/1.html</a></li><li><p>后台体验地址：<a href=\"https://blog.gougucms.com/admin/index/index.html\">https://blog.gougucms.com/admin/index/index.html</a></p>\n</li><li><p>后台体验账号：gougublog     密码：gougublog</p>\n</li><li>开发交流QQ群：24641076</li></ul>\n<h3 id=\"h3--\"><a name=\"📋 系统介绍\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>📋 系统介绍</h3><ol>\n<li>勾股博客是一款基于ThinkPHP6 + Layui + MySql打造的，简单实用的开源免费的博客系统。</li><li>博客后台各管理模块，一目了然，操作简单，博客前台具有简约，易用，访问统计，内存占用低等特点。</li><li>博客除了基本的博客文章功能，还具备类似微博的分享简短实时个人动态信息的功能、及类似阿里语雀的文档功能，知识可按目录、章节归类整理分享。</li><li>博客整合了经典富文本编辑器（ueditor）与现今流行的Mardown编辑器（editor.md）于自身，可以在后台配置根据自己的使用习惯切换不同的编辑器。其中Mardown编辑器支持截图粘贴上传图片，支持拖拽图片文件上传。</li><li>系统易于功能扩展，代码维护，方便二次开发。可以用来做个人博客，工作室官网，小说网站，自媒体官网等网站，二次开发之后也可以作为资讯、展品展示等门户网站。</li></ol>\n', '## 📐 勾股BLOG\n![输入图片说明](https://blog.gougucms.com/storage/image/slogo.jpg)\n\n### ✅ 相关链接\n- Gitee：https://gitee.com/gougucms/blog.git\n- **项目会不定时进行更新，建议⭐star⭐和👁️watch👁️一份**\n\n- 演示地址：https://blog.gougucms.com\n- 文档地址：https://blog.gougucms.com/home/book/detail/bid/1.html\n- 后台体验地址：https://blog.gougucms.com/admin/index/index.html\n\n- 后台体验账号：gougublog     密码：gougublog\n- 开发交流QQ群：24641076\n\n### 📋 系统介绍\n\n1. 勾股博客是一款基于ThinkPHP6 + Layui + MySql打造的，简单实用的开源免费的博客系统。\n2. 博客后台各管理模块，一目了然，操作简单，博客前台具有简约，易用，访问统计，内存占用低等特点。\n3. 博客除了基本的博客文章功能，还具备类似微博的分享简短实时个人动态信息的功能、及类似阿里语雀的文档功能，知识可按目录、章节归类整理分享。\n4. 博客整合了经典富文本编辑器（ueditor）与现今流行的Mardown编辑器（editor.md）于自身，可以在后台配置根据自己的使用习惯切换不同的编辑器。其中Mardown编辑器支持截图粘贴上传图片，支持拖拽图片文件上传。\n5. 系统易于功能扩展，代码维护，方便二次开发。可以用来做个人博客，工作室官网，小说网站，自媒体官网等网站，二次开发之后也可以作为资讯、展品展示等门户网站。', 1, 0, 1652499722, 1652499786, 0);
INSERT INTO `dev_knowledge_doc` VALUES (1002, 0, '勾股OA简介','', 1, 1002, '', 1, '', '<h2 id=\"h2--oa\"><a name=\"📐 勾股OA\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>📐 勾股OA</h2><p><img src=\"https://oa.gougucms.com/storage/image/slogo.jpg\" alt=\"勾股OA\"></p>\n<h4 id=\"h4--\"><a name=\"✅ 相关链接\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✅ 相关链接</h4><ul>\n<li>gitee：<a href=\"https://gitee.com/gougucms/office.git\">https://gitee.com/gougucms/office.git</a></li><li><strong>项目会不定时进行更新，建议⭐star⭐和👁️watch👁️一份。</strong></li></ul>\n<h4 id=\"h4--\"><a name=\"📋 系统介绍\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>📋 系统介绍</h4><p>勾股OA是一款基于ThinkPHP6 + Layui + MySql打造的简单实用的开源免费的企业办公系统框架。可以帮助解决企业办公项目60%的重复工作，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。使用勾股OA可以简单快速地开发出企业级的Web应用系统。</p>\n<h4 id=\"h4--\"><a name=\"✳️ 演示地址\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✳️ 演示地址</h4><p>勾股OA演示地址：<a href=\"https://oa.gougucms.com\">https://oa.gougucms.com</a></p>\n<p>PS：为了给后面的人提供良好的演示体验，体验以查看为主，如果确实需要填写数据，大家最好填些看似正常的数据，请不要乱填数据，比如：1111，aaa那些数据就不要乱来了。<br>如果大家不遵守体验规则，后期发现很多乱的数据的话，就关闭对应的填写权限了。</p>\n<p> <strong>登录账号及密码：</strong></p>\n<pre><code>   BOSS角色：suhaizhen     123456\n   人事总监：fengcailing    123456\n   财务总监：yucixin        123456\n   市场总监：qinjiaxian     123456\n   技术总监：yexiaochai     123456\n</code></pre><h4 id=\"h4--\"><a name=\"✴️ 系统特点\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✴️ 系统特点</h4><ul>\n<li>系统各功能模块，一目了然，操作简单；通用型的后台权限管理框架，员工的操作记录全覆盖跟踪，紧随潮流、极低门槛、开箱即用。</li><li>系统集成了十大办公基本的功能模块：系统管理、基础数据、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能等基础模块。</li><li>系统方便二次开发，易于功能扩展，代码维护，满足专注业务深度开发的需求。</li><li>开发人员可以快速基于此系统进行二次开发，免去写一次系统架构的痛苦，帮助开发者高效降低开发的成本，通过二次开发之后可以用来做CRM，ERP，项目管理等企业办公系统。</li></ul>\n', '## 📐 勾股OA\n\n![勾股OA](https://oa.gougucms.com/storage/image/slogo.jpg)\n\n#### ✅ 相关链接\n- gitee：https://gitee.com/gougucms/office.git\n- **项目会不定时进行更新，建议⭐star⭐和👁️watch👁️一份。**\n\n\n#### 📋 系统介绍\n勾股OA是一款基于ThinkPHP6 + Layui + MySql打造的简单实用的开源免费的企业办公系统框架。可以帮助解决企业办公项目60%的重复工作，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。使用勾股OA可以简单快速地开发出企业级的Web应用系统。\n\n#### ✳️ 演示地址\n\n勾股OA演示地址：[https://oa.gougucms.com](https://oa.gougucms.com)\n\nPS：为了给后面的人提供良好的演示体验，体验以查看为主，如果确实需要填写数据，大家最好填些看似正常的数据，请不要乱填数据，比如：1111，aaa那些数据就不要乱来了。\n如果大家不遵守体验规则，后期发现很多乱的数据的话，就关闭对应的填写权限了。\n\n **登录账号及密码：**\n~~~\n   BOSS角色：suhaizhen     123456\n   人事总监：fengcailing    123456\n   财务总监：yucixin        123456\n   市场总监：qinjiaxian     123456\n   技术总监：yexiaochai     123456\n~~~\n\n#### ✴️ 系统特点\n- 系统各功能模块，一目了然，操作简单；通用型的后台权限管理框架，员工的操作记录全覆盖跟踪，紧随潮流、极低门槛、开箱即用。\n- 系统集成了十大办公基本的功能模块：系统管理、基础数据、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能等基础模块。\n- 系统方便二次开发，易于功能扩展，代码维护，满足专注业务深度开发的需求。\n- 开发人员可以快速基于此系统进行二次开发，免去写一次系统架构的痛苦，帮助开发者高效降低开发的成本，通过二次开发之后可以用来做CRM，ERP，项目管理等企业办公系统。', 1, 0, 1652499900, 1652499948, 0);
INSERT INTO `dev_knowledge_doc` VALUES (1003, 0, '勾股DEV简介','', 1, 1003, '', 1, '', '<h2 id=\"h2--dev\"><a name=\"📐 勾股DEV\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>📐 勾股DEV</h2><p><img src=\"https://dev.gougucms.com/storage/202204/banner.jpg\" alt=\"输入图片说明\"></p>\n<h3 id=\"h3--\"><a name=\"✅ 相关链接\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✅ 相关链接</h3><ul>\n<li>gitee：<a href=\"https://gitee.com/gougucms/dev.git\">https://gitee.com/gougucms/dev.git</a></li><li>项目会不定时进行更新，建议⭐star⭐和👁️watch👁️一份。</li></ul>\n<h3 id=\"h3--\"><a name=\"📋 系统介绍\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>📋 系统介绍</h3><p>软件研发是个复杂的过程，需要协调管理者、产品、研发、测试、运维等多个角色及部门的目标一致和高效协同，需要合理的资源分配、把控项目进度、控制风险、保障产品交付质量。</p>\n<p>勾股DEV是一款研发管理、团队协作的工具及解决方案，<strong>结合 「管理」+「协作」设计理念</strong> ，管理团队的工作、项目和任务，贯穿“从需求提出到研发完成上线”整个产品研发全生命周期，促进产品、研发、测试、运维等产品研发过程中各角色的良好协作。</p>\n<p>勾股DEV的产品理念：通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。</p>\n<h3 id=\"h3--\"><a name=\"✳️ 演示地址\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✳️ 演示地址</h3><p>勾股DEV演示地址：<a href=\"https://dev.gougucms.com\">https://dev.gougucms.com</a></p>\n<p>PS：为了给后面的人提供良好的演示体验，体验以查看为主。</p>\n<p><strong>登录账号及密码：</strong></p>\n<pre><code>   技术总监：gougujishu     123456\n   工 程 师：ligong    123456\n   产品经理：ouyangchanpin        123456\n</code></pre><h3 id=\"h3--\"><a name=\"✴️ 系统特点\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>✴️ 系统特点</h3><ul>\n<li>多产品支持，可添加多产品管理</li><li>多项目支持，可以多项目同时进行管理</li><li>可配置的用户角色控制，不同的角色可配置不同的操作权限</li><li>Wiki 形式的文档撰写，Mardown编辑器，工程师使用高效便捷</li><li>每个项目配置有需求、任务、Wiki文档、动态记录、互动评论、工作记录模块</li><li>任务时间跟踪机制，项目任务多状态流转，任务成果可见可控。</li><li>工时登记，团队精细化管理，可统计每个人每天在每个项目做了多少时间</li><li>任务安排，任务分配指定人，可设置负责人、多协同人员</li><li>员工的操作记录全覆盖跟踪</li></ul>\n', '## 📐 勾股DEV\n![输入图片说明](https://dev.gougucms.com/storage/202204/banner.jpg)\n\n### ✅ 相关链接\n- gitee：https://gitee.com/gougucms/dev.git\n- 项目会不定时进行更新，建议⭐star⭐和👁️watch👁️一份。\n\n### 📋 系统介绍\n软件研发是个复杂的过程，需要协调管理者、产品、研发、测试、运维等多个角色及部门的目标一致和高效协同，需要合理的资源分配、把控项目进度、控制风险、保障产品交付质量。\n\n勾股DEV是一款研发管理、团队协作的工具及解决方案，**结合 「管理」+「协作」设计理念** ，管理团队的工作、项目和任务，贯穿“从需求提出到研发完成上线”整个产品研发全生命周期，促进产品、研发、测试、运维等产品研发过程中各角色的良好协作。\n\n勾股DEV的产品理念：通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。\n\n### ✳️ 演示地址\n\n勾股DEV演示地址：[https://dev.gougucms.com](https://dev.gougucms.com)\n\nPS：为了给后面的人提供良好的演示体验，体验以查看为主。\n\n**登录账号及密码：**\n~~~\n   技术总监：gougujishu     123456\n   工 程 师：ligong    123456\n   产品经理：ouyangchanpin        123456\n~~~\n\n### ✴️ 系统特点\n- 多产品支持，可添加多产品管理\n- 多项目支持，可以多项目同时进行管理\n- 可配置的用户角色控制，不同的角色可配置不同的操作权限\n- Wiki 形式的文档撰写，Mardown编辑器，工程师使用高效便捷\n- 每个项目配置有需求、任务、Wiki文档、动态记录、互动评论、工作记录模块\n- 任务时间跟踪机制，项目任务多状态流转，任务成果可见可控。\n- 工时登记，团队精细化管理，可统计每个人每天在每个项目做了多少时间\n- 任务安排，任务分配指定人，可设置负责人、多协同人员\n- 员工的操作记录全覆盖跟踪', 2, 0, 1652500048, 1652500097, 0);


-- ----------------------------
-- Table structure for dev_product
-- ----------------------------
DROP TABLE IF EXISTS `dev_product`;
CREATE TABLE `dev_product`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '产品名称',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `director_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '产品负责人',
  `check_admin_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '评审人，如:1,2,3',
  `is_open` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否公开：1是,2否',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0关闭,1开启',
  `content` text NULL COMMENT '产品描述',
  `md_content` text NULL COMMENT 'markdown产品描述',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '产品表';

-- ----------------------------
-- Records of dev_product
-- ----------------------------
INSERT INTO `dev_product` VALUES (1000, '勾股CMS', 1, 1, '1', 2, 1, '<p>勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。<br>系统后台各管理模块，一目了然，操作简单；通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，极低门槛、开箱即用。<br>系统整合了经典富文本编辑器（ueditor）与现今流行的Mardown编辑器（editor.md）于自身，可在后台配置根据习惯切换不同的编辑器。<br>系统易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。<br>可以快速基于此系统进行ThinkPHP6的快速开发，免去每次都写一次后台基础的痛苦，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。</p>\n<p>系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限管理、功能管理（后台菜单管理）、导航设置、网站地图、轮播广告、TAG关键字管理、搜索关键字管理、文件上传、数据备份/还原、文章功能、商品功能、用户管理、用户操作日志、用户注册/登录、 API接口等。更多的个性化功能可以基于当前系统便捷做二次开发。</p>\n', '勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。\n系统后台各管理模块，一目了然，操作简单；通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，极低门槛、开箱即用。\n系统整合了经典富文本编辑器（ueditor）与现今流行的Mardown编辑器（editor.md）于自身，可在后台配置根据习惯切换不同的编辑器。\n系统易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。\n可以快速基于此系统进行ThinkPHP6的快速开发，免去每次都写一次后台基础的痛苦，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。\n\n系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限管理、功能管理（后台菜单管理）、导航设置、网站地图、轮播广告、TAG关键字管理、搜索关键字管理、文件上传、数据备份/还原、文章功能、商品功能、用户管理、用户操作日志、用户注册/登录、 API接口等。更多的个性化功能可以基于当前系统便捷做二次开发。', 1652456581, 1652494140, 0);
INSERT INTO `dev_product` VALUES (1001, '勾股BLOG', 1, 1, '1', 2, 1, '<p>勾股博客是一款基于ThinkPHP6 + Layui + MySql打造的，简单实用的开源免费的博客系统。<br>博客后台各管理模块，一目了然，操作简单，博客前台具有简约，易用，访问统计，内存占用低等特点。<br>博客除了基本的博客文章功能，还具备类似微博的分享简短实时个人动态信息的功能、及类似阿里语雀的文档功能，知识可按目录、章节归类整理分享。<br>博客整合了经典富文本编辑器（ueditor）与现今流行的Mardown编辑器（editor.md）于自身，可以在后台配置根据自己的使用习惯切换不同的编辑器。其中Mardown编辑器支持截图粘贴上传图片，支持拖拽图片文件上传。<br>系统易于功能扩展，代码维护，方便二次开发。可以用来做个人博客，工作室官网，小说网站，自媒体官网等网站，二次开发之后也可以作为资讯、展品展示等门户网站。</p>\n<p><strong>功能矩阵</strong><br>系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限、功能管理（后台菜单管理）、导航设置、网站地图、轮播广告、TAG关键字管理、友情链接、文件上传、数据备份/还原、博客文章功能、语雀文档功能、用户管理、用户操作日志、用户注册/登录、 博客归档、博客动态、访问统计等。更多的个性化功能可以基于当前博客系统便捷做二次开发。</p>\n', '勾股博客是一款基于ThinkPHP6 + Layui + MySql打造的，简单实用的开源免费的博客系统。\n博客后台各管理模块，一目了然，操作简单，博客前台具有简约，易用，访问统计，内存占用低等特点。\n博客除了基本的博客文章功能，还具备类似微博的分享简短实时个人动态信息的功能、及类似阿里语雀的文档功能，知识可按目录、章节归类整理分享。\n博客整合了经典富文本编辑器（ueditor）与现今流行的Mardown编辑器（editor.md）于自身，可以在后台配置根据自己的使用习惯切换不同的编辑器。其中Mardown编辑器支持截图粘贴上传图片，支持拖拽图片文件上传。\n系统易于功能扩展，代码维护，方便二次开发。可以用来做个人博客，工作室官网，小说网站，自媒体官网等网站，二次开发之后也可以作为资讯、展品展示等门户网站。\n\n**功能矩阵**\n系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限、功能管理（后台菜单管理）、导航设置、网站地图、轮播广告、TAG关键字管理、友情链接、文件上传、数据备份/还原、博客文章功能、语雀文档功能、用户管理、用户操作日志、用户注册/登录、 博客归档、博客动态、访问统计等。更多的个性化功能可以基于当前博客系统便捷做二次开发。', 1652456704, 1652494130, 0);
INSERT INTO `dev_product` VALUES (1002, '勾股OA', 1, 1, '1', 2, 1, '<p>勾股OA是一款基于ThinkPHP6 + Layui + MySql打造的简单实用的开源免费的企业办公系统框架。可以帮助解决企业办公项目60%的重复工作，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。使用勾股OA可以简单快速地开发出企业级的Web应用系统。</p>\n<p><strong>系统特点</strong><br>系统各功能模块，一目了然，操作简单；通用型的后台权限管理框架，员工的操作记录全覆盖跟踪，紧随潮流、极低门槛、开箱即用。<br>系统集成了十大办公基本的功能模块：系统管理、基础数据、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能等基础模块。<br>系统方便二次开发，易于功能扩展，代码维护，满足专注业务深度开发的需求。<br>开发人员可以快速基于此系统进行二次开发，免去写一次系统架构的痛苦，帮助开发者高效降低开发的成本，通过二次开发之后可以用来做CRM，ERP，项目管理等企业办公系统。</p>\n<p><strong>功能矩阵</strong></p>\n<p>系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限、职位职称、功能菜单、模块管理、关键字管理、文件上传、数据备份/还原、基础数据、审批流程、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能、 API接口等。更多的个性化功能可以基于当前系统便捷做二次开发。</p>\n', '勾股OA是一款基于ThinkPHP6 + Layui + MySql打造的简单实用的开源免费的企业办公系统框架。可以帮助解决企业办公项目60%的重复工作，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。使用勾股OA可以简单快速地开发出企业级的Web应用系统。\n\n**系统特点**\n系统各功能模块，一目了然，操作简单；通用型的后台权限管理框架，员工的操作记录全覆盖跟踪，紧随潮流、极低门槛、开箱即用。\n系统集成了十大办公基本的功能模块：系统管理、基础数据、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能等基础模块。\n系统方便二次开发，易于功能扩展，代码维护，满足专注业务深度开发的需求。\n开发人员可以快速基于此系统进行二次开发，免去写一次系统架构的痛苦，帮助开发者高效降低开发的成本，通过二次开发之后可以用来做CRM，ERP，项目管理等企业办公系统。\n\n**功能矩阵**\n\n系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限、职位职称、功能菜单、模块管理、关键字管理、文件上传、数据备份/还原、基础数据、审批流程、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能、 API接口等。更多的个性化功能可以基于当前系统便捷做二次开发。', 1652456965, 1652494118, 0);
INSERT INTO `dev_product` VALUES (1003, '勾股DEV', 1, 1, '1', 2, 1, '<p>勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。</p>\n<p><strong>产品理念：</strong><br>通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。</p>\n<p><strong>系统特点</strong></p>\n<ul>\n<li>多产品支持，可添加多产品管理</li><li>多项目支持，可以多项目同时进行管理</li><li>可配置的用户角色控制，不同的角色可配置不同的操作权限</li><li>Wiki 形式的文档撰写，Mardown编辑器，工程师使用高效便捷</li><li>每个项目配置有需求、任务、Wiki文档、动态记录、互动评论、工作记录模块</li><li>任务时间跟踪机制，项目任务多状态流转，任务成果可见可控。</li><li>工时登记，团队精细化管理，可统计每个人每天在每个项目做了多少时间</li><li>任务安排，任务分配指定人，可设置负责人、多协同人员</li><li>员工的操作记录全覆盖跟踪</li></ul>\n', '勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。\n\n**产品理念：**\n通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。\n\n**系统特点**\n- 多产品支持，可添加多产品管理\n- 多项目支持，可以多项目同时进行管理\n- 可配置的用户角色控制，不同的角色可配置不同的操作权限\n- Wiki 形式的文档撰写，Mardown编辑器，工程师使用高效便捷\n- 每个项目配置有需求、任务、Wiki文档、动态记录、互动评论、工作记录模块\n- 任务时间跟踪机制，项目任务多状态流转，任务成果可见可控。\n- 工时登记，团队精细化管理，可统计每个人每天在每个项目做了多少时间\n- 任务安排，任务分配指定人，可设置负责人、多协同人员\n- 员工的操作记录全覆盖跟踪', 1652457166, 1652494101, 0);

-- ----------------------------
-- Table structure for dev_project
-- ----------------------------
DROP TABLE IF EXISTS `dev_project`;
CREATE TABLE `dev_project`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '项目名称',
  `product_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联产品id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `director_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目负责人',
  `start_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目开始时间',
  `end_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目结束时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：状态：0未设置,1未开始,2进行中,3已完成,4已关闭',
  `step_sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前审核步骤',
  `content` text NULL COMMENT '项目描述',
  `md_content` text NULL COMMENT 'markdown项目描述',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '项目表';

-- ----------------------------
-- Records of dev_project
-- ----------------------------
INSERT INTO `dev_project` VALUES (1000, '勾股CMS', 1000, 1, 1, 1646064000, 1656518400, 1, 1,'勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。系统后台各管理模块，一目了然，操作简单；通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，极低门槛、开箱即用。易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。', NULL, 1652457839, 1652500363, 0);
INSERT INTO `dev_project` VALUES (1001, '勾股博客', 1001, 1, 1, 1646064000, 1656518400, 1,1, '系统后台集成了主流的通用功能，如：登录验证、系统配置、操作日志管理、角色权限、功能管理（后台菜单管理）、导航设置、网站地图、轮播广告、TAG关键字管理、友情链接、文件上传、数据备份/还原、博客文章功能、语雀文档功能、用户管理、用户操作日志、用户注册/登录、 博客归档、博客动态、访问统计等。更多的个性化功能可以基于当前博客系统便捷做二次开发。', NULL, 1652493248, 0, 0);
INSERT INTO `dev_project` VALUES (1002, '勾股OA', 1002, 1, 1, 1651334400, 1656518400, 1,1, '勾股OA是一款基于ThinkPHP6 + Layui + MySql打造的简单实用的开源免费的企业办公系统框架。可以帮助解决企业办公项目60%的重复工作，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。使用勾股OA可以简单快速地开发出企业级的Web应用系统。', NULL, 1652494341, 0, 0);
INSERT INTO `dev_project` VALUES (1003, '勾股DEV', 1003, 1, 1, 1651680000, 1659196800, 1,1, '勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。', NULL, 1652494403, 0, 0);

-- ----------------------------
-- Table structure for dev_project_user
-- ----------------------------
DROP TABLE IF EXISTS `dev_project_user`;
CREATE TABLE `dev_project_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目成员id',
  `project_id` int(11) UNSIGNED NOT NULL COMMENT '关联项目id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '移除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '项目成员表';

-- ----------------------------
-- Records of dev_project_user
-- ----------------------------
INSERT INTO `dev_project_user` VALUES (1, 1, 1000, 1, 1652457839, 0);
INSERT INTO `dev_project_user` VALUES (2, 1, 1001, 3, 1652493248, 0);
INSERT INTO `dev_project_user` VALUES (3, 1, 1002, 3, 1652494341, 0);
INSERT INTO `dev_project_user` VALUES (4, 1, 1003, 3, 1652494403, 0);

-- ----------------------------
-- Table structure for dev_task
-- ----------------------------
DROP TABLE IF EXISTS `dev_task`;
CREATE TABLE `dev_task`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父任务id',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '主题',
  `project_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联项目id',
  `test_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联测试id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `plan_hours` decimal(10, 1) NOT NULL DEFAULT 0.00 COMMENT '预估工时',
  `end_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '预计结束时间',
  `over_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '实际结束时间',
  `director_uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '指派给(负责人)',
  `assist_admin_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '协助人员，如:1,2,3',
  `cate` tinyint(1) NOT NULL DEFAULT 1 COMMENT '所属工作类型',
  `type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '任务类型',
  `before_task` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '前置任务id',
  `is_bug` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是问题缺陷,1是,0否',
  `priority` tinyint(1) NOT NULL DEFAULT 1 COMMENT '优先级:1低,2中,3高,4紧急',
  `flow_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '流转状态：1待办的,2进行中,3已完成,4已拒绝,5已关闭',
  `done_ratio` int(2) NOT NULL DEFAULT 0 COMMENT '完成进度：0,20,40,50,60,80,100',
  `content` text NULL COMMENT '任务描述',
  `md_content` text NULL COMMENT 'markdown任务描述',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '任务表';

-- ----------------------------
-- Table structure for dev_document
-- ----------------------------
DROP TABLE IF EXISTS `dev_document`;
CREATE TABLE `dev_document`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '模块',
  `topic_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联主题id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `file_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '附件ids',
  `content` text NULL COMMENT '文档内容',
  `md_content` text NULL COMMENT 'markdown文档内容',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '文档表';

-- ----------------------------
-- Table structure for dev_file_interfix
-- ----------------------------
DROP TABLE IF EXISTS `dev_file_interfix`;
CREATE TABLE `dev_file_interfix`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '模块',
  `topic_id` int(11) UNSIGNED NOT NULL COMMENT '关联主题id',
  `file_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '相关联附件id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '附件关联表';

-- ----------------------------
-- Table structure for dev_link_interfix
-- ----------------------------
DROP TABLE IF EXISTS `dev_link_interfix`;
CREATE TABLE `dev_link_interfix`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '模块',
  `topic_id` int(11) UNSIGNED NOT NULL COMMENT '关联主题id',
  `desc` varchar(255) NOT NULL DEFAULT '' COMMENT '说明',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '链接',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '链接关联表';

-- ----------------------------
-- Records of dev_link_interfix
-- ----------------------------
INSERT INTO `dev_link_interfix` VALUES (1, 'product', 1000, '勾股CMS体验站', 'https://www.gougucms.com/', 1, 1652456761, 0, 0);
INSERT INTO `dev_link_interfix` VALUES (2, 'product', 1001, '勾股博客体验站', 'https://blog.gougucms.com/', 1, 1652456799, 0, 0);
INSERT INTO `dev_link_interfix` VALUES (3, 'product', 1002, '勾股OA体验站', 'https://oa.gougucms.com/', 1, 1652456989, 0, 0);
INSERT INTO `dev_link_interfix` VALUES (4, 'product', 1002, '勾股OA开源地址', 'https://gitee.com/gouguopen/office', 1, 1652457015, 0, 0);
INSERT INTO `dev_link_interfix` VALUES (5, 'product', 1001, '勾股博客开源地址', 'https://gitee.com/gouguopen/blog', 1, 1652457056, 0, 0);
INSERT INTO `dev_link_interfix` VALUES (6, 'product', 1000, '勾股CMS开源地址', 'https://gitee.com/gouguopen/gougucms', 1, 1652457099, 0, 0);
INSERT INTO `dev_link_interfix` VALUES (7, 'product', 1003, '勾股DEV体验站', 'https://dev.gougucms.com', 1, 1652457200, 0, 0);
INSERT INTO `dev_link_interfix` VALUES (8, 'product', 1003, '勾股DEV开源地址', 'https://gitee.com/gouguopen/dev', 1, 1652457220, 0, 0);

-- ----------------------------
-- Table structure for dev_comment
-- ----------------------------
DROP TABLE IF EXISTS `dev_comment`;
CREATE TABLE `dev_comment`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '模块',
  `topic_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联主题id',
  `pid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '回复内容id',
  `padmin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '回复内容用户id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `content` text NULL COMMENT '评论内容',
  `md_content` text NULL COMMENT 'markdown评论内容',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COMMENT = '评论表';

-- ----------------------------
-- Table structure for dev_log
-- ----------------------------
DROP TABLE IF EXISTS `dev_log`;
CREATE TABLE `dev_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '模块',
  `action` varchar(100) NOT NULL DEFAULT 'edit' COMMENT '动作:edit.add,del,upload',
  `field` varchar(100) NOT NULL DEFAULT '' COMMENT '字段',
  `product_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联产品id',
  `project_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联项目id',
  `task_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联任务id',
  `document_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联文档id',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作人',
  `old_content` text NULL COMMENT '修改前的内容',
  `new_content` text NULL COMMENT '修改后的内容',
  `remark` text NULL COMMENT '补充备注',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '操作记录表';

-- ----------------------------
-- Records of dev_log
-- ----------------------------
INSERT INTO `dev_log` VALUES (1, 'project', 'add', 'new', 0, 1000, 0, 0, 1, '', '勾股CMS', NULL, 1652514531);
INSERT INTO `dev_log` VALUES (2, 'project', 'add', 'new', 0, 1001, 0, 0, 1, '', '勾股BLOG', NULL, 1652514531);
INSERT INTO `dev_log` VALUES (3, 'project', 'add', 'new', 0, 1002, 0, 0, 1, '', '勾股OA', NULL, 1652514531);
INSERT INTO `dev_log` VALUES (4, 'project', 'add', 'new', 0, 1003, 0, 0, 1, '', '勾股DEV', NULL, 1652514531);

-- ----------------------------
-- Table structure for dev_step
-- ----------------------------
DROP TABLE IF EXISTS `dev_step`;
CREATE TABLE `dev_step`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_id` int(11) NOT NULL COMMENT '关联ID',
  `flow_name` varchar(255) NOT NULL DEFAULT '' COMMENT '阶段名称',
  `flow_uid` int(11) NOT NULL DEFAULT 0 COMMENT '阶段负责人ID',
  `flow_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '阶段成员ID (使用逗号隔开) 1,2,3',
  `sort` tinyint(4) NOT NULL DEFAULT 0 COMMENT '排序ID',
  `type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '阶段类型:1合同,2项目',
  `start_time` int(11) NOT NULL DEFAULT 0 COMMENT '开始时间',
  `end_time` int(11) NOT NULL DEFAULT 0 COMMENT '结束时间',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `delete_time` int(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '阶段步骤表';

-- ----------------------------
-- Table structure for dev_step_record
-- ----------------------------
DROP TABLE IF EXISTS `dev_step_record`;
CREATE TABLE `dev_step_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联ID',
  `step_id` int(11) NOT NULL DEFAULT 0 COMMENT '阶段步骤ID',
  `check_uid` int(11) NOT NULL DEFAULT 0 COMMENT '审批人ID',
  `check_time` int(11) NOT NULL COMMENT '审批时间',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '1审核通过2审核拒绝3撤销',
  `type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '阶段类型:1项目',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT '审核意见',
  `delete_time` int(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '阶段步骤记录表';
