import Theme from 'vitepress/theme'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import './custom.scss' // 引入自定义样式
import 'virtual:svg-icons-register'
import 'normalize.css'
import BaseForm from './components/BaseForm/Form.vue'
import BaseTable from './components/BaseTable/Table.vue'
import SvgIcon from './components/Global/SvgIcon/index.vue'
import 'virtual:group-icons.css'

export default {
  extends: Theme,
  enhanceApp({ app }) {
    app.use(ElementPlus, {
      locale: zhCn,
    })
    app.component('BaseForm', BaseForm)
    app.component('BaseTable', BaseTable)
    app.component('SvgIcon', SvgIcon)
    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
      app.component(key, component)
    }
  },
}
