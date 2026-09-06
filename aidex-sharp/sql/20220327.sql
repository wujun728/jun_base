-- 代码生成器增加展示列数
ALTER TABLE `gen_table`
ADD COLUMN `col_spans`  int(10) NULL COMMENT '展示列数' AFTER `gen_type`;
-- 代码生成器增加是否排序字段
ALTER TABLE `gen_table_column`
ADD COLUMN `is_column_sort`  char(10) NULL COMMENT '是否排序' AFTER `is_log`;

