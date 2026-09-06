/**
 * Vite 配置入口
 */
import path from 'path';
import { loadEnv } from 'vite';
import { getAlias, getServerConfig, getDefineConfig, getOptimizeDeps } from './base.js';
import { getBuildConfig } from './build.js';
import { getPlugins } from './plugins.js';

/**
 * 获取完整的 Vite 配置
 */
export function getViteConfig({ mode }) {
  const cwd = process.cwd();
  const envDir = path.resolve(cwd, 'env');
  // 兼容 Vite 默认 mode（development/production）与自定义 mode（dev/prod/test）
  const envMode = mode === 'production' ? 'prod' : mode === 'development' ? 'dev' : mode;
  const isProd = envMode === 'prod';
  const env = loadEnv(envMode, envDir);
  const dirname = cwd;
  
  return {
    envDir,
    plugins: getPlugins(isProd, env),
    resolve: {
      alias: getAlias(dirname)
    },
    base: env.VITE_BASE_URL,
    server: getServerConfig(envMode, cwd),
    build: getBuildConfig(isProd),
    optimizeDeps: getOptimizeDeps(),
    define: getDefineConfig(),
  };
}
