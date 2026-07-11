# OA项目与项目1 迁移至研发基座 — 详细方案

> **基座版本**: RuoYi-Vue 3.9.2 — JDK 17 + Spring Boot 3.5.14  
> **文档日期**: 2026-07-08  
> **状态**: 方案评审中

---

## 一、背景与目标

### 1.1 现状

| 项目 | JDK | Spring Boot | RuoYi 版本 | 自定义模块数 | 前端 |
|------|-----|-------------|-----------|-------------|------|
| **研发基座** (`RuoYi-Vue-3.92-springboot3-jdk17`) | 17 | 3.5.14 | 3.9.2 | 0（纯标准） | Vue2 + Element UI（1个） |
| **OA项目** (`ruoyi-vue-oa`) | 1.8 | 2.5.15 | 3.9.0 | 22个 | PC端 + UniApp移动端（2个） |
| **项目1** (`RuoYi-Vue-S`) | 1.8 | 2.5.5 | 3.7.0 | 4个 | PC端（1个） |

### 1.2 核心目标

1. 将 OA 项目与项目1 的**全部自定义业务模块**迁移到研发基座中
2. 保持研发基座**可持续升级**最新若依版本，对基座改动最小化
3. 后续其他模块可按相同模式接入

### 1.3 核心原则

- **插件化架构**：业务模块独立，仅通过根 pom `<modules>` 声明接入
- **不改基座核心**：`ruoyi-common`、`ruoyi-framework`、`ruoyi-system` 原样不动
- **扩展走扩展模块**：通用扩展抽到 `ruoyi-common-ext`，业务模块只依赖它
- **前端聚合**：前端页面和 API 并入基座 `ruoyi-ui`，移动端独立部署

---

## 二、迁移后目标架构

### 2.1 模块全景图

```
ruoyi（根 POM，packaging=pom，version=3.9.2）
│
├── 【若依核心 — 不改动 ─────────────────】
│   ├── ruoyi-admin          ← 入口（只加依赖引入，不写代码）
│   ├── ruoyi-common          ← 通用工具（原样不动）
│   ├── ruoyi-framework       ← 框架层（原样不动）
│   ├── ruoyi-system          ← 系统业务（原样不动）
│   ├── ruoyi-quartz          ← 定时任务（原样不动）
│   └── ruoyi-generator       ← 代码生成（原样不动）
│
├── 【通用扩展 — 新增 ───────────────────】
│   └── ruoyi-common-ext     ← 从OA抽出的通用扩展（脱敏、MapStruct、Redisson等）
│
├── 【OA项目业务模块 — 迁移 ──────────────】
│   ├── ruoyi-parent-flow/   ← 流程审批组（5个子模块）
│   │   ├── ruoyi-flowable
│   │   ├── ruoyi-workflow
│   │   ├── ruoyi-workflow-file
│   │   ├── ruoyi-template
│   │   └── ruoyi-seal
│   ├── ruoyi-parent-msg/    ← 消息通讯组（6个子模块）
│   │   ├── ruoyi-im-broker
│   │   ├── ruoyi-im-process
│   │   ├── ruoyi-message
│   │   ├── ruoyi-mq-core
│   │   └── ruoyi-mq-async
│   ├── ruoyi-parent-biz/    ← 业务功能组（6个子模块）
│   │   ├── ruoyi-biz-sdk
│   │   ├── ruoyi-information
│   │   ├── ruoyi-kbs
│   │   ├── ruoyi-schedule
│   │   ├── ruoyi-serial
│   │   └── ruoyi-worksetting
│   ├── ruoyi-parent-common/ ← 工具基础组（4个子模块）
│   │   ├── ruoyi-tools
│   │   ├── ruoyi-file
│   │   ├── ruoyi-sms
│   │   └── ruoyi-todo
│   └── ruoyi-qixing         ← 齐兴事务所独立模块
│
├── 【项目1业务模块 — 迁移 ──────────────】
│   ├── ruoyi-tenant         ← 多租户
│   ├── ruoyi-data-rule      ← 数据权限
│   ├── ruoyi-oss            ← OSS对象存储
│   └── ruoyi-biz            ← Demo示例模块
│
├── 【前端 ─────────────────────────────】
│   ├── ruoyi-ui/            ← 聚合后的PC端（基座 + OA + 项目1 页面）
│   └── ruoyi-oa-mobile/     ← UniApp移动端（独立部署）
│
└── sql/                     ← 聚合后的初始化脚本
```

### 2.2 依赖关系图

```
ruoyi-admin
  ├── ruoyi-framework ──────► ruoyi-system ──────► ruoyi-common
  ├── ruoyi-quartz ─────────► ruoyi-common
  ├── ruoyi-generator ──────► ruoyi-common
  │
  │  【业务模块 — 由 admin 引入，自动扫描】
  ├── ruoyi-flowable ───────► ruoyi-common
  ├── ruoyi-workflow ───────► ruoyi-common + ruoyi-flowable
  ├── ruoyi-tenant ─────────► ruoyi-common
  ├── ruoyi-data-rule ──────► ruoyi-common
  ├── ruoyi-oss ────────────► ruoyi-common
  └── ...（其他业务模块类似，均依赖 ruoyi-common）
```

---

## 三、关键技术差异与适配方案

### 3.1 差异清单

| 维度 | 研发基座 | OA项目 | 项目1 | 适配方案 |
|------|---------|--------|-------|---------|
| **JDK** | 17 | 1.8 | 1.8 | 升级到 17 |
| **Spring Boot** | 3.5.14 | 2.5.15 | 2.5.5 | 升级到 3.x |
| **命名空间** | `jakarta.*` | `javax.*` | `javax.*` | javax → jakarta 全局替换 |
| **Servlet API** | jakarta.servlet | javax.servlet | javax.servlet | 替换 import |
| **API 文档** | Springdoc 2.8.17 | Springfox 3.0 | Springfox 3.0 | 迁移到 Springdoc |
| **Druid** | `druid-spring-boot-3-starter` | standard starter | standard starter | 用 boot-3 专用版 |
| **MyBatis** | MyBatis 3.0.5 | MyBatis | **MyBatis-Plus** 3.4.2 | 项目1需要 MP 适配 Spring Boot 3 |
| **Flowable** | — | 6.7.2 | — | 需升级到 7.x（支持 Spring Boot 3） |
| **Swagger 注解** | `io.swagger.v3` | `io.swagger` (v2/springfox) | `io.swagger` (v2/springfox) | 迁移到 v3 注解 |
| **Lombok** | 无 | 1.18.20 | 无 | 可选引入 |
| **MapStruct** | 无 | 1.5.5 | 无 | 保留 |
| **Redisson** | 无 | 3.16.2 | 无 | 保留，升级版本 |

### 3.2 迁移改造量评估

| 改造项 | OA项目 | 项目1 | 自动化程度 |
|--------|--------|-------|-----------|
| javax → jakarta | ~所有 Java 文件 | ~所有 Java 文件 | **可脚本批量替换** |
| Springfox → Springdoc | ~40 个 Controller 注解 | ~34 个 Controller 注解 | **70% 可脚本化** |
| Swagger v2 → v3 注解 | `@Api` → `@Tag` 等 | `@Api` → `@Tag` 等 | **可脚本批量替换** |
| Flowable 6→7 适配 | ~5 个模块 API | — | **需手动验证**，API 基本兼容 |
| MyBatis-Plus 3→3.5+ | — | 1 个模块 | **升级版本即可** |
| Spring Security 5→6 | — | — | 基座已处理，业务模块无感知 |
| 配置文件调整 | druid/security 配置 | druid/security 配置 | **少量手动** |

---

## 四、详细迁移步骤

### 阶段一：准备工作（0.5 天）

#### 4.1 创建分支

```bash
# 在研发基座仓库创建迁移分支
git checkout -b feature/migrate-oa-and-project1
```

#### 4.2 创建 `ruoyi-common-ext` 扩展模块

**目的**：存放从 OA 项目 `ruoyi-common` 中扩展的通用工具，避免直接修改基座的 `ruoyi-common`。

**需要从 OA 的 ruoyi-common 复制的内容**：

| 源路径 (OA) | 目标路径 (基座) | 说明 |
|------------|----------------|------|
| `annotation/Sensitive.java` | `ruoyi-common-ext/.../annotation/` | 脱敏注解 |
| `config/serializer/SensitiveJsonSerializer.java` | `ruoyi-common-ext/.../config/` | Jackson 脱敏序列化 |
| `enums/DesensitizedType.java` | `ruoyi-common-ext/.../enums/` | 脱敏类型枚举 |
| `enums/WhetherStatus.java` | `ruoyi-common-ext/.../enums/` | 是否状态 |
| `utils/DesensitizedUtil.java` | `ruoyi-common-ext/.../utils/` | 脱敏工具 |
| `utils/DictUtils.java` | `ruoyi-common-ext/.../utils/` | 字典工具 |
| `utils/sign/RsaUtils.java` | `ruoyi-common-ext/.../utils/` | RSA 签名 |
| `utils/sign/Md5Utils.java` | `ruoyi-common-ext/.../utils/` | MD5 工具 |
| `utils/sign/Base64.java` | `ruoyi-common-ext/.../utils/` | Base64 工具 |
| `module/CommonOptions.java` | `ruoyi-common-ext/.../module/` | 通用选项接口 |
| `module/OptionsVO.java` | `ruoyi-common-ext/.../module/` | 选项 VO |

**pom.xml 关键依赖**：
```xml
<dependencies>
    <dependency>
        <groupId>com.ruoyi</groupId>
        <artifactId>ruoyi-common</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
    </dependency>
    <dependency>
        <groupId>org.redisson</groupId>
        <artifactId>redisson-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

### 阶段二：OA 项目迁移（5-7 天）

#### 4.3 迁移 OA 后端模块

> **前提**：先执行 javax → jakarta 和 Springfox → Springdoc 的批量替换脚本

**步骤 2.1 — 复制模块目录**

将以下目录完整复制到基座根目录：

```
源: D:\workspace_github\jun_product_center_private_qixing\ruoyi-vue-oa\
目标: D:\workspace_github\jun_base_elementui_jdk17\RuoYi-Vue-3.92-springboot3-jdk17\

复制项:
├── ruoyi-parent-flow/     (含5个子模块)
├── ruoyi-parent-msg/      (含6个子模块)
├── ruoyi-parent-biz/      (含6个子模块)
├── ruoyi-parent-common/   (含4个子模块)
└── ruoyi-qixing/          (含1个子模块)
```

**步骤 2.2 — javax → jakarta 批量替换（自动化脚本）**

对每个模块的 `src/main/java/**/*.java` 执行：

```powershell
# 1. javax 包名替换
find . -name "*.java" -exec sed -i 's/javax\./jakarta./g' {} \;

# 2. javax 配置属性替换  
find . -name "*.yml" -o -name "*.properties" -o -name "*.xml" -exec sed -i 's/javax\./jakarta./g' {} \;

# 3. 特定 import 精确替换 (Servlet API)
# javax.servlet.* → jakarta.servlet.*
# javax.annotation.* → jakarta.annotation.*
# javax.persistence.* → jakarta.persistence.*  (如果没有则跳过)
# javax.validation.* → jakarta.validation.*
```

**步骤 2.3 — 逐模块修改 pom.xml**

每个子模块的 pom.xml 修改项：

| 修改点 | 原值 (OA) | 新值 (基座) |
|--------|----------|------------|
| parent artifactId | `ruoyi` (OA的根) | `ruoyi` (基座根) |
| parent version | `3.9.0` | `3.9.2` |
| Java version | `1.8` | `17` |
| ruoyi-common 依赖版本 | 继承自 OA | 继承自基座 |
| Druid 依赖 | `druid-spring-boot-starter` | `druid-spring-boot-3-starter` |
| Flowable 版本 | 6.7.2 | **7.1.0** (支持 Spring Boot 3) |
| Servlet API | `javax.servlet:javax.servlet-api` | 删除（基座已通过 Jakarta 提供） |

父 POM 组（如 `ruoyi-parent-flow/pom.xml`）修改：

```xml
<!-- 原 -->
<parent>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi</artifactId>
    <version>3.9.0</version>
</parent>

<!-- 新：指向基座根 POM -->
<parent>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi</artifactId>
    <version>3.9.2</version>
    <relativePath>../../pom.xml</relativePath>
</parent>
```

**步骤 2.4 — Swagger 注解迁移 (Springfox → Springdoc)**

| Springfox (OA) | Springdoc (基座) |
|---------------|-----------------|
| `@Api(tags = "xxx")` | `@Tag(name = "xxx")` |
| `@ApiOperation("xxx")` | `@Operation(summary = "xxx")` |
| `@ApiParam` | `@Parameter` |
| `@ApiModel` | `@Schema` |
| `@ApiModelProperty` | `@Schema` |
| `@ApiIgnore` | `@Hidden` |
| `import springfox.*` | `import io.swagger.v3.oas.annotations.*` |

批量替换脚本（Powershell）：

```powershell
# @Api → @Tag
Get-ChildItem -Recurse -Filter "*.java" | ForEach-Object {
    (Get-Content $_.FullName) `
        -replace 'import springfox.documentation.annotations.ApiIgnore;', 'import io.swagger.v3.oas.annotations.Hidden;' `
        -replace '@ApiIgnore', '@Hidden' `
        -replace 'import io.swagger.annotations.Api;', 'import io.swagger.v3.oas.annotations.tags.Tag;' `
        -replace 'import io.swagger.annotations.ApiOperation;', 'import io.swagger.v3.oas.annotations.Operation;' `
        -replace 'import io.swagger.annotations.ApiParam;', 'import io.swagger.v3.oas.annotations.Parameter;' `
        -replace 'import io.swagger.annotations.ApiModel;', 'import io.swagger.v3.oas.annotations.media.Schema;' `
        -replace 'import io.swagger.annotations.ApiModelProperty;', 'import io.swagger.v3.oas.annotations.media.Schema;' `
        -replace '@Api\(tags\s*=\s*"([^"]*)"\)', '@Tag(name = "$1")' `
        -replace '@Api\(tags\s*=\s*\{([^}]*)\}\)', '@Tags({...})'  # 需手动处理数组情况
        -replace '@ApiOperation\("([^"]*)"\)', '@Operation(summary = "$1")' `
        -replace '@ApiModel\("([^"]*)"\)', '@Schema(description = "$1")' `
        -replace '@ApiModelProperty\("([^"]*)"\)', '@Schema(description = "$1")' `
        -replace '@ApiParam\(name\s*=\s*"([^"]*)",?\s*value\s*=\s*"([^"]*)"\)', '@Parameter(name = "$1", description = "$2")' `
        | Set-Content $_.FullName
}
```

**步骤 2.5 — Flowable 6.x → 7.x 适配**

Flowable 7.x 主要变化：
- 包路径不变（`org.flowable.*`）
- Spring Boot 3 支持内置
- 部分废弃 API 移除（如 `ProcessEngineConfiguration` 的部分方法）
- 自动配置类名可能变化

具体操作：
1. `ruoyi-flowable/pom.xml` 中将 Flowable 版本改为 `7.1.0`
2. 检查编译错误，逐个修复（Flowable 6→7 API 变化极小）
3. BPMN 资源文件（`*.bpmn20.xml`）无需变更

**步骤 2.6 — ruoyi-admin 引入业务模块**

在 `ruoyi-admin/pom.xml` 中添加依赖（**只加依赖，不写代码**）：

```xml
<!-- OA 业务模块 -->
<dependency>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi-flowable</artifactId>
</dependency>
<dependency>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi-workflow</artifactId>
</dependency>
<dependency>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi-workflow-file</artifactId>
</dependency>
<dependency>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi-template</artifactId>
</dependency>
<dependency>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi-seal</artifactId>
</dependency>
<!-- ... 其他模块 ... -->
<dependency>
    <groupId>com.ruoyi</groupId>
    <artifactId>ruoyi-qixing</artifactId>
</dependency>
```

> **重要**：业务模块需使用 Spring Boot 自动配置（`spring.factories` / `@AutoConfiguration`）来注册自己的 Controller、Service，不要手动修改 ruoyi-admin 的包扫描路径。

每个业务模块添加自动配置类（如果需要）：

```java
// ruoyi-workflow/src/main/java/com/ruoyi/workflow/config/WorkflowAutoConfiguration.java
@AutoConfiguration
@ComponentScan(basePackages = "com.ruoyi.workflow")
public class WorkflowAutoConfiguration {
}
```

以及在 `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 中声明（Spring Boot 3.x 方式）。

#### 4.4 迁移 OA 数据库

**步骤 2.7 — 数据库表迁移**

1. 从 OA 的 `sql/table.sql` 中提取**所有自定义业务表**（排除已存在于基座的 ruoyi 标准表、quartz 表）
2. 生成新的 `sql/ry_oa_*.sql`，包含：
   - Flowable 表（`act_*`）
   - ruoyi-workflow 相关表
   - ruoyi-kbs 知识库表
   - ruoyi-schedule 日程表
   - ruoyi-qixing 全部业务表
   - 等等
3. 从 OA 的 `sql/data.sql` 中提取初始数据（菜单、字典等）

**脚本生成建议**（创建 `script/sql/extract_oa_tables.py`）：

```python
# 伪代码：从 table.sql 中识别非标准表
standard_tables = ['sys_', 'qrtz_', 'gen_']  # 若依标准前缀
oa_custom_tables = extract_tables_starting_with(['act_', 'flw_', 'oa_', 'pj_', 'hr_', 'biz_', 'kbs_', ...])
```

---

### 阶段三：项目1 迁移（3-4 天）

#### 4.5 迁移项目1后端模块

**相较 OA 的特殊处理**：

| 问题 | 处理方式 |
|------|---------|
| MyBatis-Plus 3.4.2 → 3.5.x+ | 升级版本到 3.5.7+（支持 Spring Boot 3），API 基本兼容 |
| MyBatis-Plus 配置类 | `MybatisPlusConfig` 中 `PaginationInterceptor` → `MybatisPlusInterceptor` + `PaginationInnerInterceptor` |
| Mapper XML 位置 | 项目1 的 xml 在 `src/main/java/.../xml/` 下，需移到 `src/main/resources/mapper/` 或配置 `mapper-locations` |
| Beetl 模板引擎 | 如无使用可移除 |
| Swagger 2/Springfox | 同 OA 处理，迁移到 Springdoc |
| javax → jakarta | 同 OA 处理 |

**步骤 3.1 — 复制模块**

```
源: D:\workspace_github\jun_base_code\RuoYi-Vue-S\
目标: D:\workspace_github\jun_base_elementui_jdk17\RuoYi-Vue-3.92-springboot3-jdk17\

复制项:
├── ruoyi-tenant/
├── ruoyi-data-rule/
├── ruoyi-oss/
└── ruoyi-biz/
```

**步骤 3.2 — MyBatis-Plus 升级要点**

```xml
<!-- pom.xml 中 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.7</version>  <!-- 支持 Spring Boot 3 -->
</dependency>
```

配置类改造：

```java
// 旧 (MP 3.4.x)
@Bean
public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
}

// 新 (MP 3.5.x)
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
}
```

**步骤 3.3 — 多租户模块适配**

`ruoyi-tenant` 模块需要确认：
- 基座的 `ruoyi-framework` 中 Security 配置与租户拦截器是否冲突
- 如果基座已有 `TenantConfiguration`，需要合并而不是覆盖

**步骤 3.4 — ruoyi-admin 引入**

同 OA 项目，在 `ruoyi-admin/pom.xml` 添加依赖即可。

---

### 阶段四：前端聚合（3-4 天）

#### 4.6 PC 端前端合并策略

**策略**：将 OA 前端和项目1 前端的**自定义页面、组件、API、路由**合并入基座的 `ruoyi-ui`。

**步骤 4.1 — 合并 src/views/** 

```
ruoyi-ui/src/views/
  ├── [保留] 基座已有: dashboard/ error/ login.vue register.vue ...
  ├── [保留] 基座已有: monitor/ system/ tool/
  │
  ├── [新增-OA] workflow/          ← 从 oa-ui/src/views/workflow/ 复制
  ├── [新增-OA] kbs/               ← 从 oa-ui/src/views/kbs/ 复制
  ├── [新增-OA] schedule/          ← 从 oa-ui/src/views/schedule/ 复制
  ├── [新增-OA] information/       ← 从 oa-ui/src/views/information/ 复制
  ├── [新增-OA] holiday/           ← 从 oa-ui/src/views/holiday/ 复制
  ├── [新增-OA] qixing/            ← 从 oa-ui/src/views/qixing/ 复制
  ├── [新增-OA] serial/            ← 从 oa-ui/src/views/serial/ 复制
  ├── [新增-OA] setting/           ← 从 oa-ui/src/views/setting/ 复制
  ├── [新增-OA] home/              ← 从 oa-ui/src/views/home/ 复制
  ├── [新增-OA] notice/            ← 从 oa-ui/src/views/notice/ 复制
  │
  └── [新增-S]  demo/              ← 从 RuoYi-Vue-S 前端 views/demo/ 复制
```

**步骤 4.2 — 合并 src/api/**

```
ruoyi-ui/src/api/
  ├── [新增-OA] workflow/          ← 从 oa-ui/src/api/workflow/ 复制
  ├── [新增-OA] kbs/               ← 从 oa-ui/src/api/kbs/ 复制
  ├── [新增-OA] qixing/            ← 从 oa-ui/src/api/qixing/ 复制
  ├── [新增-OA] file/              ← 从 oa-ui/src/api/file/ 复制
  ├── [新增-OA] schedule/          ← 从 oa-ui/src/api/schedule/ 复制
  ├── [新增-OA] serial/            ← 从 oa-ui/src/api/serial/ 复制
  ├── [新增-OA] setting/           ← 从 oa-ui/src/api/setting/ 复制
  ├── [新增-OA] information/       ← 从 oa-ui/src/api/information/ 复制
  ├── [新增-OA] mq/                ← 从 oa-ui/src/api/mq/ 复制
  └── [新增-S]  demo/              ← 从 RuoYi-Vue-S 前端 api/demo/ 复制
```

**步骤 4.3 — 合并 src/components/**

OA 前端有大量自定义组件需要合并（注意：与基座同名组件不要覆盖）：

| 组件 | 来源 | 说明 |
|------|------|------|
| `Process/` | OA | BPMN 流程设计器（**核心组件**） |
| `parser/` | OA | 动态表单解析器 |
| `render/` | OA | 动态表单渲染器 |
| `vform/` | OA | VFormDesigner 可视化表单 |
| `customBpmn/` | OA | 自定义 BPMN 扩展 |
| `form/` | OA | 表单用户/部门选择器 |
| `org/` | OA | 组织用户选择器 |
| `flow/` | OA | 流程角色/表达式组件 |
| `WangEditorExtend/` | OA | 富文本扩展 |
| `SerialNo/` | OA | 流水号组件 |
| `Crontab/` | OA | Cron 表达式（基座已有，**不覆盖**） |

**步骤 4.4 — 合并路由配置 `router/index.js`**

在基座路由文件中追加 OA 和项目1 的路由：

```javascript
// 基座原有路由 ...

// === OA 新增路由 ===
import workflow from './modules/workflow'     // 新建文件
import kbs from './modules/kbs'               // 新建文件
import qixing from './modules/qixing'         // 新建文件
// ...

// === 项目1 新增路由 ===
import demo from './modules/demo'             // 新建文件
```

**步骤 4.5 — 合并前端依赖 package.json**

OA 前端新增的依赖（与基座已有不重复部分）：

```json
{
  "dependencies": {
    "bpmn-js": "^11.1.0",
    "bpmn-js-bpmnlint": "^0.22.3",
    "diagram-js": "^11.4.1",
    "bpmnlint": "^10.3.0",
    "@wangeditor/editor": "^5.1.23",
    "@wangeditor/editor-for-vue": "^1.0.2",
    "mavon-editor": "^2.10.1",
    "vue-esign": "^1.1.4",
    "vue-plugin-hiprint": "0.0.56",
    "vue-simple-uploader": "^0.7.6",
    "splitpanes": "2.4.1",
    "xcrud": "^0.4.19",
    "video.js": "^8.17.4",
    "pdfjs-dist": "^2.6.347",
    "vue-pdf": "^4.2.0",
    "vue-codemirror": "^4.0.6",
    "localforage": "^1.10.0",
    "spark-md5": "^3.0.2"
  },
  "devDependencies": {
    "create-bpmnlint-plugin": "^0.6.0"
  }
}
```

项目1 新增依赖（检查与基座是否重复后合并）。

**步骤 4.6 — 合并 store (Vuex)**

OA 新增的 store：
- `modules/uiStore.js`
- `modules/wangEditorStore.js`

**步骤 4.7 — 合并 utils/**

OA 新增：
- `utils/socket/` — WebSocket 通讯
- `utils/base64.js`
- `utils/StrUtil.js`
- `utils/errorCode.js`
- `utils/fileMap.js`

#### 4.7 移动端处理

`ruoyi-vue-oa-mobile/` 是 UniApp 项目，**独立部署**，不需要合并到基座前端。

- 将其复制到基座同级目录或 `ruoyi-mobile/`
- 修改 `manifest.json` 和 `config/environment.js` 中的 API 地址指向基座后端
- 移动端 API 请求路径与 PC 端共用同一后端（Controller 相同）

---

### 阶段五：数据库与配置整合（1 天）

#### 4.8 SQL 脚本整合

```
sql/
├── ry_20260417.sql              ← [保留] 基座现有
├── quartz.sql                   ← [保留] 基座现有
├── ry_oa_business.sql           ← [新增] 从 OA table.sql 抽取的业务表
├── ry_oa_data.sql               ← [新增] 从 OA data.sql 抽取的初始化数据
├── ry_s_tables.sql              ← [新增] 从项目1 ruoyi_all.sql 抽取的独有表
└── ry_s_data.sql                ← [新增] 从项目1 ruoyi_all.sql 抽取的初始化数据
```

**重要**：初始化脚本要处理表冲突：
- `sys_menu`、`sys_dict_data` 等若依标准表，需要 **追加** 模式，不是覆盖
- 新增菜单时注意 menu_id 不冲突

#### 4.9 配置文件整合

`ruoyi-admin/src/main/resources/application.yml` 追加（不覆盖已有配置）：

```yaml
# === OA 业务配置 ===
flowable:
  database-schema-update: true
  async-executor-activate: false

# === 项目1 业务配置 ===
tenant:
  enable: true
oss:
  type: local
  local:
    path: D:/ruoyi/uploadPath/oss
```

---

### 阶段六：根 POM 整合（0.5 天）

#### 4.10 修改根 pom.xml

只需在 `<modules>` 中添加新模块声明：

```xml
<modules>
    <!-- 若依核心 (原有) -->
    <module>ruoyi-admin</module>
    <module>ruoyi-framework</module>
    <module>ruoyi-system</module>
    <module>ruoyi-quartz</module>
    <module>ruoyi-generator</module>
    <module>ruoyi-common</module>

    <!-- 通用扩展 (新增) -->
    <module>ruoyi-common-ext</module>

    <!-- OA 业务模块组 (新增) -->
    <module>ruoyi-parent-flow</module>
    <module>ruoyi-parent-msg</module>
    <module>ruoyi-parent-biz</module>
    <module>ruoyi-parent-common</module>
    <module>ruoyi-qixing</module>

    <!-- 项目1 业务模块 (新增) -->
    <module>ruoyi-tenant</module>
    <module>ruoyi-data-rule</module>
    <module>ruoyi-oss</module>
    <module>ruoyi-biz</module>
</modules>
```

在 `<dependencyManagement>` 中添加新模块的版本管理。

---

## 五、自动化脚本清单

所有脚本存放在 `script/` 目录下：

| 脚本 | 用途 | 语言 |
|------|------|------|
| `script/python/javax_to_jakarta.py` | javax → jakarta 批量替换 | Python |
| `script/python/springfox_to_springdoc.py` | Swagger 注解迁移 | Python |
| `script/python/fix_pom_parent.py` | 修改所有子模块 parent 指向 | Python |
| `script/sql/extract_oa_tables.py` | 从 OA table.sql 中抽取业务表 | Python |
| `script/sql/extract_oa_data.py` | 从 OA data.sql 中抽取初始化数据 | Python |
| `script/sql/generate_merged_menu.sql` | 生成合并后的菜单 SQL | Python |
| `script/python/check_conflicts.py` | 检查同名类/配置文件冲突 | Python |

---

## 六、风险点与注意事项

### 6.1 高风险项

| 风险 | 影响 | 缓解措施 |
|------|------|---------|
| Flowable 6 → 7 API 不兼容 | 工作流模块不可用 | 先在测试分支验证，准备回退方案 |
| MyBatis 与 MyBatis-Plus 并存 | 配置冲突、事务问题 | 统一使用 MP 的 SqlSessionFactory，兼容原生 MyBatis |
| 前端组件同名冲突 | 样式/功能异常 | 人工 diff，保留两方功能 |
| 数据库表名冲突 | 建表失败 | 全量对比表名，统一命名规范 |
| 菜单 ID 冲突 | 权限混乱 | 预留 ID 段分配方案 |

### 6.2 ID 段分配建议

| 模块 | menu_id 范围 | 说明 |
|------|-------------|------|
| 基座原有 | 1 - 1999 | 若依标准菜单 |
| OA 流程 | 2000 - 2999 | 工作流相关 |
| OA 知识库 | 3000 - 3999 | KBS |
| OA 日程 | 4000 - 4999 | Schedule |
| OA 齐兴业务 | 5000 - 7999 | 项目/OA/HR |
| 项目1 扩展 | 8000 - 8499 | 租户/数据权限/OSS |
| 项目1 Demo | 8500 - 8999 | Demo 示例 |
| 预留未来 | 9000+ | 后续模块 |

### 6.3 注意事项

1. **不要修改基座原有文件**：`ruoyi-common`、`ruoyi-framework`、`ruoyi-system` 保持原样
2. **ruoyi-admin 只加依赖，不写业务代码**：Controller 都在各自的业务模块中
3. **每个业务模块需要自包含**：有自己的 `@AutoConfiguration` 注册
4. **前端合并后先 `npm install`，再逐个解决冲突**
5. **SQL 脚本要支持幂等执行**：使用 `CREATE TABLE IF NOT EXISTS` 和 `INSERT IGNORE`

---

## 七、验证清单

迁移完成后逐项验证：

- [ ] `mvn clean compile` 全部模块编译通过
- [ ] `mvn clean install -DskipTests` 构建成功
- [ ] 基座启动无报错（`mvn spring-boot:run -pl ruoyi-admin`）
- [ ] OA 工作流模块：流程定义上传、发起审批、审批流转正常
- [ ] OA 知识库模块：文档增删改查正常
- [ ] OA 齐兴业务：项目管理全流程正常
- [ ] 项目1 多租户：租户隔离正常
- [ ] 项目1 数据权限：角色数据权限生效
- [ ] 项目1 OSS：文件上传/下载正常
- [ ] 前端打包 `npm run build:prod` 无报错
- [ ] 前端页面正常访问，菜单显示正确
- [ ] 移动端 UniApp H5 模式正常访问
- [ ] Swagger UI (`/swagger-ui.html`) 显示所有 API
- [ ] 用户登录、权限校验正常

---

## 八、时间估算

| 阶段 | 工作内容 | 预估时间 |
|------|---------|---------|
| 阶段一 | 创建 ruoyi-common-ext、编写自动化脚本 | 0.5 天 |
| 阶段二 | OA 后端 20+ 模块迁移 (javax→jakarta, swagger, flowable升级) | 5-7 天 |
| 阶段三 | 项目1 后端 4 模块迁移 (MP升级, 租户适配) | 3-4 天 |
| 阶段四 | 前端聚合 (合并页面/组件/API/路由/依赖) | 3-4 天 |
| 阶段五 | 数据库与配置整合 | 1 天 |
| 阶段六 | 根 pom 整合 | 0.5 天 |
| 联调测试 | 全流程功能验证 | 3-5 天 |
| **合计** | | **16-22 天** |

---

## 九、后续模块接入规范

完成本次迁移后，后续新模块接入遵循以下模板：

```
1. 创建模块目录 ruoyi-{module}/
2. 编写独立 pom.xml（父 POM 指向根 ruoyi）
3. 依赖 ruoyi-common（和 ruoyi-common-ext 如需要）
4. 创建自动配置类 @AutoConfiguration + @ComponentScan
5. 在 META-INF/spring/...AutoConfiguration.imports 中注册
6. 在根 pom.xml <modules> 中添加
7. 在 ruoyi-admin/pom.xml 中添加依赖
8. 前端页面并入 ruoyi-ui/src/views/{module}/
9. API 并入 ruoyi-ui/src/api/{module}/
10. SQL 脚本追加到 sql/ 目录
```

---

> **文档版本**: v1.0  
> **下一步**: 评审方案后，按阶段执行迁移
