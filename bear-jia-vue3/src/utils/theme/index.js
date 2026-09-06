/**
 * 主题模块入口文件
 * 导出所有主题相关的功能和组件
 */

// 导入主题服务，以便作为默认导出
import themeService from './themeService';

// 导出主题管理器
export { default as themeManager, useTheme } from './themeManager';

// 导出主题服务
export { default as themeService, useThemeService } from './themeService';

// 导出主题配置
export { default as themeConfig } from './themeConfig';
export {
  PRESET_COLORS,
  THEME_MODES,
  LAYOUT_MODES,
  CONTENT_WIDTH_MODES,
  DEFAULT_THEME_CONFIG,
  THEME_STORAGE_KEYS,
  CSS_VARIABLES,
  THEME_CLASSES,
  THEME_EVENTS,
  ColorUtils,
} from './themeConfig';

// 导出Vue组合式API
export { useThemeComposition } from './composables/useThemeComposition';

// 默认导出主题服务
export default themeService;
