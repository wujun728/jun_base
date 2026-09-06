import App from './App.vue'
import plugins from './plugins'
import store from './store'
import uviewPlus from 'uview-plus'


import { createSSRApp } from 'vue'
import directive from './directive' // directive

import { useDict } from '@/utils/dict'
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels } from '@/utils/ruoyi'



export function createApp() {
  const app = createSSRApp(App)
  app.use(store)
  app.use(uviewPlus)
  app.use(plugins)

  // #ifndef MP-WEIXIN
  // 微信小程序中不支持自定义指令
  directive(app)
  // #endif

  // 全局方法挂载
  app.config.globalProperties.useDict = useDict
  app.config.globalProperties.parseTime = parseTime
  app.config.globalProperties.resetForm = resetForm
  app.config.globalProperties.handleTree = handleTree
  app.config.globalProperties.addDateRange = addDateRange
  app.config.globalProperties.selectDictLabel = selectDictLabel
  app.config.globalProperties.selectDictLabels = selectDictLabels

  return {
    app
  }
}
