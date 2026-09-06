import {mergeConfig} from 'vite';
import eslint from 'vite-plugin-eslint';
import baseConfig from './vite.config.base';

export default mergeConfig(
    {
        mode: 'development',
        server: {
            // 是否自动在浏览器打开
            open: true,
            // 端口
            port: 8991,
            // 是否开启 https
            https: false,
            fs: {
                strict: true,
            },
            // 设置反向代理，跨域
            proxy: {
                '/dev-api': {
                    target: `http://localhost:8080`,
                    changeOrigin: true,
                    secure: false, // 如果是https接口，需要配置这个参数
                    rewrite: (path: string) => path.replace(/^\/dev-api/, '')
                }
            },
        },
        plugins: [
            eslint({
                cache: false,
                include: ['src/**/*.ts', 'src/**/*.tsx', 'src/**/*.vue'],
                exclude: ['node_modules'],
            }),
        ],
    },
    baseConfig
);
