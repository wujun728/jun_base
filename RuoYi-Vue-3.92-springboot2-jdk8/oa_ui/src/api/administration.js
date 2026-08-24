import request from './request';

/**
 * 通知公告分类列表
 * @returns {*}
 */
export function noticeCategoryApi(data) {
  return request.get('notice/category', data);
}

/**
 * 通知公告分类创建表单
 * @returns {*}
 */
export function noticeCategoryCreateApi() {
  return request.get('notice/category/create');
}
/**
 * 通知公告全部文章
 * @returns {*}
 */
export function allArticlesApi(data) {
  return request.get('notice/index_list',data);
}

/**
 * 通知公告分类修改表单
 * @param {Number} id
 * @returns {*}
 */
export function noticeCategoryEditApi(id) {
  return request.get(`notice/category/${id}/edit`);
}

/**
 * 删除通知公告分类接口
 * @param {Number} id
 * @returns {*}
 */
export function noticeCategoryDeleteApi(id) {
  return request.delete(`notice/category/${id}`);
}

/**
 * 通知公告列表
 * @param {Object} data
 * @returns {*}
 */
export function noticeListApi(data) {
  return request.get(`notice/list`, data);
}

/**
 * 保存通知公告接口
 * @param {Object} data
 * @returns {*}
 */
export function noticeSaveApi(data) {
  return request.post(`notice/list`, data);
}

/**
 * 修改通知公告接口
 * @param {Number} id
 * @param {Object} data
 * @returns {*}
 */
export function noticeEditApi(id, data) {
  return request.put(`/notice/list/${id}`, data);
}

/**
 * 通知公告修改详情
 * @param {Number} id
 * @returns {*}
 */
export function noticeEditCreateApi(id) {
  return request.get(`notice/list/${id}/edit`);
}

/**
 * 显示隐藏通知公告
 * @param {Number} id
 * @param {Object} data
 * @returns {*}
 */
export function noticeStatusApi(id, data) {
  return request.get(`/notice/list/${id}`, data);
}

/**
 * 删除通知公告
 * @param {Number} id
 * @returns {*}
 */
export function noticeDeleteApi(id) {
  return request.delete(`/notice/list/${id}`);
}

/**
 * 通知公告详情
 * @param {Number} id
 * @returns {*}
 */
export function noticeDetailApi(id) {
  return request.get(`notice/detail/${id}`);
}

/**
 * 物资分类创建表单
 * @param {Object} data
 * @returns {*}
 */
export function storageCateCreateApi(data) {
  return request.get(`storage/cate/create`, data);
}

/**
 * 物资分类列表
 * @param {Object} data
 * @returns {*}
 */
export function storageCateApi(data) {
  return request.get(`storage/cate`, data);
}
/**
 * 物资分类批量移动1.6
 * @param {Object} data
 * @returns {*}
 */
export function storageListCateApi(data) {
  return request.post(`storage/list/cate`, data);
}

/**
 * 物资分类修改表单
 * @param {Object} data
 * @param {Number} id
 * @returns {*}
 */
export function storageCateEditApi(id, data) {
  return request.get(`storage/cate/${id}/edit`, data);
}

/**
 * 删除物资分类接口
 * @param {Number} id
 * @returns {*}
 */
export function storageCateDeleteApi(id) {
  return request.delete(`storage/cate/${id}`);
}

/**
 * 保存物资接口
 * @param {Object} data
 * @returns {*}
 */
export function storageListSaveApi(data) {
  return request.post(`storage/list`, data);
}

/**
 * 物资列表
 * @param {Object} data
 * @returns {*}
 */
export function storageListApi(data) {
  return request.get(`storage/list`, data);
}

/**
 * 删除物资
 * @param {Number} id
 * @returns {*}
 */
export function storageDeleteApi(id) {
  return request.delete(`storage/list/${id}`);
}

/**
 * 保存物资记录接口
 * @param {Object} data
 * @returns {*}
 */
export function storageRecordSaveApi(data) {
  return request.post(`storage/record`, data);
}

/**
 * 物资记录列表
 * @param {Object} data
 * @returns {*}
 */
export function storageRecordApi(data) {
  return request.get(`storage/record`, data);
}

/**
 * 物资记录人员列表
 * @param {Object} data
 * @returns {*}
 */
export function storageRecordUserApi(data) {
  return request.get(`storage/record/user`, data);
}
/**
 * 物资记录人员列表-物资记录
 * @param {Object} data
 * @returns {*}
 */
export function storageRecordUsersApi(data) {
  return request.get(`storage/record/users`, data);
}

/**
 * 获取维修记录详情
 * @param {Number} id
 * @returns {*}
 */
export function storageRecordRepairApi(id) {
  return request.get(`storage/record/repair/${id}`);
}

/**
 * 获取物资记录统计
 * @param {Object} data
 * @returns {*}
 */
export function storageRecordCensusApi(data) {
  return request.get(`storage/record/census`, data);
}

/**
 * 动态置顶接口
 * @param {Number} id
 * @returns {*}
 */
 export function noticeTopApi(id) {
  return request.put(`notice/top/${id}`);
}
