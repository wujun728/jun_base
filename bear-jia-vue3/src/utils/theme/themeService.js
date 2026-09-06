/**
 * 主题服务
 * 提供主题管理的统一接口，整合主题管理器和配置
 */

import themeManager from './themeManager';
import themeConfig from './themeConfig';
import { message } from 'ant-design-vue';

const { PRESET_COLORS, THEME_MODES, CSS_VARIABLES, THEME_CLASSES, THEME_EVENTS, ColorUtils } =
  themeConfig;

class ThemeService {
  constructor() {
    this.manager = themeManager;
    this.initialized = false;
    this.messageTimer = null;
    this.stopSystemThemeWatcher = null;
    this.lastMessage = '';
    this.messageDelay = 100; // 消息防抖延迟
  }

  /**
   * 初始化主题服务
   * @param {object} options - 初始化选项
   */
  async init(options = {}) {
    if (this.initialized) {
      console.warn('主题服务已经初始化');
      return;
    }

    try {
      // 统一由主题管理器恢复并应用持久化主题
      this.manager.init();

      // 应用主题运行时扩展状态
      this.applyRuntimePreferences();

      // 设置初始化标志
      this.initialized = true;

      // 监听系统主题变化（如果浏览器支持）
      if (options.followSystem) {
        this.watchSystemTheme();
      }

      console.log('✅ 主题服务初始化成功');
    } catch (error) {
      console.error('❌ 主题服务初始化失败:', error);
      throw error;
    }
  }

  /**
   * 应用主题运行时扩展状态
   */
  applyRuntimePreferences() {
    const colorWeak =
      typeof localStorage !== 'undefined' && localStorage.getItem('colorWeak') === 'true';
    this.setColorWeak(colorWeak, true);
  }

  /**
   * 监听系统主题变化
   */
  watchSystemTheme() {
    if (typeof window === 'undefined' || !window.matchMedia || this.stopSystemThemeWatcher) {
      return;
    }

    const darkModeQuery = window.matchMedia('(prefers-color-scheme: dark)');

    // 初始应用系统主题
    if (darkModeQuery.matches) {
      this.setThemeMode(THEME_MODES.DARK);
    }

    const handleSystemThemeChange = (e) => {
      this.setThemeMode(e.matches ? THEME_MODES.DARK : THEME_MODES.LIGHT);
    };

    darkModeQuery.addEventListener('change', handleSystemThemeChange);
    this.stopSystemThemeWatcher = () => {
      darkModeQuery.removeEventListener('change', handleSystemThemeChange);
      this.stopSystemThemeWatcher = null;
    };
  }

  /**
   * 显示消息（带防抖）
   * @param {string} msg - 消息内容
   * @param {string} type - 消息类型
   */
  showMessage(msg, type = 'success') {
    // 如果是相同的消息，且在短时间内，不重复显示
    if (this.lastMessage === msg) {
      return;
    }

    // 立即记录最后的消息，防止并发调用
    this.lastMessage = msg;

    // 清除之前的定时器
    if (this.messageTimer) {
      clearTimeout(this.messageTimer);
    }

    // 设置定时器，延迟显示消息
    this.messageTimer = setTimeout(() => {
      message[type](msg);

      // 一段时间后清除最后的消息记录
      setTimeout(() => {
        if (this.lastMessage === msg) {
          this.lastMessage = '';
        }
      }, 2000);
    }, this.messageDelay);
  }

  /**
   * 设置主题模式
   * @param {string} mode - 主题模式
   * @param {boolean} silent - 是否静默模式（不显示消息）
   */
  setThemeMode(mode, silent = false) {
    if (!Object.values(THEME_MODES).includes(mode)) {
      console.warn(`无效的主题模式: ${mode}`);
      return;
    }

    this.manager.setThemeMode(mode);

    // 触发主题模式变化事件
    this.emitEvent(THEME_EVENTS.MODE_CHANGE, { mode });

    if (!silent) {
      this.showMessage(`已切换到${mode === THEME_MODES.DARK ? '暗色' : '亮色'}主题`);
    }
  }

  /**
   * 切换主题（亮色/暗色）
   */
  toggleTheme() {
    const newMode = this.manager.isDark ? THEME_MODES.LIGHT : THEME_MODES.DARK;
    this.setThemeMode(newMode);
  }

  /**
   * 设置主题色
   * @param {string} color - 主题色
   * @param {boolean} silent - 是否静默模式
   */
  setPrimaryColor(color, silent = false) {
    if (!ColorUtils.isValidHex(color)) {
      console.warn(`无效的颜色值: ${color}`);
      return;
    }

    this.manager.setPrimaryColor(color);

    // 触发颜色变化事件
    this.emitEvent(THEME_EVENTS.COLOR_CHANGE, { color });

    if (!silent) {
      this.showMessage('主题色已更新');
    }
  }

  /**
   * 设置色弱模式
   * @param {boolean} enabled - 是否启用
   * @param {boolean} silent - 是否静默模式
   */
  setColorWeak(enabled, silent = false) {
    const newState = !!enabled;

    if (typeof localStorage !== 'undefined') {
      localStorage.setItem('colorWeak', String(newState));
    }

    if (typeof document === 'undefined') {
      return;
    }

    const root = document.documentElement;
    const currentState = root.classList.contains(THEME_CLASSES.COLOR_WEAK);

    if (currentState === newState) {
      return;
    }

    if (newState) {
      root.classList.add(THEME_CLASSES.COLOR_WEAK);
    } else {
      root.classList.remove(THEME_CLASSES.COLOR_WEAK);
    }

    if (!silent) {
      this.showMessage(`色弱模式已${newState ? '开启' : '关闭'}`);
    }
  }

  /**
   * 获取预设颜色列表
   * @returns {array} 预设颜色数组
   */
  getPresetColors() {
    return PRESET_COLORS;
  }

  /**
   * 获取所有颜色（预设+自定义）
   * @returns {array} 所有颜色数组
   */
  getAllColors() {
    return this.manager.getAllColors();
  }

  /**
   * 添加自定义颜色
   * @param {string} color - 颜色值
   * @param {string} name - 颜色名称
   */
  addCustomColor(color, name) {
    if (!ColorUtils.isValidHex(color)) {
      this.showMessage('无效的颜色值', 'error');
      return;
    }

    this.manager.addCustomColor(color, name);
    this.showMessage('自定义颜色已添加');
  }

  /**
   * 删除自定义颜色
   * @param {string} color - 颜色值
   */
  removeCustomColor(color) {
    this.manager.removeCustomColor(color);
    this.showMessage('自定义颜色已删除');
  }

  /**
   * 重置主题
   */
  resetTheme() {
    this.manager.resetTheme();
    this.setColorWeak(false, true);
    this.showMessage('主题已重置为默认值');
  }

  /**
   * 导出主题配置
   * @returns {object} 主题配置对象
   */
  exportThemeConfig() {
    const config = this.manager.exportTheme();

    // 添加额外的配置信息
    config.exportTime = new Date().toISOString();
    config.version = '1.0.0';

    return config;
  }

  /**
   * 导入主题配置
   * @param {object} config - 主题配置对象
   */
  importThemeConfig(config) {
    try {
      this.manager.importTheme(config);
      this.showMessage('主题配置已导入');
    } catch (error) {
      console.error('导入主题配置失败:', error);
      this.showMessage('主题配置导入失败', 'error');
    }
  }

  /**
   * 下载主题配置文件
   */
  downloadThemeConfig() {
    const config = this.exportThemeConfig();
    const blob = new Blob([JSON.stringify(config, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `theme-config-${new Date().toISOString().split('T')[0]}.json`;
    link.click();
    URL.revokeObjectURL(url);

    this.showMessage('主题配置已下载');
  }

  /**
   * 从文件导入主题配置
   * @param {File} file - 文件对象
   * @returns {Promise} 导入结果
   */
  importThemeFromFile(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = (e) => {
        try {
          const config = JSON.parse(e.target.result);
          this.importThemeConfig(config);
          resolve(config);
        } catch (error) {
          reject(error);
        }
      };

      reader.onerror = reject;
      reader.readAsText(file);
    });
  }

  /**
   * 触发主题事件
   * @param {string} eventName - 事件名称
   * @param {object} detail - 事件详情
   */
  emitEvent(eventName, detail) {
    if (typeof window === 'undefined') {
      return;
    }

    const event = new CustomEvent(eventName, { detail });
    window.dispatchEvent(event);
  }

  /**
   * 监听主题事件
   * @param {string} eventName - 事件名称
   * @param {function} callback - 回调函数
   * @returns {function} 取消监听函数
   */
  onEvent(eventName, callback) {
    if (typeof window === 'undefined') {
      return () => undefined;
    }

    const handler = (event) => callback(event.detail);
    window.addEventListener(eventName, handler);
    return () => window.removeEventListener(eventName, handler);
  }

  /**
   * 获取当前主题信息
   * @returns {object} 主题信息
   */
  getCurrentTheme() {
    const colorWeak =
      typeof document !== 'undefined'
        ? document.documentElement.classList.contains(THEME_CLASSES.COLOR_WEAK)
        : typeof localStorage !== 'undefined' && localStorage.getItem('colorWeak') === 'true';

    return {
      mode: this.manager.currentMode,
      primaryColor: this.manager.currentColor,
      isDark: this.manager.isDark,
      isLight: this.manager.isLight,
      colorWeak,
    };
  }

  getStoredLayoutValue(key, fallback) {
    if (typeof localStorage === 'undefined') {
      return fallback;
    }

    const value = localStorage.getItem(key);
    return value ?? fallback;
  }

  /**
   * 读取布尔布局设置
   * @param {string} key - 存储键名
   * @param {boolean} fallback - 默认值
   * @returns {boolean} 布尔设置
   */
  getStoredLayoutBoolean(key, fallback = false) {
    const value = this.getStoredLayoutValue(key, null);
    if (value === null) {
      return fallback;
    }
    return value === 'true';
  }

  /**
   * 生成布局设置快照
   * @param {object} overrides - 覆盖项
   * @returns {object} 布局设置
   */
  createLayoutSettingsSnapshot(overrides = {}) {
    const currentTheme = this.getCurrentTheme();

    return {
      primaryColor: currentTheme.primaryColor || '#1677ff',
      darkMode: currentTheme.mode === THEME_MODES.DARK,
      navMode: this.getStoredLayoutValue('navMode', 'side'),
      theme: currentTheme.mode || THEME_MODES.LIGHT,
      layout: this.getStoredLayoutValue('layout', 'mix'),
      contentWidth: this.getStoredLayoutValue('contentWidth', 'fluid'),
      fixedHeader: this.getStoredLayoutValue('fixedHeader', 'true') !== 'false',
      fixedSidebar: this.getStoredLayoutValue('fixedSidebar', 'true') !== 'false',
      splitMenus: this.getStoredLayoutBoolean('splitMenus', false),
      colorWeak: currentTheme.colorWeak,
      multiTab: this.getStoredLayoutValue('multiTab', 'true') !== 'false',
      hideFooter: this.getStoredLayoutBoolean('hideFooter', false),
      tableStriped: this.getStoredLayoutBoolean('tableStriped', false),
      tableShowHeader: this.getStoredLayoutValue('tableShowHeader', 'true') !== 'false',
      sidebarColorfulIcon: this.getStoredLayoutBoolean('sidebarColorfulIcon', false),
      ...overrides,
    };
  }

  /**
   * 应用布局传入的主题相关设置
   * @param {object} settings - 布局设置
   * @param {object} options - 应用选项
   */
  applyLayoutSettings(settings = {}, options = {}) {
    const { silent = true } = options;

    if (settings.primaryColor) {
      this.setPrimaryColor(settings.primaryColor, silent);
    }

    if (settings.darkMode !== undefined || settings.theme) {
      const mode =
        settings.darkMode !== undefined
          ? settings.darkMode
            ? THEME_MODES.DARK
            : THEME_MODES.LIGHT
          : settings.theme;
      this.setThemeMode(mode, silent);
    }

    if (settings.colorWeak !== undefined) {
      this.setColorWeak(settings.colorWeak, silent);
    }
  }

  /**
   * 生成布局可消费的主题 CSS 变量
   * @param {string} color - 主题色
   * @returns {object} CSS 变量对象
   */
  getLayoutCSSVariables(color = this.manager.currentColor) {
    return this.generateCSSVariables(color);
  }

  /**
   * 检查是否为暗色主题
   * @returns {boolean}
   */
  isDarkMode() {
    return this.manager.isDark;
  }

  /**
   * 检查是否为亮色主题
   * @returns {boolean}
   */
  isLightMode() {
    return this.manager.isLight;
  }

  /**
   * 生成主题CSS变量
   * @param {string} color - 主题色
   * @returns {object} CSS变量对象
   */
  generateCSSVariables(color) {
    const rgb = ColorUtils.hexToRgb(color);
    if (!rgb) return {};

    const variables = {};

    // 生成颜色变体
    variables[CSS_VARIABLES.PRIMARY_COLOR] = color;
    variables[CSS_VARIABLES.ANT_PRIMARY_COLOR] = color;

    // 生成hover和active颜色
    variables[CSS_VARIABLES.PRIMARY_COLOR_HOVER] = ColorUtils.adjustBrightness(color, 10);
    variables[CSS_VARIABLES.ANT_PRIMARY_COLOR_HOVER] = ColorUtils.adjustBrightness(color, 10);
    variables[CSS_VARIABLES.PRIMARY_COLOR_ACTIVE] = ColorUtils.adjustBrightness(color, -10);
    variables[CSS_VARIABLES.ANT_PRIMARY_COLOR_ACTIVE] = ColorUtils.adjustBrightness(color, -10);

    // 生成透明度变体
    variables[CSS_VARIABLES.PRIMARY_1] = `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.1)`;
    variables[CSS_VARIABLES.ANT_PRIMARY_1] = `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.1)`;

    return variables;
  }

  /**
   * 批量应用CSS变量
   * @param {object} variables - CSS变量对象
   */
  applyCSSVariables(variables) {
    if (typeof document === 'undefined') {
      return;
    }

    const root = document.documentElement;
    Object.entries(variables).forEach(([key, value]) => {
      root.style.setProperty(key, value);
    });
  }

  /**
   * 清理主题相关的类名
   */
  cleanThemeClasses() {
    if (typeof document === 'undefined') {
      return;
    }

    const root = document.documentElement;
    Object.values(THEME_CLASSES).forEach((className) => {
      root.classList.remove(className);
      document.body.classList.remove(className);
    });
  }

  /**
   * 获取系统偏好主题
   * @returns {string|null} 系统主题模式
   */
  getSystemPreferredTheme() {
    if (typeof window === 'undefined' || !window.matchMedia) return null;

    const darkModeQuery = window.matchMedia('(prefers-color-scheme: dark)');
    return darkModeQuery.matches ? THEME_MODES.DARK : THEME_MODES.LIGHT;
  }

  /**
   * 销毁主题服务
   */
  destroy() {
    // 清理定时器
    if (this.messageTimer) {
      clearTimeout(this.messageTimer);
      this.messageTimer = null;
    }

    if (this.stopSystemThemeWatcher) {
      this.stopSystemThemeWatcher();
    }

    this.initialized = false;

    console.log('主题服务已销毁');
  }
}

// 创建单例实例
const themeService = new ThemeService();

// 导出便捷方法
export const useThemeService = () => {
  return {
    // 属性
    isDark: () => themeService.isDarkMode(),
    isLight: () => themeService.isLightMode(),
    currentTheme: () => themeService.getCurrentTheme(),
    layoutSettings: themeService.createLayoutSettingsSnapshot.bind(themeService),
    layoutCSSVariables: themeService.getLayoutCSSVariables.bind(themeService),
    presetColors: () => themeService.getPresetColors(),
    allColors: () => themeService.getAllColors(),

    // 方法
    init: themeService.init.bind(themeService),
    applyLayoutSettings: themeService.applyLayoutSettings.bind(themeService),
    setMode: themeService.setThemeMode.bind(themeService),
    toggleTheme: themeService.toggleTheme.bind(themeService),
    setColor: themeService.setPrimaryColor.bind(themeService),
    setColorWeak: themeService.setColorWeak.bind(themeService),
    addCustomColor: themeService.addCustomColor.bind(themeService),
    removeCustomColor: themeService.removeCustomColor.bind(themeService),
    reset: themeService.resetTheme.bind(themeService),
    exportConfig: themeService.exportThemeConfig.bind(themeService),
    importConfig: themeService.importThemeConfig.bind(themeService),
    downloadConfig: themeService.downloadThemeConfig.bind(themeService),
    importFromFile: themeService.importThemeFromFile.bind(themeService),
    onEvent: themeService.onEvent.bind(themeService),
    destroy: themeService.destroy.bind(themeService),
  };
};

// 导出服务实例
export default themeService;
