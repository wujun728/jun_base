import defaultSettings from '@/settings'
import useSettingsStore from '@/store/modules/settings'
import { StrUtil } from './StrUtil';

/**
 * 动态修改标题
 */
export function useDynamicTitle() {
  const settingsStore = useSettingsStore();
  if (settingsStore.dynamicTitle && StrUtil.isNotBlank(settingsStore.title)) {
    document.title = settingsStore.title + ' - ' + defaultSettings.title;
  } else {
    document.title = defaultSettings.title;
  }
}