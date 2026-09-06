# jun_base_elementplus_springboot4 构建运行文档

## 主项目: ruo-yi-vue-login ✅
- 路径: D:\workspace_github_v1\devops\jun_base_elementplus_springboot4\ruo-yi-vue-login
- 类型: RuoYi 3.9.2 (SpringBoot 4.0.3 + JDK17) + Vue3 ElementPlus (Vite 6)
- 状态: ✅ 构建运行成功

## 数据库
- 库名: db1_ry_login（31 表）
- 脚本: sql/ry_20260320.sql + quartz.sql

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| application-druid.yml | localhost:3306/ry-vue(root/123456) → 127.0.0.1:3307/db1_ry_login(root/mysqladmin) |
| application.yml | port: 8080 → 8085 |

## 启动
- 后端: http://localhost:8085
- 前端: http://localhost:80 (Vite 6, dev)
- 账号: admin / admin123

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_eplus4_be.log / fe.log

## 同目录其他项目
- ruoyi-geek（含 ruoyi-geek-app / ruoyi-geek-vue3，未单独启动）
