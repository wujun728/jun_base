import storage from 'store'
import { login, loginBySms, getInfo, logout } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const user = {
  state: {
    token: '',
    name: '',
    userType: '',
    welcome: '',
    avatar: '',
    roles: [],
    portalConfigs: [],
    defaultPortal: {},
    info: {},
    platformVersion: '',
    sysNoticeList: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_USER_TYPE: (state, userType) => {
      state.userType = userType
    },
    SET_PORTAL_CONFIG: (state, portalConfigs) => {
      state.portalConfigs = portalConfigs
    },
    SET_DEFAULT_PORTAL: (state, defaultPortal) => {
      state.defaultPortal = defaultPortal
    },
    SET_PLATFORM_VERSION: (state, platformVersion) => {
      state.platformVersion = platformVersion
    },
    SET_NOTICE_LIST: (state, sysNoticeList) => {
      state.sysNoticeList = sysNoticeList
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(res => {
          storage.set(ACCESS_TOKEN, res.token, 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', res.token)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
      })
    },
   // 根据验证码登录
    LoginBySms ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        loginBySms(userInfo).then(res => {
          storage.set(ACCESS_TOKEN, res.token, 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', res.token)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    GetInfo ({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(res => {
          console.log(res)
          const user = res.user
          const avatar = (user.avatar === '' || user.avatar === null) ? require('@/assets/images/profile.jpg') : process.env.VUE_APP_BASE_API + user.avatar
          if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_PORTAL_CONFIG', res.userPortalConfig)
          commit('SET_DEFAULT_PORTAL', res.defaultPortalConfig)
          commit('SET_NAME', user.name)
          commit('SET_AVATAR', avatar)
          commit('SET_USER_TYPE', user.userType)
          commit('SET_PLATFORM_VERSION', res.lincenseInfo)
          commit('SET_NOTICE_LIST', res.sysNoticeList)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          storage.remove(ACCESS_TOKEN)
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
        })
      })
    }

  }
}

export default user
