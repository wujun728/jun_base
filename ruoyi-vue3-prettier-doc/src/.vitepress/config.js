import { defineConfig } from 'vitepress'
import {
  componentPreview,
  containerPreview,
} from '@vitepress-demo-preview/plugin'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import {
  groupIconMdPlugin,
  groupIconVitePlugin,
} from 'vitepress-plugin-group-icons'

import path from 'path'
export default defineConfig({
  title: 'RuoYi-Vue3-Prettier',
  description: '基于 Vue3、Vite、JavaScript、Element-Plus 的现代化后台管理系统',
  cleanUrls: true,
  themeConfig: {
    logo: '/logo.png',
    search: {
      provider: 'local',
      options: {
        translations: {
          button: {
            buttonText: '搜索文档',
            buttonAriaLabel: '搜索文档',
          },
          modal: {
            noResultsText: '无法找到相关结果',
            resetButtonTitle: '清除查询条件',
            footer: {
              selectText: '选择',
              navigateText: '切换',
            },
          },
        },
      },
    },
    nav: [
      { text: '首页', link: '/' },
      { text: '指南', link: '/guide/' },
    ],
    sidebar: [
      {
        text: '指南',
        collapsed: true,
        items: [
          { text: '项目介绍', link: '/guide/' },
          { text: '快速开始', link: '/guide/getting-started' },
        ],
      },
      {
        text: '方法',
        collapsed: true,
        items: [
          { text: '$tab', link: '/methods/$tab' },
          { text: '$modal', link: '/methods/$modal' },
          { text: '$auth', link: '/methods/$auth' },
          { text: '$download', link: '/methods/$download' },
          { text: '网络请求', link: '/methods/request' },
          { text: '自定义指令', link: '/methods/directive' },
          { text: '项目通用方法', link: '/methods/commonMethod' },
        ],
      },
      {
        text: '组件',
        collapsed: true,
        items: [
          { text: 'SvgIcon组件', link: '/components/icon' },
          { text: '表单组件', link: '/components/form' },
          { text: '表格组件', link: '/components/table' },
        ],
      },
    ],

    socialLinks: [
      {
        icon: 'gitee',
        link: 'https://gitee.com/codelm/ruoyi-vue3-lmw',
      },
    ],
    footer: {
      message: '基于 MIT 许可发布',
      copyright: 'Copyright © 2024-present RuoYi-Vue3-Prettier',
    },
  },
  markdown: {
    config: (md) => {
      md.use(componentPreview)
      md.use(containerPreview)
      md.use(groupIconMdPlugin)
    },
    lineNumbers: true,
  },
  vite: {
    plugins: [
      createSvgIconsPlugin({
        iconDirs: [
          path.resolve(process.cwd(), 'src/.vitepress/theme/assets/icon'),
        ],
        symbolId: 'icon-[name]',
      }),
      groupIconVitePlugin(),
    ],
    server: {
      host: '0.0.0.0',
      port: 8080,
      open: true,
      cors: true,
    },
    css: {
      preprocessorOptions: {
        scss: {
          api: 'modern-compiler',
        },
      },
    },
  },
})
