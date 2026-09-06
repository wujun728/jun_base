# jun_base_elementui_springboot4 构建运行文档

## 项目信息
- 路径: D:\workspace_github_v1\devops\jun_base_elementui_springboot4\RuoYi-Vue-3.92-springboot4-jdk17
- 类型: RuoYi-Vue 3.92 (SpringBoot 4 + JDK17 + Vue2 ElementUI)
- 状态: ✅ 构建运行成功

## 数据库
- 库名: db1_ry_vue（与 jun_base_elementui 共用，49 表已有数据）

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| application-druid.yml | localhost:3307/ry-vue → 127.0.0.1:3307/db1_ry_vue |
| application.yml | port: 8080 → 8083 |

## 启动
- 后端: http://localhost:8083 (ruoyi-admin.jar, JDK17)
- 前端: http://localhost:82 (ruoyi-ui)
- 账号: admin / admin123

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_elementui_sb4_be.log / sb4_fe.log
