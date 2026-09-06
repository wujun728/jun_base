import request from "../utils/forumRequest";
/**
 * 知识社区/登录
 * @returns {*}
 */
export function articleUserLoginApi(data: object) {
  return request.post(`user/phone_login`, data);
}

/**
 * 获取用户信息
 * @returns {*}
 */
export function knowUserInfoApi() {
  return request.get(`user/info`);
}

/**
 * 获取用户社区文章标签
 * @returns {*}
 */
export function articleUserLabelApi(data: object) {
  return request.get(`forum/user_label`, data);
}

/**
 * 获取文章分类
 * @returns {*}
 */
export function articleLabelApi() {
  return request.post(`forum/category`);
}
/**
 * 保存用户文章标签
 * @returns {*}
 */
export function articleLabeSavelApi(data: object) {
  return request.post(`forum/save_label`, data);
}
/**
 * 获取用户标签
 * @returns {*}
 */
export function getuserLabel(data: object) {
  return request.post(`forum/label`, data);
}

/**
 * 获取文章列表
 * @returns {*}
 */
export function articleListApi(data: object) {
  return request.post(`forum/list`, data);
}

/**
 * 获取文章详情
 * @returns {*}
 */
export function articleInfoApi(id: number) {
  return request.get(`forum/detail/${id}`);
}
/**
 * 收藏/取消收藏文章
 * @returns {*}
 */
export function articleCollectApi(data: { id: number; status: number }) {
  return request.get(`forum/collect/${data.id}/${data.status}`, data);
}
/**
 * 点赞/取消点赞文章
 * @returns {*}
 */
export function articleSupportApi(data: { id: number; status: number }) {
  return request.get(`forum/support/${data.id}/${data.status}`, data);
}
