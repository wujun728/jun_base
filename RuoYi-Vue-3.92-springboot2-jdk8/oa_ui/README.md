# 陀螺匠·企业助手
开发前请仔细阅读开发规范，请严格按照开发规范进行开发，新增路由、页面、文件、组件请在此文档添加说明，开发代码尽量做好注释方便自己，方便他人。

## 技术栈
vue2 + vuex + vue-router + webpack + ES6/7 + axios + elementUI + 阿里图标iconfont

## 开发规范
统一使用 ES6 语法
方法注释
/\*
- th => 表头
- data => 数据
- fileName => 文件名
- fileType => 文件类型
- sheetName => sheet 页名
  \*/
  export default function toExcel ({ th, data, fileName, fileType, sheetName })
  行注释 //

### 命名
页面目录 文件夹命名格式骆驼式命名法,例如：用户列表 userList
例如：个人中心
user 个人中心
├─ company 我的企业
├─ examine 申请审批
├- forum 知识社区
页面命名、组建、文件夹 命名格式小驼峰命名法,例如：用户列表 userList
类名函数命名 大驼峰式 例如：addUser
变量命名 小驼峰式 例如：user 或者 userInfo
常量 采用全大些下划线命名 例如：VUE_APP_API_URl

### 文件管理规范
src 页面模块必须件文件夹区分
api 接口一个模块一个文件
assets 静态文件目录
components 全局组件目录
directive 自定义v-的js文件
lang 多语言
layout 初始化组件
libs 自定义常用函数
mixins 发送短信验证码
router 全局路由设置 一个模块在 modules 中建一个文件夹
store 路由状态管理，一个模块在 modules 中建一个文件夹
styles 样式尽量采用 element-ui 自带组件，common.less 系统通用样式不要轻易动
utils 自定义工具 js 独立命名，一般不用新建文件夹
views 前端代码静态文件目录
```
## 模块命名
├─ administration 行政管理
├─ business 办公审批
├─ product 商品管理
├─ customer 客户管理
├─ error-page 全局错误页
├─ user 用户管理
├─ fd 财务管理
├─ hr 人事管理
├─ logo 登录与注册页面
├─ setting 系统身份管理 系统权限管理、系统菜单管理、操作日志
├─ user 个人中心 工作台、我的日程、办公中心、企业动态、企业通讯录、职位说明、知识社区等等

```
```
## 目录结构
主要目录结构及说明：
├── public                           # 静态资源
│   ├── iconfont                     # iconfont 图标
│   └── UEditor                      # 编辑器图标
│   └── favicon.ico                  # 浏览器窗口小图标 
├── src                              # 源代码
│   ├── api                          # 所有接口api
│   │    └──administration.js        # 请求封装
│   │    └──business.js              # 有办公审批的接口
│   │    └──config.js                # 有关初始化、考勤模块的接口
│   │    └──enterprise.js            # 有企业相关的接口
│   │    └──request.js               # 有关api初始化的接口
│   │    └──setting.js               # 有关权限管理的接口
│   │    └──system.js                # 有关系统配置的接口
│   │    └──systemForm.js            # 有关表单组件的接口
│   │    └──user.js                  # 有关登录、用户的接口
│   ├── assets                       # 图片、svg 等静态资源
│   ├── icons                        # svg 等静态资源
│   ├── components                   # 公共组件
│   │    └──common                   # 项目基础组件
│   │    └──customer                 # 客户模块组件
│   │    └──develop                  # 低代码模块组件
│   │    └──form-common              # 封装的表单组件
│   │    └──form-designer            # 低代码表单设计页面
│   │    └──form-render              # 低代码表单渲染页面
│   │    └──hr                       # 人事模块组件
│   │    └──user                     # 办公模块组件
│   │    └──invoice                  # 发票模块组件
│   │    └──mlReferenceSearch        # 图表设计
│   │    └──openFile                 # 文件预览
│   │    └──scEcharts                # 图表设计
│   │    └──setting                  # 系统模块组件
│   │    └──simpleTable              # 图表设计-表格
│   │    └──svg-icon-nc              # 图表设计-图标
│   │    └──switchStatus             # 人事审批设置-开关组件
│   │    └──uploadPicture            # 图库管理
│   │    └──uploadFrom               # 上传附件
│   │    └──uploadPicture            # 上传图片
│   ├── layout                       # 导航布局
│   │    ├──index                    # 主页面
│   │    ├──components               # 导航组件
│   │        └──headerNotice         # 顶部消息中心
│   │        └──Document             # 侧边栏帮助中心
│   │        └──Header               # 顶部面包屑导航
│   │        └──TagsView             # tab标签页导航
│   │        └──Navbar               # 头部导航
│   │        └──AppMain              # 导航路由
│   │        └──index.js             # 组件引用
│   │    └──mixins                   # 自适应大小
│   ├── libs                         # 业务相关静态数据
│   │    └──settingMer               # 配置请求地址
│   ├── views                        # 所有页面

│   │    └──login                    # 登录
│   │         └──index               # 登录

│   │    └──user                     # 个人办公
│   │         └──workbench           # 工作台
│   │         └──calendar            # 我的日程
│   │         └──examine             # 申请审批
│   │         └──daily               # 我的汇报
│   │         └──assessment          # 绩效考核
│   │         └──cloudfile           # 云盘
│   │         └──notice              # 企业动态
│   │         └──ent                 # 企业通讯录
│   │         └──duty                # 职位说明
│   │         └──forum               # 知识社区
│   │         └──memorandum          # 记事本
│   │         └──news                # 消息中心
│   │         └──statistic           # 考勤统计

│   │    └──customer                 # 客户管理
│   │         └──list                # 客户列表
│   │         └──contract            # 合同管理
│   │         └──invoice             # 发票管理
│   │         └──turnover            # 业绩统计
│   │         └──setup               # 客户设置
│   │            └──work             # 业务设置
│   │            └──label            # 客户标签
│   │            └──type             # 合同分类

│   │    └──develop                  # 开发中心
│   │         └──approve             # 流程管理
│   │         └──event               # 触发器管理
│   │         └──crud                # 实体管理
│   │            └──design           # 实体设计
│   │            └──event            # 触发器设置
│   │            └──process          # 流程设置
│   │         └──dictionary          # 数据字典
│   │            └──management       # 字典管理

│   │    └──program                  # 项目
│   │         └──programList         # 我的项目
│   │         └──programTask         # 我的任务
│   │        
│   │    └──hr                       # 人事管理
│   │         └──archivesUser        # 员工档案
│   │         └──assess              # 绩效考核
│   │         └──attendance          # 考勤管理
│   │         └──training            # 员工培训
│   │         └──enterprise          # 人事管理
│   │              └──group          # 组织架构
│   │              └──job            # 职位管理
│   │              └──rank           # 职位管理
│   │    └──business                 # 审批
│   │         └──examine             # 办公审批
│   │         └──record              # 审批记录

│   │    └──fd                       # 财务管理
              └──examine             # 财务审核
│   │         └──invoice             # 发票审核
│   │         └──enterprise        
│   │              └──list           # 收支记账
│   │              └──chart          # 收支统计
│   │         └──setup               # 财务设置
│   │              └──income         # 收入分类
│   │              └──expenditure    # 支出分类
│   │              └──type           # 支付方式
│   │          

│   │    └──administration           # 行政管理
│   │         └──notice              # 企业动态
│   │         └──material 
│   │           └──chart             # 物资概览
│   │           └──list              # 物资记录
│   │           └──staff             # 物资管理

│   │    └──setting                  # 系统管理
│   │         └──enterprise          
│   │           └──info              # 企业信息
│   │           └──group             # 用户权限
│   │           └──admin             # 角色权限
│   │           └──group             # 用户权限
│   │         └──system         
│   │           └──menu              # 菜单管理
│   │           └──log               # 操作日志
│   │         └──enterprise      
│   │           └──workbench         # 工作台设置  
│   │           └──news              # 消息设置  
│   │           └──attach            # 素材管理 
│   │         └──data             
│   │           └──legal             # 权益数据
│   │           └──order             # 历史订单
│   │           └──invoice           # 发票申请
│   │  
│   ├── filters                      # 过滤器
│   ├── router                       # 路由配置
│   │    └──company.js               # 我的简历
│   │    └──index.js                 # 路由的汇总
│   ├── store                        # Vuex 状态管理
│   ├── utils                        # 全局公用方法
│   ├── styles                       # 样式管理
│   │     └──btn.scss                # 按钮样式
│   │     └──element-ui.scss
│   │     └──element-variables.scss
│   │     └──index.scss  
│   │     └──mixin.scss
│   │     └──sidebar.scss
│   │     └──styles.scss
│   │     └──transition.scss
│   │     └──variables.scss
│   │     └──workflow.scss
│   ├── permission.js              # 路由拦截
│   ├── setting.js                 # 业务配置文件
│   ├── main.js                    # 入口文件 加载组件 初始化等
│   └── App.vue                    # 入口页面
├── tests                          # 测试
├── .env.xxx                       # 环境变量配置
├── .eslintrc.js                   # eslint 配置项
├── .babelrc                       # babel-loader 配置
├── .travis.yml                    # 自动化CI配置
├── vue.config.js                  # vue-cli 配置
├── postcss.config.js              # postcss 配置
└── package.json                   # package.json

```

## 开发打包项目

````
# 进入项目目录
$ cd view-pc

# 安装依赖
$ npm install

# 启动项目(本地开发环境)
$ npm run dev

# 打包项目
$ npm run build:prod --report

# docker容器里面运行
$ docker-compose up -d
访问：http://localhost:3000/admin/  进入后台

