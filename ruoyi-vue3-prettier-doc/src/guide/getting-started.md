# 快速开始

本章节将帮助您快速上手 RuoYi-Vue3-Prettier。

## 环境要求

- Node.js >= Node22.12.0 以上
- pnpm >= 9.15.2

## 安装

1. 克隆项目

```bash
git clone https://gitee.com/codelm/ruoyi-vue3-lmw.git
cd ruoyi-vue3-lmw
```

2. 安装依赖

```bash
pnpm install
```

3. 启动开发服务器

```bash
pnpm dev
```

## 开发

### 目录说明

- `src/api`: API 接口
- `src/assets`: 静态资源文件
- `src/BaseComponent`: 基础组件
- `src/components`: 业务组件
- `src/directive`: 自定义指令
- `src/hooks`: 钩子函数
- `src/layout`: 布局组件
- `src/plugins`: 插件配置
- `src/router`: 路由配置
- `src/store`: 状态管理
- `src/utils`: 工具函数
- `src/views`: 页面视图
- `src/App.vue`: 根组件
- `src/main.js`: 入口文件

### 开发规范

1. 组件命名

   - 使用 PascalCase 命名组件
   - 基础组件以 Base 开头

2. 文件命名

   - 使用 kebabCase 或 KebabCase 命名文件
   - 组件文件使用 PascalCase

3. 代码风格
   - 遵循 Prettier 规范
   - 使用组合式 API 和 `<script setup>`

## 部署

### 生产环境

```bash
pnpm build
```

构建完成后，将 `dist` 目录下的文件部署到 Web 服务器。

## 常见问题

1. 安装依赖失败

   - 检查 Node.js 版本是否符合要求
   - 尝试清除 pnpm 缓存：`pnpm store prune`

2. 启动开发服务器失败

   - 检查端口是否被占用
   - 检查环境变量配置是否正确

3. 构建失败
   - 检查依赖版本兼容性

## 下一步

- 了解 [项目通用方法](/methods/$tab) 掌握常用开发技巧
- 参考 [组件文档](/components/icon) 使用内置组件
