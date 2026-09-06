/**
 * 主题配置文件
 * 定义主题相关的所有配置项和常量
 */

// 预设主题色
export const PRESET_COLORS = [
  { color: '#1677ff', name: '默认蓝', key: 'default' },
  { color: '#f5222d', name: '薄暮红', key: 'sunset' },
  { color: '#fa541c', name: '火山橙', key: 'volcano' },
  { color: '#faad14', name: '日暮黄', key: 'sunset-yellow' },
  { color: '#13c2c2', name: '明青色', key: 'cyan' },
  { color: '#52c41a', name: '极光绿', key: 'green' },
  { color: '#722ed1', name: '酱紫色', key: 'purple' },
  { color: '#eb2f96', name: '粉红色', key: 'pink' },
  { color: '#2f54eb', name: '拂晓蓝', key: 'daybreak' },
  { color: '#1890ff', name: '天空蓝', key: 'sky' },
];

// 主题模式
export const THEME_MODES = {
  LIGHT: 'light',
  DARK: 'dark',
};

// 布局模式
export const LAYOUT_MODES = {
  SIDE: 'side', // 侧边栏布局
  TOP: 'top', // 顶部菜单布局
  MIX: 'mix', // 混合布局
  COLUMN: 'column', // 分栏布局
  DRAWER: 'drawer', // 抽屉布局
};

// 内容宽度模式
export const CONTENT_WIDTH_MODES = {
  FLUID: 'fluid', // 流式
  FIXED: 'fixed', // 固定
};

// 默认主题配置
export const DEFAULT_THEME_CONFIG = {
  // 主题模式
  mode: THEME_MODES.LIGHT,
  // 主题色
  primaryColor: '#1677ff',
  // 布局设置
  layout: {
    mode: LAYOUT_MODES.SIDE,
    contentWidth: CONTENT_WIDTH_MODES.FLUID,
    fixedHeader: true,
    fixedSidebar: true,
    splitMenus: false,
    multiTab: true,
    hideFooter: false,
  },
  // 色弱模式
  colorWeak: false,
  // 过渡动画
  transition: true,
  // 紧凑模式
  compact: false,
};

// 主题存储键名
export const THEME_STORAGE_KEYS = {
  CONFIG: 'theme-config',
  MODE: 'theme-mode',
  COLOR: 'theme-color',
  LAYOUT: 'theme-layout',
  // 兼容旧版本
  DARK_MODE: 'darkMode',
  PRIMARY_COLOR: 'primaryColor',
  NAV_MODE: 'navMode',
  LAYOUT_SETTINGS: 'layoutSettings',
};

// CSS变量映射
export const CSS_VARIABLES = {
  // 主题色相关
  PRIMARY_COLOR: '--primary-color',
  PRIMARY_COLOR_HOVER: '--primary-color-hover',
  PRIMARY_COLOR_ACTIVE: '--primary-color-active',
  PRIMARY_1: '--primary-1',

  // Ant Design 兼容
  ANT_PRIMARY_COLOR: '--ant-primary-color',
  ANT_PRIMARY_COLOR_HOVER: '--ant-primary-color-hover',
  ANT_PRIMARY_COLOR_ACTIVE: '--ant-primary-color-active',
  ANT_PRIMARY_1: '--ant-primary-1',

  // 背景色
  BG_COLOR: '--bg-color',
  COMPONENT_BG: '--component-background',
  SIDEBAR_BG: '--sidebar-background',

  // 文字颜色
  TEXT_PRIMARY: '--text-primary',
  TEXT_SECONDARY: '--text-secondary',
  TEXT_DISABLED: '--text-disabled',

  // 边框颜色
  BORDER_COLOR: '--border-color',
  BORDER_COLOR_BASE: '--border-color-base',
  BORDER_COLOR_SPLIT: '--border-color-split',
};

// 主题类名
export const THEME_CLASSES = {
  DARK: 'dark-theme',
  LIGHT: 'light-theme',
  COLOR_WEAK: 'color-weak',
  TRANSITION: 'theme-transition',
  COMPACT: 'compact-mode',
};

// 主题事件
export const THEME_EVENTS = {
  CHANGE: 'themeChange',
  MODE_CHANGE: 'themeModeChange',
  COLOR_CHANGE: 'themeColorChange',
  LAYOUT_CHANGE: 'themeLayoutChange',
};

// 颜色工具函数
export const ColorUtils = {
  /**
   * 判断是否为有效的十六进制颜色
   * @param {string} color - 颜色值
   * @returns {boolean}
   */
  isValidHex(color) {
    return /^#[0-9A-F]{6}$/i.test(color);
  },

  /**
   * 十六进制转RGB
   * @param {string} hex - 十六进制颜色
   * @returns {object|null} RGB对象
   */
  hexToRgb(hex) {
    const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result
      ? {
          r: parseInt(result[1], 16),
          g: parseInt(result[2], 16),
          b: parseInt(result[3], 16),
        }
      : null;
  },

  /**
   * RGB转十六进制
   * @param {number} r - 红色值
   * @param {number} g - 绿色值
   * @param {number} b - 蓝色值
   * @returns {string} 十六进制颜色
   */
  rgbToHex(r, g, b) {
    const toHex = (c) => {
      const hex = c.toString(16);
      return hex.length === 1 ? '0' + hex : hex;
    };
    return '#' + toHex(r) + toHex(g) + toHex(b);
  },

  /**
   * 调整颜色亮度
   * @param {string} color - 基础颜色
   * @param {number} percent - 调整百分比（正数变亮，负数变暗）
   * @returns {string} 调整后的颜色
   */
  adjustBrightness(color, percent) {
    const rgb = this.hexToRgb(color);
    if (!rgb) return color;

    const factor = 1 + percent / 100;
    const r = Math.min(255, Math.max(0, Math.round(rgb.r * factor)));
    const g = Math.min(255, Math.max(0, Math.round(rgb.g * factor)));
    const b = Math.min(255, Math.max(0, Math.round(rgb.b * factor)));

    return this.rgbToHex(r, g, b);
  },

  /**
   * 获取颜色的对比色
   * @param {string} color - 基础颜色
   * @returns {string} 对比色（黑色或白色）
   */
  getContrastColor(color) {
    const rgb = this.hexToRgb(color);
    if (!rgb) return '#000000';

    // 计算亮度
    const luminance = (0.299 * rgb.r + 0.587 * rgb.g + 0.114 * rgb.b) / 255;
    return luminance > 0.5 ? '#000000' : '#ffffff';
  },

  /**
   * 生成颜色渐变
   * @param {string} startColor - 起始颜色
   * @param {string} endColor - 结束颜色
   * @param {number} steps - 渐变步数
   * @returns {array} 渐变颜色数组
   */
  generateGradient(startColor, endColor, steps) {
    const start = this.hexToRgb(startColor);
    const end = this.hexToRgb(endColor);
    if (!start || !end) return [];

    const gradient = [];
    for (let i = 0; i < steps; i++) {
      const ratio = i / (steps - 1);
      const r = Math.round(start.r + (end.r - start.r) * ratio);
      const g = Math.round(start.g + (end.g - start.g) * ratio);
      const b = Math.round(start.b + (end.b - start.b) * ratio);
      gradient.push(this.rgbToHex(r, g, b));
    }

    return gradient;
  },
};

// 导出所有配置
export default {
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
};
