/**
 * 离线图标加载器
 *
 * 用于在内网环境下支持 Iconify 图标的离线加载。
 * 通过预加载图标集数据，避免运行时从 CDN 获取图标。
 *
 * 使用方式：
 * 1. 安装所需图标集：pnpm add -D @iconify-json/[icon-set-name]
 * 2. 在此文件中导入并注册图标集
 * 3. 在组件中使用：<ArtSvgIcon icon="ri:home-line" />
 *
 * @module utils/ui/iconify-loader
 * @author Art Design Pro Team
 */

import { addCollection, iconLoaded } from '@iconify/vue'
import eosIcons from '@iconify-json/eos-icons/icons.json'
import epIcons from '@iconify-json/ep/icons.json'
import lineMdIcons from '@iconify-json/line-md/icons.json'
import riIcons from '@iconify-json/ri/icons.json'
import svgSpinnersIcons from '@iconify-json/svg-spinners/icons.json'
import tablerIcons from '@iconify-json/tabler/icons.json'

addCollection(riIcons)
addCollection(epIcons)
addCollection(tablerIcons)
addCollection(lineMdIcons)
addCollection(svgSpinnersIcons)
addCollection(eosIcons)

const fallbackIcon = 'ri:menu-line'

const legacyIconMap: Record<string, string> = {
  dashboard: 'ri:pie-chart-line',
  system: 'ri:settings-3-line',
  monitor: 'ri:computer-line',
  tool: 'ri:tools-line',
  guide: 'ri:guide-line',
  console: 'ri:home-smile-2-line',
  analysis: 'ri:bar-chart-box-line',
  ecommerce: 'ri:shopping-bag-3-line',
  user: 'ri:user-line',
  peoples: 'ri:team-line',
  tree: 'ri:node-tree',
  'tree-table': 'ri:menu-line',
  post: 'ri:briefcase-line',
  dict: 'ri:book-2-line',
  edit: 'ri:edit-line',
  message: 'ri:notification-3-line',
  log: 'ri:file-list-3-line',
  online: 'ri:global-line',
  job: 'ri:timer-line',
  server: 'ri:server-line',
  redis: 'ri:database-2-line',
  code: 'ri:code-s-slash-line',
  swagger: 'ri:file-code-line'
}

export function normalizeLocalIcon(icon?: string): string {
  const candidate = legacyIconMap[icon || ''] || icon || fallbackIcon
  return iconLoaded(candidate) ? candidate : fallbackIcon
}
