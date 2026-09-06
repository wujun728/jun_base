# Project Context

## Purpose

RuoYi-SpringBoot3-Pro 是基于 RuoYi-Vue 的企业级增强版快速开发框架。提供代码加密保护、三级等保支持、AI 对话能力、多数据库适配、低代码开发等企业级功能，适合中大型项目和政企系统快速开发。

## Tech Stack

- **核心框架**: Spring Boot 3.5.8
- **安全框架**: Spring Security 6.x
- **ORM 框架**: MyBatis-Plus 3.5+
- **数据库连接池**: Druid 1.2.27
- **数据库**: MySQL 5.7+ / PostgreSQL 12+ / 达梦 DM8 / 瀚高 6.2+ / 高斯 GaussDB
- **缓存**: Redis（可选）
- **定时任务**: Quartz 2.5.2
- **API 文档**: Springdoc 2.8.14
- **低代码平台**: Magic API 2.2.2
- **AI 能力**: OpenAI Java SDK
- **代码生成**: Velocity 2.3
- **JWT**: jjwt 0.9.1
- **JSON 解析**: Fastjson2 2.0.60

## Project Conventions

### Code Style

- 使用 Java 17 版本
- 遵循阿里巴巴 Java 开发规范
- Controller → Service → Mapper 标准分层架构
- 业务代码放在 `ruoyi-biz` 模块
- 实体类继承 `BaseEntity`，Mapper 继承 `BaseMapper<T>`
- 使用 MyBatis-Plus Lambda 查询语法
- 使用 `@TableName`、`@TableId` 等注解

### Architecture Patterns

**模块结构**:

- `ruoyi-admin`: 管理后台启动模块
- `ruoyi-framework`: 框架核心模块
- `ruoyi-system`: 系统管理模块
- `ruoyi-common`: 通用工具模块
- `ruoyi-biz`: 业务开发模块（新功能在此开发）
- `ruoyi-quartz`: 定时任务模块
- `ruoyi-generator`: 代码生成模块

**数据库配置**:

- 通过 `spring.profiles.active` 切换数据库
- `devmy`: MySQL
- `devpg`: PostgreSQL
- `devdm`: 达梦
- `devhg`: 瀚高

### Testing Strategy

- 单元测试使用 JUnit 5
- 接口测试通过 Springdoc API 文档进行
- Magic API 提供接口快速测试能力

### Git Workflow

- 主分支: `master`
- 特性分支: `feature/功能名`
- 提交信息格式: `类型: 简短描述`

## Domain Context

- 这是一个企业级后台管理系统框架
- 包含用户、角色、菜单、部门、岗位、字典、参数、日志等基础功能
- 支持多租户 SaaS 架构（通过 `tenant_id` 字段隔离）
- 内置三级等保安全策略（密码周期、登录锁定、IP 黑名单）

## Important Constraints

- JDK 要求 17 或 21
- Maven 版本 3.6+
- 配置文件中敏感信息需要加密保护
- 生产环境建议启用 ClassFinal 代码加密
- 多租户表需包含 `tenant_id`、`user_id`、`dept_id` 等标准字段

## External Dependencies

- **Maven 仓库**: 阿里云镜像 (`https://maven.aliyun.com/repository/public`)
- **API 服务**: 支持 OpenAI 协议的 AI 接口（可自定义）
- **Magic API**: 低代码接口开发平台，管理界面 `/magic/web`
- **Dify 工作流**: 可导入 `sql/Dify_数据库建表.yml` 自动生成建表语句
