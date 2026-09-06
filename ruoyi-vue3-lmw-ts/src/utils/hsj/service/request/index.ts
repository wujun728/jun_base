import axios from 'axios'
import type { AxiosInstance } from 'axios'
import { Interceptors, Config, RequestConfig } from './types'

class BaseAxios {
  instance: AxiosInstance
  interceptors?: Interceptors
  constructor(config: Config) {
    this.instance = axios.create(config)
    this.interceptors = config.interceptors
    this.instance.interceptors.request.use(
      this.interceptors?.requestInterceptor,
      this.interceptors?.requestInterceptorCatch,
      this.interceptors?.requestOptions
    )
    this.instance.interceptors.response.use(
      this.interceptors?.responseInterceptor,
      this.interceptors?.responseInterceptorCatch
    )
  }
  request<T = any>(config: RequestConfig): Promise<T> {
    return new Promise((resolve, reject) => {
      if (config.interceptors?.requestInterceptor) {
        // @ts-ignore
        config = config.interceptors.requestInterceptor(config)
      }
      this.instance
        .request<T>(config)
        .then((res) => {
          if (config.interceptors?.responseInterceptor) {
            res = config.interceptors.responseInterceptor(res)
          }
          resolve(res.data)
        })
        .catch((err) => {
          reject(err)
        })
    })
  }
  get<T = any>(config: RequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'GET' })
  }
  post<T = any>(config: RequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'POST' })
  }
  delete<T = any>(config: RequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'DELETE' })
  }
  patch<T = any>(config: RequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'PATCH' })
  }
  put<T = any>(config: RequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'PUT' })
  }
}

export default BaseAxios
