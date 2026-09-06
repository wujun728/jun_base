# 配置文件目录

本目录用于集中管理项目的各种配置文件，使项目根目录更加整洁。

## 目录结构

```
config/
├── README.md           # 配置说明文档
├── vite/              # Vite 相关配置
│   ├── index.js       # Vite 配置入口
│   ├── base.js        # 基础配置
│   ├── plugins.js     # 插件配置
│   └── build.js       # 打包配置
└── eslint/            # ESLint 配置/生成文件
    └── .eslintrc-auto-import.json # unplugin-auto-import 生成的全局变量声明
```

## 配置文件说明

### Vite 配置
- `vite/index.js`: Vite 配置聚合入口
- `vite/base.js`: 基础配置（别名、环境变量等）
- `vite/plugins.js`: 插件配置（vue、jsx等）
- `vite/build.js`: 打包优化配置（代码分割、压缩等）

### 代码规范配置
- `eslint/`: ESLint 相关配置/生成文件（目前主要用于 AutoImport 的 globals）
- Prettier 配置仍在根目录：`.prettierrc.js`

## 使用方式

在根目录的配置文件中引入对应的配置模块：

```javascript
// vite.config.mjs
import { defineConfig } from 'vite';
import { getViteConfig } from './config/vite/index.js';

export default defineConfig(getViteConfig);
