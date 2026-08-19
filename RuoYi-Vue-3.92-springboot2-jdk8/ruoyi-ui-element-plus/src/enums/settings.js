/**
 * 设置相关枚举
 *
 * @description
 * 包含主题、布局、语言、设备等应用设置的枚举定义
 */

/**
 * 主题模式枚举
 */
export const ThemeMode = {
  LIGHT: "light",
  DARK: "dark",
  AUTO: "auto",
};

/**
 * 侧边栏配色方案枚举
 */
export const SidebarColor = {
  CLASSIC_BLUE: "classic-blue",
  MINIMAL_WHITE: "minimal-white",
};

/**
 * 菜单布局枚举
 */
export const LayoutMode = {
  LEFT: "left",
  TOP: "top",
  MIX: "mix",
  DOUBLE: "double",
};

/**
 * 页签风格枚举
 */
export const TagsViewStyle = {
  LINE: "line",
  CARD: "card",
};

/**
 * 侧边栏状态枚举
 */
export const SidebarStatus = {
  OPENED: "opened",
  CLOSED: "closed",
};

/**
 * 组件尺寸枚举
 */
export const ComponentSize = {
  DEFAULT: "default",
  LARGE: "large",
  SMALL: "small",
};

/**
 * 语言枚举
 */
export const LanguageEnum = {
  ZH_CN: "zh-cn",
  EN: "en",
};

/**
 * 设备枚举
 */
export const DeviceEnum = {
  DESKTOP: "desktop",
  MOBILE: "mobile",
};

/**
 * 页面切换动画枚举
 */
export const PageSwitchingAnimationEnum = {
  NONE: "none",
  FADE: "fade",
  FADE_SLIDE: "fade-slide",
  FADE_SCALE: "fade-scale",
};
export const PageSwitchingAnimationOptions = {
  none: { value: "none", label: "无动画" },
  fade: { value: "fade", label: "淡入淡出" },
  "fade-slide": { value: "fade-slide", label: "平滑切换" },
  "fade-scale": { value: "fade-scale", label: "缩放切换" },
};
