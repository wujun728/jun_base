# 自定义指令

## v-hasRole

角色权限指令，用于判断用户是否拥有某个角色。

```vue
<template>
  <!-- 单个角色验证 -->
  <el-button v-hasRole="'admin'">管理员按钮</el-button>

  <!-- 多个角色验证，包含其中任意一个角色即可 -->
  <el-button v-hasRole="['admin', 'test']">管理员或测试按钮</el-button>
</template>
```

## v-hasPermi

权限指令，用于判断用户是否拥有某个权限。

```vue
<template>
  <!-- 单个权限验证 -->
  <el-button v-hasPermi="'system:user:add'">新增用户</el-button>

  <!-- 多个权限验证，包含其中任意一个权限即可 -->
  <el-button v-hasPermi="['system:user:add', 'system:user:edit']"
    >操作按钮</el-button
  >
</template>
```

## v-copyText

复制文本指令，点击时复制指定文本内容。

```vue
<template>
  <!-- 复制静态文本 -->
  <el-button v-copyText="'要复制的文本'">点击复制</el-button>

  <!-- 复制动态文本 -->
  <el-button v-copyText="copyData">点击复制</el-button>
</template>

<script setup>
const copyData = ref('动态文本内容')
</script>
```

## v-drag

拖拽指令，使元素可以进行拖拽。

```vue
<template>
  <!-- 使元素可拖拽 -->
  <div v-drag class="drag-container">可拖拽的元素</div>
</template>

<style scoped>
.drag-container {
  position: absolute;
  cursor: move;
}
</style>
```
