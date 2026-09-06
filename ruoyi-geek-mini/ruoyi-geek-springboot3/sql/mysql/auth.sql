DROP TABLE IF EXISTS oauth_user;
CREATE TABLE oauth_user (
    id INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    uuid VARCHAR(255) NOT NULL COMMENT '第三方系统的唯一ID，详细解释请参考：名词解释',
    user_id bigint(20)  NOT NULL comment '用户ID',
    source VARCHAR(255) NOT NULL COMMENT '第三方用户来源，可选值：GITHUB、GITEE、QQ，更多请参考：AuthDefaultSource.java(opens new window)',
    access_token VARCHAR(255) NOT NULL COMMENT '用户的授权令牌',
    expire_in INT COMMENT '第三方用户的授权令牌的有效期，部分平台可能没有',
    refresh_token VARCHAR(255) COMMENT '刷新令牌，部分平台可能没有',
    open_id VARCHAR(255) COMMENT '第三方用户的 open id，部分平台可能没有',
    uid VARCHAR(255) COMMENT '第三方用户的 ID，部分平台可能没有',
    access_code VARCHAR(255) COMMENT '个别平台的授权信息，部分平台可能没有',
    union_id VARCHAR(255) COMMENT '第三方用户的 union id，部分平台可能没有',
    scope VARCHAR(255) COMMENT '第三方用户授予的权限，部分平台可能没有',
    token_type VARCHAR(255) COMMENT '个别平台的授权信息，部分平台可能没有',
    id_token VARCHAR(255) COMMENT 'id token，部分平台可能没有',
    mac_algorithm VARCHAR(255) COMMENT '小米平台用户的附带属性，部分平台可能没有',
    mac_key VARCHAR(255) COMMENT '小米平台用户的附带属性，部分平台可能没有',
    code VARCHAR(255) COMMENT '用户的授权code，部分平台可能没有',
    oauth_token VARCHAR(255) COMMENT 'Twitter平台用户的附带属性，部分平台可能没有',
    oauth_token_secret VARCHAR(255) COMMENT 'Twitter平台用户的附带属性，部分平台可能没有',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB  COMMENT = '第三方登录';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('第三方认证', '1', '1', 'oauth', 'system/oauth/index',"", 1, 0, 'C', '0', '0', 'system:oauth:list', 'checkbox', 'admin', sysdate(), '', null, '第三方认证菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('第三方认证查询', @parentId, '1',  '#', '','', 1, 0, 'F', '0', '0', 'system:oauth:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('第三方认证新增', @parentId, '2',  '#', '','', 1, 0, 'F', '0', '0', 'system:oauth:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('第三方认证修改', @parentId, '3',  '#', '','', 1, 0, 'F', '0', '0', 'system:oauth:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('第三方认证删除', @parentId, '4',  '#', '','', 1, 0, 'F', '0', '0', 'system:oauth:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, route_name,is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('第三方认证导出', @parentId, '5',  '#', '','', 1, 0, 'F', '0', '0', 'system:oauth:export',       '#', 'admin', sysdate(), '', null, '');