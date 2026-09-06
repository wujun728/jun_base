# ArtCrud 通用CRUD组件

基于现有的 `art-search-bar`、`art-table`、`art-table-header`、`art-form` 组件封装的通用 CRUD 组件，支持完整的增删改查功能，高度可配置。

## 特性

- 🚀 开箱即用的 CRUD 功能
- 🎯 高度可配置，支持自定义 API、表格列、搜索表单、编辑表单
- 🎨 丰富的插槽支持，满足各种自定义需求
- 📱 响应式设计，支持移动端
- 🔧 支持批量操作
- ⚡ 基于 `useTable` composable，性能优化
- 🎭 支持前置后置钩子，灵活控制业务逻辑

## 快速开始

### 基础用法

```vue
<template>
  <ArtCrud :config="crudConfig" />
</template>

<script setup lang="ts">
import ArtCrud from '@/components/custom/art-crud/index.vue'
import { ref } from 'vue'

const crudConfig = ref({
  // API 配置
  api: {
    list: fetchUserList, // 必须：列表查询接口
    add: addUser, // 可选：新增接口
    update: updateUser, // 可选：更新接口
    delete: deleteUser // 可选：删除接口
  },

  // 表格列配置
  columns: [
    { type: 'selection' as const },
    { type: 'index' as const, label: '序号', width: 60 },
    { prop: 'name', label: '姓名', minWidth: 120 },
    { prop: 'email', label: '邮箱', minWidth: 200 }
  ],

  // 搜索配置
  search: {
    items: [
      { key: 'name', label: '姓名', type: 'input', placeholder: '请输入姓名' },
      { key: 'status', label: '状态', type: 'select', options: statusOptions }
    ]
  },

  // 表单配置
  form: {
    items: [
      { key: 'name', label: '姓名', type: 'input', placeholder: '请输入姓名' },
      { key: 'email', label: '邮箱', type: 'input', placeholder: '请输入邮箱' }
    ]
  }
})
</script>
```

## 配置说明

### CrudConfig 接口

```typescript
interface CrudConfig {
  // API 相关
  api: {
    list: (params: any) => Promise<any> // 必须：列表查询
    add?: (data: any) => Promise<any> // 可选：新增
    update?: (data: any) => Promise<any> // 可选：更新
    delete?: (id: string | number) => Promise<any> // 可选：删除
    batchDelete?: (ids: (string | number)[]) => Promise<any> // 可选：批量删除
  }

  // 表格列配置
  columns: ColumnOption[]

  // 搜索配置
  search?: {
    items: SearchFormItem[]
    defaultValues?: Record<string, any>
    [key: string]: any
  }

  // 表单配置
  form?: {
    items: FormItem[]
    defaultValues?: Record<string, any>
    [key: string]: any
  }

  // 新增配置
  add?: {
    text?: string // 按钮文字
    beforeSubmit?: (data: any) => boolean | Promise<boolean> // 提交前钩子
    afterSubmit?: (data: any, response: any) => void // 提交后钩子
  }

  // 编辑配置
  edit?: {
    beforeSubmit?: (data: any) => boolean | Promise<boolean>
    afterSubmit?: (data: any, response: any) => void
  }

  // 删除配置
  delete?: {
    confirmText?: string
    beforeDelete?: (row: any) => boolean | Promise<boolean>
    afterDelete?: (row: any) => void
  }

  // 弹窗配置
  dialog?: {
    width?: string
    titles?: {
      add?: string
      edit?: string
      view?: string
    }
  }

  // 其他配置
  table?: Record<string, any> // 传递给 ArtTable 的额外属性
  tableHeader?: Record<string, any> // 传递给 ArtTableHeader 的额外属性
  initialParams?: Record<string, any> // 初始查询参数
}
```

### Props

| 属性名       | 类型            | 默认值 | 说明             |
| ------------ | --------------- | ------ | ---------------- |
| config       | `CrudConfig`    | -      | CRUD 配置对象    |
| showAdd      | `boolean`       | `true` | 是否显示新增按钮 |
| batchActions | `BatchAction[]` | -      | 批量操作配置     |
| immediate    | `boolean`       | `true` | 是否立即加载数据 |

### 批量操作配置

```typescript
interface BatchAction {
  key: string // 唯一标识
  text: string // 按钮文字
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'default'
  icon?: any // 图标组件
  handler: (selectedRows: any[]) => void | Promise<void> // 处理函数
}
```

## 插槽

### 搜索栏插槽

```vue
<!-- 搜索表单字段插槽 -->
<template #search-{fieldKey}="{ item, modelValue }">
  <!-- 自定义搜索字段 -->
</template>
```

### 表格插槽

```vue
<!-- 表格列插槽 -->
<template #table-{columnProp}="{ row, column, $index }">
  <!-- 自定义表格列内容 -->
</template>
```

### 表单插槽

```vue
<!-- 表单字段插槽 -->
<template #form-{fieldKey}="{ item, modelValue }">
  <!-- 自定义表单字段 -->
</template>
```

### 其他插槽

```vue
<!-- 表格头部左侧插槽 -->
<template #header-left="{ selectedRows }">
  <!-- 自定义左侧操作按钮 -->
</template>

<!-- 表格头部右侧插槽 -->
<template #header-right>
  <!-- 自定义右侧操作按钮 -->
</template>

<!-- 对话框自定义内容插槽 -->
<template #dialog-content="{ formData, dialogType, currentRow }">
  <!-- 自定义对话框内容 -->
</template>
```

## 暴露的方法

```typescript
{
  refreshData: () => Promise<void>     // 刷新数据
  getData: () => Promise<void>         // 获取数据
  showDialog: (type: DialogType, row?: any) => void  // 显示对话框
  selectedRows: Ref<any[]>             // 选中的行
  searchForm: Ref<Record<string, any>> // 搜索表单数据
  formData: Ref<Record<string, any>>   // 表单数据
  dialogVisible: Ref<boolean>          // 对话框显示状态
}
```

## 完整示例

参考 `example.vue` 文件查看完整的使用示例，包含：

- 自定义搜索字段（下拉选择）
- 自定义表格列（头像、状态标签）
- 自定义表单字段（文件上传）
- 批量操作（批量删除、导出）
- 前置后置钩子使用
- 各种插槽的使用

## 注意事项

1. **API 接口规范**：确保列表接口返回的数据格式符合 `useTable` 的要求
2. **列配置**：表格列配置中的 `type` 字段需要使用 `as const` 断言
3. **插槽命名**：插槽名称格式为 `{区域}-{字段名}`，如 `search-status`、`table-avatar`
4. **权限控制**：可以通过配置中的各种钩子函数实现权限控制
5. **样式定制**：可以通过 CSS 变量或覆盖样式进行定制

## 依赖

- Vue 3.x
- Element Plus
- @vueuse/core
- 现有的 art-\* 组件库
