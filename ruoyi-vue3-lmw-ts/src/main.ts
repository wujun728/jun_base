// main.ts
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'normalize.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/assets/css/index.scss'
import App from './App.vue'
import { getDialogWidth } from '@/utils/hsj/utils'
import elementIcons from '@/components/SvgIcon/svgicon.ts'
import SvgIcon from '@/components/SvgIcon/index.vue'

import 'virtual:svg-icons-register'
import BaseForm from '@/BaseComponent/BaseForm/index'
import BaseTable from '@/BaseComponent/BaseTable/index'
import PageContent from '@/components/PageContent/index.ts'
import PageSearch from '@/components/PageSearch'
import PageDialog from '@/components/PageDialog'
import DictTag from '@/components/DictTag/index.vue'

import hasPermi from '@/utils/hasPermi'
import store from './store/index'
import route from './router/index'
import directive from './directive/index'
import { parseTime } from '@/utils/hsj/timeFormat'

const app = createApp(App)
app.config.globalProperties.getWidth = getDialogWidth
app.config.globalProperties.hasPermi = hasPermi
app.config.globalProperties.parseTime = parseTime

app.use(store)
app.use(route)
app.use(ElementPlus)
app.use(elementIcons)
app.component('SvgIcon', SvgIcon)
app.component('BaseForm', BaseForm)
app.component('BaseTable', BaseTable)
app.component('PageContent', PageContent)
app.component('PageSearch', PageSearch)
app.component('PageDialog', PageDialog)
app.component('DictTag', DictTag)
directive(app)
app.mount('#app')
