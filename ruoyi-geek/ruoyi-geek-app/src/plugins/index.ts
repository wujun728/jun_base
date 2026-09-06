import Tab from './tab'
import Auth from './auth'
import Modal from './modal'
import Bus from './bus';
import Socket from './socketclient'
import { App } from 'vue';


export const tab = Tab;
export const auth = Auth;
export const modal = Modal;
export const bus = Bus
export const socket = Socket

/**
 * 在组合式API中可以通过 import { tab, auth, modal } form '@/plugins' 来使用tab、auth、modal
 * 在选项式API中可以通过 this.$tab  this.$auth  this.$modal 来使用tab、auth、modal
 */
export default {
  install(app: App): void {
    // 页面操作
    app.config.globalProperties.$tab = tab
    // 认证对象
    app.config.globalProperties.$auth = auth
    // 模态框对象
    app.config.globalProperties.$modal = modal
    // 全局事件总线
    app.config.globalProperties.$bus = bus
    // socket对象
    app.config.globalProperties.$socket = socket
  }
}
