import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { UserState, UserForm } from '@/types/store'
import { Module } from 'vuex'

const baseUrl = config.baseUrl

const user: Module<UserState, UserState> = {
  state: {
    token: getToken(),
    name: storage.get(constant.name),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions)
  },
  mutations: {
    SET_TOKEN: (state, token: string) => {
      state.token = token
    },
    SET_NAME: (state, name: string) => {
      state.name = name
      storage.set(constant.name, name)
    },
    SET_AVATAR: (state, avatar: string) => {
      state.avatar = avatar
      storage.set(constant.avatar, avatar)
    },
    SET_ROLES: (state, roles: Array<string>) => {
      state.roles = roles
      storage.set(constant.roles, roles)
    },
    SET_PERMISSIONS: (state, permissions: Array<string>) => {
      state.permissions = permissions
      storage.set(constant.permissions, permissions)
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo: UserForm) {
      // const username = userInfo.username
      // const password = userInfo.password
      // const code = userInfo.code
      // const uuid = userInfo.uuid
      return new Promise((resolve, reject) => { 
        login(userInfo).then((res: any) => {
		  const data = res.data;
          setToken(data.access_token)
          commit('SET_TOKEN', data.access_token)
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then((res: any) => {
		const data=res.data;
          const user = data.user
          const avatar = (user == null || user.avatar == "" || user.avatar == null) ? "/static/images/profile.jpg" : user.avatar
          const username = (user == null || user.userName == "" || user.userName == null) ? "" : user.userName
          if (data.roles && data.roles.length > 0) {
            commit('SET_ROLES', data.roles)
            commit('SET_PERMISSIONS', data.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', username)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout().then((res) => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          storage.clean()
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
