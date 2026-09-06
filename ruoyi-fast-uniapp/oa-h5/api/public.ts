import request from "../utils/request";

/**
 * 手机号登陆
 * POST
 * @return {data}
 * @returns {*}
 */
export function phoneLoginApi(data: object) {
  return request.post("user/login/phone", data);
}

/**
 * 账号密码登陆
 * POST
 * @param {Object} data
 * @returns {*}
 */
export function loginApi(data: object) {
  return request.post("user/login", data);
}

/**
 * 发送手机验证码
 * POST
 * @return {data}
 * @returns {*}
 */
export function verifyApi(data: object) {
  return request.post("user/verify/send", data);
}

/**
 * 发送手机验证码
 * POST
 * @return {data}
 * @returns {*}
 */
export function articleCaptchaApi(phone: string) {
  return request.get(`article/captcha/${phone}`);
}

/**
 * 获取手机验证码KEY
 * GET
 * @return {data}
 * @returns {*}
 */
export function getShortKeyApi() {
  return request.get("user/verify/code");
}

/**
 * 退出登陆
 * @returns {*}
 */
export function logoutApi() {
  return request.get("user/logout");
}

/**
 * 获取服务配置接口
 * @returns {*}
 */
export function serverConfigApi(data: object) {
  return request.get(`common/config/server`, data);
}

/**
 * 图片上传（无需登录）
 * @return {data}
 * @returns {*}
 */
export function uploadApi(data: object) {
  return request.post(`upload`, data);
}

/**
 * 修改密码
 * @return {data}
 * @returns {*}
 */
export function savePasswordApi(data: object) {
  return request.put(`user/update/password`, data);
}

/**
 * 滑块验证码接口
 * @returns {*}
 */
export function sliderCodeApi(): any {
  return request.get(`common/captcha_slider`);
}

/**
 * 滑块验证码接口
 * @returns {*}
 */
export function ajCaptchaCheck(data: object): any {
  return request.post(`common/captcha_verify`, data);
}

/**
 * 获取用户协议
 * @returns {*}
 */
export function commonAgreementApi(id: number): any {
  return request.get(`common/agreement/${id}`);
}

/**
 * 获取用户权限接口
 * @returns {*}
 */
export function enterpriseAuthApi(): any {
  return request.get(`company/auth`);
}

/**
 * 业务员列表接口
 * @returns {*}
 */
export function enterpriseSalesmanApi(): any {
  return request.get(`client/customer/salesman`);
}

/**
 * 获取验证相关参数接口
 * @param query 查询参数
 * @returns
 */
export function ajCaptchaDataApi(query: any): any {
  return request.get(`common/captcha`, query);
}

/**
 * 验证验证码是否合法相关接口
 * @param data 验证码相关参数
 * @returns
 */
export function ajCaptchaCheckApi(data: any): any {
  return request.post(`common/ajcheck`, data);
}
