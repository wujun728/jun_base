import Tab from './tab'
import Auth from './auth'
import Modal from './modal'
import Bus from './bus';
import Socket from './socket'
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
    app.config.globalProperties.$tab = tab
    app.config.globalProperties.$auth = auth
    app.config.globalProperties.$modal = modal
    app.config.globalProperties.$bus = bus
    app.config.globalProperties.$socket = socket
  }
}
