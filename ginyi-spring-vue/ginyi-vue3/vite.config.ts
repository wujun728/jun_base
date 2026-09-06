import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {resolve} from 'path'
import vueJsxPlugin from "@vitejs/plugin-vue-jsx";


export default defineConfig({
    envDir: ".",
    plugins: [vue(), vueJsxPlugin()],
    server: {
        host: '0.0.0.0',    // 主机地址
        open: true,         // 自动打开浏览器
        port: 3800          // 服务端口
    },
    // 路径别名
    resolve: {
        alias: {
            '@': resolve(__dirname, './src')
        }
    },
    optimizeDeps: {
        include: [
            `monaco-editor/esm/vs/language/json/json.worker`,
            `monaco-editor/esm/vs/language/css/css.worker`,
            `monaco-editor/esm/vs/language/html/html.worker`,
            `monaco-editor/esm/vs/language/typescript/ts.worker`,
            `monaco-editor/esm/vs/editor/editor.worker`
        ],
    }
})