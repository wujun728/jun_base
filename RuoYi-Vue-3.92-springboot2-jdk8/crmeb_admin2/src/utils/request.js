// +----------------------------------------------------------------------
// | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
// +----------------------------------------------------------------------
// | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
// +----------------------------------------------------------------------
// | Author: CRMEB Team <admin@crmeb.com>
// +----------------------------------------------------------------------

import axios from 'axios';
import { Message } from 'element-ui';
import store from '@/store';
import { getToken } from '@/utils/auth';
import SettingMer from '@/utils/settingMer';
import { tansParams } from '@/utils/ruoyi';
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
    const res = response.data;
    // 若依返回 code 字段，统一添加 status 字段兼容 CRMEB
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

export default service;
