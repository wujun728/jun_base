-- sys_deploy_form definition
DROP TABLE IF EXISTS `sys_deploy_form`;
CREATE TABLE `sys_deploy_form`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `form_id`   bigint(20) DEFAULT NULL COMMENT '表单主键',
    `deploy_id` varchar(50) DEFAULT NULL COMMENT '流程实例主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9623  COMMENT='流程实例关联表单';

-- sys_expression definition
DROP TABLE IF EXISTS `sys_expression`;
CREATE TABLE `sys_expression`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表单主键',
    `name`        varchar(50)  DEFAULT NULL COMMENT '表达式名称',
    `expression`  varchar(255) DEFAULT NULL COMMENT '表达式内容',
    `data_type`   varchar(255) DEFAULT NULL COMMENT '表达式类型',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    `create_by`   bigint(20) DEFAULT NULL COMMENT '创建人员',
    `update_by`   bigint(20) DEFAULT NULL COMMENT '更新人员',
    `status`      tinyint(2) DEFAULT '0' COMMENT '状态',
    `remark`      varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=69  COMMENT='流程表达式';


-- sys_listener definition
DROP TABLE IF EXISTS `sys_listener`;
CREATE TABLE `sys_listener`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表单主键',
    `name`        varchar(128) DEFAULT NULL COMMENT '名称',
    `type`        char(2)      DEFAULT NULL COMMENT '监听类型',
    `event_type`  varchar(32)  DEFAULT NULL COMMENT '事件类型',
    `value_type`  varchar(32)  DEFAULT NULL COMMENT '值类型',
    `value`       varchar(255) DEFAULT NULL COMMENT '执行内容',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    `create_by`   bigint(20) DEFAULT NULL COMMENT '创建人员',
    `update_by`   bigint(20) DEFAULT NULL COMMENT '更新人员',
    `status`      tinyint(2) DEFAULT '0' COMMENT '状态',
    `remark`      varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3  COMMENT='流程监听';


-- 流程相关菜单
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES('流程管理', 0, 6, 'flowable', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'cascader', 'tony', '2021-03-25 11:35:09', 'admin', '2022-12-29 17:39:22', '');
SELECT @flowable := LAST_INSERT_ID();
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程定义', @flowable, 2, 'definition', 'flowable/definition/index', NULL, NULL, 1, 0, 'C', '0', '0', '', 'job', 'tony', '2021-03-25 13:53:55', 'admin', '2022-12-29 17:40:39', '');

INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '任务管理', 0, 7, 'task', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'dict', 'tony', '2021-03-26 10:53:10', 'admin', '2021-03-29 09:37:40', '');
SELECT @task := LAST_INSERT_ID();
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '待办任务', @task, 2, 'todo', 'flowable/task/todo/index', NULL, NULL, 1, 1, 'C', '0', '0', '', 'cascader', 'admin', '2021-03-26 10:55:52', 'admin', '2021-03-30 09:26:36', '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '已办任务', @task, 3, 'finished', 'flowable/task/finished/index', NULL, NULL, 1, 1, 'C', '0', '0', '', 'time-range', 'admin', '2021-03-26 10:57:54', 'admin', '2021-03-30 09:26:50', '');


INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '已发任务', @task, 1, 'process', 'flowable/task/myProcess/index', NULL, NULL, 1, 1, 'C', '0', '0', '', 'guide', 'admin', '2021-03-30 09:26:23', 'admin', '2022-12-12 09:58:07', '');
SELECT @process := LAST_INSERT_ID();
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '新增', @process, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'system:deployment:add', '#', 'admin', '2021-07-07 14:25:22', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '编辑', @process, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'system:deployment:edit', '#', 'admin', '2021-07-07 14:25:47', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '删除', @process, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'system:deployment:remove', '#', 'admin', '2021-07-07 14:26:02', '', NULL, '');

INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程表达式', @flowable, 3, 'expression', 'flowable/expression/index', NULL, NULL, 1, 1, 'C', '0', '0', 'system:expression:list', 'list', 'admin', '2022-12-12 17:12:19', 'admin', '2022-12-12 17:13:44', '流程达式菜单');
SELECT @expression := LAST_INSERT_ID();
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程达式查询', @expression, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expression:query', '#', 'admin', '2022-12-12 17:12:19', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程达式新增', @expression, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expression:add', '#', 'admin', '2022-12-12 17:12:19', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程达式修改', @expression, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expression:edit', '#', 'admin', '2022-12-12 17:12:19', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程达式删除', @expression, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expression:remove', '#', 'admin', '2022-12-12 17:12:19', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程达式导出', @expression, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expression:export', '#', 'admin', '2022-12-12 17:12:19', '', NULL, '');


INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程监听', @flowable, 4, 'listener', 'flowable/listener/index', NULL, NULL, 1, 0, 'C', '0', '0', 'system:listener:list', 'monitor', 'admin', '2022-12-25 11:44:16', 'admin', '2022-12-29 08:59:21', '流程监听菜单');
SELECT @listener := LAST_INSERT_ID();
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程监听查询', @listener, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:listener:query', '#', 'admin', '2022-12-25 11:44:16', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程监听新增', @listener, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:listener:add', '#', 'admin', '2022-12-25 11:44:16', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程监听修改', @listener, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:listener:edit', '#', 'admin', '2022-12-25 11:44:16', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程监听删除', @listener, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:listener:remove', '#', 'admin', '2022-12-25 11:44:16', '', NULL, '');
INSERT INTO sys_menu ( menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES( '流程监听导出', @listener, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:listener:export', '#', 'admin', '2022-12-25 11:44:16', '', NULL, '');

-- 流程相关字段表信息

INSERT INTO sys_dict_type ( dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES( '表达式类型', 'exp_data_type', '0', 'admin', '2024-03-12 09:03:02', '', NULL, NULL);
INSERT INTO sys_dict_type ( dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES( '监听类型', 'sys_listener_type', '0', 'admin', '2022-12-18 22:03:07', '', NULL, NULL);
INSERT INTO sys_dict_type ( dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES( '监听值类型', 'sys_listener_value_type', '0', 'admin', '2022-12-18 22:03:39', '', NULL, NULL);
INSERT INTO sys_dict_type ( dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES( '监听属性', 'sys_listener_event_type', '0', 'admin', '2022-12-18 22:04:29', '', NULL, NULL);
INSERT INTO sys_dict_type ( dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES( '流程分类', 'sys_process_category', '0', 'admin', '2024-03-12 09:08:18', '', NULL, NULL);


INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 0, '系统指定', 'fixed', 'exp_data_type', NULL, 'default', 'N', '0', 'admin', '2024-03-12 09:04:46', '', NULL, NULL);
INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 0, '动态选择', 'dynamic', 'exp_data_type', NULL, 'default', 'N', '0', 'admin', '2024-03-12 09:05:02', '', NULL, NULL);
INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 0, '任务监听', '1', 'sys_listener_type', NULL, 'default', 'N', '0', 'admin', '2022-12-25 11:47:26', '', NULL, NULL);
INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 2, '执行监听', '2', 'sys_listener_type', NULL, 'default', 'N', '0', 'admin', '2022-12-25 11:47:37', '', NULL, NULL);
INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 0, 'JAVA类', 'classListener', 'sys_listener_value_type', NULL, 'default', 'N', '0', 'admin', '2022-12-25 11:48:55', 'admin', '2024-09-05 21:38:02', NULL);
INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 0, '表达式', 'expressionListener', 'sys_listener_value_type', NULL, 'default', 'N', '0', 'admin', '2022-12-25 11:49:05', 'admin', '2024-09-05 21:38:10', NULL);
INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 0, '代理表达式', 'delegateExpressionListener', 'sys_listener_value_type', NULL, 'default', 'N', '0', 'admin', '2022-12-25 11:49:16', 'admin', '2024-09-05 21:38:16', NULL);
INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 0, '请假', 'leave', 'sys_process_category', NULL, 'default', 'N', '0', 'admin', '2024-03-12 09:08:42', '', NULL, NULL);
INSERT INTO sys_dict_data ( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES( 0, '报销', 'expense', 'sys_process_category', NULL, 'default', 'N', '0', 'admin', '2024-03-12 09:09:02', '', NULL, NULL);
