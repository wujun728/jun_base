# 参与贡献

感谢你关注 Nexa Admin。提交 Issue 或 Pull Request 前，请先确认问题与本仓库的整合目标一致：保留既有后端能力，优先复用 Art 风格组件封装。

## 本地开发

环境要求：

- JDK 17+
- Maven 3.8+
- Node.js 20.19+
- pnpm 8.8+
- MySQL 8+
- Redis

启动后端：

```bash
mvn -pl ruoyi-admin -am spring-boot:run
```

启动前端：

```bash
cd artpro-ui
pnpm install
pnpm dev
```

## 开发约定

- 页面优先复用 Art 现有组件、Hooks 和布局能力。
- CRUD 页面优先使用 `ArtSearchBar`、`ArtTableHeader`、`ArtTable`、`ArtForm` 和抽屉表单。
- 后端优先复用 RuoYi `/system/*`、`/monitor/*`、`/tool/*`、`/common/*` 接口。
- 不重复实现 RuoYi RBAC、JWT、菜单权限和数据权限。
- 新增数据库结构时，同时更新完整初始化 SQL 和幂等增量 SQL。
- 涉及权限的页面和接口必须同时校验前端按钮权限与后端 `@PreAuthorize`。

## 提交前检查

后端：

```bash
mvn -pl ruoyi-admin -am test
```

前端：

```bash
cd artpro-ui
pnpm exec vue-tsc --noEmit
pnpm exec eslint <变更文件>
```

通用检查：

```bash
git diff --check
```

## 提交 Issue

- Bug 请描述复现步骤、预期结果、实际结果、浏览器与运行环境。
- 功能建议请说明使用场景、期望行为和是否愿意提交 PR。
- 安全问题不要提交公开 Issue，请参考 [SECURITY.md](SECURITY.md)。

## 提交 Pull Request

- 每个 PR 聚焦一个问题。
- 描述变更范围、验证方式和 SQL 迁移要求。
- 不要提交 `target`、`node_modules`、日志或 IDE 配置。
- 不要无理由重写 RuoYi 后端业务逻辑或混入传统 RuoYi-Vue 页面风格。
