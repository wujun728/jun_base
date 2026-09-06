import { ACCESS_TOKEN } from '@/store/mutation-types'
import storage from '@/utils/storage'
import * as LoginApi from '@/api/login'
import * as UserApi from '@/api/user'

// 登陆成功后执行
const loginSuccess = (commit, { token }) => {
  // 过期时间30天
  const expiryTime = 30 * 86400
  // 保存tokne和userId到缓存
  storage.set(ACCESS_TOKEN, token, expiryTime)
  // 记录到store全局变量
  commit('SET_TOKEN', token)
}

export const state = {
  // 用户认证token
  token: '',
  // 用户信息
  userInfo: null
}

export const mutations = {
  SET_TOKEN: (state, value) => {
    state.token = value
  },
  SET_USER: (state, value) => {
    state.userInfo = value
  },
}

export const actions = {

  // 用户登录(普通登录: 输入账号、密码和验证码)
  Login({ commit }, data) {
    return new Promise((resolve, reject) => {
      LoginApi.login(data, { custom: { catch: true } }).then(response => {
          const result = response;
          loginSuccess(commit, result)
          resolve(response)
        }).catch(reject)
    })
  },

  // 用户信息
  Info({ commit, state }) {
    return new Promise((resolve, reject) => {
      if (state.userInfo) {
        return resolve(state.userInfo)
      }
      UserApi.getInfo().then(response => {
        const result = response;
        commit('SET_USER', result)
        resolve(response)
      }).catch(reject)
    })
  },

  // 退出登录
  Logout({ commit }, data) {
    return new Promise((resolve, reject) => {
      LoginApi.logout(data, { custom: { catch: true } }).then(response => {
        storage.remove(ACCESS_TOKEN)
        commit('SET_TOKEN', '')
        resolve(response)
      }).catch(reject)
    })
  }

}
