import { watch } from 'vue'
import { useSettingStore } from '@/store/modules/setting'
import { storeToRefs } from 'pinia'
import { updateUserUiConfig } from '@/api/system/config'
import { SystemThemeEnum } from '@/enums/appEnum'

/**
 * 监听设置面板变更并同步到后端
 */
export function useSettingsSync() {
  const settingStore = useSettingStore()

  const {
    menuType,
    menuThemeType,
    menuOpenWidth,
    dualMenuShowText,
    systemThemeType,
    systemThemeMode,
    boxBorderMode,
    uniqueOpened,
    showMenuButton,
    showFastEnter,
    showRefreshButton,
    showWorkTab,
    showCrumbs,
    showLanguage,
    showNprogress,
    showSettingGuide,
    colorWeak,
    autoClose,
    containerWidth,
    systemThemeColor,
    watermarkVisible,
    tabStyle,
    pageTransition,
    customRadius
  } = storeToRefs(settingStore)

  /** 将 store 值映射为当前用户界面偏好 */
  const toUserUiConfig = (): Record<string, string> => {
    const themeModeMap: Record<string, string> = {
      [SystemThemeEnum.LIGHT]: 'light',
      [SystemThemeEnum.DARK]: 'dark',
      [SystemThemeEnum.AUTO]: 'auto'
    }
    return {
      'ui.menu.layout': String(menuType.value || ''),
      'ui.menu.style': String(menuThemeType.value || ''),
      'ui.menu.width': String(menuOpenWidth.value),
      'ui.menu.dual-show-text': String(dualMenuShowText.value),
      'ui.menu.button': String(showMenuButton.value),
      'ui.fast-enter.enabled': String(showFastEnter.value),
      'ui.refresh.enabled': String(showRefreshButton.value),
      'ui.theme.mode': themeModeMap[systemThemeType.value] || 'auto',
      'ui.theme.color': systemThemeColor.value,
      'ui.box.style': boxBorderMode.value ? 'border' : 'shadow',
      'ui.container.width': String(containerWidth.value || '100%'),
      'ui.tabs.enabled': String(showWorkTab.value),
      'ui.tab.style': tabStyle.value,
      'ui.breadcrumb.enabled': String(showCrumbs.value),
      'ui.menu.accordion': String(uniqueOpened.value),
      'ui.watermark.enabled': String(watermarkVisible.value),
      'ui.language.enabled': String(showLanguage.value),
      'ui.nprogress.enabled': String(showNprogress.value),
      'ui.setting-guide.enabled': String(showSettingGuide.value),
      'ui.color-weak.enabled': String(colorWeak.value),
      'ui.auto-close.enabled': String(autoClose.value),
      'ui.page-transition': pageTransition.value,
      'ui.radius': customRadius.value
    }
  }

  /** 防抖定时器 */
  let debounceTimer: ReturnType<typeof setTimeout> | null = null

  /** 批量同步到后端 */
  const syncToBackend = () => {
    if (debounceTimer) clearTimeout(debounceTimer)
    debounceTimer = setTimeout(async () => {
      try {
        await updateUserUiConfig(toUserUiConfig())
      } catch (e) {
        console.error('[SettingsSync] 同步设置到后端失败', e)
      }
    }, 800)
  }

  /** 启动监听 */
  const startSync = () => {
    watch(
      [
        menuType,
        menuThemeType,
        menuOpenWidth,
        dualMenuShowText,
        systemThemeType,
        systemThemeMode,
        boxBorderMode,
        uniqueOpened,
        showMenuButton,
        showFastEnter,
        showRefreshButton,
        showWorkTab,
        showCrumbs,
        showLanguage,
        showNprogress,
        showSettingGuide,
        colorWeak,
        autoClose,
        containerWidth,
        systemThemeColor,
        watermarkVisible,
        tabStyle,
        pageTransition,
        customRadius
      ],
      syncToBackend,
      { deep: false }
    )
  }

  return { startSync }
}
