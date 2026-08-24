import zhLang from './lang/zh-CN.js'
import enLang from './lang/en-US.js'
import zhCNExtension from './lang/zh-CN_extension'
import DashboardContainerWidget from './dashboard-container/dashboard-container-widget.vue'
import DashboardContainerItem from './dashboard-container/dashboard-container-item.vue'
import SectionWidget from './section/section-widget.vue'
import SectionItem from './section/section-item.vue'
import { registerChartWidgets } from './chart-widget/index.js'
import { registerChartLibProperties, registerPEWidgets } from '@/views/system/dashboard-design/charts/property-editor'

import {
  addZHExtensionLang,
  addENExtensionLang,
  PERegister
} from '../utils.js'

export const loadChartsExtension = (app) => {
  //加载语言文件
  addZHExtensionLang(zhLang)
  addZHExtensionLang(zhCNExtension)
  addENExtensionLang(enLang)
  registerChartLibProperties(app, PERegister)
  app.component(DashboardContainerWidget.name, DashboardContainerWidget)
  app.component(DashboardContainerItem.name, DashboardContainerItem)
  app.component(SectionWidget.name, SectionWidget) //注册设计期的容器组件
  app.component(SectionItem.name, SectionItem) //注册运行期的容器组件

  registerPEWidgets(app)
  registerChartWidgets(app)
}
