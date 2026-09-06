import request from "../utils/request";

/**
 * @description 获取审批配置列表
 */
export function entListApi(data: object) {
  return request.get("approve/config", data);
}

/**
 * @description 加签审批
 */
export function entSignApi(id: number, data: object) {
  return request.post(`approve/sign/${id}`, data);
}

/**
 * @description 转审审批
 */
export function entTransferApi(id: number, data: object) {
  return request.post(`approve/transfer/${id}`, data);
}

/**
 * @description 保存审批配置
 */
export function entAddApi(data: object) {
  return request.post("approve/config", data);
}

/**
 * @description 获取审批配置详情
 */
export function entInfoApi(config: number) {
  return request.get(`approve/config/${config}/edit`);
}
/**
 * @description 审批催办
 */
export function approveUrgeApi(id: number) {
  return request.get(`approve/apply/urge/${id}`);
}

/**
 * @description 修改审批配置详情
 */
export function entEditApi(config: number, data: object) {
  return request.put(`approve/config/${config}`, data);
}

/**
 * @description 删除审批配置详情
 */
export function entDeleteApi(config: number) {
  return request.delete(`approve/config/${config}`);
}

/**
 * @description 显示隐藏审批配置
 */
export function entChangeApi(config: number, data: object) {
  return request.get(`approve/config/${config}`, data);
}

/**
 * 个人办公-审批-创建审批
 * @description 创建审批
 */
export function approveApplyFormApi(id: number, data: object) {
  return request.get(`approve/apply/form/${id}`, data);
}

/**
 * 个人办公-审批-获取审批人员列表
 * @description 获取审批人员列表
 */
export function approveApplyListApi(id: number, data: object) {
  return request.post(`approve/apply/form/${id}`, data);
}

/**
 * 个人办公-审批-保存审批
 * @description 保存审批
 */
export function approveApplySaveApi(id: number, data: object) {
  return request.post(`approve/apply/save/${id}`, data);
}

/**
 * 个人办公-审批-列表
 * @description 列表
 */
export function approveApplyApi(data: object) {
  return request.get(`approve/apply`, data);
}

/**
 * 个人办公-审批-查看详情/编辑详情
 * @description 查看详情
 */
export function approveApplyEditApi(id: number, data: object) {
  return request.get(`approve/apply/${id}/edit`, data);
}

/**
 * 个人办公-审批-修改审批
 * @description 修改审批
 */
export function approveApplyPutEditApi(id: number, data: object) {
  return request.put(`approve/apply/${id}`, data);
}

/**
 * 保存审批评价
 */
export function approveReplyApi(data: object) {
  return request.post(`approve/reply`, data);
}

/**
 * 删除审批评价
 */
export function approveReplyDelApi(reply: number) {
  return request.delete(`approve/reply/${reply}`);
}

/**
 * 处理审批申请
 */
export function approveVerifyStatusApi(id: number, status: number) {
  return request.get(`approve/apply/verify/${id}/${status}`);
}

/**
 * 审批-撤销审批
 */
export function approveApplyRevokeApi(id: number, data: object) {
  return request.post(`approve/apply/revoke/${id}`, data);
}

/**
 * 审批-删除
 * @description 删除 approve/config/search/{types}
 */
export function approveApplyDeleteApi(id: number) {
  return request.delete(`approve/apply/${id}`);
}

/**
 * 审批-获取审批类型筛选列表
 * @description 获取审批类型筛选列表
 */
export function approveConfigSearchApi(types: number) {
  return request.get(`approve/config/search/${types}`);
}

/**
 * 审批-获取异常日期列表接口
 * @description 获取异常日期列表接口
 */
export function attendanceAbnormalDateApi() {
  return request.get(`attendance/abnormal_date`);
}

/**
* 审批-获取异常记录列表接口
* @description 获取异常记录列表接口
*/
export function attendanceAbnormalRecordApi(id: number) {
  return request.get(`attendance/abnormal_record/${id}`);
}

/**
* 审批-获取请假类型列表接口
*/
export function holidayTypeApi() {
  return request.get(`approve/config/holiday`);
}
