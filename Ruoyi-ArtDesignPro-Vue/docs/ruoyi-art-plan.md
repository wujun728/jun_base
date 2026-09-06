# Art 前端适配 RuoYi 实施说明

## 基础约定

- 前端目录：`artpro-ui`
- 权限模式：`VITE_ACCESS_MODE=backend`
- 开发接口地址：`http://localhost:8080`
- 登录令牌：`Authorization: Bearer ${token}`
- 分页请求：`pageNum`、`pageSize`
- 分页响应：`rows`、`total`

## 鉴权接口

| 能力           | 接口                |
| -------------- | ------------------- |
| 图片验证码配置 | `GET /captchaImage` |
| 登录           | `POST /login`       |
| 当前用户       | `GET /getInfo`      |
| 动态菜单       | `GET /getRouters`   |
| 登出           | `POST /logout`      |
| 注册           | `POST /register`    |

`sys.account.captchaType` 控制 `slider` 或 `image`。滑块模式保留 Art 前端交互，拖动到末端即可通过；图片模式继续由 RuoYi 后端校验。

## 页面范围

- 系统管理：用户、角色、菜单、部门、岗位、字典、参数、通知公告。
- 系统监控：在线用户、定时任务、任务日志、操作日志、登录日志、服务监控、缓存监控、缓存列表、Druid。
- 系统工具：表单构建、代码生成、Swagger UI。
- 个人中心：基本资料、修改密码和头像上传。

Art Dashboard 保留为默认首页。仪表盘目录、工作台、分析页和电子商务菜单写入 `sys_menu`，统一由 `/getRouters` 返回；其他 Art 示例源码继续保留，但不进入后端业务菜单。

菜单编辑器展示目录、菜单、按钮、内嵌和外链五种类型，但仍保存到 RuoYi 原有字段。内嵌使用 `is_frame = 0` 和 HTTP(S) 地址，在 Art 工作页签内通过 iframe 打开；外链使用 `is_frame = 1` 和 HTTP(S) 地址，在新浏览器页签打开。`/getRouters` 通过 `meta.isIframe` 显式下发打开方式。

## 系统管理接入

- 用户：部门树筛选、CRUD、状态切换、重置密码、分配角色、导入模板、导入和导出。
- 角色：CRUD、状态切换、菜单权限、数据权限、授权用户和导出。
- 菜单：树表、目录/菜单/按钮/内嵌/外链表单、子菜单新增、展开收起和排序保存。
- 部门：树表、部门编码、子部门新增、展开收起和排序保存。
- 岗位：CRUD、状态字典和导出。
- 字典：左右可拖动分栏，左侧字典类型 CRUD 与缓存刷新，右侧字典数据 CRUD 和导出。
- 参数：CRUD、缓存刷新和导出。
- 通知公告：CRUD、富文本编辑和已读用户抽屉。

业务列表的新增、编辑和查看统一使用右侧抽屉；导入、删除确认、密码输入和用户选择等短交互保留弹窗。

## 系统监控接入

- 在线用户：查询在线会话并支持单条强退。
- 定时任务：CRUD、状态切换、立即执行、导出和调度日志抽屉。
- 日志管理：操作日志和登录日志支持查询、删除、清空、导出；登录日志支持账户解锁。
- 服务监控：使用 Art 卡片展示 CPU、内存、JVM、服务器和磁盘状态。
- 缓存监控：使用 Art 卡片展示 Redis 信息和命令统计。
- 缓存列表：按缓存名称、键名、缓存内容三级展示，支持按名称、按键和全部清理。
- 数据监控：通过 iframe 内嵌 `/druid/index.html`。

## 系统工具接入

- 代码生成：查询、导入数据库表、编辑生成配置、字段配置、删除、同步数据库、代码预览、单表下载、批量下载和生成到自定义路径。
- 新增 `art-design-pro` 前端模板类型，导入表时默认选中 Art Design Pro TypeScript；同时保留 Element Plus 模板供手动切换。
- Art 模板覆盖单表 CRUD、树表、主子表和可选详情页，生成 `ArtSearchBar`、`ArtTableHeader`、`ArtTable`、`ArtForm`、`useTable`、`useDict`、`DictTag`、上传字段、富文本字段、`v-auth` 权限和 TypeScript API 文件。
- 现有数据库执行 `sql/20260601_default_art_design_pro_generator.sql` 后，仅修改新导入表的默认模板，不批量覆盖已有 `gen_table` 记录。
- 表单构建：当前保留 Art 本地占位页面，不增加后端接口；完整可视化设计器列入后续计划。
- 系统接口：通过 iframe 内嵌 `/swagger-ui.html`。

## 数据大屏接入

- 数据大屏：将 `dp-modern` 迁入 Art 前端，作为“数据大屏 / 大屏展示”全屏页面加载。
- 页面保留溜井报警、设备状态、矿车统计、视频监控、料位监测、出入料统计和主题切换能力。
- 大屏页面隔离独立项目的根节点样式，避免影响 Art 后台布局。
- 大屏接口可通过 `VITE_DP_API_BASE_URL`、`VITE_DP_WS_BASE_URL`、`VITE_DP_HANDLE_API_BASE_URL` 和 `VITE_DP_MEDIA_BASE_URL` 独立配置。
- 现有数据库先执行 `sql/20260601_extend_menu_meta.sql`，再执行 `sql/20260601_add_data_screen_menu.sql`。

### 表单构建后续计划

- 基于 `ArtForm` 配置结构实现新版可视化表单设计器，不回退到 RuoYi-Vue 原始页面风格。
- 支持左侧控件列表、中间拖拽画布与实时预览、右侧字段属性和校验规则配置。
- 支持基础控件、日期范围、字典选项、图片和文件上传、富文本、布局配置。
- 支持清空、预览、导出 JSON 和生成新版 `ArtForm` 页面代码。
- 优先复用现有 `vue-draggable-plus`、`ArtWangEditor`、`useDict` 和 `/common/upload`，暂不新增后端持久化接口。

## 个人中心接入

- 基本资料：复用 `GET/PUT /system/user/profile`，支持昵称、手机号、邮箱和性别维护。
- 修改密码：复用 `PUT /system/user/profile/updatePwd`，前端校验确认密码。
- 头像上传：复用 `POST /system/user/profile/avatar` 的 `avatarfile` 字段，上传后同步更新顶部头像。

## 站点配置接入

- 参数管理增加“站点配置”和“参数列表”两个页签，配置仍落在 `sys_config`。
- 站点标识：系统名称、简介、登录欢迎文案、登录页左侧文案和公共水印策略。
- 界面风格：主题模式、菜单布局、菜单风格、主题色、盒子样式、容器宽度、多页签、面包屑和菜单手风琴。
- 登录与安全：验证码类型、注册开关、令牌时长、失败锁定、账号锁定时长和密码规则。
- 令牌时长、失败锁定、账号锁定分钟数、密码长度和注册密码复杂度由 RuoYi 服务直接读取 `sys_config` 生效。
- 登录页通过匿名接口 `GET /system/config/site` 读取公共配置；管理员通过 `PUT /system/config/site` 批量保存。
- 验证码统一保留 `sys.account.captchaType = slider | image`，不再增加重复的图片和滑块布尔开关。

### 站点配置迁移

现有数据库执行 `sql/20260531_add_site_config.sql`。脚本可重复执行，只补充缺少的内置参数。

### 部门编码迁移

现有数据库先执行 `sql/20260531_add_sys_dept_code.sql`。迁移会新增
`sys_dept.dept_code`，并按 `DEPT_<deptId>` 为存量部门回填稳定唯一编码。
初始化数据库直接使用 `sql/ry_20260417.sql` 中的可读编码。

## 验收清单

- 登录页可按后台参数切换滑块或图片验证码。
- `/getRouters` 中 `F` 类型按钮转换为父页面 `meta.authList`，不生成路由。
- `v-auth` 与 `hasAuth()` 支持 RuoYi 完整权限标识和管理员 `*:*:*`。
- 所有 SQL 菜单对应的 Vue 页面均存在，刷新动态路由不出现组件缺失。
- CRUD 列表统一使用 Art 搜索、表头、表格与分页组件。
- 上传复用 `/common/upload`，导出复用模块 `/export` 接口。
- 后端测试、前端类型检查和 lint 均通过。
