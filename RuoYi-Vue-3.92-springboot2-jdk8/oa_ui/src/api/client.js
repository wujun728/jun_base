import request from '@/api/request'
//todo 客户管理相关接口

/**
 * 客户管理--客户管理--企业设置--保存
 * @return {*}
 */
export function clientConfigSaveApi(data) {
  return request.post(`client/config/save`, data)
}
/**
 * 客户管理--企业设置--客户标签--列表所有
 * @return {*}
 */
export function clientConfigGroupApi(data) {
  return request.post(`client/config/group`, data)
}

/**
 * 客户管理--客户列表--列表
 * @return {*}
 */
export function clientDataListApi(data) {
  return request.get(`client/data`, data)
}

/**
 * 客户管理--V1.4保存客户表单
 * @return {*}
 */
export function clientCustomerSaveApi(data) {
  return request.post(`client/customer`, data)
}

/**
 * 客户管理--客户列表--添加客户
 * @return {*}
 */
export function clientDataSaveApi(data) {
  return request.post(`client/data`, data)
}

/**
 * 客户管理--客户列表--删除客户信息
 * @return {*}
 */
export function clientDataDeleteApi(id) {
  return request.delete(`client/data/${id}`)
}

/**
 * 客户管理--客户列表--修改成交状态
 * @return {*}
 */
export function clientDataStatusApi(id, data) {
  return request.post(`client/data/status/${id}`, data)
}
/**
 * 合同管理-状态统计
 * @return {*}
 */
export function contractNumApi(data) {
  return request.get(`client/contracts/num`, data)
}

/**
 * 客户管理--客户列表--批量设置标签
 * @return {*}
 */
export function clientDataLabelApi(data) {
  return request.post(`client/customer/label`, data)
}

/**
 * 客户管理--客户列表--保存编辑客户
 * @return {*}
 */
export function clientDataEditApi(id, data) {
  return request.put(`client/data/${id}`, data)
}
/**
 * 客户管理--客户列表--客户详情
 * @return {*}
 */
export function clientDataInfoApi(id) {
  return request.get(`client/data/${id}`)
}

/**
 * 客户管理--客户列表--保存联系人
 * @return {*}
 */
export function clientLiaisonSaveApi(data) {
  return request.post(`client/liaisons`, data)
}

/**
 * 客户管理--客户列表--修改联系人
 * @return {*}
 */
export function clientLiaisonEditApi(id, data) {
  return request.put(`client/liaisons/${id}`, data)
}

/**
 * 客户管理--客户列表--联系人列表
 * @return {*}
 */
export function clientLiaisonListApi(data) {
  return request.get(`client/liaisons`, data)
}

/**
 * 客户管理--客户列表--删除联系人
 * @return {*}
 */
export function clientLiaisonDeleteApi(id) {
  return request.delete(`client/liaisons/${id}`)
}

/**
 * 客户管理--合同列表--添加合同
 * @return {*}
 */
export function clientContractSaveApi(data) {
  return request.post(`client/contracts`, data)
}

/**
 * 客户管理--合同列表--修改合同
 * @return {*}
 */
export function clientContractEditApi(id, data) {
  return request.put(`client/contracts/${id}`, data)
}
/**
 * 收支记账操作记录---列表
 * @return {*}
 */
export function billRecordApi(id) {
  return request.get(`bill/record/${id}`)
}
/**
 * 客户管理--合同列表--撤回付款与续费记录
 * @return {*}
 */
export function clientBillPutApi(id) {
  return request.put(`client/bill/withdraw/${id}`)
}

/**
 * 客户管理--合同列表--填写备注信息
 * @return {*}
 */
export function clientMarkApi(id, data) {
  return request.put(`client/bill/mark/${id}`, data)
}

/**
 * 客户管理--合同列表--付款提醒--填写备注信息
 * @return {*}
 */
export function clientRemindMarkApi(id, data) {
  return request.put(`client/remind/mark/${id}`, data)
}

/**
 * 财务审核--资金流水审核
 * @return {*}
 */
export function clientBillStatusApi(id, data) {
  return request.post(`client/bill/status/${id}`, data)
}

/**
 * 发票管理--发票申请列表
 * @return {*}
 */
export function clientInvoiceListApi(data) {
  return request.get(`client/invoice`, data)
}

/**
 * 发票管理--保存发票申请
 * @return {*}
 */
export function clientInvoiceSaveApi(data) {
  return request.post(`client/invoice`, data)
}

/**
 * 发票管理--修改发票申请
 * @return {*}
 */
export function clientInvoicePutApi(invoice, data) {
  return request.put(`client/invoice/${invoice}`, data)
}

/**
 * 发票管理--获取在线开票uri
 * @param invoice
 * @return {*}
 */
export function clientInvoiceUriApi(invoice) {
  return request.get(`client/invoice/uri/${invoice}`)
}

/**
 * 发票管理--开票撤回
 * @return {*}
 */
export function recallStatus(id, data) {
  return request.put(`client/invoice/withdraw/${id}`, data)
}
/**
 * 发票管理--申请作废
 * @return {*}
 */
export function invalidApply(id, data) {
  return request.put(`client/invoice/invalid_apply/${id}`, data)
}

/**
 * 发票管理--发票删除
 * @return {*}
 */
export function clientInvoiceDeleteApi(id) {
  return request.delete(`client/invoice/${id}`)
}

/**
 * 发票管理--发票填写备注
 * @return {*}
 */
export function clientInvoiceMarkApi(id, data) {
  return request.put(`client/invoice/mark/${id}`, data)
}

/**
 * 发票管理--发票编辑
 * @return {*}
 */
export function clientInvoiceEditApi(id, data) {
  return request.put(`client/invoice/${id}`, data)
}

/**
 * 发票管理--发票审核与开票
 * @return {*} client/follow
 */
export function clientInvoiceStatusApi(id, data) {
  return request.post(`client/invoice/status/${id}`, data)
}
/**
 * 发票管理--发票审核与开票
 * @return {*} client/follow
 */
export function clientInvoiceStatusPutApi(id, data) {
  return request.put(`client/invoice/status/${id}`, data)
}

export function clientInvoiceStatus(id, data) {
  return request.put(`client/invoice/invalid_review/${id}`, data)
}
/**
 * 合同管理--付款撤回
 * @return {*} client/follow
 */
export function withdrawApi(id) {
  return request.put(`client/bill/withdraw/${id}`)
}

/**
 * 客户管理--跟进记录--保存客户提醒与跟进详情
 * @return {*}
 */
export function clientFollowSaveApi(data) {
  return request.post(`client/follow`, data)
}

/**
 * 客户管理--跟进记录--修改客户提醒与跟进详情
 * @return {*}
 */
export function clientFollowEditApi(id, data) {
  return request.put(`client/follow/${id}`, data)
}

/**
 * 客户管理--跟进记录--客户提醒与跟进列表
 * @return {*}
 */
export function clientFollowListApi(data) {
  return request.get(`client/follow`, data)
}

/**
 * 客户管理--跟进记录--删除客户提醒与跟进详情
 * @return {*}
 */
export function clientFollowDeleteApi(id) {
  return request.delete(`client/follow/${id}`)
}

/**
 * 客户管理--跟进记录--删除客户跟进记录附件
 * @return {*}
 */
export function clientFileDeleteApi(id) {
  return request.delete(`client/file/delete/${id}`)
}

/**
 * 客户管理--附件相关-附件列表
 * @return {*}
 */
export function clientFileListApi(data) {
  return request.get(`client/file/index`, data)
}
/**
 * 合同管理--附件相关-资料列表
 * @return {*}
 */
export function contracFileListApi(data) {
  return request.get(`client/resources`, data)
}
/**
 * 合同管理--附件相关-保存资料
 * @return {*}
 */
export function contracFileSaveApi(data) {
  return request.post(`client/resources`, data)
}
/**
 * 合同管理--附件相关-删除资料
 * @return {*}
 */
export function contracFileDeleteApi(id) {
  return request.delete(`client/resources/${id}`)
}
/**
 * 合同管理--附件相关-编辑合同资料
 * @return {*}
 */
export function contracFileEditApi(id, data) {
  return request.put(`client/resources/${id}`, data)
}

/**
 * 合同管理--付款提醒保存
 * @return {*}
 */
export function clientRemindSaveApi(data) {
  return request.post(`client/remind`, data)
}

/**
 * 合同管理--付款提醒编辑
 * @return {*}
 */
export function clientRemindEditApi(id, data) {
  return request.put(`client/remind/${id}`, data)
}

/**
 * 合同管理--付款提醒删除
 * @return {*}
 */
export function clientRemindDeleteApi(id) {
  return request.delete(`client/remind/${id}`)
}

/**
 * 合同管理--付款提醒列表
 * @return {*}
 */
export function clientRemindListApi(data) {
  return request.get(`client/remind`, data)
}
/**
 * 客户管理--批量设置转移
 * @return {*} client/contract/shift
 */
export function clientDataShiftApi(data) {
  return request.post(`client/data/shift`, data)
}

/**
 * 合同管理--批量设置转移
 * @return {*}
 */
export function clientContractShiftApi(data) {
  return request.post(`client/contract/shift`, data)
}
/**
 * 客户导入
 * @return {*}
 */
export function importExcel(data) {
  return request.post(`client/import`, data)
}

/**
 * 获取日报列表
 * @return {*}
 */
export function getCompleted(type) {
  return request.get(`daily/schedule_record/${type}`)
}
/**
 * 申请作废
 * @return {*}
 */
export function putInvoice(id, data) {
  return request.put(`client/invoice/invalid_apply/${id}`, data)
}
/**
 * 申请作废
 * @return {*}
 */
export function putInvalid(id, data) {
  return request.put(`client/invoice/invalid_review/${id}`, data)
}
/**
 * 文件重命名
 * @return {*}
 */
export function putRealName(id, data) {
  return request.put(`client/file/real_name/${id}`, data)
}
/**
 * 考核管理范围企业列表
 * enterprise/assess/scope_frame
 * @returns {*}
 */
export function entAssessScopeFrameApi() {
  return request.get(`enterprise/assess/scope_frame`)
}

/**
 获取付款记录详情接口
 * @param {Number} id
 * @returns {*}
 */
export function clientBillDetailApi(id) {
  return request.get(`client/bill/${id}`)
}
/**
 获取发票详情接口
 * @param {Number} id
 * @returns {*}
 */
export function clientInvoiceDetailApi(id) {
  return request.get(`client/invoice/info/${id}`)
}

/**
 获取客户合同详情接口
 * @param {Number} id
 * @returns {*}
 */
export function clientContractDetailApi(id) {
  return request.get(`client/contract/${id}`)
}

/**
 * 获取客户名称接口
 * @param {Number} id
 * @returns {*}
 */
export function clientNameApi() {
  return request.get(`client/data/select`)
}
/**
 * 获取客户跟进统计接口
 * @returns {*}
 * @param data
 */
export function clientFollowNumApi(data) {
  return request.get(`client/data/follow_num`, data)
}

/**
 * 获取汇报人查看汇报列表接口
 * @returns {*}
 * @param data
 */
export function dailyReportListApi(data) {
  return request.get(`daily/report_list`, data)
}

/**
 * 获取业务员字段数据接口
 * @param {string} custom_type
 * @returns {*}
 */
export function salesmanCustomApi(custom_type) {
  return request.get(`config/form/data/fields/${custom_type}`)
}

/**
 * 保存业务员字段数据接口
 * @return {*}
 */
export function saveSalesmanCustomApi(custom_type, data) {
  return request.put(`config/form/data/fields/${custom_type}`, data)
}
