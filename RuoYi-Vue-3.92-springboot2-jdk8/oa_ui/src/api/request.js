import axios from 'axios'
import store from '@/store'
import router from '../router'
import SettingMer from '@/libs/settingMer'
import Tips from '@/utils/tips'
import { roterPre } from '@/settings'
const instance = axios.create({
  baseURL: SettingMer.https,
  timeout: 60000
})
const defaultOpt = { login: true }

function baseRequest(options) {
  const token = store.getters.token
  const unique = localStorage.getItem('unique')
  const headers = options.headers || {}
  const lang = store.getters.lang || 'zh-cn'
  if (token) {
    headers['Authorization'] = 'Bearer ' + token
  }
  

  if (unique && router.app._route&& router.app._route.name == 'share') {
    headers['Curd-Unique'] = unique
  }
  headers['laravel_lang'] = lang
  options.headers = headers

  // 后期修改成这个方法
  return new Promise((resolve, reject) => {
    instance(options)
      .then((res) => {
        const data = res.data || {}
        // 若依返回 {code,msg,data}，归一化为 OA 期望的 {status,...}
        if (data.code !== undefined && data.status === undefined) {
          data.status = data.code
          if (data.msg !== undefined && data.message === undefined) {
            data.message = data.msg
          }
        }
        if (res.status !== 200) {
          return reject({ message: '请求失败', res, data })
        } else if ([410000, 410001, 410002, 40000, 410003, 401, 403].indexOf(data.status) !== -1) {
          store.dispatch('user/resetToken').then(() => {
            // 遇到401/403/业务错误跳转到登录页面携带参数
            if (location.pathname !== `${roterPre}/login`) {
              location.href = `${roterPre}/login?redirect=${location.pathname}`
              // location.reload()
            }
          })
        } else if (data.status === 200 || data.status === 0) {
          if (data.tips && data.message !== 'ok') {
            Tips.msgSuccess(data.message)
          }
          return resolve(data)
        } else if (data.status === 400) {
          if (data.tips && data.message !== 'error') {
            Tips.msgError(data.message)
          }
          return resolve(data, res)
        } else if (data.status === 410005) {
        } else {
          if (data.message) {
            Tips.msgError(data.message)
          }
          return reject({ message: data.message, res, data })
        }
      })
      .catch((message) => reject({ message }))
  })
}
/**
 * http 请求基础类
 * 参考文档 https://www.kancloud.cn/yunye/axios/234845
 *
 */
const request = ['post', 'put', 'patch', 'delete'].reduce((request, method) => {
  /**
   *
   * @param url string 接口地址
   * @param data object get参数
   * @param options object axios 配置项
   * @returns {AxiosPromise}
   */
  request[method] = (url, data = {}, options = {}) => {
    return baseRequest(Object.assign({ url, data, method }, defaultOpt, options))
  }
  return request
}, {})

;['get', 'head'].forEach((method) => {
  /**
   *
   * @param url string 接口地址
   * @param params object get参数
   * @param options object axios 配置项
   * @returns {AxiosPromise}
   */
  request[method] = (url, params = {}, options = {}) => {
    return baseRequest(Object.assign({ url, params, method }, defaultOpt, options))
  }
})

export default request
