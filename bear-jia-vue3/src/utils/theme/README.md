# 主题管理模块文档

## 概述

主题管理模块提供了统一的主题管理解决方案，将原本分散的主题切换逻辑集中管理，提高了代码的可维护性和扩展性。

## 架构设计

```
src/utils/theme/
├── index.js                    # 模块入口文件
├── themeManager.js             # 主题管理器核心
├── themeService.js             # 主题服务层
├── themeConfig.js              # 主题配置和常量
├── composables/
│   └── useThemeComposition.js  # Vue组合式API
└── README.md                   # 文档
```

## 核心功能

### 1. 主题模式切换
- 支持亮色/暗色主题切换
- 支持跟随系统主题设置
- 主题状态持久化存储

### 2. 主题色管理
- 预设主题色选择
- 自定义主题色添加
- 主题色变体自动生成

### 3. 特殊模式
- 色弱模式支持
- 紧凑模式（预留）
- 过渡动画控制

### 4. 配置管理
- 导出主题配置
- 导入主题配置
- 重置为默认值

## 使用方法

### 在 Vue 组件中使用

```vue
<template>
  <div>
    <button @click="toggleTheme">切换主题</button>
    <button @click="setPrimaryColor('#52c41a')">设置主题色</button>
    <div>当前主题：{{ isDarkMode ? '暗色' : '亮色' }}</div>
    <div>主题色：{{ primaryColor }}</div>
  </div>
</template>

<script setup>
import { useThemeComposition } from '@/utils/theme/composables/useThemeComposition';

const {
  isDarkMode,
  primaryColor,
  toggleTheme,
  setPrimaryColor
} = useThemeComposition();
</script>
```

### 在 JavaScript 中使用

```javascript
import { themeService } from '@/utils/theme';

// 初始化主题服务
await themeService.init({
  followSystem: false // 是否跟随系统主题
});

// 切换主题模式
themeService.toggleTheme();

// 设置主题色
themeService.setPrimaryColor('#52c41a');

// 设置色弱模式
themeService.setColorWeak(true);

// 导出配置
const config = themeService.exportThemeConfig();

// 导入配置
themeService.importThemeConfig(config);
```

### 使用简化版API

```vue
<script setup>
import { useSimpleTheme } from '@/utils/theme/composables/useThemeComposition';

const { isDarkMode, primaryColor, toggleTheme, setPrimaryColor } = useSimpleTheme();
</script>
```

### 使用主题色选择器

```vue
<script setup>
import { useThemeColorPicker } from '@/utils/theme/composables/useThemeComposition';

const {
  currentColor,
  presetColors,
  selectColor,
  isActiveColor,
  addCustomColor
} = useThemeColorPicker();
</script>
```

## API 参考

### ThemeService 方法

| 方法 | 说明 | 参数 | 返回值 |
|------|------|------|--------|
| `init(options)` | 初始化主题服务 | `options: { followSystem?: boolean }` | `Promise<void>` |
| `setThemeMode(mode)` | 设置主题模式 | `mode: 'light' \| 'dark'` | `void` |
| `toggleTheme()` | 切换主题 | - | `void` |
| `setPrimaryColor(color)` | 设置主题色 | `color: string` | `void` |
| `setColorWeak(enabled)` | 设置色弱模式 | `enabled: boolean` | `void` |
| `addCustomColor(color, name)` | 添加自定义颜色 | `color: string, name: string` | `void` |
| `removeCustomColor(color)` | 删除自定义颜色 | `color: string` | `void` |
| `resetTheme()` | 重置主题 | - | `void` |
| `exportThemeConfig()` | 导出主题配置 | - | `object` |
| `importThemeConfig(config)` | 导入主题配置 | `config: object` | `void` |
| `getCurrentTheme()` | 获取当前主题信息 | - | `object` |

### useThemeComposition 返回值

| 属性/方法 | 类型 | 说明 |
|-----------|------|------|
| `themeMode` | `Ref<string>` | 当前主题模式 |
| `primaryColor` | `Ref<string>` | 当前主题色 |
| `isDarkMode` | `Ref<boolean>` | 是否为暗色主题 |
| `isLightMode` | `Ref<boolean>` | 是否为亮色主题 |
| `colorWeak` | `Ref<boolean>` | 是否启用色弱模式 |
| `presetColors` | `ComputedRef<array>` | 预设颜色列表 |
| `allColors` | `ComputedRef<array>` | 所有颜色列表 |
| `setThemeMode` | `Function` | 设置主题模式 |
| `toggleTheme` | `Function` | 切换主题 |
| `setPrimaryColor` | `Function` | 设置主题色 |
| `setColorWeak` | `Function` | 设置色弱模式 |
| `resetTheme` | `Function` | 重置主题 |

## 配置项

### 预设主题色

```javascript
PRESET_COLORS = [
  { color: '#1677ff', name: '默认蓝' },
  { color: '#f5222d', name: '薄暮红' },
  { color: '#fa541c', name: '火山橙' },
  { color: '#faad14', name: '日暮黄' },
  { color: '#13c2c2', name: '明青色' },
  { color: '#52c41a', name: '极光绿' },
  { color: '#722ed1', name: '酱紫色' },
  { color: '#eb2f96', name: '粉红色' }
]
```

### 存储键名

主题配置会自动持久化到 localStorage，使用以下键名：

- `theme-config` - 新版主题配置
- `darkMode` - 暗色模式（兼容旧版）
- `primaryColor` - 主题色（兼容旧版）

## 事件监听

主题模块会触发以下自定义事件：

- `themeChange` - 主题变化时触发
- `themeModeChange` - 主题模式变化时触发
- `themeColorChange` - 主题色变化时触发

```javascript
// 监听主题变化
themeService.onEvent('themeChange', (detail) => {
  console.log('主题已变化:', detail);
});
```

## 迁移指南

### 从旧版本迁移

1. **更新导入路径**
   ```javascript
   // 旧版
   import { applyTheme } from '@/utils/configInit';
   
   // 新版
   import { themeService } from '@/utils/theme';
   ```

2. **更新主题切换代码**
   ```javascript
   // 旧版
   localStorage.setItem('darkMode', 'true');
   location.reload();
   
   // 新版
   themeService.setThemeMode('dark');
   ```

3. **更新组件中的使用方式**
   ```vue
   <!-- 旧版 -->
   <script setup>
   const darkMode = ref(localStorage.getItem('darkMode') === 'true');
   const toggleTheme = () => {
     darkMode.value = !darkMode.value;
     localStorage.setItem('darkMode', darkMode.value);
     location.reload();
   };
   </script>
   
   <!-- 新版 -->
   <script setup>
   import { useThemeComposition } from '@/utils/theme/composables/useThemeComposition';
   const { isDarkMode, toggleTheme } = useThemeComposition();
   </script>
   ```

## 注意事项

1. 主题服务必须在应用启动时初始化
2. 主题配置会自动持久化，无需手动保存
3. 兼容旧版本的存储键名，可以平滑升级
4. 主题切换不需要刷新页面，实时生效

## 常见问题

### Q: 主题切换后部分组件样式没有更新？
A: 确保组件使用了 CSS 变量而不是硬编码的颜色值。主题系统通过更新 CSS 变量来实现主题切换。

### Q: 如何添加新的预设主题色？
A: 在 `themeConfig.js` 的 `PRESET_COLORS` 数组中添加新的颜色配置。

### Q: 主题配置没有持久化？
A: 检查浏览器的 localStorage 是否可用，以及是否有足够的存储空间。

### Q: 如何禁用主题切换动画？
A: 可以通过移除 `theme-transition` 类名来禁用过渡动画。

## 更新日志

### v1.0.0 (2024-09-30)
- 初始版本发布
- 统一主题管理逻辑
- 支持亮色/暗色主题切换
- 支持主题色自定义
- 支持色弱模式
- 提供 Vue 组合式 API
- 支持配置导入/导出