import request from "../utils/request";

/**
 * 获取客户列表
 * @return {*}
 */
export function customerListApi(data: object) {
  return request.get("client/customer", data);
}

/**
 * v1.4退回公海客户
 * @return {*}
 */
export function customerReturnApi(id: number, data: object) {
  return request.post(`client/customer/return/${id}`, data);
}

/**
 * 获取发票列表
 * @return {*}
 */
export function clientInvoiceApi(data: object) {
  return request.get("client/invoice", data);
}

/**
 * 获取发票客户合同金额
 * @return {*}
 */
export function contractPriceApi(id: number) {
  return request.get(`client/contract/price/${id}`);
}

/**
 * 发票：发票备注接口
 * @return {*}
 */
export function clientInvoiceMarkApi(id: number, data: object) {
  return request.post(`client/invoice/mark/${id}`, data);
}

/**
 * 发票：开票撤回接口
 * @return {*}
 */
export function clientInvoiceWithdrawApi(id: number, data: object) {
  return request.post(`client/invoice/withdraw/${id}`, data);
}

/**
 * 发票：发票作废申请
 * @return {*}
 */
export function clientInvoiceInvalidApplyApi(id: number, data: object) {
  return request.post(`client/invoice/invalid_apply/${id}`, data);
}

/**
 * 发票：获取发票详情
 * @return {*}
 */
export function clientInvoiceDetailsApi(id: number) {
  return request.get(`client/invoice/info/${id}`);
}
/**
 * 发票：重新提交发票
 * @return {*}
 */
export function clientInvoicePutApi(invoice: number, data: object) {
  return request.put(`client/invoice/${invoice}`, data);
}

/**
 * 发票：获取未开票付款列表
 * @return {*}
 */
export function unInvoicedListApi(data: object) {
  return request.get("client/bill/invoicing", data);
}

/**
 * 发票：获取发票类目
 * @return {*}
 */
export function invoiceCategorysApi() {
  return request.get("client/invoice_category");
}

/**
 * 发票：申请发票
 * @return {*}
 */
export function clientInvoiceSaveApi(data: object) {
  return request.post("client/invoice", data);
}

/**
 * 客户：获取客户标签
 * @return {*}
 */
export function clientlabelApi() {
  return request.get("client/label");
}

/**
 * 客户：添加客户-v1.4保存客户信息
 * @return {*}
 */
export function clientSaveApi(data: object) {
  return request.post("client/customer", data);
}

/**
 * 客户：添加合同-v1.4保存合同信息
 * @return {*}
 */
export function contractSaveApi(data: object) {
  return request.post("client/contract", data);
}
/**
 * 客户：编辑合同-v1.4保存合同信息
 * @return {*}
 */
export function contractEditSaveApi(id: number, data: object) {
  return request.put(`client/contract/${id}`, data);
}

/**
 * 客户：编辑客户-v1.4获取客户信息
 * @return {*}
 */
export function clientEditInfoApi(id: number) {
  return request.get(`client/customer/${id}/edit`);
}

/**
 * 合同：合同筛选字典
 * @return {*}
 */
export function dictSelectApi(data: object) {
  return request.get(`dict/data/select`, data);
}

/**
 * 客户：编辑合同-v1.4获取合同信息
 * @return {*}
 */
export function contractEditInfoApi(id: number): any {
  return request.get(`client/contract/${id}/edit`);
}
/**
 * 客户：编辑客户-v1.4修改客户信息
 * @return {*}
 */
export function clientPutApi(id: number, data: object): any {
  return request.put(`client/customer/${id}`, data);
}

/**
 * 客户：业绩简报
 * @return {*}
 */
export function briefStatisticsApi(data: object) {
  return request.get(`client/customer/brief_statistics`, data);
}
/**
 * 客户：合同业绩
 * @return {*}
 */
export function contractRankApi(data: object) {
  return request.get(`client/customer/contract_rank`, data);
}

/**
 * 客户：业务员业绩
 * @return {*}
 */
export function salesmanRankApi(data: object) {
  return request.get(`client/customer/salesman_rank`, data);
}

/**
 * 客户：删除客户信息
 * @return {*}
 */
export function clientDeleteApi(data: object) {
  return request.delete(`client/customer/${data}`);
}

/**
 * 客户：修改客户状态
 * @return {*}
 */
export function clientStatusApi(id: number, status: number) {
  return request.post(`client/customer/subscribe/${id}/${status}`);
}

/**
 * 客户：添加客户表单v1.4
 * @return {*}
 */
export function clientCreateFormApi() {
  return request.get(`client/customer/create`);
}
/**
 * 客户：客户标为流失v1.4
 * @return {*}
 */
export function clientlostApi(id: number) {
  return request.post(`client/customer/lost/${id}`);
}
/**
 * 客户：客户取消流失v1.4
 * @return {*}
 */
export function clientCancelLostApi(id: number) {
  return request.post(`client/customer/cancel_lost/${id}`);
}
/**
 * 客户：客户领取v1.4
 * @return {*}
 */
export function clientclaimApi(id: number) {
  return request.post(`client/customer/claim/${id}`);
}

/**
 * 客户：获取客户详情信息
 * @return {*}
 */
export function clientInfoApi(id: number) {
  return request.get(`client/customer/info/${id}`);
}

/**
 * 获取客户来源
 * @return {*}
 * @keys way:客户来源
 * @keys renew:续费类型
 * @keys invoice:发票类目
 * @keys cate:客户分类
 */
export function clientSourceApi(data: object) {
  return request.get("client/config/group", data);
}

/**
 * 客户： 保存客户跟进记录
 * @return {*}
 */
export function followSaveApi(data: object) {
  return request.post("client/follow", data);
}

/**
 * 发票： 获取发票最新记录
 * @return {*}
 */
export function recordListApi(id: number) {
  return request.get(`client/invoice/record/last/${id}`);
}

/**
 * 客户：获取客户合同列表接口
 * @return {*}
 */
export function clientContractListApi(data: object) {
  return request.get("client/contract", data);
}

/**
 * 客户：获取客户合同新增表单
 * @return {*}
 */
export function contractCreateFormApi() {
  return request.get("client/contract/create");
}

/**
 * 客户：获取客户下拉客户名称
 * @return {*}
 */
export function customerSelectApi() {
  return request.get("client/customer/select");
}

/**
 * 客户：获取客户合同详情接口
 * @return {*}
 */
export function clientContractDetailApi(id: number) {
  return request.get(`client/contract/info/${id}`);
}
/**
 * 客户：删除合同接口
 * @return {*}
 */
export function contractDeleteApi(contract: number) {
  return request.delete(`client/contract/${contract}`);
}

/**
 * 客户： 修改客户合同状态接口
  @return {*}
 */
export function clientContractStatusApi(id: number, status: number, data: object) {
  return request.post(`client/contract/subscribe/${id}/${status}`, data);
}

/**
 * 客户： 批量设置客户标签
  @return {*}
 */

export function clientContractLabelApi(id: number, data: object) {
  return request.post(`client/customer/label/${id}`, data);
}

/**
 * 客户：获取客户跟进记录列表
 * @return {*}
 */
export function followListApi(data: object) {
  return request.get("client/follow", data);
}

/**
 * 客户： 删除客户跟进记录
 * @return {*}
 */
export function followDeleteApi(follow: number) {
  return request.delete(`client/follow/${follow}`);
}

/**
 * 客户：编辑客户跟进记录
 * @return {*}
 */
export function followPutApi(follow: number, data: object) {
  return request.put(`client/follow/${follow}`, data);
}

/**
 * 客户：客户付款记录列表
 * @return {*}
 */
export function billListApi(data: object) {
  return request.get("client/bill", data);
}

/**
 * 客户：客户付款撤回
 * @return {*}
 */
export function billWithdrawApi(id: number) {
  return request.post(`client/bill/withdraw/${id}`);
}

/**
 * 客户： 客户付款备注
 * @return {*}
 */
export function billMarkApi(id: number, data: object) {
  return request.post(`client/bill/mark/${id}`, data);
}

/**
 * 客户： 客户付款记录详情
 * @return {*}
 */
export function billDetailApi(id: number) {
  return request.get(`client/bill/info/${id}`);
}
/**
 * 客户： 修改客户付款
 * @return {*}
 */
export function billPutApi(bill: number, data: object) {
  return request.put(`client/bill/${bill}`, data);
}

/**
 * 删除付款记录接口
 * @return {*}
 */
export function billDeleteApi(bill: number) {
  return request.delete(`client/bill/${bill}`);
}

/**
 * 客户：获取支付方式接口
 * @return {*}
 */
export function payTypeApi() {
  return request.get("company/pay_type");
}

/**
 * 客户：添加付款记录保存接口
 * @return {*}
 */
export function billSaveApi(data: object) {
  return request.post("client/bill", data);
}

/**
 * 客户：根据客户id获取合同列下拉选择
 * @return {*}
 */
export function contractSelectApi(eid: number) {
  return request.get(`client/contract/select/${eid}`);
}

/**
 * 获取付款提醒列表
 * @return {*}
 */
export function contractRemindListApi(data: object) {
  return request.get("client/remind", data);
}

/**
 * 客户： 获取客户联系人列表
 * @return {*}
 */
export function clientLiaisonApi(data: object) {
  return request.get("client/liaison", data);
}

/**
 * 客户： 1.4获取客户联系人表单
 * @return {*}
 */
export function liaisonCreateFormApi() {
  return request.get("client/liaison/create");
}
/**
 * 客户： 保存客户联系人
 * @return {*}
 */
export function liaisonSaveApi(data: object) {
  return request.post("client/liaison", data);
}

/**
 * 客户： 删除客户联系人列表
 * @return {*}
 */
export function liaisonDeleteApi(liaison: number) {
  return request.delete(`client/liaison/${liaison}`);
}
/**
 * 客户： 编辑客户联系人列表
 * @return {*}
 */
export function liaisonPutApi(id: number, data: object) {
  return request.put(`client/liaison/${id}`, data);
}

/**
 *删除付款提醒接口
 * @return {*}
 */
export function clientRemindDeletaApi(id: number): any {
  return request.delete(`client/remind/${id}`);
}

// /**
//  * 客户： 添加合同接口
//  * @return {*}
//  */
// export function contractSaveApi(data : object) {
//   return request.post('client/contract', data)
// }

/**
 * 客户： 获取合同分类接口
 * @return {*}
 */
export function contractCategoryApi(): any {
  return request.get("client/contract_category");
}

/**
 *保存付款提醒接口
 * @return {*}
 */
export function clientRemindSaveApi(data: object): any {
  return request.post("client/remind/", data);
}

/**
 * 获取付款提醒详情接口
 * @return {*}
 */
export function clientRemindDetailApi(id: number): any {
  return request.get(`client/remind/info/${id}`);
}

/**
 * 修改付款提醒接口
 * @return {*}
 */
export function clientRemindEditApi(id: number, data: object): any {
  return request.put(`client/remind/${id}`, data);
}

/**
 * 获取客户文件列表
 * @return {*}
 */
export function clientFileListApi(data: object): any {
  return request.get("client/file", data);
}

/**
 * 删除客户文件接口
 * @return {*}
 */
export function clientFileDeleteApi(id: number): any {
  return request.delete(`client/file/delete/${id}`);
}

/**
 * 重命名客户文件接口
 * @return {*}
 */
export function clientFileRealNameApi(id: number, data: object): any {
  return request.put(`client/file/real_name/${id}`, data);
}

/**
 * 获取联系人详情接口
 * @return {*}
 */
export function clientLiaisonDetailApi(id: number): any {
  return request.get(`client/liaison/${id}/edit`);
}

/**
 * 省市区数据
 * @return {*}
 */
export function commonCityApi(): any {
  return request.get("common/city");
}

/**
 * 客户转移接口
 * @retrue {*}
 */
export function clientDataShiftApi(id: number, data: object) {
  return request.post(`client/customer/shift/${id}`, data);
}

/**
 * 客户合同转移业务员
 * @retrue {*}
 */
export function clientContractShiftApi(id: number, data: object) {
  return request.post(`client/contract/shift/${id}`, data);
}

/**
 * 合同发票转移业务员
 * @retrue {*}
 */
export function clientInvoiceShiftApi(data: object) {
  return request.post("client/invoice/shift", data);
}

/**
 * 获取客户合同账目分类接口
 * @retrue {*}
 */
export function clientContractBillCateApi(id: number) {
  return request.get(`client/contract/bill_cate/${id}`);
}

/**
 * 保存合同转附件接口
 * @retrue {*}
 */
export function clientContractResourceApi(data: object) {
  return request.post(`client/resource`, data);
}

/**
 * 获取合同附件列表接口
 * @retrue {*}
 */
export function clientContractResourceListApi(data: object) {
  return request.get(`client/resource`, data);
}

/**
 * 删除合同转附件接口
 * @retrue {*}
 */
export function clientContractResourceDeleteApi(id: number) {
  return request.delete(`client/resource/${id}`);
}

/**
 * 获取合同附件详情接口
 * @retrue {*}
 */
export function clientContractResourceDetailApi(id: number) {
  return request.get(`client/resource/info/${id}`);
}

/**
 * 修改合同转附件接口
 * @retrue {*}
 */
export function clientContractResourceEditApi(id: number, data: object) {
  return request.put(`client/resource/${id}`, data);
}

/**
 * 合同付款统计接口
 * @retrue {*}
 */
export function clientcontractStatisticsApi(id: number) {
  return request.get(`client/bill/contract/statistics/${id}`);
}

/**
 * 付款提醒放弃接口
 * @retrue {*}
 */
export function clientRemindAbjureApi(id: number) {
  return request.post(`client/remind/abjure/${id}`);
}

/**
 * 获取客户审批配置ID接口
 * @retrue {*}
 */
export function configApproveApi(type: number = 0) {
  return request.get(`client/customer/approve/${type}`);
}
