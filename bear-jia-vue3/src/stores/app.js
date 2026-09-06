import { defineStore } from 'pinia';
import Cookies from 'js-cookie';
import { themeService } from '@/utils/theme';
import defaultLogoUrl from '@/assets/images/logo.png';

export const useAppStore = defineStore('app', {
  state: () => {
    const storedLogo = localStorage.getItem('systemLogo');
    const logo = storedLogo && !storedLogo.startsWith('/src/') ? storedLogo : defaultLogoUrl;

    return {
      sidebar: {
        opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
        withoutAnimation: false,
      },
      device: 'desktop',
      size: Cookies.get('size') || 'medium',
      layoutSettings: themeService.createLayoutSettingsSnapshot(),
      // 系统配置
      systemConfig: {
        title: localStorage.getItem('systemTitle') || 'BearJia Admin',
        logo,
        copyright: localStorage.getItem('systemCopyright') || 'Copyright © 2024 BearJia',
        version: localStorage.getItem('systemVersion') || '1.0.0',
        description: localStorage.getItem('systemDescription') || 'BearJia后台管理系统',
        keywords: localStorage.getItem('systemKeywords') || 'BearJia,Admin,Vue3,Ant Design Vue',
      },
    };
  },

  actions: {
    toggleSideBar() {
      this.sidebar.opened = !this.sidebar.opened;
      this.sidebar.withoutAnimation = false;
      if (this.sidebar.opened) {
        Cookies.set('sidebarStatus', 1);
      } else {
        Cookies.set('sidebarStatus', 0);
      }
    },

    closeSideBar(withoutAnimation) {
      Cookies.set('sidebarStatus', 0);
      this.sidebar.opened = false;
      this.sidebar.withoutAnimation = withoutAnimation;
    },

    toggleDevice(device) {
      this.device = device;
    },

    setSize(size) {
      this.size = size;
      Cookies.set('size', size);
    },

    updateSettings(settings) {
      this.layoutSettings = {
        ...this.layoutSettings,
        ...settings,
      };

      // 持久化存储每个设置项，主题相关设置统一交给主题服务
      Object.keys(settings).forEach((key) => {
        if (['primaryColor', 'darkMode', 'theme', 'colorWeak'].includes(key)) {
          return;
        }
        localStorage.setItem(key, String(settings[key]));
      });

      this.applyTheme();
    },

    // 更新系统配置
    updateSystemConfig(config) {
      this.systemConfig = { ...this.systemConfig, ...config };

      // 持久化存储系统配置
      Object.keys(config).forEach((key) => {
        localStorage.setItem(`system${key.charAt(0).toUpperCase() + key.slice(1)}`, config[key]);
      });

      // 更新页面标题
      if (config.title) {
        document.title = config.title;
      }
    },

    // 应用主题（使用新的主题服务）
    applyTheme() {
      try {
        themeService.applyLayoutSettings(this.layoutSettings, { silent: true });
      } catch (error) {
        console.error('❌ 主题应用失败:', error);
      }
    },

    // 重置所有设置
    resetSettings() {
      const defaultSettings = {
        primaryColor: '#1677ff',
        darkMode: false,
        navMode: 'side',
        theme: 'light',
        layout: 'mix',
        contentWidth: 'fluid',
        fixedHeader: true,
        fixedSidebar: true,
        splitMenus: false,
        colorWeak: false,
        multiTab: true,
        hideFooter: false,
        tableStriped: false,
        tableShowHeader: true,
        sidebarColorfulIcon: false,
      };

      this.layoutSettings = defaultSettings;

      // 清除localStorage（非主题相关）
      Object.keys(defaultSettings).forEach((key) => {
        if (!['primaryColor', 'darkMode', 'colorWeak'].includes(key)) {
          localStorage.removeItem(key);
        }
      });

      // 重置主题（使用主题服务）
      themeService.resetTheme();
    },

    // 导出配置
    exportSettings() {
      const config = {
        layoutSettings: this.layoutSettings,
        systemConfig: this.systemConfig,
        timestamp: new Date().toISOString(),
      };

      const blob = new Blob([JSON.stringify(config, null, 2)], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `bearjia-config-${new Date().toISOString().split('T')[0]}.json`;
      a.click();
      URL.revokeObjectURL(url);
    },

    // 导入配置
    importSettings(config) {
      if (config.layoutSettings) {
        this.updateSettings(config.layoutSettings);
      }
      if (config.systemConfig) {
        this.updateSystemConfig(config.systemConfig);
      }
    },
  },
});
