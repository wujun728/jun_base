[![license](https://img.shields.io/github/license/anncwb/vue-vben-admin.svg)](LICENSE)<h1>RuoYi Vben</h1>

## 提示

关于运行警告: `The CJS build of Vite's Node API is deprecated. See https://vitejs.dev/guide/troubleshooting.html#vite-cjs-node-api-deprecated for more details.` 是由于升级vite5 官方还未解决但是不影响使用 详见[vben issue](https://github.com/vbenjs/vue-vben-admin/pull/3508)

## 简介

该项目是本人另一个项目 [ruoyi plus vben](https://gitee.com/dapppp/ruoyi-plus-vben) 提取出来&适配

基于 [vben(ant-design-vue)](https://github.com/vbenjs/vue-vben-admin) 的 RuoYi-Vue 前端项目

| 组件/框架      | 版本   |
| :------------- | :----- |
| vben           | 2.11.4 |
| ant-design-vue | 4.2.1  |
| vue            | 3.4.25 |

对应后端项目: [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 微服务版本未做测试

## 预览

[预览地址](https://117.72.10.31)

## 文档

[vben 文档地址](https://doc.vvbin.cn/)

## 预览图

![图片](https://gitee.com/dapppp/RuoYi-Vben/raw/main/preview/image.png) ![图片](https://gitee.com/dapppp/RuoYi-Vben/raw/main/preview/image-1.png) ![图片](https://gitee.com/dapppp/RuoYi-Vben/raw/main/preview/image-2.png) ![图片](https://gitee.com/dapppp/RuoYi-Vben/raw/main/preview/image-3.png) ![图片](https://gitee.com/dapppp/RuoYi-Vben/raw/main/preview/image-4.png) ![图片](https://gitee.com/dapppp/RuoYi-Vben/raw/main/preview/image-5.png) ![图片](https://gitee.com/dapppp/RuoYi-Vben/raw/main/preview/image-6.png)

## 安装使用

前置准备环境(只能用pnpm)

```json
"packageManager": "pnpm@9.0.4",
"engines": {
  "node": ">=18.12.0",
  "pnpm": ">=9.0.4"
}
```

- 获取项目代码

```bash
git clone https://gitee.com/dapppp/RuoYi-Vben.git
```

- 安装依赖

```bash
cd RuoYi-Vben

pnpm install
```

- 菜单图标替换

sql文件`src\views\system\menu\icon_sql\update_icon.sql`

- 关于代码生成(非必选)

**系统工具-代码生成 表格右上角有详细操作怎么改后端!**

**系统工具-代码生成 表格右上角有详细操作怎么改后端!**

**系统工具-代码生成 表格右上角有详细操作怎么改后端!**

- 运行

```bash
pnpm serve
```

- 打包

```bash
pnpm build
```

## Git 贡献提交规范

- 参考 [vue](https://github.com/vuejs/vue/blob/dev/.github/COMMIT_CONVENTION.md) 规范 ([Angular](https://github.com/conventional-changelog/conventional-changelog/tree/master/packages/conventional-changelog-angular))

  - `feat` 增加新功能
  - `fix` 修复问题/BUG
  - `style` 代码风格相关无影响运行结果的
  - `perf` 优化/性能提升
  - `refactor` 重构
  - `revert` 撤销修改
  - `test` 测试相关
  - `docs` 文档/注释
  - `chore` 依赖更新/脚手架配置修改等
  - `workflow` 工作流改进
  - `ci` 持续集成
  - `types` 类型定义文件更改
  - `wip` 开发中

## 浏览器支持

本地开发推荐使用`Chrome 100+` 浏览器

支持现代浏览器, 不支持 IE

| [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>IE | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt=" Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Edge | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Firefox | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Chrome | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)</br>Safari |
| :-: | :-: | :-: | :-: | :-: |
| not support | last 2 versions | last 2 versions | last 2 versions | last 2 versions |

## 捐赠

如果项目帮助到您 可以考虑请作者喝杯咖啡 万分感谢您对开源的支持!

<img src=https://117.72.10.31/minio-server/plus/2024/03/16/98a9d704eb0c4c04b721bf7799217571.jpg height=360px />
