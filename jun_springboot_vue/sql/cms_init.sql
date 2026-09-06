/**
 * @copyright Copyright (c) 2025 勾股工作室
 * @license https://opensource.org/licenses/Apache-2.0
 * @link https://www.gougucms.com
 */


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cms_admin`
-- ----------------------------
DROP TABLE IF EXISTS `cms_admin`;
CREATE TABLE `cms_admin` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL DEFAULT '',
  `pwd` varchar(100) NOT NULL DEFAULT '',
  `salt` varchar(100) NOT NULL DEFAULT '',
  `nickname` varchar(255) DEFAULT '',
  `thumb` varchar(255) DEFAULT NULL,
  `theme` varchar(255) NOT NULL DEFAULT 'black' COMMENT '系统主题',
  `mobile` bigint(11) DEFAULT '0',
  `email` varchar(255) DEFAULT '',
  `desc` text COMMENT '备注',
  `did` int(11) NOT NULL DEFAULT 0 COMMENT '部门id',
  `position_id` int(11) NOT NULL DEFAULT 0 COMMENT '职位id',
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  `last_login_time` int(11) NOT NULL DEFAULT '0',
  `login_num` int(11) NOT NULL DEFAULT '0',
  `last_login_ip` varchar(64) NOT NULL DEFAULT '',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1正常,0禁止登录,-1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='管理员表';

-- ----------------------------
-- Table structure for cms_admin_rule
-- ----------------------------
DROP TABLE IF EXISTS `cms_admin_rule`;
CREATE TABLE `cms_admin_rule`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父id',
  `src` varchar(255) NOT NULL DEFAULT '' COMMENT 'url链接',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '日志操作名称',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `menu` int(1) NOT NULL DEFAULT 0 COMMENT '是否是菜单,1是,2不是',
  `sort` int(11) NOT NULL DEFAULT 1 COMMENT '越小越靠前',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态,0禁用,1正常',
  `module` varchar(255) NOT NULL DEFAULT '' COMMENT '所属模块',
  `crud` varchar(255) NOT NULL DEFAULT '' COMMENT 'crud标识',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '菜单及权限表';

-- ----------------------------
-- Records of cms_admin_rule
-- ----------------------------
INSERT INTO `cms_admin_rule` VALUES (1, 0, '', '系统管理', '系统管理', 'ri-settings-3-line', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (2, 0, '', '系统工具', '系统工具', 'ri-list-settings-line', 1, 2, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (3, 0, '', '基础数据', '基础数据', 'ri-database-2-line', 1, 3, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (4, 0, '', '平台用户', '平台用户', 'ri-group-line', 1, 4, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (5, 0, '', '资讯中心', '资讯中心', 'ri-article-line', 1, 5, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (6, 0, '', '图集中心', '图集中心', 'ri-image-2-line', 1, 6, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (7, 0, '', '商品中心', '商品中心', 'ri-shopping-bag-3-line', 1, 7, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (8, 0, '', '单 页 面', '单 页 面', 'ri-file-list-3-line', 1, 8, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (9, 1, 'admin/conf/index', '系统配置', '系统配置', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (10, 9, 'admin/conf/add', '新建/编辑', '配置项', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (11, 9, 'admin/conf/delete', '删除', '配置项', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (12, 9, 'admin/conf/edit', '编辑', '配置详情', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (13, 1, 'admin/rule/index', '功能节点', '功能节点', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (14, 13, 'admin/rule/add', '新建/编辑', '功能节点', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (15, 13, 'admin/rule/delete', '删除', '功能节点', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (16, 1, 'admin/role/index', '权限角色', '权限角色', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (17, 16, 'admin/role/add', '新建/编辑', '权限角色', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (18, 16, 'admin/role/delete', '删除', '权限角色', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (19, 1, 'admin/department/index', '部门架构', '部门', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (20, 19, 'admin/department/add', '新建/编辑', '部门', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (21, 19, 'admin/department/delete', '删除', '部门', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (22, 1, 'admin/position/index', '岗位职称', '岗位职称', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (23, 22, 'admin/position/add', '新建/编辑', '岗位职称', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (24, 22, 'admin/position/delete', '删除', '岗位职称',  '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (25, 1, 'admin/admin/index', '系统用户', '系统用户', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (26, 25, 'admin/admin/add', '添加/修改', '系统用户', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (27, 25, 'admin/admin/view', '查看', '系统用户', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (28, 25, 'admin/admin/delete', '删除', '系统用户', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (29, 2, 'admin/log/index', '操作日志', '操作日志', '', 1, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (30, 2, 'admin/crud/index', '一键CRUD', 'CRUD代码生成', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (31, 30, 'admin/crud/table', 'CRUD查看', 'CRUD查看', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (32, 30, 'admin/crud/crud', 'CRUD操作', 'CRUD代码生成', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (33, 30, 'admin/crud/menu', '生成菜单', '菜单生成', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (34, 2, 'admin/database/database', '备份数据', '数据备份', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (35, 34, 'admin/database/backup', '备份数据表', '数据', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (36, 34, 'admin/database/optimize', '优化数据表', '数据表', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (37, 34, 'admin/database/repair', '修复数据表', '数据表', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (38, 2, 'admin/database/backuplist', '还原数据', '数据还原', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (39, 38, 'admin/database/import', '还原数据表', '数据', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (40, 38, 'admin/database/downfile', '下载备份数据', '备份数据', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (41, 38, 'admin/database/del', '删除备份数据', '备份数据', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (42, 2, 'admin/files/index', '附件管理','附件管理', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (43, 42, 'admin/files/edit', '编辑附件','附件', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (44, 42, 'admin/files/move', '移动附件','附件', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (45, 42, 'admin/files/delete', '删除附件','附件', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (46, 42, 'admin/files/get_group', '附件分组','附件分组', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (47, 42, 'admin/files/add_group', '新建/编辑','附件分组', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (48, 42, 'admin/files/del_group', '删除附件分组','附件分组', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (49, 3, 'admin/nav/index', '导航设置','导航组', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (50, 53, 'admin/nav/add', '新建/编辑','导航组', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (51, 53, 'admin/nav/delete', '删除','导航组', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (52, 3, 'admin/nav/nav_info', '导航管理','导航', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (53, 52, 'admin/nav/nav_info_add', '新建/编辑','导航', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (54, 52, 'admin/nav/nav_info_delete', '删除导航','导航', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (55, 3, 'admin/sitemap/index', '网站地图','网站地图分类', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (56, 55, 'admin/sitemap/add', '新建/编辑','网站地图分类', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (57, 55, 'admin/sitemap/delete', '删除','网站地图分类', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (58, 3, 'admin/sitemap/sitemap_info', '网站地图管理','网站地图', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (59, 58, 'admin/sitemap/sitemap_info_add', '新建/编辑','网站地图', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (60, 58, 'admin/sitemap/sitemap_info_delete', '删除','网站地图', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (61, 3, 'admin/slide/index', '轮播广告','轮播组', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (62, 61, 'admin/slide/add', '新建/编辑','轮播组', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (63, 61, 'admin/slide/delete', '删除','轮播组', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (64, 3, 'admin/slide/slide_info', '轮播广告管理','轮播图', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (65, 64, 'admin/slide/slide_info_add', '新建/编辑','轮播图', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (66, 64, 'admin/slide/slide_info_delete', '删除','轮播图', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (67, 3, 'admin/links/index', '友情链接', '友情链接', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (68, 67, 'admin/links/add', '新建/编辑', '友情链接', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (69, 67, 'admin/links/delete', '删除', '友情链接', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (70, 3, 'admin/keywords/index', 'SEO关键字','SEO关键字', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (71, 70, 'admin/keywords/add', '新建/编辑','SEO关键字', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (72, 70, 'admin/keywords/delete', '删除','SEO关键字', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (73, 3, 'admin/search/index', '搜索关键字','搜索关键字', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (74, 73, 'admin/search/delete', '删除','搜索关键字', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (75, 4, 'admin/level/index', '用户等级', '用户等级', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (76, 75, 'admin/level/add', '新建/编辑', '用户等级', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (77, 75, 'admin/level/disable', '禁用/启用', '用户等级', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (78, 4, 'admin/user/index', '用户管理','用户', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (79, 78, 'admin/user/edit', '编辑','用户信息', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (80, 78, 'admin/user/view', '查看','用户信息', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (81, 78, 'admin/user/disable', '禁用/启用','用户', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (82, 4, 'admin/user/record', '操作记录','用户操作记录', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (83, 4, 'admin/user/log', '操作日志','用户操作日志', '', 1, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (84, 5, 'admin/article_cate/datalist', '文章分类','文章分类', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (85, 84, 'admin/article_cate/add', '新建','文章分类', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (86, 84, 'admin/article_cate/edit', '编辑','文章分类', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (87, 84, 'admin/article_cate/del', '删除','文章分类', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (88, 5, 'admin/article/datalist', '文章列表','文章', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (89, 88, 'admin/article/add', '新建','文章', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (90, 88, 'admin/article/edit', '编辑','文章', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (91, 88, 'admin/article/read', '查看','文章', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (92, 88, 'admin/article/del', '删除','文章', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (93, 6, 'admin/gallery_cate/datalist', '图集分类','图集分类', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (94, 93, 'admin/gallery_cate/add', '新建','图集分类', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (95, 93, 'admin/gallery_cate/edit', '编辑','图集分类', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (96, 93, 'admin/gallery_cate/del', '删除','图集分类', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (97, 6, 'admin/gallery/datalist', '图集列表','图集', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (98, 97, 'admin/gallery/add', '新建','图集', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (99, 97, 'admin/gallery/edit', '编辑','图集', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (100, 97, 'admin/gallery/read', '查看','图集', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (101, 97, 'admin/gallery/del', '删除','图集', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (102, 7, 'admin/goods_cate/datalist', '商品分类','商品分类', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (103, 102, 'admin/goods_cate/add', '新建','商品分类', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (104, 102, 'admin/goods_cate/edit', '编辑','商品分类', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (105, 102, 'admin/goods_cate/del', '删除','商品分类', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (106, 7, 'admin/goods/datalist', '商品列表','商品', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (107, 106, 'admin/goods/add', '新建','商品', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (108, 106, 'admin/goods/edit', '编辑','商品', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (109, 106, 'admin/goods/read', '查看','商品', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (110, 106, 'admin/goods/del', '删除','商品', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (111, 8, 'admin/pages/datalist', '单页面列表','单页面', '', 1, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (112, 111, 'admin/pages/add', '新建','单页面', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (113, 111, 'admin/pages/edit', '编辑','单页面', '', 2, 1, 1,'','', 0, 0);
INSERT INTO `cms_admin_rule` VALUES (114, 111, 'admin/pages/del', '删除','单页面', '', 2, 1, 1,'','', 0, 0);

INSERT INTO `cms_admin_rule` VALUES (115, 8, 'admin/analysis/index', '智能分析', '智能分析', '', 1, 1, 1,'','', 0, 0);

-- ----------------------------
-- Table structure for `cms_admin_group`
-- ----------------------------
DROP TABLE IF EXISTS `cms_admin_group`;
CREATE TABLE `cms_admin_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '',
  `status` int(1) NOT NULL DEFAULT '1',
  `rules` varchar(1000) DEFAULT '' COMMENT '用户组拥有的规则id， 多个规则","隔开',
  `desc` text COMMENT '备注',
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='权限分组表';

-- ----------------------------
-- Records of cms_admin_group
-- ----------------------------
INSERT INTO `cms_admin_group` VALUES ('1', '超级管理员', '1', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119', '超级管理员，系统自动分配所有可操作权限及菜单。', 0, 0);
INSERT INTO `cms_admin_group` VALUES (2, '测试角色', 1, '1,9,13,17,20,23,26,29,31,2,33,34,35,38,42,43,44,45,46,3,53,56,59,62,65,68,71,74,77,4,79,82,84,87,5,88,92,95,6,97,101,104,7,106,110,113,8,115,119', '测试角色', 0, 0);
-- ----------------------------
-- Table structure for `cms_admin_group_access`
-- ----------------------------
DROP TABLE IF EXISTS `cms_admin_group_access`;
CREATE TABLE `cms_admin_group_access` (
  `uid` int(11) unsigned DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  UNIQUE KEY `uid_group_id` (`uid`,`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='权限分组和管理员的关联表';

-- ----------------------------
-- Records of cms_admin_group_access
-- ----------------------------
INSERT INTO `cms_admin_group_access` VALUES ('1', '1', '0', '0');

-- ----------------------------
-- Table structure for `cms_admin_log`
-- ----------------------------
DROP TABLE IF EXISTS `cms_admin_log`;
CREATE TABLE `cms_admin_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `nickname` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '后台操作日志表';

-- ----------------------------
-- Table structure for `cms_config`
-- ----------------------------
DROP TABLE IF EXISTS `cms_config`;
CREATE TABLE `cms_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '配置名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '配置标识',
  `content` text NULL COMMENT '配置内容',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='系统配置表';

-- ----------------------------
-- Records of cms_config
-- ----------------------------
INSERT INTO `cms_config` VALUES (1, '网站配置', 'web', 'a:13:{s:2:\"id\";s:1:\"1\";s:11:\"admin_title\";s:9:\"勾股CMS\";s:5:\"title\";s:9:\"勾股cms\";s:4:\"logo\";s:39:\"/static/admin/images/nonepic360x360.jpg\";s:4:\"file\";s:0:\"\";s:6:\"domain\";s:24:\"https://www.gougucms.com\";s:3:\"icp\";s:23:\"粤ICP备1xxxxxx11号-1\";s:8:\"keywords\";s:9:\"勾股cms\";s:5:\"beian\";s:29:\"粤公网安备1xxxxxx11号-1\";s:4:\"desc\";s:258:\"勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能快速建站的内容管理系统。后台管理模块，一目了然，操作简单，通用型后台权限管理框架，紧随潮流、极低门槛、开箱即用。           \";s:4:\"code\";s:0:\"\";s:9:\"copyright\";s:39:\"© 2022 gougucms.com Apache-2.0 license\";s:7:\"version\";s:6:\"2.0.18\";}', 1, 1612514630, 1645057819);
INSERT INTO `cms_config` VALUES (2, '邮箱配置', 'email', 'a:8:{s:2:\"id\";s:1:\"2\";s:4:\"smtp\";s:11:\"smtp.qq.com\";s:9:\"smtp_port\";s:3:\"465\";s:9:\"smtp_user\";s:15:\"gougucms@qq.com\";s:8:\"smtp_pwd\";s:6:\"123456\";s:4:\"from\";s:24:\"勾股CMS系统管理员\";s:5:\"email\";s:18:\"admin@gougucms.com\";s:8:\"template\";s:122:\"<p>勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能快速建站的内容管理系统。</p>\";}', 1, 1612521657, 1619088538);
INSERT INTO `cms_config` VALUES (3, '微信配置', 'wechat', 'a:11:{s:2:\"id\";s:1:\"3\";s:5:\"token\";s:8:\"GOUGUCMS\";s:14:\"login_back_url\";s:49:\"https://www.gougucms.com/wechat/index/getChatInfo\";s:5:\"appid\";s:18:\"wxdf96xxxx7cd6f0c5\";s:9:\"appsecret\";s:32:\"1dbf319a4f0dfed7xxxxfd1c7dbba488\";s:5:\"mchid\";s:10:\"151xxxx331\";s:11:\"secrect_key\";s:29:\"gougucmsxxxxhumabcxxxxjixxxng\";s:8:\"cert_url\";s:13:\"/extend/cert/\";s:12:\"pay_back_url\";s:42:\"https://www.gougucms.com/wxappv1/wx/notify\";s:9:\"xcx_appid\";s:18:\"wxdf96xxxx9cd6f0c5\";s:13:\"xcx_appsecret\";s:28:\"gougucmsxxxxhunangdmabcxxxng\";}', 1, 1612522314, 1613789058);
INSERT INTO `cms_config` VALUES (4, 'Api Token配置', 'token', 'a:5:{s:2:\"id\";s:1:\"5\";s:3:\"iss\";s:16:\"www.gougucms.com\";s:3:\"aud\";s:8:\"gougucms\";s:7:\"secrect\";s:8:\"GOUGUCMS\";s:7:\"exptime\";s:4:\"3600\";}', 1, 1627313142, 1627376290);
INSERT INTO `cms_config` VALUES (5, '其他配置', 'other', 'a:5:{s:2:"id";s:1:"5";s:6:"author";s:15:"勾股工作室";s:7:"version";s:7:"v2.0.16";s:19:"upload_max_filesize";s:2:"10";s:6:"editor";s:1:"1";}', 1, 1613725791, 1645107069);

-- ----------------------------
-- Table structure for cms_department
-- ----------------------------
DROP TABLE IF EXISTS `cms_department`;
CREATE TABLE `cms_department`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '部门名称',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级部门id',
  `leader_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '部门负责人ID',
  `phone` varchar(60) NOT NULL DEFAULT '' COMMENT '部门联系电话',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '部门组织';

-- ----------------------------
-- Records of cms_department
-- ----------------------------
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (1, '勾股科技', 0, 0, '13688888888');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (2, '广州总公司', 1, 0, '13688888889');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (3, '人事部', 2, 0, '13688888898');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (4, '财务部', 2, 0, '13688888898');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (5, '市场部', 2, 0, '13688888978');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (6, '销售部', 2, 0, '13688889868');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (7, '技术部', 2, 0, '13688898858');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (8, '客服部', 2, 0, '13688988848');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (9, '销售一部', 6, 0, '13688998838');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (10, '销售二部', 6, 0, '13688999828');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (11, '深圳分公司', 1, 0, '13688999918');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (12, '人事部', 11, 0, '13688888886');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (13, '市场部', 11, 0, '13688888886');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (14, '财务部', 11, 0, '13688888876');
INSERT INTO `cms_department`(`id`, `title`, `pid`, `leader_id`, `phone`) VALUES (15, '销售部', 11, 0, '13688888666');

-- ----------------------------
-- Table structure for cms_position
-- ----------------------------
DROP TABLE IF EXISTS `cms_position`;
CREATE TABLE `cms_position`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '岗位名称',
  `work_price` int(10) NOT NULL DEFAULT 0 COMMENT '工时单价',
  `remark` varchar(1000) NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '岗位职称';

-- ----------------------------
-- Records of cms_position
-- ----------------------------
INSERT INTO `cms_position` VALUES (1, '董事长', 1000, '董事长', 1, 0, 0);
INSERT INTO `cms_position` VALUES (2, '人事总监', 1000, '人事部的最大领导', 1, 0, 0);
INSERT INTO `cms_position` VALUES (3, '普通员工', 500, '普通员工', 1, 0, 0);

-- ----------------------------
-- Table structure for `cms_keywords`
-- ----------------------------
DROP TABLE IF EXISTS `cms_keywords`;
CREATE TABLE `cms_keywords` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '关键字名称',
  `sort` int(11)  NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='关键字表';
-- ----------------------------
-- Records of cms_keywords
-- ----------------------------
INSERT INTO `cms_keywords` VALUES (1, '勾股CMS', 0, 1, 1610183567, 1610184824);
INSERT INTO `cms_keywords` VALUES (2, '勾股BLOG', 0, 1, 1610183567, 1610184824);
INSERT INTO `cms_keywords` VALUES (3, '勾股OA', 0, 1, 1610183567, 1610184824);
INSERT INTO `cms_keywords` VALUES (4, '勾股dev', 0, 1, 1610183567, 1610184824);

-- ----------------------------
-- Table structure for cms_sitemap_cate
-- ----------------------------
DROP TABLE IF EXISTS `cms_sitemap_cate`;
CREATE TABLE `cms_sitemap_cate`  (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '分类名称',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1可用-1禁用',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '网站地图分类表';

-- ----------------------------
-- Table structure for cms_sitemap
-- ----------------------------
DROP TABLE IF EXISTS `cms_sitemap`;
CREATE TABLE `cms_sitemap`  (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sitemap_cate_id` int(11) NOT NULL DEFAULT 0 COMMENT '分类id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `pc_img` varchar(255) NULL DEFAULT NULL COMMENT 'pc端图片',
  `pc_src` varchar(255) NULL DEFAULT NULL COMMENT 'pc端链接',
  `mobile_img` varchar(255) NULL DEFAULT NULL COMMENT '移动端图片',
  `mobile_src` varchar(255) NULL DEFAULT NULL COMMENT '移动端链接',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1可用-1禁用',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '网站地图内容表';


-- ----------------------------
-- Table structure for `cms_nav`
-- ----------------------------
DROP TABLE IF EXISTS `cms_nav`;
CREATE TABLE `cms_nav` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '标识',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1可用-1禁用',
  `desc` varchar(1000) DEFAULT NULL,
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='导航';

-- -----------------------------
-- Records of `cms_nav`
-- -----------------------------
INSERT INTO `cms_nav` VALUES ('1', '主导航', 'NAV_HOME', '1', '平台主导航', '0', '0');

-- ----------------------------
-- Table structure for `cms_nav_info`
-- ----------------------------
DROP TABLE IF EXISTS `cms_nav_info`;
CREATE TABLE `cms_nav_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT '0',
  `nav_id` int(11) unsigned NOT NULL DEFAULT '0',
  `title` varchar(255) DEFAULT '',
  `src` varchar(255) DEFAULT NULL,
  `param` varchar(255) DEFAULT NULL,
  `target` int(1) NOT NULL DEFAULT '0' COMMENT '是否新窗口打开,默认0,1新窗口打开',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1可用,-1禁用',
  `sort` int(11) NOT NULL DEFAULT '0',
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='导航详情表';

-- -----------------------------
-- Records of `cms_nav_info`
-- -----------------------------
INSERT INTO `cms_nav_info` VALUES (1, 0, 1, '首页', '/', 'index', 0, 1, 1, 0, 0);
INSERT INTO `cms_nav_info` VALUES (2, 0, 1, '开源系统', '/home/index/logs.html', 'logs', 0, 1, 2, 0, 0);
INSERT INTO `cms_nav_info` VALUES (3, 0, 1, '博客社区', 'https://blog.gougucms.com/', '', 1, 1, 3, 0, 0);
INSERT INTO `cms_nav_info` VALUES (4, 0, 1, 'API接口', '/api/index', '', 1, 1, 4, 0, 0);
INSERT INTO `cms_nav_info` VALUES (5, 0, 1, '系统文档', 'https://blog.gougucms.com/home/book/detail/bid/1.html', '', 1, 1, 5, 0, 0);
INSERT INTO `cms_nav_info` VALUES (6, 0, 1, '后台演示', '/admin/index/index.html', '', 1, 1, 6, 0, 0);
INSERT INTO `cms_nav_info` VALUES (7, 2, 1, '勾股BLOG', 'https://blog.gougucms.com/home/book/detail/bid/2.html', '', 1, 1, 7, 0, 0);
INSERT INTO `cms_nav_info` VALUES (8, 2, 1, '勾股OA办公系统', 'https://www.gougucms.com/home/pages/detail/s/gouguoa.html', '', 1, 1, 8, 0, 0);
INSERT INTO `cms_nav_info` VALUES (9, 2, 1, '勾股DEV项目管理系统', 'https://www.gougucms.com/home/pages/detail/s/gougudev.html', '', 1, 1, 9, 0, 0);
INSERT INTO `cms_nav_info` VALUES (10, 0, 1, '开发日志', '/home/index/logs.html', '', 0, 1, 10, 0, 0);


-- ----------------------------
-- Table structure for `cms_slide`
-- ----------------------------
DROP TABLE IF EXISTS `cms_slide`;
CREATE TABLE `cms_slide` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '标识',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1可用-1禁用',
  `desc` varchar(1000) DEFAULT NULL,
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='幻灯片表';

-- ----------------------------
-- Records of cms_slide
-- ----------------------------
INSERT INTO `cms_slide` VALUES ('1', '首页轮播', 'INDEX_SLIDE', '1', '首页轮播组。', '0', '0');

-- ----------------------------
-- Table structure for `cms_slide_info`
-- ----------------------------
DROP TABLE IF EXISTS `cms_slide_info`;
CREATE TABLE `cms_slide_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `slide_id` int(11) unsigned NOT NULL DEFAULT '0',
  `title` varchar(255) DEFAULT NULL,
  `desc` varchar(1000) DEFAULT NULL,
  `img` varchar(255) NOT NULL DEFAULT '',
  `src` varchar(255) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1可用-1禁用',
  `sort` int(11) NOT NULL DEFAULT '0',
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='幻灯片详情表';

-- ----------------------------
-- Table structure for cms_links
-- ----------------------------
DROP TABLE IF EXISTS `cms_links`;
CREATE TABLE `cms_links`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255)  NOT NULL DEFAULT '' COMMENT '网站标题',
  `logo` int(11) NOT NULL DEFAULT 0 COMMENT '网站logo',
  `src` varchar(255) NULL DEFAULT NULL COMMENT '链接',
  `target` int(1) NOT NULL DEFAULT 1 COMMENT '是否新窗口打开，1是,0否',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:1可用-1禁用',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '友情链接';

-- ----------------------------
-- Records of cms_links
-- ----------------------------
INSERT INTO `cms_links` VALUES (1, '勾股CMS', 0, 'https://www.gougucms.com', 1, 1, 1, 1624516962, 1624517078);
INSERT INTO `cms_links` VALUES (2, '勾股BLOG', 0, 'https://blog.gougucms.com', 0, 1, 2, 1624517262, 1624517178);
INSERT INTO `cms_links` VALUES (3, '勾股OA', 0, 'https://oa.gougucms.com', 0, 1, 3, 1624517262, 1624517178);
INSERT INTO `cms_links` VALUES (4, '勾股DEV', 0, 'https://dev.gougucms.com', 0, 1, 4, 1624517262, 1624517178);

-- ----------------------------
-- Table structure for cms_search_keywords
-- ----------------------------
DROP TABLE IF EXISTS `cms_search_keywords`;
CREATE TABLE `cms_search_keywords`  (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '关键字',
  `times` int(11) NOT NULL DEFAULT 1 COMMENT '搜索次数',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1,2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '搜索关键字表';

-- ----------------------------
-- Records of cms_search_keywords
-- ----------------------------
INSERT INTO `cms_search_keywords` VALUES (1, '勾股CMS', 1, 1);
INSERT INTO `cms_search_keywords` VALUES (2, '勾股OA', 1, 1);
INSERT INTO `cms_search_keywords` VALUES (3, '勾股DEV', 1, 1);
INSERT INTO `cms_search_keywords` VALUES (4, '勾股BLOG', 1, 1);
INSERT INTO `cms_search_keywords` VALUES (5, '勾股Admin', 1, 1);
INSERT INTO `cms_search_keywords` VALUES (6, '勾股UI', 1, 1);

-- ----------------------------
-- Table structure for cms_user_level
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_level`;
CREATE TABLE `cms_user_level`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '等级名称',
  `desc` varchar(1000) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:0禁用,1正常',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '会员等级表';

-- ----------------------------
-- Records of cms_admin_module
-- ----------------------------
INSERT INTO `cms_user_level` VALUES (1, '普通会员','', 1, 1639562910, 0);
INSERT INTO `cms_user_level` VALUES (2, '铜牌会员','', 1, 1639562910, 0);
INSERT INTO `cms_user_level` VALUES (3, '银牌会员','', 1, 1639562910, 0);
INSERT INTO `cms_user_level` VALUES (4, '黄金会员','', 1, 1639562910, 0);
INSERT INTO `cms_user_level` VALUES (5, '白金会员','', 1, 1639562910, 0);
INSERT INTO `cms_user_level` VALUES (6, '钻石会员','', 1, 1639562910, 0);

-- ----------------------------
-- Table structure for cms_user
-- ----------------------------
DROP TABLE IF EXISTS `cms_user`;
CREATE TABLE `cms_user`  (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `nickname` varchar(255) NOT NULL DEFAULT '' COMMENT '用户微信昵称',
  `nickname_a` varchar(255) NOT NULL DEFAULT '' COMMENT '用户微信昵称16进制',
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '账号',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(100) NOT NULL DEFAULT '' COMMENT '密码盐',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '手机（也可以作为登录账号)',
  `mobile_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '手机绑定状态： 0未绑定 1已绑定',
  `email` varchar(50) NOT NULL DEFAULT '' COMMENT '邮箱',
  `headimgurl` varchar(255) NOT NULL DEFAULT '' COMMENT '微信头像',
  `sex` tinyint(1) NOT NULL DEFAULT 0 COMMENT '性别 0:未知 1:女 2:男 ',    
  `desc` varchar(1000) NOT NULL DEFAULT '' COMMENT '个人简介',
  `birthday` int(11) NULL DEFAULT '0' COMMENT '生日',
  `country` varchar(20) NOT NULL DEFAULT '' COMMENT '国家',
  `province` varchar(20) NOT NULL DEFAULT '' COMMENT '省',
  `city` varchar(20) NOT NULL DEFAULT '' COMMENT '城市',  
  `company` varchar(100) NOT NULL DEFAULT '' COMMENT '公司',  
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '公司地址',
  `depament` varchar(20) NOT NULL DEFAULT '' COMMENT '部门',
  `position` varchar(20) NOT NULL DEFAULT '' COMMENT '职位',
  `puid` int(11) NOT NULL DEFAULT 0 COMMENT '推荐人ID,默认是0',
  `qrcode_invite` int(11) NOT NULL DEFAULT 0 COMMENT '邀请场景二维码id',  
  `level` tinyint(1) NOT NULL DEFAULT 1 COMMENT '等级  默认是普通会员',   
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态  -1删除 0禁用 1正常',   
  `last_login_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后登录时间',
  `last_login_ip` varchar(64) NOT NULL DEFAULT '' COMMENT '最后登录IP',
  `login_num` int(11) NOT NULL DEFAULT '0',
  `register_time` int(11) NOT NULL DEFAULT '0' COMMENT '注册时间',
  `register_ip` varchar(64) NOT NULL DEFAULT '' COMMENT '注册IP',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '信息更新时间',
  `wx_platform` int(11) NOT NULL DEFAULT 0 COMMENT '首次注册来自于哪个微信平台',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '用户表';

-- ----------------------------
-- Records of for `cms_user`
-- ----------------------------
INSERT INTO `cms_user` VALUES (1, '勾股CMS', '', 'hdm58', '7aba99e08564eb6a9a6038255aeb265c', '03K6PWjT2dAFBsa8oJYZ', '小明名', '13589858989', 0, 'hdm58@qq.com', '/static/admin/images/icon.png', 0, '勾股科技', 1627401600, '', '', '广州', '勾股科技', '珠江新城', '技术部', '技术总监', 0, 0, 1, 1, 1645009233, '163.142.175.169', 7, 1627457646, '163.142.247.150', 0, 0);


-- ----------------------------
-- Table structure for `cms_user_log`
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_log`;
CREATE TABLE `cms_user_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `nickname` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称',
  `type` varchar(80) NOT NULL DEFAULT '' COMMENT '操作类型',
  `title` varchar(80) NOT NULL DEFAULT '' COMMENT '操作标题',
  `content` text COMMENT '操作描述',
  `module` varchar(32) NOT NULL DEFAULT '' COMMENT '模块',
  `controller` varchar(32) NOT NULL DEFAULT '' COMMENT '控制器',
  `function` varchar(32) NOT NULL DEFAULT '' COMMENT '方法',
  `ip` varchar(64) NOT NULL DEFAULT '' COMMENT '登录ip',
  `param_id` int(11) unsigned NOT NULL COMMENT '操作ID',
  `param` text COMMENT '参数json格式',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0删除 1正常',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='用户操作日志表';

-- ----------------------------
-- Table structure for cms_file_group
-- ----------------------------
DROP TABLE IF EXISTS `cms_file_group`;
CREATE TABLE `cms_file_group`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '分组名',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` int(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '文件分组表';

-- ----------------------------
-- Table structure for cms_file
-- ----------------------------
DROP TABLE IF EXISTS `cms_file`;
CREATE TABLE `cms_file`  (
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
  `group_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '文件分组ID',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上传会员ID',
  `uploadip` varchar(15) NOT NULL DEFAULT '' COMMENT '上传IP',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未审核1已审核-1不通过',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `admin_id` int(11) NOT NULL COMMENT '审核者id',
  `delete_time` int(11) NOT NULL DEFAULT 0 COMMENT '删除时间',
  `audit_time` int(11) NOT NULL DEFAULT 0 COMMENT '审核时间',
  `action` varchar(50) NOT NULL DEFAULT '' COMMENT '来源模块功能',
  `use` varchar(255) NULL DEFAULT NULL COMMENT '用处',
  `download` int(11) NOT NULL DEFAULT 0 COMMENT '下载量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '文件表';

-- ----------------------------
-- Records of cms_file
-- ----------------------------
INSERT INTO `cms_file` VALUES (1, 'admin', '5125347886f07f48f7003825660117039eb8784f', '563e5e8f48e607ed54461796b0cb4844', 'f95982689eb222b84e999122a50b3780.jpg.jpg', 'f95982689eb222b84e999122a50b3780.jpg', 'https://blog.gougucms.com/storage/202202/f95982689eb222b84e999122a50b3780.jpg', 62609, 'jpg', 'image/jpeg', 0, 1, '127.0.0.1', 1, 1645057433, 1, 0, 1645057433, 'upload', 'thumb', 0);
INSERT INTO `cms_file` VALUES (2, 'admin', '5125347886f07f48f7003825660117039eb8784f', '563e5e8f48e607ed54461796b0cb4844', 'e729477de18e3be7e7eb4ec7fe2f821e.jpg', 'e729477de18e3be7e7eb4ec7fe2f821e.jpg', 'https://blog.gougucms.com/storage/202202/e729477de18e3be7e7eb4ec7fe2f821e.jpg', 62609, 'jpg', 'image/jpeg', 0, 1, '127.0.0.1', 1, 1645057433, 1, 0, 1645057433, 'upload', 'thumb', 0);
INSERT INTO `cms_file` VALUES (3, 'admin', '5125347886f07f48f7003825660117039eb8784f', '563e5e8f48e607ed54461796b0cb4844', '1193f7a1585b9f6e8a97ae17718018b3.jpg', 'images/1193f7a1585b9f6e8a97ae17718018b3.jpg', 'https://blog.gougucms.com/storage/202204/1193f7a1585b9f6e8a97ae17718018b3.jpg', 62609, 'jpg', 'image/jpeg', 0, 1, '127.0.0.1', 1, 1645057433, 1, 0, 1645057433, 'upload', 'thumb', 0);
INSERT INTO `cms_file` VALUES (4, 'admin', '5125347886f07f48f7003825660117039eb8784f', '563e5e8f48e607ed54461796b0cb4844', '0f22a5ba4797b2fa22049ea73e6f779c.jpg', 'images/0f22a5ba4797b2fa22049ea73e6f779c.jpg', 'https://blog.gougucms.com/storage/202202/0f22a5ba4797b2fa22049ea73e6f779c.jpg', 62609, 'jpg', 'image/jpeg', 0, 1, '127.0.0.1', 1, 1645057433, 1, 0, 1645057433, 'upload', 'thumb', 0);

-- ----------------------------
-- Table structure for `cms_article_cate`
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_cate`;
CREATE TABLE `cms_article_cate` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT '0' COMMENT '父类ID',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '分类名称',
  `keywords` varchar(255) DEFAULT '' COMMENT '关键字',
  `desc` varchar(1000) DEFAULT '' COMMENT '描述',
  `sort` int(5) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '添加时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '修改时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='文章分类::crud';
-- ----------------------------
-- Records of cms_article_cate
-- ----------------------------
INSERT INTO `cms_article_cate` VALUES (1, 0, '勾股开源系统', '勾股开源', '左手自研，右手开源', 0, 1610196442,0,0);

-- ----------------------------
-- Table structure for `cms_article`
-- ----------------------------
DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cate_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属分类',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `desc` varchar(1000) DEFAULT '' COMMENT '摘要',
  `thumb` int(11) NOT NULL DEFAULT 0 COMMENT '缩略图:附件id',
  `original` int(1) NOT NULL DEFAULT 0 COMMENT '是否原创:1是,0否',
  `origin` varchar(255) NOT NULL DEFAULT '' COMMENT '来源或作者',
  `origin_url` varchar(255) NOT NULL DEFAULT '' COMMENT '来源地址',
  `content` text NOT NULL COMMENT '内容',
  `md_content` text NOT NULL COMMENT 'markdown内容',
  `read` int(11) NOT NULL DEFAULT '0' COMMENT '阅读量',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '属性:1精华,2热门,3推荐',
  `is_home` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否首页显示:0否,1是',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '状态:1正常,0下架',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='文章::crud';

-- ----------------------------
-- Records of cms_article
-- ----------------------------
INSERT INTO `cms_article` VALUES (1,1, '勾股CMS——轻量级、高性能极速后台开发框架','勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。通用型的后台权限管理框架，极低门槛、操作简单、开箱即用。系统易于功能扩展，代码维护，方便二次开发，让开发者更专注于业务深度需求的开发，帮助开发者简单高效降低二次开发成本。', 1, 0, '', '', '<p>勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。通用型的后台权限管理框架，极低门槛、操作简单、开箱即用。系统易于功能扩展，代码维护，方便二次开发，让开发者更专注于业务深度需求的开发，帮助开发者简单高效降低二次开发成本。</p>\n<p><img src=\"https://blog.gougucms.com/storage/image/202109/1630457997502502.png\" /></p>', '勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。通用型的后台权限管理框架，极低门槛、操作简单、开箱即用。系统易于功能扩展，代码维护，方便二次开发，让开发者更专注于业务深度需求的开发，帮助开发者简单高效降低二次开发成本。', 0, 2, 1, 0, 1,1, 1625071256, 1650811576, 0);
INSERT INTO `cms_article` VALUES (2,1, '勾股BLOG——简约，易用开源的个人博客系统','勾股BLOG是一款实用的开源免费的个人博客系统。集成了系统管理、基础数据、博客文章、博客动态、语雀知识库、用户管理、访问统计等功能。具有简约，易用，内存占用低等特点，可以用来做个人博客，工作室官网，自...', 2, 0, '', '', '<p>勾股BLOG是一款实用的开源免费的个人博客系统。集成了系统管理、基础数据、博客文章、博客动态、语雀知识库、用户管理、访问统计等功能。具有简约，易用，内存占用低等特点，可以用来做个人博客，工作室官网，自媒体官网等网站，二次开发之后也可以作为资讯、展品展示等网站。</p>\n<p><img src=\"https://blog.gougucms.com/storage/image/b1.png\" /></p>', '勾股BLOG是一款实用的开源免费的个人博客系统。集成了系统管理、基础数据、博客文章、博客动态、语雀知识库、用户管理、访问统计等功能。具有简约，易用，内存占用低等特点，可以用来做个人博客，工作室官网，自媒体官网等网站，二次开发之后也可以作为资讯、展品展示等网站。', 0, 0, 1, 0, 1,1, 1650811439, 1650811624, 0);
INSERT INTO `cms_article` VALUES (3,1, '勾股OA——实用的开源的企业办公系统框架', '勾股OA是一款实用的开源免费的企业办公系统框架。它集成了系统设置、人事管理、消息管理、审批管理、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，...', 3, 0, '', '', '<p>勾股OA是一款实用的开源免费的企业办公系统框架。它集成了系统设置、人事管理、消息管理、审批管理、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。</p>\n<p><img src=\"https://oa.gougucms.com/storage/image/oa1.png\" /></p>', '勾股OA是一款实用的开源免费的企业办公系统框架。它集成了系统设置、人事管理、消息管理、审批管理、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。', 0, 0, 1, 0, 1,1, 1650811502, 1650811709, 0);
INSERT INTO `cms_article` VALUES (4,1, '勾股DEV——研发管理与团队协作的工具软件', '勾股DEV是一款专为IT行业研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。', 4, 0, '', '', '<p>勾股DEV是一款专为IT行业研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。</p>\n<p><img src=\"https://dev.gougucms.com/storage/202204/dev2.png\" /></p>', '勾股DEV是一款专为IT行业研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。', 0, 0, 1, 0, 1,1, 1650811542, 1650811682, 0);

-- ----------------------------
-- Table structure for `cms_article_keywords`
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_keywords`;
CREATE TABLE `cms_article_keywords` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `aid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '文章ID',
  `keywords_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '关联关键字id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `aid` (`aid`),
  KEY `inid` (`keywords_id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='文章关联表';
-- ----------------------------
-- Records of cms_article_keywords
-- ----------------------------
INSERT INTO `cms_article_keywords` VALUES (1, 1, 1, 1, 1610198553);
INSERT INTO `cms_article_keywords` VALUES (2, 2, 2, 1, 1610198553);
INSERT INTO `cms_article_keywords` VALUES (3, 3, 3, 1, 1610198553);
INSERT INTO `cms_article_keywords` VALUES (4, 4, 4, 1, 1610198553);

-- ----------------------------
-- Table structure for cms_gallery_cate
-- ----------------------------
DROP TABLE IF EXISTS `cms_gallery_cate`;
CREATE TABLE `cms_gallery_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '图集分类名称',
  `sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级id',
  `keywords` varchar(255) DEFAULT '' COMMENT '关键字',
  `desc` varchar(1000) DEFAULT '' COMMENT '描述',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '图集分类::crud';

-- ----------------------------
-- Records of cms_gallery_cate
-- ----------------------------
INSERT INTO `cms_gallery_cate` VALUES (1, '勾股图集', 1, 0, '勾股CMS', '左手自研，右手开源', 1645058420, 1645058420, 0);

-- ----------------------------
-- Table structure for cms_gallery
-- ----------------------------
DROP TABLE IF EXISTS `cms_gallery`;
CREATE TABLE `cms_gallery`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `cate_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类ID',
  `type` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '属性:1精华,2热门,3推荐',
  `is_home` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否首页显示:0否,1是',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态:0下架 1正常',
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT '图集名称',
  `thumb` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缩略图',
  `desc` varchar(1000) NOT NULL DEFAULT '' COMMENT '图集摘要',
  `content` text NULL COMMENT '内容',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `origin` varchar(255) NOT NULL DEFAULT '' COMMENT '来源或作者',
  `origin_url` varchar(255) NOT NULL DEFAULT '' COMMENT '来源地址',
  `read` int(11) NOT NULL DEFAULT '0' COMMENT '阅读量',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `admin_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '图集::crud';

-- ----------------------------
-- Records of cms_gallery
-- ----------------------------
INSERT INTO `cms_gallery` VALUES (1, 1, 3, 1, 1, '勾股开源系列系统', 1, '勾股科技专注于CMS系统，OA办公系统，CRM系统，ERP系统，在线教育系统，项目管理系统等系统软件的建设。左手研发，右手开源，未来可期！', NULL, 0, '勾股科技', 'http://www.gougucms.com', 0, 0, 1, 1655452531, 1655633932, 0);

-- ----------------------------
-- Table structure for `cms_gallery_keywords`
-- ----------------------------
DROP TABLE IF EXISTS `cms_gallery_keywords`;
CREATE TABLE `cms_gallery_keywords` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `aid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '图集ID',
  `keywords_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '关联关键字id',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `aid` (`aid`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='图集关联表';

-- ----------------------------
-- Records of cms_gallery_keywords
-- ----------------------------
INSERT INTO `cms_gallery_keywords` VALUES (1, 1, 1, 1644823517);

-- ----------------------------
-- Table structure for `cms_gallery_file`
-- ----------------------------
DROP TABLE IF EXISTS `cms_gallery_file`;
CREATE TABLE `cms_gallery_file` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `aid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '图集ID',
  `file_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '文件id',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '图片名称',
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT '标题',
  `desc` varchar(1000) NOT NULL DEFAULT '' COMMENT '摘要',
  `filepath` varchar(200) NOT NULL DEFAULT '' COMMENT '图片路径',
  `link` varchar(200) NOT NULL DEFAULT '' COMMENT '链接地址',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `aid` (`aid`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='图集关联表';

-- ----------------------------
-- Records of cms_gallery_file
-- ----------------------------
INSERT INTO `cms_gallery_file` VALUES (1, 1, 0, '勾股cms', '勾股CMS', '勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级的通用后台管理框架。简单、易用且方便二次开发，是一款免费可商用的开源项目', 'https://blog.gougucms.com/storage/202202/f95982689eb222b84e999122a50b3780.jpg', 'http://www.gougucms.com', 1, 1655633932);
INSERT INTO `cms_gallery_file` VALUES (2, 1, 0, '勾股OA', '勾股OA', '勾股OA是一款实用的企业办公系统框架。集成了系统设置、人事管理、消息管理、审批管理、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。', 'https://blog.gougucms.com/storage/202202/e729477de18e3be7e7eb4ec7fe2f821e.jpg', 'http://oa.gougucms.com', 2, 1655633932);
INSERT INTO `cms_gallery_file` VALUES (3, 1, 0, '勾股DEV', '勾股DEV', '勾股DEV是一款任务管理与团队协作的任务/项目管理系统，可以进行工作任务计划、执行、管理，完整地覆盖了项目管理的核心流程。项目基于GPL-3.0协议开源发布。', 'https://blog.gougucms.com/storage/202204/1193f7a1585b9f6e8a97ae17718018b3.jpg', 'http://dev.gougucms.com', 3, 1655633932);

-- ----------------------------
-- Table structure for cms_goods_cate
-- ----------------------------
DROP TABLE IF EXISTS `cms_goods_cate`;
CREATE TABLE `cms_goods_cate`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '分类名称',
  `sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `pid` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级id',
  `keywords` varchar(255) DEFAULT '' COMMENT '关键字',
  `desc` varchar(1000) DEFAULT '' COMMENT '描述',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '商品分类::crud';

-- ----------------------------
-- Records of cms_goods_cate
-- ----------------------------
INSERT INTO `cms_goods_cate` VALUES (1, '勾股科技', 1, 0, '勾股CMS', '左手自研，右手开源', 1645058420, 0, 0);

-- ----------------------------
-- Table structure for cms_goods
-- ----------------------------
DROP TABLE IF EXISTS `cms_goods`;
CREATE TABLE `cms_goods`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `cate_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类ID',
  `type` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '属性:1精华,2热门,3推荐',
  `is_home` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否首页显示:0否,1是',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '商品状态:0下架,1正常',
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT '商品名称',
  `thumb` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缩略图',
  `banner` varchar(1000) NOT NULL DEFAULT '' COMMENT '商品轮播图',
  `tips` varchar(255) NOT NULL DEFAULT '' COMMENT '商品卖点，一句话推销',
  `desc` varchar(1000) NOT NULL DEFAULT '' COMMENT '商品摘要',
  `content` text NOT NULL COMMENT '内容',
  `base_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '市场价格',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实际价格',
  `stocks` int(11) NOT NULL DEFAULT 0 COMMENT '商品库存',
  `sales` int(11) NOT NULL DEFAULT 0 COMMENT '商品销量',
  `address` varchar(200) NOT NULL DEFAULT '' COMMENT '商品发货地址',
  `start_time` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '开始抢购时间',
  `end_time` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '结束抢购时间',
  `read` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读量',
  `sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `is_mail` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否包邮:0否,1是',
  `tag_values` varchar(200) NOT NULL DEFAULT '' COMMENT '商品标签:1正品保证,2一年保修,3七天退换,4赠运费险,5闪电发货,6售后无忧',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '编辑时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '商品::crud';

-- ----------------------------
-- Records of cms_goods
-- ----------------------------
INSERT INTO `cms_goods` VALUES (1, 1, 1, 1, 1, '勾股CMS，勾股Blog，勾股OA定制开发方案', 1, '', '赠送一年免费维护服务', '','勾股CMS定制开发方案99元起，勾股Blog定制开发方案199元起，勾股OA定制开发方案299元起。欢迎联系QQ:327725426咨询定制 。</p>', 199.00, 99.00, 0, 0, '', 0, 0, 0, 1, 1, '1,2,6', 0, 1644823517, 0,0);

-- ----------------------------
-- Table structure for `cms_goods_keywords`
-- ----------------------------
DROP TABLE IF EXISTS `cms_goods_keywords`;
CREATE TABLE `cms_goods_keywords` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `aid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商品ID',
  `keywords_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '关联关键字id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态:-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `aid` (`aid`),
  KEY `inid` (`keywords_id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='商品关联表';

-- ----------------------------
-- Records of cms_goods_keywords
-- ----------------------------
INSERT INTO `cms_goods_keywords` VALUES (1, 1, 1, 1, 1644823517);

-- ----------------------------
-- Table structure for cms_pages
-- ----------------------------
DROP TABLE IF EXISTS `cms_pages`;
CREATE TABLE `cms_pages`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT '页面名称',
  `thumb` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缩略图',
  `banner` varchar(1000) NOT NULL DEFAULT '' COMMENT '图集相册',
  `desc` varchar(1000) NOT NULL DEFAULT '' COMMENT '页面摘要',
  `content` text NOT NULL COMMENT '内容',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '页面状态:0下架,1正常',
  `read` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读量',
  `sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT 'url文件名',
  `template` varchar(200) NOT NULL DEFAULT '' COMMENT '前端模板',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '编辑时间',
  `delete_time` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '单页面::crud';

-- ----------------------------
-- Records of cms_pages
-- ----------------------------
INSERT INTO `cms_pages` VALUES (1, '关于我们', 1, '', '勾股CMS是一套基于ThinkPHP6+Layui+MySql打造的轻量级、高性能快速建站的内容管理系统。后台管理模块，一目了然，操作简单，通用型后台权限管理框架，开箱即用。', '<p>勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。通用型的后台权限管理框架，操作简单、开箱即用。系统易于功能扩展，代码维护，方便二次开发，让开发者更专注于业务深度需求的开发，帮助开发者简单高效降低二次开发成本。</p>\n<p><img src=\"https://blog.gougucms.com/storage/image/202109/1630457997502502.png\" /></p>', 1,1, 0, '', 'default', 1, 1653984295, 1653993466,0);
INSERT INTO `cms_pages` VALUES (2, '勾股OA办公系统', 0, '', '勾股OA是一款实用的企业办公系统框架。集成了系统设置、人事管理、消息管理、审批管理、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。', '<p>勾股OA是一款实用的企业办公系统框架。集成了系统设置、人事管理、消息管理、审批管理、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。</p>', 1,9, 0, 'gouguoa', 'oa', 1, 1654419120, 0,0);
INSERT INTO `cms_pages` VALUES (3,  '勾股DEV项目管理系统', 0, '', '勾股DEV是一款专为IT研发团队打造的项目管理与团队协作的系统工具，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。', '<p >勾股DEV是一款专为IT研发团队打造的项目管理与团队协作的系统工具，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。</p>\n<p>勾股DEV的产品理念：通过&ldquo;项目（Project）&rdquo;的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。</p>', 1,8, 0, 'gougudev', 'dev', 1, 1654167139, 0,0);

-- ----------------------------
-- Table structure for `cms_pages_keywords`
-- ----------------------------
DROP TABLE IF EXISTS `cms_pages_keywords`;
CREATE TABLE `cms_pages_keywords` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `aid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '页面ID',
  `keywords_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '关联关键字id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `aid` (`aid`),
  KEY `inid` (`keywords_id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='单页面关联表';

-- ----------------------------
-- Records of cms_pages_keywords
-- ----------------------------
INSERT INTO `cms_pages_keywords` VALUES (1, 1, 1, 1, 1644823517);
INSERT INTO `cms_pages_keywords` VALUES (2, 2, 2, 1, 1644823517);
INSERT INTO `cms_pages_keywords` VALUES (3, 3, 3, 1, 1644823517);