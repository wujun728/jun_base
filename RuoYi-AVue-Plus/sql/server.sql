CREATE TABLE `sys_server` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `server_name` varchar(100) DEFAULT NULL COMMENT '服务名称',
  `domain_path` varchar(255) DEFAULT NULL COMMENT '服务域名路径',
  `accept_token` varchar(32) DEFAULT NULL COMMENT '授权token',
  `status` char(1) DEFAULT '0' COMMENT '状态（1正常 -1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- 默认测试用例中的服务节点
INSERT INTO  `sys_server`(`id`, `server_name`, `domain_path`, `accept_token`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1, 'elab-test', 'http://localhost:8765', 'abc12345', '1', '', '2021-08-20 15:19:02', '', '2021-08-27 15:31:15');

-- 服务注册菜单
INSERT INTO  `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2003, '服务注册', 1, 18, '/server', 'system/server/index', 1, 0, 'C', '0', '0', 'server:index', 'server', 'admin', '2021-08-13 18:48:23', 'admin', '2021-08-13 18:49:38', '');

-- 菜单测试案例
INSERT INTO  `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2000, '测试案例', 0, 10, 'test', NULL, 1, 0, 'M', '0', '0', '', 'example', 'admin', '2021-08-13 17:49:25', 'admin', '2021-08-15 18:21:37', '');

INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2008, 'AVUE测试', 2000, 1, 'avue', 'crud/avue-crud-test', 1, 0, 'C', '0', '0', 'test:avue', 'example', 'admin', '2021-08-25 14:19:09', 'admin', '2021-08-25 14:19:53', '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2007, '接口配置测试-需启动测试用例', 2000, 15, 'configTest?server=1&group=test-config', 'crud/server-crud', 1, 0, 'C', '0', '0', 'test:config', 'example', 'admin', '2021-08-20 16:03:41', 'admin', '2021-08-20 16:04:36', '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2006, '接口测试需启动测试用例', 2000, 13, 'service?server=1&&group=test-route', 'crud/server-crud', 1, 1, 'C', '0', '0', 'test:service', 'example', 'admin', '2021-08-19 17:39:35', 'admin', '2021-08-20 15:42:30', '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2005, '固定json参数测试', 2000, 12, 'test4?server=4&&group=tag', 'crud/crud-test', 1, 0, 'C', '0', '0', 'test:tag', 'example', 'admin', '2021-08-15 19:20:23', 'admin', '2021-08-25 10:40:59', '');