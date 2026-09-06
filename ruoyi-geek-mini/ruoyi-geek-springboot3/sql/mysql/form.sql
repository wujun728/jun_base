DROP TABLE IF EXISTS `form_template`;

CREATE TABLE form_template (
    form_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '表单ID',
    form_name VARCHAR(100) NOT NULL COMMENT '表单名称',
    form_schema JSON  COMMENT '表单JSON Schema（vForm配置）',
    form_version VARCHAR(10)  DEFAULT '1.0.0' COMMENT '表单版本（语义化版本）',
    form_status VARCHAR(2)  DEFAULT '0' COMMENT '发布状态（0: 草稿, 1: 已发布, 2: 已停用）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE = InnoDB COMMENT '表单模板表';

DROP TABLE IF EXISTS `form_data`;

CREATE TABLE form_data (
    data_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID',
    form_id BIGINT NOT NULL COMMENT '关联的表单ID',
    form_version VARCHAR(10) COMMENT '表单版本（与模板表版本一致）',
    data_content JSON NOT NULL COMMENT '表单数据内容（JSON格式）',
    status VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '数据状态（draft, submitted, approved, rejected）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE = InnoDB COMMENT '表单数据表';

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query,route_name, is_frame, is_cache, menu_type, visible, `status`, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES ('表单管理', 0, 4, 'formManagement', NULL, NULL, '',1, 0, 'M', '0', '0', NULL, 'form', 'admin', '2024-02-15 22:40:23', '', NULL, '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

select @fileParentId := @parentId;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单模板', @fileParentId, '1', 'formtemplate', 'form/template/index', 1, 0, 'C', '0', '0', 'form:template:list', '#', 'admin', sysdate(), '', null, '表单模板菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单模板查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'form:template:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单模板新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'form:template:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单模板修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'form:template:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单模板删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'form:template:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单模板导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'form:template:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单数据', @fileParentId, '1', 'formdata', 'form/data/index', 1, 0, 'C', '0', '0', 'form:data:list', '#', 'admin', sysdate(), '', null, '表单数据菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'form:data:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'form:data:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'form:data:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'form:data:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('表单数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'form:data:export',       '#', 'admin', sysdate(), '', null, '');