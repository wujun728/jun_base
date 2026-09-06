# jun_base_mini 构建运行文档

## 主项目: RuoYi-Split-Study ✅
- 路径: D:\workspace_github_v1\devops\jun_base_mini\RuoYi-Split-Study
- 类型: RuoYi 拆分学习版 (SpringBoot 2.5.14 + JDK8 + Vue2)
- 状态: ✅ 构建运行成功

## 数据库
- 库名: db1_ruoyi_split（30 表）
- 脚本: sql/ry_20220822.sql + quartz.sql

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| application-druid.yml | localhost:3306/ruoyi(root/root) → 127.0.0.1:3307/db1_ruoyi_split(root/mysqladmin) |
| application.yml | port 8080 → 8087; redis password root → 空 |
| ruoyi-ui/package.json | dev 脚本加 openssl-legacy-provider（Node 24） |

## 启动
- 后端: http://localhost:8087
- 前端: http://localhost:85
- 账号: admin / admin123

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_mini_be.log / fe.log

## 同目录其他项目（未启动）
- ruoyi-geek-mini / RuoYi-Sqlite / RuoYi-Vue-Plus-Mini / RuoYi-Vue-Single / ruoyi-vue-sqlite-no-redis（SQLite 变体无 MySQL 依赖）
