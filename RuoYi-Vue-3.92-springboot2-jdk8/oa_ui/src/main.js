import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css' // a modern alternative to CSS resets
// 邀请用户加入企业
if (window.location.href.indexOf('&invitation=') > -1) {
  const len = window.location.href.indexOf('&invitation=')
  const invitation = window.location.href.substr(len + 12)
  const entId = window.location.href.substring(window.location.href.indexOf('?enterprise=') + 12, len)
  const obj = {
    invitation,
    entId
  }
  if (localStorage.getItem('invitationStorage') !== null) localStorage.removeItem('invitationStorage')
  localStorage.setItem('invitationStorage', JSON.stringify(obj))
}
// 懒加载
import VueLazyload from 'vue-lazyload'
import moment from 'moment'
import Element from 'element-ui'
import cascader from 'element-ui/lib/cascader'
import './styles/element-variables.scss'
import '@/styles/index.scss' // global css
import '@/styles/ruoyi.scss' // ruoyi css（若依系统管理页）
import '@/icons/iconfont.css'
import '@/icons/iconfont/iconfont.css'
import '@/icons/iconfont/icon'
import App from './App'
import store from './store'
import router from './router'
import i18n from './lang' // internationalization
import { formCreate } from '@/views/business/components/formSetting/components/form-create-designer/src/index'
import uploadPicture from './components/uploadPicture/uploadFrom'
import VueUeditorWrap from 'vue-ueditor-wrap'
import nodeWrap from '@/components/workFlow/nodeWrap'
import addNode from '@/components/workFlow/addNode'
import height from '@/directive/height'
import '@/assets/iconfont/iconfont.css' // iconfont
import './icons' // icon
import './permission' // permission control
import modalForm from '@/libs/modal-form'
import { modalSure } from '@/libs/public'
import * as filters from './filters'
import notice from '@/libs/notice'
import ElementResizeDetectorMaker from 'element-resize-detector'
import './utils/directive'
import VueSmartWidget from 'vue-smart-widget'
import VueClipboard from 'vue-clipboard2' // 复制到粘贴板插件
import { processResourceUrl } from "@/utils/resourceUtil"

import { loadChartsExtension } from '@/views/system/dashboard-design/charts/charts-loader'
import "@/utils/watermark.util"

loadChartsExtension(Vue)

// bus
import { EventBus } from '@/libs/bus'
// import height from '@/directive/height'
import directive from '@/directive'
import vResize from '@theshy/v-resize'
Vue.use(vResize)
// import { loadExtension } from '@/extension/extension-loader'
// loadExtension()
// 引入外部表格；
import 'xe-utils'
Vue.filter('dateformat', function (dataStr, pattern = 'YYYY-MM-DD') {
  if (dataStr) {
    return moment(dataStr).format(pattern)
  } else {
    return dataStr
  }
})
//设置时间搜索快速选择
import pickerOptions from '@/libs/pickerOptions'
//全局注册，和component的区别是接收的参数必须有install方法
Vue.use(uploadPicture)
Vue.use(formCreate)
Vue.use(height)

VueClipboard.config.copyText = true // 复制到粘贴板插件
Vue.use(VueClipboard)
//全局注册组件
Vue.component('vue-ueditor-wrap', VueUeditorWrap)
Vue.component('nodeWrap', nodeWrap) // 初始化组件
Vue.component('addNode', addNode) // 初始化组件
Vue.use(VueLazyload, {
  preLoad: 1.3,
  error: require('./assets/images/no.png'),
  loading: require('./assets/images/moren.jpg'),
  attempt: 1,
  listenEvents: ['scroll', 'wheel', 'mousewheel', 'resize', 'animationend', 'transitionend', 'touchmove']
})

//注册的全局的变量以$开头，调用this方法调用
Vue.prototype.$modalForm = modalForm
Vue.prototype.$modalSure = modalSure
Vue.prototype.$bus = EventBus
Vue.prototype.$vue = Vue
Vue.prototype.$moment = moment
Vue.prototype.$erd = ElementResizeDetectorMaker()
Vue.prototype.$pickerOptionsTimeEle = pickerOptions
// Vue.prototype.tableHeight = document.documentElement.clientHeight - 269 + 'px'
Vue.prototype.tableHeight ='calc(100vh - 269px)'
Vue.prototype.$processResourceUrl = processResourceUrl

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  zIndex: 1000,
  i18n: (key, value) => i18n.t(key, value)
})
Vue.use(cascader)

// register global utility filters
Object.keys(filters).forEach((key) => {
  Vue.filter(key, filters[key])
})

// ===== 若依系统管理页全局注册（从 scrm_ui 移植）=====
import Pagination from '@/components/Pagination'
import RightToolbar from '@/components/RightToolbar'
import DictTag from '@/components/DictTag'
import DictData from '@/components/DictData'
import { download } from '@/utils/request'
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from '@/utils/ruoyi'
import { getDicts } from '@/api/system/dict/data'
import { getConfigKey } from '@/api/system/config'
import plugins from './plugins'

Vue.use(plugins)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('DictTag', DictTag)
Vue.component('svg-icon', () => import('@/components/svg-icon/index.vue'))
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.handleTree = handleTree
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.download = download
DictData.install()

// ===== /若依系统管理页全局注册 =====

const token = store.getters.token
let _notice
if (token) {
  _notice = notice(token)
}

var _hmt = _hmt || [];
(function() {
    var hm = document.createElement("script");
    hm.src = "https://cdn.oss.9gt.net/js/es.js?version=tuoluojiangv2.0";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
})()

 
// Vue.use(height)
Vue.use(directive)
Vue.use(VueSmartWidget)
Vue.config.productionTip = false


export default new Vue({
  el: '#app',
  router,
  data: {
    notice: _notice
  },
  methods: {
    closeNotice() {
      this.notice && this.notice()
    }
  },
  store,
  i18n,
  render: (h) => h(App)
})
