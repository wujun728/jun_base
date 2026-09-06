import { Interceptors } from '@/utils/hsj/service/request/types'

export const interceptorsMes: Interceptors = {
  requestInterceptor(config) {
    return config
  },
  responseInterceptor(data) {
    return data
  },
}
export const interceptorsData: Interceptors = {
  responseInterceptor(res) {
    return res
  },
}
