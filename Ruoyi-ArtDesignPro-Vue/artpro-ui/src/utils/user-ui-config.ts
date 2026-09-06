import { fetchUserUiConfig } from '@/api/system/config'
import { SETTING_DEFAULT_CONFIG } from '@/config/setting'
import { ContainerWidthEnum, MenuThemeEnum, MenuTypeEnum, SystemThemeEnum } from '@/enums/appEnum'
import { useSettingStore } from '@/store/modules/setting'

const isTrue = (value: string | undefined, fallback: boolean) =>
  value === undefined ? fallback : value === 'true'

/**
 * 登录后加载当前用户的数据库界面偏好。
 */
export async function loadUserUiConfig() {
  const values = await fetchUserUiConfig()
  const settingStore = useSettingStore()
  const themeMode = (values['ui.theme.mode'] ||
    SETTING_DEFAULT_CONFIG.systemThemeMode) as SystemThemeEnum

  settingStore.$patch({
    ...SETTING_DEFAULT_CONFIG,
    menuType: (values['ui.menu.layout'] || SETTING_DEFAULT_CONFIG.menuType) as MenuTypeEnum,
    menuThemeType: (values['ui.menu.style'] ||
      SETTING_DEFAULT_CONFIG.menuThemeType) as MenuThemeEnum,
    menuOpenWidth: Number(values['ui.menu.width'] || SETTING_DEFAULT_CONFIG.menuOpenWidth),
    dualMenuShowText: isTrue(
      values['ui.menu.dual-show-text'],
      SETTING_DEFAULT_CONFIG.dualMenuShowText
    ),
    systemThemeType: themeMode,
    systemThemeMode: themeMode,
    boxBorderMode: (values['ui.box.style'] || 'border') === 'border',
    containerWidth: (values['ui.container.width'] ||
      SETTING_DEFAULT_CONFIG.containerWidth) as ContainerWidthEnum,
    watermarkVisible: isTrue(
      values['ui.watermark.enabled'],
      SETTING_DEFAULT_CONFIG.watermarkVisible
    ),
    showWorkTab: isTrue(values['ui.tabs.enabled'], SETTING_DEFAULT_CONFIG.showWorkTab),
    tabStyle: values['ui.tab.style'] || SETTING_DEFAULT_CONFIG.tabStyle,
    showCrumbs: isTrue(values['ui.breadcrumb.enabled'], SETTING_DEFAULT_CONFIG.showCrumbs),
    uniqueOpened: isTrue(values['ui.menu.accordion'], SETTING_DEFAULT_CONFIG.uniqueOpened),
    showMenuButton: isTrue(values['ui.menu.button'], SETTING_DEFAULT_CONFIG.showMenuButton),
    showFastEnter: isTrue(values['ui.fast-enter.enabled'], SETTING_DEFAULT_CONFIG.showFastEnter),
    showRefreshButton: isTrue(
      values['ui.refresh.enabled'],
      SETTING_DEFAULT_CONFIG.showRefreshButton
    ),
    showLanguage: isTrue(values['ui.language.enabled'], SETTING_DEFAULT_CONFIG.showLanguage),
    showNprogress: isTrue(values['ui.nprogress.enabled'], SETTING_DEFAULT_CONFIG.showNprogress),
    showSettingGuide: isTrue(
      values['ui.setting-guide.enabled'],
      SETTING_DEFAULT_CONFIG.showSettingGuide
    ),
    colorWeak: isTrue(values['ui.color-weak.enabled'], SETTING_DEFAULT_CONFIG.colorWeak),
    autoClose: isTrue(values['ui.auto-close.enabled'], SETTING_DEFAULT_CONFIG.autoClose),
    pageTransition: values['ui.page-transition'] || SETTING_DEFAULT_CONFIG.pageTransition,
    customRadius: values['ui.radius'] || SETTING_DEFAULT_CONFIG.customRadius
  })
  settingStore.setElementTheme(values['ui.theme.color'] || SETTING_DEFAULT_CONFIG.systemThemeColor)
  settingStore.setCustomRadius(values['ui.radius'] || SETTING_DEFAULT_CONFIG.customRadius)
}
