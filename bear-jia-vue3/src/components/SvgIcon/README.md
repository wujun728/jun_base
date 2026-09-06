# SvgIcon 组件使用手册

## 组件简介

SvgIcon 是一个功能强大、灵活的 SVG 图标组件，支持多种图标加载方式，包括直接加载本地 SVG 文件、SVG Sprite 和外部链接图标。

## 特性

- ✅ 自动加载 `assets/icons/` 目录下的 SVG 文件
- ✅ 支持 SVG Sprite（symbol）方式
- ✅ 支持外部链接图标
- ✅ 完全可控的尺寸和颜色
- ✅ 使用 `currentColor` 继承父元素颜色
- ✅ 完美支持主题切换
- ✅ 向后兼容原有功能

## 安装使用

### 1. 全局注册（推荐）

```javascript
// main.js
import SvgIcon from '@/components/SvgIcon/index.vue'

app.component('SvgIcon', SvgIcon)
```

### 2. 局部引入

```vue
<script setup>
import SvgIcon from '@/components/SvgIcon/index.vue'
</script>
```

## Props 参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| iconClass | String | - | **必填**，图标名称或路径 |
| className | String | '' | 自定义 CSS 类名 |
| color | String | 'currentColor' | 图标颜色 |
| size | String/Number | '1em' | 图标尺寸 |

## 使用示例

### 基础用法

#### 1. 加载本地 SVG 文件

```vue
<template>
  <!-- 自动从 assets/icons/ 目录加载 bug.svg -->
  <svg-icon icon-class="bug" />
  
  <!-- 加载 system.svg -->
  <svg-icon icon-class="system" />
</template>
```

**文件结构：**
```
src/
  assets/
    icons/
      bug.svg
      system.svg
      user.svg
```

#### 2. 使用 SVG Sprite

```vue
<template>
  <!-- 使用已注册的 sprite 图标 -->
  <svg-icon icon-class="#icon-home" />
  
  <!-- 或者（如果 DOM 中存在 id="icon-home" 的 symbol） -->
  <svg-icon icon-class="home" />
</template>
```

#### 3. 外部链接图标

```vue
<template>
  <svg-icon icon-class="https://example.com/icon.svg" />
</template>
```

### 自定义尺寸

```vue
<template>
  <!-- 使用 em 单位 -->
  <svg-icon icon-class="bug" size="2em" />
  
  <!-- 使用像素 -->
  <svg-icon icon-class="system" :size="24" />
  
  <!-- 使用字符串 -->
  <svg-icon icon-class="user" size="32px" />
</template>
```

### 自定义颜色

```vue
<template>
  <!-- 指定颜色 -->
  <svg-icon icon-class="bug" color="#ff0000" />
  
  <!-- 使用 CSS 变量 -->
  <svg-icon icon-class="system" color="var(--primary-color)" />
  
  <!-- 继承父元素颜色（默认） -->
  <div style="color: blue;">
    <svg-icon icon-class="user" />
  </div>
</template>
```

### 结合尺寸和颜色

```vue
<template>
  <svg-icon 
    icon-class="bug" 
    :size="32"
    color="#1890ff"
  />
</template>
```

### 自定义类名

```vue
<template>
  <svg-icon 
    icon-class="system" 
    class-name="custom-icon"
  />
</template>

<style>
.custom-icon {
  margin-right: 8px;
  vertical-align: middle;
}
</style>
```

## 高级用法

### 1. 动态图标

```vue
<template>
  <svg-icon :icon-class="currentIcon" />
  
  <a-button @click="changeIcon">切换图标</a-button>
</template>

<script setup>
import { ref } from 'vue'

const icons = ['bug', 'system', 'user']
const currentIndex = ref(0)
const currentIcon = computed(() => icons[currentIndex.value])

const changeIcon = () => {
  currentIndex.value = (currentIndex.value + 1) % icons.length
}
</script>
```

### 2. 响应式颜色

```vue
<template>
  <svg-icon 
    icon-class="bug" 
    :color="isActive ? '#1890ff' : '#999'"
  />
</template>

<script setup>
import { ref } from 'vue'

const isActive = ref(false)
</script>
```

### 3. 图标列表

```vue
<template>
  <div class="icon-list">
    <svg-icon 
      v-for="icon in icons" 
      :key="icon"
      :icon-class="icon"
      :size="24"
      class="icon-item"
    />
  </div>
</template>

<script setup>
const icons = ['bug', 'build', 'system', 'user', 'tool']
</script>

<style scoped>
.icon-list {
  display: flex;
  gap: 16px;
}

.icon-item {
  cursor: pointer;
  transition: color 0.3s;
}

.icon-item:hover {
  color: #1890ff;
}
</style>
```

### 4. 在 Ant Design 表单中使用

```vue
<template>
  <a-form-item label="图标">
    <a-space>
      <svg-icon 
        v-if="form.icon"
        :icon-class="form.icon"
        :size="20"
        color="#333"
      />
      <a-button @click="showIconSelector">选择图标</a-button>
    </a-space>
  </a-form-item>
</template>
```

### 5. 在菜单中使用

```vue
<template>
  <a-menu>
    <a-menu-item key="1">
      <template #icon>
        <svg-icon icon-class="dashboard" />
      </template>
      <span>Dashboard</span>
    </a-menu-item>
    
    <a-menu-item key="2">
      <template #icon>
        <svg-icon icon-class="system" />
      </template>
      <span>System</span>
    </a-menu-item>
  </a-menu>
</template>
```

## 工作原理

组件会按以下优先级自动判断图标类型：

1. **外部链接**：如果 `iconClass` 是以 `http://` 或 `https://` 开头的 URL
2. **SVG Sprite**：如果 `iconClass` 以 `#` 开头，或在 DOM 中找到对应的 `symbol` 元素
3. **本地 SVG 文件**：自动从 `assets/icons/` 目录加载对应的 `.svg` 文件

## 颜色控制说明

### currentColor 机制

默认情况下，图标使用 `currentColor`，这意味着图标会继承父元素的文字颜色：

```vue
<template>
  <!-- 图标会显示为红色 -->
  <div style="color: red;">
    <svg-icon icon-class="bug" />
  </div>
</template>
```

### 显式指定颜色

```vue
<template>
  <!-- 图标会显示为蓝色，不受父元素影响 -->
  <div style="color: red;">
    <svg-icon icon-class="bug" color="blue" />
  </div>
</template>
```

## 注意事项

### 1. SVG 文件要求

- SVG 文件应该放在 `src/assets/icons/` 目录下
- 文件名即为 `iconClass` 的值（不含 `.svg` 后缀）
- SVG 文件中不应包含固定的 `fill` 或 `stroke` 颜色，以便支持颜色控制

**推荐的 SVG 结构：**
```xml
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
  <!-- 使用 currentColor 或不指定颜色 -->
  <path d="..." fill="currentColor"/>
</svg>
```

### 2. 尺寸单位

- 数字类型会自动转换为 `px` 单位：`:size="24"` → `24px`
- 字符串类型支持任意 CSS 单位：`size="2em"`、`size="1.5rem"`

### 3. 性能优化

- 相同的 SVG 文件只会加载一次（浏览器缓存）
- 建议使用 SVG Sprite 方式加载大量图标
- 避免加载过大的 SVG 文件

### 4. 兼容性

- 组件完全向后兼容原有的 SVG Sprite 功能
- 如果项目中已有注册的 sprite 图标，无需修改现有代码

## 常见问题

### Q: 图标不显示？

**A:** 检查以下几点：
1. SVG 文件是否存在于 `src/assets/icons/` 目录
2. `iconClass` 的值是否正确（不含 `.svg` 后缀）
3. 查看浏览器控制台是否有警告信息

### Q: 图标颜色无法改变？

**A:** 
1. 检查 SVG 文件中是否有固定的 `fill` 或 `stroke` 属性
2. 将 SVG 中的颜色改为 `currentColor` 或移除颜色属性
3. 确保传入了 `color` prop

### Q: 图标尺寸不对？

**A:** 
1. 确保传入了正确的 `size` prop
2. 检查是否有 CSS 样式覆盖了组件的尺寸
3. 查看 SVG 文件的 `viewBox` 属性是否正确

### Q: 如何添加新图标？

**A:** 
1. 将 SVG 文件放入 `src/assets/icons/` 目录
2. 文件名使用小驼峰或短横线命名（如 `userAdd.svg` 或 `user-add.svg`）
3. 直接使用：`<svg-icon icon-class="userAdd" />`

## 更新日志

### v2.0.0 (当前版本)
- ✨ 新增：支持直接加载本地 SVG 文件
- ✨ 新增：`color` 和 `size` props
- ✨ 优化：自动判断图标类型
- ✨ 优化：更好的颜色控制
- ✅ 向后兼容原有功能

### v1.0.0
- 基础功能：SVG Sprite 支持
- 基础功能：外部链接图标支持

## 相关资源

- [SVG 优化工具](https://jakearchibald.github.io/svgomg/)
- [Iconfont 图标库](https://www.iconfont.cn/)
- [Ant Design Icons](https://ant.design/components/icon-cn/)

## 技术支持

如有问题或建议，请联系开发团队。
