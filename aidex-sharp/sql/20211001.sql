-- 修改ip字段长度防止ipv6地址长度不够
-- 步骤1： 打开information_schema数据库
-- 步骤2： 替换以下SQL中的TABLE_SCHEMA用户
-- 步骤3： 拷贝执行以下SQL结果中的执行SQL列，全部执行即可
SELECT
	COLUMN_NAME AS '字段名',
	CONCAT(
		'ALTER TABLE ',
		TABLE_NAME,
		' MODIFY COLUMN ',
		COLUMN_NAME,
		' varchar(128);'
	) AS '执行SQL',
	DATA_TYPE AS `数据类型`,
	CHARACTER_MAXIMUM_LENGTH AS `字符长度`,
	NUMERIC_PRECISION AS `数字长度`
FROM
	information_schema. COLUMNS
WHERE
	TABLE_SCHEMA = 'aidex_sharp'
    AND CHARACTER_MAXIMUM_LENGTH != 128
AND COLUMN_NAME IN (
	'ipaddr',
	'oper_ip',
	'login_ip',
	'update_ip'
)