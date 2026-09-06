-- ----------------------------
-- 消息系统
-- ----------------------------
-- 消息表
DROP TABLE IF EXISTS message_system;
CREATE TABLE message_system  (
  message_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  message_title varchar(64)  NULL DEFAULT NULL COMMENT '标题',
  create_by varchar(64)  NULL DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT NULL COMMENT '创建时间',
  send_mode varchar(100)  NULL DEFAULT NULL COMMENT '发送方式(0平台 1手机号 2 邮箱)',
  code varchar(100)  NULL DEFAULT NULL COMMENT '号码',
  message_content text  NULL COMMENT '消息内容',
  message_recipient varchar(100)  NULL DEFAULT NULL COMMENT '接收人',
  message_status varchar(64)  NULL DEFAULT NULL COMMENT '消息状态(0未读 1已读)',
  message_type varchar(64)  NULL DEFAULT NULL COMMENT '消息类型',
  update_by varchar(64)  NULL DEFAULT NULL COMMENT '更新者',
  update_time datetime NULL DEFAULT NULL COMMENT '更新时间',
  remark varchar(500)  NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (message_id) USING BTREE
) ENGINE = InnoDB   COMMENT = '消息表' ;

-- 模版表
DROP TABLE IF EXISTS message_template;
CREATE TABLE message_template  (
  template_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  template_name varchar(100)  NULL DEFAULT NULL COMMENT '模版名称',
  template_code varchar(64)  NULL DEFAULT NULL COMMENT '模版CODE',
  template_type varchar(64)  NULL DEFAULT NULL COMMENT '模版类型',
  template_content text  NULL COMMENT '模版内容',
  template_variable text  NULL COMMENT '变量属性',
  create_by varchar(64)  NULL DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64)  NULL DEFAULT NULL COMMENT '更新者',
  update_time datetime NULL DEFAULT NULL COMMENT '更新时间',
  remark varchar(500)  NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (template_id) USING BTREE
) ENGINE = InnoDB   COMMENT = '模版表' ;

-- 变量表
DROP TABLE IF EXISTS message_variable;
CREATE TABLE message_variable  (
  variable_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  variable_name varchar(100)  NULL DEFAULT NULL COMMENT '变量名称',
  variable_type varchar(64)  NULL DEFAULT NULL COMMENT '变量类型',
  variable_content varchar(100)  NULL DEFAULT NULL COMMENT '变量内容',
  create_by varchar(64)  NULL DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64)  NULL DEFAULT NULL COMMENT '更新者',
  update_time datetime NULL DEFAULT NULL COMMENT '更新时间',
  remark varchar(500)  NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (variable_id) USING BTREE
) ENGINE = InnoDB   COMMENT = '变量表' ;

INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '消息系统', 0, 6, 'modelMessage', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'message', 'admin', '2024-12-31 11:57:29', 'xl', '2025-01-03 15:48:44', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

select @messageParentId := @parentId;
-- 消息系统菜单
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '消息管理', @messageParentId, 0, 'messageSystem', 'modelMessage/messageSystem/index', NULL, '', 1, 0, 'C', '0', '0', 'modelMessage:messageSystem:list', '#', 'admin', '2024-12-21 15:00:31', 'admin', '2024-12-31 15:04:49', '消息管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '消息管理查询', @parentId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:messageSystem:query', '#', 'admin', '2024-12-21 15:00:31', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '消息管理新增', @parentId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:messageSystem:add', '#', 'admin', '2024-12-21 15:00:31', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '消息管理修改', @parentId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:messageSystem:edit', '#', 'admin', '2024-12-21 15:00:31', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '消息管理删除', @parentId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:messageSystem:remove', '#', 'admin', '2024-12-21 15:00:31', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '消息管理导出', @parentId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:messageSystem:export', '#', 'admin', '2024-12-21 15:00:31', '', NULL, '');

INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '模版管理', @messageParentId, 1, 'template', 'modelMessage/template/index', NULL, '', 1, 0, 'C', '0', '0', 'modelMessage:template:list', '#', 'admin', '2024-12-31 14:59:52', '', NULL, '模版管理菜单');

SELECT @parentId := LAST_INSERT_ID();
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '模版管理查询', @parentId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:template:query', '#', 'admin', '2024-12-31 14:59:52', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '模版管理新增', @parentId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:template:add', '#', 'admin', '2024-12-31 14:59:52', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '模版管理修改', @parentId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:template:edit', '#', 'admin', '2024-12-31 14:59:52', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '模版管理删除', @parentId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:template:remove', '#', 'admin', '2024-12-31 14:59:52', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '模版管理导出', @parentId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:template:export', '#', 'admin', '2024-12-31 14:59:52', '', NULL, '');


INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '变量管理', @messageParentId, 2, 'variable', 'modelMessage/variable/index', NULL, '', 1, 0, 'C', '0', '0', 'modelMessage:variable:list', '#', 'admin', '2024-12-31 15:01:50', 'admin', '2024-12-31 15:04:56', '变量管理菜单');

SELECT @parentId := LAST_INSERT_ID();
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '变量管理查询', @parentId, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:variable:query', '#', 'admin', '2024-12-31 15:01:50', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '变量管理新增', @parentId, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:variable:add', '#', 'admin', '2024-12-31 15:01:50', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '变量管理修改', @parentId, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:variable:edit', '#', 'admin', '2024-12-31 15:01:50', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '变量管理删除', @parentId, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:variable:remove', '#', 'admin', '2024-12-31 15:01:50', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ( '变量管理导出', @parentId, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'modelMessage:variable:export', '#', 'admin', '2024-12-31 15:01:50', '', NULL, '');

-- 消息系统字典
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (142, 0, '未读', '0', 'message_status', NULL, 'primary', 'N', '0', 'xl', '2024-12-21 15:13:02', '', NULL, NULL);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (143, 1, '已读', '1', 'message_status', NULL, 'success', 'N', '0', 'xl', '2024-12-21 15:13:15', 'xl', '2024-12-21 15:13:22', NULL);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (144, 0, '平台', '0', 'send_mode', NULL, 'primary', 'N', '0', 'xl', '2024-12-25 09:40:01', '', NULL, NULL);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (145, 1, '短信', '1', 'send_mode', NULL, 'success', 'N', '0', 'xl', '2024-12-25 09:40:16', 'xl', '2025-01-01 10:12:07', NULL);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (146, 2, '邮件', '2', 'send_mode', NULL, 'warning', 'N', '0', 'xl', '2024-12-25 09:40:28', 'xl', '2025-01-01 10:12:14', NULL);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (147, 0, '验证码', '0', 'template_type', NULL, 'primary', 'N', '0', 'xl', '2025-01-03 09:22:52', '', NULL, NULL);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (148, 0, '通知', '0', 'message_type', NULL, 'primary', 'N', '0', 'xl', '2025-01-03 15:12:29', '', NULL, NULL);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (149, 0, '提示', '1', 'message_type', NULL, 'success', 'N', '0', 'xl', '2025-01-03 15:12:41', 'xl', '2025-01-03 15:12:45', NULL);
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (150, 1, '推广', '1', 'template_type', NULL, 'success', 'N', '0', 'xl', '2025-01-03 15:13:15', '', NULL, NULL);

