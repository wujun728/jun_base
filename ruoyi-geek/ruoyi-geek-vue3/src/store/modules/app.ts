import { defineStore } from 'pinia';

const useAppStore = defineStore(
  'app',
  {
    state: () => ({
      sidebar: {
        opened: localStorage.getItem('sidebarStatus') ? !!localStorage.getItem('sidebarStatus') : true,
        withoutAnimation: false,
        hide: false
      },
      device: 'desktop' as 'mobile' | 'desktop',
      size: (localStorage.getItem('size') || 'default') as 'large' | 'default' | 'small'
    }),
    actions: {
      /** 切换侧边栏 */
      toggleSideBar(withoutAnimation: boolean) {
        if (this.sidebar.hide) {
          return false;
        }
        this.sidebar.opened = !this.sidebar.opened
        this.sidebar.withoutAnimation = withoutAnimation
        if (this.sidebar.opened) {
          localStorage.setItem('sidebarStatus', '1')
        } else {
          localStorage.setItem('sidebarStatus', '0')
        }
      },
      /** 关闭侧边栏 */
      closeSideBar(withoutAnimation: boolean) {
        localStorage.setItem('sidebarStatus', '0')
        this.sidebar.opened = false
        this.sidebar.withoutAnimation = withoutAnimation
      },
      /** 切换设备类型 */
      toggleDevice(device: 'mobile' | 'desktop') {
        this.device = device
      },
      /** 设置界面尺寸 */
      setSize(size: 'large' | 'default' | 'small') {
        this.size = size;
        localStorage.setItem('size', size)
      },
      /** 切换侧边栏隐藏状态 */
      toggleSideBarHide(status: boolean) {
        this.sidebar.hide = status
      }
    }
  })

export default useAppStore
