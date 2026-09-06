import type {
  CreateAxiosDefaults,
  AxiosInterceptorOptions,
  AxiosRequestConfig,
  InternalAxiosRequestConfig,
} from 'axios'
export interface Interceptors<V = any> {
  requestInterceptor?:
    | ((
        value: InternalAxiosRequestConfig
      ) => InternalAxiosRequestConfig | Promise<InternalAxiosRequestConfig>)
    | null
  requestInterceptorCatch?: ((error: any) => any) | null
  requestOptions?: AxiosInterceptorOptions
  responseInterceptor?: ((value: V) => V | Promise<V>) | null
  responseInterceptorCatch?: ((value: V) => V | Promise<V>) | null
}
export interface Config extends CreateAxiosDefaults {
  interceptors?: Interceptors
}
export interface RequestConfig extends AxiosRequestConfig {
  interceptors?: Interceptors
}
