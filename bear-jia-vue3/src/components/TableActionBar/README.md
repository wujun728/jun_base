# TableActionBar - 表格操作列组件

## 组件说明

表格操作列组件，提供查看、编辑、删除等常用操作，并支持通过"更多"下拉菜单显示额外操作。

### 功能特性

- ✅ **智能自适应模式**：根据操作数量自动调整显示策略
  - 2个或更少：全部显示
  - 3个或更多：显示1个 + "更多"下拉菜单
- ✅ 统一的操作数组配置（推荐使用）
- ✅ 支持权限控制（v-hasPermi）
- ✅ 支持自定义图标、文字、颜色
- ✅ 内置查看、编辑、删除三个常用操作（向后兼容）
- ✅ 支持自定义操作按钮（通过插槽）
- ✅ 暗黑主题适配

### 使用示例

#### 1. 智能自适应模式（推荐 ⭐）

```vue
<template>
  <BearJiaProTable :columns="columns">
    <template #bodyCell="{ column, record }">
      <TableActionBar
        v-if="column.dataIndex === 'action'"
        :record="record"
        :actions="getActions(record)"
        :auto-adaptive="true"
      />
    </template>
  </BearJiaProTable>
</template>

<script setup>
import TableActionBar from '@/components/TableActionBar/index.vue';

const getActions = (record) => {
  return [
    {
      label: '查看',
      icon: 'eye-outlined',
      type: 'view',
      onClick: (record) => handleView(record)
    },
    {
      label: '修改',
      icon: 'edit-outlined',
      type: 'edit',
      onClick: (record) => handleEdit(record)
    },
    {
      label: '重置密码',
      icon: 'key-outlined',
      onClick: (record) => handleResetPwd(record)
    },
    {
      label: '删除',
      icon: 'delete-outlined',
      type: 'delete',
      danger: true,
      onClick: (record) => handleDelete(record)
    }
  ];
};
</script>
```

**智能显示效果：**
- 如果有 2 个操作：直接显示 2 个按钮
- 如果有 3 个操作：显示"查看" + "更多▼"（包含 修改、重置密码）
- 如果有 4 个操作：显示"查看" + "更多▼"（包含 修改、重置密码、删除）

#### 2. 基础用法（内置操作 - 向后兼容）

```vue
<template>
  <BearJiaProTable :columns="columns">
    <template #bodyCell="{ column, record }">
      <TableActionBar
        v-if="column.dataIndex === 'action'"
        :record="record"
        :view="true"
        :edit="true"
        :delete="true"
        @view="handleView"
        @edit="handleEdit"
        @delete="handleDelete"
      />
    </template>
  </BearJiaProTable>
</template>

<script setup>
import TableActionBar from '@/components/TableActionBar/index.vue';

const handleView = (record) => {
  console.log('查看', record);
};

const handleEdit = (record) => {
  console.log('编辑', record);
};

const handleDelete = (record) => {
  console.log('删除', record);
};
</script>
```

#### 3. 使用更多操作下拉菜单（旧方式）

```vue
<template>
  <TableActionBar
    :record="record"
    :view="true"
    :edit="false"
    :delete="false"
    :more-actions="getMoreActions(record)"
    @view="handleView"
  />
</template>

<script setup>
const getMoreActions = (record) => {
  return [
    {
      label: '修改',
      icon: 'edit-outlined',
      onClick: (record) => {
        console.log('修改', record);
      }
    },
    {
      label: '重置密码',
      icon: 'key-outlined',
      onClick: (record) => {
        console.log('重置密码', record);
      }
    },
    {
      label: '删除',
      icon: 'delete-outlined',
      danger: true,
      disabled: record.userName === 'admin',
      onClick: (record) => {
        console.log('删除', record);
      }
    }
  ];
};
</script>
```

#### 4. 完整配置示例（旧方式）

```vue
<template>
  <TableActionBar
    :record="record"
    :view="{
      show: true,
      text: '详情',
      icon: 'eye-outlined',
      permissions: ['system:user:query'],
      onClick: handleViewDetail
    }"
    :edit="{
      show: hasPermi('system:user:edit'),
      text: '编辑',
      icon: 'edit-outlined',
      permissions: ['system:user:edit']
    }"
    :delete="{
      show: record.userName !== 'admin',
      text: '删除',
      icon: 'delete-outlined',
      permissions: ['system:user:remove']
    }"
    :more-actions="[
      {
        label: '分配角色',
        icon: 'usergroup-add-outlined',
        permissions: ['system:user:role'],
        onClick: handleAssignRole
      },
      {
        label: '重置密码',
        icon: 'key-outlined',
        permissions: ['system:user:resetPwd'],
        onClick: handleResetPwd
      }
    ]"
    @view="handleView"
    @edit="handleEdit"
    @delete="handleDelete"
  />
</template>
```

#### 4. 使用插槽自定义操作

```vue
<template>
  <TableActionBar :record="record">
    <!-- 使用命名插槽替换默认操作 -->
    <template #view>
      <a class="action-btn custom-btn" @click="handleCustomView">
        <BearJiaIcon icon="search-outlined" /> 查看详情
      </a>
    </template>

    <!-- 使用 actions 插槽添加自定义操作 -->
    <template #actions>
      <a class="action-btn warning-btn" @click="handleCustomAction">
        <BearJiaIcon icon="warning-outlined" /> 自定义操作
      </a>
    </template>
  </TableActionBar>
</template>
```

### Props

| 参数 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| record | 当前行数据 | `Object` | - |
| view | 查看按钮配置 | `Boolean \| Object` | `true` |
| edit | 编辑按钮配置 | `Boolean \| Object` | `true` |
| delete | 删除按钮配置 | `Boolean \| Object` | `false` |
| moreActions | 更多操作配置数组 | `Array<Action>` | `[]` |

### 按钮配置对象（view/edit/delete）

当使用对象配置时，支持以下属性：

| 参数 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| show | 是否显示 | `Boolean` | `true` |
| text | 按钮文字 | `String` | - |
| icon | 图标名称 | `String` | - |
| permissions | 权限标识 | `Array<String>` | - |
| onClick | 点击回调 | `(record) => void` | - |

### MoreActions 配置项

| 参数 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| label | 操作名称 | `String` | - |
| icon | 图标名称 | `String` | - |
| onClick | 点击回调 | `(record) => void` | - |
| disabled | 是否禁用 | `Boolean` | `false` |
| danger | 是否为危险操作 | `Boolean` | `false` |
| permissions | 权限标识 | `Array<String>` | - |

### Events

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| view | 点击查看按钮 | `(record)` |
| edit | 点击编辑按钮 | `(record)` |
| delete | 点击删除按钮 | `(record)` |
| moreAction | 点击更多操作（未配置 onClick 时） | `{ action, record }` |

### Slots

| 插槽名 | 说明 | 参数 |
|--------|------|------|
| view | 自定义查看按钮 | - |
| edit | 自定义编辑按钮 | - |
| delete | 自定义删除按钮 | - |
| actions | 自定义额外操作 | `{ record }` |
| default | 默认插槽（向后兼容） | `{ record }` |

### 按钮样式类名

组件提供了预定义的按钮样式类名，可以在插槽中使用：

- `.action-btn` - 基础按钮样式
- `.view-btn` - 查看按钮样式（蓝色）
- `.edit-btn` - 编辑按钮样式（橙色）
- `.delete-btn` - 删除按钮样式（红色）
- `.custom-btn` - 自定义按钮样式（绿色）
- `.warning-btn` - 警告按钮样式（黄色）
- `.info-btn` - 信息按钮样式（青色）
- `.primary-btn` - 主要按钮样式（蓝色）

### 最佳实践

#### 1. 操作数量建议

- **2个或更少操作**：直接显示所有操作
  ```vue
  <TableActionBar
    :record="record"
    :view="true"
    :edit="true"
  />
  ```

- **3个或更多操作**：显示1-2个主要操作 + "更多"菜单
  ```vue
  <TableActionBar
    :record="record"
    :view="true"
    :more-actions="[
      { label: '修改', onClick: handleEdit },
      { label: '重置密码', onClick: handleResetPwd },
      { label: '删除', danger: true, onClick: handleDelete }
    ]"
  />
  ```

#### 2. 权限控制

使用 `permissions` 属性配置权限：

```javascript
:view="{
  show: true,
  permissions: ['system:user:query']
}"

:more-actions="[
  {
    label: '删除',
    permissions: ['system:user:remove'],
    onClick: handleDelete
  }
]"
```

#### 3. 条件禁用

根据数据动态设置禁用状态：

```javascript
:more-actions="[
  {
    label: '删除',
    disabled: record.userName === 'admin',
    danger: true,
    onClick: handleDelete
  }
]"
```

#### 4. 操作排序建议

- 第1位：最常用的操作（如"查看"）
- 第2位：次常用的操作（如"编辑"）
- 更多菜单：次要操作、危险操作

### 向后兼容

组件保持了对旧版 API 的兼容：

```vue
<!-- 旧版用法仍然有效 -->
<TableActionBar
  :record="record"
  :hasView="true"
  :hasEdit="true"
  :hasDelete="false"
  :icons="{ view: 'eye-outlined', edit: 'edit-outlined' }"
  :texts="{ view: '查看', edit: '编辑' }"
  :permissions="{ view: ['system:user:query'] }"
  @view="handleView"
  @edit="handleEdit"
/>
```

### 实际应用示例

根据你的截图，用户管理页面的操作列可以这样配置：

```vue
<template>
  <TableActionBar
    :record="record"
    :view="true"
    :more-actions="getActions(record)"
    @view="handleView"
  />
</template>

<script setup>
const getActions = (record) => {
  return [
    {
      label: '修改',
      icon: 'edit-outlined',
      permissions: ['system:user:edit'],
      onClick: (record) => handleEdit(record)
    },
    {
      label: '重置密码',
      icon: 'key-outlined',
      permissions: ['system:user:resetPwd'],
      disabled: record.userName === 'admin',
      onClick: (record) => handleResetPwd(record)
    },
    {
      label: '删除',
      icon: 'delete-outlined',
      danger: true,
      permissions: ['system:user:remove'],
      disabled: record.userName === 'admin',
      onClick: (record) => handleDelete(record)
    }
  ];
};
</script>
```

**使用新方式（推荐）：**
```vue
<TableActionBar
  :record="record"
  :actions="[
    {
      label: '查看',
      icon: 'eye-outlined',
      type: 'view',
      permissions: ['system:user:query'],
      onClick: (record) => handleView(record)
    },
    {
      label: '修改',
      icon: 'edit-outlined',
      type: 'edit',
      permissions: ['system:user:edit'],
      onClick: (record) => handleEdit(record)
    },
    {
      label: '重置密码',
      icon: 'key-outlined',
      permissions: ['system:user:resetPwd'],
      disabled: record.userName === 'admin',
      onClick: (record) => handleResetPwd(record)
    },
    {
      label: '删除',
      icon: 'delete-outlined',
      type: 'delete',
      danger: true,
      permissions: ['system:user:remove'],
      disabled: record.userName === 'admin',
      onClick: (record) => handleDelete(record)
    }
  ]"
  :auto-adaptive="true"
/>
```

**智能显示效果（autoAdaptive=true）：**
- 有 4 个操作时：显示"查看" + "更多▼"（包含：修改、重置密码、删除）
- 节省了操作列宽度
- 保持了所有功能的可访问性
- 自动根据操作数量调整显示策略
