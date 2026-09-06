DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE sys_file_info (
    file_id      BIGINT      NOT NULL AUTO_INCREMENT COMMENT '文件主键',
    file_name    VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_path    VARCHAR(500) NOT NULL COMMENT '统一逻辑路径（/开头）',
    storage_type VARCHAR(32)  NOT NULL COMMENT '存储类型（local/minio/oss）',
    file_type    VARCHAR(50)  COMMENT '文件类型/后缀',
    file_size    BIGINT       COMMENT '文件大小（字节）',
    md5          VARCHAR(64)  COMMENT '文件MD5',
    create_by    VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time  DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time  DATETIME     DEFAULT NULL COMMENT '更新时间',
    remark       VARCHAR(255) DEFAULT NULL COMMENT '备注',
    del_flag     CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    PRIMARY KEY (file_id),
    -- UNIQUE KEY uk_file_path (file_path),
    UNIQUE KEY uk_md5 (md5)
) ENGINE=InnoDB COMMENT='文件信息表';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件管理', '1', '1', 'file', 'system/file/index', 1, 0, 'C', '0', '0', 'system:file:list', 'excel', 'admin', sysdate(), '', null, '文件管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:file:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:file:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:file:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:file:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:file:export',       '#', 'admin', sysdate(), '', null, '');