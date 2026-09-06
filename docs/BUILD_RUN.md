# jun_base_antd 构建运行文档

## 主项目: aidex-sharp ✅
- 路径: D:\workspace_github_v1\devops\jun_base_antd\aidex-sharp
- 类型: AiDex Sharp (RuoYi 派生, SpringBoot 2.5.14 + JDK8 + Vue2 Ant Design Pro)
- 状态: ✅ 构建运行成功

## 数据库
- 库名: db1_aidex（42 表）
- 脚本: sql/aidex.sql + quartz.sql

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| aidex-admin/.../application-druid.yml | localhost:3306/gitee(root/root) → 127.0.0.1:3307/db1_aidex(root/mysqladmin) |
| aidex-admin/.../application.yml | port: 8080 → 8086 |
| aidex-controller/.../SysIndexController.java | 移除不存在的 import com.aidex.framework.config.AnonymousAccess（实际类在 com.aidex.common.annotation 且本文件未使用） |
| aidex-ui/package.json | dev 脚本加 SET NODE_OPTIONS=--openssl-legacy-provider（Node 24 兼容） |

## 启动
- 后端: http://localhost:8086 (JDK8)
- 前端: http://localhost:83 (vue-cli, eslint 报 5 个风格错误但编译通过可访问)
- 账号: admin / admin123

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_antd_be.log / fe.log

## 同目录其他项目（未启动）
- AutoEE / bear-jia-vue3 / ruo-yi-antdv-flowable-plus / ruo-yi-vue-artd / RuoYi-Antdv / ruoyi-pritter-antd / ruoyi-uniapp
