import request from "../utils/request";
/**
 * 生成功能页面数据-条件展示和表头展示
 * @return {*}
 */
export function crudModuleInfoApi(name: string, id: number) {
  return request.get(`crud/module/${name}/crud/info/${id}`);
}
/**
 * 生成列表数据
 * @return {*}
 */
export function crudModuleListApi(name: string, data: object) {
  return request.post(`crud/module/${name}/list`, data);
}
/**
 * 省市区数据
 * @return {*}
 */
export function getDictTreeListApi(data: object) {
  return request.get(`dict/data/tree`, data);
}
/**
 * 管理范围数据
 * @return {*}
 */
export function getDictFrameListApi(data: object) {
  return request.get(`frame/scope`, data);
}
/**
 * 新建表单
 * @return {*}
 */
export function crudModuleCreateApi(name: string, data: object) {
  return request.get(`crud/module/${name}/create`, data);
}
/**
 * 获取一对一关联展示字段
 * @return {*}
 */
export function dataModulerFieldApi(id: number) {
  return request.get(`crud/module/association_field/${id}`);
}
/**
 * 获取一对一关联展示列表
 * @return {*}
 */
export function dataModulerListApi(id: number, data: object) {
  return request.get(`crud/module/association_list/${id}`, data);
}

/**
 * 生成功能页面数据-保存数据
 * @returns {*}
 */
export function crudModuleSaveDataApi(name: string, data: object) {
  return request.post(`crud/module/${name}/save`, data);
}

/**
 * 生成功能页面数据-删除数据
 * @returns {*}
 */
export function crudModuleDelApi(name: string, id: number) {
  return request.delete(`crud/module/${name}/delete/${id}`);
}
/**
 * 生成功能页面数据-获取页面详情
 * @returns {*}
 */
export function crudModuleFindApi(name: string, id: number) {
  return request.get(`crud/module/${name}/find/${id}`);
}
/**
 * 生成功能页面数据-编辑保存接口
 * @returns {*}
 */
export function crudModuleUpdateApi(name: string, id: number, data: object) {
  return request.put(`crud/module/${name}/update/${id}`, data);
}

/**
 * 生成功能页面数据-底部菜单
 * @returns {*}
 */
export function crudModuleMenusApi(name: string) {
  return request.get(`crud/module/${name}/crud/menus`);
}

/**
 * 获取数据看板设计接口
 * @param id ID
 * @returns
 */
export function crudModuleDashboardDesignApi(id: number | string) {
  return request.get(`crud/dashboard/design/${id}`);
}

/**
 * 获取数据看板图表数据
 * @param data 查询数据
 * @returns
 */
export function crudModuleDashboardChartDataApi(data: any) {
  return request.post(`crud/dashboard/chart`, data);
}

/**
 * 获取数据看板列表数据
 * @param data 查询数据
 * @returns
 */
export function crudModuleDashboardListDataApi(data: any) {
  return request.post(`crud/dashboard/list`, data);
}

/**
 * 获取评论列表数据
 * @param data 查询数据
 * @returns
 */
export function crudModuleCommentListDataApi(name: string, id: number, data: object) {
  return request.get(`crud/module/${name}/comment/${id}`, data);
}

/**
 * 提交评论内容
 * @param data 保存数据
 * @returns
 */
export function crudModuleCommentSaveDataApi(name: string, id: number, data: object) {
  return request.post(`crud/module/${name}/comment/${id}`, data);
}

/**
 * 删除评论内容
 * @param data 删除数据
 * @returns
 */
export function crudModuleCommentDeleteDataApi(name: string, comment_id: number) {
  return request.delete(`crud/module/${name}/comment/${comment_id}`);
}

/**
 * 获取调查问卷表单设计信息
 * @param unique 表单 ID
 * @returns
 */
export function crudModuleQuestionnaireDesignApi(unique: string) {
  return request.get(`common/questionnaire_info/${unique}`);
}

/**
 * 提交调查问卷数据
 * @param table_name 表名
 * @param unique 表单 ID
 * @param data 提交数据
 * @returns
 */
export function crudModuleQuestionnaireSaveDataApi(table_name: string, unique: string, data: object) {
  return request.post(`common/questionnaire_save/${table_name}/${unique}`, data);
}
