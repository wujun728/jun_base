
-- ----------------------------
-- 18、代码生成业务表
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id`            bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name`          varchar(200)    NOT NULL  COMMENT '表名称',
  `table_alias`          varchar(200)    NOT NULL  COMMENT '表别名',
  `table_comment`       varchar(500)    DEFAULT ''  COMMENT '表描述',
  `have_sub_column`     char(1)         DEFAULT '0' COMMENT '是否含有关联字段',
  `sub_table_name`      varchar(64)     DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name`   varchar(64)     DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name`          varchar(100)    DEFAULT '' COMMENT '实体类名称',
  `tpl_category`        varchar(200)    DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type`        varchar(200)    DEFAULT 'element-plus' COMMENT '使用的模板类型',
  `package_name`        varchar(100)    DEFAULT NULL COMMENT '生成包路径',
  `module_name`         varchar(30)     DEFAULT NULL COMMENT '生成模块名',
  `business_name`       varchar(30)     DEFAULT NULL COMMENT '生成业务名',
  `function_name`       varchar(50)     DEFAULT NULL COMMENT '生成功能名',
  `function_author`     varchar(50)     DEFAULT NULL COMMENT '生成功能作者',
  `gen_type`            char(1)         DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path`            varchar(200)    DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options`             varchar(1000)   DEFAULT NULL COMMENT '其它生成选项',
  `create_by`           varchar(64)     DEFAULT '' COMMENT '创建者',
  `create_time`         datetime        DEFAULT NULL COMMENT '创建时间',
  `update_by`           varchar(64)     DEFAULT '' COMMENT '更新者',
  `update_time`         datetime        DEFAULT NULL COMMENT '更新时间',
  `remark`              varchar(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '代码生成业务表';


-- ----------------------------
-- 19、代码生成业务表字段
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id`       bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id`        varchar(64)     DEFAULT NULL COMMENT '归属表编号',
  `column_name`     varchar(200)    DEFAULT NULL COMMENT '列名称',
  `column_comment`  varchar(500)    DEFAULT NULL COMMENT '列描述',
  `column_type`     varchar(100)    DEFAULT NULL COMMENT '列类型',
  `java_type`       varchar(500)    DEFAULT NULL COMMENT 'JAVA类型',
  `java_field`      varchar(200)    DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk`           char(1)         DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment`    char(1)         DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required`     char(1)         DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert`       char(1)         DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit`         char(1)         DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list`         char(1)         DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query`        char(1)         DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type`      varchar(200)    DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type`       varchar(200)    DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type`       varchar(200)    DEFAULT '' COMMENT '字典类型',
  `sort`            int             DEFAULT NULL COMMENT '排序',
  `sub_column_table_name`   varchar(200)    DEFAULT NULL COMMENT '关联表名称',
  `sub_column_fk_name`      varchar(200)    DEFAULT NULL COMMENT '关联字段名称',
  `sub_column_name`         varchar(200)    DEFAULT NULL COMMENT '映射字段名称',
  `sub_column_java_field`   varchar(200)    DEFAULT NULL COMMENT '映射字段JAVA字段名',
  `sub_column_java_type`    varchar(255)    DEFAULT NULL COMMENT '映射字段JAVA类型',
  `create_by`       varchar(64)     DEFAULT '' COMMENT '创建者',
  `create_time`     datetime        DEFAULT NULL COMMENT '创建时间',
  `update_by`       varchar(64)     DEFAULT '' COMMENT '更新者',
  `update_time`     datetime        DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '代码生成业务表字段';



DROP TABLE IF EXISTS `gen_join_table`;
CREATE TABLE `gen_join_table` (
  `table_id`          bigint       NOT NULL   COMMENT '表编号',
  `left_table_id`     bigint       NOT NULL   COMMENT '左表名称',
  `right_table_id`    bigint       NOT NULL   COMMENT '右表编号',
  `left_table_alias`  varchar(200) NOT NULL   COMMENT '左表别名',
  `right_table_alias` varchar(200) NOT NULL   COMMENT '右表别名',
  `left_table_fk`     varchar(200) NOT NULL   COMMENT '左表关联键',
  `right_table_fk`    varchar(200) NOT NULL   COMMENT '右表关联键',
  `join_type`         varchar(200) NOT NULL   COMMENT '关联类型',
  `join_columns`      varchar(500) DEFAULT '' COMMENT '关联字段',
  `order_num`         varchar(64)  NOT NULL   COMMENT '序号',
  `new_table_id`      bigint       NOT NULL   COMMENT '新表编号',
  PRIMARY KEY (`table_id`,`right_table_id`,`left_table_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '代码生成关联表';


insert into sys_menu values('116',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           '', '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          'admin', sysdate(), '', null, '代码生成菜单');
-- 代码生成按钮
insert into sys_menu values('1055', '生成查询', '116', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1056', '生成修改', '116', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1057', '生成删除', '116', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1058', '导入代码', '116', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1059', '预览代码', '116', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1060', '生成代码', '116', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', 'admin', sysdate(), '', null, '');
