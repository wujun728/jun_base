import type {AxiosRequestConfig, AxiosResponse} from 'axios';
import axios from 'axios';
import {Message, Modal} from '@arco-design/web-vue';
import {useUserStore} from '@/store';
import {getToken} from '@/utils/auth';
import {tansParams} from "@/utils/common";
import errorCode from "@/utils/error-code";
import cache from '@/plugins/cache'

export interface HttpResponse<T = unknown> {
    msg: string;
    code: number;
    data?: T;
    rows?: T;
}

if (import.meta.env.VITE_APP_BASE_API) {
    axios.defaults.baseURL = import.meta.env.VITE_APP_BASE_API;
}

axios.interceptors.request.use(
    (config: AxiosRequestConfig) => {
        // let each request carry token
        // this example using the JWT token
        // Authorization is a custom headers key
        // please modify it according to the actual situation
        const token = getToken();
        if (!config.headers) {
            config.headers = {};
        }
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        // 是否需要设置 token
        const {isToken} = config.headers;
        // 是否需要防止数据重复提交
        const isRepeatSubmit = config.headers.repeatSubmit;
        if (getToken() && !isToken) {
            config.headers.Authorization = `Bearer ${token}`; // 让每个请求携带自定义token 请根据实际情况自行修改
        }
        // Arco列表接收分页参数转换
        if (config.params && config.params.current) {
            config.params.pageNum = config.params.current
        }
        if (config.params && config.params.isAsc) {
            config.params.isAsc = `${config.params.isAsc}ing`;
        }
        // get请求映射params参数
        if (config.method === 'get' && config.params) {
            let url = `${config.url}?${tansParams(config.params)}`;
            url = url.slice(0, -1);
            config.params = {};
            config.url = url;
        }
        if (config.params)
            // 检测重复提交
            if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
                const requestObj = {
                    url: config.url,
                    data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
                    time: new Date().getTime(),
                };
                const sessionObj = cache.session.getJSON('sessionObj');
                if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
                    cache.session.setJSON('sessionObj', requestObj);
                } else {
                    const sUrl = sessionObj.url;                  // 请求地址
                    const sData = sessionObj.data;                // 请求数据
                    const sTime = sessionObj.time;                // 请求时间
                    const interval = 1000;                         // 间隔时间(ms)，小于此时间视为重复提交
                    if (sData === requestObj.data && requestObj.time - sTime < interval && sUrl === requestObj.url) {
                        const message = '数据正在处理，请勿重复提交';
                        console.warn(`[${sUrl}]: ${message}`);
                        return Promise.reject(new Error(message));
                    }
                    cache.session.setJSON('sessionObj', requestObj);
                }
            }
        return config;
    },
    (error) => {
        // do something
        return Promise.reject(error);
    }
);
// add response interceptors
axios.interceptors.response.use(
    (res: AxiosResponse<HttpResponse>) => {
        // 未设置状态码则默认成功状态
        const code = res.data.code || 200;
        // 获取错误信息
        // @ts-ignore
        const msg = errorCode[code] || res.data.msg || errorCode.default
        // 二进制数据则直接返回
        if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
            return res.data
        }
        // if the custom code is not 20000, it is judged as an error.
        if (code !== 200) {
            Message.error({
                content: msg || 'Error',
                duration: 5 * 1000,
            });
            // 508: Illegal token; 512: Other clients logged in; 514: Token expired;
            if (
                [508, 512, 514, 401].includes(code) &&
                res.config.url !== '/api/user/info'
            ) {
                Modal.error({
                    title: 'Confirm logout',
                    content:
                        'You have been logged out, you can cancel to stay on this page, or log in again',
                    okText: 'Re-Login',
                    async onOk() {
                        const userStore = useUserStore();
                        await userStore.logout();
                        window.location.reload();
                    },
                });
            }
            return Promise.reject(new Error(msg || 'Error'));
        }
        return res.data;
    },
    (error) => {
        Message.error({
            content: error.msg || 'Request Error',
            duration: 5 * 1000,
        });
        return Promise.reject(error);
    }
);
