# CRMEB Admin 迁移若依 — 适配器方案实施记录

> 日期：2026-08-07  
> 基座：`crmeb_admin2/`  
> 目标：保留 CRMEB 布局/UI，对接若依后端 API，精简冗余电商业务代码

---

## 一、执行概览

| 阶段 | 内容 | 结果 |
|------|------|------|
| 一 | 删除冗余文件 ~300 个 → `.bak` | ✅ |
| 二 | 清理 `main.js` ~40 行 | ✅ |
| 三 | 基础设施适配（auth/request/proxy/filters） | ✅ |
| 四 | API+Store+Router+权限适配 | ✅ |
| 五 | 新增若依页面 + 路由定义 + 依赖复制 | ✅ |
| 六 | 编译验证 + 浏览器测试 | ✅ |
| 七 | 菜单图标 + 动态路由 + 仪表盘适配 | ✅ |

---

## 二、核心改动文件

### 基础设施层（7 个文件）

| 文件 | 改动 |
|------|------|
| `utils/auth.js` | `TokenKey` → `'Admin-Token'` |
| `utils/request.js` | 请求头 `Authorization: Bearer` + 响应 `code→status` 归一化 + `tansParams` 序列化 |
| `utils/settingMer.js` | `apiBaseURL` 去掉 `/api/` 后缀 |
| `.env.development` | `VUE_APP_BASE_API` → `'/dev-api'` |
| `vue.config.js` | 代理 `/dev-api` → `localhost:8080` |
| `store/getters.js` | 移除 `adminProductClassify` |
| `filters/index.js` | 移除 CRMEB 业务过滤器 |

### API + Store 层（4 个文件）

| 文件 | 改动 |
|------|------|
| `api/user.js` | 重写：`/login`、`/getInfo`、`/logout`、`/captchaImage`，login 请求体转换 |
| `api/roleApi.js` | 重写：`/getRouters` |
| `store/modules/user.js` | 重写：`getInfo` 若依字段映射、`getMenus` + `transformRuoYiMenu`（含绝对路径、title 字段） |
| `store/modules/permission.js` | 重写：`transformRuoYiMenu` + `flattenMenu` + `generateRoutes` + `comRouter` 嵌套支持 |

### 路由层（4 个文件）

| 文件 | 改动 |
|------|------|
| `router/index.js` | 移除 CRMEB 业务路由，`constantRoutes` 仅保留基础路由（404/login/redirect/dashboard），动态路由由 `asyncRoutes` 提供 |
| `router/modules/ruoyi.js` | ~~废弃~~ — 原硬编码路由已删除，不再使用 |
| `router/modules/asyncRoutes.js` | **新建**：全部可能路由（含日志管理、系统监控、缓存列表、表单构建、系统接口等），供 `comRouter` 动态匹配 |
| `permission.js` | 路由守卫调用 `getInfo` → `generateRoutes` → `addRoutes` → `getMenus`，支持直接 URL 访问动态路由 |

### 视图层

| 操作 | 说明 |
|------|------|
| 登录页 `views/login/index.vue` | 添加验证码输入框 + `getCode` 方法，logo 改用静态图片 `@/assets/imgs/index_logo.png` |
| 仪表盘 `views/dashboard/index.vue` | 移除 CRMEB 电商组件（baseInfo/visitChart/userChart/userOverview），仅保留快捷入口 |
| 仪表盘 `views/dashboard/components/gridMenu.vue` | 重写：快捷入口替换为若依页面链接（用户管理/角色管理/菜单管理/日志管理等） |
| 复制 `views/system/`、`views/monitor/`、`views/tool/` | 从 ruoyi-ui 复制完整目录 |

### 组件 + 工具依赖

| 复制来源 | 文件 |
|----------|------|
| ruoyi-ui 组件 | Crontab、ExcelImportDialog、IconSelect、TreePanel、iFrame、RightToolbar、Editor、FileUpload、ImageUpload、ImagePreview、DictTag、DictData |
| ruoyi-ui 布局 | HeaderNotice |
| ruoyi-ui 工具 | passwordRule.js、ruoyi.js、dict/（6 个文件）、generator/（7 个文件） |
| ruoyi-ui 样式 | ruoyi.scss、btn.scss、element-ui.scss、mixin.scss、sidebar.scss、transition.scss、variables.scss |
| ruoyi-ui 图标 | assets/icons/svg/、assets/logo/logo.png |
| ruoyi-ui API | system/、monitor/、tool/ 目录 |

### main.js 全局注册

```js
// 新增全局方法
Vue.prototype.addDateRange = addDateRange
Vue.prototype.getDicts = getDicts
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.getConfigKey = getConfigKey

// 新增全局组件
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('DictTag', DictTag)

// Dict 插件
DictData.install()
```

---

## 三、关键修复记录

### 阶段一~六（基础设施 + 登录）

| 问题 | 原因 | 修复 |
|------|------|------|
| 登录失败 "验证码已失效" | 登录页缺验证码输入框 | 添加验证码字段 + `getCode` 方法 |
| 登录请求格式不匹配 | CRMEB 发 `account/pwd`，若依需 `username/password` | `api/user.js` login 函数转换请求体 |
| 登录后 getInfo 报错 | 响应用于扁平结构，`res.data` 为 undefined | 优先读 `res.user`，兼容 `res.data.user` |
| 可选链 `?.` 编译错误 | Babel 不支持 | 改为 `res.data && res.data.user` |
| 菜单无文字 | 缺 `title` 字段 + localStorage 旧数据 | 添加 `title` 字段 + 清除 localStorage |
| 子菜单路径为相对路径 | RuoYi 子路由 path 为 `user` 非 `/system/user` | `transformRuoYiMenu` 拼接父路径 |
| 菜单点击 404 | `comRouter` 只匹配顶层菜单 | 添加 `flattenMenu` 扁平化 |
| 用户管理页面全屏 | 路由未用 Layout 包裹 | 改为嵌套路由结构，父路由用 Layout |
| 接口 params 类型转换错误 | `addDateRange` 生成嵌套对象被序列化为 `[object Object]` | 添加 `tansParams` 序列化逻辑 |
| 仪表盘 API 404 | CRMEB 电商 API 不存在 | Stub 为返回空数据 |
| 面包屑空 menuList 报错 | 刷新时 menuList 为空 | 添加空值保护 |
| DictData 依赖缺失 | 缺 `utils/dict/` 模块 | 从 ruoyi-ui 复制 6 个文件 |
| 样式缺失 | 缺 RuoYi 全局样式 | 复制 ruoyi.scss 等 7 个样式文件 |
| 重复 Pagination 注册 | 两次 import | 移除重复 |
| 登录页 logo 消失 | `getLoginPicApi` 调用 `/captchaImage` 不返回 logo | 改用静态 import `@/assets/imgs/index_logo.png` |

### 阶段七（菜单图标 + 动态路由 + 仪表盘）

| 问题 | 原因 | 修复 |
|------|------|------|
| 菜单图标全消失 | RuoYi 图标名（system/user/tool 等）不是 Element UI 图标 | `transformRuoYiMenu` 添加 `ICON_MAP` 映射表（system→setting, tool→tools 等 30+ 项） |
| 系统工具菜单 404 | 路由硬编码在 `ruoyi.js`，与数据库菜单结构不一致（数据库有 build/swagger 但路由无） | 新建 `asyncRoutes.js` 含全部可能路由，`comRouter` 动态匹配后端菜单 |
| 菜单写死 | 路由在 `constantRoutes` 中静态注册 | 改为 `generateRoutes` 从 `/getRouters` 获取菜单 → `comRouter` 过滤 → `router.addRoutes()` 动态添加 |
| 仪表盘快捷入口 404 | CRMEB 电商链接（/store/index, /order/index 等）不存在 | 替换为若依页面链接（/system/user, /system/menu, /tool/gen 等 16 个入口） |
| 直接 URL 访问 404 | route guard 中 `to.path` 已变为 `/404`，原始路径丢失 | `window.location.pathname` 保存原始路径，addRoutes 后跳回 |
| 菜单首次加载空白 | `columnsAside.vue` 的 `setFilterRoutes` 在 `menuList` 为空时返回，后续不更新 | 添加 `menuList` watcher + `getHeaderName` 返回 null 时兜底到第一个菜单 |
| `comRouter` 递归匹配失败 | 递归时 `hasLeft` 传 `router.path`（相对），但菜单 URL 为绝对路径 | `comRouter` 递归时传 `_leftUrl`（绝对路径）替代 `router.path` |
| `tool/build` 缺依赖 | 缺 `utils/generator/` 和 `utils/index.js` 的 `makeMap` | 复制 generator 目录 + 添加 `makeMap` 函数 |
| `tool/gen` highlight.js 报错 | 项目使用 v10.7.3，`lib/highlight` 无 default export | 改为 `import hljs from "highlight.js/lib/core"` 兼容 v10 |

---

## 四、最终测试结果

| 功能 | 状态 |
|------|------|
| 编译 | **0 error** |
| 登录 + 验证码 | 正常 |
| 仪表盘 | 正常渲染，16 个快捷入口指向若依页面 |
| 一级菜单 | 系统管理、系统工具、若依官网（**图标正常**，el-icon-setting/tools/guide） |
| 二级菜单（系统管理） | 用户管理、角色管理、菜单管理、部门管理、岗位管理、字典管理、参数设置、通知公告、日志管理（含操作日志/登录日志）、系统监控（含在线用户/定时任务/数据监控/服务监控/缓存监控/缓存列表） |
| 二级菜单（系统工具） | 表单构建、代码生成、系统接口 |
| 用户管理页面 | **左树右列表** + 数据表格正常 |
| 代码生成页面 | 正常加载 |
| 路由模式 | **全动态**：后端 `/getRouters` 驱动，`comRouter` 匹配 `asyncRoutes`，`addRoutes` 注册 |
| 布局组件 | 一级菜单 + 二级菜单 + 顶栏 + 面包屑 + 标签页 + 底部 |
| 控制台 | 0 关键错误 |

---

## 五、架构说明

### 菜单数据流

```
后端 /getRouters
  → menuListApi() 请求
  → transformRuoYiMenu() 转换（含 ICON_MAP 图标映射）
  → $store.state.user.menuList（columnsAside 渲染一级菜单）
  → $store.state.user.childMenuList（aside 渲染二级菜单）
```

### 路由数据流

```
后端 /getRouters
  → generateRoutes()
    → transformRuoYiMenu() → flattenMenu() 扁平化
    → comRouter(flatMenus, asyncRoutes) 匹配
  → router.addRoutes(accessRoutes) 动态注册
```

### ICON_MAP 映射表

```
system→setting   user→user       peoples→peoples    tree-table→tree
tree→share       post→post-office dict→collection    edit→edit
message→message  monitor→monitor  online→connection  job→timer
druid→odometer   server→cpu       redis→coin         redis-list→tickets
log→document     tool→tools       code→document-copy guide→guide
build→document-add swagger→connection form→document  logininfor→reading
cache→s-data     dashboard→s-home
```

---

## 六、项目启动

```bash
cd crmeb_admin2
npm run dev
# 访问 http://localhost:9527
# 账号：admin / admin123
# 验证码：若依后端默认开启，可在 sys_config 中关闭
```

后端需确保若依服务运行在 `localhost:8080`。

---

## 七、改动文件清单（阶段七）

| 文件 | 操作 | 说明 |
|------|------|------|
| `router/modules/asyncRoutes.js` | 新建 | 全部可能路由定义（含 log/monitor 子路由、tool/build/swagger） |
| `router/modules/ruoyi.js` | 废弃 | 不再使用，硬编码路由已移除 |
| `router/index.js` | 修改 | 移除 ruoyi.js 导入，`asyncRoutes` 从新模块导入导出 |
| `store/modules/permission.js` | 修改 | 添加 ICON_MAP、修复 comRouter 递归路径（_leftUrl 替代 router.path） |
| `store/modules/user.js` | 修改 | 添加 ICON_MAP 到 transformRuoYiMenu |
| `permission.js` | 修改 | 添加 window.location.pathname 恢复原始路径逻辑 |
| `layout/component/columnsAside.vue` | 修改 | 添加 menuList watcher、修复 setFilterRoutes 空值兜底、修复可选链语法 |
| `views/dashboard/index.vue` | 修改 | 移除 CRMEB 电商组件引用 |
| `views/dashboard/components/gridMenu.vue` | 重写 | 替换为若依 16 个快捷入口 |
| `views/login/index.vue` | 修改 | logo 改用静态 import |
| `views/tool/gen/index.vue` | 修改 | highlight.js 引用兼容 v10 |
| `utils/index.js` | 修改 | 添加 `makeMap` 函数（tool/build 依赖） |
| `utils/generator/` | 复制 | 从 ruoyi-ui 复制 7 个文件 |
| `assets/logo/logo.png` | 复制 | 从 ruoyi-ui 复制 |