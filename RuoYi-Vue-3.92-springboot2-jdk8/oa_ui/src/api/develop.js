import request from './request'

/**
 * 获取应用列表
 * @returns {*}
 */
export function getcrudCateListApi() {
  return request.get('crud/cate/list')
}

/**
 *保存应用分类
 * @returns {*}
 */
export function savecrudCateApi(data) {
  return request.post('crud/cate/save', data)
}



/**
 * 获取二级实体数据
 * @returns {*}
 */
export function getDatabaseApi() {
  return request.get('crud/database/tree')
}

/**
 * 获取密钥权限
 * @returns {*}
 */
export function getOenapiApi() {
  return request.get('openapi/role')
}

/**
 * 新增对外接口密钥
 * @returns {*}
 */
export function saveOpenKeyApi(data) {
  return request.post('openapi/key',data)
}

/**
 * 获取对外接口密钥列表
 * @returns {*}
 */
export function getOpenKeyApi(data) {
  return request.get('openapi/key',data)
}

/**
 * 获取密钥复制数据
 * @returns {*}
 */
export function getFindskApi(id) {
  return request.get(`openapi/findsk/${id}`)
}

/**
 * 删除对外接口密钥
 * @returns {*}
 */
export function deleteOpenKeyApi(key) {
  return request.delete(`openapi/key/${key}`)
}

/**
 * 修改对外接口密钥信息
 * @returns {*}
 */
export function putOpenKeyApi(key,data) {
  return request.put(`openapi/key/${key}`,data)
}

/**
 * 修改对外接口密钥状态
 * @returns {*}
 */
export function getOpenKeyStatusApi(key,data) {
  return request.get(`openapi/key/${key}`,data)
}

/**
 * 获取对外接口密钥详情
 * @returns {*}
 */
export function getOpenKeyInfoApi(key) {
  return request.get(`openapi/key/${key}/edit`)
}



/**
 * 删除看板接口
 * @returns {*}
 */
export function dashboardDelApi(id) {
  return request.delete(`crud/dashboard/${id}`)
}

/**
 * 获取看板过滤条件字段
 * @returns {*}
 */
export function viewSearchApi(id) {
  return request.get(`crud/dashboard/view/${id}`)
}

/**
 *删除应用分类
 * @returns {*}
 */
export function delcrudCateApi(id) {
  return request.delete(`crud/cate/del/${id}`)
}

/**
 *创建实体
 * @returns {*}
 */
export function databaseSaveApi(data) {
  return request.post(`crud/database/create`, data)
}

/**
 *获取实体列表
 * @returns {*}
 */
export function databaseListApi(data) {
  return request.get(`crud/database/list`, data)
}

/**
 *删除实体
 * @returns {*}
 */
export function databaseDelApi(id) {
  return request.delete(`crud/database/del/${id}`)
}

/**
 *复制实体
 * @returns {*}
 */
export function databaseCopyApi(id, data) {
  return request.post(`crud/database/copy/${id}`, data)
}

/**
 *修改实体
 * @returns {*}
 */
export function databasePutApi(id, data) {
  return request.put(`crud/database/update/${id}`, data)
}

/**
 *根据id获取实体信息
 * @returns {*}
 */
export function databaseInfoApi(id) {
  return request.get(`crud/database/info/${id}`)
}

/**
 *获取表单所有字段类型
 * @returns {*}
 */
export function dataFieldTypeApi() {
  return request.get(`crud/field/type`)
}

/**
 *获取实体对应字段列表
 * @returns {*}
 */
export function dataFieldListApi(id, data) {
  return request.get(`crud/field/list/${id}`, data)
}

/**
 *保存字段表单
 * @returns {*}
 */
export function dataFieldSaveApi(data) {
  return request.post(`crud/field/save`, data)
}

/**
 *删除字段列表
 * @returns {*}
 */
export function dataFieldDeleteApi(id) {
  return request.delete(`crud/field/del/${id}`)
}

/**
 *获取字段详情
 * @returns {*}
 */
export function dataFieldInfoApi(id) {
  return request.get(`crud/field/info/${id}`)
}

/**
 *修改字段详情
 * @returns {*}
 */
export function dataFieldUpdateApi(id, data) {
  return request.put(`crud/field/update/${id}`, data)
}

/**
 *主字段展示修改
 * @returns {*}
 */
export function dataFieldMainApi(id) {
  return request.put(`crud/field/main/${id}`)
}

/**
 * 获取某个实体下面的字段信息和数据字典信息
 * @returns {*}
 */
export function dataDatabaseFieldsApi(id, data) {
  return request.get(`crud/database/fields/${id}`, data)
}

/**
 * 条件搜索
 * @returns {*}
 */
export function dataOperatorApi() {
  return request.get(`crud/field/operator`)
}

/**
 * 获取一对一关联展示字段
 * @returns {*}
 */
export function dataModulerFieldApi(id) {
  return request.get(`crud/module/association_field/${id}`)
}

/**
 * 获取一对一关联展示列表
 * @returns {*}
 */
export function dataModulerListApi(id, data) {
  return request.get(`crud/module/association_list/${id}`, data)
}

/**
 * 获取流程配置列表
 * @returns {*}
 */
export function dataApproveListApi(data) {
  return request.get(`crud/approve`, data)
}

/**
 * 保存流程配置
 * @returns {*}
 */
export function dataApproveSaveApi(data) {
  return request.post(`crud/approve`, data)
}

/**
 * 获取流程配置详情
 * @returns {*}
 */
export function dataApproveInfoApi(id) {
  return request.get(`crud/approve/${id}/edit`)
}

/**
 * 删除流程列表
 * @returns {*}
 */
export function dataApproveDeleteApi(id) {
  return request.delete(`crud/approve/${id}`)
}

/**
 * 显示隐藏流程配置接口
 * @returns {*}
 */
export function dataApproveStatusApi(id, data) {
  return request.get(`crud/approve/${id}`, data)
}

/**
 * 编辑保存流程配置接口
 * @returns {*}
 */
export function dataApprovePutApi(id, data) {
  return request.put(`crud/approve/${id}`, data)
}

/**
 * 获取触发器列表接口
 * @returns {*}
 */
export function dataEventListApi(id, data) {
  return request.get(`crud/event/list/${id}`, data)
}

/**
 * 获取触发器管理列表接口
 * @returns {*}
 */
export function dataEventGuanListApi(data) {
  return request.get(`crud/event/list`, data)
}

/**
 * 获取触发器类型
 * @returns {*}
 */
export function dataEventTypeApi() {
  return request.get(`crud/event/type`)
}

/**
 * 新建触发器保存接口
 * @returns {*}
 */
export function dataEventSaveApi(data) {
  return request.post(`crud/event/save`, data)
}

/**
 * 删除触发器接口
 * @returns {*}
 */
export function dataEventDelApi(id) {
  return request.delete(`crud/event/del/${id}`)
}

/**
 * 获取触发器执行动作类型
 * @returns {*}
 */
export function dataEventActionApi() {
  return request.get(`crud/event/action`)
}

/**
 * 修改触发器状态接口
 * @returns {*}
 */
export function dataEventStatusApi(id, data) {
  return request.put(`crud/event/status/${id}`, data)
}

/**
 * 获取触发器详情
 * @returns {*}
 */
export function dataEventInfoApi(id) {
  return request.get(`crud/event/info/${id}`)
}

/**
 * 获取触发器关联数据
 * @returns {*}
 */
export function dataEventCrudApi(id, eventId) {
  return request.get(`crud/event/crud/${id}/${eventId}`)
}

/**
 * 编辑保存触发器页面数据
 * @returns {*}
 */
export function dataEventUpdateApi(id, data) {
  return request.put(`crud/event/update/${id}`, data)
}

/**
 * 生成功能页面列表数据
 * @returns {*}
 */
export function crudModuleListApi(name, data) {
  return request.post(`crud/module/${name}/list`, data)
}

/**
 * 生成功能页面数据-条件展示和表头展示
 * @returns {*}
 */
export function crudModuleInfoApi(name, id,data) {
  return request.get(`crud/module/${name}/crud/info/${id}`,data)
}
/**
 * 列表设计-条件展示和表头展示
 * @returns {*}
 */
export function crudViewInfoApi(id) {
  return request.get(`crud/view/info/${id}`)
}

/**
 * 生成功能页面数据-保存条件展示和表头展示
 * @returns {*}
 */
export function crudModuleSaveApi(name, data) {
  return request.post(`crud/module/${name}/crud`, data)
}

/**
 * 生成功能页面数据-新增数据
 * @returns {*}
 */
export function crudModuleCreateApi(name, data) {
  return request.get(`crud/module/${name}/create`, data)
}

/**
 * 生成功能页面数据-保存数据
 * @returns {*}
 */
export function crudModuleSaveDataApi(name, data) {
  return request.post(`crud/module/${name}/save`, data)
}

/**
 * 生成功能页面数据-删除数据
 * @returns {*}
 */
export function crudModuleDelApi(name, id) {
  return request.delete(`crud/module/${name}/delete/${id}`)
}
/**
 * 生成功能页面数据-删除数据
 * @returns {*}
 */
export function crudModuleBatchDelApi(name, data) {
  return request.delete(`crud/module/${name}/batchdelete`, data)
}

/**
 * 生成功能页面数据-获取页面详情
 * @returns {*}
 */
export function crudModuleFindApi(name, id) {
  return request.get(`crud/module/${name}/find/${id}`)
}

/**
 * 生成功能页面数据-编辑保存接口
 * @returns {*}
 */
export function crudModuleUpdateApi(name, id, data) {
  return request.put(`crud/module/${name}/update/${id}`, data)
}

/**
 * 列表设计-保存视图接口
 * @returns {*}
 */
export function crudViewSaveApi(id, data) {
  return request.post(`crud/view/save/${id}`, data)
}

/**
 * 视图管理-展示数据
 * @returns {*}
 */
export function crudSeniorListApi(name, data) {
  return request.get(`crud/module/${name}/senior/list`, data)
}
/**
 * 视图管理-保存
 * @returns {*}
 */
export function crudSeniorSaveApi(name, data) {
  return request.post(`crud/module/${name}/senior/save`, data)
}
/**
 * 视图管理-保存
 * @returns {*}
 */
export function crudSeniorDelApi(name, id) {
  return request.delete(`crud/module/${name}/senior/del/${id}`)
}
/**
 * 视图管理-排序
 * @returns {*}
 */
export function crudSeniorSortApi(name, data) {
  return request.post(`crud/module/${name}/senior/sort`, data)
}
/**
 * 接口管理-测试请求并返回请求数据
 * @returns {*}
 */
export function crudTestSendApi(data) {
  return request.post(`crud/test_send`, data)
}
/**
 * 接口管理-添加数据
 * @returns {*}
 */
export function crudAddCurlApi(data) {
  return request.post(`crud/curl`, data)
}
/**
 * 接口管理-列表查询
 * @returns {*}
 */
export function crudGetCurlListApi(data) {
  return request.get(`crud/curl`, data)
}
/**
 * 接口管理-列表查询
 * @returns {*}
 */
export function crudGetCurlEditApi(id) {
  return request.get(`crud/curl/${id}/edit`)
}
/**
 * 接口管理-列表查询
 * @returns {*}
 */
export function crudPutCurlApi(id, data) {
  return request.put(`crud/curl/${id}`, data)
}

/**
 * 接口管理-列表删除
 * @returns {*}
 */
export function crudDeleteCurlApi(id) {
  return request.delete(`crud/curl/${id}`)
}

/**
 * 触发器发送请求
 * @returns {*}
 */
export function crudSendApi(id) {
  return request.post(`crud/send/${id}`)
}

/**
 * 批量导入
 * @returns {*}
 */
export function crudImportApi(name,data) {
  return request.post(`crud/module/${name}/import`,data)
}

/**
 * 获取接口文档
 * @returns {*}
 */
export function getDocsApi() {
  return request.get(`openapi/docs`)
}


/**
 * 低代码单个字段修改
 * @returns {*}
 */
export function putUpdateFieldApi(name,id,data) {
  return request.put(`crud/module/${name}/update_field/${id}`,data)
}

/**
 * 低代码获取实体日志
 * @returns {*}
 */
export function getModuleLogApi(name,id,data) {
  return request.get(`crud/module/${name}/log/${id}`,data)
}

/**
 * 低代码获取实体评论
 * @returns {*}
 */
export function getModuleCommentApi(name,id,data) {
  return request.get(`crud/module/${name}/comment/${id}`,data)
}

/**
 * 低代码写评论
 * @returns {*}
 */
export function saveModuleCommentApi(name,id,data) {
  return request.post(`crud/module/${name}/comment/${id}`,data)
}

/**
 * 低代码修改评论
 * @returns {*}
 */
export function putModuleCommentApi(name,comment_id,data) {
  return request.put(`crud/module/${name}/comment/${comment_id}`,data)
}

/**
 * 低代码删除评论
 * @returns {*}
 */
export function deleteModuleCommentApi(name,comment_id) {
  return request.delete(`crud/module/${name}/comment/${comment_id}`)
}

/**
 *  低代码列表数据移交
 * @returns {*}
 */
export function moduleTransferApi(name,data) {
  return request.post(`crud/module/${name}/transfer`,data)
}

/**
 *  低代码列表管理范围
 * @returns {*}
 */
export function moduleFramerApi(name,data) {
  return request.get(`crud/module/${name}/frame/tree`,data)
}

/**
 *  低代码列表数据共享
 * @returns {*}
 */
export function moduleShareApi(name,data) {
  return request.post(`crud/module/${name}/share`,data)
}

/**
 *  低代码列表共享列表
 * @returns {*}
 */
export function moduleShareListApi(name,data_id,data) {
  return request.get(`crud/module/${name}/share/${data_id}`,data)
}

/**
 *  低代码列表共享删除
 * @returns {*}
 */
export function delModuleShareApi(name,id,data) {
  return request.delete(`crud/module/${name}/share/${id}`,data)
}

/**
 *  低代码列表共享修改
 * @returns {*}
 */
export function putModuleShareApi(name,id,data) {
  return request.put(`crud/module/${name}/share/${id}`,data)
}

/**
 *  低代码创建问卷调查
 * @returns {*}
 */
export function moduleQuestionnaireApi(name,data) {
  return request.post(`crud/module/${name}/questionnaire`,data)
}

/**
 *  低代码修改问卷调查
 * @returns {*}
 */
export function putModuleQuestionnaireApi(name,id,data) {
  return request.put(`crud/module/${name}/questionnaire/${id}`,data)
}

/**
 *  低代码删除问卷调查
 * @returns {*}
 */
export function delModuleQuestionnaireApi(name,id) {
  return request.delete(`crud/module/${name}/questionnaire/${id}`)
}

/**
 *  低代码获取问卷调查列表
 * @returns {*}
 */
export function getModuleQuestionnaireApi(name,data) {
  return request.get(`crud/module/${name}/questionnaire`,data)
}
/**
 *  低代码获取问卷表单信息
 * @returns {*}
 */
export function getModuleQuestionnaireInfoApi(unique) {
  return request.get(`common/questionnaire_info/${unique}`)
}
/**
 *  低代码分享问卷滑块验证
 * @returns {*}
 */
export function getModuleCaptchaApi(data) {
  return request.get(`common/aj_captcha`,data)
}

/**
 *  低代码分享问卷一次验证
 * @returns {*}
 */
export function saveModuleCaptchaApi(data) {
  return request.post(`common/ajcheck`,data)
}

/**
 *  低代码分享问卷一次验证
 * @returns {*}
 */
export function saveModuleFormApi(name,unique,data) {
  return request.post(`common/questionnaire_save/${name}/${unique}`,data)
}
/**
 *  低代码取消协作
 * @returns {*}
 */
export function delCancelShareApi(name,data_id) {
  return request.delete(`crud/module/${name}/cancel_share/${data_id}`)
}

