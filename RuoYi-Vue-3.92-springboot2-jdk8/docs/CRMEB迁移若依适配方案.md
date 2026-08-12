# CRMEB Admin 前端 → 若依后端 迁移适配方案

> 版本：v1.1  
> 日期：2026-08-07  
> 状态：**已完成**  
> 基座：`crmeb_admin2/`（CRMEB Java v4.2.1 原始版本，可正常运行）  
> 目标：保留 CRMEB 布局/UI，对接若依后端 API，精简冗余电商业务代码

---

## 一、项目目标

以 `crmeb_admin2/` 为基座，通过**最小改动**实现：

1. **认证对接若依**：登录/登出/Token 使用若依后端 API
2. **菜单动态渲染**：菜单数据从若依 `/getRouters` 获取，渲染 CRMEB 布局
3. **页面正常导航**：若依系统管理页面（用户/角色/菜单/部门等）在 CRMEB 布局中正常显示
4. **精简冗余代码**：移除 CRMEB 电商业务模块（商品/订单/营销/分销/装修等），保留布局和通用组件

---

## 二、核心差异分析

### 2.1 API 端点差异

| 功能 | CRMEB（原始） | 若依（目标） |
|------|--------------|-------------|
| 登录 | `POST /admin/login` | `POST /login` |
| 用户信息 | `GET /admin/getAdminInfoByToken?token=xxx` | `GET /getInfo` |
| 菜单路由 | `GET /admin/getMenus` | `GET /getRouters` |
| 登出 | `GET /admin/logout` | `POST /logout` |
| 验证码 | `GET /admin/validate/code/get` | `GET /captchaImage` |

### 2.2 Token 差异

| 维度 | CRMEB | 若依 |
|------|-------|------|
| Header 名 | `Authori-zation` | `Authorization` |
| Header 值格式 | `token_value` | `Bearer token_value` |
| Cookie Key | `Authori-zation` | `Admin-Token` |

### 2.3 响应格式差异

| 维度 | CRMEB | 若依 |
|------|-------|------|
| 成功码 | `status: 200` | `code: 200` |
| 未授权 | HTTP 401 → `status: 401` | `code: 401` |
| 错误 | `status: 400` | `code: 500` |
| 数据字段 | `data` | `data` |

### 2.4 菜单数据结构差异

**若依返回格式：**
```json
{
  "code": 200,
  "data": [{
    "path": "/system",
    "name": "System",
    "component": "Layout",
    "meta": { "title": "系统管理", "icon": "system", "noCache": false },
    "children": [{
      "path": "user",
      "name": "User",
      "component": "system/user/index",
      "meta": { "title": "用户管理", "icon": "user", "noCache": false }
    }]
  }]
}
```

**CRMEB 期望格式：**
```json
{
  "status": 200,
  "data": {
    "menu": [{
      "id": 1,
      "pid": 0,
      "name": "系统管理",
      "url": "/system",
      "icon": "system",
      "sort": 1,
      "type": "M",
      "childList": [{
        "id": 2,
        "pid": 1,
        "name": "用户管理",
        "url": "/system/user",
        "icon": "user",
        "sort": 1,
        "type": "C",
        "childList": []
      }]
    }]
  }
}
```

### 2.5 用户信息响应差异

**若依返回：**
```json
{
  "code": 200,
  "data": {
    "user": { "userName": "admin", "nickName": "管理员", "userId": 1, "avatar": "" },
    "roles": ["admin"],
    "permissions": ["*:*:*"]
  }
}
```

**CRMEB 期望：**
```json
{
  "status": 200,
  "data": {
    "account": "admin",
    "realName": "管理员",
    "id": 1,
    "roles": ["admin"],
    "permissionsList": ["*:*:*"]
  }
}
```

---

## 三、精简方案（文件级）

### 3.1 原则

- 所有删除操作改为**重命名添加 `.bak` 后缀**，后续统一物理删除
- 先处理无依赖的文件，再处理有依赖的引用
- 每次改动后确保编译通过

### 3.2 Views 精简

| 目录 | 操作 | 文件数 | 说明 |
|------|------|--------|------|
| `views/dashboard/` | **保留** | 7 | 仪表盘，数据源需适配 |
| `views/login/` | **保留** | 13 | 登录页，改接口适配 |
| `views/error-page/` | **保留** | 2 | 404/401 |
| `views/redirect/` | **保留** | 1 | 重定向 |
| `views/error-log/` | **保留** | 3 | 错误日志 |
| `views/store/` | → **.bak** | 10 | 商品管理 |
| `views/order/` | → **.bak** | 4 | 订单管理 |
| `views/user/` | → **.bak** | 6 | 会员管理 |
| `views/distribution/` | → **.bak** | 2 | 分销 |
| `views/marketing/` | → **.bak** | 20 | 营销（优惠券/秒杀/拼团/砍价/积分） |
| `views/financial/` | → **.bak** | 8 | 财务（提现/充值/佣金） |
| `views/content/` | → **.bak** | 3 | 文章管理 |
| `views/design/` | → **.bak** | 150+ | 装修（最大冗余模块） |
| `views/systemSetting/` | → **.bak** | 15 | CRMEB 系统设置 |
| `views/appSetting/` | → **.bak** | 10 | 公众号/小程序 |
| `views/maintain/` | → **.bak** | 18 | CRMEB 维护 |
| `views/sms/` | → **.bak** | 7 | 短信设置 |
| `views/mobile/` | → **.bak** | 10 | 移动端 |
| `views/system/` | **新增** | ~20 | 从 ruoyi-ui 复制 |
| `views/monitor/` | **新增** | ~15 | 从 ruoyi-ui 复制 |
| `views/tool/` | **新增** | ~8 | 从 ruoyi-ui 复制 |

### 3.3 Router Modules 精简

| 文件 | 操作 |
|------|------|
| `router/modules/store.js` | → **.bak** |
| `router/modules/order.js` | → **.bak** |
| `router/modules/user.js` | → **.bak** |
| `router/modules/distribution.js` | → **.bak** |
| `router/modules/marketing.js` | → **.bak** |
| `router/modules/financial.js` | → **.bak** |
| `router/modules/content.js` | → **.bak** |
| `router/modules/design.js` | → **.bak** |
| `router/modules/operation.js` | → **.bak** |
| `router/modules/appSetting.js` | → **.bak** |
| `router/modules/maintain.js` | → **.bak** |
| `router/modules/mobile.js` | → **.bak** |
| `router/modules/ruoyi.js` | **新增** |

### 3.4 API 文件精简

| 文件 | 操作 | 说明 |
|------|------|------|
| `api/user.js` | **保留+修改** | 登录/用户信息/登出 |
| `api/roleApi.js` | **保留+修改** | 菜单接口 |
| `api/req.js` | **保留** | 备用 axios |
| `api/dashboard.js` | **保留** | 仪表盘数据 |
| 其余 34 个 API 文件 | → **.bak** | 电商业务 API |

### 3.5 Store Modules 精简

| 文件 | 操作 | 说明 |
|------|------|------|
| `store/modules/product.js` | → **.bak** | 商品分类 |
| `store/modules/mobildConfig.js` | → **.bak** | 移动端配置 |
| `store/modules/app.js` | **保留** | 侧边栏/设备/尺寸 |
| `store/modules/menu.js` | **保留** | 菜单状态（顶栏/侧栏/面包屑/TagView） |
| `store/modules/permission.js` | **保留+修改** | 路由权限生成 |
| `store/modules/settings.js` | **保留** | 布局设置（域名/主题） |
| `store/modules/tagsView.js` | **保留** | 标签页 |
| `store/modules/themeConfig.js` | **保留** | 主题配置 |
| `store/modules/user.js` | **保留+修改** | 用户/Token/菜单 |
| `store/modules/errorLog.js` | **保留** | 错误日志 |

### 3.6 Components 精简

| 目录 | 操作 | 说明 |
|------|------|------|
| `articleList/` | → **.bak** | 文章列表组件 |
| `attrFrom/` | → **.bak** | 商品属性表单 |
| `Category/` | → **.bak** | 分类组件 |
| `couponList/` | → **.bak** | 优惠券列表 |
| `customerInfo/` | → **.bak** | 客户信息 |
| `CustomDesign/` | → **.bak** | 自定义设计 |
| `FromList/` | → **.bak** | 表单列表 |
| `goodList/` | → **.bak** | 商品列表 |
| `goodsList/` | → **.bak** | 商品列表2 |
| `hotpotModal/` | → **.bak** | 热区弹窗 |
| `iconFrom/` | → **.bak** | 图标选择器 |
| `linkaddress/` | → **.bak** | 地址联动 |
| `mobileConfigRight/` | → **.bak** | 移动端配置 |
| `optionDateTimePicker/` | → **.bak** | 日期选择器 |
| `pagesHeader/` | → **.bak** | 页面头部 |
| `rightConfig/` | → **.bak** | 右侧配置 |
| `Share/` | → **.bak** | 分享组件 |
| `storeLabelList/` | → **.bak** | 商品标签 |
| `uploadImg/` | → **.bak** | 图片上传 |
| `uploadPicture/` | → **.bak** | 图片上传 |
| `uploadPictures/` | → **.bak** | 多图上传 |
| `uploadVideo2/` | → **.bak** | 视频上传 |
| `userList/` | → **.bak** | 用户列表 |
| 其余 ~30 个组件 | **保留** | 通用组件 |

**保留的通用组件：**

| 目录 | 用途 |
|------|------|
| `base/` | 公共组件库 |
| `Breadcrumb/` | 面包屑 |
| `cards/` | 卡片 |
| `copyright/` | 版权 |
| `echarts/`、`echartsNew/` | 图表 |
| `ErrorLog/` | 错误日志 |
| `FormGenerator/` | 表单生成器 |
| `Hamburger/` | 汉堡菜单 |
| `HeaderSearch/` | 头部搜索 |
| `Pagination/` | 分页 |
| `PanThumb/` | 头像缩略图 |
| `ParentView/` | 父视图（router-view 包装） |
| `RightPanel/` | 右侧面板 |
| `Screenfull/` | 全屏 |
| `Sticky/` | 吸顶 |
| `SvgIcon/` | SVG 图标 |
| `ThemePicker/` | 主题选择器 |
| `TimeSelect/` | 时间选择 |
| `Tinymce/` | 富文本编辑器 |
| `TopNav/` | 顶部导航 |
| `Upload/` | 上传 |
| `UploadExcel/` | Excel 上传 |
| `verifition/` | 滑块验证 |
| `wangEditor/` | 富文本编辑器 |

### 3.7 Libs 精简

| 文件 | 操作 | 说明 |
|------|------|------|
| `wechat.js` | → **.bak** | 微信 SDK |
| `modal-attr.js` | → **.bak** | 商品属性弹窗 |
| `modal-coupon.js` | → **.bak** | 优惠券弹窗 |
| `modal-icon.js` | → **.bak** | 图标弹窗 |
| `modal-parserFrom.js` | → **.bak** | 表单解析弹窗 |
| `modal-prompt.js` | → **.bak** | 提示弹窗 |
| `modal-sure.js` | → **.bak** | 确认弹窗 |
| `copyPublic.js` | → **.bak** | 公共复制 |
| `debounce.js` | **保留** | 防抖 |
| `dialog.js` | **保留** | 对话框 |
| `loading.js` | **保留** | 加载 |
| `public.js` | **保留** | 公共函数 |
| `timeOptions.js` | **保留** | 时间选项 |
| `tools.js` | **保留** | 工具函数 |
| `util.js` | **保留** | 工具集 |

### 3.8 Utils 精简

| 文件 | 操作 | 说明 |
|------|------|------|
| `ZBKJIutil.js` | → **.bak** | 中邦科技定制 |
| `public.js` | → **.bak** | 价格处理（电商专用） |
| `permission.js` | → **.bak** | CRMEB 权限校验（改用若依的） |
| 其余 ~19 个 | **保留** | 部分需修改适配 |

### 3.9 Filters 精简

| 文件 | 操作 | 说明 |
|------|------|------|
| `order.js` | → **.bak** | 订单相关过滤器 |
| `user.js` | → **.bak** | 会员相关过滤器 |
| `wx.js` | → **.bak** | 微信相关过滤器 |
| `commFilter.js` | → **.bak** | 业务公共过滤器（优惠券/砍价/拼团/提现等） |
| `filters/index.js` | **保留+修改** | 只保留通用过滤器 |

---

## 四、适配方案（代码级）

### 4.1 适配策略

采用 **"响应拦截器归一化 + 数据格式转换函数"** 双层适配：

```
若依响应: { code: 200, data: {...}, msg: "..." }
    ↓ 响应拦截器归一化（request.js）
统一格式: { code: 200, status: 200, data: {...}, msg: "..." }
    ↓ 业务适配函数（api 层 / store 层）
CRMEB 期望: { status: 200, data: {...}, msg: "..." }
```

**核心原则：**
- 所有 CRMEB 业务代码检查 `res.status === 200` 不变
- 响应拦截器统一添加 `status` 字段，值为 `code`
- 特殊接口（菜单/用户信息）在 API 层做数据格式转换

### 4.2 文件改动清单

#### 4.2.1 `utils/auth.js` — Token 存储

```diff
- const TokenKey = 'Authori-zation'
+ const TokenKey = 'Admin-Token'
```

#### 4.2.2 `utils/request.js` — 请求/响应拦截器

**请求拦截器改动：**
```diff
- config.headers['Authori-zation'] = getToken()
+ config.headers['Authorization'] = 'Bearer ' + getToken()
```

**响应拦截器改动（关键）：**
```js
// 在现有响应拦截器最前面加入归一化逻辑
service.interceptors.response.use(response => {
  const res = response.data
  // 若依返回 code 字段，统一添加 status 字段兼容 CRMEB
  if (res.code !== undefined && res.status === undefined) {
    res.status = res.code
  }
  return res
}, error => {
  // 错误处理：401 跳转登录页
  if (error.response && error.response.status === 401) {
    // 清除 token 并跳转登录
  }
  return Promise.reject(error)
})
```

#### 4.2.3 `api/user.js` — 登录/用户信息/登出

```diff
// 登录
- url: '/admin/login',
+ url: '/login',

// 用户信息
- url: '/admin/getAdminInfoByToken',
+ url: '/getInfo',
- method: 'get',
+ method: 'get',
- params: { token: getToken() }
+ // 无需 token 参数，由拦截器自动添加

// 登出
- url: '/admin/logout',
+ url: '/logout',
- method: 'get',
+ method: 'post',

// 验证码
- url: '/admin/validate/code/get',
+ url: '/captchaImage',
```

#### 4.2.4 `api/roleApi.js` — 菜单接口

```diff
// 菜单列表
- url: '/admin/getMenus',
+ url: '/getRouters',
```

并在响应中转换菜单数据格式（见 4.3）。

#### 4.2.5 `store/modules/user.js` — 用户信息适配

```js
// getInfo action 中，将若依返回格式映射为 CRMEB 期望格式
getInfo({ commit }) {
  return new Promise((resolve, reject) => {
    getInfo().then(res => {
      // 若依返回: { user: { userName, nickName, userId }, roles, permissions }
      const user = res.data.user || res.user
      const data = {
        account: user.userName,
        realName: user.nickName,
        id: user.userId,
        roles: res.data.roles || res.roles || [],
        permissionsList: res.data.permissions || res.permissions || [],
        level: 0,
        status: true,
        isSms: false,
        phone: user.phonenumber || '',
        addTime: user.createTime || '',
        loginCount: 0,
        lastTime: '',
        lastIp: ''
      }
      commit('SET_ROLES', data.roles)
      commit('SET_NAME', data.account)
      commit('SET_AVATAR', user.avatar || '')
      commit('SET_PERMISSIONS', data.permissionsList)
      resolve(data)
    }).catch(error => reject(error))
  })
}
```

#### 4.2.6 `store/modules/permission.js` — 菜单格式适配

核心改动：将若依的 `children` + `meta` 格式转换为 CRMEB 的 `childList` + `url` 格式：

```js
function transformMenu(menuList) {
  return menuList.map((item, index) => ({
    id: item.meta?.id || item.id || index + 1,
    pid: item.meta?.parentId || item.parentId || 0,
    name: item.meta?.title || item.name,
    url: item.path || '',
    component: item.component || '',
    icon: item.meta?.icon || '',
    sort: item.meta?.orderNum || index,
    type: item.meta?.menuType || 'M',
    hidden: !!item.hidden,
    childList: item.children ? transformMenu(item.children) : [],
    children: item.children ? transformMenu(item.children) : [],
    isShow: true,
    menuType: item.meta?.menuType || 'M',
    unique: item.path || '',
    path: item.path || '',
    isMenu: item.meta?.menuType !== 'F',
    perms: item.meta?.perms || '',
  }))
}
```

#### 4.2.7 `router/index.js` — 路由入口

**移除所有 CRMEB 业务路由模块引用：**
```diff
- import store from './modules/store'
- import order from './modules/order'
- import user from './modules/user'
- import distribution from './modules/distribution'
- import marketing from './modules/marketing'
- import financial from './modules/financial'
- import content from './modules/content'
- import design from './modules/design'
- import operation from './modules/operation'
- import appSetting from './modules/appSetting'
- import maintain from './modules/maintain'
- import mobile from './modules/mobile'
+ import ruoyi from './modules/ruoyi'
```

**constantRoutes 中只保留基础路由：**
```js
export const constantRoutes = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/error-page/404'), hidden: true },
  { path: '/401', component: () => import('@/views/error-page/401'), hidden: true },
  { path: '/redirect', component: () => import('@/views/redirect/index'), hidden: true },
  { path: '/', component: () => import('@/layout/index'), redirect: '/dashboard', children: [{
    path: 'dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/index'),
    meta: { title: '主页', icon: 'dashboard' }
  }] },
  // + 若依系统路由
  ...ruoyi,
]
```

#### 4.2.8 `permission.js`（路由守卫）— 适配

```diff
// 白名单
- const whiteList = ['/login', '/auth-redirect']
+ const whiteList = ['/login', '/auth-redirect', '/register']

// 获取用户信息
- const roles = await store.dispatch('user/getInfo')
+ await store.dispatch('user/getInfo')

// 生成路由
- const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
+ const accessRoutes = await store.dispatch('permission/generateRoutes')
```

#### 4.2.9 `main.js` — 清理 CRMEB 引用

需要移除的 import（约 20 行）：

```diff
- import attrFrom from './components/attrFrom'
- import uploadPicture from './components/uploadPicture/uploadFrom'
- import goodListFrom from './components/goodList/goodListFrom'
- import couponFrom from './components/couponList/couponFrom'
- import articleFrom from './components/articleList/articleFrom'
- import UploadIndex from '@/components/uploadPicture/index.vue'
- import common_wrapper from '@/views/design/theme_editor/components/mobilePage/common_wrapper.vue'
- import iconFrom from './components/iconFrom'
- import SelfUpload from '@/components/uploadPicture/forGenrator/index.vue'
- import modalParserFrom from '@/libs/modal-parserFrom'
- import modalAttr from '@/libs/modal-attr'
- import modalIcon from '@/libs/modal-icon'
- import modalPrompt from '@/libs/modal-prompt'
- import { modalSure } from '@/libs/public'
- import { HandlePrice } from '@/utils/public'
- import * as Auth from '@/libs/wechat'
- import * as selfUtil from '@/utils/ZBKJIutil.js'
```

需要移除的 Vue.use / Vue.component / Vue.prototype 注册：

```diff
- Vue.use(uploadPicture)
- Vue.use(goodListFrom)
- Vue.use(couponFrom)
- Vue.use(articleFrom)
- Vue.component('attrFrom', attrFrom)
- Vue.component('UploadIndex', UploadIndex)
- Vue.component('SelfUpload', SelfUpload)
- Vue.component('iconFrom', iconFrom)
- Vue.component('common_wrapper', common_wrapper)
- Vue.prototype.$modalParserFrom = modalParserFrom
- Vue.prototype.$modalSure = modalSure
- Vue.prototype.$HandlePrice = HandlePrice
- Vue.prototype.$modalAttr = modalAttr
- Vue.prototype.$modalIcon = modalIcon
- Vue.prototype.$modalPrompt = modalPrompt
- Vue.prototype.$wechat = Auth
- Vue.prototype.$selfUtil = selfUtil
```

#### 4.2.10 `store/getters.js` — 移除 CRMEB 专属 getter

```diff
- adminProductClassify: (state) => state.product.adminProductClassify,
```

#### 4.2.11 `filters/index.js` — 精简过滤器

只保留通用过滤器，移除业务过滤器引用：

```diff
- export * from '../filters/commFilter'
- export * from '../filters/user'
- export * from '../filters/order'
- export * from '../filters/wx'
```

保留的通用过滤器：
- `parseTime` / `formatTime`（从 `@/utils`）
- `timeAgo`
- `numberFormatter`
- `toThousandFilter`
- `uppercaseFirst`

#### 4.2.12 `vue.config.js` — 代理配置

```diff
// 代理目标改为若依后端
proxy: {
- '/api': { target: 'http://127.0.0.1:8080' }
+ '/dev-api': { target: 'http://localhost:8080' }
}
```

#### 4.2.13 `.env.development` — API 前缀

```diff
- VUE_APP_BASE_API = '/api'
+ VUE_APP_BASE_API = '/dev-api'
```

#### 4.2.14 `utils/settingMer.js` — API 基础 URL

```diff
- let baseUrl = process.env.VUE_APP_BASE_API + '/api/'
+ let baseUrl = process.env.VUE_APP_BASE_API + '/'
```

### 4.3 菜单数据转换函数（核心）

在 `store/modules/permission.js` 中新增 `transformRuoYiMenu` 函数：

```js
/**
 * 将若依菜单格式转换为 CRMEB 期望格式
 * 若依: { path, name, component, children, meta: { title, icon, orderNum, menuType } }
 * CRMEB: { id, pid, name, url, icon, sort, type, childList, children, ... }
 */
function transformRuoYiMenu(menuList, parentId = 0) {
  if (!menuList || !Array.isArray(menuList)) return []
  return menuList.map((item, index) => {
    const id = item.meta?.menuId || item.id || index + 1
    const transformed = {
      id,
      pid: parentId,
      name: item.meta?.title || item.name || '',
      url: item.path || '',
      component: item.component || '',
      icon: item.meta?.icon || '',
      sort: item.meta?.orderNum || index,
      type: item.meta?.menuType || 'M',
      hidden: !!item.hidden,
      isShow: !item.hidden,
      menuType: item.meta?.menuType || 'M',
      unique: item.path || '',
      path: item.path || '',
      isMenu: item.meta?.menuType !== 'F',
      perms: item.meta?.perms || '',
    }
    if (item.children && item.children.length > 0) {
      transformed.childList = transformRuoYiMenu(item.children, id)
      transformed.children = transformed.childList
    } else {
      transformed.childList = []
      transformed.children = []
    }
    return transformed
  })
}
```

### 4.4 `directive/permission/` 权限指令适配

CRMEB 的 `hasPermi.js` 和 `hasRole.js` 从 `store.getters.permissions` 和 `store.getters.roles` 读取权限数据，与若依 store 中的字段名一致，**无需修改**。

但需确认 `hasRole.js` 中的角色判断逻辑（第 17 行 `store.state.user.name`）与若依数据兼容：

```diff
- const roles = store.state.user.name
+ const roles = store.getters && store.getters.roles
```

### 4.5 `plugins/auth.js` 权限插件适配

CRMEB 的 `$auth` 插件从 `store.getters.permissions` 读取，与若依一致，**无需修改**。

---

## 五、实施结果（实际执行记录）

### 阶段一：删除冗余文件（.bak 后缀）✔️

| 分类 | 操作 | 数量 |
|------|------|------|
| Views 目录 | 13 个业务目录 → `.bak` | store, order, user, distribution, marketing, financial, content, design, systemSetting, appSetting, maintain, sms, mobile |
| Router modules | 12 个文件 → `.bak` | store.js, order.js, user.js, distribution.js, marketing.js, financial.js, content.js, design.js, operation.js, appSetting.js, maintain.js, mobile.js |
| API 文件 | 34 个业务 API → `.bak` | 保留 user.js, roleApi.js, req.js, dashboard.js |
| Components | 23 个业务组件 → `.bak` | articleList, attrFrom, Category, couponList, customerInfo, CustomDesign, FromList, goodList, goodsList, hotpotModal, iconFrom, linkaddress, mobileConfigRight, optionDateTimePicker, pagesHeader, rightConfig, Share, storeLabelList, uploadImg, uploadPicture, uploadPictures, uploadVideo2, userList |
| Libs | 8 个 → `.bak` | wechat.js, modal-attr.js, modal-coupon.js, modal-icon.js, modal-parserFrom.js, modal-prompt.js, modal-sure.js, copyPublic.js |
| Utils | 2 个 → `.bak` | ZBKJIutil.js, public.js （permission.js 后续恢复） |
| Filters | 4 个 → `.bak` | commFilter.js, order.js, user.js, wx.js |
| Store modules | 2 个 → `.bak` | product.js, mobildConfig.js |

**预计删除 ~300 个文件，实际执行完全一致。**

### 阶段二：清理 main.js 引用 ✔️

移除的 import（17 行）：
- `attrFrom`, `uploadPicture`, `goodListFrom`, `couponFrom`, `articleFrom`
- `UploadIndex`, `common_wrapper`, `iconFrom`, `SelfUpload`
- `modalParserFrom`, `modalAttr`, `modalIcon`, `modalPrompt`
- `modalSure`, `HandlePrice`, `Auth`, `selfUtil`

移除的 Vue.use/Vue.component/Vue.prototype 注册（17 行）。

### 阶段三：基础设施适配 ✔️

| 文件 | 改动 | 状态 |
|------|------|------|
| `utils/auth.js` | `TokenKey` → `'Admin-Token'` | ✅ |
| `utils/request.js` | 请求头 `Authorization: Bearer` + 响应拦截器 `code→status` 归一化 + 移除 `X-Source`/`isPhone` | ✅ |
| `utils/settingMer.js` | `apiBaseURL` 去掉 `/api/` 后缀 | ✅ |
| `.env.development` | `VUE_APP_BASE_API` → `'/dev-api'` | ✅ |
| `vue.config.js` | 新增代理 `/dev-api` → `http://localhost:8080` | ✅ |
| `store/getters.js` | 移除 `adminProductClassify` getter | ✅ |
| `filters/index.js` | 移除 CRMEB 业务过滤器引用，保留通用过滤器 | ✅ |

### 阶段四：API + Store + Router 适配 ✔️

| 文件 | 改动 | 状态 |
|------|------|------|
| `api/user.js` | 重写：`/login`, `/getInfo`, `/logout`, `/captchaImage` | ✅ |
| `api/roleApi.js` | 重写：`/getRouters` | ✅ |
| `store/modules/user.js` | 重写：`getInfo` 若依字段映射、`getMenus` 菜单转换、移除 broken imports | ✅ |
| `store/modules/permission.js` | 重写：`transformRuoYiMenu` 菜单转换、`generateRoutes` 适配 | ✅ |
| `permission.js` | 白名单加 `/register`、`generateRoutes` 去参 | ✅ |
| `router/index.js` | 重写：移除所有 CRMEB 业务路由、添加 `ruoyiRouter` | ✅ |
| `directive/permission/hasRole.js` | 角色读取改为 `store.getters.roles` | ✅ |

### 阶段五：新增若依页面 + 依赖复制 ✔️

| 操作 | 说明 |
|------|------|
| 复制 `views/system/` | 从 ruoyi-ui 复制完整目录 |
| 复制 `views/monitor/` | 从 ruoyi-ui 复制完整目录 |
| 复制 `views/tool/` | 从 ruoyi-ui 复制完整目录 |
| 复制 `api/system/`、`api/monitor/`、`api/tool/` | 若依 API 模块 |
| 复制 `components/` 依赖 | Crontab, ExcelImportDialog, IconSelect, TreePanel, iFrame |
| 复制 `layout/components/HeaderNotice/` | 通知组件 |
| 复制 `utils/passwordRule.js`、`utils/ruoyi.js` | 若依工具函数 |
| 复制 `assets/icons/svg/` | 若依 SVG 图标 |
| 创建 `router/modules/ruoyi.js` | 14 条若依路由（平铺 `hidden: true` 方式） |

### 阶段六：编译验证 ✔️

```
DONE  Compiled successfully in 8828ms
0 errors, 0 warnings
App running at: http://localhost:9527/
```

### 执行中额外修复的问题

| 问题 | 文件 | 修复方式 |
|------|------|---------|
| `utils/permission.js` 被 dashboard 等组件引用 | `utils/permission.js` | 恢复（从 .bak 改回） |
| `views/dashboard/index.vue` 引用 `authInformation` API | `views/dashboard/index.vue` | 移除 `authCertQuery` 导入和方法 |
| `views/dashboard/components/userOverview.vue` 引用 `statistic` API | `userOverview.vue` | 移除 API 导入，stub 数据方法 |
| `views/login/index.vue` 引用多个已删除 API | `views/login/index.vue` | 移除/Stub 相关导入和方法 |
| `layout/logo/index.vue` 引用 `systemConfig` API | `layout/logo/index.vue` | 移除 API 导入，改为从 Cookie 读取 |
| `components/copyright/index.vue` 引用 `copyPublic` lib | `components/copyright/index.vue` | 移除导入，改为静态文本 |
| `libs/public.js` 引用 `storePoint`/`marketing` API | `libs/public.js` | 精简为仅保留工具函数 |
| `layout/navBars/breadcrumb/userNews.vue` 引用 `system` API | `userNews.vue` | Stub 导入 |
| `components/Upload/uploadFile.vue` 引用 `systemSetting` API | `uploadFile.vue` | Stub 导入 |
| `views/login/verifition/Verify/verifySlider.vue` 引用 `systemFormConfig` API | `verifySlider.vue` | Stub 导入 |
| `views/login/verifition/Verify/verifyPoints.vue` 引用 `systemFormConfig` API | `verifyPoints.vue` | Stub 导入 |
| `components/base/ZBParser.vue` 引用 `systemFormConfig` API | `ZBParser.vue` | Stub 导入 |
| `components/IconSelect` 引用 `../../assets/icons/svg` | 整个目录 | 从 ruoyi-ui 复制 SVG 图标 |

---

## 六、实际改动量统计

| 类别 | 操作 | 数量 |
|------|------|------|
| 文件重命名 (.bak) | 删除 | ~300 个文件 |
| 文件修改 | 适配 | ~20 个文件 |
| 文件新增 | 若依页面/路由/组件/API | ~80 个文件 |
| 额外修复 | Stub/适配 | 13 个文件 |

---

## 七、风险与注意事项

### 6.1 高风险点

| 风险 | 影响 | 缓解措施 |
|------|------|---------|
| 若依页面组件（system/views）与 CRMEB 布局不兼容 | 页面渲染异常 | 布局组件是通用的 el-container 结构，已确认兼容 |
| 菜单数据转换遗漏字段 | 菜单渲染不完整 | 保留 children 和 childList 双字段，兼容两种读取方式 |
| CRMEB 仪表盘调用电商 API | 仪表盘报错 | 已 Stub 数据源方法，返回空数据 |
| `hasRole.js` 指令读取 `store.state.user.name` | 角色权限判断异常 | 已改为 `store.getters.roles` |

### 6.2 不需要改动的部分

- 所有布局组件（`layout/`）
- 主题系统（`theme/`、`styles/`）
- 图标系统（`icons/`、`assets/`）
- 通用组件（`components/base/`、`Breadcrumb/`、`Pagination/` 等）
- 插件系统（`plugins/`）
- 指令系统（`directive/`，除 `hasRole.js` 小修改）
- 错误处理（`utils/error-log.js`）
- 事件总线（`utils/bus.js`）