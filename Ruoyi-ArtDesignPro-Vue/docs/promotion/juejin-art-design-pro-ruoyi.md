# Nexa Admin：RBAC、代码生成、监控和 AI 助手都保留

大家好，这次开源一个后台管理项目：**Nexa Admin**。

项目地址：https://github.com/Searchld/Nexa-Admin

演示地址：https://art.beta.kim/

演示账号：

```text
用户名：admin
密码：admin123
```

## 为什么做这个项目

RuoYi 的后端能力很稳，RBAC、菜单权限、按钮权限、数据权限、字典、日志、代码生成、定时任务、缓存监控这些能力都很完整。

但传统 RuoYi-Vue 的界面风格已经比较旧。如果直接换一套前端，又容易把 RuoYi 的权限体系、菜单模型和代码生成能力打散。

所以这个项目的目标很明确：

- 后端继续复用 RuoYi。
- 权限继续走 RuoYi RBAC。
- 动态菜单继续来自 `/getRouters`。
- 按钮权限继续使用 RuoYi 权限标识。
- 前端换成 Vue 3 + TypeScript + Art 风格组件体系的现代化界面。

换句话说，不重写 RuoYi，而是把 RuoYi 的成熟业务底座接到更现代的 Vue 3 管理后台上。

## 目前已经接入的能力

### 1. RuoYi 权限体系完整保留

登录、用户信息、动态路由和按钮权限都复用 RuoYi 原接口：

- `POST /login`
- `GET /getInfo`
- `GET /getRouters`
- `POST /logout`

前端使用后端权限模式，菜单由 RuoYi 菜单表转换为 Art 动态路由，按钮权限继续支持 `system:user:add`、`monitor:cache:remove` 这类权限标识。

### 2. 系统管理和系统监控页面适配 Art 风格

已适配用户、角色、菜单、部门、岗位、字典、参数、公告、在线用户、操作日志、登录日志、定时任务、服务监控、缓存监控等页面。

页面尽量复用 Art 项目内的表格、搜索、表单、抽屉和权限封装，不按传统 RuoYi-Vue 风格重写。

### 3. 代码生成适配新模板

这块是重点。

项目保留 RuoYi 原来的代码生成能力，同时新增 **Art Design Pro TypeScript 前端模板**。

导入业务表后，可以生成符合当前 Art 前端结构的页面代码，生成结果直接复用：

- Art 表格
- Art 搜索栏
- Art 表单
- 抽屉表单
- RuoYi 按钮权限
- 字典渲染
- 导入导出封装

这比只做 UI 迁移更有价值，因为后续业务模块可以继续用 RuoYi 的生成器提效。

### 4. 接入了右上角 Art Bot AI 助手

右上角 Art Bot 已经接入 OpenAI Chat Completions 兼容协议。

支持：

- 后台配置多个模型
- 设置默认模型
- 测试连接
- SSE 流式输出
- 按用户保存历史会话
- Markdown 安全渲染
- 独立 `artbot:chat:use` 使用权限

模型管理也走 RuoYi 权限，普通用户可以只拥有聊天权限，不需要拥有模型管理权限。

### 5. 用户级主题配置入库

主题模式、主题色、菜单布局等界面风格按用户 ID 持久化到数据库。

全局站点配置和用户界面偏好彻底分开：

- 系统标识、登录安全等是全局配置。
- 界面风格是用户级配置。

## 项目截图

最新动图和截图已经放在仓库 README 里：

https://github.com/Searchld/Nexa-Admin

包括：

- 工作台
- Art 风格代码生成
- AI 模型管理
- Art Bot 智能助手

## 技术栈

前端：

- Vue 3
- TypeScript
- Vite
- Art Design Pro / artpro-ui
- Element Plus
- Pinia
- Vue Router

后端：

- RuoYi
- Spring Boot
- Spring Security
- JWT
- MyBatis
- MySQL
- Redis
- Quartz

## 适合谁

如果你正在用 RuoYi，但觉得前端界面风格偏旧，可以看看这个项目。

如果你喜欢 Art Design Pro 的界面，又希望后端有完整的权限、菜单、日志、代码生成和监控能力，也可以直接基于这个项目二开。

项目不是一个纯 UI Demo，而是围绕 RuoYi 实际后台能力做的整合。

## 后续计划

接下来会继续补：

- 更多代码生成模板细节
- 在线演示环境持续更新
- 更多页面交互细节优化
- 使用文档和二开说明
- AI 助手更多配置项

项目地址：

https://github.com/Searchld/Nexa-Admin

欢迎 star，也欢迎提 issue 交流。
