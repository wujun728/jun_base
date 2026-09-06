// https://next.vuex.vuejs.org/zh/
// 
// ├── index.html
// ├── main.js
// ├── api
// │   └── ... # 抽取出API请求
// ├── components
// │   ├── App.vue
// │   └── ...
// └── store
//     ├── index.js          # 我们组装模块并导出 store 的地方
//     ├── actions.js        # 根级别的 action
//     ├── mutations.js      # 根级别的 mutation
//     └── modules
//         ├── cart.js       # 购物车模块
//         └── products.js   # 产品模块

import { createStore } from 'vuex'
import app from './modules/app'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
// import settings from './modules/settings'
import getters from './getters'


const vueStore =  createStore({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    app,
    user,
    tagsView,
    permission,
    // settings
  },
  getters
});

export default vueStore;
