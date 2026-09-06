
import request, { download } from '@/utils/request'
type Nullish<T> = {
    [P in keyof T]: T[P] | null;
};
export type RequestPageType<T> = Nullish<T> & {
    pageNum: number,
    pageSize: number,
    params: { [key: string]: string }
}
export type ResponseResultType<T> = {
    code: number
    data: T
    msg: string
}
export type ResponseTableType<T> = {
    code: number
    msg: string
    rows: Array<T>
    total: number
}
export type ApiServiceType<T> = {
    list: (query: RequestPageType<T>) => Promise<ResponseTableType<T>>
    get: (id: number | string) => Promise<ResponseResultType<T>>
    add: (data: T) => Promise<ResponseResultType<T>>
    update: (data: T) => Promise<ResponseResultType<T>>
    del: (id: number | string | Array<number | string>) => Promise<ResponseResultType<undefined>>
    export: (query: RequestPageType<T>) => Promise<void>
}
export function Page<T>(target: new (...args: any[]) => T): RequestPageType<T> {
    const o: Nullish<T> = new target();
    for (const key in o) {
        o[key] = null
    }
    return {
        pageNum: 1,
        pageSize: 10,
        params: {},
        ...o
    };
}

export function ApiService<T>(target: new (...args: any[]) => T): ApiServiceType<T> {
    return target.prototype.api
}

export default function Api(url: string) {
    const serviceName = url.split('/').pop();
    return function <T>(target: any) {
        const api: ApiServiceType<T> = {
            list: (query: RequestPageType<T>) => request({ url: url + '/list', method: 'get', params: query }),
            get: (id: number | string) => request({ url: url + '/' + id, method: 'get' }),
            add: (data: T) => request({ url, method: 'post', data }),
            update: (data: T) => request({ url, method: 'put', data }),
            del: (id: number | string | Array<number | string>) => request({ url: url + '/' + id, method: 'delete' }),
            export: (query: RequestPageType<T>) => download(url + '/export', query, `${serviceName}_${new Date().getTime()}.xlsx`)
        };
        target.prototype.api = api;
    }
}
