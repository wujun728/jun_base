import Cookies from 'js-cookie'
import { getLanguage } from '@/lang/index'
const state = {
  sidebar: {
    // Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  device: 'desktop',
  size: Cookies.get('size') || 'medium',
  language: getLanguage(),
  sidebarType: false, // 默认是有二级分类
  parentMenuId: '' || window.sessionStorage.getItem('parentMenuId'),
  sidebarParentCur: null || JSON.parse(localStorage.getItem('sidebarParentCur')), // 父级下标
  defaultOpen: [], // 默认展开的菜单
  isClickTab: false, // 是否顶部导航点击
  menuTabData: JSON.parse(localStorage.getItem('menuTabData')) || {},
  keyWord: '',
  crudList: [] // 表单数据
}

const mutations = {
  TOGGLE_SIDEBAR: (state) => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
      // localStorage.setItem('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
      // localStorage.setItem('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    // localStorage.setItem('sidebarStatus', 0)

    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  // 添加导航展开
  OPEN_SIDEBAR: (state) => {
    // localStorage.setItem('sidebarStatus', 1)
    Cookies.set('sidebarStatus', 1)
    state.sidebar.opened = true
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  SET_SIZE: (state, size) => {
    state.size = size
    Cookies.set('size', size)
  },
  SET_LANGUAGE: (state, language) => {
    state.language = language
    Cookies.set('language', language)
  },
  SET_BARTYPE: (state, data) => {
    state.sidebarType = data
  },
  SETPID: (state, id) => {
    state.parentMenuId = id
    window.sessionStorage.setItem('parentMenuId', id)
  },
  SET_PARENTCUR: (state, index) => {
    state.sidebarParentCur = index
    localStorage.setItem('sidebarParentCur', JSON.stringify(index))
  },
  SET_DEFAULTOPEN: (state, obj) => {
    state.defaultOpen.push(obj)
  },
  SET_CLICK_TAB: (state, status) => {
    state.isClickTab = status
  },
  SET_CLICK_KEY: (state, status) => {
    state.keyWord = status
  },

  SET__MENU_TAB_DATA: (state, menuTabData) => {
    state.menuTabData = menuTabData
    localStorage.setItem('menuTabData', JSON.stringify(menuTabData))
  },
  SET_FORM_LIST_DATA: (state, crudList) => {
    state.crudList = crudList
  },
  SET_FORM_INDEX_SET: (state, data) => {
    // state.crudList[data.pidx].field.splice(data.i, 0, data)
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  openSideBar({ commit }) {
    commit('OPEN_SIDEBAR')
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  setSize({ commit }, size) {
    commit('SET_SIZE', size)
  },
  setLanguage({ commit }, language) {
    commit('SET_LANGUAGE', language)
  },
  setFormList({ commit }, crudList) {
    commit('SET_FORM_LIST_DATA', crudList)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
