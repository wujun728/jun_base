# jun_base_avue 构建运行文档

## 主项目: RuoYi-AVue-Plus ✅
- 路径: D:\workspace_github_v1\devops\jun_base_avue\RuoYi-AVue-Plus
- 类型: RuoYi + Avue (SpringBoot 2 + JDK8 + Vue2 Avue)
- 状态: ✅ 构建运行成功

## 数据库
- 库名: db1_avue（30 表）
- 脚本: sql/ry_20210731.sql + quartz.sql

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| ruoyi-admin/.../application-dev.yml | 192.168.0.11:3306/ry(root/Elab@123) → 127.0.0.1:3307/db1_avue(root/mysqladmin) |
| ruoyi-admin/.../application-dev.yml | redis host 192.168.0.11 → localhost; password elab!264ELAB → 空（本地无密码） |

## 启动
- 后端: http://localhost:8768 (JDK8)
- 前端: http://localhost:84
- 账号: admin / admin123

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_avue_be.log / fe.log
