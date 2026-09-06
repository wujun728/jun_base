import vue from '@vitejs/plugin-vue'
import setupExtend from 'unplugin-vue-setup-extend-plus/vite'
import createAutoImport from './auto-import'
import createSvgIcon from './svg-icon'
import createCompression from './compression'
import vueJsx from '@vitejs/plugin-vue-jsx'
export default function createVitePlugins(
  viteEnv: Record<string, string>,
  isBuild = false
) {
  const vitePlugins = [vue(), setupExtend({}), vueJsx()]
  vitePlugins.push(createAutoImport())
  vitePlugins.push(createSvgIcon(isBuild))
  isBuild && vitePlugins.push(...createCompression(viteEnv))
  return vitePlugins
}
