ALTER TABLE gen_table ALTER COLUMN table_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS gen_table_table_id_seq CASCADE;
CREATE SEQUENCE gen_table_table_id_seq;
ALTER TABLE gen_table ALTER COLUMN table_id SET DEFAULT nextval('gen_table_table_id_seq');
SELECT setval('gen_table_table_id_seq', (SELECT MAX(table_id) FROM gen_table));

ALTER TABLE gen_table_column ALTER COLUMN column_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS gen_table_column_column_id_seq CASCADE;
CREATE SEQUENCE gen_table_column_column_id_seq;
ALTER TABLE gen_table_column ALTER COLUMN column_id SET DEFAULT nextval('gen_table_column_column_id_seq');
SELECT setval('gen_table_column_column_id_seq', (SELECT MAX(column_id) FROM gen_table_column));

ALTER TABLE sys_config ALTER COLUMN config_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_config_config_id_seq CASCADE;
CREATE SEQUENCE sys_config_config_id_seq;
ALTER TABLE sys_config ALTER COLUMN config_id SET DEFAULT nextval('sys_config_config_id_seq');
SELECT setval('sys_config_config_id_seq', (SELECT MAX(config_id) FROM sys_config));

ALTER TABLE sys_dept ALTER COLUMN dept_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_dept_dept_id_seq CASCADE;
CREATE SEQUENCE sys_dept_dept_id_seq;
ALTER TABLE sys_dept ALTER COLUMN dept_id SET DEFAULT nextval('sys_dept_dept_id_seq');
SELECT setval('sys_dept_dept_id_seq', (SELECT MAX(dept_id) FROM sys_dept));

ALTER TABLE sys_dict_data ALTER COLUMN dict_code DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_dict_data_dict_code_seq CASCADE;
CREATE SEQUENCE sys_dict_data_dict_code_seq;
ALTER TABLE sys_dict_data ALTER COLUMN dict_code SET DEFAULT nextval('sys_dict_data_dict_code_seq');
SELECT setval('sys_dict_data_dict_code_seq', (SELECT MAX(dict_code) FROM sys_dict_data));

ALTER TABLE sys_dict_type ALTER COLUMN dict_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_dict_type_dict_id_seq CASCADE;
CREATE SEQUENCE sys_dict_type_dict_id_seq;
ALTER TABLE sys_dict_type ALTER COLUMN dict_id SET DEFAULT nextval('sys_dict_type_dict_id_seq');
SELECT setval('sys_dict_type_dict_id_seq', (SELECT MAX(dict_id) FROM sys_dict_type));

ALTER TABLE sys_job ALTER COLUMN job_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_job_job_id_seq CASCADE;
CREATE SEQUENCE sys_job_job_id_seq;
ALTER TABLE sys_job ALTER COLUMN job_id SET DEFAULT nextval('sys_job_job_id_seq');
SELECT setval('sys_job_job_id_seq', (SELECT MAX(job_id) FROM sys_job));

ALTER TABLE sys_job_log ALTER COLUMN job_log_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_job_log_job_log_id_seq CASCADE;
CREATE SEQUENCE sys_job_log_job_log_id_seq;
ALTER TABLE sys_job_log ALTER COLUMN job_log_id SET DEFAULT nextval('sys_job_log_job_log_id_seq');
SELECT setval('sys_job_log_job_log_id_seq', (SELECT MAX(job_log_id) FROM sys_job_log));

ALTER TABLE sys_logininfor ALTER COLUMN info_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_logininfor_info_id_seq CASCADE;
CREATE SEQUENCE sys_logininfor_info_id_seq;
ALTER TABLE sys_logininfor ALTER COLUMN info_id SET DEFAULT nextval('sys_logininfor_info_id_seq');
SELECT setval('sys_logininfor_info_id_seq', (SELECT MAX(info_id) FROM sys_logininfor));

ALTER TABLE sys_menu ALTER COLUMN menu_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_menu_menu_id_seq CASCADE;
CREATE SEQUENCE sys_menu_menu_id_seq;
ALTER TABLE sys_menu ALTER COLUMN menu_id SET DEFAULT nextval('sys_menu_menu_id_seq');
SELECT setval('sys_menu_menu_id_seq', (SELECT MAX(menu_id) FROM sys_menu));

ALTER TABLE sys_notice ALTER COLUMN notice_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_notice_notice_id_seq CASCADE;
CREATE SEQUENCE sys_notice_notice_id_seq;
ALTER TABLE sys_notice ALTER COLUMN notice_id SET DEFAULT nextval('sys_notice_notice_id_seq');
SELECT setval('sys_notice_notice_id_seq', (SELECT MAX(notice_id) FROM sys_notice));

ALTER TABLE sys_oper_log ALTER COLUMN oper_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_oper_log_oper_id_seq CASCADE;
CREATE SEQUENCE sys_oper_log_oper_id_seq;
ALTER TABLE sys_oper_log ALTER COLUMN oper_id SET DEFAULT nextval('sys_oper_log_oper_id_seq');
SELECT setval('sys_oper_log_oper_id_seq', (SELECT MAX(oper_id) FROM sys_oper_log));

ALTER TABLE sys_post ALTER COLUMN post_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_post_post_id_seq CASCADE;
CREATE SEQUENCE sys_post_post_id_seq;
ALTER TABLE sys_post ALTER COLUMN post_id SET DEFAULT nextval('sys_post_post_id_seq');
SELECT setval('sys_post_post_id_seq', (SELECT MAX(post_id) FROM sys_post));

ALTER TABLE sys_role ALTER COLUMN role_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_role_role_id_seq CASCADE;
CREATE SEQUENCE sys_role_role_id_seq;
ALTER TABLE sys_role ALTER COLUMN role_id SET DEFAULT nextval('sys_role_role_id_seq');
SELECT setval('sys_role_role_id_seq', (SELECT MAX(role_id) FROM sys_role));

ALTER TABLE sys_user ALTER COLUMN user_id DROP DEFAULT;  
DROP SEQUENCE IF EXISTS sys_user_user_id_seq CASCADE;
CREATE SEQUENCE sys_user_user_id_seq;
ALTER TABLE sys_user ALTER COLUMN user_id SET DEFAULT nextval('sys_user_user_id_seq');
SELECT setval('sys_user_user_id_seq', (SELECT MAX(user_id) FROM sys_user));
  