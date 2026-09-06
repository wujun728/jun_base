-- 增加验证码开关增量脚本
INSERT INTO `sys_config` VALUES ('验证码开关', 'sys.captcha.onOff', 'true', 'Y', '1', '2021-09-29 23:51:21', '1', '2021-09-29 23:51:21', '是否开启验证码功能（true开启，false关闭）', 'b3158f15cbed4a3d93e43f29d5fb5893', '1', '0', '127.0.0.1', '100');
-- 删除无用配置参数
DELETE FROM sys_config where config_key = 'system.upload.save.type'

-- 更新日志表字段类型，用于表头排序功能
ALTER TABLE `sys_oper_log`
MODIFY COLUMN `take_up_time`  int(11) NULL DEFAULT NULL AFTER `form_id`;

-- 增加是否允许用户注册开关
INSERT INTO `sys_config` VALUES ('用户是否可注册开关', 'sys.account.registerUser', 'true', 'Y', '1', '2021-09-29 23:51:21', '1', '2021-09-29 23:51:21', '是否开启验证码功能（true开启，false关闭）', 'b3158f15cbed4a3d93e43f19d5fb5893', '1', '0', '127.0.0.1', '100');

-- 调整列顺序
ALTER TABLE `sys_dict_data`
MODIFY COLUMN `id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位ID' FIRST ,
MODIFY COLUMN `dict_type`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型' AFTER `id`,
MODIFY COLUMN `dict_value`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典键值' AFTER `dict_code`,
MODIFY COLUMN `dict_label`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典标签' AFTER `dict_value`;

-- 数据字典增加选项：日志操作类型增加选型
INSERT INTO `sys_dict_data` VALUES ('ab9bbed6-9395-420f-9fa2-c9be42550017', 'sys_oper_type', '28', '11', '校验', '110', null, null, null, '0', '1', '2021-10-04 22:40:50', '1', '2021-10-04 22:50:02', '校验', '2', '0', '127.0.0.1', '100');
INSERT INTO `sys_dict_data` VALUES ('205c0b4c-e4d2-44b1-b70b-0d231b07012d', 'sys_oper_type', '27', '10', '查询', '100', null, null, null, '0', '1', '2021-10-04 22:37:38', '1', '2021-10-04 22:49:32', '查询', '3', '0', '127.0.0.1', '100');

-- 处理岗位表中的数据，版本为空的问题
update sys_post set version = '1' where version is null;

-- 处理数据字典表中的数据，版本为空的问题
update sys_dict_data set version = 1 where version is null;

-- 处理数据字典主表表中的数据，版本为空的问题
update sys_dict_type set version = '1' where version is null;

-- 删除文档关系表，暂不需要
DROP TABLE IF EXISTS `sys_secret_relation`;

-- 删除文档关系数据字典信息
delete from sys_dict_type where dict_type = 'sys_user_secret_level';

-- 删除文档关系数据字典子表信息
delete from sys_dict_data where dict_type = 'sys_user_secret_level';

-- 删除用户表中的密级字段
ALTER TABLE `sys_user` DROP COLUMN `secret_level`;

-- 删除短信息表
DROP TABLE IF EXISTS `sys_sms`;

-- 删除表更日志表
DROP TABLE IF EXISTS `sys_change_log`;

-- 删除表更日志表
DROP TABLE IF EXISTS `sys_log`;

-- 增加表备注
ALTER TABLE `sys_menu` COMMENT='菜单信息表';

-- 增加表备注
ALTER TABLE `sys_theme_config` COMMENT='用户主题信息记录表';

-- 增加表备注
ALTER TABLE `sys_portal_config` COMMENT='工作台配置表';


ALTER TABLE `gen_table` MODIFY COLUMN `options`  varchar(4000)  DEFAULT NULL COMMENT '扩展选项';


ALTER TABLE `sys_dept`
MODIFY COLUMN `dept_pinyin`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门拼音' AFTER `zip_code`;

ALTER TABLE `sys_menu`
MODIFY COLUMN `id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID' FIRST ;

ALTER TABLE `sys_oper_log`
MODIFY COLUMN `id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位ID' FIRST ,
MODIFY COLUMN `json_result`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '返回结果集' AFTER `oper_param`,
MODIFY COLUMN `log_content`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志变更内容' AFTER `json_result`,
MODIFY COLUMN `form_id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单数据ID' AFTER `del_flag`,
MODIFY COLUMN `take_up_time`  int(11) NULL DEFAULT NULL COMMENT '耗时' AFTER `form_id`;

-- 删除无用数据字典
delete from sys_dict_type where dict_type = 'sys_batch_type';

-- 删除无用数据字典
delete from sys_dict_data where dict_type = 'sys_batch_type';

-- 删除无用数据字典
delete from sys_dict_type where dict_type = 'sys_upload_file_status';

-- 删除无用数据字典
delete from sys_dict_data where dict_type = 'sys_upload_file_status';

-- 删除无用数据字典
delete from sys_dict_type where dict_type = 'sys_download_file_status';

-- 删除无用数据字典
delete from sys_dict_data where dict_type = 'sys_download_file_status';

-- 删除无用数据字典
delete from sys_dict_type where dict_type = 'sys_data_rule_type';

-- 删除无用数据字典
delete from sys_dict_data where dict_type = 'sys_data_rule_type';

-- 删除无用数据字典
delete from sys_dict_type where dict_type = 'sys_logical_symbol';

-- 删除无用数据字典
delete from sys_dict_data where dict_type = 'sys_logical_symbol';

-- 修改部门表ID字段长度
ALTER TABLE `sys_dept`
MODIFY COLUMN `id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门id' FIRST ;

