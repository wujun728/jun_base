# jun_base_plus 构建运行文档

## 主项目: ruoyi-fast-service ✅
- 路径: D:\workspace_github_v1\devops\jun_base_plus\ruoyi-fast-service
- 类型: RuoYi-Vue-Plus 5.2.0 精简版 (SpringBoot 3.2.6 + JDK17 + Sa-Token + Undertow + Vue3 Vite)
- 状态: ✅ 构建运行成功

## 数据库
- 库名: db1_ruoyi_fast（19 表）
- 脚本: script/sql/ruoyi_fast.sql

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| ruoyi-admin/.../application-dev.yml | localhost:3306/ry-vue(root/root) → 127.0.0.1:3307/db1_ruoyi_fast(root/mysqladmin) |
| ruoyi-admin/.../application-dev.yml | 删除 redis ssl: false（SpringBoot 3.2 中 ssl 属性类型变更导致绑定失败） |
| ruoyi-admin/.../application.yml | port 8080 → 8088 |
| ruoyi-common-log 模块 | **源码缺失**(仅剩 META-INF)，从 RuoYi-Vue-Plus-Single 复制 Log/LogAspect/BusinessType 等 7 个 Java 文件并调整包名；补 SystemConstants.java；StopWatch.getDuration()→getTime()（commons-lang3 版本差异） |

## 启动
- 后端: http://localhost:8088
- 前端: http://localhost:81 (Vite, 80 被占用自动跳 81)
- 账号: admin / admin123

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_plus_be.log / fe.log

## 同目录其他项目（未启动）
- jun_ui_sa_admin / ruoyi-fast-uniapp / RuoYi-Vue-Plus-Single（作为 ruoyi-common-log 源码补齐来源）
