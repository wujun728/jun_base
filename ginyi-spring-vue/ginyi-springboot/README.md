### 项目地址

接口文档：http://114.132.120.190:8066/doc.html

代码仓库：https://gitee.com/Ginyi/ginyi-spring-vue



### 帮助文档

- 没有~不想写文档...如果`Star`数量破`1k`，可以考虑一下！
- 可以参考`Ruoyi`的文档 http://doc.ruoyi.vip/



### 项目特色

- 使用主流技术栈`SpringBoot`、`Mybatis-plus`、`Redis`、`MySQL`、`MongoDB`等，易上手！
- 基于`SpringSecurity`安全框架实现`RBAC`的权限模式，易扩展易维护！
- 基于`Maven`多模块，可在上层应用中创建多个服务！模块分明，各司其职！
- 内置多个后台系统常用功能，搭配完善的接口文档：
  - 系统管理：`用户管理`、`部门管理`、`岗位管理`、`角色管理`、`菜单管理`等
  - 日志管理：`登录日志`、`操作日志`、`在线用户`等



### 前置知识

**重点：**使用本项目时，你需要具备以下基础知识：

1. `SpringBoot`框架基础，熟悉`MVC`分层的开发模式！
2. `Maven`基础，掌握多模块项目下，服务间的依赖引用！
3. `Redis`、`MongoDB`中间件、`Mybatis`和`Mybatis-plus`等`ORM`框架的使用！



### 项目环境

**注意：**使用本项目时，为了你能愉快地进行开发，请尽可能使用以下环境进行开发：

1. `JDK v1.8`
2. `Maven v3.6.3`
3. `MySQL v8.0`
4. `IntelliJ IDEA`



### 项目结构

```bash
ginyi-springboot
|
├─ ginyi-common  # 通用模块
│  ├─ ginyi-common-annotation      # 通用注解
│  ├─ ginyi-common-constants       # 通用常量
│  ├─ ginyi-common-enums           # 通用枚举
│  ├─ ginyi-common-exception       # 通用异常处理
│  ├─ ginyi-common-mysql           # 通用MySql配置
│  ├─ ginyi-common-redis           # 通用Redis配置
│  ├─ ginyi-common-result          # 通用结果集
│  ├─ ginyi-common-swagger         # 通用在线接口文档配置
│  └─ ginyi-common-utils           # 通用工具类
|
├─ ginyi-framework  # 框架模块
│  ├─ ginyi-framework-core         # 框架核心
│  ├─ ginyi-framework-security     # 安全框架
│  └─ ginyi-framework-websocket    # 实时通讯框架
|
├─ ginyi-server   # 服务模块（对外提供api接口）
│  ├─ ginyi-server-admin           # 后台系统端（包含系统接口）
│  ├─ ginyi-server-common          # 通用服务
│  └─ ginyi-server-web             # 前台应用端
|
├─ ginyi-system  # 系统服务模块（包含yaml配置、系统Entity、系统Service、系统Mapper等）
|
└─ sql  # 数据库文件
```

