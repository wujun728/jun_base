# 前端模块迁移清单

## 迁移日期: 2026-07-08

---

## 一、项目概览

| 项目 | 来源路径 | 状态 | 说明 |
|------|----------|:--:|------|
| **项目1** (RuoYi-Vue-S) | `D:\workspace_github\jun_base_code\RuoYi-Vue-S\ruoyi-ui\` | ✅ 已迁移 | 租户管理 + 数据权限规则 |
| **OA** (ruoyi-vue-oa-ui) | `D:\workspace_github\jun_product_center_private_qixing\ruoyi-vue-oa-ui\` | ✅ 已迁移 | 33个OA模块 + workflow + 知识库 |
| **Geek** (ruoyi-geek-vue3) | `D:\workspace_github\jun_base_code\ruoyi-springboot3\ruoyi-geek-vue3\` | ✅ 核心功能已重写 | Vue3→Vue2重写了在线接口+关联表 |

---

## 二、项目1迁移文件清单

### 新增 views/
```
src/views/system/tenant/index.vue          -- 租户管理列表
src/views/system/dataRule/index.vue         -- 数据权限管理
src/views/system/dataRule/roleDataRuleList.vue  -- 角色数据权限配置
```

### 新增 api/
```
src/api/system/tenant.js         -- 租户管理 API (CRUD + 导出)
src/api/system/dataRule.js       -- 数据权限 API (CRUD + 导出 + 角色关联)
```

### 修改文件
```
src/api/system/config.js         -- 新增 exportConfig() 导出功能
```

---

## 三、OA项目迁移文件清单

### 新增 views/ (11个模块目录)
```
src/views/qixing/       -- OA核心业务 (33个子模块)
src/views/workflow/     -- 工作流 (待办/已办/草稿/发起/流程设计)
src/views/home/         -- OA门户首页
src/views/schedule/     -- 日程管理
src/views/kbs/          -- 知识库 (文档/主题/收藏)
src/views/holiday/      -- 节假日管理
src/views/information/  -- 信息发布
src/views/notice/       -- 通知公告
src/views/setting/      -- 系统设置 (委托/秘书)
src/views/serial/       -- 流水号管理
src/views/mq/           -- 消息队列
```

### 新增 api/ (9个模块目录)
```
src/api/qixing/          -- OA核心业务API (40个文件)
src/api/workflow/        -- 工作流API (18个文件)
src/api/kbs/             -- 知识库API (9个文件)
src/api/schedule/        -- 日程API (3个文件)
src/api/serial/          -- 流水号API (2个文件)
src/api/setting/         -- 系统设置API (2个文件)
src/api/mq/              -- 消息队列API
src/api/information/     -- 信息发布API
src/api/file/            -- 文件管理API (2个文件)
src/api/system/holidaySetting.js      -- 假期设置API
src/api/system/holidayWorkSetting.js  -- 节假日补班API
```

### 新增 components/ (11个自定义组件)
```
src/components/customBpmn/     -- BPMN流程设计器
src/components/flow/           -- 流程组件 (表达式/角色/用户)
src/components/Process/        -- 流程引擎通用组件
src/components/SerialNo/       -- 流水号组件
src/components/vform/          -- VForm表单设计器
src/components/WangEditorExtend/ -- 富文本扩展
src/components/org/            -- 组织架构组件
src/components/form/           -- 动态表单组件
src/components/parser/         -- 表单解析器
src/components/render/         -- 表单渲染器
src/components/tinymce/        -- Tinymce编辑器
```

### 新增 store/modules/
```
uiStore.js           -- UI状态存储
wangEditorStore.js   -- WangEditor状态存储
```

### 新增 utils/
```
socket/wssocket.js       -- WebSocket长连接
directive/dialogDrag.js  -- 弹窗拖拽指令
db.js, base64.js, fileMap.js, StrUtil.js, pluginsConfig.js
loadBeautifier.js, loadMonaco.js, loadTinymce.js
```

### 新增 assets/
```
assets/custom_icons/    -- 自定义SVG图标
assets/custom_styles/   -- 自定义样式 (home.scss, index.scss, mixin.scss)
```

### 修改的基座文件
```
src/main.js             -- 新增 OA 模块注册 (VForm, WebSocket, 事件总线等)
src/store/index.js       -- 新增 uiStore + wangEditorStore 模块
.env.development         -- 新增 VUE_APP_PREVIEW_URL, VUE_APP_URL, VUE_APP_WS_URL
.env.production          -- 新增 VUE_APP_PREVIEW_URL, VUE_APP_URL, VUE_APP_WS_URL
package.json             -- 新增 22 个 OA 依赖包
```

---

## 四、Geek项目精选功能迁移 (Vue3→Vue2重写)

### 功能1: MyBatis在线接口 (online/mb)

| 文件 | 说明 |
|------|------|
| `src/views/online/mb/index.vue` | CRUD管理页面，支持新增/修改/删除/导出 |
| `src/views/online/mb/mybatis-input.vue` | SQL编辑器，支持MyBatis语法高亮（关键字/标签/参数着色） |
| `src/views/online/mb/online-preview.vue` | 预览与在线测试对话框（生成XML/API代码+在线调接口） |
| `src/api/online/mb.js` | 在线接口CRUD API |
| `src/api/online/db.js` | 数据库表/字段浏览API |

**后端需求**: `/online/mb/*` 和 `/online/db/*` 接口 + `online_mb` 表
**字典依赖**: `online_api_tag`, `online_api_method`, `online_api_result`, `online_api_actuator`

### 功能2: 代码生成器关联表 (join table)

| 文件 | 说明 |
|------|------|
| `src/views/tool/gen/joinTableForm.vue` | 多表JOIN关联配置组件（NEW） |
| `src/views/tool/gen/editTable.vue` | 新增"关联表"Tab（MODIFIED） |
| `src/api/tool/gen.js` | 提交时包含 joinTablesMate 数据 |

**后端需求**: 基座 `ruoyi-module-gen` 已有 GenJoin/IGenJoinService，前端已对接

### 修改的基座文件 (追加)
```
src/views/tool/gen/editTable.vue  -- 新增关联表Tab + joins数据提交
```

---

## 五、SQL迁移文件清单

### sql/migration/ 目录
```
migrate_project1_tenant_datarule.sql   -- 项目1: 3张表 (sys_tenant, sys_data_rule, sys_role_data_rule) + 字典数据
migrate_oa_custom_tables.sql           -- OA: 48张自定义表 (t_* 前缀)
oa_init_template_qixing.sql            -- OA: 工作流模板初始化数据 (23条)
oa_init_data.sql                       -- OA: 系统初始化数据 (菜单/角色/用户/部门等)
migrate_geek_online_genjoin.sql        -- Geek: 2张表 (online_mb + gen_join) + 4个字典类型
```

---

## 六、保持基座可升级的策略

1. **目录隔离**: 所有自定义视图/API/组件直接放在 `src/` 对应目录下, 与基座标准目录共存
2. **最小侵入**: 仅修改 main.js、store/index.js、package.json、.env 四个基座文件
3. **模块标记**: 所有新增代码用 `// OA模块:` 注释标记
4. **禁止覆盖**: 基座原有文件不动, 仅新增同级别目录
5. **版本锁定**: package.json 中保持基座原有的核心依赖版本不变

---

## 七、后续工作

1. **后端迁移**: 需同步迁移后端的 Mapper/Service/Controller/Entity
2. **菜单配置**: 在后台"系统管理→菜单管理"中添加新模块菜单
   - 在线接口: 组件路径 `online/mb/index`
   - 关联表功能: 已集成在现有代码生成器编辑页中,无需额外菜单
3. **npm install**: 前端需重新安装依赖 (新增22个包)
4. **数据库初始化**: 执行 sql/migration/ 下的脚本
5. **字典初始化**: 在线接口模块需要以下字典类型
   - `online_api_tag`: select/insert/update/delete
   - `online_api_method`: GET/POST/PUT/DELETE
   - `online_api_result`: (后端定义)
   - `online_api_actuator`: (后端定义)
6. **测试验证**: 逐个模块验证功能正常
