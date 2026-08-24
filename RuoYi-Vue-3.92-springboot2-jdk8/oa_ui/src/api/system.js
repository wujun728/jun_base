import request from './request'
/**
 * @description 菜单管理 -- 新增表单
 */
export function menuCreateApi(data) {
  return request.get('system/menus/create', data)
}

/**
 * @description 菜单管理 -- 菜单列表
 */
export function menuListApi(data) {
  return request.get('system/menus', data)
}
/**
 * @description 菜单管理 -- 菜单状态显示隐藏
 */
export function menuShowApi(id, data) {
  return request.get(`system/menus/${id}`, data)
}
/**
 * @description 菜单管理 -- 菜单列表 -- 获取某个菜单下得权限
 */
export function menuRuleListApi(id) {
  return request.get('system/menus/rule_list/' + id)
}
// /**
//  * @description 菜单管理 -- 菜单列表 -- 获取修改菜单得数据
//  */
// export function menuEditApi(id) {
//   return request.get(`system/menus/${id}/edit`);
// }

/**
 * @description 菜单管理 -- 删除菜单
 */
export function menuDeleteitApi(id) {
  return request.delete(`system/menus/${id}`)
}
/**
 * @description 菜单管理 -- 新增表单 -- 权限列表
 */
export function menuNotSaveApi() {
  return request.post('system/menus/not_save')
}

/**
 * @description 素材库分类 -- 列表
 */
export function formatLstApi(id) {
  return request.get(`system/attach_cate`)
}

/**
 * @description 素材库分类 -- 添加
 */
export function attachmentCreateApi() {
  return request.get(`system/attach_cate/create`)
}

/**
 * @description 素材库分类 -- 编辑
 */
export function attachmentUpdateApi(id) {
  return request.get(`system/attach_cate/${id}/edit`)
}

/**
 * @description 素材库分类 -- 删除
 */
export function attachmentDeleteApi(id) {
  return request.delete(`system/attach_cate/${id}`)
}

/**
 * @description 素材库 -- 列表
 */
export function attachmentListApi(data) {
  return request.get(`system/attach/list`, data)
}

/**
 * @description 素材库 -- 删除
 */
export function picDeleteApi(data) {
  return request.delete(`system/attach/delete`, data)
}

/**
 * @description 素材库 -- 修改名称
 */
export function picNameEditApi(id, data) {
  return request.put(`system/attach/update/${id}`, data)
}

/**
 * @description 素材库 -- 图片移动
 */
export function categoryApi(data) {
  return request.put(`system/attach/move`, data)
}
/**
 * @description 订单支付结果
 */
export function orderResultApi(order_id) {
  return request.get(`order/result/${order_id}`)
}
