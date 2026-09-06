import { BASE_URL, TIME_OUT } from './request/config'
import BaseAxios from './request/index'
import errorCode from '@/utils/errorCode'
import modal from '@/plugins/modal'
import { getToken } from '@/utils/auth'
import { tansParams, blobValidate } from '@/utils/utils.js'
import session from '@/utils/useSession'
import useUserStore from '@/store/modules/user'
import { saveAs } from 'file-saver'

const hideMsg = [
  'changeStatus',
  'refreshCache',
  'cancel',
  'selectAll',
  'unlock',
  '/monitor/online/list',
  '/monitor/cache',
  'synchDb',
  `${BASE_URL}/login`,
  '/updatePwd',
  '/system/user/profile/avatar',
  '/system/user/profile',
  '/logout',
]
const isHideMsg = (arr, str) => {
  return arr.some((item) => str.includes(item))
}

export let isRelogin = { show: false }
const BaseRequest = new BaseAxios({
  baseURL: BASE_URL,
  timeout: TIME_OUT,
  // 单个实例的拦截
  interceptors: {
    // 请求拦截
    requestInterceptor: (config) => {
      // 是否需要设置 token
      const isToken = (config.headers || {}).isToken === false
      // 是否需要防止数据重复提交
      const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
      if (getToken() && !isToken) {
        config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
      }
      // get请求映射params参数
      if (config.method === 'get' && config.params) {
        let url = config.url + '?' + tansParams(config.params)
        url = url.slice(0, -1)
        config.params = {}
        config.url = url
      }
      if (
        !isRepeatSubmit &&
        (config.method === 'post' || config.method === 'put')
      ) {
        const requestObj = {
          url: config.url,
          data:
            typeof config.data === 'object'
              ? JSON.stringify(config.data)
              : config.data,
          time: new Date().getTime(),
        }
        const requestSize = Object.keys(JSON.stringify(requestObj)).length // 请求数据大小
        const limitSize = 5 * 1024 * 1024 // 限制存放数据5M
        if (requestSize >= limitSize) {
          console.warn(
            `[${config.url}]: ` +
              '请求数据大小超出允许的5M限制，无法进行防重复提交验证。'
          )
          return config
        }
        const sessionObj = session.get('sessionObj')
        if (sessionObj) {
          const s_url = sessionObj.url // 请求地址
          const s_data = sessionObj.data // 请求数据
          const s_time = sessionObj.time // 请求时间
          const interval = 1000 // 间隔时间(ms)，小于此时间视为重复提交
          if (
            s_data === requestObj.data &&
            requestObj.time - s_time < interval &&
            s_url === requestObj.url
          ) {
            const message = '数据正在处理，请勿重复提交'
            console.warn(`[${s_url}]: ` + message)
            return Promise.reject(new Error(message))
          } else {
            session.set('sessionObj', requestObj)
          }
        } else {
          session.set('sessionObj', requestObj)
        }
      }
      return config
    },
    requestInterceptorCatch: (error) => {
      return error
    },
    //  响应拦截
    responseInterceptor: (res) => {
      // 未设置状态码则默认成功状态
      const code = res.data.code || 200
      // 获取错误信息
      const msg = errorCode[code] || res.data.msg || errorCode['default']
      // 二进制数据则直接返回
      if (
        res.request.responseType === 'blob' ||
        res.request.responseType === 'arraybuffer'
      ) {
        return Promise.resolve(res.data)
      }
      if (
        msg &&
        msg !== '查询成功' &&
        code === 200 &&
        !isHideMsg(hideMsg, res.request.responseURL) &&
        res.config.method !== 'get'
      ) {
        modal.msgSuccess(msg)
      }

      if (code === 401) {
        if (!isRelogin.show) {
          isRelogin.show = true
          modal.confirm(
            '登录状态已过期，您可以继续留在该页面，或者重新登录',
            '系统提示',
            {
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              onOk: () => {
                isRelogin.show = false
                return useUserStore()
                  .logOut()
                  .then(() => {
                    location.href = '/index'
                  })
              },
              onCancel: () => {
                isRelogin.show = false
              },
            }
          )
        }
        return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
      } else if (code === 500) {
        console.log({ msg })
        modal.msgError(msg ?? '后端 500 报错')
        return Promise.reject(`msg:${msg},code:${code}`)
      } else if (code === 601) {
        modal.msgWarning(msg ?? '后端 601 报错')
        return Promise.reject(new Error(msg))
      } else if (code !== 200) {
        modal.msgError(msg)
        return Promise.reject(res.data)
      } else {
        return Promise.resolve(res.data)
      }
    },
    responseInterceptorCatch: (error) => {
      let { message } = error
      if (message) {
        if (message == 'Network Error') {
          message = '后端接口连接异常'
        } else if (message.includes('timeout')) {
          message = '系统接口请求超时'
        } else if (message.includes('Request failed with status code')) {
          message = '系统接口' + message.substr(message.length - 3) + '异常'
        }
        if (error.response?.status !== 304) {
          modal.msgError(message ?? '304')
        }
      }
      return Promise.reject(error)
    },
  },
})
export default BaseRequest
export const request = (config) => {
  return BaseRequest.request(config)
}

// 通用下载方法
export function download(url, data, filename, config) {
  modal.loading('正在下载数据，请稍候')
  return BaseRequest.post({
    url,
    data,
    transformRequest: [
      (data) => {
        return tansParams(data)
      },
    ],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config,
  })
    .then(async (resData) => {
      const isLogin = await blobValidate(resData)
      if (isLogin) {
        const blob = new Blob([resData])
        saveAs(blob, filename)
      } else {
        const resText = await resData.text()
        const rspObj = JSON.parse(resText)
        const errMsg =
          errorCode[rspObj.code] || rspObj.msg || errorCode['default']
        modal.msgError(errMsg)
      }
    })
    .catch((r) => {
      console.error(r)
      modal.msgError('下载文件出现错误，请联系管理员！')
    })
    .finally(() => {
      modal.closeLoading()
    })
}
