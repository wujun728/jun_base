import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import lock from './modules/lock'
import dict from './modules/dict'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import settings from './modules/settings'
import getters from './getters'
// OA模块: UI状态 + WangEditor状态
import uiStore from './modules/uiStore'
import wangEditorStore from './modules/wangEditorStore'

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
    // OA模块
    uiStore,
    wangEditorStore
  },
  getters
})

export default store
