-- 增加验证码开关增量脚本
INSERT INTO `sys_config` VALUES ('验证码开关', 'sys.captcha.onOff', 'true', 'Y', '1', '2021-09-29 23:51:21', '1', '2021-09-29 23:51:21', '是否开启验证码功能（true开启，false关闭）', 'b3158f15cbed4a3d93e43f29d5fb5893', '1', '0', '127.0.0.1', '100');
-- 删除无用配置参数
DELETE FROM sys_config where config_key = 'system.upload.save.type'
