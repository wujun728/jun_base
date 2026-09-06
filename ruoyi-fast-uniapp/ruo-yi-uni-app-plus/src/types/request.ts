interface BaseRequestConfig {
    headers?: {
        /** 是否在请求头中添加token 默认是 */
        isToken: boolean
        /** 请求是否需要加密 默认否 */
        isEncrypt: boolean;
    },
    /** 请求头配置 */
    header?: any,
    /** 接口路径 */
    url: string,
    /** 请求参数 */
    params?: any,
    /** 超时事件 */
    timeout?: number | undefined,
}
export interface RequestConfig extends BaseRequestConfig {
    /** 请求方式 */
    method?: "OPTIONS" | "GET" | "HEAD" | "POST" | "PUT" | "DELETE" | "CONNECT" | undefined
    /** 重新设置baseUrl */
    baseUrl?: string,
    /** 请求体参数 */
    data?: any
}

export interface RequestUploadConfig extends BaseRequestConfig {
    /** 文件路径 */
    filePath: string,
    /** 文件对应变量名 默认file */
    name?: string,
    /** 其他表单参数 */
    formData: any
}

export interface ResponseData<T> {
    /** 响应码 */
    code: number,
    /** 响应数据 */
    data: any,
    /** 响应信息 */
    msg: string
    /** 图片路径 */
    imgUrl: string,
    /** 数据总数 */
    total:number,
    /** 表格数据 */
    rows:Array<T>,
}
