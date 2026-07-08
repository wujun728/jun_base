# AGENTS.md — jun_base_elementui (RuoYi-Vue)

## 全局工作规则

1. 全程使用**简体中文**沟通，回答保持专业、简洁。
2. 每次会话内容（提问及回答均保存）导出为 MD 文件至 `./docs/chat` 目录；文件名根据对话核心内容自动提炼，必须为中文。
3. 工作前优先读取 `./docs` 目录历史内容。
4. 生成脚本统一存放至 `./script` 目录；Python 脚本放 `./script/python`，SQL 脚本放 `./script/sql`。
5. 没有明确说明编译/运行时，无需编译或运行。
6. 生成脚本优先选择 Python，次选 PowerShell，次选 Shell。
7. 处理长整数 ID（雪花 ID 等）时不可丢失精度；通过 MCP 查询 MySQL 时用 `CAST(id AS CHAR)` 避免精度丢失。
8. 不要重复写代码，需有理有据，不确认的逻辑先行确认。
9. 默认只使用 RuoYi-Vue-3.92-springboot3-jdk17，暂不使用springboot4版本。特殊情况自当说明

## 项目架构

本仓库是**若依 (RuoYi-Vue) v3.9.2** 的派生项目，提供两个并行变体：

| 目录 | JDK | Spring Boot | 用途 |
|------|-----|-------------|------|
| `RuoYi-Vue-3.92/` | **17+** | **4.0.6** | 主版本，当前活跃开发 |
| `RuoYi-Vue-3.92-springboot2-jdk8/` | 1.8 | 2.5.15 | 兼容旧环境 |

根 `pom.xml` 仅聚合 `RuoYi-Vue-3.92` 模块（`io.github.wujun728:jun_base_elementui:1.25.01`）。

### 后端模块（Maven 多模块）

| 模块 | 职责 |
|------|------|
| `ruoyi-admin` | 入口模块（SpringBootApplication），含 Controller |
| `ruoyi-common` | 通用工具、枚举、异常 |
| `ruoyi-framework` | 框架层（Security、数据源、AOP、过滤器） |
| `ruoyi-system` | 系统业务（用户、角色、菜单、部门等 Service + Mapper） |
| `ruoyi-quartz` | 定时任务管理 |
| `ruoyi-generator` | 代码生成器（Velocity 模板驱动） |

包根：`com.ruoyi`

### 前端

- Vue 2.6 + Element UI 2.15 + Vue CLI 4（Webpack），**不是 Vite**
- 目录：`ruoyi-ui/`
- 后端代理目标：`http://localhost:8080`（通过 `vue.config.js` 中的 `baseUrl` 配置）

## 构建命令

```bash
# === 后端（主版本，JDK 17+）===
# 编译整个项目（跳过测试）
cd RuoYi-Vue-3.92
mvn clean install -DskipTests

# 运行单个测试
mvn test -Dtest=ClassName
mvn test -Dtest=ClassName#methodName

# 启动（入口在 ruoyi-admin）
mvn spring-boot:run -pl ruoyi-admin

# 打包后运行
java -jar ruoyi-admin/target/ruoyi-admin.jar

# === 后端（SpringBoot 2 / JDK 8 兼容版本）===
cd RuoYi-Vue-3.92-springboot2-jdk8
mvn clean install -DskipTests

# === 前端 ===
cd ruoyi-ui
npm run dev          # 开发模式（默认端口 80）
npm run build:prod   # 生产构建
```

## 必须知道的约定

### 后端

- **框架栈**：Spring Boot + Spring Security + JWT + MyBatis + Druid + Redis
- **API 文档**：Springdoc OpenAPI 3（`/v3/api-docs`，`/swagger-ui.html`），**不是 Swagger 2**
- **Controller 返回**：统一使用 `AjaxResult`（`com.ruoyi.common.core.domain.AjaxResult`）包装
- **分页**：PageHelper，Controller 中使用 `startPage()` 后查询
- **实体**：`com.ruoyi.**.domain` 下的类，MyBatis XML 映射到 `mapper/**/*Mapper.xml`
- **权限**：`@PreAuthorize("@ss.hasPermi('xxx:xxx:xxx')")` 或 `@PreAuthorize("@ss.hasRole('admin')")`
- **主键**：自增 ID（非雪花），数据库 `int` / `bigint`
- **日志**：使用 `com.ruoyi.common.utils.LogUtils` 记录操作日志
- **数据库**：Druid 连接池，主从读写分离（从库默认关闭），MySQL 驱动 `com.mysql.cj.jdbc.Driver`

### 数据库

- 默认数据库名：`ry-vue`（两个变体均使用此名）
- 主版本端口：**3307**，springboot2 版本端口：**3306**
- Redis：`localhost:6379`，无密码，db 0
- 初始化 SQL：`sql/` 目录下的 `ry_*.sql` 和 `quartz.sql`

### 代码生成器（ruoyi-generator）

- 模板引擎：Apache Velocity
- 代码模板路径：`ruoyi-generator/src/main/resources/vm/`
- 入口：`ruoyi-admin` 的 `GenController`
- 数据库表导入后一键生成 CRUD（entity、mapper xml、service、controller、vue 页面）

### 日期类型

后端使用 `java.util.Date`（项目原始风格），非 `LocalDateTime`。

### 前端

- 项目基于 Vue CLI（Webpack），**不是 Vite**
- `@` 别名指向 `src/`
- SVG 图标使用 `svg-sprite-loader`，图标存放于 `src/assets/icons/`
- 环境变量文件：`.env.development`、`.env.production`、`.env.staging`
- `VUE_APP_BASE_API` 是前端请求前缀，通过代理转发到后端

## 两个变体的关键差异

| 特性 | RuoYi-Vue-3.92 (主) | springboot2-jdk8 |
|------|---------------------|-------------------|
| JDK | 17+ | 1.8 |
| Spring Boot | 4.0.6 | 2.5.15 |
| API 文档 | Springdoc OpenAPI | Springdoc OpenAPI |
| 默认 MySQL 端口 | 3307 | 3306 |
| javax/jakarta | jakarta | javax |

## 其他

- Maven 仓库：阿里云镜像（`maven.aliyun.com/repository/public`）
- 两个变体**互不依赖**，分别独立构建
- 修改基础工具类（`ruoyi-common`）时，需要完整的 `mvn install` 使其他模块获取更新
