### 温馨提示

1. `Ginyi-vue3`是一个`Vue3`的项目，如果你不了解`Vue3`，可以去看看其他框架。
2. `Ginyi-vue3`全量使用`script-return`，如果你不喜欢它，可以去看看其他框架。



### 项目地址

演示地址：http://114.132.120.190:3800 （`仅PC端`）

代码仓库：https://gitee.com/Ginyi/ginyi-spring-vue



### 帮助文档

- 没有~不想写文档...如果`Star`数量破`1k`，可以考虑一下！



### 项目特色

- 界面简洁、舒适！

- 动态菜单，粒子化的权限控制！

- 不使用代码规范插件，代码灵活不受约束，如需要可自行添加！

- 丰富的题主配置，内置多款主题色可供选择，同时可切换亮色/暗色模式！

- 使用最新技术栈`Vite4 + Vue3 + TypeScript + Pinia`，搭配优秀`Vue3`专用组件库`Naive UI`!

- 二次封装常用组件：`CommonTable、CommonForm、CommonModal`强强联合，15分钟搞定一套CURD！

- 提供多个自定义指令：`v-focus、v-draggable、v-permission`等，同时提供多个常用数据字典：`是否、正常禁用、性别`等，减少重复代码！



### 前置知识

**重点：**使用本项目时，你需要具备以下基础知识：

1. `Vue3`语法基础，可以不熟悉`<script setup>`语法糖，但需要掌握`setup-return`基础！
2. `TypeScript`语法基础，需要掌握的包括基础数据类型，接口等以及`TSX`语法！
3. `Pinia`语法基础，需要掌握如何创建`pinia`对象，如何管理组件的状态！
4. `Naive UI`语法基础，需要掌握组件库的使用！



### 项目环境

**注意：**使用本项目时，为了你能愉快地进行开发，请尽可能使用以下环境进行开发：

1. `Node v14.19.0`
2. `yarn v1.22.19` or `npm v6.14.16`，本项目使用`yarn`作为包管理工具
3. `IntelliJ IDEA 2021.3.1`，本项目使用`IDEA`作为开发工具



### 项目运行

```bash
# 安装依赖
yarn install 或者 npm install

# 启动项目
yarn dev 或者 npm run dev

# 打包项目
yarn build 或者 npm run build
```



### 目录结构

```bash
ginyi-vue3 
|
├─ src  # 源文件夹
│  ├─ api           # 网络请求
│  ├─ assets        # 静态资源文件，参与打包
│  ├─ components    # 全局组件
│  ├─ config        # 项目配置
│  ├─ dictionary    # 全局字典
│  ├─ directives    # 全局指令
│  ├─ enums         # 全局枚举
│  ├─ hooks         # 钩子函数（包含工具类）
│  ├─ interface     # 接口类型
│  ├─ layout        # 布局
│  ├─ plugins       # 插件
│  ├─ router        # 路由
│  ├─ store         # 状态管理
│  ├─ style         # 全局样式
│  └─ views         # 视图文件
|
└─ public  # 静态资源文件，不参与打包
```

