import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false;


// 安装 element-ui 
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

// 安装jquery与layer, (cdn方式引入的直接挂载到原型)
Vue.prototype.$ = window.$;
Vue.prototype.layer = window.layer;

// 安装sa对象
import sa from './static/sa.js';
Vue.prototype.sa = sa;
import './static/sa.css';

// 安装sa_admin初始化方法
import SaAdminInIt from './sa-resources/sa-admin-init.js';
Vue.prototype.SaAdminInIt = SaAdminInIt;


// 打开vue 
new Vue({
	render: h => h(App)
}).$mount('#app')
