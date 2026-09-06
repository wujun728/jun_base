import { createApp } from 'vue'

import 'element-plus/dist/index.css'
import '@/assets/styles/index.scss' // global css

import App from './App.vue'
const app = createApp(App)

import { download } from '@/utils/request'
import { useDict } from '@/utils/dict'
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels } from '@/utils/ruoyi'
// 全局方法挂载
app.config.globalProperties.useDict = useDict
app.config.globalProperties.download = download
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm
app.config.globalProperties.handleTree = handleTree
app.config.globalProperties.addDateRange = addDateRange
app.config.globalProperties.selectDictLabel = selectDictLabel
app.config.globalProperties.selectDictLabels = selectDictLabels


import router from './router'           // 引入路由
import store from './store'             // 引入状态管理
import plugins from './plugins'         // 引入插件
import directive from './directive'     // 引入指令
import compomemts from './components'   // 引入全局组件
app.use(router).use(store).use(plugins).use(directive).use(compomemts)

import VForm3 from '@lib/vform/designer.umd.js'  //引入VForm 3库
import '@lib/vform/designer.style.css'  //引入VForm3样式
app.use(VForm3)

app.mount('#app')