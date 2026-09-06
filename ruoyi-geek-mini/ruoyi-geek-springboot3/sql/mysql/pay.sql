-- ----------------------------
-- 订单表
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`  (
  order_id         bigint          NOT NULL AUTO_INCREMENT    COMMENT '订单id',
  order_number     varchar(255)    NULL DEFAULT NULL          COMMENT '订单号',
  third_number     varchar(255)    NULL DEFAULT NULL          COMMENT '第三方订单号',
  order_status     varchar(255)    NULL DEFAULT NULL          COMMENT '订单状态',
  total_amount     varchar(255)    NULL DEFAULT NULL          COMMENT '订单总金额',
  actual_amount    varchar(255)    NULL DEFAULT NULL          COMMENT '实际支付金额',
  order_content    varchar(255)    NULL DEFAULT NULL          COMMENT '订单内容',
  order_message    varchar(255)    NULL DEFAULT NULL          COMMENT '负载信息',
  pay_type         varchar(255)    NULL DEFAULT NULL          COMMENT '支付方式',
  pay_time         datetime        DEFAULT NULL               COMMENT '支付时间',
  pay_by           varchar(64)     DEFAULT ''                 COMMENT '支付人',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  PRIMARY KEY (`order_id`)
) ENGINE = InnoDB  COMMENT = '订单';

-- ----------------------------
-- 发票表
-- ----------------------------
DROP TABLE IF EXISTS `pay_invoice`;
CREATE TABLE `pay_invoice`  (
  invoice_id       bigint          NOT NULL AUTO_INCREMENT    COMMENT '发票id',
  order_number     varchar(255)    NULL DEFAULT NULL          COMMENT '订单号',
  invoice_type     varchar(255)    NULL DEFAULT NULL          COMMENT '发票类型',
  invoice_header   varchar(255)    NULL DEFAULT NULL          COMMENT '发票抬头',
  invoice_number   varchar(255)    NULL DEFAULT NULL          COMMENT '纳税人识别号',
  invoice_phone    varchar(255)    NULL DEFAULT NULL          COMMENT '收票人手机号',
  invoice_email    varchar(255)    NULL DEFAULT NULL          COMMENT '收票人邮箱',
  invoice_remark   varchar(255)    NULL DEFAULT NULL          COMMENT '发票备注',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  PRIMARY KEY (`invoice_id`)
) ENGINE = InnoDB  COMMENT = '发票';

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query,route_name, is_frame, is_cache, menu_type, visible, `status`, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ('支付管理', 0, 4, 'pay', NULL, NULL, '',1, 0, 'M', '0', '0', NULL, 'money', 'admin', '2024-02-15 22:40:23', '', NULL, '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

select @payParentId := @parentId;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单', @payParentId, '1', 'order', 'pay/order/index', '',1, 0, 'C', '0', '0', 'pay:order:list', '#', 'admin', sysdate(), '', null, '订单菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单查询', @parentId, '1',  '#', '', '',1, 0, 'F', '0', '0', 'pay:order:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单新增', @parentId, '2',  '#', '', '',1, 0, 'F', '0', '0', 'pay:order:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单修改', @parentId, '3',  '#', '', '',1, 0, 'F', '0', '0', 'pay:order:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单删除', @parentId, '4',  '#', '', '',1, 0, 'F', '0', '0', 'pay:order:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单导出', @parentId, '5',  '#', '', '',1, 0, 'F', '0', '0', 'pay:order:export',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('发票', @payParentId, '1', 'invoice', 'pay/invoice/index', '',1, 0, 'C', '0', '0', 'pay:invoice:list', '#', 'admin', sysdate(), '', null, '发票菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('发票查询', @parentId, '1',  '#', '', '',1, 0, 'F', '0', '0', 'pay:invoice:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('发票新增', @parentId, '2',  '#', '', '',1, 0, 'F', '0', '0', 'pay:invoice:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('发票修改', @parentId, '3',  '#', '', '',1, 0, 'F', '0', '0', 'pay:invoice:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('发票删除', @parentId, '4',  '#', '', '',1, 0, 'F', '0', '0', 'pay:invoice:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('发票导出', @parentId, '5',  '#', '', '',1, 0, 'F', '0', '0', 'pay:invoice:export',       '#', 'admin', sysdate(), '', null, '');