/**
 * Vite 主配置文件
 * 配置已模块化，详见 config/vite 目录
 */
import { defineConfig } from 'vite';
import { getViteConfig } from './config/vite/index.js';

export default defineConfig(getViteConfig);
