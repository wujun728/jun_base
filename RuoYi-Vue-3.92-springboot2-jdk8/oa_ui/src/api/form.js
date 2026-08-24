import request from './request'
/**
 * @description 获取字典列表接口
 */
export function getDictListApi(data) {
  return request.get('config/dict_type', data)
}

/**
 * @description 新增字典表单
 */
export function getDictCreateApi() {
  return request.get('config/dict_type/create')
}

/**
 * @description 编辑字典表单
 */
export function getDictEditApi(id) {
  return request.get(`config/dict_type/${id}/edit`)
}

/**
 * @description 修改字典状态
 */
export function getDictPutShowApi(dict_type, data) {
  return request.get(`config/dict_type/${dict_type}`, data)
}

/**
 * @description 删除字典列表
 */
export function getDictDeleteShowApi(id) {
  return request.delete(`config/dict_type/${id}`)
}

/**
 * @description 获取字典数据列表接口
 */
export function getDictDataListApi(data) {
  return request.get(`config/dict_data`, data)
}
/**
 * @description 获取自定义数据列表接口
 */
export function getDictTreeListApi(data) {
  return request.post(`config/dict_data/tree`, data)
}

/**
 * @description 新增字典数据表单接口
 */
export function getDictDataCreateApi(data) {
  return request.get(`config/dict_data/create`, data)
}

/**
 * @description 编辑字典数据表单接口
 */
export function getDictDataEditeApi(dict_datum) {
  return request.get(`config/dict_data/${dict_datum}/edit`)
}

/**
 * @description 修改字典数据状态
 */
export function getDictDataPutApi(dict_datum, data) {
  return request.get(`config/dict_data/${dict_datum}`, data)
}

/**
 * @description 删除字典数据
 */
export function getDictDataDeleteApi(id) {
  return request.delete(`config/dict_data/${id}`)
}

/**
 * @description 获取字典详情
 */
export function getDictDatainfoApi(id) {
  return request.get(`config/dict_type/info/${id}`)
}

/**
 * @description 获取自定义表单分组列表
 */
export function getFormListApi(data) {
  return request.get(`config/form/cate`, data)
}
/**
 * @description 获取保存分组接口
 */
export function formCateSaveApi(type, data) {
  return request.post(`config/form/cate/${type}`, data)
}
/**
 * @description 编辑分组接口
 */
export function formCatePutSaveApi(id, data) {
  return request.put(`config/form/cate/${id}`, data)
}
/**
 * @description 删除分组接口
 */
export function formCateDeleteApi(id) {
  return request.delete(`config/form/cate/${id}`)
}
/**
 * @description 修改分组状态
 */
export function formCatePutApi(id, data) {
  return request.get(`config/form/cate/${id}`, data)
}
/**
 * @description 保存自定义表单接口
 */
export function formCateSaveDataApi(types, data) {
  return request.post(`config/form/data/${types}`, data)
}
/**
 * @description 移动分组接口
 */
export function formCateMoveApi(types, data) {
  return request.put(`config/form/data/move/${types}`, data)
}

/**
 * @description 获取表单配置
 * @param {string} id - 实体ID
 * @returns {Promise} - 返回一个Promise对象，resolve时返回表单配置数据
 */
export function formCrudList(id) {
  return request.get(`crud/form/list/${id}`)
}

/**
 * @description 保存表单配置
 * @param {string} id - 实体ID
 * @param {object} data - 表单配置数据
 * @returns {Promise} - 返回一个Promise对象，resolve时返回表单配置数据
 */
export function formCrudSave(id, data) {
  return request.post(`crud/form/save/${id}`, data)
}

/**
 * @description 获取表单详情
 * @param {string} id - 实体ID
 * @returns {Promise} - 返回一个Promise对象，resolve时返回表单详情数据
 */
export function formInfo(id) {
  return request.get(`crud/form/info/${id}`)
}
