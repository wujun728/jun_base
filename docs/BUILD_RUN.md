# jun_base_naiveui 构建运行文档

## 主项目: ginyi-spring-vue ✅
- 路径: D:\workspace_github_v1\devops\jun_base_naiveui\ginyi-spring-vue
- 类型: Ginyi (SpringBoot 2 + JDK8 + Vue3 NaiveUI + MongoDB 可选)
- 状态: ✅ 构建运行成功（ginyi-server-admin 8066）

## 数据库
- 库名: db1_ginyi_spring_vue（17 表，MySQL）
- 脚本: sql/ginyi-spring-vue-mysql.sql
- MongoDB: 配置有 mongodb://127.0.0.1:27017/ginyi-spring-vue，本地未安装 MongoDB，启动时仅监控告警不影响运行

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| ginyi-system/.../application.yml | 127.0.0.1:3306/ginyi-spring-vue(root/123456) → 127.0.0.1:3307/db1_ginyi_spring_vue(root/mysqladmin) |

## 启动
- 后端 admin: http://localhost:8066 (ginyi-server-admin.jar)
- 后端 web: http://localhost:8055 (ginyi-server-web.jar，未启动)
- 前端: http://localhost:3800 (Vite 4, /index.html 200)
- 接口文档: http://localhost:8066/doc.html

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_naiveui_be.log / fe.log

## 待安装环境
- MongoDB 27017（若需完整功能）
