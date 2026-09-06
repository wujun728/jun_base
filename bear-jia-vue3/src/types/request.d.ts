import type { AxiosInstance, AxiosRequestConfig } from 'axios';

/**
 * Axios instance after response interceptor normalization.
 *
 * The project intercepts responses and returns `res.data` instead of the full `AxiosResponse`.
 * This type models that behavior so API modules can describe `Promise<T>` results.
 */
export interface RequestInstance {
  <T = any, D = any>(config: AxiosRequestConfig<D>): Promise<T>;
  request<T = any, D = any>(config: AxiosRequestConfig<D>): Promise<T>;

  get<T = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<T>;
  delete<T = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<T>;
  head<T = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<T>;
  options<T = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<T>;

  post<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;
  put<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;
  patch<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;

  defaults: AxiosInstance['defaults'];
  interceptors: AxiosInstance['interceptors'];
}

