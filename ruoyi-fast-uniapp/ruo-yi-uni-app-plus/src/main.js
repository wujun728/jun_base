import App from './App.vue'
import plugins from './plugins'
import uviewPlus from 'uview-plus'


import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  app.use(uviewPlus)
  app.use(plugins)
  return {
    app
  }
}
