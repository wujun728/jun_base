import Tab from './tab'
import Auth from './auth'
import Cache from './cache'
import Modal from './modal'
import Download from './download'
import Bus from './bus';
import Socket from './socketclient'
import type { App } from 'vue'

export const tab = Tab;
export const auth = Auth;
export const modal = Modal;
export const bus = Bus
export const socket = Socket
export const download = Download
export const cache = Cache

export default function installPlugins(app: App<Element>) {
  // 页签操作
  app.config.globalProperties.$tab = tab
  // 认证对象
  app.config.globalProperties.$auth = auth
  // 缓存对象
  app.config.globalProperties.$cache = cache
  // 模态框对象
  app.config.globalProperties.$modal = modal
  // 全局事件总线
  app.config.globalProperties.$bus = bus
  // socket对象
  app.config.globalProperties.$socket = socket
  // 下载文件
  app.config.globalProperties.$download = download
}
