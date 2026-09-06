import $ from 'jquery'

function api(url, method, param, fnCallback) {
  this[method](url, param, fnCallback);
}

/**
 * POST请求
 * @param url
 * @param param
 * @param fnCallback
 */
function post(url, param, fnCallback) {
  let options = {};
  $.ajax({
    url: url,
    type: "POST",
    timeout: 300000,
    // headers: "Access-Control-Allow-Origin:*",
    contentType: "application/json;charset=UTF-8",
    dataType: "json",
    data: JSON.stringify(param)
  }).done(function (data, textStatus, jqXHR) {
    if (jqXHR.getResponseHeader('sessionStatus') == 'timeout') {
      options.content = '会话已经超时.';
    } else if (!data) {
      options.content = '无返回信息.';
    } else {
      fnCallback(data);
    }
  }).fail(function (jqXHR, textStatus, errorThrown) {
    if (jqXHR.status === 0) {
      self.errorMsg('请求异常，网络连接失败！ -> ' + this.url);
    } else if (jqXHR.status === 404) {
      self.errorMsg("请求找不到:" + this.url);
    } else {
      self.errorMsg("请求异常，状态码：" + jqXHR.status + this.url);
    }
  });
}

/**
 * GET请求
 * @param url
 * @param param
 * @param fnCallback
 */
function get(url, param, fnCallback) {
  $.ajax({
    url: url,
    type: "GET",
    timeout: 300000,
    headers: "Access-Control-Allow-Origin:*",
    contentType: "application/json;charset=UTF-8",
    dataType: "json",
    data: param
  }).done(function (data, textStatus, jqXHR) {
    if (jqXHR.getResponseHeader('sessionStatus') == 'timeout') {
      options.content = '会话已经超时.';
    } else if (!data) {
      options.content = '无返回信息.';
    } else {
      fnCallback(data);
    }
  }).fail(function (jqXHR, textStatus, errorThrown) {
    if (jqXHR.status === 0) {
      self.errorMsg('请求异常，网络连接失败！ -> ' + this.url);
    } else if (jqXHR.status === 404) {
      self.errorMsg("请求找不到:" + this.url);
    } else {
      self.errorMsg("请求异常，状态码：" + jqXHR.status + this.url);
    }
  });
};

export default {
  post,
  get,
  api
};
