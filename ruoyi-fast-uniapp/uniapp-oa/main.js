
// #ifndef VUE3
import Vue from 'vue'
import App from './App'

Vue.config.productionTip = false

App.mpType = 'app'

let fontSize = '75px';
const res = uni.getSystemInfoSync();
if(res.screenWidth < 750){//根据屏幕宽度进行适配
      fontSize = 75 * (res.screenWidth / 750) + 'px';
}
Vue.prototype.$fontSize = fontSize;

const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
import App from './App.vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif