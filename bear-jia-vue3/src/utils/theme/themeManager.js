/**
 * 主题管理器 - 统一管理主题相关的所有功能
 * @module ThemeManager
 */

import { ref, computed, watch } from 'vue';

class ThemeManager {
  constructor() {
    // 主题配置
    this.config = ref({
      // 当前主题模式
      mode: 'light', // light | dark
      // 主题色
      primaryColor: '#1677ff',
      // 预设主题色列表
      presetColors: [
        { color: '#1677ff', name: '默认蓝' },
        { color: '#f5222d', name: '薄暮红' },
        { color: '#fa541c', name: '火山橙' },
        { color: '#faad14', name: '日暮黄' },
        { color: '#13c2c2', name: '明青色' },
        { color: '#52c41a', name: '极光绿' },
        { color: '#722ed1', name: '酱紫色' },
        { color: '#eb2f96', name: '粉红色' },
      ],
      // 自定义主题色
      customColors: [],
    });

    this.initialized = false;
    this.stopThemeWatcher = null;
  }

  /**
   * 初始化主题管理器
   */
  init() {
    if (this.initialized) {
      this.applyTheme();
      return;
    }

    // 从localStorage加载主题配置
    this.loadThemeFromStorage();

    // 监听主题变化
    this.watchThemeChanges();

    // 应用初始主题
    this.applyTheme();

    this.initialized = true;
  }

  /**
   * 从localStorage加载主题配置
   */
  loadThemeFromStorage() {
    if (typeof localStorage === 'undefined') {
      return;
    }

    try {
      const savedTheme = localStorage.getItem('theme-config');
      if (savedTheme) {
        const themeConfig = JSON.parse(savedTheme);
        this.config.value = { ...this.config.value, ...themeConfig };
      }

      // 兼容旧版本配置
      const darkMode = localStorage.getItem('darkMode');
      if (darkMode !== null) {
        this.config.value.mode = darkMode === 'true' ? 'dark' : 'light';
      }

      const primaryColor = localStorage.getItem('primaryColor');
      if (primaryColor) {
        this.config.value.primaryColor = primaryColor;
      }
    } catch (error) {
      console.error('加载主题配置失败:', error);
    }
  }

  /**
   * 保存主题配置到localStorage
   */
  saveThemeToStorage() {
    if (typeof localStorage === 'undefined') {
      return;
    }

    try {
      localStorage.setItem('theme-config', JSON.stringify(this.config.value));
      // 同时保存到旧版本的键，保持兼容性
      localStorage.setItem('darkMode', String(this.config.value.mode === 'dark'));
      localStorage.setItem('primaryColor', this.config.value.primaryColor);
    } catch (error) {
      console.error('保存主题配置失败:', error);
    }
  }

  /**
   * 监听主题变化
   */
  watchThemeChanges() {
    if (this.stopThemeWatcher) {
      return;
    }

    this.stopThemeWatcher = watch(
      () => this.config.value,
      (_newConfig) => {
        this.applyTheme();
        this.saveThemeToStorage();
      },
      { deep: true }
    );
  }

  /**
   * 应用主题
   */
  applyTheme() {
    if (typeof document === 'undefined') {
      return;
    }

    const root = document.documentElement;
    const { mode, primaryColor } = this.config.value;

    // 应用主题模式
    if (mode === 'dark') {
      root.classList.add('dark-theme');
      document.body.classList.add('dark-theme');
    } else {
      root.classList.remove('dark-theme');
      document.body.classList.remove('dark-theme');
    }

    // 应用主题色
    this.applyPrimaryColor(primaryColor);

    // 触发主题变化事件
    this.emitThemeChange();
  }

  /**
   * 应用主题色
   * @param {string} color - 主题色
   */
  applyPrimaryColor(color) {
    if (typeof document === 'undefined') {
      return;
    }

    const root = document.documentElement;

    // 设置主CSS变量
    root.style.setProperty('--primary-color', color);
    root.style.setProperty('--ant-primary-color', color);

    // 计算并设置颜色变体
    const variants = this.generateColorVariants(color);
    Object.entries(variants).forEach(([key, value]) => {
      root.style.setProperty(key, value);
    });
  }

  /**
   * 生成颜色变体
   * @param {string} color - 基础颜色
   * @returns {object} 颜色变体对象
   */
  generateColorVariants(color) {
    const variants = {};

    // 使用CSS color-mix计算变体
    variants['--ant-primary-color-hover'] = `color-mix(in srgb, ${color} 90%, white)`;
    variants['--ant-primary-color-active'] = `color-mix(in srgb, ${color} 110%, black)`;
    variants['--ant-primary-1'] = `color-mix(in srgb, ${color} 20%, white)`;
    variants['--primary-color-hover'] = variants['--ant-primary-color-hover'];
    variants['--primary-color-active'] = variants['--ant-primary-color-active'];
    variants['--primary-1'] = variants['--ant-primary-1'];

    // 生成透明度变体
    const rgb = this.hexToRgb(color);
    if (rgb) {
      for (let i = 1; i <= 9; i++) {
        const opacity = i / 10;
        variants[`--ant-primary-${i}`] = `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, ${opacity})`;
      }
    }

    return variants;
  }

  /**
   * 十六进制颜色转RGB
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
  }

  /**
   * 切换主题模式
   * @param {string} mode - 主题模式 (light | dark)
   */
  setThemeMode(mode) {
    if (['light', 'dark'].includes(mode)) {
      this.config.value.mode = mode;
    }
  }

  /**
   * 切换主题（亮色/暗色）
   */
  toggleTheme() {
    this.config.value.mode = this.config.value.mode === 'light' ? 'dark' : 'light';
  }

  /**
   * 设置主题色
   * @param {string} color - 主题色
   */
  setPrimaryColor(color) {
    if (/^#[0-9A-F]{6}$/i.test(color)) {
      this.config.value.primaryColor = color;
    }
  }

  /**
   * 添加自定义颜色
   * @param {string} color - 颜色值
   * @param {string} name - 颜色名称
   */
  addCustomColor(color, name) {
    if (!this.config.value.customColors.find((c) => c.color === color)) {
      this.config.value.customColors.push({ color, name });
      this.saveThemeToStorage();
    }
  }

  /**
   * 删除自定义颜色
   * @param {string} color - 颜色值
   */
  removeCustomColor(color) {
    const index = this.config.value.customColors.findIndex((c) => c.color === color);
    if (index > -1) {
      this.config.value.customColors.splice(index, 1);
      this.saveThemeToStorage();
    }
  }

  /**
   * 获取所有可用颜色
   * @returns {array} 颜色列表
   */
  getAllColors() {
    return [...this.config.value.presetColors, ...this.config.value.customColors];
  }

  /**
   * 重置主题为默认值
   */
  resetTheme() {
    this.config.value = {
      mode: 'light',
      primaryColor: '#1677ff',
      presetColors: this.config.value.presetColors,
      customColors: [],
    };
    this.saveThemeToStorage();
  }

  /**
   * 导出主题配置
   * @returns {object} 主题配置对象
   */
  exportTheme() {
    return {
      mode: this.config.value.mode,
      primaryColor: this.config.value.primaryColor,
      customColors: this.config.value.customColors,
    };
  }

  /**
   * 导入主题配置
   * @param {object} themeConfig - 主题配置对象
   */
  importTheme(themeConfig) {
    if (themeConfig.mode) {
      this.config.value.mode = themeConfig.mode;
    }
    if (themeConfig.primaryColor) {
      this.config.value.primaryColor = themeConfig.primaryColor;
    }
    if (themeConfig.customColors) {
      this.config.value.customColors = themeConfig.customColors;
    }
    this.saveThemeToStorage();
  }

  /**
   * 触发主题变化事件
   */
  emitThemeChange() {
    if (typeof window === 'undefined') {
      return;
    }

    const event = new CustomEvent('themeChange', {
      detail: {
        mode: this.config.value.mode,
        primaryColor: this.config.value.primaryColor,
      },
    });
    window.dispatchEvent(event);
  }

  /**
   * 监听主题变化
   * @param {function} callback - 回调函数
   * @returns {function} 取消监听函数
   */
  onThemeChange(callback) {
    if (typeof window === 'undefined') {
      return () => undefined;
    }

    const handler = (event) => callback(event.detail);
    window.addEventListener('themeChange', handler);
    return () => window.removeEventListener('themeChange', handler);
  }

  /**
   * 获取当前主题模式
   * @returns {string} 主题模式
   */
  get currentMode() {
    return this.config.value.mode;
  }

  /**
   * 获取当前主题色
   * @returns {string} 主题色
   */
  get currentColor() {
    return this.config.value.primaryColor;
  }

  /**
   * 判断是否为暗色主题
   * @returns {boolean}
   */
  get isDark() {
    return this.config.value.mode === 'dark';
  }

  /**
   * 判断是否为亮色主题
   * @returns {boolean}
   */
  get isLight() {
    return this.config.value.mode === 'light';
  }
}

// 创建单例实例
const themeManager = new ThemeManager();

// 导出便捷方法
export const useTheme = () => {
  return {
    // 响应式数据
    config: computed(() => themeManager.config.value),
    isDark: computed(() => themeManager.isDark),
    isLight: computed(() => themeManager.isLight),
    currentMode: computed(() => themeManager.currentMode),
    currentColor: computed(() => themeManager.currentColor),

    // 方法
    setThemeMode: themeManager.setThemeMode.bind(themeManager),
    toggleTheme: themeManager.toggleTheme.bind(themeManager),
    setPrimaryColor: themeManager.setPrimaryColor.bind(themeManager),
    addCustomColor: themeManager.addCustomColor.bind(themeManager),
    removeCustomColor: themeManager.removeCustomColor.bind(themeManager),
    getAllColors: themeManager.getAllColors.bind(themeManager),
    resetTheme: themeManager.resetTheme.bind(themeManager),
    exportTheme: themeManager.exportTheme.bind(themeManager),
    importTheme: themeManager.importTheme.bind(themeManager),
    onThemeChange: themeManager.onThemeChange.bind(themeManager),
  };
};

// 导出主题管理器实例
export default themeManager;
