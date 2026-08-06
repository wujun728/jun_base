import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import lock from './modules/lock'
import dict from './modules/dict'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import settings from './modules/settings'
import themeConfig from './modules/themeConfig'
import menu from './modules/menu'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    lock,
    dict,
    user,
    tagsView,
    permission,
    settings,
    themeConfig,
    menu
  },
  getters
})

export default store
