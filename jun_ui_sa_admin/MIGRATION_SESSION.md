# Vue2 → Sa-Admin 迁移会话记录

**日期**：2026-05-25  
**状态**：方案已确认，待执行

---

## 项目背景

- **sa-admin**：`D:\workspace_github\jun_base_vue_mybatisplus\jun_ui_sa_admin`
  - 纯 HTML/JS iframe 框架，无需构建，Sa-Admin v1.41.0
  - 技术栈：Vue 2.6 + Element UI 2.13 + jQuery + http-vue-loader
  - 支持三种页面风格：`.vue`（主）/ `.html` / `.js`
  
- **ruoyi-ui**：`D:\workspace_github\jun_base_vue_mybatisplus\ruoyi-ui`
  - 标准 Vue2 SPA，webpack 构建
  - 迁移源代码

- **目标**：用 nginx 部署 sa-admin，替代 ruoyi-ui，原 vue2 代码归档保留

---

## 已完成工作

1. ✅ 探索了两个前端的完整代码结构
2. ✅ 分析了 `ruoyi-util.js` 桥接库（已完备）
3. ✅ 制定了完整迁移方案（见下方）
4. ✅ 方案文件保存在 `C:\Users\Administrator\.claude\plans\buzzing-splashing-firefly.md`

---

## 迁移方案（完整版）

### 三种代码风格定位

| 方式 | 适用场景 | 优先级 |
|------|----------|--------|
| `.vue` 文件（http-vue-loader） | 所有业务 CRUD 页面 | **主要** |
| `.html` 文件 | 现有已迁移页面保持原状 | 次要 |
| `.js` 文件 | 工具库（ruoyi-util.js 等） | 仅工具 |

### 关键转换对照表

| vue2 SPA 写法 | sa-admin `.vue` 写法 | 备注 |
|---|---|---|
| `import { listUser } from '@/api/system/user'` | `ruoyi.get('/system/user/list', params, cb)` | 无需 import |
| `export default {}` | `module.exports = {}` | CommonJS |
| `this.$store.getters.permissions` | `ruoyi.hasPermi('xxx:xxx')` | 权限检查 |
| `v-hasPermi="['user:add']"` | `v-hasPermi="['user:add']"` | ✅ 完全一致 |
| `this.getDicts('sys_xxx')` | `ruoyi.getDicts('sys_xxx', cb)` | 改为回调风格 |
| `this.parseTime(t)` | `this.parseTime(t)` | ✅ 已挂载 Vue.prototype |
| `this.handleTree(data)` | `this.handleTree(data)` | ✅ 已挂载 Vue.prototype |
| `this.$modal.msgSuccess(m)` | `this.$modal.msgSuccess(m)` | ✅ 完全一致 |
| `this.resetForm('form')` | `this.resetForm('form')` | ✅ 完全一致 |
| `this.addDateRange(p, r)` | `this.addDateRange(p, r)` | ✅ 完全一致 |
| `this.download(url, params)` | `ruoyi.download(url, params, filename)` | 多一个 filename 参数 |
| `<Pagination>` 组件 | `<el-pagination>` 原生 | 直接用 Element UI |
| `<DictTag>` 组件 | `ruoyi.selectDictLabel(opts, val)` | 内联调用 |
| `<Treeselect>` 组件 | `<el-cascader>` 或 CDN 引入 | 需额外处理 |

### 标准 `.vue` 文件模板

```vue
<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item label="xxx" prop="xxx">
        <el-input v-model="queryParams.xxx" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini"
          v-hasPermi="['system:xxx:add']" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit"
            v-hasPermi="['system:xxx:edit']" @click="handleUpdate(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page.sync="queryParams.pageNum"
      :page-size.sync="queryParams.pageSize"
      :total="total"
      @current-change="getList"
      @size-change="getList"
      layout="total, sizes, prev, pager, next, jumper" />
  </div>
</template>

<script>
module.exports = {
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10 },
      statusOptions: [],
    }
  },
  methods: {
    getList() {
      this.loading = true;
      ruoyi.get('/system/xxx/list', this.queryParams, (res) => {
        this.list = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm('queryForm');
      this.handleQuery();
    },
    handleDelete(row) {
      this.$modal.confirm('确认删除？').then(() => {
        ruoyi.del('/system/xxx/' + row.id, () => {
          this.$modal.msgSuccess('删除成功');
          this.getList();
        });
      });
    }
  },
  created() {
    ruoyi.getDicts('sys_normal_disable', (data) => { this.statusOptions = data; });
    this.getList();
  }
}
</script>

<style scoped>
.app-container { padding: 20px; }
.mb8 { margin-bottom: 8px; }
</style>
```

### 页面迁移清单（当前进度）

| 页面 | 原文件 | sa-admin 文件 | 状态 |
|------|--------|--------------|------|
| 用户管理 | views/system/user/index.vue | sa-view/system/user/index.html | ⚠️ html版，待转.vue |
| 个人中心 | views/system/user/profile/ | sa-view/system/user/profile/index.html | ⚠️ html版 |
| 角色管理 | views/system/role/index.vue | sa-view/system/role/index.html | ⚠️ html版 |
| 角色授权 | views/system/role/authUser.vue | sa-view/system/role/authUser.html | ⚠️ html版 |
| 菜单管理 | views/system/menu/index.vue | sa-view/system/menu/index.html | ⚠️ html版 |
| 部门管理 | views/system/dept/index.vue | sa-view/system/dept/index.html | ⚠️ html版 |
| 岗位管理 | views/system/post/index.vue | sa-view/system/post/index.html | ⚠️ html版 |
| 字典类型 | views/system/dict/index.vue | sa-view/system/dict/index.html | ⚠️ html版 |
| 字典数据 | views/system/dict/data.vue | sa-view/system/dict/data.html | ⚠️ html版 |
| 参数设置 | views/system/config/index.vue | sa-view/system/config/index.html | ⚠️ html版 |
| 通知公告 | views/system/notice/index.vue | sa-view/system/notice/index.html | ⚠️ html版 |
| 在线用户 | views/monitor/online/index.vue | sa-view/monitor/online/index.html | ⚠️ html版 |
| 定时任务 | views/monitor/job/index.vue | sa-view/monitor/job/index.html | ⚠️ html版 |
| 调度日志 | views/monitor/job/log.vue | sa-view/monitor/job/log.html | ⚠️ html版 |
| 登录日志 | views/monitor/logininfor/index.vue | sa-view/monitor/logininfor/index.html | ⚠️ html版 |
| 操作日志 | views/monitor/operlog/index.vue | sa-view/monitor/operlog/index.html | ⚠️ html版 |
| 缓存监控 | views/monitor/cache/index.vue | sa-view/monitor/cache/index.html | ⚠️ html版 |
| 缓存列表 | views/monitor/cache/list.vue | sa-view/monitor/cache/list.html | ⚠️ html版 |
| 服务监控 | views/monitor/server/index.vue | sa-view/monitor/server/index.html | ⚠️ html版 |
| 数据监控 | views/monitor/druid/index.vue | ❌ 无 | ❌ 待迁移 |
| 代码生成 | views/tool/gen/index.vue | sa-view/tool/gen/index.html | ⚠️ html版 |
| 生成配置 | views/tool/gen/editTable.vue | sa-view/tool/gen/editTable.html | ⚠️ html版 |
| 表单构建 | views/tool/build/index.vue | ❌ 无 | ❌ 待迁移 |
| Swagger | views/tool/swagger/index.vue | sa-view/tool/swagger/index.html | ⚠️ html版 |

> ⚠️ html版 = 文件存在但需确认内容是否已从 ruoyi-ui 完整迁移

### 执行阶段规划

**Phase 0**：示例代码归档
- `sa-view/console/` → `sa-view/_demo/console/`
- `sa-view/cfg/` → `sa-view/_demo/cfg/`

**Phase 1**：验证现有 `.html` 文件完整性
- 检查每个 `.html` 是否已完整迁移 ruoyi-ui 对应功能

**Phase 2**：按优先级逐页迁移为 `.vue` 风格
1. system 模块（9个页面）
2. monitor 模块（8个页面）
3. tool 模块（3个页面）
4. dashboard 仪表盘

**Phase 3**：特殊页面
- `monitor/druid`：iframe 直链方式
- `tool/build`：复杂拖拽组件移植

---

## 关键文件路径速查

| 文件 | 用途 |
|------|------|
| `jun_ui_sa_admin/static/ruoyi-util.js` | 核心桥接库（HTTP/字典/权限/菜单转换） |
| `jun_ui_sa_admin/sa-frame/sa-code.js` | 动态菜单和用户信息加载入口 |
| `jun_ui_sa_admin/sa-frame/menu-list.js` | 菜单配置（已动态化） |
| `jun_ui_sa_admin/index.html` | 主框架入口 |
| `jun_ui_sa_admin/login.html` | 登录页 |
| `jun_ui_sa_admin/sa-view/` | 所有业务页面 |
| `ruoyi-ui/src/views/` | 迁移源代码 |
| `ruoyi-ui/src/api/` | API 定义参考 |

---

## nginx 部署配置

```nginx
server {
    listen 80;
    server_name your-domain.com;

    root /path/to/jun_ui_sa_admin;
    index login.html;

    location / {
        try_files $uri $uri/ =404;
    }

    location /prod-api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 下次继续时的起点

1. 确认从哪个阶段开始执行（Phase 0 归档 or 直接 Phase 2 迁移）
2. 建议第一个迁移示范页面：`system/user/index.vue`（最典型的 CRUD 页面）
3. 迁移完一个页面后，确立标准模板，后续批量迁移

---

*记录时间：2026-05-25*  
*记录人：Claude Code*
