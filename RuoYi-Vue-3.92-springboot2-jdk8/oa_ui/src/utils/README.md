# utils

## 目录说明
- **路径**: `./template/src/utils`
- **用途**: 工具函数库，包含通用的辅助函数和工具类
- **归属模块**: 前端工具层
- **创建时间**: 2026-03-18
- **最后更新时间**: 2026-03-18

## 文件结构
```
├── auth.js
├── beautifierLoader.js
├── compressImg.js
├── directive.js
├── emitter.js
├── file.js
├── format.js
├── get-page-title.js
├── i18n.js
├── i18ns.js
├── index.js
├── numberFormat.js
├── preload.js
├── resourceUtil.js
├── rgbaster.js
├── scroll-to.js
├── tips.js
├── tree.md
├── uploadChunk.js
├── uploadCloud.js
├── util.js
├── validate.js
├── validators.js
└── watermark.util.js
```

## 重要文件说明

### 核心文件
| 文件名 | 用途 | 关键类/函数 | 依赖关系 |
|--------|------|-------------|----------|
| auth.js | JavaScript文件 | 请填写 | 请填写 |
| beautifierLoader.js | JavaScript文件 | 请填写 | 请填写 |
| compressImg.js | JavaScript文件 | 请填写 | 请填写 |
| directive.js | JavaScript文件 | 请填写 | 请填写 |
| emitter.js | JavaScript文件 | 请填写 | 请填写 |
| file.js | JavaScript文件 | 请填写 | 请填写 |
| format.js | JavaScript文件 | 请填写 | 请填写 |
| get-page-title.js | JavaScript文件 | 请填写 | 请填写 |
| i18n.js | JavaScript文件 | 请填写 | 请填写 |
| i18ns.js | JavaScript文件 | 请填写 | 请填写 |
| index.js | JavaScript文件 | 请填写 | 请填写 |
| numberFormat.js | JavaScript文件 | 请填写 | 请填写 |
| preload.js | JavaScript文件 | 请填写 | 请填写 |
| resourceUtil.js | JavaScript文件 | 请填写 | 请填写 |
| rgbaster.js | JavaScript文件 | 请填写 | 请填写 |
| scroll-to.js | JavaScript文件 | 请填写 | 请填写 |
| tips.js | JavaScript文件 | 请填写 | 请填写 |
| tree.md | 文档文件 | 请填写 | 请填写 |
| uploadChunk.js | JavaScript文件 | 请填写 | 请填写 |
| uploadCloud.js | JavaScript文件 | 请填写 | 请填写 |
| ... | (更多文件) | ... | ... |


## 使用示例

### 代码示例
```javascript
// 工具函数示例
// format.js - 格式化工具
export function formatDate(date) {
  if (!date) return ''
  return new Date(date).toLocaleDateString()
}

export function formatNumber(num) {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

export function debounce(func, delay) {
  let timer
  return function(...args) {
    clearTimeout(timer)
    timer = setTimeout(() => func.apply(this, args), delay)
  }
}

// validate.js - 验证工具
export function isEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}

export function isPhone(phone) {
  return /^1[3-9]\d{9}$/.test(phone)
}
```

### 调用方式
```javascript
// 在组件中使用工具函数
import { formatDate, isEmail } from '@/utils'

export default {
  methods: {
    submitForm() {
      const email = this.form.email
      if (!isEmail(email)) {
        this.$message.error('邮箱格式不正确')
        return
      }
      // 提交表单
    },
    displayTime(time) {
      return formatDate(time)
    }
  }
}
```

## 注意事项
1. **函数导出**: 使用 ES6 模块导出，避免污染全局作用域
2. **命名规范**: 使用驼峰命名，如 `formatDate`, `isEmail`
3. **类型检查**: 添加参数类型检查和注释
4. **纯函数**: 工具函数应该是纯函数，无副作用
5. **单元测试**: 重要工具函数应该编写单元测试
6. **性能优化**: 避免在循环中创建对象，合理使用缓存

## 更新历史
| 日期 | 版本 | 变更说明 | 修改人 |
|------|------|----------|--------|
| 2026-03-18 | 1.1 | 完善目录说明和使用示例 | AI助手 |
| 2026-03-18 | 1.0 | 自动生成 | 系统 |