# views

## 目录说明
- **路径**: `./template/src/views`
- **用途**: 页面视图组件目录，包含所有业务页面
- **归属模块**: 前端页面层
- **创建时间**: 2026-03-18
- **最后更新时间**: 2026-03-18

## 文件结构
```
├── share.vue
└── tree.md
```

## 重要文件说明

### 核心文件
| 文件名 | 用途 | 关键类/函数 | 依赖关系 |
|--------|------|-------------|----------|
| share.vue | Vue组件文件 | 请填写 | 请填写 |
| tree.md | 文档文件 | 请填写 | 请填写 |


## 使用示例

### 代码示例
```vue
<template>
  <div class="page-container">
    <el-table :data="list">
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="email" label="邮箱" />
    </el-table>
  </div>
</template>

<script>
import { getUserList } from '@/api/user'

export default {
  name: 'UserList',
  data() {
    return {
      list: []
    }
  },
  async created() {
    const { data } = await getUserList()
    this.list = data
  }
}
</script>

<style scoped>
.page-container {
  padding: 20px;
}
</style>
```

### 调用方式
```javascript
// 路由配置中注册页面
{
  path: '/users',
  name: 'UserList',
  component: () => import('@/views/users/index.vue'),
  meta: { title: '用户管理', requiresAuth: true }
}
```

## 注意事项
1. **页面命名**: 使用大驼峰命名，如 `UserList`
2. **路由懒加载**: 使用 `() => import()` 懒加载页面，优化首屏
3. **Meta 信息**: 在路由 meta 中配置页面标题和权限
4. **生命周期**: 合理使用 Vue 生命周期钩子
5. **组件解耦**: 避免页面组件过于复杂，拆分为子组件
6. **API 调用**: 统一使用 `@/api/` 目录下的封装方法

## 更新历史
| 日期 | 版本 | 变更说明 | 修改人 |
|------|------|----------|--------|
| 2026-03-18 | 1.1 | 完善目录说明和使用示例 | AI助手 |
| 2026-03-18 | 1.0 | 自动生成 | 系统 |