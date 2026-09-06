<p align="center">
	<img alt="logo" src="https://oscimg.oschina.net/oscnet/up-d3d0a9303e11d522a06cd263f3079027715.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">RuoYi SpringBoot3 Pro v3.9.1</h1>
<h4 align="center">基于 SpringBoot 3 + MyBatis-Plus 的企业级快速开发框架</h4>

## 📑 目录导航

- [平台简介](#平台简介)
- [演示项目](#演示项目)
- [前端项目](#前端项目)
- [UniApp 移动端](#uniapp-移动端)
- [开发教程](#开发教程)
- [核心新特性](#-核心新特性)
- [技术栈](#技术栈)
- [内置功能](#内置功能)
- [快速开始](#快速开始)
- [项目结构](#项目结构)
- [与原版对比](#与原版对比)
- [部署说明](#部署说明)
- [请我喝杯咖啡](#请我喝杯咖啡)
- [参与贡献](#-参与贡献)
- [开源协议](#-开源协议)
- [鸣谢](#-鸣谢)
- [联系方式](#-联系方式)
- [友情链接](#友情链接)

## 平台简介

RuoYi SpringBoot3 Pro 是在 **RuoYi-Vue** 基础上的企业级增强版本，保留了原有的所有功能，并新增了多项企业级特性，全部开源免费。

本项目采用最新技术栈,提供 **代码加密保护**、**三级等保支持**、**AI 对话能力**、**更强大的 ORM 支持**、**多数据库适配**、**低代码开发能力**等企业级功能,适合中大型项目和政企系统快速开发。

| 公众号                                       | 微信交流群                                                      | QQ群                                          |
| -------------------------------------------- | --------------------------------------------------------------- | --------------------------------------------- |
| ![公众号](https://cdn.undsky.com/img/gh.jpg) | ![微信交流群](https://cdn.undsky.com/img/doudouqun.jpg?v=2.3.0) | ![QQ群](https://cdn.undsky.com/img/qqqun.jpg) |

## 演示项目

- 🖥️ Element Plus 版本：http://ry.undsky.com/admin/login?redirect=/index
- 🎨 Vue3 Prettier 版本：http://ry2.undsky.com/login?redirect=/index
- 🐻 BearJia Vue3 版本：http://ry1.undsky.com/login3

## 前端项目

- 🖥️ Element Plus 版本：https://github.com/undsky/RuoYi-SpringBoot3-ElementPlus
- 🎨 Vue3 Prettier 版本：https://github.com/undsky/ruoyi-vue3-prettier
  （ fork from https://gitee.com/codelm/ruoyi-vue3-lmw ）
- 🐻 BearJia Vue3 版本：https://github.com/undsky/BearJia-Vue3/blob/master/docs/ruoyi-usage.md
  （ fork from https://gitee.com/javaxiaobear_admin/bear-jia-vue3 ）

## UniApp 移动端

- 📱 [RuoYi-SpringBoot3-UniApp](https://github.com/undsky/RuoYi-SpringBoot3-UniApp)

## 开发教程

- [【RuoYi-SpringBoot3-Pro】](https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzAwOTExOTc1MQ==&action=getalbum&album_id=4260128209435361290&scene=126&sessionid=1769410963148#wechat_redirect)

## ✨ 核心新特性

### 1. 🛡️ ClassFinal 代码加密

集成 ClassFinal Maven 插件，保护核心业务代码不被反编译：

- **字节码加密**:对编译后的 class 文件进行深度加密
- **防反编译**:即使被反编译也无法查看源代码
- **选择性加密**:可指定需要加密的包（如 `com.ruoyi.biz`）
- **配置文件保护**:支持对 yml、properties 等配置文件加密
- **排除机制**:可排除第三方库（如 `org.spring`）
- **密码保护**:使用密码保护加密的代码，防止未授权运行
- **Maven 集成**:打包时自动加密，无需额外操作
- **企业级方案**:适合商业项目代码保护

### 2. 🔒 三级等保支持

满足国家信息安全等级保护三级要求，内置完善的安全策略：

- **密码更新周期**:可配置密码有效期（0-365 天），超期强制修改
- **登录失败锁定**:支持 N 次失败锁定 M 分钟策略（如 5-30 表示 5 次失败锁定 30 分钟）
- **初始密码强制修改**:首次登录强制修改初始密码
- **密码过期提醒**:登录时自动检测密码是否过期，提示用户更新
- **IP 黑名单**:支持 IP 黑名单配置，支持通配符和网段匹配
- **失败次数跟踪**:数据库记录登录失败次数，自动锁定/解锁
- **可配置化管理**:所有安全策略通过系统配置表动态管理，无需重启
- **Redis 缓存支持**:失败次数缓存到 Redis，提升性能

### 3. 🤖 AI 能力集成

内置 OpenAI 工具类，快速集成 AI 对话能力：

- **OpenAI SDK 集成**:基于官方 OpenAI Java SDK 封装
- **同步对话**:支持标准的请求-响应模式
- **流式对话**:支持 SSE 流式输出，实时展示生成内容
- **多角色支持**:支持 User、System、Assistant 三种角色
- **代理支持**:支持 HTTP/SOCKS 代理配置
- **自定义 API**:支持配置自定义 API 地址（兼容 OpenAI 协议的 API）
- **开箱即用**:提供完整的工具类和示例代码

### 4. 🚀 MyBatis-Plus 集成

- 替换原有 MyBatis，提供更强大的 ORM 功能
- **分页插件**:自动识别数据库类型，无需手动配置
- **乐观锁插件**:防止并发修改数据丢失
- **防全表更新删除插件**:避免误操作造成数据丢失
- **多租户插件**:企业级 SaaS 应用必备能力
- Lambda 查询语法，更优雅的代码风格

### 5. 🎨 Magic API 低代码开发

- **可视化接口开发**:通过 Web 界面快速开发 REST API
- **无需编译**:接口修改即时生效，大幅提升开发效率
- **数据库持久化**:接口脚本存储在数据库，支持版本控制
- **Redis 缓存支持**:内置 Redis 插件，轻松实现接口缓存
- **历史记录**:支持接口变更历史记录和回滚
- 访问地址：`http://localhost:8087/magic/web`（默认账号：jyx / jyx_692483）

### 6. 🗄️ 多数据库支持

支持国内外主流数据库，满足不同场景需求：

| 数据库         | 支持版本 | 配置文件              | 初始化脚本       |
| -------------- | -------- | --------------------- | ---------------- |
| **MySQL**      | 5.7+     | application-devmy.yml | ruoyi-mysql.sql  |
| **PostgreSQL** | 12+      | application-devpg.yml | ruoyi-pgsql.sql  |
| **达梦数据库** | DM8+     | application-devdm.yml | ruoyi-dm8.dmp    |
| **瀚高数据库** | 6.2+     | application-devhg.yml | ruoyi-highgo.sql |
| **高斯数据库** | GaussDB  | application-devgs.yml | ruoyi-gauss.sql  |

> 💡 切换数据库只需修改 `application.yml` 中的 `spring.profiles.active` 配置

### 7. 🏢 多租户支持

- **基于字段隔离**:通过 `tenant_id` 字段自动隔离租户数据
- **自动过滤**:SQL 自动注入租户条件，无需手动处理
- **灵活配置**:支持配置忽略表和忽略用户
- **透明化使用**:业务代码无感知，框架自动处理
- 配置开关：`tenant.enable: true/false`

### 8. 📦 ruoyi-biz 业务模块

新增独立业务模块，提供标准化开发示例：

- **模块化设计**:业务代码与系统代码分离
- **标准化结构**:Controller → Service → Mapper 标准分层
- **Excel 导入导出**:内置自定义 Excel 处理器示例
- **定时任务示例**:提供定时任务开发模板
- 包含地区管理等完整功能示例

### 9. 📊 Dify 数据库建表工作流

提供 Dify 工作流配置文件，通过 AI 快速生成多数据库建表语句：

- **多数据库支持**：支持 MySQL、PostgreSQL/瀚高、openGauss、SQLite 四种数据库
- **智能字段命名**：支持拼音或英文两种字段命名规范
- **标准化表结构**：自动生成包含租户 ID、用户 ID、部门 ID、状态、创建/更新信息等标准字段
- **索引自动创建**：自动为常用字段创建索引（tenant_id、user_id、dept_id）
- **简洁输入格式**：只需输入"表名：字段（类型）"即可生成完整建表语句
- **开箱即用**：导入 `sql/Dify_数据库建表.yml` 到 Dify 即可使用

### 10. 🎯 代码生成模板优化

针对 MyBatis-Plus 优化代码生成模板：

- **MyBatis-Plus 适配**:生成的 Mapper 继承 BaseMapper，自动拥有 CRUD 方法
- **Lambda 查询支持**:生成的代码支持 Lambda 表达式查询
- **多数据库兼容**:生成的 SQL 和实体类适配多种数据库
- **注解增强**:自动添加 `@TableName`、`@TableId` 等 MyBatis-Plus 注解
- **分页优化**:使用 MyBatis-Plus Page 对象，无需手动配置
- **代码更精简**:减少样板代码，提高开发效率
- **低代码结合**:可配合 Magic API 快速开发，双管齐下

## 技术栈

### 后端技术

| 技术            | 版本   | 说明               |
| --------------- | ------ | ------------------ |
| Spring Boot     | 3.5.8  | 容器 + MVC 框架    |
| Spring Security | 6.x    | 认证和授权框架     |
| MyBatis-Plus    | 3.5+   | ORM 框架（增强版） |
| OpenAI Java SDK | Latest | AI 对话能力        |
| Magic API       | 2.2.2  | 低代码快速开发平台 |
| Redis           | -      | 分布式缓存（可选） |
| JWT             | 0.9.1  | JWT 令牌           |
| Druid           | 1.2.27 | 数据库连接池       |
| Springdoc       | 2.8.14 | API 文档           |
| Quartz          | 2.5.2  | 定时任务           |
| Velocity        | 2.3    | 代码生成模板       |

### 数据库支持

- MySQL 5.7+
- PostgreSQL 12+
- 达梦数据库 DM8
- 瀚高数据库 6.2+
- 高斯数据库 GaussDB

## 内置功能

1.  ✅ 用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  ✅ 部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  ✅ 岗位管理：配置系统用户所属担任职务。
4.  ✅ 菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  ✅ 角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  ✅ 字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  ✅ 参数管理：对系统动态配置常用参数。
8.  ✅ 通知公告：系统通知公告信息发布维护。
9.  ✅ 操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. ✅ 登录日志：系统登录日志记录查询包含登录异常。
11. ✅ 在线用户：当前系统中活跃用户状态监控。
12. ✅ 定时任务：在线（添加、修改、删除）任务调度包含执行结果日志。
13. ✅ 代码生成：前后端代码的生成（java、html、xml、sql）支持 CRUD 下载。
14. ✅ 系统接口：根据业务代码自动生成相关的 API 接口文档。
15. ✅ 服务监控：监视当前系统 CPU、内存、磁盘、堆栈等相关信息。
16. ✅ 缓存监控：对系统的缓存信息查询，命令统计等。
17. ✅ 连接池监视：监视当前系统数据库连接池状态，可进行分析 SQL 找出系统性能瓶颈。
18. ✨ **ClassFinal 代码加密**：字节码加密保护，防止核心业务代码被反编译（新增）。
19. ✨ **三级等保支持**：密码更新周期、登录失败锁定、IP 黑名单等安全策略（新增）。
20. ✨ **AI 对话能力**：内置 OpenAI 工具类，支持同步和流式对话（新增）。
21. ✨ **代码生成优化**：针对 MyBatis-Plus 优化的代码生成模板（优化）。
22. ✨ **地区管理**：省市区三级联动数据管理（新增）。
23. ✨ **Magic API**：可视化接口开发平台（新增）。
24. ✨ **多租户管理**：SaaS 多租户数据隔离（新增）。
25. ✨ **Dify 数据库建表**：AI 驱动的多数据库建表语句生成工作流（新增）。

## 快速开始

### 环境要求

- JDK 17 或 JDK 21
- Maven 3.6+
- 选择以下数据库之一：
  - MySQL 5.7+
  - PostgreSQL 12+
  - 达梦数据库 DM8
  - 瀚高数据库 6.2+
  - 高斯数据库 GaussDB
- Redis

### 安装步骤

1. **导入数据库脚本**

```bash
# MySQL 示例
mysql -u root -p < sql/ruoyi-mysql.sql
mysql -u root -p < sql/magic-api-mysql.sql
mysql -u root -p < sql/region-mysql.sql

# PostgreSQL 示例
psql -U postgres -d ruoyi < sql/ruoyi-pgsql.sql
psql -U postgres -d ruoyi < sql/magic-api-pgsql.sql
psql -U postgres -d ruoyi < sql/region-pgsql.sql
```

2. **修改配置文件**

编辑 `ruoyi-admin/src/main/resources/application.yml`：

```yaml
spring:
  profiles:
    active: devmy # 选择对应的数据库配置文件
```

然后编辑对应的配置文件（如 `application-devmy.yml`），修改数据库连接信息。

3. **编译打包**

```bash
# 使用 Maven 编译
mvn clean package

# 或使用提供的脚本（Windows）
bin\package.bat
```

4. **启动项目**

```bash
# 方式一：直接运行 jar
java -jar ruoyi-admin/target/ruoyi-admin.jar

# 方式二：使用 Maven 运行
mvn spring-boot:run -pl ruoyi-admin

# 方式三：使用提供的脚本（Windows）
bin\run.bat
```

5. **访问系统**

- 系统地址：http://localhost:8087
- 接口文档：http://localhost:8087/doc.html
- Magic API：http://localhost:8087/magic/web
- 默认账号：admin / jyx_692483

6. **启动前端**

**Element Plus 版本（推荐企业级应用）：**

```bash
# 克隆项目
git clone https://github.com/undsky/RuoYi-SpringBoot3-ElementPlus.git
cd RuoYi-SpringBoot3-ElementPlus

# 安装依赖
npm install

# 启动项目
npm run dev

# 访问地址：http://localhost:80
```

### ClassFinal 代码加密使用

Pro 版本集成了 ClassFinal 代码加密插件，保护核心业务代码：

**加密配置（ruoyi-admin/pom.xml）：**

```xml
<plugin>
    <groupId>com.gitee.lcm742320521</groupId>
    <artifactId>classfinal-maven-plugin</artifactId>
    <version>1.4.1</version>
    <configuration>
        <!-- 需要加密的包，多个用逗号分隔 -->
        <packages>com.ruoyi.biz</packages>

        <!-- 需要加密的配置文件 -->
        <cfgfiles>*.yml</cfgfiles>

        <!-- 排除的包，不进行加密 -->
        <excludes>org.spring</excludes>

        <!-- 加密密码，运行时需要此密码 -->
        <password>RuoyiSpringBoot3@123456!</password>
    </configuration>
</plugin>
```

**使用步骤：**

1. **打包加密**

```bash
# 执行 Maven 打包，自动触发加密
mvn clean package

# 加密后的 jar 文件在 target 目录
```

2. **运行加密的 jar**

```bash
# 需要提供密码参数才能运行
java -jar RuoyiSpringBoot3.jar -pwd=RuoyiSpringBoot3@123456!
```

**注意事项：**

- ⚠️ 加密后的代码无法反编译，请妥善保管源代码
- ⚠️ 密码一定要保存好，丢失无法恢复
- ✅ 建议只加密核心业务模块（如 `com.ruoyi.biz`）
- ✅ 不要加密第三方库，会导致启动失败
- ✅ 生产环境使用更复杂的密码

**适用场景：**

- 商业项目代码保护
- 交付给客户的项目
- 防止核心算法泄露
- 知识产权保护

### 三级等保安全配置

Pro 版本内置完善的等保三级安全策略，通过系统参数配置即可启用：

**1. 密码更新周期配置**

在系统管理 → 参数设置中配置 `sys.account.passwordValidateDays`：

```
参数名称：用户管理-账号密码更新周期
参数键名：sys.account.passwordValidateDays
参数键值：90  （90天强制修改密码，0表示不限制）
```

配置后效果：

- 用户密码超过 90 天未更新，登录时会强制提示修改密码
- 系统自动检测 `pwd_update_date` 字段判断密码是否过期

**2. 登录失败锁定策略**

配置 `sys.account.tryLoginCount` 参数：

```
参数名称：用户管理-账号密码尝试登录次数
参数键名：sys.account.tryLoginCount
参数键值：5-30  （5次失败后锁定30分钟，格式：次数-锁定时长）
```

配置后效果：

- 连续 5 次登录失败后，账号自动锁定 30 分钟
- 锁定期间无法登录，提示"连续 5 次登录失败，请 30 分钟后再试"
- 锁定时间到期或管理员手动解锁后恢复

**3. 初始密码强制修改**

配置 `sys.account.initPasswordModify` 参数：

```
参数名称：用户管理-初始密码修改策略
参数键名：sys.account.initPasswordModify
参数键值：1  （1表示启用，0表示关闭）
```

配置后效果：

- 新用户首次登录时，强制修改初始密码
- 直到修改密码后才能正常使用系统

**4. IP 黑名单配置**

配置 `sys.login.blackIPList` 参数：

```
参数名称：用户登录-黑名单列表
参数键名：sys.login.blackIPList
参数键值：192.168.1.*;10.0.0.0/8  （多个用分号分隔，支持通配符和网段）
```

配置后效果：

- 黑名单中的 IP 无法登录系统
- 支持通配符匹配（如 192.168.1.\*）
- 支持网段匹配（如 10.0.0.0/8）

**数据库支持**

系统会自动跟踪：

- `sys_user.try_count`：记录用户登录失败次数
- `sys_user.pwd_update_date`：记录密码最后更新时间

### AI 对话功能使用

Pro 版本内置了 OpenAI 工具类，可快速集成 AI 对话能力：

**同步对话示例：**

```java
import com.ruoyi.common.utils.ai.*;
import com.openai.client.OpenAIClient;
import java.util.Arrays;

// 1. 创建客户端
OpenAIClient client = OpenAI.chatClient(
    "your-api-key",
    "https://api.openai.com/v1",
    null  // 如需代理，传入 Proxy 对象
);

// 2. 构建消息列表
List<AIMessage> messages = Arrays.asList(
    new AIMessage(AIRole.SYSTEM, "你是一个有帮助的助手"),
    new AIMessage(AIRole.USER, "介绍一下 Spring Boot")
);

// 3. 创建对话参数
ChatCompletionCreateParams params = OpenAI.chatParams("gpt-3.5-turbo", messages);

// 4. 发送请求并获取响应
String response = OpenAI.chat(client, params);
System.out.println(response);
```

**流式对话示例（SSE）：**

```java
// Controller 中使用
@PostMapping("/chat/stream")
public ResponseEntity<StreamingResponseBody> chatStream(@RequestBody ChatRequest request) {
    OpenAIClient client = OpenAI.chatClient("your-api-key", "https://api.openai.com/v1", null);

    List<AIMessage> messages = Arrays.asList(
        new AIMessage(AIRole.USER, request.getMessage())
    );

    ChatCompletionCreateParams params = OpenAI.chatParams("gpt-3.5-turbo", messages);

    return OpenAI.chatStream(client, params, new OpenAI.StreamContentListener() {
        @Override
        public void onContent(String content) {
            // 每次接收到增量内容时调用
            System.out.print(content);
        }

        @Override
        public void onComplete(String fullContent) {
            // 流式输出完成后调用
            System.out.println("\n完整内容：" + fullContent);
        }
    });
}
```

**支持自定义 API（如国内大模型）：**

```java
// 使用兼容 OpenAI 协议的其他 API
OpenAIClient client = OpenAI.chatClient(
    "your-api-key",
    "https://your-custom-api.com/v1",  // 自定义 API 地址
    null
);
```

### MyBatis-Plus 使用示例

```java
// 1. 实体类继承 BaseEntity
@TableName("sys_user")
public class SysUser extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String userName;
    // ...
}

// 2. Mapper 继承 BaseMapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 无需写任何代码，已自动拥有 CRUD 方法
}

// 3. Service 使用
// Lambda 查询
List<SysUser> users = userMapper.selectList(
    new LambdaQueryWrapper<SysUser>()
        .eq(SysUser::getStatus, "0")
        .like(SysUser::getUserName, "admin")
);

// 分页查询
Page<SysUser> page = new Page<>(1, 10);
userMapper.selectPage(page,
    new LambdaQueryWrapper<SysUser>()
        .orderByDesc(SysUser::getCreateTime)
);
```

### Magic API 使用

1. 访问 Magic API 管理界面：`http://localhost:8087/magic/web`
2. 使用账号登录：jyx / jyx_692483
3. 创建接口分组和接口
4. 编写接口脚本（支持 SQL、JavaScript 等）
5. 测试接口
6. 发布接口，前端即可调用

### 代码生成器使用

Pro 版本的代码生成器已针对 MyBatis-Plus 优化：

1. 访问系统管理 → 代码生成
2. 选择需要生成代码的表
3. 生成的代码特点：
   - Mapper 继承 `BaseMapper<T>`，自带 CRUD 方法
   - 实体类添加 `@TableName`、`@TableId` 等注解
   - Service 层支持 MyBatis-Plus 的 Lambda 查询
   - 自动适配当前使用的数据库类型
   - 支持 MyBatis-Plus 分页对象

生成的 Mapper 示例：

```java
public interface UserMapper extends BaseMapper<User> {
    // 无需编写基础 CRUD，已由 BaseMapper 提供
    // 只需编写复杂的自定义查询
}
```

### 多租户配置

在 `application.yml` 中配置：

```yaml
tenant:
  enable: true # 是否启用多租户
  column: tenant_id # 租户字段名
  ignoreTables: # 忽略的表（不进行租户隔离）
    - sys_user
    - sys_role
  ignoreLoginNames: # 忽略的用户（不进行租户隔离）
    - admin
```

### Dify 数据库建表工作流

项目提供了 Dify 工作流配置文件，可通过 AI 快速生成多数据库建表语句。

**导入工作流：**

1. 登录 Dify 平台
2. 创建新应用，选择"导入 DSL"
3. 上传 `sql/Dify_数据库建表.yml` 文件
4. 配置 LLM 模型（默认使用通义千问 deepseek-v3）

**使用方式：**

1. 选择目标数据库类型（MySQL / PostgreSQL/瀚高 / openGauss / SQLite）
2. 选择字段命名规范（拼音 / 英文）
3. 输入表名和字段信息，格式：`表名：字段1（类型），字段2（类型）`

**输入示例：**

```
订单：订单号（varchar），金额（decimal），下单时间（datetime）
商品：商品名称，价格（decimal），库存（int）
```

**输出示例（MySQL）：**

```sql
DROP TABLE IF EXISTS `biz_dingdan`;
CREATE TABLE `biz_dingdan`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tenant_id` bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
  `dingdanhao` varchar(50) NULL DEFAULT NULL COMMENT '订单号',
  `jine` decimal(20,4) NULL DEFAULT NULL COMMENT '金额',
  `xiadan_shijian` datetime(0) NULL DEFAULT NULL COMMENT '下单时间',
  -- ... 标准字段
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '订单';
```

**支持的数据库：**

| 数据库          | 主键类型       | 特点                 |
| --------------- | -------------- | -------------------- |
| MySQL           | AUTO_INCREMENT | 标准 InnoDB 引擎     |
| PostgreSQL/瀚高 | IDENTITY       | 支持 COMMENT ON 语法 |
| openGauss       | BIGSERIAL      | 华为高斯数据库语法   |
| SQLite          | AUTOINCREMENT  | 轻量级数据库         |

### 切换数据库

1. 修改 `application.yml` 中的 `spring.profiles.active`
2. 选择对应的配置文件：
   - `devmy`：开发环境 MySQL
   - `devpg`：开发环境 PostgreSQL
   - `devdm`：开发环境达梦数据库
   - `devhg`：开发环境瀚高数据库
   - 等等...

## 项目结构

```
RuoYi-SpringBoot3-Pro
├── bin                     # 启动脚本
│   ├── clean.bat          # 清理脚本
│   ├── package.bat        # 打包脚本
│   └── run.bat            # 启动脚本
├── sql                     # SQL脚本
│   ├── ruoyi-mysql.sql    # MySQL 初始化脚本
│   ├── ruoyi-pgsql.sql    # PostgreSQL 初始化脚本
│   ├── ruoyi-dm8.dmp      # 达梦数据库脚本
│   ├── magic-api-*.sql    # Magic API 脚本
│   ├── region-*.sql       # 地区数据脚本
│   └── Dify_数据库建表.yml  # Dify 建表工作流
├── ruoyi-admin             # 管理后台模块
├── ruoyi-framework         # 框架核心模块
├── ruoyi-system            # 系统模块
├── ruoyi-common            # 通用模块
├── ruoyi-biz               # ⭐ 业务模块（新增）
├── ruoyi-quartz            # 定时任务模块
└── ruoyi-generator         # 代码生成模块
```

## 与原版对比

| 特性            | RuoYi-Vue-springboot3 | RuoYi-SpringBoot3-Pro              |
| --------------- | --------------------- | ---------------------------------- |
| Spring Boot     | 3.5.4                 | 3.5.8                              |
| ORM 框架        | MyBatis               | MyBatis-Plus ⭐                    |
| 代码加密保护    | ❌                    | ClassFinal 加密 ⭐                 |
| 三级等保        | 部分支持              | 完整支持 ⭐                        |
| AI 能力         | ❌                    | OpenAI 集成 ⭐                     |
| 低代码开发      | ❌                    | Magic API ⭐                       |
| 多数据库支持    | MySQL                 | MySQL/PostgreSQL/达梦/瀚高/高斯 ⭐ |
| 多租户          | ❌                    | ✅ ⭐                              |
| Redis           | 必须                  | 可选 ⭐                            |
| 业务示例模块    | ❌                    | ruoyi-biz ⭐                       |
| 代码生成模板    | MyBatis               | MyBatis-Plus 适配 ⭐               |
| 文件上传大小    | 10MB/20MB             | 100MB/200MB ⭐                     |
| Dify 建表工作流 | ❌                    | ✅ ⭐                              |

## 部署说明

[部署系统](https://doc.ruoyi.vip/ruoyi-vue/document/hjbs.html#%E9%83%A8%E7%BD%B2%E7%B3%BB%E7%BB%9F)

## 请我喝杯咖啡

如果项目对你有帮助，可以请我喝杯咖啡 ☕️

<img src="https://cdn.undsky.com/img/weixin10.jpg" max-width="300" height="500" /> <img src="https://cdn.undsky.com/img/zhifubao10.jpg" max-width="300" height="500" />

## 🤝 参与贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交改动 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 开源协议

本项目基于 [MIT](LICENSE) 协议开源，可免费用于个人和企业项目。

## 🙏 鸣谢

- [若依（RuoYi）](https://gitee.com/y_project/RuoYi-Vue) - 优秀的开源后台管理系统
- [Spring Boot](https://spring.io/projects/spring-boot) - 企业级 Java 开发框架
- [MyBatis-Plus](https://baomidou.com/) - 优秀的 MyBatis 增强工具
- 所有贡献者

## 📞 联系方式

- 网站：[https://www.undsky.com](https://www.undsky.com)
- GitHub：[https://github.com/undsky/RuoYi-SpringBoot3-Pro](https://github.com/undsky/RuoYi-SpringBoot3-Pro)
- Issues：[https://github.com/undsky/RuoYi-SpringBoot3-Pro/issues](https://github.com/undsky/RuoYi-SpringBoot3-Pro/issues)

---

⭐ 如果觉得项目不错，请点个 Star 支持一下！

## 友情链接

- [LINUX DO - 新的理想型社区](https://linux.do/)
