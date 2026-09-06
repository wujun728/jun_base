import defaultSettings, { resolveMenuLayout, type MenuLayout } from '@/settings'
import { useDynamicTitle } from '@/utils/dynamicTitle'
import { defineStore } from 'pinia'
const { theme, sideTheme, showSettings, menuLayout, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle, footerVisible, footerContent, initDbSetting } = defaultSettings

const storageSetting: typeof defaultSettings = JSON.parse(
  localStorage.getItem('layout-setting') || '{}'
)

const useSettingsStore = defineStore('settings', {
  state: () => ({
    title: '',
    theme: '#11A983',
    sideTheme: 'theme-light',
    showSettings: showSettings,
    menuLayout: 'left' as MenuLayout,
    topNav: false,
    tagsView: true,
    fixedHeader: true,
    sidebarLogo: true,
    dynamicTitle: true,
    footerVisible: footerVisible,
    footerContent: footerContent,
    inited: false
  }),
  actions: {
    async initSetting() {
      if (this.inited) return
      try {
        const config = await initDbSetting()
        this.theme = storageSetting.theme ?? config.theme ?? theme
        this.sideTheme = storageSetting.sideTheme ?? config.sideTheme ?? sideTheme
        this.menuLayout = resolveMenuLayout(storageSetting.menuLayout, storageSetting.topNav)
        if (!storageSetting.menuLayout && !storageSetting.topNav) {
          this.menuLayout = config.menuLayout ?? resolveMenuLayout(undefined, config.topNav ?? topNav)
        }
        this.topNav = this.menuLayout !== 'left'
        this.tagsView = storageSetting.tagsView ?? config.tagsView ?? tagsView
        this.fixedHeader = storageSetting.fixedHeader ?? config.fixedHeader ?? fixedHeader
        this.sidebarLogo = storageSetting.sidebarLogo ?? config.sidebarLogo ?? sidebarLogo
        this.dynamicTitle = storageSetting.dynamicTitle ?? config.dynamicTitle ?? dynamicTitle
      } finally {
        this.inited = true
      }
    },
    /** 修改布局设置 */
    changeSetting(data: { key: keyof typeof storageSetting, value: any }) {
      const { key, value } = data
      if (key === 'menuLayout') {
        this.menuLayout = resolveMenuLayout(value)
        this.topNav = this.menuLayout !== 'left'
        return
      }
      if (this.hasOwnProperty(key)) {
        //@ts-ignore
        this[key] = value
        if (key === 'topNav') {
          this.menuLayout = value ? 'mix' : 'left'
        }
      }
    },
    /** 设置网页标题 */
    setTitle(title: string) {
      this.title = title
      useDynamicTitle();
    }
  },
  getters: {
    isLeftMenu: state => state.menuLayout === 'left',
    isMixMenu: state => state.menuLayout === 'mix',
    isTopMenu: state => state.menuLayout === 'top'
  }
})

export default useSettingsStore
