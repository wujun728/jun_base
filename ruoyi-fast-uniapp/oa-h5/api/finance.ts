import request from "../utils/request";
/**
 * 获取付款详情接口
 * @return {*}
 */
export function financeBillDetailApi(id: number) {
  return request.get(`finance/bill/${id}`);
}

/**
 * 删除付款记录接口
 * @return {*}
 */
export function financeBillDeletelApi(id: number) {
  return request.delete(`client/bill/finance/delete/${id}`);
}

/**
 * 财务审核付款记录
 * @return {*}
 */
export function financeBillStatuslApi(id: number, data: object) {
  return request.post(`client/bill/status/${id}`, data);
}

/**
 * 修改付款记录接口
 */
export function financeBillEditApi(id: number, data: object) {
  return request.post(`client/bill/finance/update/${id}`, data);
}

/**
 * 获取发票列表接口
 * @return {*}
 */
export function financeInvoiceListlApi(data: object) {
  return request.get(`client/invoice/list`, data);
}
/**
 * 审核发票接口
 * @return {*}
 */
export function financeInvoiceStatusApi(id: number, data: object) {
  return request.post(`client/invoice/status/${id}`, data);
}

/**
 * 发票作废接口
 * @return {*}
 */
export function financeInvoiceInvalidApi(id: number, data: object) {
  return request.post(`client/invoice/invalid/${id}`, data);
}

/**
 * 获取账目分类列表接口
 * @return {*}
 */
export function financeBillCateApi(data: object) {
  return request.get(`finance/bill_cate`, data);
}

/**
 * 获取账目列表接口
 * @param data
 * @returns {*}
 */
export function financeBillList(data: object) {
  return request.post(`finance/bill/list`, data);
}

/**
 * 获取账目详情接口
 * @return {*}
 */
export function financeBillDetailsApi(id: string) {
  return request.get(`finance/bill/detail/${id}`);
}

/**
 * 删除账目流水信息
 * @param id string
 * @returns {*}
 */
export function financeBillDetailsDelApi(id: string) {
  return request.delete(`finance/bill/${id}`);
}

/**
 * 新增账目流水
 * @param data object
 * @returns {*}
 */
export function financeBillDetailsAddApi(data: object) {
  return request.post(`finance/bill`, data);
}

/**
 * 修改账目流水
 * @param id string
 * @param data object
 * @returns {*}
 */
export function financeBillDetailsEditApi(id: string, data: object) {
  return request.put(`finance/bill/${id}`, data);
}

/**
 * 获取账单统计简报数据
 * @param data object
 * @returns {*}
 */
export function financeBillStatisticTotalApi(data: object) {
  return request.post(`finance/bill/chart`, data);
}

/**
 * 获取账单统计图标数据
 * @param data object
 * @returns {*}
 */
export function financeBillStatisticChartApi(data: object) {
  return request.post(`finance/bill/rank_analysis`, data);
}
