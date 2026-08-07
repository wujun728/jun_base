import axios from 'axios';
import { MessageBox, Message } from 'element-ui';
import store from '@/store';
import { getToken } from '@/utils/auth';
import { saveAs } from 'file-saver';

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 60000,
});

service.interceptors.request.use(
  (config) => {
    const token = !store.getters.token ? sessionStorage.getItem('token') : store.getters.token;
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    if (config.method === 'get' && config.params) {
      let url = config.url + '?' + tansParams(config.params);
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

function tansParams(params) {
  let result = '';
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    if (value !== null && value !== '' && typeof value !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
            result += encodeURIComponent(propName + '[' + key + ']') + '=' + encodeURIComponent(value[key]) + '&';
          }
        }
      } else {
        result += encodeURIComponent(propName) + '=' + encodeURIComponent(value) + '&';
      }
    }
  }
  return result;
}

service.interceptors.response.use(
  (response) => {
    if (response.config.responseType === 'blob' || response.config.responseType === 'arraybuffer') {
      return response.data;
    }
    const res = response.data;
    if (res.code === 401) {
      Message.error('无效的会话，或者登录已过期，请重新登录。');
      if (window.location.pathname !== '/login') location.href = '/login';
    } else if (res.code === 403) {
      Message.error('没有权限访问。');
    }
    if (res.code !== 200 && res.code !== 0) {
      Message({ message: res.msg || res.message || 'Error', type: 'error', duration: 5 * 1000 });
      return Promise.reject(res);
    }
    return res;
  },
  (error) => {
    Message({ message: error.message, type: 'error', duration: 5 * 1000 });
    return Promise.reject(error);
  },
);

export default service;

export function download(url, params, filename, config) {
  return service.post(url, params, {
    transformRequest: [(p) => tansParams(p)],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config
  }).then((data) => {
    saveAs(data, filename);
  }).catch((r) => {
    console.error(r);
    Message.error('下载文件出现错误，请联系管理员！');
  });
}