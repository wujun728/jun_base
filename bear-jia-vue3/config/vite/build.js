/**
 * Vite 打包配置
 */

/**
 * 获取打包基础配置
 */
export function getBuildBase(isProd) {
  return {
    target: 'es2015',
    outDir: 'dist',
    emptyOutDir: true,
    sourcemap: isProd ? false : 'inline',
    chunkSizeWarningLimit: 1500,
    cssCodeSplit: true,
    cssMinify: isProd,
    assetsInlineLimit: 4096,
    reportCompressedSize: false
  };
}

/**
 * 获取压缩配置
 */
export function getTerserOptions(isProd) {
  if (!isProd) return undefined;
  
  return {
    compress: {
      drop_console: true,
      drop_debugger: true,
      pure_funcs: ['console.log', 'console.info', 'console.debug', 'console.trace'],
      passes: 2,
    },
    format: {
      comments: false,
    }
  };
}

/**
 * 获取静态资源文件名配置
 */
export function getAssetFileNames(assetInfo) {
  const info = assetInfo.name.split('.');
  let extType = info[info.length - 1];
  
  if (/\.(png|jpe?g|gif|svg|webp|ico)$/i.test(assetInfo.name)) {
    extType = 'img';
  } else if (/\.(woff2?|eot|ttf|otf)$/i.test(assetInfo.name)) {
    extType = 'fonts';
  } else if (/\.css$/i.test(assetInfo.name)) {
    extType = 'css';
  }
  
  return `${extType}/[name]-[hash].[ext]`;
}

/**
 * 手动代码分割策略 - 优化版
 */
export function manualChunks(id) {
  // 将 node_modules 中的代码分割为不同的 chunk
  if (id.includes('node_modules')) {
    // 核心Vue生态库 - 最高优先级，确保基础框架独立
    if (id.includes('vue/') && !id.includes('vue-router') && !id.includes('vue-request') && !id.includes('ant-design-vue') && !id.includes('@ant-design')) {
      return 'vue-core';
    }
    if (id.includes('vue-router')) {
      return 'vue-router';
    }
    if (id.includes('pinia')) {
      return 'pinia';
    }
    if (id.includes('@vueuse')) {
      return 'vueuse';
    }
    
    // Ant Design Vue 放在一个 chunk 中，避免模块之间相互引用导致运行时报错
    if (id.includes('ant-design-vue')) {
      return 'ant-design-vue';
    }
    if (id.includes('@ant-design/icons-vue')) {
      return 'antd-icons';
    }

    // 表单设计器依赖 Ant Design Vue，独立分包可避免落入 vendor-misc 后形成循环 chunk
    if (
      id.includes('@form-create') ||
      id.includes('node_modules/wangeditor') ||
      id.includes('codemirror') ||
      id.includes('js-beautify') ||
      id.includes('marked') ||
      id.includes('signature_pad') ||
      id.includes('vuedraggable')
    ) {
      return 'form-designer';
    }

    // 编辑器相关
    if (id.includes('@wangeditor/editor-for-vue')) {
      return 'wangeditor-vue';
    }
    if (id.includes('@wangeditor')) {
      return 'wangeditor-core';
    }
    if (id.includes('highlight.js')) {
      return 'highlight';
    }
    
    // 图表库
    if (id.includes('echarts')) {
      if (id.includes('echarts/core')) {
        return 'echarts-core';
      }
      if (id.includes('echarts/charts')) {
        return 'echarts-charts';
      }
      if (id.includes('echarts/components')) {
        return 'echarts-components';
      }
      if (id.includes('echarts/renderers')) {
        return 'echarts-renderers';
      }
      return 'echarts-base';
    }
    
    // 工具库
    if (id.includes('axios')) {
      return 'axios';
    }
    if (id.includes('dayjs')) {
      return 'dayjs';
    }
    if (id.includes('js-cookie')) {
      return 'js-cookie';
    }
    if (id.includes('nprogress')) {
      return 'nprogress';
    }
    
    // 其他工具库
    if (id.includes('sortablejs')) {
      return 'sortable';
    }
    if (id.includes('file-saver')) {
      return 'file-saver';
    }
    if (id.includes('vue-request')) {
      return 'vue-request';
    }
    
    return 'vendor-misc';
  }
}

/**
 * 获取完整的打包配置
 */
export function getBuildConfig(isProd) {
  const baseConfig = getBuildBase(isProd);
  
  return {
    ...baseConfig,
    minify: isProd ? 'terser' : false,
    terserOptions: getTerserOptions(isProd),
    rollupOptions: {
      output: {
        chunkFileNames: 'js/[name]-[hash].js',
        entryFileNames: 'js/[name]-[hash].js',
        assetFileNames: getAssetFileNames,
        manualChunks
      },
      external: isProd ? [] : [],
    }
  };
}
