# jun_base_elementplus 构建运行文档

## 主项目: Ruoyi-ArcoDesignPro-Vue ✅
- 路径: D:\workspace_github_v1\devops\jun_base_elementplus\Ruoyi-ArcoDesignPro-Vue
- 类型: RuoYi (SpringBoot 3 + JDK17) + Arco Design Pro Vue3 (Vite)
- 状态: ✅ 构建运行成功

## 数据库
- 库名: db1_aio（30 表）
- 脚本: sql/ry_20250417.sql + quartz.sql

## 配置修改记录
| 文件 | 修改内容 |
|------|---------|
| application-druid.yml | 192.168.10.102:3306/aio(用户aio) → 127.0.0.1:3307/db1_aio(用户root/mysqladmin) |
| application.yml | port: 8080 → 8084 |
| ruoyi-ui/src/views/tool/gen/components/import-table.vue | @arco-design/web-vue/es/table/interface 路径不存在 → import type { TableColumnData } from "@arco-design/web-vue" |

## 启动
- 后端: http://localhost:8084 (JDK17)
- 前端: http://localhost:8993 (Vite, 仅监听 IPv6 [::1], 浏览器访问 http://localhost:8993 正常)
- 账号: admin / admin123

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_elementplus_be.log / fe.log

## 同目录其他项目（未启动，均为独立模板）
- art-design-pro / Geeker-Admin / Geeker-Admin-Ruoyi / Ruoyi-ArtDesignPro-Vue / ruoyi-geek-vue3 / ruoyi-plus / RuoYi-SpringBoot3-ElementPlus / RuoYi-SpringBoot3-Pro / RuoYi-Vue3 / ruoyi-vue3-lmw(-cloud/-ts/-prettier) / youlai
