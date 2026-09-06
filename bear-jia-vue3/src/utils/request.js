// @ts-check

import axios from 'axios';
import { saveAs } from 'file-saver';
import { useUserStore } from '@/stores/user';
import { getToken } from '@/utils/auth';
import errorCode from '@/utils/errorCode';
import { notification } from 'ant-design-vue';
import { blobValidate } from '@/utils/bearjia';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

axios.defaults.headers.common['Content-Type'] = 'application/json;charset=utf-8';
const API_BASE_URL =
  import.meta.env.VITE_APP_BASE_API ||
  import.meta.env.VITE_API_BASE_URL ||
  import.meta.env.VITE_API_DOMAIN ||
  '/api';
let isSessionExpiredHandling = false;
const REQUEST_ERROR_DEDUP_MS = 1200;
const REQUEST_ERROR_DEDUP_MAX_KEYS = 200;
const requestErrorNotificationTimestamps = new Map();

/**
 * 标记错误已被业务层处理，避免全局 unhandledrejection 重复提示
 * @param {any} error
 * @returns {any}
 */
const markHandledError = (error) => {
  if (error && (typeof error === 'object' || typeof error === 'function')) {
    try {
      error._handled = true;
    } catch {
      // ignore: 某些错误对象可能不可扩展
    }
  }
  return error;
};

/**
 * @param {{ message: string, description: string, duration?: number }} options
 */
const notifyRequestError = ({ message, description, duration = 3 }) => {
  if (requestErrorNotificationTimestamps.size > REQUEST_ERROR_DEDUP_MAX_KEYS) {
    const now = Date.now();
    for (const [key, timestamp] of requestErrorNotificationTimestamps.entries()) {
      if (now - timestamp > REQUEST_ERROR_DEDUP_MS) {
        requestErrorNotificationTimestamps.delete(key);
      }
    }
  }

  const dedupeKey = `${message}::${description}`;
  const now = Date.now();
  const lastShownAt = requestErrorNotificationTimestamps.get(dedupeKey) || 0;
  if (now - lastShownAt < REQUEST_ERROR_DEDUP_MS) {
    return;
  }
  requestErrorNotificationTimestamps.set(dedupeKey, now);
  notification.error({
    message,
    description,
    duration,
  });
};

// 创建axios实例
/** @type {import('@/types/request').RequestInstance} */
const service = /** @type {any} */ (
  axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: API_BASE_URL,
    // 超时
    timeout: 10000,
  })
);
// 白名单接口，不需要token验证
const whiteList = [
  '/captchaImage', // 验证码接口
  '/login', // 登录接口
];

// request拦截器
service.interceptors.request.use(
  (config) => {
    const headers = /** @type {any} */ (config.headers);
    const token = getToken();
    if (token) {
      headers['Authorization'] = 'Bearer ' + token;
    }
    // 判断是否为白名单接口
    const requestUrl = config.url || '';
    const isWhiteList = whiteList.some((url) => requestUrl.includes(url));
    // 是否需要设置 token
    const isToken = headers?.isToken === false;
    if (!isWhiteList && !isToken && !token) {
      return Promise.reject(markHandledError(new Error('未登录或登录已过期')));
    }
    // get请求映射params参数
    if (config.method === 'get' && config.params) {
      let url = config.url + '?';
      for (const propName of Object.keys(config.params)) {
        const value = config.params[propName];
        var part = encodeURIComponent(propName) + '=';
        if (value !== null && typeof value !== 'undefined') {
          if (typeof value === 'object') {
            for (const key of Object.keys(value)) {
              if (value[key] !== null && typeof value[key] !== 'undefined') {
                let params = propName + '[' + key + ']';
                let subPart = encodeURIComponent(params) + '=';
                url += subPart + encodeURIComponent(value[key]) + '&';
              }
            }
          } else {
            url += part + encodeURIComponent(value) + '&';
          }
        }
      }
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }
    return config;
  },
  (error) => {
    console.error(error);
    return Promise.reject(markHandledError(error));
  }
);

// 响应拦截器
service.interceptors.response.use(
  (res) => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;
    // 获取错误信息
    const msg = errorCode[code] || res.data.msg || errorCode['default'];

    if (code === 401) {
      const userStore = useUserStore();
      if (!isSessionExpiredHandling) {
        isSessionExpiredHandling = true;
        notifyRequestError({
          message: '系统提示',
          description: '登录状态已过期，请重新登录',
          duration: 3,
        });
        userStore.logout().finally(() => {
          isSessionExpiredHandling = false;
          const redirect = encodeURIComponent(
            `${window.location.pathname}${window.location.search}`
          );
          window.location.href = `/login?redirect=${redirect}`;
        });
      }
      return Promise.reject(
        markHandledError(new Error('无效的会话，或者会话已过期，请重新登录。'))
      );
    } else if (code === 500) {
      notifyRequestError({
        message: '系统提示',
        description: msg,
        duration: 3,
      });
      return Promise.reject(markHandledError(new Error(msg)));
    } else if (code !== 200) {
      notifyRequestError({
        message: '系统提示',
        description: msg,
        duration: 3,
      });
      return Promise.reject(markHandledError(new Error(msg)));
    } else {
      return res.data;
    }
  },
  (error) => {
    console.error('err' + error);
    let message = error?.message || '';
    if (message === 'Network Error') {
      message = '后端接口连接异常';
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时';
    } else if (message.includes('Request failed with status code')) {
      message = '系统接口' + message.slice(-3) + '异常';
    }
    notifyRequestError({
      message: '错误',
      description: message,
      duration: 3,
    });
    return Promise.reject(markHandledError(error));
  }
);

// 通用下载方法
/**
 * @param {string} url
 * @param {Record<string, any>} params
 * @param {string} filename
 * @returns {Promise<void>}
 */
export function download(url, params, filename) {
  BearJiaUtil.messageInfo('开始下载数据，请稍候...');
  return service
    .get(url, {
      params,
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob',
    })
    .then(async (data) => {
      const isLogin = await blobValidate(data);
      if (isLogin) {
        const blob = new Blob([data]);
        saveAs(blob, filename);
      } else {
        const resText = await data.text();
        const rspObj = JSON.parse(resText);
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default'];
        notifyRequestError({
          message: '请求后端服务失败',
          description: errMsg,
          // duration: null,
        });
      }
    })
    .catch((error) => {
      console.error(error);
      const isHandledError = Boolean(error && typeof error === 'object' && error._handled);
      if (isHandledError) {
        return;
      }
      notifyRequestError({
        message: '请求后端服务失败',
        description: '下载文件出现错误，请联系管理员！',
        // duration: null,
      });
    });
}

export default service;
