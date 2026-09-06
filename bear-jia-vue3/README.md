# 🐻 BearJia Admin - Vue3 前端框架

<div align="center">

![Vue](https://img.shields.io/badge/Vue-3.4.21-brightgreen.svg)
![Ant Design Vue](https://img.shields.io/badge/Ant%20Design%20Vue-4.1.2-blue.svg)
![Vite](https://img.shields.io/badge/Vite-5.1.4-646CFF.svg)
![Pinia](https://img.shields.io/badge/Pinia-2.1.7-yellow.svg)
![TypeScript](https://img.shields.io/badge/TypeScript-5.4.5-blue.svg)
![Version](https://img.shields.io/badge/Version-1.4.0-orange.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

**基于 Vue3 + Composition API + Vite5 + Ant Design Vue 4 的现代化企业级管理后台框架**

[后端仓库](https://gitee.com/javaxiaobear_admin/bearjia-admin-backend) | [技术文档](https://javaxiaobear.cn) | [在线预览](http://bearjia.javaxiaobear.cn) | [更新日志](./docs/CHANGELOG.md)

</div>

## 📸 项目预览

![image-20260604093846975](https://javaxiaobear-1301481032.cos.ap-guangzhou.myqcloud.com/picture-bed/image-20260604093846975.png)

![image-20260604093915987](https://javaxiaobear-1301481032.cos.ap-guangzhou.myqcloud.com/picture-bed/image-20260604093915987.png)

![image-20260604093931129](https://javaxiaobear-1301481032.cos.ap-guangzhou.myqcloud.com/picture-bed/image-20260604093931129.png)

![image-20260604093948457](https://javaxiaobear-1301481032.cos.ap-guangzhou.myqcloud.com/picture-bed/image-20260604093948457.png)

![image-20260604094008819](https://javaxiaobear-1301481032.cos.ap-guangzhou.myqcloud.com/picture-bed/image-20260604094008819.png)

## 📖 项目简介

BearJia Admin 是一个基于 Vue3.4 + Composition API + Vite5 + Ant Design Vue 4 打造的现代化企业级管理后台框架。项目采用最新的前端技术栈和工程化最佳实践,通过模块化架构设计和组件化开发模式,为开发者提供一个功能完整、易于扩展、开箱即用的中后台解决方案。

框架内置了完善的 RBAC 权限管理体系、系统监控功能、代码生成工具、文件管理系统、表单设计器等企业级核心功能模块,同时支持多主题切换、5种布局模式、虚拟滚动等特性。无论是快速原型开发还是大型企业应用构建,都能满足不同场景的开发需求。

### ✨ 技术特色

#### 🚀 前沿技术栈
- **Vue3.4 生态**：基于 Vue 3.4.21 + Composition API,享受最新的响应式系统和性能优化
- **Vite5 构建**：极速的开发体验,HMR 毫秒级热更新,生产环境高效打包优化
- **Pinia 状态管理**：Vue 官方推荐的状态管理方案,完美的 TypeScript 支持
- **Ant Design Vue 4.x**：企业级 UI 组件库 4.1.2 版本,丰富的组件生态
- **TypeScript 支持**：渐进式 TypeScript 集成,类型检查保障代码质量

#### 🎯 开发体验
- **语法糖优化**：全面采用 `<script setup>` 语法,代码量减少 30%+,开发效率显著提升
- **组合式函数**：封装 useTable、useDict、useVirtualScroll 等实用 Composables,逻辑复用更优雅
- **智能导入**：unplugin-auto-import 自动导入 Vue API,unplugin-vue-components 自动注册组件
- **热重载支持**：Vite HMR 实现代码修改即时生效,开发调试更高效
- **代码片段**：提供丰富的代码模板,快速生成常用代码结构

#### 🎨 界面设计
- **多主题系统**：亮色/暗色主题无缝切换,支持 10+ 种预设主题色,满足个性化需求
- **5种布局模式**：侧边栏、顶部、混合、分栏、抽屉布局,灵活适配不同业务场景
- **响应式设计**：基于 Ant Design 栅格系统,完美适配桌面、平板、手机等各种设备
- **流畅动画**：页面切换、组件交互采用流畅过渡动画,提升用户体验
- **精致细节**：统一的视觉风格、合理的间距、清晰的层级关系

#### 🛠️ 工程化实践
- **代码规范**：ESLint + Prettier 统一代码风格,EditorConfig 跨编辑器配置一致性
- **类型检查**：vue-tsc 提供完整的 TypeScript 类型检查,确保代码健壮性
- **构建优化**：多环境构建配置,代码分割,资源压缩,Tree Shaking,按需加载
- **模块化配置**：Vite 配置模块化拆分,插件系统化管理,易于维护和扩展
- **实用脚本**：代码清理、日志替换、导出检查等自动化脚本,提升开发效率

#### 🔐 安全与性能
- **权限控制**：基于 RBAC 的精细化权限管理,支持菜单、按钮、数据权限控制
- **安全防护**：XSS 防护,CSRF Token,敏感信息加密存储,安全的请求拦截
- **性能优化**：路由懒加载,组件异步加载,图片懒加载,虚拟滚动优化大数据渲染
- **监控体系**：错误边界捕获,性能监控上报,用户行为追踪分析
- **虚拟滚动**：支持万级数据流畅渲染,内存占用降低 70%,性能提升 90%

## 🎯 核心功能

### 功能模块概览

| 🔐 权限管理 | 📊 系统监控 | 🛠️ 系统工具 | 🎨 界面特色 |
|:---:|:---:|:---:|:---:|
| 用户管理<br/>角色权限<br/>菜单管理<br/>部门管理<br/>岗位管理 | 在线用户<br/>定时任务<br/>数据监控<br/>服务监控<br/>缓存监控 | 代码生成器<br/>表单设计器<br/>系统配置<br/>字典管理<br/>通知公告 | 多主题系统<br/>5种布局<br/>消息中心<br/>图标选择器<br/>文件管理 |

### ✨ 核心亮点

#### 🎨 企业级功能
- **文件管理系统**：支持文件夹树形结构、文件上传下载、在线预览、统计分析,新增 3,595 行核心代码
- **表单设计器**：可视化拖拽式表单设计,支持 20+ 种表单组件,动态表单渲染引擎
- **角色数据权限**：支持自定义数据权限规则,可视化权限配置界面,精细化数据隔离
- **代码生成器**：支持单表、树表、主子表代码生成,自动生成 CRUD 代码,模板类型动态切换,字段联动选择配置,提升开发效率 80%+

#### ⚡ 性能优化
- **虚拟滚动**：10,000 条数据渲染从 3s 降至 0.3s,性能提升 90%
- **内存优化**：大数据场景内存占用从 150MB 降至 45MB,降低 70%
- **首屏优化**：首屏加载时间从 2.5s 降至 0.5s,提升 80%
- **滚动流畅**：高频滚动帧率从 30fps 提升至 60fps,提升 100%

#### 🔧 开发增强
- **组件封装**：ProTable 统一表格、SearchForm 搜索表单、HistoryNav 标签页等高度封装组件
- **工具函数**：useTable、useDict、useVirtualScroll 等实用 Composables
- **图标系统**：677 行代码支持自定义 SVG 图标和 Ant Design 图标分类展示
- **主题服务**：集成主题管理服务,支持动态主题切换和自定义主题配置

#### 🚀 使用体验
- **开箱即用**：完整的企业级管理系统功能,克隆即可运行,无需从零开始
- **高度可定制**：支持主题色、布局模式、功能模块的灵活配置和扩展
- **全端适配**：一套代码完美适配桌面、平板、移动端,响应式布局
- **极速开发**：基于 Vite5 构建,开发环境启动提升 40%,HMR 毫秒级响应

## 🚀 快速开始

### 环境要求
- Node.js >= 18.0.0
- 现代浏览器（Chrome 88+、Firefox 78+、Safari 14+、Edge 88+）

### 一键启动
```bash
# 克隆项目
git clone https://github.com/javaxiaobear/BearJia.git
# 或使用 Gitee
git clone https://gitee.com/javaxiaobear/BearJia.git

# 进入项目目录
cd BearJia/bear-jia-vue3

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 默认访问 http://localhost:8090
# 可通过 env/.env.dev 中的 VITE_PORT 配置端口
```

### 常用命令
```bash
npm run dev          # 启动开发服务器
npm run build        # 生产环境构建
npm run preview      # 预览生产构建
npm run lint         # ESLint 代码检查
npm run lint:fix     # ESLint 检查并自动修复
npm run format       # Prettier 格式化代码
npm run typecheck    # TypeScript 类型检查
```

### 📋 更多命令

```bash
# 构建相关
npm run build:prod      # 生产环境构建
npm run build:test      # 测试环境构建

# 代码质量检查
npm run lint            # ESLint 代码检查
npm run lint:fix        # ESLint 检查并自动修复
npm run format          # Prettier 格式化
npm run format:check    # Prettier 检查格式
npm run typecheck       # TypeScript 类型检查

# 实用工具脚本
npm run cleanup         # 清理 node_modules 和构建产物
```

## 🔧 配置说明

### 环境变量

项目支持多环境配置,通过 `env/` 目录下的 `.env` 文件管理：

- `env/.env` - 基础配置
- `env/.env.dev` - 开发环境配置
- `env/.env.test` - 测试环境配置
- `env/.env.prod` - 生产环境配置

建议先复制一份 `env/.env.example` 作为本地配置模板,再按需调整。

```bash
# 开发环境配置示例 (env/.env.dev)
# 开发服务器端口
VITE_PORT=8090

# 应用基础路径
VITE_BASE_URL=./

# 应用标题
VITE_APP_TITLE=BearJia Admin

# API 基础路径
VITE_APP_BASE_API=/dev-api

# 开发代理目标地址(后端服务地址)
VITE_PROXY_TARGET=http://localhost:8081

# 构建分析(生成 dist/stats.html)
VITE_VISUALIZER=false
VITE_VISUALIZER_OPEN=false
```

### 后端对接

本项目可以对接多种后端框架：

1. **推荐：BearJia SpringBoot**
   - 仓库地址：[BearJia-SpringBoot](https://gitee.com/javaxiaobear_admin/bearjia-admin-backend)
   - 完美适配,功能齐全

2. **兼容：RuoYi-Vue 后端**
   - 仓库地址：[RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)
   - 本框架可作为 RuoYi-Vue 的前端替代方案
   - 需要少量接口适配（详见文档：[若依对接指南](./docs/ruoyi-usage.md)）

## 📁 项目结构

<details>
<summary>🗂️ 点击展开详细结构</summary>

```
bear-jia-vue3/
├── 📁 src/                    # 源代码目录
│   ├── 🔌 api/                # API接口层
│   ├── 🎨 assets/             # 静态资源文件
│   ├── 🧩 components/         # 公共组件库
│   ├── 🎯 composables/        # Vue3组合式函数
│   ├── ⚙️ config/             # 配置文件
│   ├── 📋 directive/          # 自定义指令
│   ├── 🏗️ layout/            # 布局组件
│   ├── 🔧 plugins/           # 插件配置
│   ├── 🚏 router/            # 路由配置
│   ├── 📦 stores/            # Pinia状态管理
│   ├── 🔤 types/             # TypeScript类型
│   ├── 🛠️ utils/             # 工具函数
│   └── 📄 views/             # 页面组件
├── 📁 config/                 # 构建配置
├── 📁 scripts/                # 构建脚本
├── 📄 vite.config.mjs        # Vite配置
└── 📄 package.json           # 项目配置
```

</details>

**核心目录说明：**
- `src/api/` - 接口层,统一管理所有API调用
- `src/components/` - 公共组件,可复用的业务组件
- `src/composables/` - 组合式函数,逻辑复用的核心
- `src/layout/` - 布局组件,多种布局模式支持
- `src/views/` - 页面组件,具体的业务页面

## 🔧 核心组件

### 封装组件

| 🏷️ 组件名称 | 📋 功能描述 | ⭐ 核心特性 |
|:---:|:---|:---|
| **BearJiaProTable** | 企业级表格组件 | 工具栏/列设置/搜索/分页/导出/虚拟滚动 |
| **TableActionBar** | 表格操作列组件 | 智能自适应/统一配置数组/权限控制/暗色适配 |
| **SearchForm** | 高级搜索表单 | 动态表单/条件筛选/展开收起/快速重置 |
| **HistoryNav** | 多标签页导航 | 智能标签页/右键菜单/路由同步/持久化 |
| **WangEditor** | 富文本编辑器 | 图片上传/视频支持/自定义工具栏/主题适配 |
| **IconSelector** | 图标选择器 | SVG图标/Ant图标/分类展示/搜索过滤 |
| **FileUpload** | 文件上传组件 | 拖拽上传/进度显示/类型限制/预览下载 |
| **PageContainer** | 页面容器 | 标准布局/面包屑/标题/操作区/响应式 |
| **SettingDrawer** | 设置抽屉 | 主题配置/布局切换/实时预览/配置导出 |

### 组合式函数

| 🔧 Composable | 📋 功能说明 |
|:---:|:---|
| **useTable** | 表格状态管理,分页、搜索、刷新等逻辑封装 |
| **useDict** | 字典数据管理,自动缓存、响应式更新 |
| **useVirtualScroll** | 虚拟滚动实现,支持万级数据流畅渲染 |
| **useThemeComposition** | 主题管理组合式 API,亮暗切换/主题色/色弱模式 |

### 💡 组件设计理念
- 🎯 **高度封装**：开箱即用,减少 80% 重复代码
- 🔧 **灵活扩展**：丰富的插槽和配置选项,满足各种业务场景
- 📱 **响应式设计**：完美适配桌面、平板、手机等各种设备
- 🎨 **主题一致**：统一的视觉风格、交互体验和动画效果
- ⚡ **性能优越**：虚拟滚动、懒加载等优化,流畅体验

## 🏗️ 技术栈

<details>
<summary>📋 完整技术栈列表</summary>

| 类别 | 技术 | 版本 | 描述 |
|:---:|:---|:---:|:---|
| **核心框架** | Vue | 3.4.21 | 渐进式 JavaScript 框架 |
| **构建工具** | Vite | 5.1.4 | 下一代前端构建工具 |
| **UI 组件库** | Ant Design Vue | 4.1.2 | 企业级 UI 组件库 |
| **路由管理** | Vue Router | 4.3.0 | Vue 官方路由管理器 |
| **状态管理** | Pinia | 2.1.7 | Vue 官方状态管理 |
| **HTTP 客户端** | Axios | 1.6.7 | Promise based HTTP client |
| **类型支持** | TypeScript | 5.4.5 | JavaScript 的超集 |
| **富文本编辑器** | @wangeditor/editor | 5.1.23 | 轻量级富文本编辑器 |
| **图表库** | ECharts | 5.6.0 | 强大的数据可视化库 |
| **工具集** | @vueuse/core | 10.9.0 | Vue Composition 工具集 |
| **日期处理** | Day.js | 1.11.10 | 轻量级日期处理库 |
| **图标库** | @ant-design/icons-vue | 7.0.1 | Ant Design 图标库 |
| **文件保存** | file-saver | 2.0.5 | 客户端文件保存 |
| **代码高亮** | highlight.js | 11.11.1 | 语法高亮显示 |

</details>

## 📈 更新日志

### v1.4.0 (2026-05) - 全面重构版本 🚀

#### ✨ 主要特性
- **项目配置重构**：重构项目配置和依赖管理架构，构建配置模块化拆分至 `config/` 目录
- **状态管理重构**：重构应用状态管理和主题配置，优化 Pinia store 架构设计
- **代码生成器增强**：新增树表和主子表配置功能，支持模板类型动态切换和字段联动选择
- **树形组件集成**：添加通用树形组件功能，优化部门树查询逻辑和验证码展示
- **登录页面重设计**：全面重构登录页面 UI 和交互体验，优化品牌展示区域

#### 🎨 界面优化
- 整体配色优化调整，解决暗色模式下 404 页面显示异常
- 表格组件样式全面优化：固定列样式、排序图标、滚动条尺寸(8px→6px)和主题适配
- 顶部菜单和混合布局支持水平滚动，修复溢出显示问题
- 按钮和表单样式优化，提升暗色主题下的视觉体验
- 菜单右侧边框样式移除，界面更加简洁

#### 🔧 技术优化
- Vite 构建配置优化，调整 Ant Design Vue 打包策略
- 项目依赖管理重构，依赖按需加载，减少打包体积
- 全局错误处理优化，修复重复报错问题
- 加载逻辑优化，减少不必要的异步请求

#### 🐛 问题修复
- 修复顶部菜单布局溢出和显示异常
- 修复 Ant Design 表格测量行高度问题
- 修复布局和表格组件暗色模式显示问题
- 修复图标展示异常和 SVG 图标 filter 动态颜色问题
- 修复角色授权用户取消接口参数传递错误
- 去除多余的用户端布局，精简代码结构

#### 📊 技术数据
- 💻 代码量：60,000+ 行
- 📦 组件数：35+ 个封装组件
- 🎯 功能模块：18+ 个核心模块
- ⚡ 性能提升：构建速度提升 30%，打包体积减少 25%

### v1.3.0 (2024-09) - 开源发布版本 🎉

#### ✨ 主要特性
- **完整功能体系**：权限管理、系统监控、代码生成等核心功能模块
- **企业级组件**：文件管理系统(3,595行)、表单设计器(305行)、虚拟滚动(682行)
- **多主题系统**：支持亮色/暗色主题,10+ 种预设主题色
- **5种布局模式**：侧边栏、顶部、混合、分栏、抽屉布局
- **性能优化**：虚拟滚动性能提升 90%,内存占用降低 70%

#### 🎨 界面优化
- 重新设计登录页面品牌展示区域
- 优化消息中心展示样式和交互体验
- 新增 677 行图标系统代码,支持 SVG 和 Ant Design 图标

#### 🔧 技术优化
- Vite 构建配置优化,启动速度提升 40%
- 集成主题管理服务,支持动态主题切换
- 重构配置文件结构,统一配置管理
- 优化组件自动导入,减少代码量 80%

#### 🐛 问题修复
- 修复数据监控页面无法打开的问题
- 修复刷新页面后左侧菜单树未联动激活的问题
- 优化菜单显示逻辑,支持 alwaysShow 配置

#### 📊 技术数据
- 💻 代码量：50,000+ 行
- 📦 组件数：30+ 个封装组件
- 🎯 功能模块：15+ 个核心模块
- ⚡ 性能提升：开发启动快 40%,虚拟滚动快 90%

---

**完整更新记录请查看：** [CHANGELOG.md](./CHANGELOG.md)

## 🤝 参与贡献

我们欢迎所有形式的贡献！

详细贡献流程请参考：[CONTRIBUTING.md](./CONTRIBUTING.md)

### 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

### 开发规范

- 🔧 **代码规范**：遵循 ESLint 配置
- 📝 **提交规范**：使用 Conventional Commits
- 🧪 **测试覆盖**：新功能需要添加测试
- 📚 **文档更新**：重要变更需要更新文档

## 🐛 问题反馈

如果您在使用过程中遇到问题,请通过以下方式反馈:

1. **GitHub Issues**: [提交问题](https://github.com/javaxiaobear/BearJia/issues)
2. **Gitee Issues**: [提交问题](https://gitee.com/javaxiaobear/BearJia/issues)
3. **邮箱**: javaxiaobear@qq.com

## 📊 项目统计

- **Star数**: 持续增长中
- **Fork数**: 欢迎Fork和贡献
- **贡献者**: 感谢所有贡献者
- **更新频率**: 持续维护更新

## 📄 开源协议

本项目基于 [MIT License](../LICENSE) 开源协议。

## 🙏 致谢

感谢以下优秀的开源项目：

- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Ant Design Vue](https://antdv.com/) - 企业级UI组件库
- [Vite](https://vitejs.dev/) - 下一代前端构建工具
- [WangEditor](https://www.wangeditor.com/) - 轻量级富文本编辑器
- [ECharts](https://echarts.apache.org/) - 强大的数据可视化库
- [VueUse](https://vueuse.org/) - Vue Composition API 工具集

## 📞 联系方式

- 🌐 **个人网站**：[https://javaxiaobear.cn](https://javaxiaobear.cn)
- 📧 **邮箱**：javaxiaobear@qq.com
- 🐙 **GitHub**：[JavaXiaoBear](https://github.com/javaxiaobear)
- 🦄 **Gitee**：[JavaXiaoBear](https://gitee.com/javaxiaobear)
- 📱 **公众号**：小熊学Java
- 💬 **技术交流群**：欢迎加入讨论

---

<div align="center">

**🐻 Made with ❤️ by JavaXiaoBear**

如果这个项目对您有帮助，请给我们一个 ⭐ Star 支持一下！

[⬆ 回到顶部](#-bearjia-admin---vue3-前端框架)

</div>
