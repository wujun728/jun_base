import { BASE_URL, TIME_OUT } from './request/config'
import errorCode from '@/utils/errorCode'
import { ElNotification, ElMessageBox, ElLoading } from 'element-plus'
import { getToken } from '@/utils/auth'
import { tansParams, blobValidate } from '@/utils/ruoyi.ts'
import session from '@/utils/hsj/useSession'
import { saveAs } from 'file-saver'
import { RequestConfig } from './request/types'
import type { AxiosRequestConfig } from 'axios'
import BaseAxios from './request/index'

const hideElNotification = [
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
const isHideNotify = (arr: string[], str: string) => {
  return arr.some((item) => str.includes(item))
}
export let isRelogin = { show: false }
let downloadLoadingInstance: ReturnType<typeof ElLoading.service>
const BaseRequest = new BaseAxios({
  baseURL: BASE_URL,
  timeout: TIME_OUT,
  interceptors: {
    requestInterceptor(config) {
      const isToken = (config.headers || {}).isToken === false
      const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
      if (getToken() && !isToken) {
        config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
      }
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
    responseInterceptorCatch(err) {
      return err
    },
    responseInterceptor(res) {
      return new Promise((resolve, reject) => {
        const code = res.data.code || 200
        // 获取错误信息
        const msg = errorCode[code] || res.data.msg || errorCode['default']
        if (
          res.request.responseType === 'blob' ||
          res.request.responseType === 'arraybuffer'
        ) {
          return resolve(res)
        }
        if (
          msg &&
          msg !== '查询成功' &&
          code === 200 &&
          !isHideNotify(hideElNotification, res.request.responseURL) &&
          res.config.method !== 'get'
        ) {
          ElNotification({
            type: 'success',
            message: msg,
          })
        }

        if (code === 401) {
          if (!isRelogin.show) {
            isRelogin.show = true
            ElMessageBox.confirm(
              '登录状态已过期，您可以继续留在该页面，或者重新登录',
              '系统提示',
              {
                confirmButtonText: '重新登录',
                cancelButtonText: '取消',
                type: 'warning',
              }
            )
              .then(() => {
                isRelogin.show = false
              })
              .catch(() => {
                isRelogin.show = false
              })
          }
          return reject('无效的会话，或者会话已过期，请重新登录。')
        } else if (code === 500) {
          ElNotification({
            type: 'error',
            duration: 4000,
            message: msg ?? '后端 500 报错',
          })
          return reject(`msg:${msg},code:${code}`)
        } else if (code === 601) {
          ElNotification({
            type: 'warning',
            duration: 4000,
            message: msg ?? '后端 601 报错',
          })
          return reject(new Error(msg))
        } else if (code !== 200) {
          ElNotification.error({
            title: msg,
          })
          return reject(res)
        } else {
          return resolve(res)
        }
      })
    },
    requestInterceptorCatch(error) {
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
          ElNotification({
            type: 'error',
            duration: 4000,
            message: message ?? '304',
          })
        }
      }
      return Promise.reject(error)
    },
  },
})
export const request = <T = any>(config: RequestConfig) => {
  return BaseRequest.request<T>(config)
}
export default BaseRequest
// 通用下载方法
export function download(
  url: string,
  data: anyObj,
  filename: string,
  config?: AxiosRequestConfig<any>
) {
  downloadLoadingInstance = ElLoading.service({
    text: '正在下载数据，请稍候',
    background: 'rgba(0, 0, 0, 0.7)',
  })
  return BaseRequest.post<BlobPart>({
    url,
    data,
    transformRequest: [
      (data: any) => {
        return tansParams(data)
      },
    ],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config,
  })
    .then(async (data) => {
      const isBlob = await blobValidate(data)
      if (isBlob) {
        const blob = new Blob([data])
        saveAs(blob, filename)
      } else {
        ElNotification({
          type: 'error',
          duration: 4000,
          message: 'blob类型检测错误',
        })
      }
      downloadLoadingInstance.close()
    })
    .catch((r) => {
      console.error(r)
      ElNotification({
        type: 'error',
        duration: 4000,
        message: '下载文件出现错误，请联系管理员！',
      })
      downloadLoadingInstance.close()
    })
}
