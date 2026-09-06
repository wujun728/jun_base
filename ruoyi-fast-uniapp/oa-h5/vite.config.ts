import { defineConfig } from "vite";

// @ts-ignore
import uni from "@dcloudio/vite-plugin-uni";
// 自动导入vue/uni-app
import AutoImport from "unplugin-auto-import/vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    uni(),
    AutoImport({
      include: [
        /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
        /\.vue$/,
        /\.vue\?vue/, // .vue
      ],
      imports: [
        "vue",
        "uni-app",
      ],
      dts: "typings/auto-imports.d.ts",
    }),
  ],
  // 发布时删除 console
  build: {
    minify: "terser",
    terserOptions: {
      compress: {
        drop_console: true,
      },
    },
  },
  server: {
    proxy: {
      "/api": {
        target: "http://dev.oa.crmeb.net",
        changeOrigin: true
      }
    }
  }
});
