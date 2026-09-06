/**
 * Vite 基础配置
 */
import path from 'path';
import { loadEnv } from 'vite';

/**
 * 获取别名配置
 */
export function getAlias(dirname) {
  return {
    '@': path.resolve(dirname, 'src'),
    '@components': path.resolve(dirname, 'src/components'),
    '@utils': path.resolve(dirname, 'src/utils'),
    '@api': path.resolve(dirname, 'src/api'),
    '@views': path.resolve(dirname, 'src/views'),
    '@stores': path.resolve(dirname, 'src/stores'),
    '@assets': path.resolve(dirname, 'src/assets'),
    '@style': path.resolve(dirname, 'src/style')
  };
}

/**
 * 获取服务器配置
 */
export function getServerConfig(mode, cwd) {
  const env = loadEnv(mode, path.resolve(cwd, 'env'));
  const proxyTarget = env.VITE_PROXY_TARGET || 'http://localhost:8080';
  const baseApi = env.VITE_APP_BASE_API || env.VITE_API_DOMAIN || '/dev-api';
  const proxyPrefix = baseApi.startsWith('/') ? baseApi : '/dev-api';
  const proxyPrefixRE = new RegExp(`^${proxyPrefix.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}`);

  return {
    host: '0.0.0.0',
    port: parseInt(env.VITE_PORT || 3000),
    open: true,
    proxy: {
      [proxyPrefix]: {
        target: proxyTarget,
        changeOrigin: true,
        rewrite: (path) => path.replace(proxyPrefixRE, ''),
        timeout: 30000,
        ws: true,
        retry: 0,
        configure: (proxy, options) => {
          proxy.on('error', (err) => {
            console.log('代理错误', err);
          });
          proxy.on('proxyReq', (proxyReq, req) => {
            // 修改请求头，禁用缓存
            proxyReq.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');
            proxyReq.setHeader('Pragma', 'no-cache');
            proxyReq.setHeader('Expires', '0');
          });
          proxy.on('proxyRes', (proxyRes, req, res) => {
            // 修改响应头，禁用缓存
            proxyRes.headers['cache-control'] = 'no-cache, no-store, must-revalidate';
            proxyRes.headers['pragma'] = 'no-cache';
            proxyRes.headers['expires'] = '0';
          });
        }
      },
    },
  };
}

/**
 * 获取全局常量定义
 */
export function getDefineConfig() {
  return {
    __VUE_PROD_DEVTOOLS__: false,
    __VUE_OPTIONS_API__: true,
    __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: false
  };
}

/**
 * 获取依赖优化配置
 * 关键优化：将大型库预构建，减少开发环境请求数量
 */
export function getOptimizeDeps() {
  return {
    include: [
      // Vue 核心生态
      'vue',
      'vue-router',
      'pinia',
      '@vueuse/core',

      // Ant Design Vue - 关键优化！预构建整个库
      'ant-design-vue',
      'ant-design-vue/es',
      '@ant-design/icons-vue',

      // 常用工具库
      'dayjs',
      'axios',
      'js-cookie',
      'nprogress',
      'sortablejs',
      'file-saver',

      // 代码高亮
      'highlight.js',

      // 其他依赖
      'vue-request',
      'echarts',
    ],
    exclude: [
      // 只排除确实有问题的包
    ],
    // 强制预构建
    force: false,
    // esbuild 配置
    esbuildOptions: {
      target: 'es2015',
    }
  };
}
