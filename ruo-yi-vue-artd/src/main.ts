import { createApp } from 'vue'

import App from './App.vue'
import { setupGlobDirectives } from './directives'
import language from './locales' // 国际化
import { initRouter } from './router' // Router

import '@styles/core/tailwind.css' // tailwind
import '@styles/index.scss' // 样式
import '@utils/sys/console' // 控制台输出内容
import { initStore } from './store' // Store
import { setupErrorHandle } from './utils/sys/error-handle'

document.addEventListener('touchstart', function () {}, { passive: false })

const app = createApp(App)
initStore(app)
initRouter(app)
setupGlobDirectives(app)
setupErrorHandle(app)

app.use(language)
app.mount('#app')
