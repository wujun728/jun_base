import { resolve } from 'node:path';
import uni from '@dcloudio/vite-plugin-uni';
import Unocss from 'unocss/vite';
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { defineConfig } from 'vite';
import checker from 'vite-plugin-checker';

import env from './src/config/env';

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  plugins: [
    AutoImport({
      dts: 'src/auto-imports.d.ts',
      imports: ['vue', 'uni-app', 'pinia'],
      exclude: ['createApp'],
      eslintrc: {
        enabled: true
      }
    }),
    checker({
      eslint: {
        lintCommand: 'eslint "./src/**/*.{ts,tsx,vue,js,jsx}"'
      },
      overlay: false
    }),
    Components({
      extensions: ['vue'],
      dts: 'src/components.d.ts'
    }),
    uni(),
    Unocss()
  ],
  server: {
    open: true, // 自动打开
    base: './ ', // 生产环境路径
    proxy: {
      // 本地开发环境通过代理实现跨域，生产环境使用 nginx 转发
      // 正则表达式写法
      '^/api': {
        target: env.apiBaseUrl, // 后端服务实际地址
        changeOrigin: true, // 开启代理
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  build: {
    watch: {
      exclude: ['node_modules/**', '/__uno.css']
    }
  }
});
