// with polyfills


import 'core-js/stable'
import 'regenerator-runtime/runtime'

import Vue from 'vue'
import Cookies from 'js-cookie'

import App from './App.vue'
import router from './router'
import permission from './directive/permission'
import store from './store/'
import i18n from './locales'
import { VueAxios } from './utils/request'
import ProLayout, { PageHeaderWrapper } from '@/components/ProLayout'
import FooterToolBar from '@/components/FooterToolbar'
import themePluginConfig from '../config/themePluginConfig'


import echarts from 'echarts'
// 全局定义echarts
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'
Vue.component('v-chart', ECharts)

// mock
// WARNING: `mockjs` NOT SUPPORT `IE` PLEASE DO NOT USE IN `production` ENV.
// import './mock'

import '@/assets/styles/antv-theme.less'
import bootstrap from './core/bootstrap'
import './core/lazy_use' // use lazy load components
import './permission' // permission control
import './utils/filter' // global filter
import draggable from '@/utils/drag'
import './global.less' // global style
import { getDicts, getAllDicts } from '@/api/system/dict/data'
import { getConfigKey } from '@/api/system/config'
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, download, downloadTask, downloadByPath, handleTree, appendTreeNode, removeTreeNode, expandTree } from '@/utils/aidex'
import Highlight from './utils/highlight'
import expand from '@/utils/expand'
import Avue from '@smallwei/avue'
import '@smallwei/avue/lib/index.css'
Vue.use(Avue)
// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getAllDicts = getAllDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.downloadTask = downloadTask
Vue.prototype.downloadByPath = downloadByPath
Vue.prototype.handleTree = handleTree
Vue.prototype.appendTreeNode = appendTreeNode
Vue.prototype.removeTreeNode = removeTreeNode
Vue.prototype.expandTree = expandTree
Vue.config.productionTip = false

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: 'success' })
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: 'error' })
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg)
}
let loadingInstance
import { Loading } from 'element-ui'
Vue.prototype.showLoading = function () {
  loadingInstance = Loading.service({ fullscreen: true })
}
Vue.prototype.hideLoading = function () {
  loadingInstance.close()
}

// mount axios to `Vue.$http` and `this.$http`
Vue.use(VueAxios)
// use pro-layout components
Vue.component('pro-layout', ProLayout)
Vue.component('page-container', PageHeaderWrapper)
Vue.component('page-header-wrapper', PageHeaderWrapper)
Vue.component('footer-tool-bar', FooterToolBar)

Vue.use(permission)
Vue.use(Highlight)
Vue.use(draggable)
window.umi_plugin_ant_themeVar = themePluginConfig.theme
// Vue.mixin(expand)
new Vue({
  router,
  store,
  i18n,
  // init localstorage, vuex
  created: bootstrap,
  render: h => h(App)
}).$mount('#app')
