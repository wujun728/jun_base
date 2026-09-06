import { defineStore } from 'pinia'
import { STORE_CONFIG } from '@/store/constant/cacheKey.js'
import { syncLayoutCssVars } from '@/utils/layoutTheme.js'

const layoutInitValue = {
  /* 全局 */
  showDrawer: false,
  // 是否收缩布局(小屏设备)
  shrink: false,
  // 后台布局方式，可选值<Default|Classic|Streamline|Double>
  layoutMode: 'Classic',
  // 是否暗黑模式
  isDark: false,
  /* 侧边菜单 */
  // 侧边菜单背景色
  menuBackground: ['#ffffff', '#1a1a1a'],
  // 侧边菜单文字颜色
  menuColor: ['#1a1a1a', '#ffffff'],
  // 侧边菜单激活项文字色
  menuActiveColor: ['#1a1a1a', '#ffffff'],
  // 侧边菜单悬停背景颜色
  menuHoverBackground: ['#f1f1f1', '#2d2d2d'],
  // 侧边菜单激活项背景色
  menuActiveBackground: ['#f1f1f1', '#2d2d2d'],
  // 侧边菜单宽度(展开时)，单位px
  menuWidth: 245,
  // 侧边菜单项默认图标
  menuDefaultIcon: 'fa fa-circle-o',
  // 是否水平折叠收起菜单
  menuCollapse: false,
  // 是否只保持一个子菜单的展开(手风琴)
  menuUniqueOpened: false,
}
export const useConfig = defineStore(
  'config',
  () => {
    const layout = reactive(layoutInitValue)
    function menuWidth() {
      if (layout.shrink) {
        return layout.menuCollapse ? '0px' : layout.menuWidth + 'px'
      }
      // 菜单是否折叠
      return layout.menuCollapse ? '64px' : layout.menuWidth + 'px'
    }

    const setLayout = (name, value) => {
      layout[name] = value
      syncLayoutCssVars({ layout, getColorVal })
    }
    function setLayoutMode(data) {
      layout.layoutMode = data
      syncLayoutCssVars({ layout, getColorVal })
    }
    const getColorVal = function (name) {
      const colors = layout[name]
      if (layout.isDark) {
        return colors[1]
      } else {
        return colors[0]
      }
    }
    const init = () => {
      syncLayoutCssVars({ layout, getColorVal })
    }
    return {
      layout,
      menuWidth,
      setLayoutMode,
      setLayout,
      getColorVal,
      init,
    }
  },
  {
    persist: {
      key: STORE_CONFIG,
    },
  }
)
