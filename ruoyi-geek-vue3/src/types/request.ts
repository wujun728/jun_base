import { AxiosInstance, AxiosPromise, AxiosRequestHeaders, AxiosResponse, Method } from 'axios'

export type GeekRequestConfig = {
    /** 请求地址 */
    url?: string,
    /** get请求映射params参数 */
    method?: Method | string,
    /** 请求数据 */
    data?: any,
    /** get请求映射params参数 */
    params?: any,
    headers?: {
        /** 是否需要防止数据重复提交 */
        repeatSubmit?: boolean,
        /** 是否需要设置 token */
        isToken?: boolean,
    } | AxiosRequestHeaders
}

export type GeekResponse<T = any> = {
    code: number;
    msg: string;
    data: T;
    total: number;
    rows: Array<T>
}

export type GeekResponseForList<T = any> = {
    code: number;
    msg: string;
    total: number;
    rows: Array<T>
}

export type GeekResponseForData<T = any> = {
    code: number;
    msg: string;
    data: T;
}