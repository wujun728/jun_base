/*
 Navicat Premium Dump SQL

 Source Server         : localhost-pgsql
 Source Server Type    : PostgreSQL
 Source Server Version : 170006 (170006)
 Source Host           : localhost:5432
 Source Catalog        : magicapi
 Source Schema         : magicapi

 Target Server Type    : PostgreSQL
 Target Server Version : 170006 (170006)
 File Encoding         : 65001

 Date: 16/09/2025 09:16:44
*/


-- ----------------------------
-- Table structure for magic_api_file
-- ----------------------------
DROP TABLE IF EXISTS "ruoyi"."magic_api_file";
CREATE TABLE "ruoyi"."magic_api_file" (
  "file_path" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "file_content" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of magic_api_file
-- ----------------------------

-- ----------------------------
-- Table structure for magic_backup_record
-- ----------------------------
DROP TABLE IF EXISTS "ruoyi"."magic_backup_record";
CREATE TABLE "ruoyi"."magic_backup_record" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "create_date" int8 NOT NULL,
  "tag" varchar(32) COLLATE "pg_catalog"."default",
  "type" varchar(32) COLLATE "pg_catalog"."default",
  "name" varchar(64) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "create_by" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "ruoyi"."magic_backup_record"."id" IS '原对象ID';
COMMENT ON COLUMN "ruoyi"."magic_backup_record"."create_date" IS '备份时间';
COMMENT ON COLUMN "ruoyi"."magic_backup_record"."tag" IS '标签';
COMMENT ON COLUMN "ruoyi"."magic_backup_record"."type" IS '类型';
COMMENT ON COLUMN "ruoyi"."magic_backup_record"."name" IS '原名称';
COMMENT ON COLUMN "ruoyi"."magic_backup_record"."content" IS '备份内容';
COMMENT ON COLUMN "ruoyi"."magic_backup_record"."create_by" IS '操作人';

-- ----------------------------
-- Records of magic_backup_record
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table magic_api_file
-- ----------------------------
ALTER TABLE "ruoyi"."magic_api_file" ADD CONSTRAINT "idx_magic_api_file_primary" PRIMARY KEY ("file_path");

-- ----------------------------
-- Primary Key structure for table magic_backup_record
-- ----------------------------
ALTER TABLE "ruoyi"."magic_backup_record" ADD CONSTRAINT "idx_magic_backup_record_primary" PRIMARY KEY ("id", "create_date");
