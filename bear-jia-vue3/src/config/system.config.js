/**
 * 系统配置文件
 * 统一管理系统级别的配置信息
 */

// 系统基础信息
export const SYSTEM_INFO = {
  // 系统名称
  name: 'BearJia Admin',
  // 系统简称
  shortName: 'BearJia',
  // 公司名称
  companyName: 'BearJia',
  // 系统版本
  version: '1.0.0',
  // 系统描述
  description: '基于 Vue3 + Ant Design Vue 的管理系统',
  // 版权信息
  copyright: 'Copyright © 2024 BearJia Admin',
  // 作者
  author: 'BearJia Team',
  // 网站地址
  website: 'https://bearjia.com',
  // Logo路径
  logo: '/logo.png',
  // Favicon路径
  favicon: '/favicon.ico',
  // SEO关键词
  keywords: 'BearJia,Admin,Vue3,Ant Design Vue',
};

// 系统功能配置
export const SYSTEM_FEATURES = {
  // 是否启用多标签页
  multiTab: true,
  // 是否启用面包屑导航
  breadcrumb: false,
  // 是否启用页脚
  footer: true,
  // 是否启用主题切换
  theme: true,
  // 是否启用国际化
  i18n: false,
  // 是否启用权限控制
  permission: true,
  // 是否启用错误日志
  errorLog: true,
  // 是否启用水印
  watermark: false,
};

// 布局配置
export const LAYOUT_CONFIG = {
  // 默认布局模式: 'side' | 'top' | 'mix'
  defaultMode: 'side',
  // 侧边栏宽度
  sidebarWidth: 208,
  // 侧边栏折叠宽度
  sidebarCollapsedWidth: 48,
  // 顶部高度
  headerHeight: 48,
  // 标签页高度
  tabHeight: 40,
  // 内容区域宽度模式: 'Fluid' | 'Fixed'
  contentWidth: 'Fluid',
  // 固定宽度
  fixedWidth: 1200,
};

// 主题配置
export const THEME_CONFIG = {
  // 默认主题色
  defaultPrimaryColor: '#1677ff',
  // 默认主题模式: 'light' | 'dark'
  defaultMode: 'light',
  // 是否跟随系统主题
  followSystem: false,
  // 是否启用紧凑模式
  compact: false,
  // 是否启用色弱模式
  colorWeak: false,
};

// API配置
export const API_CONFIG = {
  // API基础地址
  baseURL:
    import.meta.env.VITE_APP_BASE_API ||
    import.meta.env.VITE_API_BASE_URL ||
    import.meta.env.VITE_API_DOMAIN ||
    '/api',
  // 请求超时时间（毫秒）
  timeout: 30000,
  // 是否携带cookie
  withCredentials: true,
  // Token键名
  tokenKey: 'Authorization',
  // Token前缀
  tokenPrefix: 'Bearer ',
  // 刷新Token的URL
  refreshTokenUrl: '/auth/refresh',
};

// 存储配置
export const STORAGE_CONFIG = {
  // 存储前缀
  prefix: 'bearjia_',
  // Token存储键
  tokenKey: 'access_token',
  // 用户信息存储键
  userKey: 'user_info',
  // 权限信息存储键
  permissionKey: 'permissions',
  // 设置信息存储键
  settingsKey: 'settings',
  // 语言存储键
  localeKey: 'locale',
};

// 路由配置
export const ROUTER_CONFIG = {
  // 首页路径
  homePath: '/workbench',
  // 登录页路径
  loginPath: '/login',
  // 404页面路径
  notFoundPath: '/404',
  // 白名单路由（不需要登录）
  whiteList: ['/login', '/register', '/forgot-password', '/404', '/403'],
  // 默认跳转路径
  defaultRedirect: '/workbench',
};

// 国际化配置
export const I18N_CONFIG = {
  // 默认语言
  defaultLocale: 'zh-CN',
  // 支持的语言列表
  locales: [
    { key: 'zh-CN', label: '简体中文' },
    { key: 'en-US', label: 'English' },
    { key: 'ja-JP', label: '日本語' },
  ],
  // 是否显示语言切换器
  showPicker: false,
};

// 获取系统标题
export function getPageTitle(pageTitle) {
  const title = SYSTEM_INFO.name;
  if (pageTitle) {
    return `${pageTitle} - ${title}`;
  }
  return title;
}

// 获取存储键名（带前缀）
export function getStorageKey(key) {
  return `${STORAGE_CONFIG.prefix}${key}`;
}

// 系统配置管理类
class SystemConfig {
  constructor() {
    this.config = {
      system: SYSTEM_INFO,
      features: SYSTEM_FEATURES,
      layout: LAYOUT_CONFIG,
      theme: THEME_CONFIG,
      api: API_CONFIG,
      storage: STORAGE_CONFIG,
      router: ROUTER_CONFIG,
      i18n: I18N_CONFIG,
    };
  }

  /**
   * 获取配置项
   * @param {string} path - 配置路径，如 'system.name'
   * @returns {any} 配置值
   */
  get(path) {
    const keys = path.split('.');
    let value = this.config;

    for (const key of keys) {
      if (value && typeof value === 'object' && key in value) {
        value = value[key];
      } else {
        return undefined;
      }
    }

    return value;
  }

  /**
   * 设置配置项
   * @param {string} path - 配置路径
   * @param {any} value - 配置值
   */
  set(path, value) {
    const keys = path.split('.');
    const lastKey = keys.pop();
    let target = this.config;

    for (const key of keys) {
      if (!(key in target) || typeof target[key] !== 'object') {
        target[key] = {};
      }
      target = target[key];
    }

    target[lastKey] = value;
  }

  /**
   * 更新系统信息
   * @param {object} info - 系统信息对象
   */
  updateSystemInfo(info) {
    this.config.system = { ...this.config.system, ...info };
  }

  /**
   * 获取完整配置
   * @returns {object} 配置对象
   */
  getAll() {
    return this.config;
  }

  /**
   * 重置配置
   */
  reset() {
    this.config = {
      system: { ...SYSTEM_INFO },
      features: { ...SYSTEM_FEATURES },
      layout: { ...LAYOUT_CONFIG },
      theme: { ...THEME_CONFIG },
      api: { ...API_CONFIG },
      storage: { ...STORAGE_CONFIG },
      router: { ...ROUTER_CONFIG },
      i18n: { ...I18N_CONFIG },
    };
  }
}

// 创建单例实例
const systemConfig = new SystemConfig();

// 导出配置实例
export default systemConfig;

// 便捷方法导出
export const useSystemConfig = () => {
  return {
    // 获取系统信息
    systemInfo: systemConfig.get('system'),
    // 获取系统名称
    systemName: systemConfig.get('system.name'),
    // 获取版本号
    version: systemConfig.get('system.version'),
    // 获取配置
    getConfig: systemConfig.get.bind(systemConfig),
    // 设置配置
    setConfig: systemConfig.set.bind(systemConfig),
    // 更新系统信息
    updateSystemInfo: systemConfig.updateSystemInfo.bind(systemConfig),
    // 获取页面标题
    getPageTitle,
    // 获取存储键
    getStorageKey,
  };
};
