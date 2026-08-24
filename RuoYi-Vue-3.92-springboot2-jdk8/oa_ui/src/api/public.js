import request from './request'

/**
 * @description 组织架构tree型数据
 */
export function frameTreeApi(data) {
  return request.get('config/frame/tree', data)
}
/**
 * @description V1.4组织架构选择人员数据
 */
export function frameUserApi(data) {
  return request.get('config/frame/user', data)
}
/**
 * @description 我的汇报组织架构tree型数据
 */
export function reportTreeApi(data) {
  return request.get('config/frame/scope', data)
}

/**
 * @description 获取城市tree结构
 */
export function getCityListApi() {
  return request.get(`common/city`)
}



/**
 * @description WPS浏览文件
 */
export function browseWpsApi(data) {
  return request.get(`common/browse/`, data)
}

/**
 * @description 获取下载文件地址
 * @param data
 */
export function downloadUrlApi(data) {
  return request.get(`common/download_url`, data)
}

/**
 * @description 获取网站配置（若依无 common/config，失败时返回空配置不阻断流程）
 * @param data
 */
export function appConfigApi(data) {
  return request.get(`common/config`, data).catch(() => Promise.resolve({ data: {}, status: 200 }))
}

/**
 * @description 获取消息列表 common/message/{id}/{isRead}

 * @param data
 */
export function messageListApi(data) {
  return request.get(`common/message`, data)
}
/**
 * @description 菜单（若依 /getRouters）
 */
export function userMenusApi() {
  return request.get(`/getRouters`)
}

/**
 * @description 获取上传配置
 */
export function configUploadApi() {
  return request.get(`config/upload`)
}
/**
 * @description 获取上传id
 */
export function attachSaveApi(data) {
  return request.post(`system/attach/save`, data)
}

/**
 * @description 获取网站配置接口
 */
export function entSiteApi() {
  return request.get(`common/site_address`)
}

/**
 * @description 获取上传参数
 */
export function getUploadKeysApi() {
  return request.get(`common/upload_key`)
}

/**
 * @description 云存储 url 上传
 * @param {String} url 传值参数
 * @param {Object} data 传值参数
 */
export function ossUpload(url, data) {
  return request.post(url, data)
}

/**
 * @description 云存储 url 上传
 * @param {Object} data 传值参数
 */
export function localUpload(data) {
  return request.post('system/attach/upload', data)
}

/**
 * @description 文件上传
 * @param {String} pid 传值参数
 * @param {String} id 传值参数
 * @param {File} file 传值参数
 */
export function fileUpload(pid, id, file) {
  return request.put(`cloud/file/${pid}/update/${id}`, file)
}

/**
 * @description 获取 AI 配置
 */
export function getAiConfigApi() {
  return request.get(`common/site_address`);
}