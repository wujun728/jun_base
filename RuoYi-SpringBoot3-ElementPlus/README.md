<p align="center">
	<img alt="logo" src="https://oscimg.oschina.net/oscnet/up-d3d0a9303e11d522a06cd263f3079027715.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">RuoYi-SpringBoot3-ElementPlus v3.9.1</h1>
<h4 align="center">基于 RuoYi-Vue3 的增强优化版本</h4>

## 📑 目录

- [项目简介](#-项目简介)
- [演示项目](#演示项目)
- [后端项目](#后端项目)
- [核心新特性](#-核心新特性)
- [功能对比](#-功能对比)
- [快速开始](#-快速开始)
- [项目结构](#-项目结构)
- [配置说明](#-配置说明)
- [内置功能](#-内置功能)
- [请我喝杯咖啡](#请我喝杯咖啡)
- [贡献指南](#-贡献指南)
- [许可证](#-许可证)
- [致谢](#-致谢)
- [联系方式](#-联系方式)
- [友情链接](#友情链接)

## 📝 项目简介

本项目是在 **RuoYi-Vue3** 基础上进行的深度优化和功能扩展版本，保留了原有的所有功能，并新增了大量实用的组件和工具，致力于提供更好的开发体验和更强大的功能支持。

| 公众号                                       | 微信交流群                                                      | QQ群                                          |
| -------------------------------------------- | --------------------------------------------------------------- | --------------------------------------------- |
| ![公众号](https://cdn.undsky.com/img/gh.jpg) | ![微信交流群](https://cdn.undsky.com/img/doudouqun.jpg?v=2.3.0) | ![QQ群](https://cdn.undsky.com/img/qqqun.jpg) |

## 演示项目

- 🖥️ Element Plus 版本：http://ry.undsky.com/admin/login?redirect=/index
- 🎨 Vue3 Prettier 版本：http://ry2.undsky.com/login?redirect=/index
- 🐻 BearJia Vue3 版本：http://ry1.undsky.com/login3

## 后端项目

- 💻 https://github.com/undsky/RuoYi-SpringBoot3-Pro

### 技术栈

- **前端框架**: Vue 3.5.16 + Vite 6.3.5
- **UI 组件库**: Element Plus 2.9.11 + Vant 4.9.17
- **状态管理**: Pinia 3.0.2
- **路由管理**: Vue Router 4.5.1
- **工具库**: xe-utils、dayjs、mitt、VueUse
- **富文本编辑器**: Vue Quill + UEditor
- **其他**: ECharts、axios、sortablejs、pdf-vue3

---

## ✨ 核心新特性

### 🎯 1. 智能化组件增强

#### 📍 省市区级联选择器 (RegionCascader)

一个功能强大的省市区三级级联选择器组件，专为中国行政区划设计。

**核心特性**：

- ✅ 支持省-市-区三级联动，灵活的层级控制（1-3 级可选）
- ✅ 按需懒加载数据，极大提升性能
- ✅ 支持 v-model 双向绑定
- ✅ 内置完整的错误处理机制
- ✅ 响应式设计，支持多种尺寸和样式
- ✅ 全局注册，开箱即用

**使用示例**：

```vue
<template>
    <!-- 三级联动（默认） -->
    <RegionCascader v-model="region" placeholder="请选择省市区" />

    <!-- 只显示省市 -->
    <RegionCascader v-model="region" :level="2" placeholder="请选择省市" />

    <!-- 只显示省份 -->
    <RegionCascader v-model="region" :level="1" placeholder="请选择省份" />
</template>

<script setup>
import { ref } from 'vue';
const region = ref([]);
</script>
```

[📖 查看完整文档](src/components/RegionCascader/README.md)

#### 💰 千分位数字输入框 (InputNumberQianfen)

基于 `xe-utils` 实现的智能千分位格式化输入框。

**核心特性**：

- ✅ 自动千分位格式化显示（如：1,234,567.89）
- ✅ 支持小数位数控制
- ✅ 输入时自动解析，输出纯数字
- ✅ 支持负数和小数
- ✅ 完美集成 Element Plus 输入框

**使用示例**：

```vue
<template>
    <InputNumberQianfen v-model="amount" :digits="2" placeholder="请输入金额" />
</template>

<script setup>
import { ref } from 'vue';
const amount = ref('');
</script>
```

#### 📝 UEditor 富文本编辑器 (UEditorPlus)

集成强大的 UEditor 富文本编辑器，提供更丰富的编辑功能。

**核心特性**：

- ✅ 支持图片、视频、音频上传
- ✅ 丰富的文本格式化选项
- ✅ 表格、公式、代码编辑
- ✅ 支持全屏编辑
- ✅ 自定义工具栏配置

#### 📸 ImageUpload 图片上传增强

对原有图片上传组件进行了全面增强，提供更强大的功能和更好的用户体验。

**🎯 核心增强特性**：

**1. 拖拽上传支持**

- ✅ 支持拖拽文件上传，操作更便捷
- ✅ 拖拽排序已上传的图片，调整显示顺序
- ✅ 集成 sortablejs 实现流畅的拖拽体验

**2. 视频支持**

- ✅ 支持上传视频文件（mp4, webm, ogg）
- ✅ 支持上传音频文件（mp3）
- ✅ 视频预览自动播放，带播放控制器
- ✅ 自动识别文件类型，图片和视频分别展示

**3. 图片懒加载**

- ✅ 使用 `loading="lazy"` 属性优化加载性能
- ✅ 大量图片时显著提升页面性能

**4. 高级预览功能**

- ✅ 集成 `el-image-viewer` 组件
- ✅ 支持图片放大、缩小、旋转
- ✅ 支持键盘快捷键操作
- ✅ 支持图片列表浏览，可切换查看

**5. 响应式设计**

- ✅ 完美适配移动端
- ✅ 移动端预览全屏显示
- ✅ 自动识别设备类型调整界面

**6. 更强大的文件支持**

- ✅ 文件大小限制从 5MB 提升到 50MB
- ✅ 支持自定义 accept 属性
- ✅ 支持更多文件格式

**7. 数据格式增强**

- ✅ 支持 JSON 格式数据
- ✅ 支持 Array/String/Object 多种数据格式
- ✅ 使用 xe-utils 进行数据处理

**8. 防重复删除机制**

- ✅ 添加状态标记防止重复删除
- ✅ 确保数据一致性

**对比原版改进**：

| 功能          | RuoYi-Vue3    | 本项目增强版  |
| ------------- | ------------- | ------------- |
| 拖拽上传      | ❌            | ✅ 支持       |
| 拖拽排序      | ✅ 基础       | ✅ 优化       |
| 视频支持      | ❌            | ✅ 完整支持   |
| 图片懒加载    | ❌            | ✅ 支持       |
| 图片预览器    | ❌ 简单对话框 | ✅ 专业浏览器 |
| 移动端适配    | ❌ 基础       | ✅ 完美适配   |
| 文件大小限制  | 5MB           | 50MB          |
| Accept 属性   | ❌            | ✅ 支持       |
| JSON 数据格式 | ❌            | ✅ 支持       |
| 防重复删除    | ❌            | ✅ 支持       |

**使用示例**：

```vue
<template>
    <el-form :model="form">
        <!-- 基础用法：上传图片 -->
        <el-form-item label="商品图片">
            <ImageUpload v-model="form.images" :limit="5" />
        </el-form-item>

        <!-- 上传视频 -->
        <el-form-item label="商品视频">
            <ImageUpload v-model="form.video" :limit="1" :file-type="['mp4', 'webm']" :file-size="100" accept="video/*" />
        </el-form-item>

        <!-- 拖拽上传 + 大文件 -->
        <el-form-item label="产品图册">
            <ImageUpload v-model="form.album" :limit="10" :file-size="50" :drag="true" />
        </el-form-item>

        <!-- 禁用状态（仅查看） -->
        <el-form-item label="已上传图片">
            <ImageUpload v-model="form.uploaded" :disabled="true" />
        </el-form-item>
    </el-form>
</template>

<script setup>
import { ref } from 'vue';

const form = ref({
    images: '', // 支持逗号分隔的字符串
    video: [], // 支持数组
    album: '[]', // 支持 JSON 字符串
    uploaded: ''
});
</script>
```

**Props 参数**：

| 参数       | 说明               | 类型                | 默认值                                        |
| ---------- | ------------------ | ------------------- | --------------------------------------------- |
| modelValue | 绑定值             | String/Array/Object | -                                             |
| action     | 上传接口地址       | String              | /common/upload                                |
| data       | 上传携带的额外参数 | Object              | -                                             |
| disabled   | 是否禁用           | Boolean             | false                                         |
| accept     | 接受的文件类型     | String              | \*                                            |
| limit      | 最大上传数量       | Number              | 5                                             |
| fileSize   | 文件大小限制(MB)   | Number              | 50                                            |
| fileType   | 文件类型限制       | Array               | ['png','jpg','jpeg','mp4','webm','ogg','mp3'] |
| isShowTip  | 是否显示提示       | Boolean             | true                                          |
| drag       | 是否启用拖拽排序   | Boolean             | true                                          |

**技术实现**：

- 基于 Element Plus Upload 组件
- 使用 sortablejs 实现拖拽排序
- 使用 el-image-viewer 实现图片预览
- 使用 xe-utils 进行数据处理

---

### 🛠️ 2. 开发工具增强

#### 🔍 代码检查器 (Code Inspector)

集成 `code-inspector-plugin`，实现浏览器中一键定位源代码。

**使用方法**：

1. 在浏览器中按住 `Ctrl/Cmd + Shift` 并点击任意元素
2. IDE 将自动打开对应的源代码文件
3. 极大提升开发调试效率

#### 🎨 Prettier 代码格式化

统一的代码格式化配置，确保团队代码风格一致。

**特性**：

- ✅ 自动格式化 Vue、JavaScript、CSS 等文件
- ✅ 支持保存时自动格式化
- ✅ 统一的缩进、引号、换行等规则

**配置文件**: `prettier.config.cjs`

---

### 📦 3. 工具库增强

#### ⏰ dayjs - 轻量级日期处理

替代 moment.js 的轻量级日期处理库，仅 2KB 大小。

```javascript
import dayjs from 'dayjs';

// 格式化日期
dayjs().format('YYYY-MM-DD HH:mm:ss');

// 相对时间
dayjs().from(dayjs('2024-01-01'));

// 日期计算
dayjs().add(7, 'day');
```

#### 🚌 mitt - 轻量级事件总线

Vue 3 推荐的事件总线解决方案，用于组件间通信。

```javascript
import mitt from 'mitt';

const emitter = mitt();

// 监听事件
emitter.on('foo', (e) => console.log('foo', e));

// 触发事件
emitter.emit('foo', { a: 'b' });
```

#### 🧰 xe-utils - 强大的工具函数库

提供 300+ 实用工具函数，覆盖日常开发常见场景。

```javascript
import { commafy, toNumber, clone, isEmpty } from 'xe-utils';

// 千分位格式化
commafy(1234567.89); // "1,234,567.89"

// 深拷贝
const obj2 = clone(obj1);

// 判空
isEmpty(null); // true
```

#### 📄 pdf-vue3 - PDF 预览

Vue 3 版本的 PDF 预览组件，支持在线预览 PDF 文件。

```vue
<template>
    <VuePDF :pdf="pdfUrl" />
</template>

<script setup>
import { VuePDF } from 'pdf-vue3';
const pdfUrl = ref('/path/to/file.pdf');
</script>
```

#### 🔀 sortablejs - 拖拽排序

强大的拖拽排序库，支持列表、表格、卡片等多种场景。

```vue
<template>
    <div ref="listRef">
        <div v-for="item in list" :key="item.id">{{ item.name }}</div>
    </div>
</template>

<script setup>
import Sortable from 'sortablejs';
import { onMounted } from 'vue';

const listRef = ref();

onMounted(() => {
    Sortable.create(listRef.value, {
        onEnd: (evt) => {
            console.log('拖拽完成', evt);
        }
    });
});
</script>
```

#### 📱 Vant - 移动端 UI 组件库

轻量、可靠的移动端 Vue 组件库，适配移动端场景。

```vue
<template>
    <van-button type="primary">按钮</van-button>
    <van-field v-model="value" placeholder="请输入用户名" />
</template>

<script setup>
import { Button, Field } from 'vant';
</script>
```

---

### 🚀 4. 部署工具增强

#### 📤 FTP/SFTP 自动部署

内置 FTP 和 SFTP 自动部署功能，构建后一键上传至服务器。

**配置文件**: `ftp/ftp.js` 或 `ftp/sftp.js`

```javascript
// ftp/ftp.js 配置示例
const config = {
    user: 'your-username', // FTP 用户名
    password: 'your-password', // FTP 密码
    host: 'your-host.com', // FTP 主机地址
    port: 21, // FTP 端口
    localRoot: './dist', // 本地构建目录
    remoteRoot: '/www/admin' // 远程目标路径
};
```

**使用方法**：

```bash
# 构建并自动部署
npm run build:prod
```

构建完成后会自动上传到配置的服务器，无需手动操作。

---

### 🔒 5. 安全增强

#### 🛡️ 三级等保 - 用户无操作自动登出

符合信息安全等级保护三级标准的自动登出机制，当用户在指定时间内无任何操作时，系统将自动登出以保护账户安全。

**核心特性**：

- ✅ **监控用户操作**：监听键盘、鼠标、滚动等用户交互行为
- ✅ **智能计时器**：用户每次操作后自动重置计时器
- ✅ **自动登出**：超时后自动退出登录，跳转至登录页面
- ✅ **灵活配置**：通过环境变量自定义超时时长
- ✅ **安全合规**：满足三级等保安全要求

**实现原理**：

```vue
<!-- src/App.vue -->
<script setup>
import useUserStore from '@/store/modules/user';
const userStore = useUserStore();

function logout() {
    userStore.logOut().then(() => {
        location.href = '/admin/index';
    });
}

onMounted(() => {
    // 从环境变量读取超时时长（毫秒）
    const logoutLimit = import.meta.env.VITE_LOGOUT_LIMIT;

    if (logoutLimit && logoutLimit > 0) {
        // 创建自动登出计时器
        let logoutTimer = setTimeout(logout, logoutLimit);

        // 用户操作时重置计时器
        let userOpDelay = () => {
            clearTimeout(logoutTimer);
            logoutTimer = setTimeout(logout, logoutLimit);
        };

        // 监听用户各种操作事件
        document.getElementById('app').addEventListener('keydown', userOpDelay);
        document.getElementById('app').addEventListener('mousemove', userOpDelay);
        document.getElementById('app').addEventListener('mousedown', userOpDelay);
        document.getElementById('app').addEventListener('click', userOpDelay);
        document.getElementById('app').addEventListener('scroll', userOpDelay);
    }
});
</script>
```

**配置方法**：

在项目根目录创建 `.env` 或 `.env.production` 文件：

```bash
# 设置自动登出时长（单位：毫秒）
# 例如：1800000 = 30分钟，3600000 = 60分钟

# 开发环境 (.env.development)
VITE_LOGOUT_LIMIT=1800000

# 生产环境 (.env.production)
VITE_LOGOUT_LIMIT=1800000

# 如果设置为 0 或不设置，则不启用自动登出
# VITE_LOGOUT_LIMIT=0
```

**使用场景**：

- ✅ 政府、金融等对安全要求高的行业
- ✅ 需要通过信息安全等级保护三级认证的系统
- ✅ 公共场所使用的管理系统
- ✅ 涉及敏感数据的后台管理系统

**监控的用户操作类型**：

- 🖱️ **鼠标操作**：移动（mousemove）、点击（mousedown/click）
- ⌨️ **键盘操作**：按键按下（keydown）
- 📜 **滚动操作**：页面滚动（scroll）

**安全说明**：

- 🔐 超时后立即清除所有用户会话信息
- 🔐 自动跳转至登录页，需要重新输入密码
- 🔐 防止用户离开座位后被他人非法操作
- 🔐 符合《信息安全技术 网络安全等级保护基本要求》GB/T 22239-2019

---

### 🎨 6. UI/UX 增强

#### 🔐 第二种登录页面

新增 `login2.vue` 登录页面样式，提供更多视觉选择。

特性：

- ✅ 现代化设计风格
- ✅ 响应式布局
- ✅ 流畅的动画效果

---

## 📊 功能对比

| 功能模块     | RuoYi-Vue3           | 本项目                   | 说明                       |
| ------------ | -------------------- | ------------------------ | -------------------------- |
| 基础框架     | Vue 3 + Element Plus | ✅ 完全兼容              | 保持一致                   |
| 省市区选择器 | ❌                   | ✅ RegionCascader        | 新增，支持三级联动         |
| 千分位输入   | ❌                   | ✅ InputNumberQianfen    | 新增，金额场景适用         |
| UEditor      | ❌                   | ✅ UEditorPlus           | 新增，功能更强大           |
| 图片上传     | ✅ 基础              | ✅ 增强版                | 拖拽上传、视频支持、懒加载 |
| 日期处理     | ❌                   | ✅ dayjs                 | 新增，更轻量               |
| 事件总线     | ❌                   | ✅ mitt                  | 新增，组件通信             |
| PDF 预览     | ❌                   | ✅ pdf-vue3              | 新增，在线预览             |
| 拖拽排序     | ❌                   | ✅ sortablejs            | 新增，拖拽功能             |
| 移动端 UI    | ❌                   | ✅ Vant                  | 新增，移动端适配           |
| 工具函数库   | ❌                   | ✅ xe-utils              | 新增，300+ 函数            |
| 代码检查器   | ❌                   | ✅ code-inspector        | 新增，开发效率提升         |
| 代码格式化   | ❌                   | ✅ Prettier              | 新增，统一代码风格         |
| AI 编程规范  | ❌                   | ✅ Cursor/Windsurf Rules | 新增，智能代码建议         |
| 自动部署     | ❌                   | ✅ FTP/SFTP              | 新增，一键部署             |
| 三级等保安全 | ❌                   | ✅ 无操作自动登出        | 新增，符合等保要求         |
| 登录页面     | 1 种                 | 2 种                     | 新增第二种样式             |

---

## 🚀 快速开始

### 环境要求

- Node.js >= 20
- npm 或 yarn 或 pnpm

### 安装依赖

```bash
# 克隆项目
git clone [项目地址]

# 进入项目目录
cd RuoYi-SpringBoot3-ElementPlus

# 安装依赖（推荐使用国内镜像）
npm install --registry=https://registry.npmmirror.com

# 或使用 yarn
yarn --registry=https://registry.npmmirror.com

# 或使用 pnpm
pnpm install --registry=https://registry.npmmirror.com
```

### 启动开发服务器

```bash
# 启动开发服务器
npm run dev

# 浏览器访问
http://localhost:80
```

### 构建生产环境

```bash
# 构建生产环境（会自动执行 FTP 部署）
npm run build:prod

# 构建测试环境
npm run build:stage

# 本地预览构建结果
npm run preview
```

---

## 📁 项目结构

```
RuoYi-SpringBoot3-ElementPlus/
├── .cursor/                      # 🆕 Cursor/Windsurf 编辑器配置
│   ├── rules/                   # 🆕 AI 编程规范 (Cursor)
│   │   ├── clean-code.mdc      # 简洁代码指南
│   │   ├── codequality.mdc     # 代码质量指南
│   │   ├── common.mdc          # 通用开发规范
│   │   ├── project.mdc         # 项目特定规范
│   │   ├── vue.mdc             # Vue 最佳实践
│   │   ├── vue-3-composition-api---general.mdc  # Vue3 组合式 API 指南
│   │   └── vue-3-project-structure.mdc  # Vue3 项目结构
│   └── mcp.json                # Cursor MCP 配置
├── .windsurf/                    # 🆕 Windsurf 编辑器配置
│   └── rules/                   # 🆕 AI 编程规范 (Windsurf)
├── ftp/                          # 🆕 FTP/SFTP 部署配置
│   ├── ftp.js                   # FTP 部署脚本
│   └── sftp.js                  # SFTP 部署脚本
├── public/                       # 静态资源
│   ├── favicon.ico
│   ├── logo.png
│   └── UEditorPlus/             # UEditor 资源文件
├── src/
│   ├── api/                      # API 接口
│   │   ├── biz/                 # 🆕 业务接口
│   │   │   └── Region.js        # 🆕 省市区接口
│   │   ├── login.js
│   │   ├── menu.js
│   │   ├── monitor/             # 系统监控
│   │   ├── system/              # 系统管理
│   │   └── tool/                # 系统工具
│   ├── assets/                   # 资源文件
│   │   ├── icons/               # 图标
│   │   ├── images/              # 图片
│   │   │   └── login-background2.jpg  # 🆕 新登录背景
│   │   ├── logo/                # Logo
│   │   └── styles/              # 样式文件
│   ├── components/               # 全局组件
│   │   ├── Breadcrumb/          # 面包屑
│   │   ├── Crontab/             # Cron 表达式
│   │   ├── DictTag/             # 字典标签
│   │   ├── Editor/              # Quill 富文本编辑器
│   │   ├── FileUpload/          # 文件上传
│   │   ├── IconSelect/          # 图标选择器
│   │   ├── ImageUpload/         # 🆕 图片上传增强版（拖拽、视频、懒加载）
│   │   ├── InputNumberQianfen/  # 🆕 千分位输入框
│   │   ├── Pagination/          # 分页组件
│   │   ├── RegionCascader/      # 🆕 省市区级联选择器
│   │   ├── RightToolbar/        # 右侧工具栏
│   │   ├── UEditorPlus/         # 🆕 UEditor 富文本编辑器
│   │   └── ...                  # 其他组件
│   ├── directive/                # 全局指令
│   ├── layout/                   # 布局组件
│   ├── plugins/                  # 插件
│   ├── router/                   # 路由配置
│   ├── store/                    # Pinia 状态管理
│   ├── utils/                    # 工具函数
│   ├── views/                    # 页面视图
│   │   ├── error/               # 错误页面
│   │   ├── login.vue            # 登录页面
│   │   ├── login2.vue           # 🆕 第二种登录页面
│   │   ├── monitor/             # 系统监控
│   │   ├── system/              # 系统管理
│   │   └── tool/                # 系统工具
│   ├── App.vue                   # 根组件
│   └── main.js                   # 入口文件
├── vite/                         # Vite 配置
│   └── plugins/                 # Vite 插件
├── .prettierignore              # 🆕 Prettier 忽略配置
├── prettier.config.cjs          # 🆕 Prettier 格式化配置
├── vite.config.js               # Vite 配置文件
├── package.json                 # 项目依赖
└── README.md                    # 项目说明

🆕 标记为本项目新增内容
```

---

## 🔧 配置说明

### 1. 后端接口配置

修改 `vite.config.js` 中的 `baseUrl`：

```javascript
const baseUrl = 'http://localhost:8087'; // 修改为你的后端接口地址
```

### 2. FTP 部署配置

编辑 `ftp/ftp.js` 或 `ftp/sftp.js`：

```javascript
const config = {
    user: '', // 你的 FTP 用户名
    password: '', // 你的 FTP 密码
    host: '', // FTP 主机地址
    port: 21, // FTP 端口
    localRoot: './dist',
    remoteRoot: '/dist' // 远程目标路径
};
```

### 3. Prettier 配置

编辑 `prettier.config.cjs` 自定义代码格式化规则。

### 4. 三级等保安全配置

在项目根目录创建环境变量文件：

**开发环境 `.env.development`**：

```bash
# 开发环境可以设置更长的超时时间，方便调试
VITE_LOGOUT_LIMIT=3600000  # 60分钟
```

**生产环境 `.env.production`**：

```bash
# 生产环境建议设置 30 分钟，符合三级等保要求
VITE_LOGOUT_LIMIT=1800000  # 30分钟
```

**测试环境 `.env.staging`**：

```bash
# 测试环境可与生产环境保持一致
VITE_LOGOUT_LIMIT=1800000  # 30分钟
```

**不启用自动登出**：

```bash
# 如果不需要自动登出功能，设置为 0 或不配置该变量
VITE_LOGOUT_LIMIT=0
```

### 5. 代码检查器配置

代码检查器已在 `vite/plugins/index.js` 中配置，开发时可直接使用。

---

## 📖 内置功能

基于 RuoYi-Vue3 的完整功能：

1. **用户管理**：用户是系统操作者，该功能主要完成系统用户配置
2. **部门管理**：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限
3. **岗位管理**：配置系统用户所属担任职务
4. **菜单管理**：配置系统菜单，操作权限，按钮权限标识等
5. **角色管理**：角色菜单权限分配、设置角色按机构进行数据范围权限划分
6. **字典管理**：对系统中经常使用的一些较为固定的数据进行维护
7. **参数管理**：对系统动态配置常用参数
8. **通知公告**：系统通知公告信息发布维护
9. **操作日志**：系统正常操作日志记录和查询；系统异常信息日志记录和查询
10. **登录日志**：系统登录日志记录查询包含登录异常
11. **在线用户**：当前系统中活跃用户状态监控
12. **定时任务**：在线（添加、修改、删除)任务调度包含执行结果日志
13. **代码生成**：前后端代码的生成（java、html、xml、sql）支持 CRUD 下载
14. **系统接口**：根据业务代码自动生成相关的 api 接口文档
15. **服务监控**：监视当前系统 CPU、内存、磁盘、堆栈等相关信息
16. **缓存监控**：对系统的缓存信息查询，命令统计等
17. **在线构建器**：拖动表单元素生成相应的 HTML 代码
18. **连接池监视**：监视当前系统数据库连接池状态，可进行分析 SQL 找出系统性能瓶颈

---

## 请我喝杯咖啡

如果项目对你有帮助，可以请我喝杯咖啡 ☕️

<img src="https://cdn.undsky.com/img/weixin10.jpg" max-width="300" height="500" /> <img src="https://cdn.undsky.com/img/zhifubao10.jpg" max-width="300" height="500" />

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

---

## 📄 许可证

本项目基于 [MIT License](LICENSE) 开源。

---

## 🙏 致谢

- 感谢 [RuoYi-Vue3](https://github.com/yangzongzhuan/RuoYi-Vue3) 提供的优秀基础框架
- 感谢所有开源组件的作者和贡献者

---

## 📞 联系方式

- 网站：[https://www.undsky.com](https://www.undsky.com)
- GitHub：[https://github.com/undsky/RuoYi-SpringBoot3-ElementPlus](https://github.com/undsky/RuoYi-SpringBoot3-ElementPlus)
- Issues：[https://github.com/undsky/RuoYi-SpringBoot3-ElementPlus/issues](https://github.com/undsky/RuoYi-SpringBoot3-ElementPlus/issues)

---

如果这个项目对你有帮助，请给个 Star ⭐ 支持一下！

## 友情链接

- [LINUX DO - 新的理想型社区](https://linux.do/)
