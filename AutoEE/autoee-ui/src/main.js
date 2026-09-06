import { createApp } from 'vue';
import App from './App.vue';
// 引入src\router\vueRouter.js中定义的vueRouter
import vueRouter from './router/vueRouter.js';
// 引入src\store\vueStore.js中定义的vueStore
import vueStore from './store/vueStore.js';
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import directive from './directive';
import hasPermi from './directive/permission/hasPermi';
import VueHighlightJS from 'vue3-highlightjs';
import 'vue3-highlightjs/styles/solarized-light.css';

// 创建应用
// 将APP.vue挂载到index.html中的app元素上
// 使用router进行路由管理：路由规则定义在src\router\index.js中
// 使用store进行状态管理：状态规则定义在src\store\index.js中
createApp(App).use(vueStore).use(vueRouter).use(Antd).use(directive).use(VueHighlightJS).mount('#app');

import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from '@/utils/ruoyi.js';
