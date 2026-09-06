/**
 * Vite 插件配置
 */
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers';
import { visualizer } from 'rollup-plugin-visualizer';

/**
 * 获取基础插件配置
 */
export function getPlugins(isProd, env = {}) {
  const plugins = [
    vue(),
    vueJsx(),
    
    // 自动导入 Vue API 和 Vue Router API
    AutoImport({
      imports: [
        'vue',
        'vue-router',
        'pinia',
        '@vueuse/core',
        {
          'ant-design-vue/es': [
            'message',
            'notification',
            'Modal',
          ],
          'dayjs': [
            ['default', 'dayjs']
          ],
          'axios': [
            ['default', 'axios']
          ],
        }
      ],
      dts: 'src/types/auto-imports.d.ts', // 生成类型定义文件
      eslintrc: {
        enabled: true, // 生成 .eslintrc-auto-import.json
        filepath: 'config/eslint/.eslintrc-auto-import.json',
      },
    }),
    
    // 自动导入组件
    Components({
      resolvers: [
        AntDesignVueResolver({
          importStyle: false, // css in js
        }),
      ],
      dts: 'src/types/components.d.ts', // 生成类型定义文件
      directoryAsNamespace: false,
      globalNamespaces: [],
      directives: true,
      include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
      exclude: [/[\\/]node_modules[\\/]/, /[\\/]\.git[\\/]/, /[\\/]\.nuxt[\\/]/],
    }),
  ];
  
  // 生产环境添加额外的插件
  const enableVisualizer = env.VITE_VISUALIZER === 'true' || env.VITE_ANALYZE === 'true';
  const visualizerOpen = env.VITE_VISUALIZER_OPEN === 'true' || env.VITE_ANALYZE_OPEN === 'true';

  if (isProd && enableVisualizer) {
    // 添加打包分析插件
    plugins.push(
      visualizer({
        filename: 'dist/stats.html',
        open: visualizerOpen,
        gzipSize: true,
        brotliSize: true,
      })
    );
  }
  
  return plugins;
}
