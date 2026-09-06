import axios from 'axios';
import vueStore from '../store/vueStore.js';
import { getToken } from '/src/utils/auth';
import errorCode from '/src/utils/errorCode';
import { notification } from 'ant-design-vue';
import { h, defineComponent } from 'vue';
import { Modal } from 'ant-design-vue';
import { AutoeeIcon } from '@/utils/AutoeeIcon.js';
import { tansParams, blobValidate } from '@/utils/ruoyi';
import AutoeeUtil from '@/utils/AutoeeUtil.js';

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: import.meta.env.VITE_API_DOMAIN,
  // 超时
  timeout: 10000,
});
// request拦截器
service.interceptors.request.use(
  (config) => {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false;
    if (getToken() && !isToken) {
      config.headers['Authorization'] = 'Bearer ' + getToken(); // 让每个请求携带自定义token 请根据实际情况自行修改
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
    console.log(error);
    Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (res) => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;

    // 获取错误信息
    let msg = errorCode[code] || res.data.msg || errorCode['default'];
    if (code != 200) {
      // console.log('res=' + JSON.stringify(res));
      if (code === 403) {
        msg = msg + '请求url：' + res.config.url;
      }
    }
    // if (true) {
    if (code === 401) {
      Modal.warning({
        content: '登录状态已过期，请重新登录！',
        // icon: AutoeeIcon({ icon: 'ExclamationCircleOutlined' }),
        // icon: createVNode(ExclamationCircleOutlined),
        okText: '确定',
        onOk() {
          vueStore.dispatch('LogOut_Action').then(() => {
            location.href = '/index';
          });
        },
        // cancelText: 'Click to destroy all',
        // onCancel() {},
      });

      return Promise.reject('无效的会话，或者会话已过期，请重新登录。');
    } else if (code === 500) {
      notification.error({
        message: '请求后端服务失败',
        description: '失败信息：' + msg,
        // duration: null,
      });
      return Promise.reject(msg);
    } else if (code !== 200) {
      // notification.error({
      //   title: msg
      // })
      notification.error({
        message: '请求后端服务失败',
        description: '失败信息：' + msg,
        // duration: null,
      });
      return Promise.reject('error');
    } else {
      return res.data;
    }
  },
  (error) => {
    // console.log('err' + error);
    let { message } = error;
    if (message == 'Network Error') {
      message = '后端接口连接异常';
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时';
    } else if (message.includes('Request failed with status code')) {
      message = '系统接口' + message.substr(message.length - 3) + '异常';
    }
    notification.error({
      message: '请求后端服务失败',
      description: '失败信息：' + message,
      // duration: null,
    });
    return Promise.reject(error);
  }
);

// 通用下载方法
export function download(url, params, filename) {
  AutoeeUtil.messageInfo('开始下载数据，请稍候...');
  return service
    .get(url, params, {
      transformRequest: [
        (params) => {
          return tansParams(params);
        },
      ],
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
        notification.error({
          message: '请求后端服务失败',
          description: errMsg,
          // duration: null,
        });
      }
    })
    .catch((r) => {
      console.error(r);
      notification.error({
        message: '请求后端服务失败',
        description: '下载文件出现错误，请联系管理员！',
        // duration: null,
      });
    });
}

export default service;
