# jun_base_vben 构建运行文档

## 项目: RuoYi-Vben ✅（无后端）
- 路径: D:\workspace_github_v1\devops\jun_base_vben\RuoYi-Vben
- 类型: RuoYi-Plus-Vben 前端 (Vue3 Vite5 TS UnoCSS vxe-table, pnpm monorepo)
- 状态: ✅ 前端启动成功；❌ 无后端（纯前端模板，默认对接外部 API）

## 环境
- pnpm@8（npm i -g pnpm@8 安装）
- 前端: http://localhost:5175 (vite 直启动)

## 启动注意
- `pnpm dev` 因 [ERR_PNPM_IGNORED_BUILDS] 自动触发 install 失败
- 解决: 直接 `node node_modules/vite/bin/vite.js`（或 pnpm approve-builds 后正常）
- Sass 大量 DEPRECATION WARNING（vxe-table），不影响运行

## 日志
- D:\workspace_github_v1\devops\logs\jun_base_vben_fe.log / install.log
