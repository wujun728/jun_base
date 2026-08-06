import store from '@/store'
import router from '@/router'
import cache from '@/plugins/cache'
import { MessageBox } from 'element-ui'
import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { isHttp, isEmpty } from "@/utils/validate"
import defAva from '@/assets/images/profile.jpg'
import { formatFlatteningRoutes } from '@/utils/system'

const user = {
  state: {
    token: getToken(),
    id: '',
    name: '',
    nickName: '',
    avatar: '',
    roles: [],
    permissions: [],
    menuList: [],
    oneLvMenus: [],
    oneLvRoutes: [],
    childMenuList: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_NICK_NAME: (state, nickName) => {
      state.nickName = nickName
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_MENU_LIST: (state, menuList) => {
      state.menuList = menuList
    },
    setOneLvMenus(state, oneLvMenus) {
      state.oneLvMenus = oneLvMenus
    },
    setOneLvRoute(state, oneLvRoutes) {
      state.oneLvRoutes = oneLvRoutes
    },
    childMenuList(state, list) {
      state.childMenuList = list
    }
  },

  actions: {
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          store.dispatch('lock/unlockScreen')
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          const userData = res.user
          let avatar = userData.avatar || ""
          if (!isHttp(avatar)) {
            avatar = (isEmpty(avatar)) ? defAva : process.env.VUE_APP_BASE_API + avatar
          }
          if (res.roles && res.roles.length > 0) {
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_ID', userData.userId)
          commit('SET_NAME', userData.userName)
          commit('SET_NICK_NAME', userData.nickName)
          commit('SET_AVATAR', avatar)
          cache.session.set('pwrChrtype', res.pwdChrtype)
          if(res.isDefaultModifyPwd) {
            MessageBox.confirm('您的密码还是初始密码，请修改密码！',  '安全提示', {  confirmButtonText: '确定',  cancelButtonText: '取消',  type: 'warning' }).then(() => {
              router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
            }).catch(() => {})
          }
          if(!res.isDefaultModifyPwd && res.isPasswordExpired) {
            MessageBox.confirm('您的密码已过期，请尽快修改密码！',  '安全提示', {  confirmButtonText: '确定',  cancelButtonText: '取消',  type: 'warning' }).then(() => {
              router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
            }).catch(() => {})
          }
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getMenus({ commit }) {
      return new Promise((resolve) => {
        let accessRoutes = store.getters.sidebarRouters || []
        accessRoutes = adaptRuoyiRoutesToMenu(accessRoutes)
        commit('SET_MENU_LIST', accessRoutes)
        let arr = formatFlatteningRoutes(router.options.routes)
        commit('setOneLvMenus', arr)
        let routes = formatFlatteningRoutes(accessRoutes)
        commit('setOneLvRoute', routes)
        resolve(accessRoutes)
      })
    },

    LogOut({ commit, state, dispatch }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          commit('SET_MENU_LIST', [])
          commit('childMenuList', [])
          removeToken()
          dispatch('tagsView/delAllViews', null, { root: true })
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    handleLogout({ dispatch }) {
      return dispatch('LogOut')
    },

    resetToken({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_MENU_LIST', [])
        commit('childMenuList', [])
        removeToken()
        resolve()
      })
    }
  }
}

function adaptRuoyiRoutesToMenu(routes) {
  return routes.filter(item => !item.hidden).map(item => {
    const adapted = {
      path: item.path,
      name: item.name,
      title: (item.meta && item.meta.title) || item.name || '',
      icon: (item.meta && item.meta.icon) || '',
      meta: item.meta || {},
      hidden: item.hidden || false,
      component: item.component
    }
    if (item.children && item.children.length) {
      adapted.children = adaptRuoyiRoutesToMenu(item.children)
    } else {
      adapted.children = []
    }
    return adapted
  })
}

export default user
