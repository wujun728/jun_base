/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.26-log : Database - ruoyi
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ruoyi` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `ruoyi`;

/*Table structure for table `demo_car` */

DROP TABLE IF EXISTS `demo_car`;

CREATE TABLE `demo_car` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `code` varchar(100) DEFAULT NULL COMMENT '品牌代码',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` char(1) DEFAULT NULL,
  `tenant_id` varchar(64) NOT NULL DEFAULT '00000000' COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='车辆品牌';

/*Data for the table `demo_car` */

insert  into `demo_car`(`id`,`name`,`code`,`remark`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`,`tenant_id`) values (1,'比亚迪','001',NULL,NULL,NULL,NULL,NULL,NULL,'00000000'),(2,'吉利','002',NULL,NULL,NULL,NULL,NULL,NULL,'00000000');

/*Table structure for table `demo_car_model` */

DROP TABLE IF EXISTS `demo_car_model`;

CREATE TABLE `demo_car_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `car_id` varchar(32) NOT NULL COMMENT '汽车',
  `name` varchar(100) DEFAULT NULL COMMENT '型号名',
  `value` varchar(100) DEFAULT NULL COMMENT '型号代码',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  `remark` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` char(1) DEFAULT NULL,
  `tenant_id` varchar(64) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='车辆型号';

/*Data for the table `demo_car_model` */

insert  into `demo_car_model`(`id`,`car_id`,`name`,`value`,`sort`,`remark`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`,`tenant_id`) values (1,'1','宋dmi','001',1,NULL,NULL,NULL,NULL,NULL,NULL,'00000000'),(2,'2','星越L','001',1,NULL,NULL,NULL,NULL,NULL,NULL,'00000000'),(3,'1','唐DM','002',2,NULL,NULL,NULL,NULL,NULL,NULL,'00000000');

/*Table structure for table `demo_expand_table` */

DROP TABLE IF EXISTS `demo_expand_table`;

CREATE TABLE `demo_expand_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '商品名称',
  `shop` varchar(100) NOT NULL COMMENT '所属店铺',
  `category` varchar(100) DEFAULT NULL COMMENT '商品分类',
  `address` text COMMENT '店铺地址',
  `description` longtext COMMENT '商品描述',
  `tag` varchar(200) DEFAULT NULL COMMENT '标签',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `del_flag` char(1) DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `organization_id` varchar(64) DEFAULT NULL COMMENT '组织ID',
  `tenant_id` varchar(64) NOT NULL DEFAULT '00000000' COMMENT '租户ID',
  `image` text COMMENT '图片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `demo_expand_table` */

insert  into `demo_expand_table`(`id`,`name`,`shop`,`category`,`address`,`description`,`tag`,`create_time`,`update_by`,`update_time`,`remark`,`del_flag`,`create_by`,`organization_id`,`tenant_id`,`image`) values (1,'红米手机','小米公司','电子产品','{\"lng\":116.415202,\"lat\":39.909298}','红米note9 pro','便宜好用',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'00000000',NULL),(2,'华为手机','华为公司','电子产品','{\"lng\":116.332342,\"lat\":39.907306}','华为mate50','高端牌子',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'00000000',NULL);

/*Table structure for table `demo_table` */

DROP TABLE IF EXISTS `demo_table`;

CREATE TABLE `demo_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `author` varchar(100) NOT NULL COMMENT '作者',
  `type` varchar(250) DEFAULT NULL COMMENT '类型',
  `level` varchar(100) DEFAULT NULL COMMENT '密码',
  `content` longtext COMMENT '内容',
  `status` varchar(32) DEFAULT NULL COMMENT '状态',
  `tag` varchar(200) DEFAULT NULL COMMENT '标签',
  `readings` int(11) DEFAULT NULL COMMENT '阅读数',
  `publish_date` datetime DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `del_flag` char(1) DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `tenant_id` varchar(64) NOT NULL DEFAULT '00000000' COMMENT '租户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_test_table_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Data for the table `demo_table` */

insert  into `demo_table`(`id`,`title`,`author`,`type`,`level`,`content`,`status`,`tag`,`readings`,`publish_date`,`create_time`,`update_by`,`update_time`,`remark`,`del_flag`,`create_by`,`tenant_id`) values (1,'测试','sunseagear','1','123132','<p>阿斯顿发送到发斯蒂芬阿斯蒂芬阿萨德发送到发的发送到发的发</p>','0','111',1111,'2021-10-28 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,'00000000');

/*Table structure for table `demo_tree_and_table` */

DROP TABLE IF EXISTS `demo_tree_and_table`;

CREATE TABLE `demo_tree_and_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '部门名称',
  `type` varchar(250) DEFAULT NULL COMMENT '类型',
  `tag` varchar(200) DEFAULT NULL COMMENT '标签',
  `area_id` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `del_flag` char(1) DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `tenant_id` varchar(64) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `demo_tree_and_table` */

insert  into `demo_tree_and_table`(`id`,`name`,`type`,`tag`,`area_id`,`create_time`,`update_by`,`update_time`,`remark`,`del_flag`,`create_by`,`tenant_id`) values (1,'开发部门',NULL,'java开发','2',NULL,NULL,NULL,NULL,NULL,NULL,'00000000'),(2,'市场部',NULL,'办事处','4',NULL,NULL,NULL,NULL,NULL,NULL,'00000000');

/*Table structure for table `demo_tree_table` */

DROP TABLE IF EXISTS `demo_tree_table`;

CREATE TABLE `demo_tree_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '机构名称',
  `geocoding` varchar(32) DEFAULT NULL COMMENT '是否叶子节点',
  `postal_code` varchar(32) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父节点',
  `ancestors` varchar(1000) DEFAULT NULL COMMENT '父节点路径',
  `create_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` char(1) DEFAULT NULL COMMENT '删除标记',
  `remark` varchar(255) DEFAULT NULL,
  `tenant_id` varchar(64) NOT NULL DEFAULT '00000000' COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `demo_tree_table` */

insert  into `demo_tree_table`(`id`,`name`,`geocoding`,`postal_code`,`parent_id`,`ancestors`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`,`remark`,`tenant_id`) values (1,'陕西','001','001','0','0,',NULL,NULL,NULL,NULL,NULL,'Shaanxi','00000000'),(2,'西安','001001','001001','1','0,1,',NULL,NULL,NULL,NULL,NULL,'xi\'an','00000000'),(4,'宝鸡','001003','001003','1','0,1,',NULL,NULL,NULL,NULL,NULL,'baoji','00000000'),(5,'雁塔','001001001','001001001','2','0,1,2,',NULL,NULL,NULL,NULL,NULL,'yanta','00000000'),(6,'兴平','132132','123132','8','0,1,8,',NULL,NULL,NULL,NULL,NULL,'asdfasdf','00000000'),(8,'咸阳','1111','111','1','0,1,',NULL,NULL,NULL,NULL,NULL,'11','00000000'),(9,'安康','111','222','1','0,1,',NULL,NULL,NULL,NULL,NULL,'333','00000000');

/*Table structure for table `gen_table` */

DROP TABLE IF EXISTS `gen_table`;

CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='代码生成业务表';

/*Data for the table `gen_table` */

insert  into `gen_table`(`table_id`,`table_name`,`table_comment`,`sub_table_name`,`sub_table_fk_name`,`class_name`,`tpl_category`,`package_name`,`module_name`,`business_name`,`function_name`,`function_author`,`gen_type`,`gen_path`,`options`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,'sys_config','参数配置表',NULL,NULL,'SysConfig','crud','com.ruoyi.system','system','config','参数配置','ruoyi','0','/',NULL,'admin','2021-09-21 18:07:52','',NULL,NULL,'00000000'),(2,'sys_tenant','租户管理',NULL,NULL,'SysTenant','crud','com.ruoyi.system','system','tenant','租户管理','ruoyi','0','/',NULL,'admin','2021-10-10 16:10:50','',NULL,NULL,'00000000'),(4,'sys_data_rule','数据权限表',NULL,NULL,'SysDataRule','crud','com.ruoyi.system','system','rule','数据权限','ruoyi','0','/','{}','admin','2021-10-19 17:14:27','','2021-10-19 17:14:50',NULL,'00000000'),(5,'demo_table','综合表格',NULL,NULL,'Table','crud','com.ruoyi.demo','demo','table','综合表格','ruoyi','0','/','{}','admin','2021-10-21 20:43:12','','2021-10-22 13:27:41',NULL,'00000000'),(6,'demo_tree_table','树形表格','','','TreeTable','tree','com.ruoyi.demo','demo','treeTable','树形表格','ruoyi','0','/','{\"treeCode\":\"id\",\"treeName\":\"name\",\"treeParentCode\":\"parent_id\"}','admin','2021-10-22 14:12:07','','2021-10-22 14:17:03',NULL,'00000000'),(7,'demo_expand_table','展开表格',NULL,NULL,'ExpandTable','crud','com.ruoyi.system','demo','expandTable','展开表格','ruoyi','0','/','{}','admin','2021-10-22 22:58:00','','2021-10-22 22:59:56',NULL,'00000000'),(8,'demo_car','车辆品牌',NULL,NULL,'Car','crud','com.ruoyi.system','demo','car','车辆品牌','ruoyi','0','/','{}','admin','2021-10-22 23:33:37','','2021-10-22 23:34:25',NULL,'00000000'),(9,'demo_car_model','车辆型号',NULL,NULL,'CarModel','crud','com.ruoyi.system','demo','car','车辆型号','ruoyi','0','/','{}','admin','2021-10-22 23:33:38','','2021-10-22 23:36:05',NULL,'00000000'),(10,'demo_tree_and_table','左树右表',NULL,NULL,'TreeAndTable','crud','com.ruoyi.system','system','treeandtable','左树右表','ruoyi','0','/','{}','admin','2021-10-23 11:58:21','','2021-10-23 12:01:14',NULL,'00000000');

/*Table structure for table `gen_table_column` */

DROP TABLE IF EXISTS `gen_table_column`;

CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8mb4 COMMENT='代码生成业务表字段';

/*Data for the table `gen_table_column` */

insert  into `gen_table_column`(`column_id`,`table_id`,`column_name`,`column_comment`,`column_type`,`java_type`,`java_field`,`is_pk`,`is_increment`,`is_required`,`is_insert`,`is_edit`,`is_list`,`is_query`,`query_type`,`html_type`,`dict_type`,`sort`,`create_by`,`create_time`,`update_by`,`update_time`,`tenant_id`) values (1,'1','config_id','参数主键','int(5)','Integer','configId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(2,'1','config_name','参数名称','varchar(100)','String','configName','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(3,'1','config_key','参数键名','varchar(100)','String','configKey','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(4,'1','config_value','参数键值','varchar(500)','String','configValue','0','0',NULL,'1','1','1','1','EQ','textarea','',4,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(5,'1','config_type','系统内置（Y是 N否）','char(1)','String','configType','0','0',NULL,'1','1','1','1','EQ','select','',5,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(6,'1','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',6,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(7,'1','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',7,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(8,'1','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',8,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(9,'1','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',9,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(10,'1','remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',10,'admin','2021-09-21 18:07:52','',NULL,'00000000'),(11,'2','info_id','访问ID','bigint(20)','Long','infoId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-10-10 16:10:50','',NULL,'00000000'),(12,'2','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',2,'admin','2021-10-10 16:10:50','',NULL,'00000000'),(13,'2','create_time','创建日期','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',3,'admin','2021-10-10 16:10:50','',NULL,'00000000'),(14,'2','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',4,'admin','2021-10-10 16:10:50','',NULL,'00000000'),(15,'2','update_time','更新日期','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',5,'admin','2021-10-10 16:10:50','',NULL,'00000000'),(16,'2','del_flag','删除标识','char(1)','String','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',6,'admin','2021-10-10 16:10:50','',NULL,'00000000'),(17,'2','remarks','备注','text','String','remarks','0','0',NULL,'1','1','1','1','EQ','textarea','',7,'admin','2021-10-10 16:10:50','',NULL,'00000000'),(18,'2','tenant_id','租户标识','varchar(64)','String','tenantId','0','0',NULL,'1','1','1','1','EQ','input','',8,'admin','2021-10-10 16:10:51','',NULL,'00000000'),(19,'2','contact','联系人','varchar(64)','String','contact','0','0','1','1','1','1','1','EQ','input','',9,'admin','2021-10-10 16:10:51','',NULL,'00000000'),(20,'2','phone','电话','varchar(64)','String','phone','0','0','1','1','1','1','1','EQ','input','',10,'admin','2021-10-10 16:10:51','',NULL,'00000000'),(21,'2','name','租户名称','varchar(64)','String','name','0','0','1','1','1','1','1','LIKE','input','',11,'admin','2021-10-10 16:10:51','',NULL,'00000000'),(22,'2','user_id',NULL,'varchar(64)','String','userId','0','0',NULL,'1','1','1','1','EQ','input','',12,'admin','2021-10-10 16:10:51','',NULL,'00000000'),(44,'4','id','主键','bigint(20)','Long','id','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-10-19 17:14:27','','2021-10-19 17:14:50','00000000'),(45,'4','resource_code','资源编号','varchar(255)','String','resourceCode','0','0','1','1','1','1',NULL,'EQ','input','',2,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:50','00000000'),(46,'4','scope_name','数据权限名称','varchar(255)','String','scopeName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:50','00000000'),(47,'4','scope_field','数据权限字段','varchar(255)','String','scopeField','0','0','1','1','1','1',NULL,'EQ','input','',4,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:50','00000000'),(48,'4','scope_class','数据权限类名','varchar(500)','String','scopeClass','0','0','1','1','1','1',NULL,'EQ','textarea','',5,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:50','00000000'),(49,'4','scope_column','数据权限字段','varchar(255)','String','scopeColumn','0','0','1','1','1','1',NULL,'EQ','input','',6,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:50','00000000'),(50,'4','scope_type','数据权限类型','int(2)','Integer','scopeType','0','0','1','1','1','1',NULL,'EQ','select','',7,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:50','00000000'),(51,'4','scope_value','数据权限值域','varchar(2000)','String','scopeValue','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',8,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:51','00000000'),(52,'4','remarks','数据权限备注','varchar(255)','String','remarks','0','0',NULL,'1','1','1',NULL,'EQ','input','',9,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:51','00000000'),(53,'4','create_by','创建人','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',10,'admin','2021-10-19 17:14:29','','2021-10-19 17:14:51','00000000'),(54,'4','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',11,'admin','2021-10-19 17:14:30','','2021-10-19 17:14:51','00000000'),(55,'4','update_by','修改人','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',12,'admin','2021-10-19 17:14:30','','2021-10-19 17:14:51','00000000'),(56,'4','update_time','修改时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',13,'admin','2021-10-19 17:14:30','','2021-10-19 17:14:51','00000000'),(57,'4','del_flag','是否已删除','int(2)','Integer','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',14,'admin','2021-10-19 17:14:30','','2021-10-19 17:14:51','00000000'),(58,'4','table_name','数据库表','varchar(64)','String','tableName','0','0',NULL,'1','1','1',NULL,'LIKE','input','',15,'admin','2021-10-19 17:14:30','','2021-10-19 17:14:51','00000000'),(59,'4','user_column','用户表对应字段','varchar(64)','String','userColumn','0','0',NULL,'1','1','1',NULL,'EQ','input','',16,'admin','2021-10-19 17:14:30','','2021-10-19 17:14:51','00000000'),(60,'4','tenant_id','租户ID','varchar(64)','String','tenantId','0','0','1','1','1','1',NULL,'EQ','input','',17,'admin','2021-10-19 17:14:30','','2021-10-19 17:14:51','00000000'),(61,'4','user_entity_field','user实体类对应字段','varchar(64)','String','userEntityField','0','0',NULL,'1','1','1',NULL,'EQ','input','',18,'admin','2021-10-19 17:14:31','','2021-10-19 17:14:51','00000000'),(62,'5','id','主键','varchar(64)','String','id','1','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-10-21 20:43:12','','2021-10-22 13:27:41','00000000'),(63,'5','title','标题','varchar(255)','String','title','0','0',NULL,'1','1','1','1','EQ','input','',2,'admin','2021-10-21 20:43:12','','2021-10-22 13:27:41','00000000'),(64,'5','author','作者','varchar(100)','String','author','0','0','1','1','1','1',NULL,'EQ','input','',3,'admin','2021-10-21 20:43:12','','2021-10-22 13:27:41','00000000'),(65,'5','type','类型','varchar(250)','String','type','0','0',NULL,'1','1','1',NULL,'EQ','select','sys_notice_type',4,'admin','2021-10-21 20:43:12','','2021-10-22 13:27:41','00000000'),(66,'5','level','密码','varchar(100)','String','level','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(67,'5','content','内容','longtext','String','content','0','0',NULL,'1','1','1',NULL,'EQ','editor','',6,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(68,'5','status','状态','varchar(32)','String','status','0','0',NULL,'1','1','1',NULL,'EQ','radio','sys_common_status',7,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(69,'5','tag','标签','varchar(200)','String','tag','0','0',NULL,'1','1','1',NULL,'EQ','input','',8,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(70,'5','readings','阅读数','int(11)','Long','readings','0','0',NULL,'1','1','1',NULL,'EQ','input','',9,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(71,'5','publish_date','发布时间','datetime','Date','publishDate','0','0',NULL,'1','1','1',NULL,'EQ','datetime','',10,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(72,'5','create_time',NULL,'datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',11,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(73,'5','update_by',NULL,'varchar(32)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',12,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(74,'5','update_time',NULL,'datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',13,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(75,'5','remarks',NULL,'varchar(255)','String','remarks','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',14,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(76,'5','del_flag',NULL,'char(1)','String','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',15,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(77,'5','create_by',NULL,'varchar(32)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',16,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(78,'5','tenant_id','租户ID','varchar(64)','String','tenantId','0','0','1','1',NULL,NULL,NULL,'EQ','input','',17,'admin','2021-10-21 20:43:13','','2021-10-22 13:27:42','00000000'),(79,'6','id',NULL,'varchar(64)','String','id','1','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-10-22 14:12:07','','2021-10-22 14:17:03','00000000'),(80,'6','name','机构名称','varchar(100)','String','name','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2021-10-22 14:12:08','','2021-10-22 14:17:03','00000000'),(81,'6','geocoding','是否叶子节点','varchar(32)','String','geocoding','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:03','00000000'),(82,'6','postal_code',NULL,'varchar(32)','String','postalCode','0','0',NULL,'1','1','1','1','EQ','input','',4,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:03','00000000'),(83,'6','parent_id','父节点','varchar(32)','String','parentId','0','0',NULL,'1','1','1',NULL,'EQ','input','',5,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:03','00000000'),(84,'6','parent_ids','父节点路径','varchar(1000)','String','parentIds','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',6,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:03','00000000'),(85,'6','create_by',NULL,'varchar(32)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',7,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:04','00000000'),(86,'6','create_time',NULL,'datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:04','00000000'),(87,'6','update_by',NULL,'varchar(32)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',9,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:04','00000000'),(88,'6','update_time',NULL,'datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',10,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:04','00000000'),(89,'6','del_flag','删除标记','char(1)','String','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',11,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:04','00000000'),(90,'6','remark',NULL,'varchar(255)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','input','',12,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:04','00000000'),(91,'6','tenant_id','租户ID','varchar(64)','String','tenantId','0','0','1','1','1','1',NULL,'EQ','input','',13,'admin','2021-10-22 14:12:09','','2021-10-22 14:17:04','00000000'),(92,'7','id','主键','bigint(20)','Long','id','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-10-22 22:58:00','','2021-10-22 22:59:56','00000000'),(93,'7','name','商品名称','varchar(255)','String','name','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2021-10-22 22:58:00','','2021-10-22 22:59:57','00000000'),(94,'7','shop','所属店铺','varchar(100)','String','shop','0','0','1','1','1','1',NULL,'EQ','input','',3,'admin','2021-10-22 22:58:00','','2021-10-22 22:59:57','00000000'),(95,'7','category','商品分类','varchar(100)','String','category','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2021-10-22 22:58:00','','2021-10-22 22:59:57','00000000'),(96,'7','address','店铺地址','text','String','address','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',5,'admin','2021-10-22 22:58:00','','2021-10-22 22:59:57','00000000'),(97,'7','description','商品描述','longtext','String','description','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',6,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(98,'7','tag','标签','varchar(200)','String','tag','0','0',NULL,'1','1','1',NULL,'EQ','input','',7,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(99,'7','create_time',NULL,'datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(100,'7','update_by',NULL,'varchar(32)','String','updateBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',9,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(101,'7','update_time',NULL,'datetime','Date','updateTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',10,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(102,'7','remark',NULL,'varchar(255)','String','remark','0','0',NULL,'1',NULL,'1',NULL,'EQ','input','',11,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(103,'7','del_flag',NULL,'char(1)','String','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',12,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(104,'7','create_by',NULL,'varchar(32)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',13,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(105,'7','organization_id','组织ID','varchar(64)','String','organizationId','0','0',NULL,'1',NULL,'1',NULL,'EQ','input','',14,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(106,'7','tenant_id','租户ID','varchar(64)','String','tenantId','0','0',NULL,'1',NULL,'1',NULL,'EQ','input','',15,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(107,'7','image','图片','text','String','image','0','0',NULL,'1','1','1','1','EQ','imageUpload','',16,'admin','2021-10-22 22:58:01','','2021-10-22 22:59:57','00000000'),(108,'8','id','主键','bigint(20)','Long','id','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-10-22 23:33:37','','2021-10-22 23:34:25','00000000'),(109,'8','name','名称','varchar(100)','String','name','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2021-10-22 23:33:37','','2021-10-22 23:34:25','00000000'),(110,'8','code','品牌代码','varchar(100)','String','code','0','0',NULL,'1','1','1',NULL,'EQ','input','',3,'admin','2021-10-22 23:33:37','','2021-10-22 23:34:25','00000000'),(111,'8','remark','备注','varchar(100)','String','remark','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',4,'admin','2021-10-22 23:33:37','','2021-10-22 23:34:25','00000000'),(112,'8','create_by',NULL,'varchar(32)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',5,'admin','2021-10-22 23:33:37','','2021-10-22 23:34:25','00000000'),(113,'8','create_time',NULL,'datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',6,'admin','2021-10-22 23:33:38','','2021-10-22 23:34:25','00000000'),(114,'8','update_by',NULL,'varchar(32)','String','updateBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',7,'admin','2021-10-22 23:33:38','','2021-10-22 23:34:25','00000000'),(115,'8','update_time',NULL,'datetime','Date','updateTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2021-10-22 23:33:38','','2021-10-22 23:34:26','00000000'),(116,'8','del_flag',NULL,'char(1)','String','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',9,'admin','2021-10-22 23:33:38','','2021-10-22 23:34:26','00000000'),(117,'8','tenant_id','租户ID','varchar(64)','String','tenantId','0','0','1','1',NULL,NULL,NULL,'EQ','input','',10,'admin','2021-10-22 23:33:38','','2021-10-22 23:34:26','00000000'),(118,'9','id','主键','bigint(20)','Long','id','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:05','00000000'),(119,'9','car_id','汽车','varchar(32)','String','carId','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',2,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:05','00000000'),(120,'9','name','型号名','varchar(100)','String','name','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:05','00000000'),(121,'9','value','型号代码','varchar(100)','String','value','0','0','1','1','1','1',NULL,'EQ','input','',4,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:05','00000000'),(122,'9','sort','排序','int(5)','Integer','sort','0','0','1','1','1','1',NULL,'EQ','input','',5,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:05','00000000'),(123,'9','remark','描述','varchar(100)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','input','',6,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:05','00000000'),(124,'9','create_by',NULL,'varchar(32)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',7,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:05','00000000'),(125,'9','create_time',NULL,'datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:05','00000000'),(126,'9','update_by',NULL,'varchar(32)','String','updateBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',9,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:06','00000000'),(127,'9','update_time',NULL,'datetime','Date','updateTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',10,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:06','00000000'),(128,'9','del_flag',NULL,'char(1)','String','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',11,'admin','2021-10-22 23:33:38','','2021-10-22 23:36:06','00000000'),(129,'9','tenant_id',NULL,'varchar(64)','String','tenantId','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',12,'admin','2021-10-22 23:33:39','','2021-10-22 23:36:06','00000000'),(130,'10','id','主键','varchar(64)','String','id','1','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-10-23 11:58:21','','2021-10-23 12:01:14','00000000'),(131,'10','name','部门名称','varchar(255)','String','name','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2021-10-23 11:58:21','','2021-10-23 12:01:14','00000000'),(132,'10','type','类型','varchar(250)','String','type','0','0',NULL,'1','1','1',NULL,'EQ','select','',3,'admin','2021-10-23 11:58:21','','2021-10-23 12:01:14','00000000'),(133,'10','tag','标签','varchar(200)','String','tag','0','0',NULL,'1','1','1',NULL,'EQ','input','',4,'admin','2021-10-23 11:58:21','','2021-10-23 12:01:14','00000000'),(134,'10','area_id','区域','varchar(64)','String','areaId','0','0',NULL,'1','1',NULL,NULL,'EQ','select','',5,'admin','2021-10-23 11:58:21','','2021-10-23 12:01:14','00000000'),(135,'10','create_time',NULL,'datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',6,'admin','2021-10-23 11:58:22','','2021-10-23 12:01:14','00000000'),(136,'10','update_by',NULL,'varchar(32)','String','updateBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',7,'admin','2021-10-23 11:58:22','','2021-10-23 12:01:14','00000000'),(137,'10','update_time',NULL,'datetime','Date','updateTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2021-10-23 11:58:22','','2021-10-23 12:01:14','00000000'),(138,'10','remark',NULL,'varchar(255)','String','remark','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',9,'admin','2021-10-23 11:58:22','','2021-10-23 12:01:14','00000000'),(139,'10','del_flag',NULL,'char(1)','String','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',10,'admin','2021-10-23 11:58:22','','2021-10-23 12:01:14','00000000'),(140,'10','create_by',NULL,'varchar(32)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',11,'admin','2021-10-23 11:58:22','','2021-10-23 12:01:14','00000000'),(141,'10','tenant_id',NULL,'varchar(64)','String','tenantId','0','0','1','1',NULL,NULL,NULL,'EQ','input','',12,'admin','2021-10-23 11:58:22','','2021-10-23 12:01:14','00000000');

/*Table structure for table `qrtz_blob_triggers` */

DROP TABLE IF EXISTS `qrtz_blob_triggers`;

CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Blob类型的触发器表';

/*Data for the table `qrtz_blob_triggers` */

/*Table structure for table `qrtz_calendars` */

DROP TABLE IF EXISTS `qrtz_calendars`;

CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日历信息表';

/*Data for the table `qrtz_calendars` */

/*Table structure for table `qrtz_cron_triggers` */

DROP TABLE IF EXISTS `qrtz_cron_triggers`;

CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Cron类型的触发器表';

/*Data for the table `qrtz_cron_triggers` */

insert  into `qrtz_cron_triggers`(`sched_name`,`trigger_name`,`trigger_group`,`cron_expression`,`time_zone_id`) values ('RuoyiScheduler','TASK_CLASS_NAME1','DEFAULT','0/10 * * * * ?','Asia/Shanghai'),('RuoyiScheduler','TASK_CLASS_NAME2','DEFAULT','0/15 * * * * ?','Asia/Shanghai'),('RuoyiScheduler','TASK_CLASS_NAME3','DEFAULT','0/20 * * * * ?','Asia/Shanghai');

/*Table structure for table `qrtz_fired_triggers` */

DROP TABLE IF EXISTS `qrtz_fired_triggers`;

CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(13) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(13) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='已触发的触发器表';

/*Data for the table `qrtz_fired_triggers` */

/*Table structure for table `qrtz_job_details` */

DROP TABLE IF EXISTS `qrtz_job_details`;

CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务详细信息表';

/*Data for the table `qrtz_job_details` */

insert  into `qrtz_job_details`(`sched_name`,`job_name`,`job_group`,`description`,`job_class_name`,`is_durable`,`is_nonconcurrent`,`is_update_data`,`requests_recovery`,`job_data`) values ('RuoyiScheduler','TASK_CLASS_NAME1','DEFAULT',NULL,'com.ruoyi.quartz.util.QuartzDisallowConcurrentExecution','0','1','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0TASK_PROPERTIESsr\0com.ruoyi.quartz.domain.SysJob\0\0\0\0\0\0\0\0L\0\nconcurrentt\0Ljava/lang/String;L\0cronExpressionq\0~\0	L\0invokeTargetq\0~\0	L\0jobGroupq\0~\0	L\0jobIdt\0Ljava/lang/Long;L\0jobNameq\0~\0	L\0\rmisfirePolicyq\0~\0	L\0statusq\0~\0	xr\0\'com.ruoyi.common.core.domain.BaseEntity\0\0\0\0\0\0\0\0L\0createByq\0~\0	L\0\ncreateTimet\0Ljava/util/Date;L\0paramsq\0~\0L\0remarkq\0~\0	L\0searchValueq\0~\0	L\0updateByq\0~\0	L\0\nupdateTimeq\0~\0xpt\0adminsr\0java.util.Datehj�KYt\0\0xpw\0\0|�F�xpt\0\0pppt\01t\00/10 * * * * ?t\0ryTask.ryNoParamst\0DEFAULTsr\0java.lang.Long;��̏#�\0J\0valuexr\0java.lang.Number������\0\0xp\0\0\0\0\0\0\0t\0系统默认（无参）t\03t\01x\0'),('RuoyiScheduler','TASK_CLASS_NAME2','DEFAULT',NULL,'com.ruoyi.quartz.util.QuartzDisallowConcurrentExecution','0','1','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0TASK_PROPERTIESsr\0com.ruoyi.quartz.domain.SysJob\0\0\0\0\0\0\0\0L\0\nconcurrentt\0Ljava/lang/String;L\0cronExpressionq\0~\0	L\0invokeTargetq\0~\0	L\0jobGroupq\0~\0	L\0jobIdt\0Ljava/lang/Long;L\0jobNameq\0~\0	L\0\rmisfirePolicyq\0~\0	L\0statusq\0~\0	xr\0\'com.ruoyi.common.core.domain.BaseEntity\0\0\0\0\0\0\0\0L\0createByq\0~\0	L\0\ncreateTimet\0Ljava/util/Date;L\0paramsq\0~\0L\0remarkq\0~\0	L\0searchValueq\0~\0	L\0updateByq\0~\0	L\0\nupdateTimeq\0~\0xpt\0adminsr\0java.util.Datehj�KYt\0\0xpw\0\0|�J�xpt\0\0pppt\01t\00/15 * * * * ?t\0ryTask.ryParams(\'ry\')t\0DEFAULTsr\0java.lang.Long;��̏#�\0J\0valuexr\0java.lang.Number������\0\0xp\0\0\0\0\0\0\0t\0系统默认（有参）t\03t\01x\0'),('RuoyiScheduler','TASK_CLASS_NAME3','DEFAULT',NULL,'com.ruoyi.quartz.util.QuartzDisallowConcurrentExecution','0','1','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0TASK_PROPERTIESsr\0com.ruoyi.quartz.domain.SysJob\0\0\0\0\0\0\0\0L\0\nconcurrentt\0Ljava/lang/String;L\0cronExpressionq\0~\0	L\0invokeTargetq\0~\0	L\0jobGroupq\0~\0	L\0jobIdt\0Ljava/lang/Long;L\0jobNameq\0~\0	L\0\rmisfirePolicyq\0~\0	L\0statusq\0~\0	xr\0\'com.ruoyi.common.core.domain.BaseEntity\0\0\0\0\0\0\0\0L\0createByq\0~\0	L\0\ncreateTimet\0Ljava/util/Date;L\0paramsq\0~\0L\0remarkq\0~\0	L\0searchValueq\0~\0	L\0updateByq\0~\0	L\0\nupdateTimeq\0~\0xpt\0adminsr\0java.util.Datehj�KYt\0\0xpw\0\0|�J�xpt\0\0pppt\01t\00/20 * * * * ?t\08ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)t\0DEFAULTsr\0java.lang.Long;��̏#�\0J\0valuexr\0java.lang.Number������\0\0xp\0\0\0\0\0\0\0t\0系统默认（多参）t\03t\01x\0');

/*Table structure for table `qrtz_locks` */

DROP TABLE IF EXISTS `qrtz_locks`;

CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储的悲观锁信息表';

/*Data for the table `qrtz_locks` */

insert  into `qrtz_locks`(`sched_name`,`lock_name`) values ('RuoyiScheduler','STATE_ACCESS'),('RuoyiScheduler','TRIGGER_ACCESS');

/*Table structure for table `qrtz_paused_trigger_grps` */

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;

CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='暂停的触发器表';

/*Data for the table `qrtz_paused_trigger_grps` */

/*Table structure for table `qrtz_scheduler_state` */

DROP TABLE IF EXISTS `qrtz_scheduler_state`;

CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(13) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(13) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调度器状态表';

/*Data for the table `qrtz_scheduler_state` */

insert  into `qrtz_scheduler_state`(`sched_name`,`instance_name`,`last_checkin_time`,`checkin_interval`) values ('RuoyiScheduler','DESKTOP-V4I2IAS1642336404267',1642337346063,15000);

/*Table structure for table `qrtz_simple_triggers` */

DROP TABLE IF EXISTS `qrtz_simple_triggers`;

CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(7) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(12) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(10) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简单触发器的信息表';

/*Data for the table `qrtz_simple_triggers` */

/*Table structure for table `qrtz_simprop_triggers` */

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;

CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='同步机制的行锁表';

/*Data for the table `qrtz_simprop_triggers` */

/*Table structure for table `qrtz_triggers` */

DROP TABLE IF EXISTS `qrtz_triggers`;

CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(13) DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(13) DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(13) NOT NULL COMMENT '开始时间',
  `end_time` bigint(13) DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(2) DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='触发器详细信息表';

/*Data for the table `qrtz_triggers` */

insert  into `qrtz_triggers`(`sched_name`,`trigger_name`,`trigger_group`,`job_name`,`job_group`,`description`,`next_fire_time`,`prev_fire_time`,`priority`,`trigger_state`,`trigger_type`,`start_time`,`end_time`,`calendar_name`,`misfire_instr`,`job_data`) values ('RuoyiScheduler','TASK_CLASS_NAME1','DEFAULT','TASK_CLASS_NAME1','DEFAULT',NULL,1642336410000,-1,5,'PAUSED','CRON',1642336404000,0,NULL,2,''),('RuoyiScheduler','TASK_CLASS_NAME2','DEFAULT','TASK_CLASS_NAME2','DEFAULT',NULL,1642336410000,-1,5,'PAUSED','CRON',1642336405000,0,NULL,2,''),('RuoyiScheduler','TASK_CLASS_NAME3','DEFAULT','TASK_CLASS_NAME3','DEFAULT',NULL,1642336420000,-1,5,'PAUSED','CRON',1642336406000,0,NULL,2,'');

/*Table structure for table `sys_config` */

DROP TABLE IF EXISTS `sys_config`;

CREATE TABLE `sys_config` (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';

/*Data for the table `sys_config` */

insert  into `sys_config`(`config_id`,`config_name`,`config_key`,`config_value`,`config_type`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2021-09-21 17:02:04','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow','00000000'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2021-09-21 17:02:04','',NULL,'初始化密码 123456','00000000'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2021-09-21 17:02:05','',NULL,'深色主题theme-dark，浅色主题theme-light','00000000'),(4,'账号自助-验证码开关','sys.account.captchaOnOff','true','Y','admin','2021-09-21 17:02:05','',NULL,'是否开启验证码功能（true开启，false关闭）','00000000'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2021-09-21 17:02:05','',NULL,'是否开启注册用户功能（true开启，false关闭）','00000000');

/*Table structure for table `sys_data_rule` */

DROP TABLE IF EXISTS `sys_data_rule`;

CREATE TABLE `sys_data_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_code` varchar(255) NOT NULL COMMENT '资源编号',
  `scope_name` varchar(255) NOT NULL COMMENT '数据权限名称',
  `scope_field` varchar(255) NOT NULL COMMENT '数据权限字段',
  `scope_class` varchar(500) NOT NULL COMMENT '数据权限类名',
  `scope_column` varchar(255) NOT NULL COMMENT '数据权限字段',
  `scope_type` int(2) NOT NULL COMMENT '数据权限类型',
  `scope_value` varchar(2000) DEFAULT NULL COMMENT '数据权限值域',
  `remark` varchar(255) DEFAULT NULL COMMENT '数据权限备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(2) DEFAULT NULL COMMENT '是否已删除',
  `table_name` varchar(64) DEFAULT NULL COMMENT '数据库表',
  `user_column` varchar(64) DEFAULT NULL COMMENT '用户表对应字段',
  `tenant_id` varchar(64) NOT NULL DEFAULT '00000000' COMMENT '租户ID',
  `user_entity_field` varchar(64) DEFAULT NULL COMMENT 'user实体类对应字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='数据权限表';

/*Data for the table `sys_data_rule` */

insert  into `sys_data_rule`(`id`,`resource_code`,`scope_name`,`scope_field`,`scope_class`,`scope_column`,`scope_type`,`scope_value`,`remark`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`,`table_name`,`user_column`,`tenant_id`,`user_entity_field`) values (1,'expandTable','test1','*','com.ruoyi.web.demo.expandtable.mapper.ExpandTableMapper.selectPage','dept_id',4,NULL,NULL,'4028ea815a3d2a8c015a3d2f8d2a0002','2019-12-03 07:30:19','4028ea815a3d2a8c015a3d2f8d2a0002','2020-05-29 22:54:32',0,'sys_dept','organizationId','00000000',NULL),(2,'table','test','id,title,author,type,level,content,`user.realname`,`user.username`,status','com.ruoyi.web.demo.table.mapper.TableMapper.selectPage','author',2,NULL,NULL,'4028ea815a3d2a8c015a3d2f8d2a0002','2019-11-29 15:18:25','4028ea815a3d2a8c015a3d2f8d2a0002','2020-10-31 12:01:59',0,'sys_dept','organization_id','00000000','dept_id');

/*Table structure for table `sys_dept` */

DROP TABLE IF EXISTS `sys_dept`;

CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

/*Data for the table `sys_dept` */

insert  into `sys_dept`(`dept_id`,`parent_id`,`ancestors`,`dept_name`,`order_num`,`leader`,`phone`,`email`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`tenant_id`) values (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:30','',NULL,'00000000'),(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:31','',NULL,'00000000'),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:31','',NULL,'00000000'),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:31','',NULL,'00000000'),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:32','',NULL,'00000000'),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:32','',NULL,'00000000'),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:33','',NULL,'00000000'),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:33','',NULL,'00000000'),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:33','',NULL,'00000000'),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2021-09-21 17:00:34','',NULL,'00000000'),(203,0,'0','Ruoyi-Vue-S',0,NULL,NULL,NULL,'0','0','','2021-10-12 15:56:31','',NULL,'48927269'),(204,203,'0,203','研发部门',0,NULL,NULL,NULL,'0','0','ruoyi','2021-10-19 15:16:37','',NULL,'48927269'),(205,0,'0,','测试',0,NULL,NULL,NULL,'0','0','','2021-11-11 10:37:56','',NULL,'15839669');

/*Table structure for table `sys_dict_data` */

DROP TABLE IF EXISTS `sys_dict_data`;

CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

/*Data for the table `sys_dict_data` */

insert  into `sys_dict_data`(`dict_code`,`dict_sort`,`dict_label`,`dict_value`,`dict_type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,1,'男','0','sys_user_sex','','','Y','0','admin','2021-09-21 17:01:53','',NULL,'性别男','00000000'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2021-09-21 17:01:53','',NULL,'性别女','00000000'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2021-09-21 17:01:54','',NULL,'性别未知','00000000'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2021-09-21 17:01:54','',NULL,'显示菜单','00000000'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2021-09-21 17:01:54','',NULL,'隐藏菜单','00000000'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2021-09-21 17:01:55','',NULL,'正常状态','00000000'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2021-09-21 17:01:55','',NULL,'停用状态','00000000'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2021-09-21 17:01:55','',NULL,'正常状态','00000000'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2021-09-21 17:01:56','',NULL,'停用状态','00000000'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2021-09-21 17:01:56','',NULL,'默认分组','00000000'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2021-09-21 17:01:56','',NULL,'系统分组','00000000'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2021-09-21 17:01:57','',NULL,'系统默认是','00000000'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2021-09-21 17:01:57','',NULL,'系统默认否','00000000'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2021-09-21 17:01:58','',NULL,'通知','00000000'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2021-09-21 17:01:58','',NULL,'公告','00000000'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2021-09-21 17:01:58','',NULL,'正常状态','00000000'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2021-09-21 17:01:59','',NULL,'关闭状态','00000000'),(18,1,'新增','1','sys_oper_type','','info','N','0','admin','2021-09-21 17:01:59','',NULL,'新增操作','00000000'),(19,2,'修改','2','sys_oper_type','','info','N','0','admin','2021-09-21 17:01:59','',NULL,'修改操作','00000000'),(20,3,'删除','3','sys_oper_type','','danger','N','0','admin','2021-09-21 17:02:00','',NULL,'删除操作','00000000'),(21,4,'授权','4','sys_oper_type','','primary','N','0','admin','2021-09-21 17:02:00','',NULL,'授权操作','00000000'),(22,5,'导出','5','sys_oper_type','','warning','N','0','admin','2021-09-21 17:02:01','',NULL,'导出操作','00000000'),(23,6,'导入','6','sys_oper_type','','warning','N','0','admin','2021-09-21 17:02:01','',NULL,'导入操作','00000000'),(24,7,'强退','7','sys_oper_type','','danger','N','0','admin','2021-09-21 17:02:01','',NULL,'强退操作','00000000'),(25,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2021-09-21 17:02:02','',NULL,'生成操作','00000000'),(26,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2021-09-21 17:02:02','',NULL,'清空操作','00000000'),(27,1,'成功','0','sys_common_status','','primary','N','0','admin','2021-09-21 17:02:02','',NULL,'正常状态','00000000'),(28,2,'失败','1','sys_common_status','','danger','N','0','admin','2021-09-21 17:02:03','',NULL,'停用状态','00000000'),(100,1,'全部','1','data_rule_type',NULL,'default','N','0','admin','2021-10-24 20:08:27','admin','2021-10-24 20:10:36',NULL,'00000000'),(101,2,'本人可见','2','data_rule_type',NULL,'default','N','0','admin','2021-10-24 20:08:47','admin','2021-10-24 20:09:58',NULL,'00000000'),(102,3,'所在机构可见','3','data_rule_type',NULL,'default','N','0','admin','2021-10-24 20:09:34','admin','2021-10-24 20:10:46',NULL,'00000000'),(103,4,'所在机构及子级可见','4','data_rule_type',NULL,'default','N','0','admin','2021-10-24 20:10:15','admin','2021-10-24 20:10:40',NULL,'00000000'),(104,5,'自定义','5','data_rule_type',NULL,'default','N','0','admin','2021-10-24 20:10:27','',NULL,NULL,'00000000');

/*Table structure for table `sys_dict_type` */

DROP TABLE IF EXISTS `sys_dict_type`;

CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

/*Data for the table `sys_dict_type` */

insert  into `sys_dict_type`(`dict_id`,`dict_name`,`dict_type`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,'用户性别','sys_user_sex','0','admin','2021-09-21 17:01:49','',NULL,'用户性别列表','00000000'),(2,'菜单状态','sys_show_hide','0','admin','2021-09-21 17:01:49','',NULL,'菜单状态列表','00000000'),(3,'系统开关','sys_normal_disable','0','admin','2021-09-21 17:01:49','',NULL,'系统开关列表','00000000'),(4,'任务状态','sys_job_status','0','admin','2021-09-21 17:01:50','',NULL,'任务状态列表','00000000'),(5,'任务分组','sys_job_group','0','admin','2021-09-21 17:01:50','',NULL,'任务分组列表','00000000'),(6,'系统是否','sys_yes_no','0','admin','2021-09-21 17:01:50','',NULL,'系统是否列表','00000000'),(7,'通知类型','sys_notice_type','0','admin','2021-09-21 17:01:51','',NULL,'通知类型列表','00000000'),(8,'通知状态','sys_notice_status','0','admin','2021-09-21 17:01:51','',NULL,'通知状态列表','00000000'),(9,'操作类型','sys_oper_type','0','admin','2021-09-21 17:01:51','',NULL,'操作类型列表','00000000'),(10,'系统状态','sys_common_status','0','admin','2021-09-21 17:01:52','',NULL,'登录状态列表','00000000'),(100,'数据权限类型','data_rule_type','0','admin','2021-10-24 20:07:28','',NULL,NULL,'00000000');

/*Table structure for table `sys_job` */

DROP TABLE IF EXISTS `sys_job`;

CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='定时任务调度表';

/*Data for the table `sys_job` */

insert  into `sys_job`(`job_id`,`job_name`,`job_group`,`invoke_target`,`cron_expression`,`misfire_policy`,`concurrent`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2021-09-21 17:02:07','',NULL,'','00000000'),(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2021-09-21 17:02:08','',NULL,'','00000000'),(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2021-09-21 17:02:08','',NULL,'','00000000');

/*Table structure for table `sys_job_log` */

DROP TABLE IF EXISTS `sys_job_log`;

CREATE TABLE `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务调度日志表';

/*Data for the table `sys_job_log` */

/*Table structure for table `sys_logininfor` */

DROP TABLE IF EXISTS `sys_logininfor`;

CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` text COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录';

/*Data for the table `sys_logininfor` */

insert  into `sys_logininfor`(`info_id`,`user_name`,`ipaddr`,`login_location`,`browser`,`os`,`status`,`msg`,`login_time`,`tenant_id`) values (100,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-09-21 17:04:41','00000000'),(101,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2021-09-21 17:09:21','00000000'),(102,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-09-21 17:09:27','00000000'),(103,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-09-21 18:03:06','00000000'),(104,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-08 14:38:59','00000000'),(105,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-08 14:39:09','00000000'),(106,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2021-10-08 14:44:54','00000000'),(107,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-08 14:44:59','00000000'),(108,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-08 14:50:49','00000000'),(109,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-09 09:38:46','00000000'),(110,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2021-10-09 12:03:11','00000000'),(111,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-09 12:03:15','00000000'),(112,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-10 12:58:40','00000000'),(113,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','验证码已失效','2021-10-10 14:45:29','00000000'),(114,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','验证码错误','2021-10-10 14:45:33','00000000'),(115,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-10 14:45:38','00000000'),(116,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-10 15:55:38','00000000'),(117,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-10 15:55:42','00000000'),(118,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','验证码错误','2021-10-10 15:55:47','00000000'),(119,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-10 15:55:50','00000000'),(120,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-10 15:57:14','00000000'),(121,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-10 15:58:50','00000000'),(122,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-10 15:58:56','00000000'),(123,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-10 15:59:38','00000000'),(124,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-10 16:01:12','00000000'),(125,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-10 16:02:32','00000000'),(126,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码错误','2021-10-10 16:02:52','00000000'),(127,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-10 16:02:57','00000000'),(128,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-11 21:31:19','00000000'),(129,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-11 22:30:43','00000000'),(130,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-11 23:18:16','00000000'),(131,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-12 14:35:51','00000000'),(132,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-12 17:29:00','00000000'),(133,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-12 18:07:11','00000000'),(134,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-12 18:07:20','00000000'),(135,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-12 18:07:21','00000000'),(136,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 14:18:44','00000000'),(137,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 14:24:45','00000000'),(138,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 14:25:11','00000000'),(139,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码错误','2021-10-19 14:25:35','00000000'),(140,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 14:25:40','00000000'),(141,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2021-10-19 14:30:27','00000000'),(142,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 14:30:31','00000000'),(143,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2021-10-19 14:41:23','00000000'),(144,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 14:41:28','00000000'),(145,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-19 14:44:45','00000000'),(146,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 14:44:54','00000000'),(147,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-19 14:45:01','00000000'),(148,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 14:45:08','00000000'),(149,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-19 14:45:09','00000000'),(150,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 14:47:47','00000000'),(151,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-19 14:47:57','00000000'),(152,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码错误','2021-10-19 14:48:08','00000000'),(153,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 14:48:15','00000000'),(154,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-19 14:48:25','00000000'),(155,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 14:48:33','00000000'),(156,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-19 14:57:08','00000000'),(157,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 14:57:17','00000000'),(158,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-19 15:01:49','00000000'),(159,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 15:01:59','00000000'),(160,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-19 15:08:52','00000000'),(161,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 15:09:01','00000000'),(162,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 15:59:24','00000000'),(163,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 16:01:19','00000000'),(164,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 16:11:50','00000000'),(165,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 16:16:19','00000000'),(166,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 16:19:52','00000000'),(167,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2021-10-19 16:24:23','00000000'),(168,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 16:24:28','00000000'),(169,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码错误','2021-10-19 16:26:10','00000000'),(170,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 16:27:41','00000000'),(171,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码错误','2021-10-19 16:30:55','00000000'),(172,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 16:31:00','00000000'),(173,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 16:32:34','00000000'),(174,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2021-10-19 16:33:58','00000000'),(175,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 16:39:05','00000000'),(176,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error querying database.  Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常\r\n### The error may exist in file [D:\\code\\research\\RuoYi-Vue-S\\ruoyi-system\\target\\classes\\mapper\\system\\SysUserMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUserMapper.selectUserByUserName\r\n### The error occurred while executing a query\r\n### Cause: com.ruoyi.common.exception.ServiceException: 获取用户信息异常','2021-10-19 16:46:20','00000000'),(177,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2021-10-19 16:54:19','00000000'),(178,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-19 16:54:33','00000000'),(179,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-20 11:23:22','00000000'),(180,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-21 20:42:56','00000000'),(181,'admin','127.0.0.1','内网IP','Chrome','Windows 10','0','登录成功','2021-10-22 11:31:35','00000000'),(182,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-22 11:54:54','00000000'),(183,'admin','127.0.0.1','内网IP','Chrome','Windows 10','0','登录成功','2021-10-22 13:21:17','00000000'),(184,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-22 13:22:41','00000000'),(185,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-22 14:09:40','00000000'),(186,'admin','127.0.0.1','内网IP','Chrome','Windows 10','0','登录成功','2021-10-22 14:21:07','00000000'),(187,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-22 16:13:36','00000000'),(188,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-22 22:35:00','00000000'),(189,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-22 22:55:32','00000000'),(190,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-23 10:45:53','00000000'),(191,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-23 11:31:35','00000000'),(192,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-23 17:05:57','00000000'),(193,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-23 22:32:24','00000000'),(194,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-24 12:04:01','00000000'),(195,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-24 12:59:55','00000000'),(196,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-24 19:46:35','00000000'),(197,'admin','127.0.0.1','内网IP','Chrome','Windows 10','0','登录成功','2021-10-25 09:35:56','00000000'),(198,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 10:43:27','00000000'),(199,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 11:36:04','00000000'),(200,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 11:36:13','00000000'),(201,'ruoyi','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 11:36:26','00000000'),(202,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 11:36:37','00000000'),(203,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 11:40:17','00000000'),(204,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 11:40:24','00000000'),(205,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 11:50:16','00000000'),(206,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码错误','2021-10-25 11:50:23','00000000'),(207,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码错误','2021-10-25 11:50:27','00000000'),(208,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 11:50:34','00000000'),(209,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 11:51:09','00000000'),(210,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 11:51:15','00000000'),(211,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 11:53:20','00000000'),(212,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 11:53:25','00000000'),(213,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 11:56:38','00000000'),(214,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 11:56:47','00000000'),(215,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 18:28:11','00000000'),(216,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 18:29:35','00000000'),(217,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 18:29:43','00000000'),(218,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 18:33:38','00000000'),(219,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 18:33:42','00000000'),(220,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 18:34:38','00000000'),(221,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 18:34:45','00000000'),(222,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 18:49:15','00000000'),(223,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 18:49:27','00000000'),(224,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 18:51:28','00000000'),(225,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 18:51:34','00000000'),(226,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 18:52:36','00000000'),(227,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 18:52:40','00000000'),(228,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 18:58:41','00000000'),(229,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 18:58:45','00000000'),(230,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-10-25 18:59:16','00000000'),(231,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-25 19:01:26','00000000'),(232,'ry','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-25 19:01:37','00000000'),(233,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-26 16:32:04','00000000'),(234,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-10-26 16:32:18','00000000'),(235,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-26 16:32:38','00000000'),(236,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-10-26 17:06:59','00000000'),(237,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-11-11 10:08:31','00000000'),(238,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-11-11 10:38:05','00000000'),(239,'test','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-11-11 10:38:14','00000000'),(240,'test','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-11-11 10:38:28','00000000'),(241,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-11-11 10:38:35','00000000'),(242,'admin','127.0.0.1','内网IP','Chrome','Windows 10','1','验证码错误','2021-11-15 14:24:13','00000000'),(243,'admin','127.0.0.1','内网IP','Chrome','Windows 10','0','登录成功','2021-11-15 14:24:17','00000000'),(244,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-11-15 14:37:26','00000000'),(245,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-12-23 18:52:25','00000000'),(246,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-12-24 14:21:33','00000000'),(247,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','验证码已失效','2022-01-15 16:45:56','00000000'),(248,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-15 16:46:01','00000000'),(249,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-15 18:59:47','00000000'),(250,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-15 19:31:08','00000000'),(251,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-15 21:39:21','00000000'),(252,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-15 23:13:43','00000000'),(253,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-16 13:16:21','00000000'),(254,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-16 15:31:07','00000000'),(255,'admin','192.168.1.4','内网IP','Chrome 9','Windows 10','1','验证码错误','2022-01-16 17:13:07','00000000'),(256,'admin','192.168.1.4','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-16 17:13:10','00000000'),(257,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-16 17:24:35','00000000'),(258,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-16 18:01:10','00000000'),(259,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-16 19:18:54','00000000'),(260,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-16 20:24:58','00000000'),(261,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-01-16 20:37:56','00000000');

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1482616947476238340 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,'系统管理',0,1,'system',NULL,'',1,0,'M','0','0','','system','admin','2021-09-21 17:00:40','',NULL,'系统管理目录','00000000'),(2,'系统监控',0,2,'monitor',NULL,'',1,0,'M','0','0','','monitor','admin','2021-09-21 17:00:41','',NULL,'系统监控目录','00000000'),(3,'系统工具',0,3,'tool',NULL,'',1,0,'M','0','0','','tool','admin','2021-09-21 17:00:41','',NULL,'系统工具目录','00000000'),(4,'若依官网',0,5,'http://ruoyi.vip',NULL,'',0,0,'M','0','0','','guide','admin','2021-09-21 17:00:41','ry','2021-10-25 19:02:36','若依官网地址','00000000'),(100,'用户管理',1,1,'user','system/user/index','',1,0,'C','0','0','system:user:list','user','admin','2021-09-21 17:00:42','',NULL,'用户管理菜单','00000000'),(101,'角色管理',1,2,'role','system/role/index','',1,0,'C','0','0','system:role:list','peoples','admin','2021-09-21 17:00:42','',NULL,'角色管理菜单','00000000'),(102,'菜单管理',1,3,'menu','system/menu/index','',1,0,'C','0','0','system:menu:list','tree-table','admin','2021-09-21 17:00:42','',NULL,'菜单管理菜单','00000000'),(103,'部门管理',1,4,'dept','system/dept/index','',1,0,'C','0','0','system:dept:list','tree','admin','2021-09-21 17:00:43','',NULL,'部门管理菜单','00000000'),(104,'岗位管理',1,5,'post','system/post/index','',1,0,'C','0','0','system:post:list','post','admin','2021-09-21 17:00:43','',NULL,'岗位管理菜单','00000000'),(105,'字典管理',1,6,'dict','system/dict/index','',1,0,'C','0','0','system:dict:list','dict','admin','2021-09-21 17:00:44','',NULL,'字典管理菜单','00000000'),(106,'参数设置',1,7,'config','system/config/index','',1,0,'C','0','0','system:config:list','edit','admin','2021-09-21 17:00:44','',NULL,'参数设置菜单','00000000'),(107,'通知公告',1,8,'notice','system/notice/index','',1,0,'C','0','0','system:notice:list','message','admin','2021-09-21 17:00:44','',NULL,'通知公告菜单','00000000'),(108,'日志管理',1,9,'log','','',1,0,'M','0','0','','log','admin','2021-09-21 17:00:45','',NULL,'日志管理菜单','00000000'),(109,'在线用户',2,1,'online','monitor/online/index','',1,0,'C','0','0','monitor:online:list','online','admin','2021-09-21 17:00:45','',NULL,'在线用户菜单','00000000'),(110,'定时任务',2,2,'job','monitor/job/index','',1,0,'C','0','0','monitor:job:list','job','admin','2021-09-21 17:00:45','',NULL,'定时任务菜单','00000000'),(111,'数据监控',2,3,'druid','monitor/druid/index','',1,0,'C','0','0','monitor:druid:list','druid','admin','2021-09-21 17:00:46','',NULL,'数据监控菜单','00000000'),(112,'服务监控',2,4,'server','monitor/server/index','',1,0,'C','0','0','monitor:server:list','server','admin','2021-09-21 17:00:46','',NULL,'服务监控菜单','00000000'),(113,'缓存监控',2,5,'cache','monitor/cache/index','',1,0,'C','0','0','monitor:cache:list','redis','admin','2021-09-21 17:00:47','',NULL,'缓存监控菜单','00000000'),(114,'表单构建',3,2,'build','tool/build/index','',1,0,'C','0','0','tool:build:list','build','admin','2021-09-21 17:00:47','admin','2022-01-16 20:45:06','表单构建菜单','00000000'),(115,'代码生成',3,3,'gen','tool/gen/index','',1,0,'C','0','0','tool:gen:list','code','admin','2021-09-21 17:00:47','admin','2022-01-16 20:45:22','代码生成菜单','00000000'),(116,'系统接口',3,4,'swagger','tool/swagger/index','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2021-09-21 17:00:48','admin','2022-01-16 20:45:31','系统接口菜单','00000000'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','',1,0,'C','0','0','monitor:operlog:list','form','admin','2021-09-21 17:00:48','',NULL,'操作日志菜单','00000000'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2021-09-21 17:00:48','',NULL,'登录日志菜单','00000000'),(1001,'用户查询',100,1,'','','',1,0,'F','0','0','system:user:query','#','admin','2021-09-21 17:00:49','',NULL,'','00000000'),(1002,'用户新增',100,2,'','','',1,0,'F','0','0','system:user:add','#','admin','2021-09-21 17:00:49','',NULL,'','00000000'),(1003,'用户修改',100,3,'','','',1,0,'F','0','0','system:user:edit','#','admin','2021-09-21 17:00:50','',NULL,'','00000000'),(1004,'用户删除',100,4,'','','',1,0,'F','0','0','system:user:remove','#','admin','2021-09-21 17:00:50','',NULL,'','00000000'),(1005,'用户导出',100,5,'','','',1,0,'F','0','0','system:user:export','#','admin','2021-09-21 17:00:50','',NULL,'','00000000'),(1006,'用户导入',100,6,'','','',1,0,'F','0','0','system:user:import','#','admin','2021-09-21 17:00:51','',NULL,'','00000000'),(1007,'重置密码',100,7,'','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2021-09-21 17:00:51','',NULL,'','00000000'),(1008,'角色查询',101,1,'','','',1,0,'F','0','0','system:role:query','#','admin','2021-09-21 17:00:51','',NULL,'','00000000'),(1009,'角色新增',101,2,'','','',1,0,'F','0','0','system:role:add','#','admin','2021-09-21 17:00:52','',NULL,'','00000000'),(1010,'角色修改',101,3,'','','',1,0,'F','0','0','system:role:edit','#','admin','2021-09-21 17:00:52','',NULL,'','00000000'),(1011,'角色删除',101,4,'','','',1,0,'F','0','0','system:role:remove','#','admin','2021-09-21 17:00:52','',NULL,'','00000000'),(1012,'角色导出',101,5,'','','',1,0,'F','0','0','system:role:export','#','admin','2021-09-21 17:00:53','',NULL,'','00000000'),(1013,'菜单查询',102,1,'','','',1,0,'F','0','0','system:menu:query','#','admin','2021-09-21 17:00:53','',NULL,'','00000000'),(1014,'菜单新增',102,2,'','','',1,0,'F','0','0','system:menu:add','#','admin','2021-09-21 17:00:53','',NULL,'','00000000'),(1015,'菜单修改',102,3,'','','',1,0,'F','0','0','system:menu:edit','#','admin','2021-09-21 17:00:54','',NULL,'','00000000'),(1016,'菜单删除',102,4,'','','',1,0,'F','0','0','system:menu:remove','#','admin','2021-09-21 17:00:54','',NULL,'','00000000'),(1017,'部门查询',103,1,'','','',1,0,'F','0','0','system:dept:query','#','admin','2021-09-21 17:00:55','',NULL,'','00000000'),(1018,'部门新增',103,2,'','','',1,0,'F','0','0','system:dept:add','#','admin','2021-09-21 17:00:55','',NULL,'','00000000'),(1019,'部门修改',103,3,'','','',1,0,'F','0','0','system:dept:edit','#','admin','2021-09-21 17:00:55','',NULL,'','00000000'),(1020,'部门删除',103,4,'','','',1,0,'F','0','0','system:dept:remove','#','admin','2021-09-21 17:00:56','',NULL,'','00000000'),(1021,'岗位查询',104,1,'','','',1,0,'F','0','0','system:post:query','#','admin','2021-09-21 17:00:56','',NULL,'','00000000'),(1022,'岗位新增',104,2,'','','',1,0,'F','0','0','system:post:add','#','admin','2021-09-21 17:00:56','',NULL,'','00000000'),(1023,'岗位修改',104,3,'','','',1,0,'F','0','0','system:post:edit','#','admin','2021-09-21 17:00:57','',NULL,'','00000000'),(1024,'岗位删除',104,4,'','','',1,0,'F','0','0','system:post:remove','#','admin','2021-09-21 17:00:57','',NULL,'','00000000'),(1025,'岗位导出',104,5,'','','',1,0,'F','0','0','system:post:export','#','admin','2021-09-21 17:00:58','',NULL,'','00000000'),(1026,'字典查询',105,1,'#','','',1,0,'F','0','0','system:dict:query','#','admin','2021-09-21 17:00:58','',NULL,'','00000000'),(1027,'字典新增',105,2,'#','','',1,0,'F','0','0','system:dict:add','#','admin','2021-09-21 17:00:58','',NULL,'','00000000'),(1028,'字典修改',105,3,'#','','',1,0,'F','0','0','system:dict:edit','#','admin','2021-09-21 17:00:59','',NULL,'','00000000'),(1029,'字典删除',105,4,'#','','',1,0,'F','0','0','system:dict:remove','#','admin','2021-09-21 17:00:59','',NULL,'','00000000'),(1030,'字典导出',105,5,'#','','',1,0,'F','0','0','system:dict:export','#','admin','2021-09-21 17:00:59','',NULL,'','00000000'),(1031,'参数查询',106,1,'#','','',1,0,'F','0','0','system:config:query','#','admin','2021-09-21 17:01:00','',NULL,'','00000000'),(1032,'参数新增',106,2,'#','','',1,0,'F','0','0','system:config:add','#','admin','2021-09-21 17:01:00','',NULL,'','00000000'),(1033,'参数修改',106,3,'#','','',1,0,'F','0','0','system:config:edit','#','admin','2021-09-21 17:01:01','',NULL,'','00000000'),(1034,'参数删除',106,4,'#','','',1,0,'F','0','0','system:config:remove','#','admin','2021-09-21 17:01:01','',NULL,'','00000000'),(1035,'参数导出',106,5,'#','','',1,0,'F','0','0','system:config:export','#','admin','2021-09-21 17:01:01','',NULL,'','00000000'),(1036,'公告查询',107,1,'#','','',1,0,'F','0','0','system:notice:query','#','admin','2021-09-21 17:01:02','',NULL,'','00000000'),(1037,'公告新增',107,2,'#','','',1,0,'F','0','0','system:notice:add','#','admin','2021-09-21 17:01:02','',NULL,'','00000000'),(1038,'公告修改',107,3,'#','','',1,0,'F','0','0','system:notice:edit','#','admin','2021-09-21 17:01:02','',NULL,'','00000000'),(1039,'公告删除',107,4,'#','','',1,0,'F','0','0','system:notice:remove','#','admin','2021-09-21 17:01:03','',NULL,'','00000000'),(1040,'操作查询',500,1,'#','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2021-09-21 17:01:03','',NULL,'','00000000'),(1041,'操作删除',500,2,'#','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2021-09-21 17:01:03','',NULL,'','00000000'),(1042,'日志导出',500,4,'#','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2021-09-21 17:01:04','',NULL,'','00000000'),(1043,'登录查询',501,1,'#','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2021-09-21 17:01:04','',NULL,'','00000000'),(1044,'登录删除',501,2,'#','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2021-09-21 17:01:04','',NULL,'','00000000'),(1045,'日志导出',501,3,'#','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2021-09-21 17:01:05','',NULL,'','00000000'),(1046,'在线查询',109,1,'#','','',1,0,'F','0','0','monitor:online:query','#','admin','2021-09-21 17:01:05','',NULL,'','00000000'),(1047,'批量强退',109,2,'#','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2021-09-21 17:01:06','',NULL,'','00000000'),(1048,'单条强退',109,3,'#','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2021-09-21 17:01:06','',NULL,'','00000000'),(1049,'任务查询',110,1,'#','','',1,0,'F','0','0','monitor:job:query','#','admin','2021-09-21 17:01:06','',NULL,'','00000000'),(1050,'任务新增',110,2,'#','','',1,0,'F','0','0','monitor:job:add','#','admin','2021-09-21 17:01:07','',NULL,'','00000000'),(1051,'任务修改',110,3,'#','','',1,0,'F','0','0','monitor:job:edit','#','admin','2021-09-21 17:01:07','',NULL,'','00000000'),(1052,'任务删除',110,4,'#','','',1,0,'F','0','0','monitor:job:remove','#','admin','2021-09-21 17:01:07','',NULL,'','00000000'),(1053,'状态修改',110,5,'#','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2021-09-21 17:01:08','',NULL,'','00000000'),(1054,'任务导出',110,7,'#','','',1,0,'F','0','0','monitor:job:export','#','admin','2021-09-21 17:01:08','',NULL,'','00000000'),(1055,'生成查询',115,1,'#','','',1,0,'F','0','0','tool:gen:query','#','admin','2021-09-21 17:01:09','',NULL,'','00000000'),(1056,'生成修改',115,2,'#','','',1,0,'F','0','0','tool:gen:edit','#','admin','2021-09-21 17:01:09','',NULL,'','00000000'),(1057,'生成删除',115,3,'#','','',1,0,'F','0','0','tool:gen:remove','#','admin','2021-09-21 17:01:09','',NULL,'','00000000'),(1058,'导入代码',115,2,'#','','',1,0,'F','0','0','tool:gen:import','#','admin','2021-09-21 17:01:10','',NULL,'','00000000'),(1059,'预览代码',115,4,'#','','',1,0,'F','0','0','tool:gen:preview','#','admin','2021-09-21 17:01:10','',NULL,'','00000000'),(1060,'生成代码',115,5,'#','','',1,0,'F','0','0','tool:gen:code','#','admin','2021-09-21 17:01:10','',NULL,'','00000000'),(2000,'租户管理',1,0,'tenant','system/tenant/index',NULL,1,0,'C','0','0','system:tenant:list','theme','admin','2021-10-10 16:17:46','admin','2021-10-11 22:32:32','','00000000'),(2001,'租户查询',2000,1,'','','',1,0,'F','0','0','system:tenant:query','#','admin','2021-09-21 17:00:51','admin','2021-10-11 22:38:17','','00000000'),(2002,'租户新增',2000,2,'','','',1,0,'F','0','0','system:tenant:add','#','admin','2021-09-21 17:00:52','admin','2021-10-11 22:38:21','','00000000'),(2003,'租户修改',2000,3,'','','',1,0,'F','0','0','system:tenant:edit','#','admin','2021-09-21 17:00:52','admin','2021-10-11 22:38:26','','00000000'),(2004,'租户删除',2000,4,'','','',1,0,'F','0','0','system:tenant:remove','#','admin','2021-09-21 17:00:52','admin','2021-10-11 22:38:32','','00000000'),(2005,'租户导出',2000,5,'','','',1,0,'F','0','0','system:tenant:export','#','admin','2021-09-21 17:00:53','admin','2021-10-11 22:38:37','','00000000'),(2006,'数据权限',1,10,'datarule','system/dataRule/index',NULL,1,0,'C','0','0','system:dataRule:list','validCode','admin','2021-10-20 11:28:20','admin','2021-10-20 11:29:02','','00000000'),(2007,'数据权限查询',2006,1,'','','',1,0,'F','0','0','system:dataRule:query','#','admin','2021-09-21 17:00:51','admin','2021-10-11 22:38:17','','00000000'),(2008,'数据权限新增',2006,2,'','','',1,0,'F','0','0','system:dataRule:add','#','admin','2021-09-21 17:00:52','admin','2021-10-11 22:38:21','','00000000'),(2009,'数据权限修改',2006,3,'','','',1,0,'F','0','0','system:dataRule:edit','#','admin','2021-09-21 17:00:52','admin','2021-10-11 22:38:26','','00000000'),(2010,'数据权限删除',2006,4,'','','',1,0,'F','0','0','system:dataRule:remove','#','admin','2021-09-21 17:00:52','admin','2021-10-11 22:38:32','','00000000'),(2011,'数据权限导出',2006,5,'','','',1,0,'F','0','0','system:dataRule:export','#','admin','2021-09-21 17:00:53','admin','2021-10-11 22:38:37','','00000000'),(2012,'综合表格',2018,1,'table','demo/table/index',NULL,1,0,'C','0','0','demo:table:list','#','admin','2021-10-21 20:55:54','admin','2021-10-21 20:57:40','综合表格菜单','00000000'),(2013,'综合表格查询',2012,1,'#','',NULL,1,0,'F','0','0','demo:table:query','#','admin','2021-10-21 20:55:55','',NULL,'','00000000'),(2014,'综合表格新增',2012,2,'#','',NULL,1,0,'F','0','0','demo:table:add','#','admin','2021-10-21 20:55:55','',NULL,'','00000000'),(2015,'综合表格修改',2012,3,'#','',NULL,1,0,'F','0','0','demo:table:edit','#','admin','2021-10-21 20:55:55','',NULL,'','00000000'),(2016,'综合表格删除',2012,4,'#','',NULL,1,0,'F','0','0','demo:table:remove','#','admin','2021-10-21 20:55:56','',NULL,'','00000000'),(2017,'综合表格导出',2012,5,'#','',NULL,1,0,'F','0','0','demo:table:export','#','admin','2021-10-21 20:55:56','',NULL,'','00000000'),(2018,'代码样例',0,4,'demo',NULL,NULL,1,0,'M','0','0','','code','admin','2021-10-21 20:56:58','ry','2021-10-25 19:02:31','','00000000'),(2019,'树形表格',2018,3,'treeTable','demo/treeTable/index',NULL,1,0,'C','0','0','demo:treeTable:list','#','admin','2021-10-22 14:18:59','ry','2021-10-25 19:02:09','树形表格菜单','00000000'),(2020,'树形表格查询',2019,1,'#','',NULL,1,0,'F','0','0','demo:treeTable:query','#','admin','2021-10-22 14:19:00','',NULL,'','00000000'),(2021,'树形表格新增',2019,2,'#','',NULL,1,0,'F','0','0','demo:treeTable:add','#','admin','2021-10-22 14:19:01','',NULL,'','00000000'),(2022,'树形表格修改',2019,3,'#','',NULL,1,0,'F','0','0','demo:treeTable:edit','#','admin','2021-10-22 14:19:01','',NULL,'','00000000'),(2023,'树形表格删除',2019,4,'#','',NULL,1,0,'F','0','0','demo:treeTable:remove','#','admin','2021-10-22 14:19:02','',NULL,'','00000000'),(2024,'树形表格导出',2019,5,'#','',NULL,1,0,'F','0','0','demo:treeTable:export','#','admin','2021-10-22 14:19:02','',NULL,'','00000000'),(2025,'展开表格',2018,2,'expandTable','demo/expandTable/index',NULL,1,0,'C','0','0','demo:expandTable:list','#','admin','2021-10-22 23:01:27','ry','2021-10-25 19:02:04','展开表格菜单','00000000'),(2026,'展开表格查询',2025,1,'#','',NULL,1,0,'F','0','0','demo:expandTable:query','#','admin','2021-10-22 23:01:28','',NULL,'','00000000'),(2027,'展开表格新增',2025,2,'#','',NULL,1,0,'F','0','0','demo:expandTable:add','#','admin','2021-10-22 23:01:28','',NULL,'','00000000'),(2028,'展开表格修改',2025,3,'#','',NULL,1,0,'F','0','0','demo:expandTable:edit','#','admin','2021-10-22 23:01:29','',NULL,'','00000000'),(2029,'展开表格删除',2025,4,'#','',NULL,1,0,'F','0','0','demo:expandTable:remove','#','admin','2021-10-22 23:01:29','',NULL,'','00000000'),(2030,'展开表格导出',2025,5,'#','',NULL,1,0,'F','0','0','demo:expandTable:export','#','admin','2021-10-22 23:01:29','',NULL,'','00000000'),(2031,'级联表格',2018,4,'car','demo/twoTable/index',NULL,1,0,'C','0','0','demo:twotable:car:list','#','admin','2021-10-22 23:42:03','ry','2021-10-25 19:02:19','车辆品牌菜单','00000000'),(2032,'车辆品牌查询',2031,1,'#','',NULL,1,0,'F','0','0','demo:twotable:car:query','#','admin','2021-10-22 23:42:04','',NULL,'','00000000'),(2033,'车辆品牌新增',2031,2,'#','',NULL,1,0,'F','0','0','demo:twotable:car:add','#','admin','2021-10-22 23:42:04','',NULL,'','00000000'),(2034,'车辆品牌修改',2031,3,'#','',NULL,1,0,'F','0','0','demo:twotable:car:edit','#','admin','2021-10-22 23:42:05','',NULL,'','00000000'),(2035,'车辆品牌删除',2031,4,'#','',NULL,1,0,'F','0','0','demo:twotable:car:remove','#','admin','2021-10-22 23:42:05','',NULL,'','00000000'),(2036,'车辆品牌导出',2031,5,'#','',NULL,1,0,'F','0','0','demo:twotable:car:export','#','admin','2021-10-22 23:42:05','',NULL,'','00000000'),(2037,'左树右表',2018,5,'treeandtable','demo/treeAndTable/index',NULL,1,0,'C','0','0','demo:treeandtable:list','#','admin','2021-10-23 12:01:35','admin','2021-10-23 12:11:48','左树右表菜单','00000000'),(2038,'左树右表查询',2037,1,'#','',NULL,1,0,'F','0','0','demo:treeandtable:query','#','admin','2021-10-23 12:01:36','',NULL,'','00000000'),(2039,'左树右表新增',2037,2,'#','',NULL,1,0,'F','0','0','demo:treeandtable:add','#','admin','2021-10-23 12:01:36','',NULL,'','00000000'),(2040,'左树右表修改',2037,3,'#','',NULL,1,0,'F','0','0','demo:treeandtable:edit','#','admin','2021-10-23 12:01:37','',NULL,'','00000000'),(2041,'左树右表删除',2037,4,'#','',NULL,1,0,'F','0','0','demo:treeandtable:remove','#','admin','2021-10-23 12:01:37','',NULL,'','00000000'),(2042,'左树右表导出',2037,5,'#','',NULL,1,0,'F','0','0','demo:treeandtable:export','#','admin','2021-10-23 12:01:37','',NULL,'','00000000'),(1482616947476238338,'仓颉平台',3,1,'http://sunseagear.com:8081/admin/','http://sunseagear.com:8081/admin/',NULL,0,0,'M','0','0','','component','admin','2022-01-16 20:41:36','admin','2022-01-16 20:47:57','','00000000'),(1482616947476238339,'仓颉介绍',3,0,'cangjie','documentation/index',NULL,1,0,'C','0','0','','log','admin','2022-01-16 20:44:29','admin','2022-01-16 20:48:08','','00000000');

/*Table structure for table `sys_notice` */

DROP TABLE IF EXISTS `sys_notice`;

CREATE TABLE `sys_notice` (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

/*Data for the table `sys_notice` */

insert  into `sys_notice`(`notice_id`,`notice_title`,`notice_type`,`notice_content`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,'温馨提醒：2018-07-01 若依新版本发布啦','2','新版本内容','0','admin','2021-09-21 17:02:10','',NULL,'管理员','00000000'),(2,'维护通知：2018-07-01 若依系统凌晨维护','1','维护内容','0','admin','2021-09-21 17:02:10','',NULL,'管理员','00000000');

/*Table structure for table `sys_oper_log` */

DROP TABLE IF EXISTS `sys_oper_log`;

CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`oper_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

/*Data for the table `sys_oper_log` */

/*Table structure for table `sys_post` */

DROP TABLE IF EXISTS `sys_post`;

CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='岗位信息表';

/*Data for the table `sys_post` */

insert  into `sys_post`(`post_id`,`post_code`,`post_name`,`post_sort`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,'ceo','董事长',1,'0','admin','2021-09-21 17:00:36','',NULL,'','00000000'),(2,'se','项目经理',2,'0','admin','2021-09-21 17:00:37','',NULL,'','00000000'),(3,'hr','人力资源',3,'0','admin','2021-09-21 17:00:37','',NULL,'','00000000'),(4,'user','普通员工',4,'0','admin','2021-09-21 17:00:38','',NULL,'','00000000');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`role_id`,`role_name`,`role_key`,`role_sort`,`data_scope`,`menu_check_strictly`,`dept_check_strictly`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2021-09-21 17:00:39','',NULL,'超级管理员','00000000'),(2,'租户管理员','tenantAdmin',2,'1',1,1,'0','0','admin','2021-10-08 15:16:18','admin','2021-10-25 18:34:18',NULL,'00000000'),(3,'普通角色','common',3,'2',1,1,'0','0','admin','2021-09-21 17:00:39','admin','2021-10-25 11:39:38','普通角色','00000000');

/*Table structure for table `sys_role_data_rule` */

DROP TABLE IF EXISTS `sys_role_data_rule`;

CREATE TABLE `sys_role_data_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_rule_category` int(2) DEFAULT NULL COMMENT '权限类型(1:数据权限、2:接口权限)',
  `data_rule_id` varchar(64) DEFAULT NULL COMMENT '权限id',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='角色数据权限关联表';

/*Data for the table `sys_role_data_rule` */

insert  into `sys_role_data_rule`(`id`,`data_rule_category`,`data_rule_id`,`role_id`) values (5,NULL,'2','2');

/*Table structure for table `sys_role_dept` */

DROP TABLE IF EXISTS `sys_role_dept`;

CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';

/*Data for the table `sys_role_dept` */

insert  into `sys_role_dept`(`role_id`,`dept_id`) values (2,100),(2,101),(2,105);

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`role_id`,`menu_id`) values (2,1),(2,2),(2,3),(2,4),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,114),(2,115),(2,116),(2,500),(2,501),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060),(2,2012),(2,2013),(2,2014),(2,2015),(2,2016),(2,2017),(2,2018),(2,2019),(2,2020),(2,2021),(2,2022),(2,2023),(2,2024),(2,2025),(2,2026),(2,2027),(2,2028),(2,2029),(2,2030),(2,2031),(2,2032),(2,2033),(2,2034),(2,2035),(2,2036),(2,2037),(2,2038),(2,2039),(2,2040),(2,2041),(2,2042),(3,2012),(3,2013),(3,2014),(3,2015),(3,2016),(3,2017),(3,2018),(3,2019),(3,2020),(3,2021),(3,2022),(3,2023),(3,2024),(3,2025),(3,2026),(3,2027),(3,2028),(3,2029),(3,2030),(3,2031),(3,2032),(3,2033),(3,2034),(3,2035),(3,2036),(3,2037),(3,2038),(3,2039),(3,2040),(3,2041),(3,2042),(100,1),(100,2),(100,3),(100,100),(100,101),(100,102),(100,103),(100,104),(100,105),(100,106),(100,107),(100,108),(100,109),(100,110),(100,111),(100,112),(100,113),(100,114),(100,115),(100,116),(100,500),(100,501),(100,1001),(100,1002),(100,1003),(100,1004),(100,1005),(100,1006),(100,1007),(100,1008),(100,1009),(100,1010),(100,1011),(100,1012),(100,1013),(100,1014),(100,1015),(100,1016),(100,1017),(100,1018),(100,1019),(100,1020),(100,1021),(100,1022),(100,1023),(100,1024),(100,1025),(100,1026),(100,1027),(100,1028),(100,1029),(100,1030),(100,1031),(100,1032),(100,1033),(100,1034),(100,1035),(100,1036),(100,1037),(100,1038),(100,1039),(100,1040),(100,1041),(100,1042),(100,1043),(100,1044),(100,1045),(100,1046),(100,1047),(100,1048),(100,1049),(100,1050),(100,1051),(100,1052),(100,1053),(100,1054),(100,1055),(100,1056),(100,1057),(100,1058),(100,1059),(100,1060);

/*Table structure for table `sys_tenant` */

DROP TABLE IF EXISTS `sys_tenant`;

CREATE TABLE `sys_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `del_flag` char(1) DEFAULT NULL COMMENT '删除标识',
  `remark` text COMMENT '备注',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户标识',
  `contact` varchar(64) NOT NULL COMMENT '联系人',
  `phone` varchar(64) NOT NULL COMMENT '电话',
  `name` varchar(64) NOT NULL COMMENT '租户名称',
  `user_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='租户管理';

/*Data for the table `sys_tenant` */

insert  into `sys_tenant`(`id`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`,`remark`,`tenant_id`,`contact`,`phone`,`name`,`user_id`) values (1,NULL,NULL,NULL,NULL,NULL,NULL,'00000000','sunseagear','1311111111','sunseagear','1'),(2,NULL,NULL,NULL,NULL,NULL,NULL,'48927269','若依','15888888888','Ruoyi-Vue-S','103'),(3,NULL,NULL,NULL,NULL,NULL,NULL,'15839669','测试','13111111111','测试','104');

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(8) NOT NULL DEFAULT '00000000',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_id`,`dept_id`,`user_name`,`nick_name`,`user_type`,`email`,`phonenumber`,`sex`,`avatar`,`password`,`status`,`del_flag`,`login_ip`,`login_date`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`,`tenant_id`) values (1,103,'admin','若依','00','ry@163.com','15888888888','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2022-01-16 20:37:55','admin','2021-09-21 17:00:35','','2022-01-16 20:37:56','管理员','00000000'),(2,105,'ry','若依','00','ry@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2021-10-25 19:01:39','admin','2021-09-21 17:00:35','admin','2021-10-25 19:01:37','测试员','00000000'),(103,203,'ruoyi','Ruoyi-Vue-S','00','','15888888888','0','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2021-10-25 11:36:13','','2021-10-12 15:56:31','','2021-10-25 11:36:13','租户管理员','48927269'),(104,NULL,'test','测试','00','','13111111111','0','','$2a$10$T0k7h.P/z9gy/nJuVdsvn.cJup9m7WllbqU4FxeCkOSJtEy72M8mi','0','0','127.0.0.1','2021-11-11 10:38:15','','2021-11-11 10:37:56','','2021-11-11 10:38:14',NULL,'15839669');

/*Table structure for table `sys_user_post` */

DROP TABLE IF EXISTS `sys_user_post`;

CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与岗位关联表';

/*Data for the table `sys_user_post` */

insert  into `sys_user_post`(`user_id`,`post_id`) values (1,1),(2,2);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`user_id`,`role_id`) values (1,1),(2,2),(2,100),(103,2),(104,2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
