# api

## 目录说明
- **路径**: `./template/src/api`
- **用途**: API 请求封装层，统一管理后端接口调用
- **归属模块**: API 请求层
- **创建时间**: 2026-03-18
- **最后更新时间**: 2026-03-18

## 文件结构
```
├── administration.js
├── business.js
├── chart.js
├── chatAi.js
├── client.js
├── cloud.js
├── config.js
├── develop.js
├── enterprise.js
├── form.js
├── program.js
├── public.js
├── request.js
├── setting.js
├── system.js
├── systemForm.js
├── tree.md
└── user.js
```

## 重要文件说明

### 核心文件
| 文件名 | 用途 | 关键类/函数 | 依赖关系 |
|--------|------|-------------|----------|
| administration.js | JavaScript文件 | 请填写 | 请填写 |
| business.js | JavaScript文件 | 请填写 | 请填写 |
| chart.js | JavaScript文件 | 请填写 | 请填写 |
| chatAi.js | JavaScript文件 | 请填写 | 请填写 |
| client.js | JavaScript文件 | 请填写 | 请填写 |
| cloud.js | JavaScript文件 | 请填写 | 请填写 |
| config.js | JavaScript文件 | 请填写 | 请填写 |
| develop.js | JavaScript文件 | 请填写 | 请填写 |
| enterprise.js | JavaScript文件 | 请填写 | 请填写 |
| form.js | JavaScript文件 | 请填写 | 请填写 |
| program.js | JavaScript文件 | 请填写 | 请填写 |
| public.js | JavaScript文件 | 请填写 | 请填写 |
| request.js | JavaScript文件 | 请填写 | 请填写 |
| setting.js | JavaScript文件 | 请填写 | 请填写 |
| system.js | JavaScript文件 | 请填写 | 请填写 |
| systemForm.js | JavaScript文件 | 请填写 | 请填写 |
| tree.md | 文档文件 | 请填写 | 请填写 |
| user.js | JavaScript文件 | 请填写 | 请填写 |


## 使用示例

### 代码示例
```javascript
// api/user.js - 用户相关 API
import request from '@/utils/request'

export function getUserList(params) {
  return request({
    url: '/api/users',
    method: 'get',
    params
  })
}

export function createUser(data) {
  return request({
    url: '/api/users',
    method: 'post',
    data
  })
}

export function updateUser(id, data) {
  return request({
    url: `/api/users/${id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/api/users/${id}`,
    method: 'delete'
  })
}
```

### 调用方式
```javascript
// 在组件中使用 API
import { getUserList, createUser } from '@/api/user'

export default {
  data() {
    return {
      list: []
    }
  },
  methods: {
    async loadUsers() {
      const { data } = await getUserList({ page: 1, limit: 10 })
      this.list = data.list
    },
    async addUser() {
      await createUser({ name: 'John', email: 'john@example.com' })
      this.$message.success('创建成功')
    }
  }
}
```

## 注意事项
1. **统一封装**: 所有 API 请求必须通过 `@/utils/request` 统一封装
2. **Token 管理**: 自动在请求头添加 Authorization Token
3. **错误处理**: 统一处理 HTTP 错误和业务错误
4. **响应拦截**: 使用响应拦截器处理统一响应格式
5. **TypeScript**: 如使用 TypeScript，需要定义接口类型
6. **超时设置**: 合理设置请求超时时间，避免长时间等待

## 更新历史
| 日期 | 版本 | 变更说明 | 修改人 |
|------|------|----------|--------|
| 2026-03-18 | 1.1 | 完善目录说明和使用示例 | AI助手 |
| 2026-03-18 | 1.0 | 自动生成 | 系统 |