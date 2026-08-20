const http = uni.$u.http;

/**
 * 混入默认个性化配置
 * @param {*} config 
 * @returns 
 */
function mixinCustom (config) {
  config.custom = Object.assign({
    auth: true,
    toast: true,
    catch: true,
    loading: true
  }, config.custom || {});
  return config;
}

/**
 * 格式化get请求url参数，将对象解析为字符串
 * @param {*} url 
 * @param {*} params 
 * @returns 
 */
function urlFormater (url, params) {
  if (params) {
    let paramList = [];
    for (let key in params) {
      paramList.push(key + '=' + params[key])
    }
    return url.indexOf('?') > -1 ? (url + '&' + paramList.join('&')) : (url + '?' + paramList.join('&'))
  }
  return url;
}

const request = {
  // post提交
  post (url, params, config = {}) {
    config = mixinCustom(config)
    return http.post(url, params, config);
  },
  // get提交
  get (url, params, config = {}) {
    config = mixinCustom(config)
    let path = urlFormater(url, params)
    return http.get(path, config);
  },

  // put提交
  put (url, params, config = {}) {
    config = mixinCustom(config)
    return http.put(url, params, config);
  },

  // delete提交
  delete (url, params, config = {}) {
    config = mixinCustom(config)
    return http.delete(url, params, config);
  },

};

export default request;