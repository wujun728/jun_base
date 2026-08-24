# src

## 目录说明
- **路径**: `./template/src`
- **用途**: Vue 前端源代码目录，包含组件、页面、API、路由等
- **归属模块**: 前端源代码
- **创建时间**: 2026-03-18
- **最后更新时间**: 2026-03-18

## 文件结构
```
├── api/
├── assets/
├── components/
├── config/
├── directive/
├── filters/
├── icons/
├── lang/
├── layout/
├── libs/
├── mixins/
├── router/
├── store/
├── styles/
├── utils/
├── views/
├── App.vue
├── main.js
├── permission.js
├── settings.js
└── tree.md
```

## 重要文件说明

### 核心文件
| 文件名 | 用途 | 关键类/函数 | 依赖关系 |
|--------|------|-------------|----------|
| App.vue | 应用根组件，包含路由视图和全局布局 | `App.vue` | Vue Router |
| main.js | 应用入口文件，初始化 Vue 实例 | `createApp` | Vue, ElementUI |
| permission.js | 权限控制文件，定义路由守卫 | `router.beforeEach` | Vue Router |
| settings.js | 应用配置文件，全局配置项 | - | - |
| tree.md | 目录结构文档 | - | - |

### 子目录
| 目录名 | 用途 |
|--------|------|
| api/ | API 请求封装 |
| assets/ | 静态资源文件 |
| components/ | 可复用组件 |
| config/ | 前端配置 |
| directive/ | Vue 自定义指令 |
| filters/ | Vue 过滤器 |
| icons/ | 图标资源 |
| lang/ | 国际化语言包 |
| layout/ | 布局组件（Header, Sidebar 等） |
| libs/ | 第三方库封装 |
| mixins/ | Vue 混入 |
| router/ | 路由配置 |
| store/ | Vuex 状态管理 |
| styles/ | 全局样式文件 |
| utils/ | 工具函数库 |
| views/ | 页面视图组件 |


## 使用示例

### 代码示例
```javascript
// main.js - 应用入口
import Vue from 'vue'
import ElementUI from 'element-ui'
import App from './App.vue'
import router from './router'
import store from './store'

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

// permission.js - 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})
```

### 调用方式
```bash
# 启动开发服务器
npm run dev

# 构建生产环境
npm run build:prod

# 代码检查
npm run lint
```

## 注意事项
1. **组件规范**: 遵循 Vue 2 组件开发规范，使用单文件组件
2. **API 调用**: 统一使用 `src/api` 目录下的封装方法
3. **状态管理**: 共享状态使用 Vuex Store，避免直接修改
4. **路由权限**: 使用路由元信息（meta）控制页面访问权限
5. **样式隔离**: 使用 scoped CSS 避免样式污染
6. **代码分割**: 使用路由懒加载优化首屏加载速度

## 更新历史
| 日期 | 版本 | 变更说明 | 修改人 |
|------|------|----------|--------|
| 2026-03-18 | 1.1 | 完善目录说明和使用示例 | AI助手 |
| 2026-03-18 | 1.0 | 自动生成 | 系统 |