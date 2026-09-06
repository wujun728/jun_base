# FAQ

## 这个项目和 RuoYi-Vue 原版有什么区别？

Nexa Admin 保留 RuoYi 后端能力，使用 Vue 3 与 Art 风格组件体系替换传统前端体验。权限、菜单、字典、日志、文件上传、定时任务和代码生成仍然复用 RuoYi 体系。

## 是否重写了 RuoYi 权限系统？

没有。项目继续使用 RuoYi RBAC、JWT、菜单权限、按钮权限和数据权限。前端通过 `/getInfo`、`/getRouters` 和权限标识适配 Art 动态路由与按钮控制。

## 前端权限模式是什么？

使用后端权限模式：

```env
VITE_ACCESS_MODE = backend
```

菜单来自 RuoYi `/getRouters`，按钮权限来自 `/getInfo` 返回的 `permissions`。

## 代码生成有什么特别之处？

代码生成器新增 `Art Design Pro TypeScript` 模板。导入表时默认选择 `art-design-pro`，生成的前端页面直接复用 `ArtSearchBar`、`ArtTableHeader`、`ArtTable`、`ArtForm`、`useTable`、`useDict`、`DictTag`、上传字段、富文本字段和 `v-auth` 权限控制。

原有 Element Plus 模板仍然保留，可以在导入表或修改生成配置时切换。

## Art Bot 支持哪些模型？

首版支持 OpenAI Chat Completions 兼容协议。模型管理中配置 API Base URL、API Key、模型标识、温度、最大 Token 和系统提示词后即可使用。

后端会统一拼接 `/chat/completions`。暂不实现厂商专属协议、文件上传、图片理解和联网搜索。

## API Key 怎么保存？

当前版本 API Key 以明文写入数据库。管理接口不会回传明文，只会返回掩码。生产环境应限制数据库访问权限，并为模型服务配置最小权限 Key。

## 界面风格保存在哪里？

主题模式、主题色、菜单布局等界面风格按用户 ID 保存到 `sys_user_ui_setting`，不再写入全局 `sys_config`。系统标识、登录安全、站点文案和公共水印仍属于全局站点配置。

## 为什么查看操作日志不会再产生新的操作日志？

普通查询默认不审计，避免查看操作日志时产生递归噪声。缓存详情这类敏感读取会单独记录审计日志，但不会记录缓存值响应内容。

## 缓存监控权限怎么分配？

缓存权限拆成三类：

- `monitor:cache:list`：查看缓存概览、缓存名称和键名。
- `monitor:cache:query`：查看具体缓存值。
- `monitor:cache:remove`：清理单个键、缓存分类或全部缓存。

新增敏感权限不会自动授予普通角色，管理员可按需分配。

## 新环境怎么初始化数据库？

新环境直接导入：

```bash
mysql -uroot -p ry-vue < sql/ry_20260417.sql
```

已有 RuoYi 数据库请按 README 中的 SQL 顺序执行增量脚本。

## 默认账号是什么？

```text
账号：admin
密码：admin123
```

部署生产环境前请修改默认密码、数据库密码、Redis 配置和 JWT Secret。

## 前端默认端口是多少？

默认前端端口是 `3006`，后端默认端口是 `8080`。

```text
http://localhost:3006
```

## 如何反馈问题？

- Bug：提交 Issue，并附复现步骤、运行环境和日志。
- 功能建议：提交 Feature Request 或在 Discussions 讨论。
- 安全问题：不要公开提交 Issue，请参考 `SECURITY.md`。
