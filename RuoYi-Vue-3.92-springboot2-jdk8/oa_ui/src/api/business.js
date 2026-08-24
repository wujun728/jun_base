import request from './request'

/**
 * @description 获取审批配置列表
 */
export function entListApi(data) {
  return request.get('approve/config', data)
}

/**
 * @description 加签
 */
export function approveSignApi(id,data) {
  return request.post(`approve/apply/sign/${id}`, data)
}
/**
 * @description 转审
 */
export function approveTransferApi(id,data) {
  return request.post(`approve/apply/transfer/${id}`, data)
}

/**
 * @description 保存审批配置
 */
export function entAddApi(data) {
  return request.post('approve/config', data)
}

/**
 * @description 获取审批配置详情
 */
export function entInfoApi(config) {
  return request.get(`approve/config/${config}/edit`)
}

/**
 * @description 修改审批配置详情
 */
export function entEditApi(config, data) {
  return request.put(`approve/config/${config}`, data)
}

/**
 * @description 删除审批配置详情
 */
export function entDeleteApi(config) {
  return request.delete(`approve/config/${config}`)
}

/**
 * @description 显示隐藏审批配置
 */
export function entChangeApi(config, data) {
  return request.get(`approve/config/${config}`, data)
}

/**
 * 个人办公-审批-创建审批
 * @description 删除审批配置详情
 */
export function approveApplyFormApi(id, data) {
  return request.get(`approve/apply/form/${id}`, data)
}

/**
 * 个人办公-审批-获取审批人员列表
 * @description 获取审批人员列表
 */
export function approveApplyListApi(id, data) {
  return request.post(`approve/apply/form/${id}`, data)
}

/**
 * 个人办公-审批-保存审批
 * @description 保存审批
 */
export function approveApplySaveApi(id, data) {
  return request.post(`approve/apply/save/${id}`, data)
}

/**
 * 个人办公-审批-列表
 * @description 列表
 */
export function approveApplyApi(data) {
  return request.get(`approve/apply`, data)
}
/**
 * 个人办公-审批-导出
 * @description 列表
 */
export function approveApplyExportApi(data) {
  return request.get(`approve/apply/export`, data)
}

/**
 * 个人办公-审批-查看详情/编辑详情
 * @description 查看详情
 */
export function approveApplyEditApi(id, data) {
  return request.get(`approve/apply/${id}/edit`, data)
}

/**
 * 个人办公-审批-修改审批
 * @description 修改审批
 */
export function approveApplyPutEditApi(id, data) {
  return request.put(`approve/apply/${id}`, data)
}

/**
 * 保存审批评价
 */
export function approveReplyApi(data) {
  return request.post(`approve/reply`, data)
}

/**
 * 删除审批评价
 */
export function approveReplyDelApi(reply) {
  return request.delete(`approve/reply/${reply}`)
}

/**
 * 处理审批申请
 */
export function approveVerifyStatusApi(id, status) {
  return request.get(`approve/apply/verify/${id}/${status}`)
}

/**
 * 审批-撤销审批
 */
export function approveApplyRevokeApi(id,data) {
  return request.post(`approve/apply/revoke/${id}`,data)
}
/**
 * 审批-催办审批
 */
export function approveApplyUrgeApi(id) {
  return request.get(`approve/apply/urge/${id}`)
}

/**
 * 审批-删除
 * @description 删除 approve/config/search/{types}
 */
export function approveApplyDeleteApi(id) {
  return request.delete(`approve/apply/${id}`)
}

/**
 * 审批-获取审批类型筛选列表
 * @description 获取审批类型筛选列表
 */
export function approveConfigSearchApi(types) {
  return request.get(`approve/config/search/${types}`)
}

/**
 * 审批-获取异常日期列表接口
 * @description 获取异常日期列表接口
 */
export function attendanceAbnormalDateApi() {
  return request.get(`attendance/abnormal_date`)
}

/**
 * 审批-获取异常记录列表接口
 * @description 获取异常记录列表接口
 */
export function attendanceAbnormalRecordApi(id) {
  return request.get(`attendance/abnormal_record/${id}`)
}

/**
 * 假期类型-获取假期类型列表接口
 */
export function approveHolidayTypeApi(data) {
  return request.get(`approve/holiday_type/list`, data)
}

/**
 * 假期类型-保存假期类型
 */
export function saveHolidayTypeApi(data) {
  return request.post(`approve/holiday_type`, data)
}

/**
 * 假期类型-修改假期类型
 */
export function putHolidayTypeApi(id, data) {
  return request.put(`approve/holiday_type/${id}`, data)
}

/**
 * 假期类型-删除
 */
export function holidayTypeDeleteApi(id) {
  return request.delete(`approve/holiday_type/${id}`)
}

/**
 * 假期类型-获取假期类型详情
 */
export function approveHolidayTypeInfoApi(id) {
  return request.get(`approve/holiday_type/info/${id}`)
}

/**
 * 假期类型-假期类型下拉列表接口
 */
export function approveHolidayTypeSelectApi() {
  return request.get(`approve/holiday_type/select`)
}

/**
 * 汇报人-获取汇报人列表接口
 */
export function dailyReportMemberApi() {
  return request.get(`daily/report_member`)
}
