import defaultSettings from '@/settings'
import { useDynamicTitle } from '@/utils/dynamicTitle'
import { defineStore } from 'pinia'
import { handleThemeStyle } from '@/utils/theme'
const { theme, sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle, footerVisible, footerContent, initDbSetting } = defaultSettings

const storageSetting: typeof defaultSettings = JSON.parse(
  localStorage.getItem('layout-setting') || '{}'
)

const useSettingsStore = defineStore('settings', {
  state: () => ({
    title: '',
    theme: '#11A983',
    sideTheme: 'theme-light',
    showSettings: showSettings,
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
      const config = await initDbSetting()
      this.theme = storageSetting.theme ?? config.theme ?? theme
      this.sideTheme = storageSetting.sideTheme ?? config.sideTheme ?? sideTheme
      this.topNav = storageSetting.topNav ?? config.topNav ?? topNav
      this.tagsView = storageSetting.tagsView ?? config.tagsView ?? tagsView
      this.fixedHeader = storageSetting.fixedHeader ?? config.fixedHeader ?? fixedHeader
      this.sidebarLogo = storageSetting.sidebarLogo ?? config.sidebarLogo ?? sidebarLogo
      this.dynamicTitle = storageSetting.dynamicTitle ?? config.dynamicTitle ?? dynamicTitle
      this.inited = true
      handleThemeStyle(this.theme)
    },
    // 修改布局设置
    changeSetting(data: { key: keyof typeof storageSetting, value: any }) {
      const { key, value } = data
      if (this.hasOwnProperty(key)) {
        //@ts-ignore
        this[key] = value
      }
    },
    // 设置网页标题
    setTitle(title: string) {
      this.title = title
      useDynamicTitle();
    }
  }
})

export default useSettingsStore
