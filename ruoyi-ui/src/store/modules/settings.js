import defaultSettings from '@/settings'

const { sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle } = defaultSettings

const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''
const state = {
  title: '',
  theme: storageSetting.theme || '#409EFF',
  sideTheme: storageSetting.sideTheme || sideTheme,
  showSettings: showSettings,
  topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
  tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
  fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
  sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
  dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle,
  // Sa-Admin 主题系统
  saAdminTheme: localStorage.getItem('saAdminTheme') || 'theme-0'
}
const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  // 修改布局设置
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  },
  // 设置网页标题
  setTitle({ commit }, title) {
    state.title = title
  },
  // 切换 Sa-Admin 主题
  changeSaAdminTheme({ commit }, theme) {
    commit('CHANGE_SETTING', { key: 'saAdminTheme', value: theme })
    localStorage.setItem('saAdminTheme', theme)
    // 应用主题到 body
    const body = document.body
    const classList = body.className.split(' ')
    const filteredClasses = classList.filter(cls => !cls.startsWith('theme-'))
    body.className = [...filteredClasses, theme].join(' ')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

