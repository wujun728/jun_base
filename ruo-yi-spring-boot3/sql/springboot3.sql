/*
SQLyog Ultimate v12.14 (64 bit)
MySQL - 8.0.13 : Database - ry-vue
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ry-vue` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `ry-vue`;

/*Table structure for table `chat_model_list` */

DROP TABLE IF EXISTS `chat_model_list`;

CREATE TABLE `chat_model_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天模型列表ID',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置URL',
  `api_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'apiKey',
  `model_version` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模型版本',
  `model_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模型名称',
  `num_gpu` int(5) DEFAULT NULL COMMENT 'GPU 数量',
  `temperature` float(10,2) DEFAULT NULL COMMENT '随机性',
  `top_k` int(50) DEFAULT NULL COMMENT '限制模型单词',
  `top_p` float(10,2) DEFAULT NULL COMMENT '核采样',
  `repeat_penalty` float(10,2) DEFAULT NULL COMMENT '防止重复',
  `presence_penalty` float(10,2) DEFAULT NULL COMMENT '惩罚已出现的词',
  `frequency_penalty` float(10,2) DEFAULT NULL COMMENT '惩罚频繁出现的词',
  `num_predict` int(5) DEFAULT NULL COMMENT '最大token 数',
  `num_thread` int(5) DEFAULT NULL COMMENT 'CPU线程数',
  `low_vram` tinyint(1) DEFAULT NULL COMMENT '是否启用低显存模式',
  `is_chose` tinyint(1) DEFAULT NULL COMMENT '是否选择',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `chat_model_list` */

insert  into `chat_model_list`(`id`,`base_url`,`api_key`,`model_version`,`model_name`,`num_gpu`,`temperature`,`top_k`,`top_p`,`repeat_penalty`,`presence_penalty`,`frequency_penalty`,`num_predict`,`num_thread`,`low_vram`,`is_chose`) values

(1,'http://127.0.0.1:11434','sk-86e64018e9734f5f9f49dd5fed341e26','deepseek-r1:8b','deepseek',1,0.74,0,0.02,0.00,0.02,0.02,98,2,1,1);

/*Table structure for table `gen_table` */

DROP TABLE IF EXISTS `gen_table`;

CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) COLLATE utf8mb4_general_ci DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) COLLATE utf8mb4_general_ci DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成业务表';

/*Data for the table `gen_table` */

insert  into `gen_table`(`table_id`,`table_name`,`table_comment`,`sub_table_name`,`sub_table_fk_name`,`class_name`,`tpl_category`,`tpl_web_type`,`package_name`,`module_name`,`business_name`,`function_name`,`function_author`,`gen_type`,`gen_path`,`options`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(2,'chat_model_list','模型列表',NULL,NULL,'ChatModelList','crud','element-ui','com.ruoyi.ai','ai','modelList','模型列表','欧阳日峰','0','/','{\"parentMenuId\":2000}','admin','2025-02-06 19:47:50','','2025-02-10 20:57:33','模型列表'),

(3,'vector_database_list','向量库列表',NULL,NULL,'VectorDatabaseList','crud','element-ui','com.ruoyi.ai','ai','databaseList','向量库列表','欧阳日峰','0','/','{\"parentMenuId\":2000}','admin','2025-02-28 14:21:06','','2025-02-28 14:25:31',NULL);

/*Table structure for table `gen_table_column` */

DROP TABLE IF EXISTS `gen_table_column`;

CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint(20) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) COLLATE utf8mb4_general_ci DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成业务表字段';

/*Data for the table `gen_table_column` */

insert  into `gen_table_column`(`column_id`,`table_id`,`column_name`,`column_comment`,`column_type`,`java_type`,`java_field`,`is_pk`,`is_increment`,`is_required`,`is_insert`,`is_edit`,`is_list`,`is_query`,`query_type`,`html_type`,`dict_type`,`sort`,`create_by`,`create_time`,`update_by`,`update_time`) values

(15,2,'id','聊天模型列表ID','bigint(20)','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(16,2,'model_version','模型版本','varchar(255)','String','modelVersion','0','0','0','1','1','1','1','EQ','input','',4,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(17,2,'model_name','模型名称','varchar(255)','String','modelName','0','0','0','1','1','1','1','LIKE','input','',5,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(18,2,'num_gpu','GPU 数量','int(5)','Integer','numGpu','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(19,2,'temperature','随机性','float(10,2)','BigDecimal','temperature','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(20,2,'top_k','限制模型单词','int(50)','Long','topK','0','0','0','1','1','1','1','EQ','input','',8,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(21,2,'top_p','核采样','int(50)','BigDecimal','topP','0','0','0','1','1','1','1','EQ','input','',9,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(22,2,'repeat_penalty','防止重复','float(10,2)','BigDecimal','repeatPenalty','0','0','0','1','1','1','1','EQ','input','',10,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(23,2,'presence_penalty','惩罚已出现的词','float(10,2)','BigDecimal','presencePenalty','0','0','0','1','1','1','1','EQ','input','',11,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(24,2,'frequency_penalty','惩罚频繁出现的词','float(10,2)','BigDecimal','frequencyPenalty','0','0','0','1','1','1','1','EQ','input','',12,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(25,2,'num_predict','最大token 数','int(5)','Integer','numPredict','0','0','0','1','1','1','1','EQ','input','',13,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(26,2,'num_thread','CPU线程数','int(5)','Integer','numThread','0','0','0','1','1','1','1','EQ','input','',14,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(27,2,'low_vram','是否启用低显存模式','tinyint(1)','Integer','lowVram','0','0','0','1','1','1','1','EQ','input','',15,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(28,2,'is_chose','是否选择','tinyint(1)','Integer','isChose','0','0','0','1','1','1','1','EQ','input','',16,'admin','2025-02-06 19:47:50','','2025-02-24 13:52:57'),

(29,2,'base_url','配置URL','varchar(255)','String','baseUrl','0','0','0','1','1','1','1','EQ','input','',2,'','2025-02-10 20:49:52','','2025-02-24 13:52:57'),

(30,2,'api_key','apiKey','varchar(255)','String','apiKey','0','0','0','1','1','1','1','EQ','input','',3,'','2025-02-24 13:52:57','',NULL),

(31,3,'id','向量数据库列表ID','bigint(20)','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(32,3,'alias_name','数据库别名','varchar(255)','String','aliasName','0','0','0','1','1','1','1','LIKE','input','',2,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(33,3,'host','数据库地址','varchar(255)','String','host','0','0','0','1','1','1','0','EQ','input','',3,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(34,3,'port','数据库端口','int(10)','Integer','port','0','0','0','1','1','1','0','EQ','input','',4,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(35,3,'database','数据库名','varchar(255)','String','database','0','0','0','1','1','1','0','EQ','input','',5,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(36,3,'table','数据库表名称','varchar(255)','String','table','0','0','0','1','1','1','0','EQ','input','',6,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(37,3,'user','数据库用户名','varchar(255)','String','user','0','0','0','1','1','1','0','EQ','input','',7,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(38,3,'password','数据库密码','varchar(255)','String','password','0','0','0','1','1','1','0','EQ','input','',8,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(39,3,'dimension','向量纬度','int(10)','Integer','dimension','0','0','0','1','1','1','0','EQ','input','',9,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31'),

(40,3,'is_chose','是否选择','tinyint(1)','Integer','isChose','0','0','0','1','1','1','0','EQ','input','',10,'admin','2025-02-28 14:21:06','','2025-02-28 14:25:31');

/*Table structure for table `qrtz_blob_triggers` */

DROP TABLE IF EXISTS `qrtz_blob_triggers`;

CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Blob类型的触发器表';

/*Data for the table `qrtz_blob_triggers` */

/*Table structure for table `qrtz_calendars` */

DROP TABLE IF EXISTS `qrtz_calendars`;

CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日历信息表';

/*Data for the table `qrtz_calendars` */

/*Table structure for table `qrtz_cron_triggers` */

DROP TABLE IF EXISTS `qrtz_cron_triggers`;

CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Cron类型的触发器表';

/*Data for the table `qrtz_cron_triggers` */

/*Table structure for table `qrtz_fired_triggers` */

DROP TABLE IF EXISTS `qrtz_fired_triggers`;

CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(13) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(13) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='已触发的触发器表';

/*Data for the table `qrtz_fired_triggers` */

/*Table structure for table `qrtz_job_details` */

DROP TABLE IF EXISTS `qrtz_job_details`;

CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务详细信息表';

/*Data for the table `qrtz_job_details` */

/*Table structure for table `qrtz_locks` */

DROP TABLE IF EXISTS `qrtz_locks`;

CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储的悲观锁信息表';

/*Data for the table `qrtz_locks` */

/*Table structure for table `qrtz_paused_trigger_grps` */

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;

CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='暂停的触发器表';

/*Data for the table `qrtz_paused_trigger_grps` */

/*Table structure for table `qrtz_scheduler_state` */

DROP TABLE IF EXISTS `qrtz_scheduler_state`;

CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(13) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(13) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='调度器状态表';

/*Data for the table `qrtz_scheduler_state` */

/*Table structure for table `qrtz_simple_triggers` */

DROP TABLE IF EXISTS `qrtz_simple_triggers`;

CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(7) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(12) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(10) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='简单触发器的信息表';

/*Data for the table `qrtz_simple_triggers` */

/*Table structure for table `qrtz_simprop_triggers` */

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;

CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='同步机制的行锁表';

/*Data for the table `qrtz_simprop_triggers` */

/*Table structure for table `qrtz_triggers` */

DROP TABLE IF EXISTS `qrtz_triggers`;

CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(13) DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(13) DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(13) NOT NULL COMMENT '开始时间',
  `end_time` bigint(13) DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(2) DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='触发器详细信息表';

/*Data for the table `qrtz_triggers` */

/*Table structure for table `sys_config` */

DROP TABLE IF EXISTS `sys_config`;

CREATE TABLE `sys_config` (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) COLLATE utf8mb4_general_ci DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='参数配置表';

/*Data for the table `sys_config` */

insert  into `sys_config`(`config_id`,`config_name`,`config_key`,`config_value`,`config_type`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2025-01-31 23:51:59','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),

(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2025-01-31 23:51:59','',NULL,'初始化密码 123456'),

(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2025-01-31 23:51:59','',NULL,'深色主题theme-dark，浅色主题theme-light'),

(4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2025-01-31 23:51:59','',NULL,'是否开启验证码功能（true开启，false关闭）'),

(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2025-01-31 23:51:59','',NULL,'是否开启注册用户功能（true开启，false关闭）'),

(6,'用户登录-黑名单列表','sys.login.blackIPList','','Y','admin','2025-01-31 23:51:59','',NULL,'设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

/*Table structure for table `sys_dept` */

DROP TABLE IF EXISTS `sys_dept`;

CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门表';

/*Data for the table `sys_dept` */

insert  into `sys_dept`(`dept_id`,`parent_id`,`ancestors`,`dept_name`,`order_num`,`leader`,`phone`,`email`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`) values

(100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL),

(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-01-31 23:51:58','',NULL);

/*Table structure for table `sys_dict_data` */

DROP TABLE IF EXISTS `sys_dict_data`;

CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) COLLATE utf8mb4_general_ci DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典数据表';

/*Data for the table `sys_dict_data` */

insert  into `sys_dict_data`(`dict_code`,`dict_sort`,`dict_label`,`dict_value`,`dict_type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,1,'男','0','sys_user_sex','','','Y','0','admin','2025-01-31 23:51:59','',NULL,'性别男'),

(2,2,'女','1','sys_user_sex','','','N','0','admin','2025-01-31 23:51:59','',NULL,'性别女'),

(3,3,'未知','2','sys_user_sex','','','N','0','admin','2025-01-31 23:51:59','',NULL,'性别未知'),

(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2025-01-31 23:51:59','',NULL,'显示菜单'),

(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'隐藏菜单'),

(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2025-01-31 23:51:59','',NULL,'正常状态'),

(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'停用状态'),

(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2025-01-31 23:51:59','',NULL,'正常状态'),

(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'停用状态'),

(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2025-01-31 23:51:59','',NULL,'默认分组'),

(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2025-01-31 23:51:59','',NULL,'系统分组'),

(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2025-01-31 23:51:59','',NULL,'系统默认是'),

(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'系统默认否'),

(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2025-01-31 23:51:59','',NULL,'通知'),

(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2025-01-31 23:51:59','',NULL,'公告'),

(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2025-01-31 23:51:59','',NULL,'正常状态'),

(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'关闭状态'),

(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2025-01-31 23:51:59','',NULL,'其他操作'),

(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2025-01-31 23:51:59','',NULL,'新增操作'),

(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2025-01-31 23:51:59','',NULL,'修改操作'),

(21,3,'删除','3','sys_oper_type','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'删除操作'),

(22,4,'授权','4','sys_oper_type','','primary','N','0','admin','2025-01-31 23:51:59','',NULL,'授权操作'),

(23,5,'导出','5','sys_oper_type','','warning','N','0','admin','2025-01-31 23:51:59','',NULL,'导出操作'),

(24,6,'导入','6','sys_oper_type','','warning','N','0','admin','2025-01-31 23:51:59','',NULL,'导入操作'),

(25,7,'强退','7','sys_oper_type','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'强退操作'),

(26,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2025-01-31 23:51:59','',NULL,'生成操作'),

(27,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'清空操作'),

(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2025-01-31 23:51:59','',NULL,'正常状态'),

(29,2,'失败','1','sys_common_status','','danger','N','0','admin','2025-01-31 23:51:59','',NULL,'停用状态');

/*Table structure for table `sys_dict_type` */

DROP TABLE IF EXISTS `sys_dict_type`;

CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典类型',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典类型表';

/*Data for the table `sys_dict_type` */

insert  into `sys_dict_type`(`dict_id`,`dict_name`,`dict_type`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,'用户性别','sys_user_sex','0','admin','2025-01-31 23:51:59','',NULL,'用户性别列表'),

(2,'菜单状态','sys_show_hide','0','admin','2025-01-31 23:51:59','',NULL,'菜单状态列表'),

(3,'系统开关','sys_normal_disable','0','admin','2025-01-31 23:51:59','',NULL,'系统开关列表'),

(4,'任务状态','sys_job_status','0','admin','2025-01-31 23:51:59','',NULL,'任务状态列表'),

(5,'任务分组','sys_job_group','0','admin','2025-01-31 23:51:59','',NULL,'任务分组列表'),

(6,'系统是否','sys_yes_no','0','admin','2025-01-31 23:51:59','',NULL,'系统是否列表'),

(7,'通知类型','sys_notice_type','0','admin','2025-01-31 23:51:59','',NULL,'通知类型列表'),

(8,'通知状态','sys_notice_status','0','admin','2025-01-31 23:51:59','',NULL,'通知状态列表'),

(9,'操作类型','sys_oper_type','0','admin','2025-01-31 23:51:59','',NULL,'操作类型列表'),

(10,'系统状态','sys_common_status','0','admin','2025-01-31 23:51:59','',NULL,'登录状态列表');

/*Table structure for table `sys_job` */

DROP TABLE IF EXISTS `sys_job`;

CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) COLLATE utf8mb4_general_ci DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='定时任务调度表';

/*Data for the table `sys_job` */

insert  into `sys_job`(`job_id`,`job_name`,`job_group`,`invoke_target`,`cron_expression`,`misfire_policy`,`concurrent`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2025-01-31 23:51:59','',NULL,''),

(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2025-01-31 23:51:59','',NULL,''),

(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2025-01-31 23:51:59','',NULL,'');

/*Table structure for table `sys_job_log` */

DROP TABLE IF EXISTS `sys_job_log`;

CREATE TABLE `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志信息',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='定时任务调度日志表';

/*Data for the table `sys_job_log` */

/*Table structure for table `sys_logininfor` */

DROP TABLE IF EXISTS `sys_logininfor`;

CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作系统',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_sys_logininfor_s` (`status`),
  KEY `idx_sys_logininfor_lt` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统访问记录';

/*Data for the table `sys_logininfor` */

insert  into `sys_logininfor`(`info_id`,`user_name`,`ipaddr`,`login_location`,`browser`,`os`,`status`,`msg`,`login_time`) values

(1,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','1','用户不存在/密码错误','2025-02-06 22:57:12'),

(2,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-06 22:57:22'),

(3,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','1','用户不存在/密码错误','2025-02-09 01:11:52'),

(4,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-09 01:12:01'),

(5,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-10 20:49:35'),

(6,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-10 22:35:39'),

(7,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-11 00:00:48'),

(8,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-11 09:28:48'),

(9,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','1','验证码错误','2025-02-19 21:41:16'),

(10,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-19 21:41:24'),

(11,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-19 22:50:10'),

(12,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-20 11:03:08'),

(13,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-20 14:03:15'),

(14,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-20 16:45:04'),

(15,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-20 21:43:12'),

(16,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-20 22:23:39'),

(17,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-21 16:20:52'),

(18,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-21 17:17:33'),

(19,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-22 01:04:27'),

(20,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','1','验证码错误','2025-02-22 14:11:55'),

(21,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-22 14:12:04'),

(22,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','退出成功','2025-02-22 14:50:53'),

(23,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-22 14:51:01'),

(24,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','退出成功','2025-02-22 14:53:41'),

(25,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-22 14:53:48'),

(26,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','1','用户不存在/密码错误','2025-02-22 21:00:30'),

(27,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-22 21:00:39'),

(28,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-23 00:51:02'),

(29,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-24 09:37:44'),

(30,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-24 13:41:48'),

(31,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-24 15:22:01'),

(32,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-24 21:19:30'),

(33,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-24 23:19:14'),

(34,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','1','用户不存在/密码错误','2025-02-27 17:14:17'),

(35,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-27 17:14:25'),

(36,'admin','127.0.0.1','内网IP','Chrome 12','Windows 10','0','登录成功','2025-02-28 13:50:45');

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2014 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`route_name`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,'系统管理',0,1,'system',NULL,'','',1,0,'M','0','0','','system','admin','2025-01-31 23:51:58','',NULL,'系统管理目录'),

(2,'系统监控',0,2,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2025-01-31 23:51:58','',NULL,'系统监控目录'),

(3,'系统工具',0,3,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2025-01-31 23:51:58','',NULL,'系统工具目录'),

(4,'若依官网',0,5,'http://ruoyi.vip',NULL,'','',0,0,'M','0','0','','guide','admin','2025-01-31 23:51:58','admin','2025-02-06 19:41:30','若依官网地址'),

(100,'用户管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2025-01-31 23:51:58','',NULL,'用户管理菜单'),

(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2025-01-31 23:51:58','',NULL,'角色管理菜单'),

(102,'菜单管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2025-01-31 23:51:58','',NULL,'菜单管理菜单'),

(103,'部门管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2025-01-31 23:51:58','',NULL,'部门管理菜单'),

(104,'岗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2025-01-31 23:51:58','',NULL,'岗位管理菜单'),

(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2025-01-31 23:51:58','',NULL,'字典管理菜单'),

(106,'参数设置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2025-01-31 23:51:58','',NULL,'参数设置菜单'),

(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2025-01-31 23:51:58','',NULL,'通知公告菜单'),

(108,'日志管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2025-01-31 23:51:58','',NULL,'日志管理菜单'),

(109,'在线用户',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2025-01-31 23:51:58','',NULL,'在线用户菜单'),

(110,'定时任务',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2025-01-31 23:51:58','',NULL,'定时任务菜单'),

(111,'数据监控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2025-01-31 23:51:58','',NULL,'数据监控菜单'),

(112,'服务监控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2025-01-31 23:51:58','',NULL,'服务监控菜单'),

(113,'缓存监控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2025-01-31 23:51:58','',NULL,'缓存监控菜单'),

(114,'缓存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2025-01-31 23:51:58','',NULL,'缓存列表菜单'),

(115,'表单构建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2025-01-31 23:51:58','',NULL,'表单构建菜单'),

(116,'代码生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2025-01-31 23:51:58','',NULL,'代码生成菜单'),

(117,'系统接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2025-01-31 23:51:58','',NULL,'系统接口菜单'),

(500,'操作日志',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2025-01-31 23:51:58','',NULL,'操作日志菜单'),

(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2025-01-31 23:51:58','',NULL,'登录日志菜单'),

(1000,'用户查询',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1001,'用户新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1002,'用户修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1003,'用户删除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1004,'用户导出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2025-01-31 23:51:58','',NULL,''),

(1005,'用户导入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2025-01-31 23:51:58','',NULL,''),

(1006,'重置密码',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2025-01-31 23:51:58','',NULL,''),

(1007,'角色查询',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1010,'角色删除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1011,'角色导出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2025-01-31 23:51:58','',NULL,''),

(1012,'菜单查询',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1013,'菜单新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1014,'菜单修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1015,'菜单删除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1016,'部门查询',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1017,'部门新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1018,'部门修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1019,'部门删除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1020,'岗位查询',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1021,'岗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1022,'岗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1023,'岗位删除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1024,'岗位导出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2025-01-31 23:51:58','',NULL,''),

(1025,'字典查询',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1028,'字典删除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1029,'字典导出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2025-01-31 23:51:58','',NULL,''),

(1030,'参数查询',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1031,'参数新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1032,'参数修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1033,'参数删除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1034,'参数导出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2025-01-31 23:51:58','',NULL,''),

(1035,'公告查询',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1038,'公告删除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1039,'操作查询',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1040,'操作删除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1041,'日志导出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2025-01-31 23:51:58','',NULL,''),

(1042,'登录查询',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1043,'登录删除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1044,'日志导出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2025-01-31 23:51:58','',NULL,''),

(1045,'账户解锁',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2025-01-31 23:51:58','',NULL,''),

(1046,'在线查询',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1047,'批量强退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2025-01-31 23:51:58','',NULL,''),

(1048,'单条强退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2025-01-31 23:51:58','',NULL,''),

(1049,'任务查询',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1050,'任务新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2025-01-31 23:51:58','',NULL,''),

(1051,'任务修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1052,'任务删除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1053,'状态修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2025-01-31 23:51:58','',NULL,''),

(1054,'任务导出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2025-01-31 23:51:58','',NULL,''),

(1055,'生成查询',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2025-01-31 23:51:58','',NULL,''),

(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2025-01-31 23:51:58','',NULL,''),

(1057,'生成删除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2025-01-31 23:51:58','',NULL,''),

(1058,'导入代码',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2025-01-31 23:51:58','',NULL,''),

(1059,'预览代码',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2025-01-31 23:51:58','',NULL,''),

(1060,'生成代码',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2025-01-31 23:51:58','',NULL,''),

(2000,'AIGC管理',0,4,'aigc',NULL,NULL,'',1,0,'M','0','0','','star','admin','2025-02-06 19:41:14','admin','2025-02-06 20:30:51',''),

(2001,'模型列表',2000,1,'modelList','ai/modelList/index',NULL,'',1,0,'C','0','0','ai:modelList:list','star','admin','2025-02-06 19:56:00','admin','2025-02-06 20:31:08','模型列表菜单'),

(2002,'模型列表查询',2001,1,'#','',NULL,'',1,0,'F','0','0','ai:modelList:query','#','admin','2025-02-06 19:56:00','',NULL,''),

(2003,'模型列表新增',2001,2,'#','',NULL,'',1,0,'F','0','0','ai:modelList:add','#','admin','2025-02-06 19:56:00','',NULL,''),

(2004,'模型列表修改',2001,3,'#','',NULL,'',1,0,'F','0','0','ai:modelList:edit','#','admin','2025-02-06 19:56:00','',NULL,''),

(2005,'模型列表删除',2001,4,'#','',NULL,'',1,0,'F','0','0','ai:modelList:remove','#','admin','2025-02-06 19:56:00','',NULL,''),

(2006,'模型列表导出',2001,5,'#','',NULL,'',1,0,'F','0','0','ai:modelList:export','#','admin','2025-02-06 19:56:00','',NULL,''),

(2007,'智能助手',2000,2,'chatBox','ai/chatBox/index',NULL,'',1,0,'C','0','0',NULL,'wechat','admin','2025-02-22 14:42:19','',NULL,''),

(2008,'向量库列表',2000,1,'databaseList','ai/databaseList/index',NULL,'',1,0,'C','0','0','ai:databaseList:list','table','admin','2025-02-28 14:27:07','admin','2025-02-28 14:56:27','向量库列表菜单'),

(2009,'向量库列表查询',2008,1,'#','',NULL,'',1,0,'F','0','0','ai:databaseList:query','#','admin','2025-02-28 14:27:07','',NULL,''),

(2010,'向量库列表新增',2008,2,'#','',NULL,'',1,0,'F','0','0','ai:databaseList:add','#','admin','2025-02-28 14:27:07','',NULL,''),

(2011,'向量库列表修改',2008,3,'#','',NULL,'',1,0,'F','0','0','ai:databaseList:edit','#','admin','2025-02-28 14:27:07','',NULL,''),

(2012,'向量库列表删除',2008,4,'#','',NULL,'',1,0,'F','0','0','ai:databaseList:remove','#','admin','2025-02-28 14:27:07','',NULL,''),

(2013,'向量库列表导出',2008,5,'#','',NULL,'',1,0,'F','0','0','ai:databaseList:export','#','admin','2025-02-28 14:27:07','',NULL,'');

/*Table structure for table `sys_notice` */

DROP TABLE IF EXISTS `sys_notice`;

CREATE TABLE `sys_notice` (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通知公告表';

/*Data for the table `sys_notice` */

insert  into `sys_notice`(`notice_id`,`notice_title`,`notice_type`,`notice_content`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,'温馨提醒：2018-07-01 若依新版本发布啦','2','新版本内容','0','admin','2025-01-31 23:51:59','',NULL,'管理员'),

(2,'维护通知：2018-07-01 若依系统凌晨维护','1','维护内容','0','admin','2025-01-31 23:51:59','',NULL,'管理员');

/*Table structure for table `sys_oper_log` */

DROP TABLE IF EXISTS `sys_oper_log`;

CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(20) DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志记录';

/*Data for the table `sys_oper_log` */

insert  into `sys_oper_log`(`oper_id`,`title`,`business_type`,`method`,`request_method`,`operator_type`,`oper_name`,`dept_name`,`oper_url`,`oper_ip`,`oper_location`,`oper_param`,`json_result`,`status`,`error_msg`,`oper_time`,`cost_time`) values

(2,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"frequencyPenalty\":0.32,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.24,\"temperature\":0.43,\"topK\":10,\"topP\":0.07}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-06 22:57:41',59),

(3,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/chat_model_list','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:49:52',81),

(4,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"modelList\",\"className\":\"ChatModelList\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"聊天模型列表ID\",\"columnId\":15,\"columnName\":\"id\",\"columnType\":\"bigint(20)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-06 19:47:50\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2025-02-10 20:49:52\",\"usableColumn\":false},{\"capJavaField\":\"BaseUrl\",\"columnComment\":\"配置URL\",\"columnId\":29,\"columnName\":\"base_url\",\"columnType\":\"varchar(255)\",\"createBy\":\"\",\"createTime\":\"2025-02-10 20:49:52\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"baseUrl\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"ModelVersion\",\"columnComment\":\"模型版本\",\"columnId\":16,\"columnName\":\"model_version\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-06 19:47:50\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"modelVersion\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2025-02-10 20:49:52\",\"usableColumn\":false},{\"capJavaField\":\"ModelName\",\"columnComment\":\"模型名称\",\"columnId\":17,\"columnName\":\"model_name\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-06 19:47:50\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:50:19',66),

(5,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"modelList\",\"className\":\"ChatModelList\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"聊天模型列表ID\",\"columnId\":15,\"columnName\":\"id\",\"columnType\":\"bigint(20)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-06 19:47:50\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2025-02-10 20:50:19\",\"usableColumn\":false},{\"capJavaField\":\"BaseUrl\",\"columnComment\":\"配置URL\",\"columnId\":29,\"columnName\":\"base_url\",\"columnType\":\"varchar(255)\",\"createBy\":\"\",\"createTime\":\"2025-02-10 20:49:52\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"baseUrl\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2025-02-10 20:50:19\",\"usableColumn\":false},{\"capJavaField\":\"ModelVersion\",\"columnComment\":\"模型版本\",\"columnId\":16,\"columnName\":\"model_version\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-06 19:47:50\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"modelVersion\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2025-02-10 20:50:19\",\"usableColumn\":false},{\"capJavaField\":\"ModelName\",\"columnComment\":\"模型名称\",\"columnId\":17,\"columnName\":\"model_name\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-06 19:47:50\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:57:33',39),

(6,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.32,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.24,\"temperature\":0.43,\"topK\":10,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:58:55',77),

(7,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.32,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.24,\"temperature\":0.43,\"topK\":10,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:59:04',3),

(8,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.32,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.24,\"temperature\":0.43,\"topK\":10,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:59:12',2),

(9,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.32,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":10,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:59:20',37),

(10,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.22,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":10,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:59:28',5),

(11,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.22,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":18,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:59:36',10),

(12,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.22,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":18,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 20:59:52',3),

(13,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.2:11434\",\"frequencyPenalty\":0.22,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":18,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 21:12:01',81),

(14,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.22,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":18,\"topP\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-10 21:12:21',61),

(15,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.22,\"id\":1,\"isChose\":true,\"lowVram\":false,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":18,\"topP\":0.12}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-22 14:19:08',116),

(16,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.22,\"id\":1,\"isChose\":true,\"lowVram\":true,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":13,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":18,\"topP\":0.12}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-22 14:19:22',59),

(17,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.22,\"id\":1,\"isChose\":true,\"lowVram\":true,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":8,\"numPredict\":89,\"numThread\":10,\"params\":{},\"presencePenalty\":0.32,\"repeatPenalty\":0.22,\"temperature\":0.43,\"topK\":18,\"topP\":0.12}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-22 14:29:11',95),

(18,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.02,\"id\":1,\"isChose\":true,\"lowVram\":true,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":1,\"numPredict\":98,\"numThread\":2,\"params\":{},\"presencePenalty\":0.02,\"repeatPenalty\":0.03,\"temperature\":0.02,\"topK\":0,\"topP\":0.02}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-22 14:31:16',97),

(19,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"ai/chatBox/index\",\"createBy\":\"admin\",\"icon\":\"wechat\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"智能助手\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2000,\"path\":\"chatBox\",\"status\":\"0\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-22 14:42:19',68),

(20,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/chat_model_list','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-24 13:52:57',69),

(21,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"apiKey\":\"sk-86e64018e9734f5f9f49dd5fed341e26\",\"baseUrl\":\"https://api.deepseek.com\",\"frequencyPenalty\":0.02,\"id\":1,\"isChose\":true,\"lowVram\":true,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":1,\"numPredict\":98,\"numThread\":2,\"params\":{},\"presencePenalty\":0.02,\"repeatPenalty\":0.03,\"temperature\":0.02,\"topK\":0,\"topP\":0.02}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-24 14:04:30',53),

(22,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"apiKey\":\"sk-86e64018e9734f5f9f49dd5fed341e26\",\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.02,\"id\":1,\"isChose\":true,\"lowVram\":true,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":1,\"numPredict\":98,\"numThread\":2,\"params\":{},\"presencePenalty\":0.02,\"repeatPenalty\":0.03,\"temperature\":0.02,\"topK\":0,\"topP\":0.02}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-24 15:23:23',69),

(23,'模型列表',2,'com.ruoyi.ai.controller.ChatModelListController.edit()','PUT',1,'admin','研发部门','/ai/modelList','127.0.0.1','内网IP','{\"apiKey\":\"sk-86e64018e9734f5f9f49dd5fed341e26\",\"baseUrl\":\"http://127.0.0.1:11434\",\"frequencyPenalty\":0.02,\"id\":1,\"isChose\":true,\"lowVram\":true,\"modelName\":\"deepseek\",\"modelVersion\":\"deepseek-r1:8b\",\"numGpu\":1,\"numPredict\":98,\"numThread\":2,\"params\":{},\"presencePenalty\":0.02,\"repeatPenalty\":0,\"temperature\":0.74,\"topK\":0,\"topP\":0.02}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-24 15:32:41',31),

(24,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"vector_database_list\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-28 14:21:06',110),

(25,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"databaseList\",\"className\":\"VectorDatabaseList\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"向量数据库列表ID\",\"columnId\":31,\"columnName\":\"id\",\"columnType\":\"bigint(20)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-28 14:21:06\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":3,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"AliasName\",\"columnComment\":\"数据库别名\",\"columnId\":32,\"columnName\":\"alias_name\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-28 14:21:06\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"aliasName\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"LIKE\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":3,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Host\",\"columnComment\":\"数据库地址\",\"columnId\":33,\"columnName\":\"host\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-28 14:21:06\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"0\",\"isRequired\":\"0\",\"javaField\":\"host\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":3,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Port\",\"columnComment\":\"数据库端口\",\"columnId\":34,\"columnName\":\"port\",\"columnType\":\"int(10)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-28 14:21:06\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"0\",\"isRequired\":\"0','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-28 14:25:31',69),

(26,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"vector_database_list\"}',NULL,0,NULL,'2025-02-28 14:25:39',94),

(27,'向量库列表',1,'com.ruoyi.ai.controller.VectorDatabaseListController.add()','POST',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}',NULL,1,'\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\r\n### The error may exist in file [F:\\ruoyi-deepseek\\ruoyi-ai\\target\\classes\\mapper\\ai\\VectorDatabaseListMapper.xml]\r\n### The error may involve com.ruoyi.ai.mapper.VectorDatabaseListMapper.insertVectorDatabaseList-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into vector_database_list          ( alias_name,             host,             port,             database,             table,             user,             password,             dimension )           values ( ?,             ?,             ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\n; bad SQL grammar []','2025-02-28 14:33:50',198),

(28,'向量库列表',1,'com.ruoyi.ai.controller.VectorDatabaseListController.add()','POST',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}',NULL,1,'\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\r\n### The error may exist in file [F:\\ruoyi-deepseek\\ruoyi-ai\\target\\classes\\mapper\\ai\\VectorDatabaseListMapper.xml]\r\n### The error may involve com.ruoyi.ai.mapper.VectorDatabaseListMapper.insertVectorDatabaseList-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into vector_database_list          ( alias_name,             host,             port,             database,             table,             user,             password,             dimension )           values ( ?,             ?,             ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\n; bad SQL grammar []','2025-02-28 14:33:57',3),

(29,'向量库列表',1,'com.ruoyi.ai.controller.VectorDatabaseListController.add()','POST',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"isChose\":false,\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}',NULL,1,'\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\r\n### The error may exist in file [F:\\ruoyi-deepseek\\ruoyi-ai\\target\\classes\\mapper\\ai\\VectorDatabaseListMapper.xml]\r\n### The error may involve com.ruoyi.ai.mapper.VectorDatabaseListMapper.insertVectorDatabaseList-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into vector_database_list          ( alias_name,             host,             port,             database,             table,             user,             password,             dimension,             is_chose )           values ( ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\n; bad SQL grammar []','2025-02-28 14:37:35',158),

(30,'向量库列表',1,'com.ruoyi.ai.controller.VectorDatabaseListController.add()','POST',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"isChose\":false,\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}',NULL,1,'\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\r\n### The error may exist in file [F:\\ruoyi-deepseek\\ruoyi-ai\\target\\classes\\mapper\\ai\\VectorDatabaseListMapper.xml]\r\n### The error may involve com.ruoyi.ai.mapper.VectorDatabaseListMapper.insertVectorDatabaseList-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into vector_database_list          ( alias_name,             host,             port,             database,             table,             user,             password,             dimension,             is_chose )           values ( ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\n; bad SQL grammar []','2025-02-28 14:37:39',4),

(31,'向量库列表',1,'com.ruoyi.ai.controller.VectorDatabaseListController.add()','POST',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"isChose\":true,\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}',NULL,1,'\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\r\n### The error may exist in file [F:\\ruoyi-deepseek\\ruoyi-ai\\target\\classes\\mapper\\ai\\VectorDatabaseListMapper.xml]\r\n### The error may involve com.ruoyi.ai.mapper.VectorDatabaseListMapper.insertVectorDatabaseList-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into vector_database_list          ( alias_name,             host,             port,             database,             table,             user,             password,             dimension,             is_chose )           values ( ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            table,\n            user,\n            password,\n           \' at line 5\n; bad SQL grammar []','2025-02-28 14:37:46',2),

(32,'向量库列表',1,'com.ruoyi.ai.controller.VectorDatabaseListController.add()','POST',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"isChose\":true,\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}',NULL,1,'\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            `table`,\n            `user`,\n            password,\n       \' at line 5\r\n### The error may exist in file [F:\\ruoyi-deepseek\\ruoyi-ai\\target\\classes\\mapper\\ai\\VectorDatabaseListMapper.xml]\r\n### The error may involve com.ruoyi.ai.mapper.VectorDatabaseListMapper.insertVectorDatabaseList-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into vector_database_list          ( alias_name,             host,             port,             database,             `table`,             `user`,             password,             dimension,             is_chose )           values ( ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near \'database,\n            `table`,\n            `user`,\n            password,\n       \' at line 5\n; bad SQL grammar []','2025-02-28 14:39:38',162),

(33,'向量库列表',1,'com.ruoyi.ai.controller.VectorDatabaseListController.add()','POST',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"id\":1,\"isChose\":true,\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-28 14:41:24',91),

(34,'向量库列表',2,'com.ruoyi.ai.controller.VectorDatabaseListController.edit()','PUT',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":385,\"host\":\"192.168.48.129\",\"id\":1,\"isChose\":true,\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-28 14:43:21',101),

(35,'向量库列表',2,'com.ruoyi.ai.controller.VectorDatabaseListController.edit()','PUT',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"id\":1,\"isChose\":false,\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-28 14:43:30',45),

(36,'向量库列表',2,'com.ruoyi.ai.controller.VectorDatabaseListController.edit()','PUT',1,'admin','研发部门','/ai/databaseList','127.0.0.1','内网IP','{\"aliasName\":\"PgVector向量库\",\"database\":\"postgres\",\"dimension\":384,\"host\":\"192.168.48.129\",\"id\":1,\"isChose\":true,\"params\":{},\"port\":5432,\"table\":\"knowledge_vector\",\"user\":\"box\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-28 14:43:35',44),

(37,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"ai/databaseList/index\",\"createTime\":\"2025-02-28 14:27:07\",\"icon\":\"table\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2008,\"menuName\":\"向量库列表\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"databaseList\",\"perms\":\"ai:databaseList:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-02-28 14:56:28',72);

/*Table structure for table `sys_post` */

DROP TABLE IF EXISTS `sys_post`;

CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位信息表';

/*Data for the table `sys_post` */

insert  into `sys_post`(`post_id`,`post_code`,`post_name`,`post_sort`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,'ceo','董事长',1,'0','admin','2025-01-31 23:51:58','',NULL,''),

(2,'se','项目经理',2,'0','admin','2025-01-31 23:51:58','',NULL,''),

(3,'hr','人力资源',3,'0','admin','2025-01-31 23:51:58','',NULL,''),

(4,'user','普通员工',4,'0','admin','2025-01-31 23:51:58','',NULL,'');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色信息表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`role_id`,`role_name`,`role_key`,`role_sort`,`data_scope`,`menu_check_strictly`,`dept_check_strictly`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2025-01-31 23:51:58','',NULL,'超级管理员'),

(2,'普通角色','common',2,'2',1,1,'0','0','admin','2025-01-31 23:51:58','',NULL,'普通角色');

/*Table structure for table `sys_role_dept` */

DROP TABLE IF EXISTS `sys_role_dept`;

CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色和部门关联表';

/*Data for the table `sys_role_dept` */

insert  into `sys_role_dept`(`role_id`,`dept_id`) values

(2,100),

(2,101),

(2,105);

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色和菜单关联表';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`role_id`,`menu_id`) values

(2,1),

(2,2),

(2,3),

(2,4),

(2,100),

(2,101),

(2,102),

(2,103),

(2,104),

(2,105),

(2,106),

(2,107),

(2,108),

(2,109),

(2,110),

(2,111),

(2,112),

(2,113),

(2,114),

(2,115),

(2,116),

(2,117),

(2,500),

(2,501),

(2,1000),

(2,1001),

(2,1002),

(2,1003),

(2,1004),

(2,1005),

(2,1006),

(2,1007),

(2,1008),

(2,1009),

(2,1010),

(2,1011),

(2,1012),

(2,1013),

(2,1014),

(2,1015),

(2,1016),

(2,1017),

(2,1018),

(2,1019),

(2,1020),

(2,1021),

(2,1022),

(2,1023),

(2,1024),

(2,1025),

(2,1026),

(2,1027),

(2,1028),

(2,1029),

(2,1030),

(2,1031),

(2,1032),

(2,1033),

(2,1034),

(2,1035),

(2,1036),

(2,1037),

(2,1038),

(2,1039),

(2,1040),

(2,1041),

(2,1042),

(2,1043),

(2,1044),

(2,1045),

(2,1046),

(2,1047),

(2,1048),

(2,1049),

(2,1050),

(2,1051),

(2,1052),

(2,1053),

(2,1054),

(2,1055),

(2,1056),

(2,1057),

(2,1058),

(2,1059),

(2,1060);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) COLLATE utf8mb4_general_ci DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '手机号码',
  `sex` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '密码',
  `status` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_id`,`dept_id`,`user_name`,`nick_name`,`user_type`,`email`,`phonenumber`,`sex`,`avatar`,`password`,`status`,`del_flag`,`login_ip`,`login_date`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values

(1,103,'admin','若依','00','ry@163.com','15888888888','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2025-02-28 13:50:46','admin','2025-01-31 23:51:58','','2025-02-28 13:50:45','管理员'),

(2,105,'ry','若依','00','ry@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2025-01-31 23:51:58','admin','2025-01-31 23:51:58','',NULL,'测试员');

/*Table structure for table `sys_user_online` */

DROP TABLE IF EXISTS `sys_user_online`;

CREATE TABLE `sys_user_online` (
  `sessionId` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户会话id',
  `login_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录账号',
  `dept_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '部门名称',
  `ipaddr` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作系统',
  `status` varchar(10) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '在线状态on_line在线off_line离线',
  `start_timestamp` datetime DEFAULT NULL COMMENT 'session创建时间',
  `last_access_time` datetime DEFAULT NULL COMMENT 'session最后访问时间',
  `expire_time` int(5) DEFAULT '0' COMMENT '超时时间，单位为分钟',
  PRIMARY KEY (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='在线用户记录';

/*Data for the table `sys_user_online` */

/*Table structure for table `sys_user_post` */

DROP TABLE IF EXISTS `sys_user_post`;

CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户与岗位关联表';

/*Data for the table `sys_user_post` */

insert  into `sys_user_post`(`user_id`,`post_id`) values

(1,1),

(2,2);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户和角色关联表';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`user_id`,`role_id`) values

(1,1),

(2,2);

/*Table structure for table `vector_database_list` */

DROP TABLE IF EXISTS `vector_database_list`;

CREATE TABLE `vector_database_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '向量数据库列表ID',
  `alias_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库别名',
  `host` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库地址',
  `port` int(10) DEFAULT NULL COMMENT '数据库端口',
  `database` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库名',
  `table` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库表名称',
  `user` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库用户名',
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库密码',
  `dimension` int(10) DEFAULT NULL COMMENT '向量纬度',
  `is_chose` tinyint(1) DEFAULT NULL COMMENT '是否选择',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `vector_database_list` */

insert  into `vector_database_list`(`id`,`alias_name`,`host`,`port`,`database`,`table`,`user`,`password`,`dimension`,`is_chose`) values

(1,'PgVector向量库','192.168.48.129',5432,'postgres','knowledge_vector','box','123456',384,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session`
(
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天会话ID',
  `user_id`     bigint(20) NOT NULL COMMENT '用户ID',
  `title`       varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '聊天会话名',
  `memory`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '聊天会话记忆',
  `create_time` datetime                                DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='聊天会话表';


DROP TABLE IF EXISTS `chat_session_detail`;
CREATE TABLE `chat_session_detail`
(
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天会话详细ID',
  `session_id`  bigint(20) NOT NULL COMMENT '聊天会话ID',
  `role`        varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话角色 user:用户 ai:AI',
  `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '聊天内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY           `idx_session_id` (`session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='聊天会话详情表';
