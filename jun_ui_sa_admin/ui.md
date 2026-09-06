# jun_ui_sa_admin 操作文档

## 1. 项目简介

`jun_ui_sa_admin` 是基于 Sa-Admin 框架构建的若依后台管理系统前端，采用纯 HTML/JS 开发，无需 Node.js 构建工具。通过 Nginx 反向代理对接 RuoYi Spring Boot 后端，实现完整的后台管理功能。

**技术栈：**
- Vue 2.6 + Element UI 2.13（CDN 加载）
- jQuery 3.4
- Sa-Admin v1.41.0（多标签页 iframe 框架）
- ruoyi-util.js（核心桥接库）

---

## 2. 目录结构

```
jun_ui_sa_admin/
├── index.html                  # 主框架页面（菜单 + 标签页 + 内容区）
├── login.html                  # 登录页面
├── main.html                   # 欢迎页
├── nginx.conf.example          # Nginx 配置参考
│
├── sa-frame/                   # 框架核心
│   ├── sa-code.js              # 初始化配置（Token检查、动态菜单、用户信息）
│   ├── menu-list.js            # 菜单列表（已改为动态加载，设为空数组）
│   ├── index/                  # 框架引擎（index.js、theme.css 等）
│   ├── nav/                    # 导航组件（菜单栏、标签栏、工具栏）
│   ├── com/                    # 可复用表单组件（sa-item、sa-info、sa-td）
│   └── login/                  # 登录页资源（背景图、粒子动画）
│
├── sa-view/                    # 业务页面
│   ├── console/                # 首页仪表盘
│   ├── system/                 # 系统管理（12个页面）
│   │   ├── user/               # 用户管理、分配角色、个人中心
│   │   ├── role/               # 角色管理、分配用户
│   │   ├── menu/               # 菜单管理
│   │   ├── dept/               # 部门管理
│   │   ├── post/               # 岗位管理
│   │   ├── dict/               # 字典类型、字典数据
│   │   ├── config/             # 参数设置
│   │   └── notice/             # 通知公告
│   ├── monitor/                # 系统监控（8个页面）
│   │   ├── online/             # 在线用户
│   │   ├── job/                # 定时任务、任务日志
│   │   ├── operlog/            # 操作日志
│   │   ├── logininfor/         # 登录日志
│   │   ├── server/             # 服务监控
│   │   └── cache/              # 缓存监控、缓存列表
│   ├── tool/                   # 系统工具（3个页面）
│   │   ├── gen/                # 代码生成、生成配置
│   │   └── swagger/            # 系统接口
│   └── error-page/             # 错误页面（403、404、500）
│
└── static/                     # 静态资源
    ├── sa.js                   # 框架工具库（ajax、弹窗、权限等）
    ├── sa.css                  # 框架样式
    ├── ruoyi-util.js           # RuoYi 核心桥接库
    ├── ruoyi.css               # RuoYi 业务样式
    └── kj/                     # 第三方库（ECharts、wangEditor、layer 等）
```

---

## 3. 环境部署

### 3.1 前置条件

- RuoYi 后端服务已启动（默认端口 8080）
- Nginx 已安装
- 数据库已初始化（MySQL）

### 3.2 Nginx 配置

编辑 Nginx 配置文件，参考 `nginx.conf.example`：

```nginx
server {
    listen       80;
    server_name  localhost;

    # 前端静态文件
    location / {
        root   /path/to/jun_ui_sa_admin;
        index  index.html login.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 反向代理
    location /prod-api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        client_max_body_size 50m;
    }
}
```

将 `/path/to/jun_ui_sa_admin` 替换为实际的项目目录路径。

### 3.3 启动步骤

1. 启动 RuoYi 后端服务（端口 8080）
2. 启动 Nginx
3. 浏览器访问 `http://localhost`（或配置的域名）
4. 自动跳转到登录页面

### 3.4 默认账号

| 账号 | 密码 | 说明 |
|------|------|------|
| admin | admin123 | 超级管理员 |
| ry | admin123 | 普通用户 |

---

## 4. 登录流程

1. 访问 `login.html`，页面自动加载验证码图片（`GET /captchaImage`）
2. 输入用户名、密码、验证码，点击"登录"
3. 前端调用 `POST /login` 发送 `{username, password, code, uuid}`
4. 登录成功后，Token 存储到 Cookie（`Admin-Token`），跳转到 `index.html`
5. 主框架加载时：
   - 调用 `GET /getInfo` 获取用户信息和权限
   - 调用 `GET /getRouters` 获取动态菜单
   - 将后端路由格式转换为 Sa-Admin 菜单格式并渲染

**记住密码功能：** 勾选后，用户名和密码（Base64 编码）保存在 Cookie 中，有效期 30 天。

---

## 5. 功能页面说明

### 5.1 系统管理

#### 用户管理 (`system/user/index.html`)
- 左侧部门树筛选 + 右侧用户列表
- 支持新增、编辑、删除、导入、导出
- 重置密码、分配角色、启用/禁用状态切换
- 权限标识：`system:user:add/edit/remove/import/export`

#### 角色管理 (`system/role/index.html`)
- 角色 CRUD + 菜单权限树（支持全选/反选、父子联动开关）
- 数据权限设置（全部/自定义/本部门/本部门及以下/仅本人）
- 分配用户功能
- 权限标识：`system:role:add/edit/remove/export`

#### 菜单管理 (`system/menu/index.html`)
- 树形表格展示，支持展开/折叠
- 三种菜单类型：目录(M)、菜单(C)、按钮(F)
- 配置路由路径、组件路径、权限标识、图标等
- 权限标识：`system:menu:add/edit/remove`

#### 部门管理 (`system/dept/index.html`)
- 树形表格展示部门层级
- 支持新增、编辑、删除（保护根部门）
- 权限标识：`system:dept:add/edit/remove`

#### 岗位管理 (`system/post/index.html`)
- 标准 CRUD + 导出
- 权限标识：`system:post:add/edit/remove/export`

#### 字典管理 (`system/dict/index.html`)
- 字典类型列表，点击类型名可跳转到字典数据页
- 支持刷新缓存（清除 localStorage 中的字典缓存）
- 权限标识：`system:dict:add/edit/remove/export`

#### 字典数据 (`system/dict/data.html`)
- 通过 URL 参数 `dictId`、`dictType` 关联到具体字典
- 支持标签样式配置（default/primary/success/info/warning/danger）
- 权限标识：`system:dict:add/edit/remove/export`

#### 参数设置 (`system/config/index.html`)
- 系统参数键值对管理
- 区分系统内置(Y)和非内置(N)
- 支持刷新缓存
- 权限标识：`system:config:add/edit/remove/export`

#### 通知公告 (`system/notice/index.html`)
- 支持通知和公告两种类型
- 集成 wangEditor 富文本编辑器
- 权限标识：`system:notice:add/edit/remove`

### 5.2 系统监控

#### 在线用户 (`monitor/online/index.html`)
- 查看当前在线用户列表
- 支持强制退出操作
- 权限标识：`monitor:online:forceLogout`

#### 定时任务 (`monitor/job/index.html`)
- 任务 CRUD + 状态切换（暂停/启动）
- 支持执行一次、查看任务详情、查看执行日志
- Cron 表达式配置、执行策略、并发控制
- 权限标识：`monitor:job:add/edit/remove/export/changeStatus`

#### 任务日志 (`monitor/job/log.html`)
- 通过 URL 参数 `jobId`、`jobName` 关联到具体任务
- 支持查看详情、删除、清空、导出

#### 操作日志 (`monitor/operlog/index.html`)
- 只读查看，支持详情弹窗
- 按系统模块、操作人员、业务类型、状态筛选
- 支持删除、清空、导出
- 权限标识：`monitor:operlog:remove/export`

#### 登录日志 (`monitor/logininfor/index.html`)
- 登录记录查看，支持排序
- 支持解锁账户、删除、清空、导出
- 权限标识：`monitor:logininfor:remove/export/unlock`

#### 服务监控 (`monitor/server/index.html`)
- 展示 CPU、内存、JVM、磁盘、服务器信息
- 使用进度条显示使用率，颜色编码（绿 < 50%、黄 50-80%、红 >= 80%）

#### 缓存监控 (`monitor/cache/index.html`)
- Redis 基本信息展示（版本、连接数、内存等）
- ECharts 图表：命令统计饼图 + 内存使用仪表盘

#### 缓存列表 (`monitor/cache/list.html`)
- 三栏布局：缓存名称 → 键名列表 → 值详情
- 支持按名称/键名/全部清理缓存

### 5.3 系统工具

#### 代码生成 (`tool/gen/index.html`)
- 导入数据库表、预览生成代码、下载代码
- 支持同步数据库结构
- 权限标识：`tool:gen:code/import/edit/remove`

#### 生成配置 (`tool/gen/editTable.html`)
- 三个配置标签页：基本信息、生成信息、字段信息
- 支持单表(crud)、树表(tree)、主子表(sub) 三种模板
- 可配置生成路径、包名、模块名、菜单归属等

#### 系统接口 (`tool/swagger/index.html`)
- iframe 嵌入后端 Swagger 文档页面（`/prod-api/doc.html`）

---

## 6. 核心机制

### 6.1 认证机制

- Token 存储在 Cookie 中，键名 `Admin-Token`
- 所有 API 请求自动附带 `Authorization: Bearer {token}` 请求头
- 后端返回 401 时自动清除 Token 并跳转登录页
- 后端返回 601 时弹出警告提示

### 6.2 权限控制

**菜单权限：** 后端 `GET /getRouters` 返回用户可访问的菜单，前端动态渲染。

**操作权限：** 使用 Vue 指令控制按钮显示：
```html
<el-button v-hasPermi="['system:user:add']">新增</el-button>
<el-button v-hasRole="['admin']">管理员操作</el-button>
```

权限数据来自 `GET /getInfo` 返回的 `permissions` 和 `roles` 数组。

### 6.3 字典缓存

- 字典数据缓存在 `localStorage`，键名格式：`ruoyi_dict_{dictType}`
- 页面中通过 `ruoyi.loadDicts(vm, ['dict_type1', 'dict_type2'])` 加载
- 字典管理页面提供"刷新缓存"按钮清除本地缓存

### 6.4 动态菜单

后端返回 Vue Router 格式的菜单数据，前端通过 `ruoyi.convertRoutersToMenuList()` 转换为 Sa-Admin 的菜单格式：

| 后端字段 | Sa-Admin 字段 |
|----------|--------------|
| name | id |
| meta.title | name |
| meta.icon | icon（转换为 Element UI 图标类） |
| component path | url |
| children | childList |

### 6.5 页面间导航

从一个页面打开新标签页：
```javascript
parent.sa_admin.showTab({
    id: 'unique-tab-id',
    name: '页面标题',
    url: 'sa-view/module/page.html?param=value'
});
```

关闭当前标签页：
```javascript
parent.sa_admin.closeTabById('tab-id');
```

### 6.6 文件上传

上传组件需携带 Token：
```javascript
uploadHeaders: { Authorization: 'Bearer ' + ruoyi.getToken() }
```

上传地址使用 `/prod-api` 前缀，通过 Nginx 转发到后端。

---

## 7. 页面文件清单

### 新建文件

| 文件 | 说明 |
|------|------|
| `static/ruoyi-util.js` | 核心桥接库（Token、HTTP、字典、权限、菜单转换） |
| `static/ruoyi.css` | RuoYi 业务样式 |
| `nginx.conf.example` | Nginx 配置参考 |
| `sa-view/system/user/index.html` | 用户管理 |
| `sa-view/system/user/authRole.html` | 用户分配角色 |
| `sa-view/system/user/profile/index.html` | 个人中心 |
| `sa-view/system/role/index.html` | 角色管理 |
| `sa-view/system/role/authUser.html` | 角色分配用户 |
| `sa-view/system/menu/index.html` | 菜单管理 |
| `sa-view/system/dept/index.html` | 部门管理 |
| `sa-view/system/post/index.html` | 岗位管理 |
| `sa-view/system/dict/index.html` | 字典类型 |
| `sa-view/system/dict/data.html` | 字典数据 |
| `sa-view/system/config/index.html` | 参数设置 |
| `sa-view/system/notice/index.html` | 通知公告 |
| `sa-view/monitor/online/index.html` | 在线用户 |
| `sa-view/monitor/job/index.html` | 定时任务 |
| `sa-view/monitor/job/log.html` | 任务日志 |
| `sa-view/monitor/operlog/index.html` | 操作日志 |
| `sa-view/monitor/logininfor/index.html` | 登录日志 |
| `sa-view/monitor/server/index.html` | 服务监控 |
| `sa-view/monitor/cache/index.html` | 缓存监控 |
| `sa-view/monitor/cache/list.html` | 缓存列表 |
| `sa-view/tool/gen/index.html` | 代码生成 |
| `sa-view/tool/gen/editTable.html` | 生成配置 |
| `sa-view/tool/swagger/index.html` | 系统接口 |

### 修改文件

| 文件 | 修改内容 |
|------|----------|
| `static/sa.js` | `api_url` 改为 `/prod-api` |
| `index.html` | 添加 `ruoyi-util.js` 和 `ruoyi.css` 引用 |
| `login.html` | 重写为真实登录（验证码 + 后端认证） |
| `sa-frame/sa-code.js` | 重写为动态菜单加载 + 用户信息获取 |
| `sa-frame/menu-list.js` | 清空为空数组（改用动态菜单） |
| `main.html` | 更新为系统欢迎页 |
| `sa-view/console/console-main.html` | 更新为用户信息 + 系统信息仪表盘 |

---

## 8. API 接口汇总

### 认证相关

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/captchaImage` | 获取验证码图片 |
| POST | `/login` | 用户登录 |
| POST | `/logout` | 退出登录 |
| GET | `/getInfo` | 获取用户信息和权限 |
| GET | `/getRouters` | 获取动态菜单路由 |

### 系统管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET/POST/PUT/DELETE | `/system/user/*` | 用户管理 CRUD |
| PUT | `/system/user/changeStatus` | 用户状态切换 |
| PUT | `/system/user/resetPwd` | 重置密码 |
| PUT | `/system/user/authRole` | 用户分配角色 |
| GET/PUT | `/system/user/profile` | 个人资料 |
| POST | `/system/user/profile/avatar` | 头像上传 |
| GET/POST/PUT/DELETE | `/system/role/*` | 角色管理 CRUD |
| PUT | `/system/role/dataScope` | 数据权限设置 |
| GET/PUT | `/system/role/authUser/*` | 角色用户分配 |
| GET/POST/PUT/DELETE | `/system/menu/*` | 菜单管理 CRUD |
| GET | `/system/menu/treeselect` | 菜单树数据 |
| GET/POST/PUT/DELETE | `/system/dept/*` | 部门管理 CRUD |
| GET | `/system/dept/treeselect` | 部门树数据 |
| GET/POST/PUT/DELETE | `/system/post/*` | 岗位管理 CRUD |
| GET/POST/PUT/DELETE | `/system/dict/type/*` | 字典类型 CRUD |
| GET/POST/PUT/DELETE | `/system/dict/data/*` | 字典数据 CRUD |
| GET/POST/PUT/DELETE | `/system/config/*` | 参数设置 CRUD |
| GET/POST/PUT/DELETE | `/system/notice/*` | 通知公告 CRUD |

### 系统监控

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/monitor/online/list` | 在线用户列表 |
| DELETE | `/monitor/online/{tokenId}` | 强制退出 |
| GET/POST/PUT/DELETE | `/monitor/job/*` | 定时任务 CRUD |
| PUT | `/monitor/job/run` | 执行一次任务 |
| GET/DELETE | `/monitor/jobLog/*` | 任务日志 |
| GET/DELETE | `/monitor/operlog/*` | 操作日志 |
| GET/DELETE | `/monitor/logininfor/*` | 登录日志 |
| GET | `/monitor/logininfor/unlock/{userName}` | 解锁账户 |
| GET | `/monitor/server` | 服务器信息 |
| GET | `/monitor/cache` | 缓存监控数据 |
| GET/DELETE | `/monitor/cache/*` | 缓存列表操作 |

### 系统工具

| 方法 | 路径 | 说明 |
|------|------|------|
| GET/PUT/DELETE | `/tool/gen/*` | 代码生成 |
| POST | `/tool/gen/importTable` | 导入表 |
| GET | `/tool/gen/synchDb/{tableName}` | 同步数据库 |
| GET | `/tool/gen/batchGenCode` | 批量生成代码 |

---

## 9. 常见问题

### Q: 页面打开后显示空白或报错
检查浏览器控制台是否有 CORS 或 401 错误。确认 Nginx 的 `/prod-api/` 反向代理配置正确，后端服务正常运行。

### Q: 登录时验证码不显示
确认后端验证码功能已开启，`GET /captchaImage` 接口可正常访问。

### Q: 菜单不显示
确认登录用户已分配角色，角色已关联菜单权限。检查 `GET /getRouters` 返回数据是否正常。

### Q: 文件上传失败
检查 Nginx 的 `client_max_body_size` 配置是否足够大，默认设为 50m。

### Q: 字典数据显示为空
首次加载后字典会缓存在 localStorage 中。如果后台修改了字典数据，需要在字典管理页面点击"刷新缓存"，或手动清除浏览器 localStorage。

### Q: 页面样式异常
确认 `static/ruoyi.css` 和 `static/sa.css` 文件正常加载。各业务页面使用相对路径引用（2 层深度用 `../../static/`，3 层深度用 `../../../static/`）。
