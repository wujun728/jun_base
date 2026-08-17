
import axios from 'axios';
import { Loading, Message } from 'element-ui';
import store from '@/store';
import { getToken } from '@/utils/auth';
import SettingMer from '@/utils/settingMer';
import { tansParams, blobValidate } from '@/utils/ruoyi';
import { saveAs } from 'file-saver';
import errorCode from '@/utils/errorCode';
const service = axios.create({
  baseURL: SettingMer.apiBaseURL,
  timeout: 60000,
});

// request interceptor
service.interceptors.request.use(
  (config) => {
    const token = !store.getters.token ? sessionStorage.getItem('token') : store.getters.token;
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    if (/get/i.test(config.method)) {
      config.params = config.params || {};
      config.params.temp = Date.parse(new Date()) / 1000;
      let url = config.url + '?' + tansParams(config.params);
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// response interceptor
service.interceptors.response.use(
  (response) => {
    // 二进制数据则直接返回
    if (response.config.responseType === 'blob' || response.config.responseType === 'arraybuffer') {
      return response.data;
    }
    const res = response.data;
    // 若依返回 code 字段，统一添加 status 字段兼容 SCRM
    if (res.code !== undefined) {
      res.status = res.code;
    }
    if (res.code === 401) {
      Message.error('无效的会话，或者登录已过期，请重新登录。');
      if (window.location.pathname !== '/login') location.href = '/login';
    } else if (res.code === 403) {
      Message.error('没有权限访问。');
    }
    if (res.code !== 200 && res.code !== 0 && res.code !== 401) {
      Message({
        message: res.msg || res.message || 'Error',
        type: 'error',
        duration: 5 * 1000,
      });
      return Promise.reject(res || 'Error');
    } else {
      return res;
    }
  },
  (error) => {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000,
    });
    return Promise.reject(error);
  },
);

let downloadLoadingInstance;

// 通用下载方法
export function download(url, params, filename, config) {
  downloadLoadingInstance = Loading.service({ text: '正在下载数据，请稍候', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)' });
  return service
    .post(url, params, {
      transformRequest: [(params) => { return tansParams(params); }],
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob',
      ...config,
    })
    .then(async (data) => {
      const isBlob = blobValidate(data);
      if (isBlob) {
        const blob = new Blob([data]);
        saveAs(blob, filename);
      } else {
        const resText = await data.text();
        const rspObj = JSON.parse(resText);
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default'];
        Message.error(errMsg);
      }
      downloadLoadingInstance.close();
    })
    .catch((r) => {
      console.error(r);
      Message.error('下载文件出现错误，请联系管理员！');
      downloadLoadingInstance.close();
    });
}

export default service;
