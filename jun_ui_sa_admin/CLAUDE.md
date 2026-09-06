# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在此代码库中工作时提供指导。

## 项目概述

**Sa-Admin v1.41.0** - 一个纯 HTML/JS 构建的多窗口后台管理模板，无需构建系统或 Node.js 工具。项目通过任意 HTTP 服务器（如 Apache/XAMPP）直接在浏览器中打开 `index.html` 即可运行。

**技术栈：**
- Vue 2.6（从 CDN 加载）
- Element UI 2.13
- jQuery 3.4
- layer.js（弹窗库）
- http-vue-loader（运行时加载 `.vue` 文件，无需打包）

**许可证：** MIT

## 运行项目

无构建命令。运行方式：
1. 确保项目通过 HTTP 服务（如 XAMPP 的 `http://localhost/jun_ui_sa_admin/`）提供
2. 在浏览器中打开 `index.html`
3. 登录页面位于 `login.html`

项目没有 `package.json`、没有 npm 依赖、没有 webpack/vite、没有代码检查工具、也没有测试。

## 架构概览

### 目录结构

```
.
├── index.html              # 主管理框架（菜单 + 标签页 + 内容框架）
├── login.html              # 登录页面
├── main.html               # 首页（重定向到 console-main.html）
├── sa-frame/               # 框架核心（不要直接修改）
├── sa-view/                # 业务页面（加载到标签页的视图）
└── static/                 # 共享 CSS、JS 工具库、第三方库
```

### 三层分离

1. **`sa-frame/`** - 框架基础设施（菜单渲染、标签页管理、导航组件）
2. **`sa-view/`** - 按业务领域组织的内容页面（用户、文章、角色、控制台等）
3. **`static/`** - 全局工具库（`sa.js`、`sa.css`）和第三方库

## 核心框架文件

### 核心引擎
- **`sa-frame/index/index.js`** - 根 Vue 实例 `sa_admin`，管理：
  - 菜单系统（`setMenuList`、`initMenu`、`showMenuById`）
  - 标签页生命周期（`addTab`、`showTab`、`closeTab`、`f5Tab`）
  - 多窗口通信（`getTabWindow`）
  - 基于哈希的路由（URL 锚点跟踪活动标签页）
  - 主题状态和 localStorage 持久化

### 自定义入口文件
- **`sa-frame/sa-code.js`** - 你应该编辑的文件
  - 配置：`sa_admin.title`、`sa_admin.logo`、`sa_admin.user`、`sa_admin.dropList`
  - 初始化菜单：`sa_admin.setMenuList(menuList)`
  - 必须调用：最后调用 `sa_admin.init()`

### 菜单配置
- **`sa-frame/menu-list.js`** - 定义 `window.menuList` 数组
  - 每个项目包含：`id`（唯一）、`name`、`icon`、`url`、`childList`（子菜单）、`parentId`
  - 支持最多 4 层嵌套
  - `isShow`、`isBlank`、`click` 属性用于高级控制

### 全局工具库
- **`static/sa.js`** - 所有页面可用的综合工具库 `sa`：
  - Ajax：`sa.ajax()`（真实）、`sa.ajax2()`（模拟，600ms 延迟 - 演示中使用）
  - 弹窗：`sa.msg()`、`sa.alert()`、`sa.confirm()`、`sa.showIframe()`
  - 权限：`sa.setAuth(codeList)`、`sa.isAuth(code)`、`sa.checkAuth(code)`
  - 日期：`sa.forDate(timestamp, format)`
  - 数组：`sa.arrayGet()`、`sa.arrayDelete()`、`sa.getArrayField()`
  - 配置：`sa.cfg.api_url`（基于环境的 API 端点）

## 双视图系统

Sa-Admin 每个标签页支持两种渲染模式：

### iframe 模式（`.html` 文件）
每个视图都是一个**完全独立的 HTML 页面**，拥有自己的 `<head>`，重新引入所有依赖（Vue、Element UI、jQuery、layer、sa.js 来自 CDN）。每个页面创建自己的 `new Vue({el: '.vue-box'})` 实例。

**标准 `.html` 视图模式：**
```html
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://unpkg.com/element-ui@2.13.0/lib/theme-chalk/index.css">
    <link rel="stylesheet" href="../../static/sa.css">
    <script src="https://unpkg.com/vue@2.6.10/dist/vue.js"></script>
    <script src="https://unpkg.com/element-ui@2.13.0/lib/index.js"></script>
    <script src="https://unpkg.com/jquery@3.4.1/dist/jquery.min.js"></script>
    <script src="../../static/kj/layer/layer.js"></script>
    <script src="../../static/sa.js"></script>
</head>
<body>
    <div class="vue-box" style="display: none;" :style="'display: block;'">
        <!-- Element UI components here -->
    </div>
    <script>
        var app = new Vue({
            el: '.vue-box',
            data: { p: {}, dataList: [], dataCount: 0 },
            methods: {
                f5: function() { sa.ajax2('/api/endpoint', this.p, function(res) {...}); }
            },
            created: function() { this.f5(); }
        });
    </script>
</body>
</html>
```

### .vue 组件模式（`.vue` 文件）
当 URL 以 `.vue` 结尾时自动检测。通过 `http-vue-loader` 在运行时加载。组件在父 Vue 实例内运行并共享依赖。

**何时使用：**
- **iframe（`.html`）**：复杂页面、需要隔离、最常见的业务视图
- **`.vue` 组件**：轻量级视图、需要与父框架紧密集成

## 创建新视图

### 1. 创建视图文件
在 `sa-view/your-module/your-page.html` 中添加 `.html` 文件，遵循上述标准模式。

### 2. 添加菜单项
编辑 `sa-frame/menu-list.js`：
```javascript
{
    id: '9-1',
    name: 'Your Page Name',
    icon: 'el-icon-document',
    url: 'sa-view/your-module/your-page.html'
}
```

### 3. 开发时使用模拟数据
使用 `sa.ajax2()` 获取模拟响应（自动在 600ms 延迟后返回）：
```javascript
sa.ajax2('/api/yourEndpoint', {id: 123}, function(res) {
    this.dataList = res.data.list;
}.bind(this));
```

当集成真实后端时，切换到 `sa.ajax()` 并配置 `sa.cfg.api_url`。

## 可复用表单组件

Sa-Admin 提供三个强大的组件（必须通过 httpVueLoader 导入）：

### `sa-item.vue` - 通用输入组件
支持 20 多种类型：text、num、textarea、date、datetime、slider、money、img、audio、video、file、img-list、richtext、enum、color、rate 等。

```html
<sa-item type="text" name="商品名称" v-model="m.name"></sa-item>
<sa-item type="date" name="日期选择" v-model="m.create_time" br></sa-item>
<sa-item type="enum" name="状态" v-model="m.status" :jv="{1: '正常', 2: '禁用'}" jtype="1" br></sa-item>
```

### `sa-info.vue` - 通用展示组件
镜像 sa-item 的类型用于只读显示。

```html
<sa-info name="商品名称" :value="m.name"></sa-info>
<sa-info name="图片展示" :value="m.url" type="img" br></sa-info>
```

### `sa-td.vue` - 通用表格列组件
封装 `el-table-column`，提供增强的显示类型。

```html
<el-table :data="dataList">
    <sa-td type="selection"></sa-td>
    <sa-td name="商品名称" prop="name"></sa-td>
    <sa-td name="状态" prop="status" type="enum" :jv="{1: '正常[green]', 2: '禁用[red]'}"></sa-td>
</el-table>
```

在 Vue 实例中导入：
```javascript
components: {
    "sa-item": httpVueLoader('../../sa-frame/com/sa-item.vue'),
    "sa-info": httpVueLoader('../../sa-frame/com/sa-info.vue'),
    "sa-td": httpVueLoader('../../sa-frame/com/sa-td.vue'),
}
```

## 多窗口通信

### 从子 iframe 到父窗口
```javascript
parent.sa_admin.showTab({name: '新页面', url: 'sa-view/xxx.html'});
parent.sa_admin.msg('来自子窗口的消息');
```

### 从父窗口到子 iframe
```javascript
var win = sa_admin.getTabWindow('2-1');  // 根据标签页 id 获取 iframe 的 window 对象
if(win) {
    win.app.f5();  // 调用子窗口 Vue 实例的方法
}
```

注意：跨域 iframe 无法访问彼此的 window 对象。

## 菜单系统

### 菜单属性
- `id`（必填、唯一） - 用于标签页跟踪、路由、权限
- `name` - 显示文本
- `icon` - Element UI 图标类名（如 'el-icon-user'）
- `url` - 页面 URL（支持 .html 和 .vue）
- `childList` - 子菜单项数组（支持 4 层嵌套）
- `parentId` - 用于扁平数组；框架通过 `arrayToTree()` 转换为树形结构
- `isShow` - 可见性切换（默认 true）
- `isBlank` - 在新浏览器标签页打开而不是 iframe
- `click` - 执行 JS 函数而不是导航

### 基于角色的菜单过滤
```javascript
// 在 sa-code.js 中，第二个参数过滤哪些菜单可见
var visibleMenuIds = ['1', '1-1', '5', '5-1']; // 通常来自后端
sa_admin.setMenuList(window.menuList, visibleMenuIds);
```

## 权限认证

框架通过 localStorage 提供**客户端**权限检查：

```javascript
// 登录时，存储用户的权限码
var codes = ['user:add', 'user:delete', 'article:edit']; // 来自后端
sa.setAuth(codes);

// 在页面脚本中检查权限（失败时重定向到 403）
sa.checkAuth('user:delete');

// 在模板中检查权限（显示/隐藏元素）
<button v-if="sa.isAuth('user:delete')">删除</button>

// 退出登录时清除
sa.clearAuth();
```

**重要：** 这仅是 UI 层面的。真正的授权必须在后端完成。永远不要信任客户端检查来保证安全。

## 主题系统

- 10 个内置主题定义在 `sa-frame/index/theme.css` 中，使用 CSS 变量
- 主题选择持久化在 localStorage
- 通过工具栏或 `sa_admin.themeV = N` 切换
- 要添加自定义主题，在 `theme.css` 中添加 `.theme-N` 类和 CSS 变量

## 常用 API 方法

### 标签页操作
```javascript
sa_admin.showHome();              // 显示主页标签页
sa_admin.showTabById('1-1');      // 根据菜单 id 显示标签页
sa_admin.closeTabById('1-1');     // 根据 id 关闭标签页
sa_admin.addTab({name: '新页面', url: 'http://example.com/'});  // 添加新标签页
sa_admin.showTab({name: '新页面', url: 'sa-view/xxx.html'});    // 添加并立即显示
sa_admin.f5Tab('1-1');            // 根据 id 刷新标签页
```

### 弹窗方法
```javascript
sa.msg('成功消息');
sa.error('错误消息');
sa.alert('提醒消息', function() { /* 回调 */ });
sa.confirm('确认吗？', function() { /* 确认回调 */ });
sa.loading('加载中...', 'auto');  // 'auto' 1秒后自动关闭
```

## 重要模式

### 基于哈希的路由
活动标签页在 URL 哈希（`#tabId`）中跟踪。页面刷新时，框架自动重新打开上次查看的标签页。

### Ajax 响应格式
所有 `sa.ajax()` 和 `sa.ajax2()` 期望以下格式的响应：
```javascript
{
    code: 200,        // 200 = 成功，其他 = 错误
    msg: "Success",   // 显示的消息
    data: {...}       // 响应数据
}
```

### 模拟数据模式
演示页面通过 `<script src="./data-list.js">` 引入模拟数据文件（如 `data-list.js`）。这些文件定义 `var mockData = {...}`，由 `sa.ajax2()` 返回。

## `sa-view/` 中的文件组织

- `console/` - 带图表的仪表盘
- `user/` - 用户 CRUD 操作
- `article/` - 文章管理
- `role/` - 角色和权限
- `home/` - 首页设置（轮播图/swiper）
- `sys-type/` - 分类管理
- `cfg/` - 系统配置
- `case/` - 示例页面（查询、表格、表单模式）
- `error-page/` - 403、404、500 页面
- `sa-doc/` - 内置文档

## 导航组件（在 `sa-frame/nav/` 中）

这些通过 httpVueLoader 加载并渲染管理框架：
- `nav-logo.vue` - 左上角 Logo
- `nav-menu-bar.vue` - 左侧菜单栏（递归 4 层）
- `nav-tool-bar.vue` - 顶部工具栏（折叠、搜索、用户下拉、主题、全屏）
- `nav-tab-bar.vue` - 标签栏，支持拖拽排序（使用 vuedraggable）
- `nav-view-vessel.vue` - 主内容容器（渲染 iframe 或 .vue 组件）
- `com-right-menu.vue` - 标签页右键菜单
- `com-add-tab.vue` - 添加自定义外部标签页的对话框

**不要修改这些组件。** 使用 `sa_admin` API 代替。

## 自定义理念

框架设计为**无需修改源代码**即可使用。所有自定义通过以下方式进行：
1. 编辑 `sa-frame/sa-code.js`（配置）
2. 编辑 `sa-frame/menu-list.js`（菜单结构）
3. 在 `sa-view/` 中创建新视图
4. 如需要，扩展 `static/sa.js`（用于共享工具）

这种方式保持框架可更新，并防止内部修改导致的意外错误。
