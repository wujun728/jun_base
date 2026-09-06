import { getConfigKey } from '@/api/system/config'
export type MenuLayout = 'left' | 'mix' | 'top'

export function resolveMenuLayout(menuLayout?: string | null, topNav?: boolean): MenuLayout {
  if (menuLayout === 'left' || menuLayout === 'mix' || menuLayout === 'top') {
    return menuLayout
  }
  return topNav ? 'mix' : 'left'
}

interface Setting {
  title: string;
  theme: string;
  sideTheme: string;
  showSettings: boolean;
  menuLayout: MenuLayout;
  topNav: boolean;
  tagsView: boolean;
  fixedHeader: boolean;
  sidebarLogo: boolean;
  dynamicTitle: boolean;
  footerVisible: boolean;
  footerContent: string;
  errorLog: string | string[];
  initDbSetting: () => Promise<Setting>;
}
const setting = {
  /** 网页标题 */
  title: import.meta.env.VITE_APP_TITLE,
  /** 主题色 */
  theme: '#409EFF',
  /** 侧边栏主题 深色主题theme-dark，浅色主题theme-light */
  sideTheme: 'theme-dark',
  /** 是否系统布局配置 */
  showSettings: false,
  /** 菜单布局模式：left 左侧菜单，mix 混合菜单，top 顶部菜单 */
  menuLayout: 'left' as MenuLayout,
  /** 是否显示顶部导航 */
  topNav: false,
  /** 是否显示 tagsView */
  tagsView: true,
  /** 是否固定头部 */
  fixedHeader: false,
  /** 是否显示logo */
  sidebarLogo: true,
  /** 是否显示动态标题 */
  dynamicTitle: false,
  /** 是否显示底部版权 */
  footerVisible: true,
  /** 底部版权文本内容 */
  footerContent: 'Copyright © 2018-2026 RuoYi-Geek. All Rights Reserved.',

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description 需要显示错误日志组件的环境。
   * 默认只在生产环境中使用。
   * 如果你也想在开发环境中使用，可以传 ['production', 'development']。
   */
  errorLog: 'production',

  /**
   * 获取后端配置的设置
   * @returns Promise<Object>
   */
  async initDbSetting(): Promise<Setting> {
    const config = (key: string, type: any, defaultValue?: any) => getConfigKey(key).then(res => {
      if (type === String) {
        return res.msg
      } else if (type === Number) {
        return +res.msg
      } else if (type === Boolean) {
        return res.msg === 'true'
      } else if (type === Array) {
        return res.msg.split(',')
      } else {
        return new type(res.msg)
      }
    }).catch(() => {
      if (defaultValue !== undefined) {
        return defaultValue
      }
      if (type === String) {
        return ''
      } else if (type === Number) {
        return 0
      } else if (type === Boolean) {
        return false
      } else if (type === Array) {
        return []
      } else {
        return new type()
      }
    })
    const dbTopNav = await config("sys.index.topNav", Boolean)
    const dbMenuLayout = await config("sys.index.menuLayout", String, '')
    return {
      theme: await config("sys.index.theme", String, "#409eff"),
      sideTheme: await config("sys.index.sideTheme", String),
      menuLayout: resolveMenuLayout(dbMenuLayout, dbTopNav),
      topNav: dbTopNav,
      tagsView: await config("sys.index.tagsView", Boolean),
      fixedHeader: await config("sys.index.fixedHeader", Boolean),
      sidebarLogo: await config("sys.index.sidebarLogo", Boolean),
      dynamicTitle: await config("sys.index.dynamicTitle", Boolean),
    } as Setting;
  }
}


export default setting