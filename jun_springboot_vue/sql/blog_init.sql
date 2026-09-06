/**
 * @copyright Copyright (c) 2021 勾股工作室
 * @license https://opensource.org/licenses/Apache-2.0
 * @link https://blog.gougucms.com
 */

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `blog_admin`
-- ----------------------------
DROP TABLE IF EXISTS `blog_admin`;
CREATE TABLE `blog_admin` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL DEFAULT '',
  `pwd` varchar(100) NOT NULL DEFAULT '',
  `salt` varchar(100) NOT NULL DEFAULT '',
  `nickname` varchar(255) DEFAULT '',
  `thumb` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` bigint(11) DEFAULT '0',
  `desc` text COMMENT '备注',
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
-- Table structure for `blog_admin_group`
-- ----------------------------
DROP TABLE IF EXISTS `blog_admin_group`;
CREATE TABLE `blog_admin_group` (
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
-- Records of blog_admin_group
-- ----------------------------
INSERT INTO `blog_admin_group` VALUES ('1', '超级管理员', '1', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88', '超级管理员，系统自动分配所有可操作权限及菜单。', '0', '0');
INSERT INTO `blog_admin_group` VALUES (2, '测试角色', 1, '1,7,11,14,17,20,22,24,25,29,2,33,36,39,42,45,48,51,54,57,3,59,62,64,66,67,4,68,71,5,74,76,6,78,81,82', '测试角色', 0, 0);

-- ----------------------------
-- Table structure for `blog_admin_group_access`
-- ----------------------------
DROP TABLE IF EXISTS `blog_admin_group_access`;
CREATE TABLE `blog_admin_group_access` (
  `uid` int(11) unsigned DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  UNIQUE KEY `uid_group_id` (`uid`,`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='权限分组和管理员的关联表';

-- ----------------------------
-- Records of blog_admin_group_access
-- ----------------------------
INSERT INTO `blog_admin_group_access` VALUES ('1', '1', '0', '0');

-- ----------------------------
-- Table structure for blog_admin_module
-- ----------------------------
DROP TABLE IF EXISTS `blog_admin_module`;
CREATE TABLE `blog_admin_module`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '模块名称',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '模块所在目录，小写字母',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态,0禁用,1正常',
  `type` int(1) NOT NULL DEFAULT 2 COMMENT '模块类型,2普通模块,1系统模块',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '功能模块表';

-- ----------------------------
-- Records of blog_admin_module
-- ----------------------------
INSERT INTO `blog_admin_module` VALUES (1, '后台模块', 'admin', '', 1, 1, 1639562910, 0);
INSERT INTO `blog_admin_module` VALUES (2, '前台模块', 'home', '', 1, 1, 1639562910, 0);

-- ----------------------------
-- Table structure for blog_admin_rule
-- ----------------------------
DROP TABLE IF EXISTS `blog_admin_rule`;
CREATE TABLE `blog_admin_rule`  (
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
-- Records of blog_admin_rule
-- ----------------------------
INSERT INTO `blog_admin_rule` VALUES (1, 0, '', '系统管理', '系统管理', 'bi-gear', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (2, 0, '', '基础数据', '基础数据', 'bi-box', 1, 2, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (3, 0, '', '平台用户', '平台用户', 'bi-people', 1, 3, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (4, 0, '', '博客文章', '博客文章', 'bi-journal-text', 1, 4, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (5, 0, '', '博客动态', '博客动态', 'bi-slash-square', 1, 5, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (6, 0, '', '知 识 库', '知识库', 'bi-pencil-square', 1, 6, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (7, 0, '', '单 页 面', '知识库', 'bi-stickies', 1, 7, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (8, 1, 'admin/conf/index', '系统配置', '系统配置', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (9, 8, 'admin/conf/add', '新建/编辑', '配置项', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (10, 8, 'admin/conf/delete', '删除', '配置项', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (11, 8, 'admin/conf/edit', '编辑', '配置详情', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (12, 1, 'admin/module/index', '功能模块', '功能模块', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (13, 12, 'admin/module/add', '新建/编辑', '功能模块', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (14, 12, 'admin/module/disable', '禁用/启用', '功能模块', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (15, 1, 'admin/rule/index', '功能节点', '功能节点', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (16, 15, 'admin/rule/add', '新建/编辑', '功能节点', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (17, 15, 'admin/rule/delete', '删除', '功能节点', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (18, 1, 'admin/role/index', '权限角色', '权限角色', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (19, 18, 'admin/role/add', '新建/编辑', '权限角色', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (20, 18, 'admin/role/delete', '删除', '权限角色', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (21, 1, 'admin/admin/index', '管 理 员', '管理员', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (22, 21, 'admin/admin/add', '添加/修改', '管理员', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (23, 21, 'admin/admin/view', '查看', '管理员', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (24, 21, 'admin/admin/delete', '删除', '管理员', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (25, 1, 'admin/log/index', '操作日志', '操作日志', '', 1, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (26, 1, 'admin/database/database', '备份数据', '数据备份', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (27, 26, 'admin/database/backup', '备份数据表', '数据', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (28, 26, 'admin/database/optimize', '优化数据表', '数据表', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (29, 26, 'admin/database/repair', '修复数据表', '数据表', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (30, 1, 'admin/database/backuplist', '还原数据', '数据还原', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (31, 30, 'admin/database/import', '还原数据表', '数据', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (32, 30, 'admin/database/downfile', '下载备份数据', '备份数据', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (33, 30, 'admin/database/del', '删除备份数据', '备份数据', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (34, 2, 'admin/nav/index', '导航设置','导航组', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (35, 34, 'admin/nav/add', '新建/编辑','导航组', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (36, 34, 'admin/nav/delete', '删除','导航组', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (37, 2, 'admin/nav/nav_info', '导航管理','导航', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (38, 37, 'admin/nav/nav_info_add', '新建/编辑','导航', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (39, 37, 'admin/nav/nav_info_delete', '删除导航','导航', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (40, 2, 'admin/sitemap/index', '网站地图','网站地图分类', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (41, 40, 'admin/sitemap/add', '新建/编辑','网站地图分类', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (42, 40, 'admin/sitemap/delete', '删除','网站地图分类', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (43, 2, 'admin/sitemap/sitemap_info', '网站地图管理','网站地图', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (44, 43, 'admin/sitemap/sitemap_info_add', '新建/编辑','网站地图', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (45, 43, 'admin/sitemap/sitemap_info_delete', '删除','网站地图', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (46, 2, 'admin/slide/index', '轮播广告','轮播组', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (47, 46, 'admin/slide/add', '新建/编辑','轮播组', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (48, 46, 'admin/slide/delete', '删除','轮播组', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (49, 2, 'admin/slide/slide_info', '轮播广告管理','轮播图', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (50, 49, 'admin/slide/slide_info_add', '新建/编辑','轮播图', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (51, 49, 'admin/slide/slide_info_delete', '删除','轮播图', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (52, 2, 'admin/links/index', '友情链接', '友情链接', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (53, 52, 'admin/links/add', '新建/编辑', '友情链接', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (54, 52, 'admin/links/delete', '删除', '友情链接', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (55, 2, 'admin/keywords/index', 'SEO关键字','SEO关键字', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (56, 55, 'admin/keywords/add', '新建/编辑','SEO关键字', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (57, 55, 'admin/keywords/delete', '删除','SEO关键字', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (58, 2, 'admin/search/index', '搜索关键字','搜索关键字', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (59, 58, 'admin/search/delete', '删除','搜索关键字', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (60, 3, 'admin/level/index', '用户等级', '用户等级', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (61, 60, 'admin/level/add', '新建/编辑', '用户等级', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (62, 60, 'admin/level/disable', '禁用/启用', '用户等级', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (63, 3, 'admin/user/index', '用户管理','用户', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (64, 63, 'admin/user/edit', '编辑','用户信息', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (65, 63, 'admin/user/view', '查看','用户信息', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (66, 63, 'admin/user/disable', '禁用','用户', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (67, 3, 'admin/user/record', '操作记录','用户操作记录', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (68, 3, 'admin/user/log', '操作日志','用户操作日志', '', 1, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (69, 4, 'admin/article/cate', '文章分类','文章分类', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (70, 69, 'admin/article/cate_add', '新建/编辑','文章分类', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (71, 69, 'admin/article/cate_delete', '删除','文章分类', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (72, 4, 'admin/article/index', '文章列表','文章', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (73, 72, 'admin/article/add', '新建/编辑','文章', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (74, 72, 'admin/article/delete', '删除','文章', '', 2, 1, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (75, 5, 'admin/action/index', '动态列表','动态', '', 1, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (76, 75, 'admin/action/add', '新建/编辑','动态', '', 2, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (77, 75, 'admin/action/view', '查看','动态', '', 2, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (78, 75, 'admin/action/delete', '删除','动态', '', 2, 0, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (79, 6, 'admin/book/index', '知识库列表', '知识库', '', 1, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (80, 79, 'admin/book/add', '新建/编辑', '知识库', '', 2, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (81, 79, 'admin/book/delete', '删除', '知识库', '', 2, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (82, 79, 'admin/book/view', '详情', '知识库', '', 2, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (83, 6, 'admin/book/doc_tree', '知识库文档列表', '知识库文档', '', 2, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (84, 83, 'admin/book/doc_add', '新建/编辑', '知识库文档', '', 2, 0, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (85, 83, 'admin/book/doc_delete', '删除', '知识库文档', '', 2, 0, 1, 0, 0);

INSERT INTO `blog_admin_rule` VALUES (86, 7, 'admin/pages/index', '单页面列表','单页面', '', 1, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (87, 86, 'admin/pages/add', '新建/编辑','单页面', '', 2, 1, 1, 0, 0);
INSERT INTO `blog_admin_rule` VALUES (88, 86, 'admin/pages/delete', '删除','单页面', '', 2, 1, 1, 0, 0);

-- ----------------------------
-- Table structure for `blog_admin_log`
-- ----------------------------
DROP TABLE IF EXISTS `blog_admin_log`;
CREATE TABLE `blog_admin_log`  (
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
-- Table structure for `blog_config`
-- ----------------------------
DROP TABLE IF EXISTS `blog_config`;
CREATE TABLE `blog_config` (
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
-- Records of blog_config
-- ----------------------------'
INSERT INTO `blog_config` VALUES (1, '网站配置', 'web', 'a:13:{s:2:\"id\";s:1:\"1\";s:11:\"admin_title\";s:12:\"勾股博客\";s:5:\"title\";s:60:\"勾股博客 - 基于ThinkPHP6的开源免费的博客系统\";s:4:\"logo\";s:39:\"/static/admin/images/nonepic360x360.jpg\";s:4:\"file\";s:0:\"\";s:6:\"domain\";s:25:\"https://blog.gougucms.com\";s:3:\"icp\";s:24:\"粤ICP备20210xxxx1号-1\";s:8:\"keywords\";s:104:\"勾股博客,勾股BLOG,ThinkPHP开源博客,开源博客系统,PHP开源博客系统,免费博客系统\";s:5:\"beian\";s:33:\"粤公网安备 4406050xxxx578号\";s:4:\"desc\";s:329:\"勾股博客是一款基于ThinkPHP6 + Layui + MySql打造的，简单实用的开源免费的博客系统，具有简约，易用，访问统计，内存占用低等特点，可以用来做个人博客，工作室官网，自媒体官网等站点，二次开发之后也可以作为资讯、展品展示等门户网站。       \";s:4:\"code\";s:0:\"\";s:9:\"copyright\";s:39:\"© 2022 gougucms.com Apache-2.0 license\";s:7:\"version\";s:6:\"2.0.16\";}', 1, 1612514630, 1635265805);

INSERT INTO `blog_config` VALUES (2, '微信配置', 'wechat', 'a:11:{s:2:\"id\";s:1:\"2\";s:5:\"token\";s:9:\"GOUGUBOKE\";s:14:\"login_back_url\";s:49:\"http://blog.gougucms.com/wechat/index/getChatInfo\";s:5:\"appid\";s:18:\"wxdf96xxxx7cd6f0c5\";s:9:\"appsecret\";s:32:\"1dbf319a4f0dfed7xxxxfd1c7dbba488\";s:5:\"mchid\";s:10:\"151xxxx331\";s:11:\"secrect_key\";s:32:\"beiyuexxxxhunangdmabcxxxxjixxxng\";s:8:\"cert_url\";s:13:\"/extend/cert/\";s:12:\"pay_back_url\";s:44:\"http://blog.gougucms.com/wechat/index/notify\";s:9:\"xcx_appid\";s:8:\"fsdfssfs\";s:13:\"xcx_appsecret\";s:14:\"hf74675fsfdsfs\";}', 1, 1612522314, 1626279037);
INSERT INTO `blog_config` VALUES (3, '博客配置', 'blog', 'a:7:{s:2:\"id\";s:1:\"3\";s:5:\"title\";s:12:\"勾股博客\";s:11:\"create_time\";s:10:\"2021-07-15\";s:6:\"avatar\";s:77:\"https://blog.gougucms.com/storage/202107/e79cfee139d988ee7560473335142f4d.jpg\";s:4:\"file\";s:0:\"\";s:9:\"signature\";s:69:\"随缘寻缘，始于足下；只有心，能决定自己的去处。\";s:4:\"desc\";s:69:\"随缘寻缘，始于足下；只有心，能决定自己的去处。\";}', 1, 1613725791, 1626278883);
INSERT INTO `blog_config` VALUES (4, 'Api Token配置', 'token', 'a:5:{s:2:\"id\";s:1:\"5\";s:3:\"iss\";s:16:\"www.gougucms.com\";s:3:\"aud\";s:8:\"gougucms\";s:7:\"secrect\";s:8:\"GOUGUCMS\";s:7:\"exptime\";s:4:\"3600\";}', 1, 1627313142, 1627376290);
INSERT INTO `blog_config` VALUES (5, '其他配置', 'other', 'a:4:{s:2:\"id\";s:1:\"5\";s:6:\"author\";s:15:\"勾股工作室\";s:7:\"version\";s:7:\"v2.0.16\";s:6:\"editor\";s:1:\"2\";}', 1, 1613725791, 1645107069);


-- ----------------------------
-- Table structure for `blog_keywords`
-- ----------------------------
DROP TABLE IF EXISTS `blog_keywords`;
CREATE TABLE `blog_keywords` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '关键字名称',
  `sort` int(11)  NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 0禁用 1启用',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='关键字';
-- ----------------------------
-- Records of blog_keywords
-- ----------------------------
INSERT INTO `blog_keywords` VALUES (1, '勾股CMS', 0, 1, 1610183567, 1610184824);
INSERT INTO `blog_keywords` VALUES (2, '勾股BLOG', 0, 1, 1624509335, 0);
INSERT INTO `blog_keywords` VALUES (3, '勾股OA', 0, 1, 1624509335, 0);
INSERT INTO `blog_keywords` VALUES (4, '勾股DEV', 0, 1, 1624438906, 0);
INSERT INTO `blog_keywords` VALUES (5, 'ThinkPHP', 0, 1, 1624438737, 0);
INSERT INTO `blog_keywords` VALUES (6, 'PHP', 0, 1, 1624438752, 0);
INSERT INTO `blog_keywords` VALUES (7, 'MySql', 0, 1, 1624438770, 0);
INSERT INTO `blog_keywords` VALUES (8, 'Vue', 0, 1, 1624438843, 0);
INSERT INTO `blog_keywords` VALUES (9, 'Git', 0, 1, 1624438861, 0);
INSERT INTO `blog_keywords` VALUES (10, 'layui', 0, 1, 1624438882, 0);
INSERT INTO `blog_keywords` VALUES (11, 'composer', 0, 1, 1624438943, 0);
INSERT INTO `blog_keywords` VALUES (12, 'ngix', 0, 1, 1624438943, 0);

-- ----------------------------
-- Table structure for `blog_article_cate`
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_cate`;
CREATE TABLE `blog_article_cate` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT '0' COMMENT '父类ID',
  `sort` int(5) NOT NULL DEFAULT '0' COMMENT '排序',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `keywords` varchar(255) DEFAULT '' COMMENT '关键字',
  `desc` varchar(1000) DEFAULT '' COMMENT '描述',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '添加时间',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='文章分类表';
-- ----------------------------
-- Records of blog_article_cate
-- ----------------------------
INSERT INTO `blog_article_cate` VALUES (1, 0, 0, '勾股开源系统', '1', '左手自研，右手开源', 0, 1610196442);

-- ----------------------------
-- Table structure for `blog_article`
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `keywords` varchar(255) DEFAULT '' COMMENT '关键字',
  `desc` varchar(1000) DEFAULT '' COMMENT '摘要',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1正常-1下架',
  `thumb` int(11) NOT NULL DEFAULT 0 COMMENT '缩略图id',
  `original` int(1) NOT NULL DEFAULT 0 COMMENT '是否原创，1原创',
  `origin` varchar(255) NOT NULL DEFAULT '' COMMENT '来源或作者',
  `origin_url` varchar(255) NOT NULL DEFAULT '' COMMENT '来源地址',
  `content` text NOT NULL COMMENT '内容',
  `md_content` text NOT NULL COMMENT 'markdown内容',
  `read` int(11) NOT NULL DEFAULT '0' COMMENT '阅读量',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '属性：1精华 2热门 3推荐',
  `is_home` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否首页显示，0否，1是',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `article_cate_id` int(11) NOT NULL DEFAULT '0',
  `create_time` int(11) NOT NULL DEFAULT '0',
  `update_time` int(11) NOT NULL DEFAULT '0',
  `delete_time` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='文章表';

-- ----------------------------
-- Records of blog_article
-- ----------------------------
INSERT INTO `blog_article` VALUES (1, '勾股CMS，开源免费的CMS内容管理系统', '', '勾股CMS是一套基于ThinkPHP6+Layui+MySql打造的轻量级、高性能极速后台开发框架。通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，操作简单、极低门槛、开箱即用。', 1, 4, 0, '', '', '<h4 id=\"h4--cms-\"><a name=\"勾股CMS简介\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>勾股CMS简介</h4><p>勾股CMS是一套基于<code>ThinkPHP6</code> + <code>Layui</code> + <code>MySql</code>打造的轻量级、高性能极速后台开发框架。通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，操作简单、极低门槛、开箱即用。系统易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。</p>\n<h4 id=\"h4--cms-\"><a name=\"勾股CMS链接\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>勾股CMS链接</h4><ol>\n<li>勾股CMS：<a href=\"https://www.gougucms.com\">https://www.gougucms.com</a></li><li>gitee：<a href=\"https://gitee.com/gougucms/gougucms.git\">https://gitee.com/gougucms/gougucms.git</a></li><li>后台体验地址：<a href=\"https://www.gougucms.com/admin/index/index.html\">https://www.gougucms.com/admin/index/index.html</a></li><li>后台体验账号：gougucms 密码：gougucms</li></ol>\n<h4 id=\"h4-u529Fu80FDu77E9u9635\"><a name=\"功能矩阵\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>功能矩阵</h4><p><img src=\"https://www.gougucms.com/storage/image/gougucms2.0.png\" alt=\"功能导图\" title=\"功能导图\"><br>系统后台集成了主流的通用功能，有：<code>登录验证</code>、<code>系统配置</code>、<code>操作日志管理</code>、<code>管理员</code>、<code>角色权限</code>、<code>功能节点</code>、<code>功能模块</code>、<code>导航设置</code>、<code>网站地图</code>、<code>轮播广告</code>、 <code>TAG关键字管理</code>、<code>文件上传</code>、<code>数据备份/还原</code>、<code>文章功能</code>、<code>商品功能</code>、<code>用户管理</code>、<code>用户操作日志</code>、<code>用户注册/登录</code>、<code>API接口</code> 等。更多的个性化功能可以基于当前系统便捷做二次开发。</p>\n', '#### 勾股CMS简介\n勾股CMS是一套基于`ThinkPHP6` + `Layui` + `MySql`打造的轻量级、高性能极速后台开发框架。通用型的后台权限管理框架，前后台用户的操作记录覆盖跟踪，操作简单、极低门槛、开箱即用。系统易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。\n\n#### 勾股CMS链接\n1. 勾股CMS：https://www.gougucms.com\n2. gitee：https://gitee.com/gougucms/gougucms.git\n3. 后台体验地址：https://www.gougucms.com/admin/index/index.html\n4. 后台体验账号：gougucms 密码：gougucms\n\n#### 功能矩阵\n![功能导图](https://www.gougucms.com/storage/image/gougucms2.0.png \"功能导图\")\n系统后台集成了主流的通用功能，有：`登录验证`、`系统配置`、`操作日志管理`、`管理员`、`角色权限`、`功能节点`、`功能模块`、`导航设置`、`网站地图`、`轮播广告`、 `TAG关键字管理`、`文件上传`、`数据备份/还原`、`文章功能`、`商品功能`、`用户管理`、`用户操作日志`、`用户注册/登录`、`API接口` 等。更多的个性化功能可以基于当前系统便捷做二次开发。', 0, 2, 1, 0, 1, 1625071256, 1646547366, 0);
INSERT INTO `blog_article` VALUES (2, '勾股BLOG，简单实用的开源免费的博客软件', '', '勾股博客是一款简单实用的开源免费的博客软件，各管理模块，操作简单，具有简约，易用，访问统计，内存占用低等特点，系统易于功能扩展，代码维护，方便二次开发，可以用来做个人博客，工作室官网，自媒体官网等网站。', 1, 5, 0, '', '', '<h4 id=\"h4--blog\"><a name=\"勾股BLOG\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>勾股BLOG</h4><p>勾股博客是一款基于ThinkPHP6 + Layui + MySql打造的，简单实用的开源免费的博客系统。各管理模块，操作简单，具有简约，易用，访问统计，内存占用低等特点，系统易于功能扩展，代码维护，方便二次开发。可以用来做个人博客，工作室官网，自媒体官网等网站，二次开发之后也可以作为资讯、展品展示等门户网站。</p>\n<h4 id=\"h4--blog-\"><a name=\"勾股BLOG链接\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>勾股BLOG链接</h4><ul>\n<li>博客：<a href=\"https://blog.gougucms.com\">https://blog.gougucms.com</a></li><li>gitee：<a href=\"https://gitee.com/gougucms/blog.git\">https://gitee.com/gougucms/blog.git</a></li><li>后台体验地址：<a href=\"https://blog.gougucms.com/admin/index/index.html\">https://blog.gougucms.com/admin/index/index.html</a></li><li>后台体验账号：gougublog 密码：gougublog</li></ul>\n<h4 id=\"h4-u529Fu80FDu77E9u9635\"><a name=\"功能矩阵\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>功能矩阵</h4><p>系统后台集成了主流的通用功能，如：<code>登录验证</code>、<code>系统配置</code>、<code>操作日志管理</code>、<code>用户（组）管理</code>、<code>用户（组）权限</code>、<code>功能管理（后台菜单管理）</code>、<code>导航设置</code>、<code>网站地图</code>、<code>轮播广告</code>、<code>TAG关键字管理</code>、<code>友情链接</code>、<code>文件上传</code>、<code>数据备份/还原</code>、<code>博客文章功能</code>、<code>用户管理</code>、<code>用户操作日志</code>、<code>用户注册/登录</code>、<code>博客归档</code>、<code>博客动态</code>、<code>语雀知识库</code>、<code>访问统计</code>等。更多的个性化功能可以基于当前博客系统便捷做二次开发。</p>\n', '#### 勾股BLOG\n勾股博客是一款基于ThinkPHP6 + Layui + MySql打造的，简单实用的开源免费的博客系统。各管理模块，操作简单，具有简约，易用，访问统计，内存占用低等特点，系统易于功能扩展，代码维护，方便二次开发。可以用来做个人博客，工作室官网，自媒体官网等网站，二次开发之后也可以作为资讯、展品展示等门户网站。\n\n#### 勾股BLOG链接\n- 博客：https://blog.gougucms.com\n- gitee：https://gitee.com/gougucms/blog.git\n- 后台体验地址：https://blog.gougucms.com/admin/index/index.html\n- 后台体验账号：gougublog 密码：gougublog\n\n#### 功能矩阵\n\n系统后台集成了主流的通用功能，如：`登录验证`、`系统配置`、`操作日志管理`、`用户（组）管理`、`用户（组）权限`、`功能管理（后台菜单管理）`、`导航设置`、`网站地图`、`轮播广告`、`TAG关键字管理`、`友情链接`、`文件上传`、`数据备份/还原`、`博客文章功能`、`用户管理`、`用户操作日志`、`用户注册/登录`、` 博客归档`、`博客动态`、`语雀知识库`、`访问统计`等。更多的个性化功能可以基于当前博客系统便捷做二次开发。', 1, 0, 1, 0, 1, 1646123791, 1646547518, 0);
INSERT INTO `blog_article` VALUES (3, '勾股OA，实用的开源免费的企业办公系统框架', '', '勾股OA是一款简单实用的开源免费的企业办公系统框架。系统集成了十大办公基本的功能模块：系统管理、基础数据、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能等基础模块。', 1, 6, 0, '', '', '<h4 id=\"h4-u7B80u4ECB\"><a name=\"简介\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>简介</h4><p>1、勾股OA是一款基于<code>ThinkPHP6</code> + <code>Layui</code> +<code>MySql</code>打造的，简单实用的开源免费的企业办公系统框架。<br>2、系统各功能模块，一目了然，操作简单；通用型的后台权限管理框架，员工的操作记录覆盖跟踪，紧随潮流、极低门槛、开箱即用。<br>3、系统集成了九大办公基本的功能模块：系统管理、基础数据、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能等基础模块。<br>4、系统易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。<br>5、开发人员可以快速基于此系统进行二次开发，免去写一次系统架构的痛苦，通过二次开发之后可以用来做<code>CRM</code>，<code>ERP</code>，<code>项目管理</code>等企业办公系统。</p>\n<h4 id=\"h4-u6F14u793Au5730u5740\"><a name=\"演示地址\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>演示地址</h4><p>   勾股OA演示地址：<a href=\"https://oa.gougucms.com\">https://oa.gougucms.com</a></p>\n<p>   <strong>登录账号及密码：</strong></p>\n<pre><code>   BOSS角色：suhaizhen     123456\n   人事总监：fengcailing    123456\n   财务总监：yucixin        123456\n   市场总监：qinjiaxian     123456\n   技术总监：yexiaochai     123456\n</code></pre><h4 id=\"h4-u529Fu80FDu77E9u9635\"><a name=\"功能矩阵\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>功能矩阵</h4><p>系统后台集成了主流的通用功能，如：<code>登录验证</code>、<code>系统配置</code>、<code>操作日志管理</code>、<code>用户（组）管理</code>、<code>用户（组）权限</code>、<code>功能管理</code>、<code>模块管理</code>、<code>TAG关键字管理</code>、<code>文件上传</code>、<code>数据备份/还原</code>、<code>基础数据</code>、<code>审批流程</code>、<code>员工管理</code>、<code>消息通知</code>、<code>企业公告</code>、<code>知识文章</code>、<code>办公审批</code>、<code>日常办公</code>、<code>财务管理</code>、<code>商业智能</code>、 <code>API接口</code>等。更多的个性化功能可以基于当前系统便捷做二次开发。<br><strong>功能导图：</strong><br><img src=\"https://oa.gougucms.com/storage/image/gouguoa2.0.png\" alt=\"输入图片说明\"></p>\n', '#### 简介\n1、勾股OA是一款基于`ThinkPHP6` + `Layui` +` MySql`打造的，简单实用的开源免费的企业办公系统框架。\n2、系统各功能模块，一目了然，操作简单；通用型的后台权限管理框架，员工的操作记录覆盖跟踪，紧随潮流、极低门槛、开箱即用。\n3、系统集成了九大办公基本的功能模块：系统管理、基础数据、员工管理、消息通知、企业公告、知识文章、办公审批、日常办公、财务管理、商业智能等基础模块。\n4、系统易于功能扩展，代码维护，方便二次开发，帮助开发者简单高效降低二次开发的成本，满足专注业务深度开发的需求。\n5、开发人员可以快速基于此系统进行二次开发，免去写一次系统架构的痛苦，通过二次开发之后可以用来做`CRM`，`ERP`，`项目管理`等企业办公系统。\n\n#### 演示地址\n\n   勾股OA演示地址：[https://oa.gougucms.com](https://oa.gougucms.com)\n\n   **登录账号及密码：**\n~~~\n   BOSS角色：suhaizhen     123456\n   人事总监：fengcailing    123456\n   财务总监：yucixin        123456\n   市场总监：qinjiaxian     123456\n   技术总监：yexiaochai     123456\n~~~\n#### 功能矩阵\n\n系统后台集成了主流的通用功能，如：`登录验证`、`系统配置`、`操作日志管理`、`用户（组）管理`、`用户（组）权限`、`功能管理`、`模块管理`、`TAG关键字管理`、`文件上传`、`数据备份/还原`、`基础数据`、`审批流程`、`员工管理`、`消息通知`、`企业公告`、`知识文章`、`办公审批`、`日常办公`、`财务管理`、`商业智能`、 `API接口`等。更多的个性化功能可以基于当前系统便捷做二次开发。\n**功能导图：**\n![输入图片说明](https://oa.gougucms.com/storage/image/gouguoa2.0.png)', 1, 0, 1, 0, 1, 1646547615, 1646547674, 0);
INSERT INTO `blog_article` VALUES (4, '勾股DEV——研发管理与团队协作的工具软件', '', '勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。', 1, 7, 0, '', '', '<h4 id=\"h4-u7CFBu7EDFu4ECBu7ECD\"><a name=\"系统介绍\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>系统介绍</h4><p>勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。</p>\n<p>勾股DEV的产品理念：通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。</p>\n<h4 id=\"h4-u6F14u793Au5730u5740\"><a name=\"演示地址\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>演示地址</h4><p>勾股DEV演示地址：<a href=\"https://dev.gougucms.com\">https://dev.gougucms.com</a></p>\n<p>PS：为了给后面的人提供良好的演示体验，体验以查看为主。</p>\n<p><strong>登录账号及密码：</strong></p>\n<pre><code>   技术总监：gougujishu     123456\n   工 程 师：ligong    123456\n   产品经理：ouyangchanpin        123456\n</code></pre><h4 id=\"h4-u7CFBu7EDFu7279u70B9\"><a name=\"系统特点\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>系统特点</h4><ul>\n<li>多产品支持，可添加多产品管理</li><li>多项目支持，可以多项目同时进行管理</li><li>可配置的用户角色控制，不同的角色可配置不同的操作权限</li><li>Wiki 形式的文档撰写，Mardown编辑器，工程师使用高效便捷</li><li>每个项目配置有需求、任务、Wiki文档、动态记录、互动评论、工作记录模块</li><li>任务时间跟踪机制，项目任务多状态流转，任务成果可见可控。</li><li>工时登记，团队精细化管理，可统计每个人每天在每个项目做了多少时间</li><li>任务安排，任务分配指定人，可设置负责人、多协同人员</li><li>员工的操作记录全覆盖跟踪</li></ul>\n', '#### 系统介绍\n勾股DEV是一款专为IT研发团队打造的智能化项目管理与团队协作的工具软件，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。\n\n勾股DEV的产品理念：通过“项目（Project）”的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。\n\n#### 演示地址\n\n勾股DEV演示地址：[https://dev.gougucms.com](https://dev.gougucms.com)\n\nPS：为了给后面的人提供良好的演示体验，体验以查看为主。\n\n**登录账号及密码：**\n~~~\n   技术总监：gougujishu     123456\n   工 程 师：ligong    123456\n   产品经理：ouyangchanpin        123456\n~~~\n\n#### 系统特点\n- 多产品支持，可添加多产品管理\n- 多项目支持，可以多项目同时进行管理\n- 可配置的用户角色控制，不同的角色可配置不同的操作权限\n- Wiki 形式的文档撰写，Mardown编辑器，工程师使用高效便捷\n- 每个项目配置有需求、任务、Wiki文档、动态记录、互动评论、工作记录模块\n- 任务时间跟踪机制，项目任务多状态流转，任务成果可见可控。\n- 工时登记，团队精细化管理，可统计每个人每天在每个项目做了多少时间\n- 任务安排，任务分配指定人，可设置负责人、多协同人员\n- 员工的操作记录全覆盖跟踪', 1, 0, 1, 0, 1, 1650819123, 1650819471, 0);

-- ----------------------------
-- Table structure for `blog_article_keywords`
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_keywords`;
CREATE TABLE `blog_article_keywords` (
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
-- Records of blog_article_keywords
-- ----------------------------
INSERT INTO `blog_article_keywords` VALUES (1, 1, 1, 1, 1646547750);
INSERT INTO `blog_article_keywords` VALUES (2, 2, 2, 1, 1646547764);
INSERT INTO `blog_article_keywords` VALUES (3, 3, 3, 1, 1646547779);
INSERT INTO `blog_article_keywords` VALUES (4, 4, 4, 1, 1646547779);

-- ----------------------------
-- Table structure for blog_sitemap_cate
-- ----------------------------
DROP TABLE IF EXISTS `blog_sitemap_cate`;
CREATE TABLE `blog_sitemap_cate`  (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '分类名称',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1可用-1禁用',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '网站地图分类表';

-- ----------------------------
-- Table structure for blog_sitemap
-- ----------------------------
DROP TABLE IF EXISTS `blog_sitemap`;
CREATE TABLE `blog_sitemap`  (
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
-- Table structure for `blog_nav`
-- ----------------------------
DROP TABLE IF EXISTS `blog_nav`;
CREATE TABLE `blog_nav` (
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
-- Records of `blog_nav`
-- -----------------------------
INSERT INTO `blog_nav` VALUES ('1', '主导航', 'NAV_HOME', '1', '平台主导航', '0', '0');

-- ----------------------------
-- Table structure for `blog_nav_info`
-- ----------------------------
DROP TABLE IF EXISTS `blog_nav_info`;
CREATE TABLE `blog_nav_info` (
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
-- Records of `blog_nav_info`
-- -----------------------------
INSERT INTO `blog_nav_info` VALUES (1, 0, 1, '首页', '/', 'index', 0, 1, 1, 0, 0);
INSERT INTO `blog_nav_info` VALUES (2, 0, 1, '博客文章', '/home/article/cate.html', 'article', 0, 1, 2, 0, 0);
INSERT INTO `blog_nav_info` VALUES (3, 0, 1, '博客动态', '/home/action/index.html', 'action', 0, 1, 3, 0, 0);
INSERT INTO `blog_nav_info` VALUES (4, 0, 1, '开源系统', '', '', 0, 1, 4, 0, 0);
INSERT INTO `blog_nav_info` VALUES (5, 0, 1, '语雀文档', '/home/book/index.html', 'book', 0, 1, 5, 0, 0);
INSERT INTO `blog_nav_info` VALUES (6, 0, 1, '后台演示', '/admin', '', 1, 1, 6, 0, 0);
INSERT INTO `blog_nav_info` VALUES (7, 4, 1, '勾股CMS', 'https://www.gougucms.com/', '', 1, 1, 7, 0, 0);
INSERT INTO `blog_nav_info` VALUES (8, 4, 1, '勾股OA办公系统', 'https://www.gougucms.com/home/pages/detail/s/gouguoa.html', '', 1, 1, 8, 0, 0);
INSERT INTO `blog_nav_info` VALUES (9, 4, 1, '勾股DEV项目管理系统', 'https://www.gougucms.com/home/pages/detail/s/gougudev.html', '', 1, 1, 9, 0, 0);
INSERT INTO `blog_nav_info` VALUES (10, 0, 1, '文章归档', '/home/archives/index.html', 'archives', 0, 1, 10, 0, 0);

-- ----------------------------
-- Table structure for `blog_slide`
-- ----------------------------
-- ----------------------------
DROP TABLE IF EXISTS `blog_slide`;
CREATE TABLE `blog_slide` (
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
-- Records of blog_slide
-- ----------------------------
INSERT INTO `blog_slide` VALUES ('1', '首页轮播', 'INDEX_SLIDE', '1', '首页轮播组。', '0', '0');

-- ----------------------------
-- Table structure for `blog_slide_info`
-- ----------------------------
DROP TABLE IF EXISTS `blog_slide_info`;
CREATE TABLE `blog_slide_info` (
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
-- Records of blog_slide_info
-- ----------------------------
INSERT INTO `blog_slide_info` VALUES (1, 1, '科技创新的星辰大海、未来的无限可能性', '', '1', 'https://www.gougucms.com/', 1, 0, 1624442489, 1624508023);
INSERT INTO `blog_slide_info` VALUES (2, 1, '数字化 信息化 智能化', '', '2', 'https://blog.gougucms.com/home/book/detail/bid/3.html', 1, 0, 1624444396, 1624508885);
INSERT INTO `blog_slide_info` VALUES (3, 1, '创新自己，为盛世添彩', '', '3', 'https://blog.gougucms.com/', 1, 0, 1624500902, 1624508936);

-- ----------------------------
-- Table structure for blog_links
-- ----------------------------
DROP TABLE IF EXISTS `blog_links`;
CREATE TABLE `blog_links`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '网站标题',
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
-- Records of blog_links
-- ----------------------------
INSERT INTO `blog_links` VALUES (1, '勾股CMS', 0, 'https://www.gougucms.com', 1, 1, 1, 1624516962, 1624517078);
INSERT INTO `blog_links` VALUES (2, '勾股博客', 0, 'https://blog.gougucms.com', 0, 1, 2, 1624517262, 1624517178);
INSERT INTO `blog_links` VALUES (3, '勾股OA', 0, 'https://blog.gougucms.com/home/book/detail/bid/3.html', 0, 1, 3, 1624517262, 1624517178);
INSERT INTO `blog_links` VALUES (4, '勾股DEV', 0, 'https://blog.gougucms.com/home/book/detail/bid/7.html', 0, 1, 4, 1624517262, 1624517178);

-- ----------------------------
-- Table structure for blog_search_keywords
-- ----------------------------
DROP TABLE IF EXISTS `blog_search_keywords`;
CREATE TABLE `blog_search_keywords`  (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '关键字',
  `times` int(11) NOT NULL DEFAULT 1 COMMENT '搜索次数',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1,2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '搜索关键字表';

-- ----------------------------
-- Records of blog_search_keywords
-- ----------------------------
INSERT INTO `blog_search_keywords` VALUES (1, '勾股CMS', 1, 1);
INSERT INTO `blog_search_keywords` VALUES (2, '勾股BLOG', 1, 1);
INSERT INTO `blog_search_keywords` VALUES (3, '勾股OA', 1, 1);
INSERT INTO `blog_search_keywords` VALUES (4, '勾股DEV', 1, 1);

-- ----------------------------
-- Table structure for blog_user_level
-- ----------------------------
DROP TABLE IF EXISTS `blog_user_level`;
CREATE TABLE `blog_user_level`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '等级名称',
  `desc` varchar(1000) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态,0禁用,1正常',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '会员等级表';

-- ----------------------------
-- Records of blog_admin_module
-- ----------------------------
INSERT INTO `blog_user_level` VALUES (1, '普通会员','', 1, 1639562910, 0);
INSERT INTO `blog_user_level` VALUES (2, '铜牌会员','', 1, 1639562910, 0);
INSERT INTO `blog_user_level` VALUES (3, '银牌会员','', 1, 1639562910, 0);
INSERT INTO `blog_user_level` VALUES (4, '黄金会员','', 1, 1639562910, 0);
INSERT INTO `blog_user_level` VALUES (5, '白金会员','', 1, 1639562910, 0);
INSERT INTO `blog_user_level` VALUES (6, '钻石会员','', 1, 1639562910, 0);


-- ----------------------------
-- Table structure for blog_user
-- ----------------------------
DROP TABLE IF EXISTS `blog_user`;
CREATE TABLE `blog_user`  (
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
-- Records of for `blog_user`
-- ----------------------------
INSERT INTO `blog_user` VALUES (1, '勾股CMS', '', 'hdm58', '7aba99e08564eb6a9a6038255aeb265c', '03K6PWjT2dAFBsa8oJYZ', '小明名', '13589858989', 0, 'hdm58@qq.com', '/static/admin/images/icon.png', 0, '勾股科技', 1627401600, '', '', '广州', '勾股科技', '珠江新城', '技术部', '技术总监', 0, 0, 1, 1, 1645009233, '163.142.175.169', 7, 1627457646, '163.142.247.150', 0, 0);

-- ----------------------------
-- Table structure for `blog_user_log`
-- ----------------------------
DROP TABLE IF EXISTS `blog_user_log`;
CREATE TABLE `blog_user_log` (
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
-- Table structure for `blog_file`
-- ----------------------------
DROP TABLE IF EXISTS `blog_file`;
CREATE TABLE `blog_file` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `module` varchar(15) NOT NULL DEFAULT '' COMMENT '所属模块',
  `sha1` varchar(60) NOT NULL COMMENT 'sha1',
  `md5` varchar(60) NOT NULL COMMENT 'md5',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '原始文件名',
  `filename` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名',
  `filepath` varchar(255) NOT NULL DEFAULT '' COMMENT '文件路径+文件名',
  `filesize` int(10)  NOT NULL DEFAULT 0 COMMENT '文件大小',
  `fileext` varchar(10) NOT NULL DEFAULT '' COMMENT '文件后缀',
  `mimetype` varchar(100) NOT NULL DEFAULT '' COMMENT '文件类型',
  `user_id` int(11) unsigned NOT NULL DEFAULT 0 COMMENT '上传会员ID',
  `uploadip` varchar(15) NOT NULL DEFAULT '' COMMENT '上传IP',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未审核1已审核-1不通过',
  `create_time` int(11) NOT NULL DEFAULT '0',
  `admin_id` int(11) NOT NULL COMMENT '审核者id',
  `audit_time` int(11) NOT NULL DEFAULT '0' COMMENT '审核时间',
  `action` varchar(50) NOT NULL DEFAULT '' COMMENT '来源模块功能',
  `use` varchar(255) NULL DEFAULT NULL COMMENT '用处',
  `download` int(11) NOT NULL DEFAULT 0 COMMENT '下载量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT='文件表';

-- ----------------------------
-- Records of blog_file
-- ----------------------------
INSERT INTO `blog_file` VALUES (1, 'admin', 'f879803efe5aef5a238739fed8b1e31ca1e21b03', '42eb6431486066280f58c2ea4fc861b1', 'banner1.jpg', '202107/42eb6431486066280f58c2ea4fc861b1.jpg', '//blog.gougucms.com/storage/202107/42eb6431486066280f58c2ea4fc861b1.jpg', 92269, 'jpg', 'image/jpeg', 1, '127.0.0.1', 1, 1625070962, 1, 1625070962, 'upload', 'thumb', 0);
INSERT INTO `blog_file` VALUES (2, 'admin', 'd5884728a79067a5110eb2f03b18949f969c5a3f', '37613580d8ebf3ccee2fc5bb6a7218ae', 'banner2.jpg', '202107/37613580d8ebf3ccee2fc5bb6a7218ae.jpg', '//blog.gougucms.com/storage/202107/37613580d8ebf3ccee2fc5bb6a7218ae.jpg', 76572, 'jpg', 'image/jpeg', 1, '127.0.0.1', 1, 1625070952, 1, 1625070952, 'upload', 'thumb', 0);
INSERT INTO `blog_file` VALUES (3, 'admin', '8ae571c86b19196251d5990f9c3dd8a937ddb43e', '4b8ed715027c1a411b1b7e484e95f14b', 'banner3.jpg', '202107/4b8ed715027c1a411b1b7e484e95f14b.jpg', '//blog.gougucms.com/storage/202107/4b8ed715027c1a411b1b7e484e95f14b.jpg', 121772, 'jpg', 'image/jpeg', 1, '127.0.0.1', 1, 1625070934, 1, 1625070934, 'upload', 'thumb', 0);
INSERT INTO `blog_file` VALUES (4, 'admin', 'c023e859a0ebb50ad4314b8f6cabff1e971d30a9', 'f95982689eb222b84e999122a50b3780', 'banner4.jpg', '202203/f95982689eb222b84e999122a50b3780.jpg', '//blog.gougucms.com/storage/202202/f95982689eb222b84e999122a50b3780.jpg', 49414, 'jpg', 'image/jpeg', 1, '127.0.0.1', 1, 1646547747, 1, 1646547747, 'upload', 'thumb', 0);
INSERT INTO `blog_file` VALUES (5, 'admin', '1d8fdebf31de031bdcc081f685ce0501d5b1c561', '0f22a5ba4797b2fa22049ea73e6f779c', 'banner5.jpg', '202203/0f22a5ba4797b2fa22049ea73e6f779c.jpg', '//blog.gougucms.com/storage/202202/0f22a5ba4797b2fa22049ea73e6f779c.jpg', 39580, 'jpg', 'image/jpeg', 1, '127.0.0.1', 1, 1646547761, 1, 1646547761, 'upload', 'thumb', 0);
INSERT INTO `blog_file` VALUES (6, 'admin', '160933bd92c2db7474fbe8690abb492e98543eb3', 'e729477de18e3be7e7eb4ec7fe2f821e', 'banner6.jpg', '202203/e729477de18e3be7e7eb4ec7fe2f821e.jpg', '//blog.gougucms.com/storage/202202/e729477de18e3be7e7eb4ec7fe2f821e.jpg', 58603, 'jpg', 'image/jpeg', 1, '127.0.0.1', 1, 1646547777, 1, 1646547777, 'upload', 'thumb', 0);
INSERT INTO `blog_file` VALUES (7, 'admin', '98c8ee44688e5773a30ef2a45bd129873c741831', '1193f7a1585b9f6e8a97ae17718018b3', '1010.jpg', '202204/1193f7a1585b9f6e8a97ae17718018b3.jpg', '//blog.gougucms.com/storage/202204/1193f7a1585b9f6e8a97ae17718018b3.jpg', 66777, 'jpg', 'image/jpeg', 1, '127.0.0.1', 1, 1650819465, 1, 1650819465, 'upload', 'thumb', 0);


-- ----------------------------
-- Table structure for blog_action
-- ----------------------------
DROP TABLE IF EXISTS `blog_action`;
CREATE TABLE `blog_action`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1正常-1下架',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '类型：1文字 2视频 3音频',
  `uid` int(11) NOT NULL DEFAULT 1 COMMENT '关联用户',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COMMENT = '动态表';

-- ----------------------------
-- Records of blog_action
-- ----------------------------
INSERT INTO `blog_action` VALUES (1, 1, '欢迎使用勾股blog开启您的新旅程！', 1, 1, 1635214480, 0);

-- ----------------------------
-- Table structure for cms_pages
-- ----------------------------
DROP TABLE IF EXISTS `blog_pages`;
CREATE TABLE `blog_pages`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '-1删除 0下架 1正常',
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT '页面名称',
  `thumb` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缩略图',
  `banner` varchar(1000) NOT NULL DEFAULT '' COMMENT '图集相册',
  `desc` varchar(1000) NOT NULL DEFAULT '' COMMENT '页面摘要',
  `content` text NOT NULL COMMENT '内容',
  `md_content` text NOT NULL COMMENT 'markdown内容',
  `read` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读量',
  `sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT 'url文件名',
  `template` varchar(200) NOT NULL DEFAULT '' COMMENT '前端模板',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '编辑时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '单页面表';

-- ----------------------------
-- Records of cms_pages
-- ----------------------------
INSERT INTO `blog_pages` VALUES (1, 1, '关于我们', 1, '', '勾股CMS是一套基于ThinkPHP6+Layui+MySql打造的轻量级、高性能快速建站的内容管理系统。后台管理模块，一目了然，操作简单，通用型后台权限管理框架，开箱即用。', '<p>勾股CMS是一套基于ThinkPHP6 + Layui + MySql打造的轻量级、高性能极速后台开发框架。通用型的后台权限管理框架，操作简单、开箱即用。系统易于功能扩展，代码维护，方便二次开发，让开发者更专注于业务深度需求的开发，帮助开发者简单高效降低二次开发成本。</p>\n<p><img src=\"https://blog.gougucms.com/storage/image/202109/1630457997502502.png\" /></p>', '', 1, 0, '', 'default', 0, 1653984295, 1653993466);
INSERT INTO `blog_pages` VALUES (2, 1, '勾股OA办公系统', 0, '', '勾股OA是一款实用的企业办公系统框架。集成了系统设置、人事管理、消息管理、审批管理、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。', '<p>勾股OA是一款实用的企业办公系统框架。集成了系统设置、人事管理、消息管理、审批管理、日常办公、财务管理等基础模块。系统简约，易于功能扩展，方便二次开发，让开发者更专注于业务深度需求的开发，通过二次开发之后可以用来做CRM，ERP，业务管理等系统。</p>', '', 9, 0, 'gouguoa', 'oa', 0, 1654419120, 0);
INSERT INTO `blog_pages` VALUES (3, 1, '勾股DEV项目管理系统', 0, '', '勾股DEV是一款专为IT研发团队打造的项目管理与团队协作的系统工具，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。', '<p >勾股DEV是一款专为IT研发团队打造的项目管理与团队协作的系统工具，可以在线管理团队的工作、项目和任务，覆盖从需求提出到研发完成上线整个过程的项目协作。</p>\n<p>勾股DEV的产品理念：通过&ldquo;项目（Project）&rdquo;的形式把成员、需求、任务、缺陷(BUG)、文档、互动讨论以及各种形式的资源组织在一起，团队成员参与更新任务、文档等内容来推动项目的进度，同时系统利用时间线索和各种动态的报表的形式来自动给成员汇报项目进度。</p>', '', 8, 0, 'gougudev', 'dev', 0, 1654167139, 0);

-- ----------------------------
-- Table structure for `blog_pages_keywords`
-- ----------------------------
DROP TABLE IF EXISTS `blog_pages_keywords`;
CREATE TABLE `blog_pages_keywords` (
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
INSERT INTO `blog_pages_keywords` VALUES (1, 1, 1, 1, 1644823517);
INSERT INTO `blog_pages_keywords` VALUES (2, 2, 2, 1, 1644823517);
INSERT INTO `blog_pages_keywords` VALUES (3, 3, 3, 1, 1644823517);

-- ----------------------------
-- Table structure for blog_book
-- ----------------------------
DROP TABLE IF EXISTS `blog_book`;
CREATE TABLE `blog_book`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '作者',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '排序',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `thumb` varchar(255) NOT NULL DEFAULT '' COMMENT '缩略图',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '描述',
  `create_time` int(11) NOT NULL DEFAULT 0 COMMENT '添加时间',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '修改时间',
  `is_index` int(1) NOT NULL DEFAULT 1 COMMENT '是否首页展示:1是2否',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:1正常0垃圾箱-1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '文档分类表';

-- ----------------------------
-- Records of blog_book
-- ----------------------------
INSERT INTO `blog_book` VALUES (1, 1, 0, '勾股CMS在线文档', '//blog.gougucms.com/storage/202202/f95982689eb222b84e999122a50b3780.jpg', '勾股CMS在线文档', 1638592378, 1638612466, 1,1);
INSERT INTO `blog_book` VALUES (2, 1, 0, '勾股BLOG在线文档', '//blog.gougucms.com/storage/202202/0f22a5ba4797b2fa22049ea73e6f779c.jpg', '勾股BLOG在线文档', 1638596734, 1638613301, 1,1);
INSERT INTO `blog_book` VALUES (3, 1, 0, '勾股OA在线文档', '//blog.gougucms.com/storage/202202/e729477de18e3be7e7eb4ec7fe2f821e.jpg', '勾股OA在线文档', 1638599320, 1638612950, 1,1);

-- ----------------------------
-- Table structure for blog_doc
-- ----------------------------
DROP TABLE IF EXISTS `blog_doc`;
CREATE TABLE `blog_doc`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT 0 COMMENT '父章节',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `type` int(1) NOT NULL DEFAULT 1 COMMENT '类型:1文档2章节3跳转链接',
  `book_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联文档分类id',
  `desc` varchar(1000) NULL DEFAULT '' COMMENT '摘要',
  `admin_id` int(11) NOT NULL DEFAULT 0 COMMENT '作者',
  `link` varchar(255) NOT NULL DEFAULT '' COMMENT '跳转地址',
  `content` text NULL COMMENT '内容',
  `md_content` text NULL COMMENT 'markdown内容',
  `read` int(11) NOT NULL DEFAULT 0 COMMENT '阅读量',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态:1正常0垃圾箱-1删除',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` int(11) NOT NULL DEFAULT 0,
  `update_time` int(11) NOT NULL DEFAULT 0,
  `delete_time` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '文档表';
