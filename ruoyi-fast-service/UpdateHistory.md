## 240804
1. 简化代码，去除service 接口，去除一些没必要校验。
2. 逻辑删除优化：0代表存在 1代表删除。
3. BaseEntity 优化 Date->LocalDateTime, createBy和updateBy保持String。
4. 状态修改，1代表正常，0代表停用。
5. 简化数据库支持，目前只支持mysql。
6. docker 暂时不支持。
7. 代码生成前端vue文件去掉一些不需要的方法。
8. 代码生成前端vue文件优化，增加el-card，表格样式修改。
9. system模块增加vo、bo。
10. 全局异常处理优化:增加文件名或填写内容过长提示。
11. service端baseMapper改类名小驼峰。
12. 代码生成，前端编辑页面由dialog改为drawer

## 240627
1. 删除 snail-job monitor-admin ruoyi-extend 基本用不到
2. 优化 获取aop代理的方式 减少与其他使用aop的功能冲突的概率

## 240620
1. 完成重构 common, framework 包结构, 参照 cloud 版本拆分子模块 ;
update 更新模块包名 ;
2. 使用 jdk17 语法优化代码
3. mapstruct-plus 接入替换 BeanUtil ,用于实体类转换 ; 原先BeanUtil 对jdk17实体类转换会出错。

## 240613
1.删除vue代码，vue相关代码重新建项目。