import { createApp } from 'vue'
import Antd from 'ant-design-vue'
import App from './App'
import 'ant-design-vue/dist/reset.css'
import 'normalize.css'
import 'virtual:svg-icons-register'
import './assets/css/index.scss'
import router from './router'
import store from './store'
import SvgIcon from '@/components/SvgIcon/index.vue'
import AntIcon from '@/components/SvgIcon/antIcon'
import { getDialogWidth } from '@/utils/utils'
import { parseTime } from '@/utils/timeFormat'
import hasPermi from '@/utils/hasPermi'
import BaseForm from '@/BaseComponent/BaseForm/index'
import BaseTable from '@/BaseComponent/BaseTable/index'
import PageContent from '@/components/PageContent/index'
import PageDialog from '@/components/PageDialog/index'
import PageSearch from '@/components/PageSearch/index'
import DictTag from '@/components/DictTag/index'
import directive from '@/directive/index'

const app = createApp(App)
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.getWidth = getDialogWidth
app.config.globalProperties.hasPermi = hasPermi

app.component('SvgIcon', SvgIcon)
app.component('BaseForm', BaseForm)
app.component('BaseTable', BaseTable)
app.component('PageContent', PageContent)
app.component('PageDialog', PageDialog)
app.component('PageSearch', PageSearch)
app.component('DictTag', DictTag)

app.use(AntIcon)
app.use(Antd)
app.use(router)
app.use(store)
directive(app)

app.mount('#app')
