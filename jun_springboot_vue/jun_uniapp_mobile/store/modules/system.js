
import storage from '@/utils/storage'

export const state = {
  systemInfo: null
}

export const mutations = {
  SET_SYSTEM_INFO: (state, value) => {
    state.systemInfo = value
  },
}

export const actions = {
  // 获取用户终端系统信息
  SystemInfo ({ commit, state }) {
    return new Promise((resolve, reject) => {
      if (state.info) {
        resolve(res)
      } else {
        uni.getSystemInfo({
          success (res) {
            commit('SET_SYSTEM_INFO', res)
            // 信息存入缓存（有效期1天）
            storage.set('SYSTEM_INFO', res, 86400);
            resolve(res)
          },
          fail (err) {
            reject(err)
          }
        })
      }
    })
  },
}

export const getters = {

  getSystemInfo (state) {
    if (state.systemInfo) {
      return state.systemInfo
    } else if (storage.get('SYSTEM_INFO')) {
      return JSON.parse(storage.get('SYSTEM_INFO'))
    }
    return {}
  }

}
