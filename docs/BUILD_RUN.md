# jun_base_bigscreen 构建运行文档

## 主项目: ruoyi-plus-big ✅
- 路径: D:\workspace_github_v1\devops\jun_base_bigscreen\ruoyi-plus-big
- 类型: RuoYi 3.9.0 (SpringBoot 3 + JDK17) + Vue3 大屏 (Vite 6)
- 状态: ✅ 构建运行成功

## 数据库
- 库名: db1_ry_plus_big（31 表）
- 脚本: sql/ruoyibig-20260127.sql + quartz.sql

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| application-druid.yml | 127.0.0.1:3306/ry-plus-big(root/root) → 127.0.0.1:3307/db1_ry_plus_big(root/mysqladmin) |

## 启动
- 后端: http://localhost:7777
- 前端: http://localhost:17777
- 账号: admin / admin123

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_bigscreen_be.log / fe.log
