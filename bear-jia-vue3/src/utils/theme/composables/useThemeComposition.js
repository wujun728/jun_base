/**
 * Vue组合式API - 主题相关功能
 * 提供在Vue组件中使用主题功能的便捷接口
 */

import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import themeService from '../themeService';
import { THEME_EVENTS } from '../themeConfig';

/**
 * 主题组合式函数
 * @returns {object} 主题相关的响应式数据和方法
 */
export function useThemeComposition() {
  // 响应式主题状态
  const themeMode = ref('light');
  const primaryColor = ref('#1677ff');
  const isDarkMode = ref(false);
  const isLightMode = ref(true);
  const colorWeak = ref(false);

  // 预设颜色列表
  const presetColors = computed(() => themeService.getPresetColors());

  // 所有颜色列表（预设+自定义）
  const allColors = computed(() => themeService.getAllColors());

  // 当前主题信息
  const currentTheme = computed(() => ({
    mode: themeMode.value,
    primaryColor: primaryColor.value,
    isDark: isDarkMode.value,
    isLight: isLightMode.value,
    colorWeak: colorWeak.value,
  }));

  // 更新主题状态
  const updateThemeState = () => {
    const theme = themeService.getCurrentTheme();
    themeMode.value = theme.mode;
    primaryColor.value = theme.primaryColor;
    isDarkMode.value = theme.isDark;
    isLightMode.value = theme.isLight;

    colorWeak.value = theme.colorWeak;
  };

  // 初始化主题
  const initTheme = async (options = {}) => {
    await themeService.init(options);
    updateThemeState();
  };

  // 设置主题模式
  const setThemeMode = (mode) => {
    themeService.setThemeMode(mode);
    updateThemeState();
  };

  // 切换主题
  const toggleTheme = () => {
    themeService.toggleTheme();
    updateThemeState();
  };

  // 设置主题色
  const setPrimaryColor = (color) => {
    themeService.setPrimaryColor(color);
    updateThemeState();
  };

  // 设置色弱模式
  const setColorWeak = (enabled) => {
    themeService.setColorWeak(enabled);
    colorWeak.value = enabled;
  };

  // 添加自定义颜色
  const addCustomColor = (color, name) => {
    return themeService.addCustomColor(color, name);
  };

  // 删除自定义颜色
  const removeCustomColor = (color) => {
    return themeService.removeCustomColor(color);
  };

  // 重置主题
  const resetTheme = () => {
    themeService.resetTheme();
    updateThemeState();
  };

  // 导出主题配置
  const exportThemeConfig = () => {
    return themeService.exportThemeConfig();
  };

  // 导入主题配置
  const importThemeConfig = (config) => {
    themeService.importThemeConfig(config);
    updateThemeState();
  };

  // 下载主题配置
  const downloadThemeConfig = () => {
    themeService.downloadThemeConfig();
  };

  // 从文件导入主题配置
  const importThemeFromFile = async (file) => {
    await themeService.importThemeFromFile(file);
    updateThemeState();
  };

  // 事件监听器
  let unsubscribers = [];

  // 组件挂载时初始化
  onMounted(() => {
    // 更新初始状态
    updateThemeState();

    // 监听主题变化事件
    unsubscribers.push(
      themeService.onEvent(THEME_EVENTS.CHANGE, updateThemeState),
      themeService.onEvent(THEME_EVENTS.MODE_CHANGE, updateThemeState),
      themeService.onEvent(THEME_EVENTS.COLOR_CHANGE, updateThemeState)
    );
  });

  // 组件卸载时清理
  onUnmounted(() => {
    unsubscribers.forEach((unsubscribe) => unsubscribe());
    unsubscribers = [];
  });

  return {
    // 响应式状态
    themeMode,
    primaryColor,
    isDarkMode,
    isLightMode,
    colorWeak,
    presetColors,
    allColors,
    currentTheme,

    // 方法
    initTheme,
    setThemeMode,
    toggleTheme,
    setPrimaryColor,
    setColorWeak,
    addCustomColor,
    removeCustomColor,
    resetTheme,
    exportThemeConfig,
    importThemeConfig,
    downloadThemeConfig,
    importThemeFromFile,
  };
}

/**
 * 简化版的主题组合式函数
 * 只提供最常用的功能
 */
export function useSimpleTheme() {
  const { isDarkMode, primaryColor, toggleTheme, setPrimaryColor } = useThemeComposition();

  return {
    isDarkMode,
    primaryColor,
    toggleTheme,
    setPrimaryColor,
  };
}

/**
 * 主题色选择器组合式函数
 */
export function useThemeColorPicker() {
  const {
    primaryColor,
    presetColors,
    allColors,
    setPrimaryColor,
    addCustomColor,
    removeCustomColor,
  } = useThemeComposition();

  // 选择颜色
  const selectColor = (color) => {
    setPrimaryColor(color);
  };

  // 是否为当前颜色
  const isActiveColor = computed(() => (color) => {
    return primaryColor.value === color;
  });

  return {
    currentColor: primaryColor,
    presetColors,
    allColors,
    selectColor,
    isActiveColor,
    addCustomColor,
    removeCustomColor,
  };
}

/**
 * 主题模式切换器组合式函数
 */
export function useThemeModeToggle() {
  const { themeMode, isDarkMode, isLightMode, setThemeMode, toggleTheme } = useThemeComposition();

  // 设置为亮色主题
  const setLightMode = () => {
    setThemeMode('light');
  };

  // 设置为暗色主题
  const setDarkMode = () => {
    setThemeMode('dark');
  };

  // 跟随系统主题
  const followSystemTheme = () => {
    const systemTheme = themeService.getSystemPreferredTheme();
    if (systemTheme) {
      setThemeMode(systemTheme);
    }
  };

  return {
    themeMode,
    isDarkMode,
    isLightMode,
    toggleTheme,
    setLightMode,
    setDarkMode,
    followSystemTheme,
  };
}

/**
 * 响应式主题CSS变量
 */
export function useThemeCSSVariables() {
  const cssVariables = ref({});
  const { primaryColor } = useThemeComposition();

  // 监听主题色变化，更新CSS变量
  watch(
    primaryColor,
    (newColor) => {
      cssVariables.value = themeService.generateCSSVariables(newColor);
    },
    { immediate: true }
  );

  return {
    cssVariables,
  };
}

// 默认导出
export default useThemeComposition;
