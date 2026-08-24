import request from './request'
/**
 * @description 企业日志 --- 列表
 * @param data
 */
export function enterpriseLogApi(data) {
  return request.get(`system/log`, data)
}

/**
 * @description 当前登录企业详情
 * @param data
 */
export function enterpriseEntInfoApi(data) {
  return request.get(`company/info`, data)
}
/**
 * @description 业绩统计-圆环统计下钻
 * @param data
 */
export function contracRankApi(data) {
  return request.post(`client/customer/contract_rank`, data)
}
/**
 * @description 业绩统计-业绩趋势图
 * @param data
 */
export function trendStatisticsApi(data) {
  return request.post(`client/customer/trend_statistics`, data)
}

/**
 * @description 当前待审核数量统计
 * @param type
 */
export function getQuantity(type) {
  return request.get(`company/quantity/${type}`)
}

/**
 * @description 合同分类创建表单
 */
export function createContract() {
  return request.get(`client/contract_category/create`)
}
/**
 * @description 合同分类列表
 * @param data
 */
export function contractList(data) {
  return request.get(`client/contract_category`, data)
}
/**
 * @description 合同分类修改列表
 * @param id
 */
export function editContractCreate(id) {
  return request.get(`client/contract_category/${id}/edit`)
}
/**
 * @description 合同分类删除
 * @param id
 */
export function deleteContract(id) {
  return request.delete(`client/contract_category/${id}`)
}
/**
 * @description 点击饼状图
 * @param data
 */
export function billChangeBie(data) {
  return request.post(`bill/rank_analysis`, data)
}

/**
 * @description 修改当前登录企业
 * @param data
 */
export function entInfoUpdateApi(data) {
  return request.put(`company/info`, data)
}
/**
 * @description 修改当前登录密码
 * @param data
 */
export function userPwdPut(data) {
  return request.post(`system/roles/pwd`, data)
}

/**
 * @description 获取汇报列表
 * @param data
 */
export function enterpriseDailyApi(data) {
  return request.get(`daily`, data)
}
/**
 * @description 获取汇报列表导出
 * @param data
 */
export function enterpriseDailyExportApi(data) {
  return request.get(`daily/export`, data)
}

/**
 * @description 员工档案列表
 * @param data
 */
export function enterpriseCardApi(data) {
  return request.post(`company/card`, data)
}
/**
 * @description 导入员工档案列表
 * @param data
 */
export function importCardApi(data) {
  return request.post(`company/card/import`, data)
}
/**
 * @description 修改客户标签
 * @param label
 * @param data
 */
export function putcLientLabel(label, data) {
  return request.put(`client/labels/${label}`, data)
}
/**
 * 人事管理--模板下载
 * @return {*}
 */
export function getTemp() {
  return request.get(`company/card/import/temp`)
}

/**
 * @description 批量删除员工档案列表
 * @param data
 */
export function enterpriseBatchApi(data) {
  return request.delete(`company/card/batch`, data)
}
/**
 * @description 保存修改员工档案
 * @param id
 * @param data
 */
export function putCardApi(id, data) {
  return request.put(`company/card/${id}`, data)
}

/**
 * @description 修改成员获取信息接口
 * @param id
 */
export function enterpriseCardIdApi(id) {
  return request.get(`company/card/info/${id}`)
}

/**
 * @description 获取工作经历创建接口
 * @param data
 */
export function enterpriseWorkCreateApi(data) {
  return request.get(`work/create`, data)
}
/**
 * @description 编辑工作经历创建接口
 * @param work
 * @param id
 */
export function enterpriseWorkEditApi(work) {
  return request.get(`work/${work}/edit`)
}
/**
 * @description 删除工作经历接口
 * @param work
 * @param data
 */
export function enterpriseWorkDeleteApi(work, data) {
  return request.delete(`work/${work}`, data)
}

/**
 * @description 删除教育经历接口
 * @param education
 * @param data
 */
export function educationDeleteApi(education) {
  return request.delete(`education/${education}`)
}
/**
 * @description 删除单个员工
 * @param id
 */
export function deleteCard(id) {
  return request.delete(`company/card/${id}`)
}

/**
 * @description 获取教育经历创建接口
 * @param data
 */
export function enterpriseEducationCreateApi(data) {
  return request.get(`education/create`, data)
}

/**
 * @description 获取工作经历列表接口
 * @param data
 */
export function getWorkList(data) {
  return request.get(`work`, data)
}

/**
 * @description 教育经历列表接口
 * @param data
 */
export function getEducationList(data) {
  return request.get(`education`, data)
}
/**
 * @description 入职接口接口
 * @param id
 */
export function entryCard(id) {
  return request.post(`company/card/entry/${id}`)
}
/**
 * @description 编辑教育经历接口
 * @param education
 * @param data
 */
export function enterpriseEducationApi(education) {
  return request.get(`education/${education}/edit`)
}

/**
 * @description 删除员工信息
 * @param id
 */
export function enterpriseCardDeleteApi(id) {
  return request.delete(`company/card/${id}`)
}

/**
 * 我的日报-保存日报
 * @param {Object} data
 * @return {*}
 */
export function enterpriseDaily(data) {
  return request.post('daily', data)
}

/**
 * 我的日报-修改日报
 * @param  {Object} data
 * @param {Number} id
 * @return {*}
 */
export function getDailyEdit(data, id) {
  return request.put(`daily/${id}`, data)
}

/**
 * 我的日报-日报列表
 * @param {Object} data
 * @return {*}
 */
export function getEnterpriseDaily(data) {
  return request.get('daily', data)
}

/**
 * 我的日报-获取人员列表
 * @return {*}
 */
export function getEnterpriseUsersApi() {
  return request.get('daily/users')
}

/**
 * 我的日报-获取查看日报
 * @param  {Number} edit
 * @return {*}
 */
export function getEnterpriseEdit(edit) {
  return request.get(`daily/${edit}/edit`)
}

/**
 * 我的日报-日报回复
 * @param data
 * @return {*}
 */
export function dailyReply(data) {
  return request.post('daily/reply', data)
}

/**
 * 我的日报-删除日报回复
 * @return {*}
 * @param id
 * @param dailyId
 */
export function dailydel(id, dailyId) {
  return request.delete(`daily/reply/${id}/${dailyId}`)
}

/**
 * 保存岗位
 * @param data
 * @return {*}
 */
export function endJobSaveApi(data) {
  return request.post(`jobs`, data)
}

/**
 * 获取岗位tree型列表接口
 * @param data
 * @return {*}
 */
export function endJobApi(data) {
  return request.get(`jobs`, data)
}

/**
 * 修改岗位状态
 * @return {*}
 * @param id
 * @param status
 */
export function endJobStatusApi(id, status) {
  return request.put(`jobs/show/${id}/${status}`)
}

/**
 * 获取岗位信息
 * @param id
 * @param data
 * @return {*}
 */
export function endJobInfoApi(id, data) {
  return request.get(`jobs/${id}/edit`)
}

/**
 * 放弃续费提醒
 * @return {*}
 * @param id
 */
export function remindAbjureApi(id) {
  return request.put(`client/remind/abjure/${id}`)
}

/**
 * 获取续费到期统计
 * @return {*}
 * @param cid
 */
export function renewCensusApi(cid) {
  return request.get(`client/bill/renew_census/${cid}`)
}

/**
 * 获取添加类型
 * @return {*}
 */
export function addRanktype() {
  return request.get(`enterprise/rankType/create`)
}

/**
 * 获取职级类型列表
 * @param data
 * @return {*}
 */
export function rankTypeInfo(data) {
  return request.get(`enterprise/rankType`, data)
}

/**
 * 修改职级类型状态
 * @param id
 * @param data
 * @return {*}
 */
export function rankTypeStatusApi(id, data) {
  return request.get(`enterprise/rankType/${id}`, data)
}

/**
 * 修改职级类别信息
 * @return {*}
 * @param rankCate
 */
export function rankTypeEdit(rankCate) {
  return request.get(`enterprise/rankType/${rankCate}/edit`)
}

/**
 * 删除职级类别信息
 * @return {*}
 * @param rankCate
 */
export function rankTypeDelet(rankCate) {
  return request.delete(`enterprise/rankType/${rankCate}`)
}

/**
 * 删除岗位
 * @param id
 * @return {*}
 */
export function endJobDeleteApi(id) {
  return request.delete(`jobs/${id}`)
}

/**
 * 获取创建岗位接口
 * @param data
 * @return {*}
 */
export function jobsCreate(data) {
  return request.get(`jobs/create`, data)
}

/**
 * 资金分类列表接口
 * @param data
 * @return {*}
 */
export function billCateApi(data) {
  return request.get(`bill_cate`, data)
}
/**
 * 付款账目分类回显列表接口
 * @return {*}
 * @param id
 */
export function getbillCate(id) {
  return request.get(`client/contracts/bill_cate/${id}`)
}
/**
 * 付款账目分类编辑列表接口
 * @param id
 * @param data
 * @return {*}
 */
export function billFinanceApi(id, data) {
  return request.put(`client/bill/finance/${id}`, data)
}
/**
 * 付款账目分类删除列表接口
 * @return {*}
 * @param id
 */
export function billDelFinanceApi(id) {
  return request.delete(`client/bill/finance/${id}`)
}

/**
 * 资金记账批量导入
 * @param data
 * @return {*}
 */
export function billImportApi(data) {
  return request.post(`bill/import`, data)
}

/**
 * 获取资金分类表单创建接口
 * @return {*}
 */
export function billCateCreateApi(pid) {
  return request.get(`bill_cate/create?pid=${pid}`)
}
/**
 * 财务管理-账目设置-支付方式-创建分类
 * @return {*}
 */
export function enterprisePayTypeCreateApi() {
  return request.get(`pay_type/create`)
}

/**
 * 财务管理-账目设置-支付方式-列表
 * @return {*}
 */
export function enterprisePayTypeApi(data) {
  return request.get(`pay_type`, data)
}
/**
 * 人事管理---列表
 * @return {*}
 */
export function getInterview() {
  return request.get(`company/card/interview`)
}

/**
 * 合同管理---累计金额
 * @return {*}
 */
export function getContractStatisticsApi(cid) {
  return request.get(`client/bill/contract_statistics/${cid}`)
}

/**
 * 客户管理---付款金额统计
 * @return {*}
 */
export function getCustomerStatisticsApi(eid) {
  return request.get(`client/bill/customer_statistics/${eid}`)
}

/**
 * 财务管理-账目设置-支付方式-编辑分类
 * @return {*}
 */
export function enterprisePayTypeDeleteApi(id) {
  return request.delete(`pay_type/${id}`)
}

/**
 * 财务管理-账目设置-支付方式-编辑分类
 * @return {*}
 */
export function enterprisePayTypeEditApi(id) {
  return request.get(`pay_type/${id}/edit`)
}

/**
 * 财务管理-账目设置-支付方式-状态设置
 * @return {*}
 */
export function enterprisePayTypeStatusApi(data) {
  return request.put(`enterprise/payType`, data)
}

/**
 * 获取资金分类表单编辑接口
 * @return {*}
 */
export function billCateEditApi(id) {
  return request.get(`bill_cate/${id}/edit`)
}

/**
 * 删除资金分类接口
 * @return {*}
 */
export function billCateDeleteApi(id) {
  return request.delete(`bill_cate/${id}`)
}

/**
 * 收支统计列表详情
 * @return {*}
 */
export function billListApi(data) {
  return request.post(`bill/list`, data)
}

/**
 * 收支统计创建表单详情
 * @return {*}
 */
export function billListCreateApi() {
  return request.get(`bill/create`)
}

/**
 * 获取收支统计修改表单详情
 * @return {*}
 */
export function billListEditApi(id) {
  return request.get(`bill/${id}/edit`)
}

/**
 * 删除收支统计
 * @return {*}
 */
export function billListDeleteApi(id) {
  return request.delete(`bill/${id}`)
}

/**
 * 收支统计图标
 * @return {*}
 */
export function billChartApi(data) {
  return request.post(`bill/chart`, data)
}

/**
 * 绩效考核-考核设置-考核评分列表获取
 * @return {*}
 */
export function assessScoreApi() {
  return request.get(`assess/score`)
}

/**
 * 绩效考核-考核设置-考核评分修改获取
 * @return {*}
 */
export function assessScoreUpdateApi(data) {
  return request.post(`assess/score`, data)
}

/**
 * 绩效考核-考核流程-审核人员配置ß
 * @return {*}
 */
export function assessVerifyApi() {
  return request.get(`assess/verify`)
}
/**
 * 绩效模板-模板分类-分类创建表单
 * @return {*}
 * types 分类类型：0、指标；1、考核；
 */
export function assessTargetCateApi(types) {
  return request.get(`assess/target_cate/create/${types}`)
}

/**
 * 绩效模板-模板分类-分类列表获取
 * @return {*}
 * types 分类类型：0、指标；1、考核；
 */
export function assessTargetCateListApi(data) {
  return request.get(`assess/target_cate`, data)
}

/**
 * 绩效模板-模板分类-分类编辑
 * @return {*} assess/target_cate/{targetCate}
 */
export function assessTargetCateEditApi(id) {
  return request.get(`assess/target_cate/${id}/edit`)
}

/**
 * 绩效模板-模板分类-分类删除
 * @return {*}
 */
export function targetCateDeleteApi(targetCate) {
  return request.delete(`assess/target_cate/${targetCate}`)
}

/**
 * 绩效模板-模板分类-指标模板列表获取
 * @return {*}
 */
export function assessTargetListApi(data) {
  return request.get(`assess/target`, data)
}

/**
 * 绩效模板-模板分类-指标模板-修改
 * @return {*}
 */
export function assessTargetEditApi(target) {
  return request.get(`assess/target/${target}/edit`)
}

/**
 * 绩效模板-模板分类-指标模板创建表单
 * @return {*} assess/target/{target}
 */
export function assessTargetCreateApi() {
  return request.get(`assess/target/create`)
}

/**
 * 绩效模板-模板分类-指标模板-删除
 * @return {*}
 */
export function assessTargetDeleteApi(target) {
  return request.delete(`assess/target/${target}`)
}

/**
 * 绩效模板-模板分类-指标模板-修改状态
 * @return {*}
 */
export function assessTargetStatusApi(target, data) {
  return request.get(`assess/target/${target}`, data)
}

/**
 * 绩效模板-计划考核-计划考核获取
 * @return {*}
 */
export function assessPlanGetApi(id) {
  return request.get(`assess/plan/${id}/edit`)
}

/**
 * 绩效模板-计划考核-计划考核修改
 * @return {*}
 */
export function assessPlanPutApi(id, data) {
  return request.put(`assess/plan/${id}`, data)
}

/**
 * 绩效模板-模板库-考核模板-列表
 * @return {*}
 */
export function assessTemplateListApi(data) {
  return request.get(`assess/template`, data)
}

/**
 * 绩效模板-模板库-考核模板-收藏与取消
 * @return {*}
 */
export function assessTemplateCollectApi(id) {
  return request.get(`assess/template/collect/${id}`)
}

/**
 * 绩效模板-模板库-考核模板-编辑获取
 * @return {*}
 */
export function assessTemplateEditApi(id, data) {
  return request.get(`assess/template/${id}/edit`, data)
}

/**
 * 绩效模板-模板库-考核模板-编辑保存
 * @return {*}
 */
export function assessTemplatePutApi(id, data) {
  return request.put(`assess/template/${id}`, data)
}

/**
 * 绩效模板-模板库-考核模板-删除
 * @return {*}
 */
export function templateDeleteApi(id) {
  return request.delete(`assess/template/${id}`)
}

/**
 * 绩效模板-模板库-考核模板-设置封面图-列表
 * @return {*}
 */
export function attachCoverListApi(data) {
  return request.get(`system/attach/cover`, data)
}

/**
 * 绩效模板-模板库-考核模板-删除封面图
 * @return {*}
 */
export function attachCoverDeleteApi(data) {
  return request.delete(`system/attach/cover`, data)
}

/**
 * 绩效模板-模板库-考核模板-设置封面图
 * @return {*}
 */
export function attachCoverSetApi(id, data) {
  return request.post(`assess/template/cover/${id}`, data)
}

/**
 * 绩效模板-考核档案-员工绩效-考核统计
 * @return {*}
 */
export function assessCensusApi(data) {
  return request.post(`assess/census_bar`, data)
}

// /**
//  * 绩效模板-考核档案-员工绩效-考核统计
//  * @return {*}
//  */
// export function assessCensusLineApi(data) {
//   return request.post(`user/assess/census_line`, data);
// }
/**
 * v1.4效模板-考核档案-员工绩效-考核统计
 * @return {*}
 */
export function assessCensusLineApi(data) {
  return request.post(`assess/census`, data)
}

/**
 * 绩效模板-考核档案-考核记录-列表
 * @return {*}
 */
export function assessGroupApi(data) {
  return request.get(`user/assess/group`, data)
}

/**
 * 绩效模板-考核设置-获取已启用的考核周期
 * @return {*}
 */
export function assessPlanPeriodApi() {
  return request.get(`assess/plan/period`)
}

/**
 * 客户管理--客户管理--企业设置--保存
 * @return {*}
 */
export function clientConfigSaveApi(data) {
  return request.post(`client/config/save`, data)
}

/**
 * 客户管理--企业设置--列表
 * @return {*}
 */
export function clientConfigListApi(data) {
  return request.get(`client/config/list`, data)
}

/**
 * 客户管理--企业设置--客户标签--列表
 * @return {*}
 */
export function clientConfigLabelApi(data) {
  return request.get(`client/labels`, data)
}

/**
 * 客户管理--V1.4新增客户表单
 * @return {*}
 */
export function chargeCreateApi() {
  return request.get(`client/customer/create`)
}

/**
 * 客户管理--V1.4新增联系人表单
 * @return {*}
 */
export function liaisonCreateApi() {
  return request.get(`client/liaisons/create`)
}

/**
 * 客户管理--V1.4编辑联系人表单
 * @return {*}
 */
export function liaisonEditCreateApi(id) {
  return request.get(`client/liaisons/${id}/edit`)
}

/**
 * 客户管理--V1.4保存联系人表单
 * @return {*}
 */
export function liaisonSaveApi(data) {
  return request.post(`client/liaisons`, data)
}
/**
 * 客户管理--V1.4修改联系人表单
 * @return {*}
 */
export function liaisonEditSaveApi(id, data) {
  return request.put(`client/liaisons/${id}`, data)
}

/**
 * 客户管理--V1.4编辑回显客户表单
 * @return {*}
 */
export function chargeEditApi(id) {
  return request.get(`client/customer/${id}/edit`)
}

/**
 * 客户管理--V1.4编辑提交客户表单
 * @return {*}
 */
export function chargeEditSubmitApi(id, data) {
  return request.put(`client/customer/${id}`, data)
}

/**
 * 客户管理--V1.4查看客户基本信息详情
 * @return {*}
 */
export function chargeDetailsApi(id) {
  return request.get(`client/customer/info/${id}`)
}
/**
 * 客户管理--企业设置--客户标签--保存
 * @return {*}
 */
export function clientConfigLabelSaveApi(data) {
  return request.post(`client/labels`, data)
}

/**
 * 客户管理--企业设置--客户标签--修改
 * @return {*}
 */
export function clientConfigLabelEditApi(id, data) {
  return request.put(`client/labels/${id}`, data)
}

/**
 * 客户管理--企业设置--客户标签--删除
 * @return {*}
 */
export function clientConfigLabelDeleteApi(id) {
  return request.delete(`client/labels/${id}`)
}

/**
 * 客户管理--V1.4保存客户表单
 * @return {*}
 */
export function clientCustomerSaveApi(data) {
  return request.post(`client/customer`, data)
}
/**
 * 合同管理--V1.4添加合同表单
 * @return {*}
 */
export function contractCreateApi() {
  return request.get(`client/contracts/create`)
}
/**
 * 合同管理--V1.4保存合同表单
 * @return {*}
 */
export function contractAddApi(data) {
  return request.post(`client/contracts`, data)
}

/**
 * 合同管理--V1.4获取编辑合同表单
 * @return {*}
 */
export function contractEditCreateApi(id) {
  return request.get(`client/contracts/${id}/edit`)
}

/**
 * 合同管理--V1.4编辑合同提交
 * @return {*}
 */
export function contractEditApi(id, data) {
  return request.put(`client/contracts/${id}`, data)
}

/**
 * 合同管理--V1.4查看合同详情
 * @return {*}
 */
export function contractDetailApi(id) {
  return request.get(`client/contracts/info/${id}`)
}

/**
 * 合同管理--V1.4获取下拉选择客户数据
 * @return {*}
 */
export function customerSelectApi() {
  return request.get(`client/customer/select`)
}

/**
 * 合同管理--V1.4获取合同分类数据
 * @return {*}
 */
export function contractCategorySelectApi() {
  return request.get(`client/contract_category/select`)
}

/**
 * 客户管理--客户列表--删除客户信息
 * @return {*}
 */
export function clientDataDeleteApi(id) {
  return request.delete(`client/customer/${id}`)
}

/**
 * 客户管理--客户列表--修改成交状态
 * @return {*}
 */
export function clientDataStatusApi(id, data) {
  return request.post(`client/data/status/${id}`, data)
}

/**
 * 客户管理--客户列表--批量设置标签
 * @return {*}
 */
export function clientDataLabelApi(data) {
  return request.post(`client/customer/label`, data)
}

/**
 * 客户管理--客户列表--客户详情
 * @return {*}
 */
export function clientDataInfoApi(id) {
  return request.get(`client/data/${id}`)
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
 * 客户管理--合同列表--列表
 * @return {*}
 */
export function clientContractListApi(data) {
  return request.post(`client/contracts/list`, data)
}

/**
 * 客户管理--添加付款--选择合同
 * @return {*}
 */
export function selectContractListApi(data) {
  return request.get(`client/contracts/select`, data)
}
/**
 * 客户管理--合同列表--删除合同
 * @return {*} client/bill
 */
export function clientContractDeleteApi(id) {
  return request.delete(`client/contracts/${id}`)
}

/**
 * 业务员---列表
 * @return {*}
 */
export function getSalesman() {
  return request.get(`client/customer/salesman`)
}

/**
 * 发票累计金额---列表
 * @return {*}
 */
export function accumulatedAmountApi(data) {
  return request.get(`client/invoice/price_statistics`, data)
}
/**
 * 发票操作记录---列表
 * @return {*}
 */
export function operationRecordApi(id) {
  return request.get(`client/invoice/record/${id}`)
}
/**
 * 收支记账操作记录---列表
 * @return {*}
 */
export function billRecordApi(id) {
  return request.get(`bill/record/${id}`)
}

/**
 * 未开票---列表
 * @return {*}
 */
export function uninvoicedListApi(data) {
  return request.get(`client/bill/un_invoiced_list`, data)
}
/**
 * 已付款订单---列表
 * @return {*}
 */
export function paymentRecordApi(id) {
  return request.get(`client/invoice/bill/${id}`)
}

/**
 * 客户管理--合同列表--保存付款与续费记录
 * @return {*}
 */
export function clientBillSaveApi(data) {
  return request.post(`client/bill`, data)
}

/**
 * 客户管理--合同列表--付款与续费记录列表
 * @return {*}
 */
export function clientBillListApi(data) {
  return request.get(`client/bill`, data)
}

/**
 * 客户管理--合同列表--付款与续费记录列表全部记录
 * @return {*}
 */
export function clientBillAllListApi(data) {
  return request.get(`client/bill/list`, data)
}

/**
 * 客户管理--合同列表--保存付款与续费记录
 * @return {*}
 */
export function clientBillEditApi(id, data) {
  return request.put(`client/bill/${id}`, data)
}

/**
 * 客户管理--合同列表--删除付款与续费记录V1.8
 * @return {*}
 */
export function clientBillDeleteApi(id, data) {
  return request.delete(`client/bill/${id}`, data)
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
 * v1.4发票管理--发票申请列表
 * @return {*}
 */
export function financeInvoiceListApi(data) {
  return request.get(`client/invoice/list`, data)
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
 * 人事管理--新添加档案保存
 * @return {*}
 */
export function saveCard(type, data) {
  return request.post(`company/card/save/${type}`, data)
}
/**
 * 人事管理--邀请完善档案
 * @return {*}
 */
export function perfectCard(id) {
  return request.get(`company/card/perfect/${id}`)
}
/**
 * 人事管理--人事异动列表
 * @return {*}
 */
export function changeCard(id) {
  return request.get(`company/card/change`, id)
}

/**
 * 人事管理--转正
 * @return {*}
 */
export function formalCard(id) {
  return request.get(`company/card/formal/${id}`)
}
/**
 * 人事管理--离职
 * @return {*}
 */
export function quitCard(id, data) {
  return request.post(`company/card/quit/${id}`, data)
}
/**
 * 人事管理--保存调薪记录
 * @return {*}
 */
export function getSalary(data) {
  return request.post(`company/salary`, data)
}
/**
 * 人事管理--获取调薪记录
 * @return {*}
 */
export function getSalaryList(data) {
  return request.get(`company/salary`, data)
}
/**
 * 人事管理--删除调薪记录
 * @return {*}
 */
export function deleteSalaryList(salary) {
  return request.delete(`company/salary/${salary}`)
}

/**
 * 人事管理--获取调薪详情
 * @return {*}
 */
export function getSalaryContent(salary) {
  return request.get(`company/salary/${salary}/edit`)
}
/**
 * 人事管理--单独修改调薪详情
 * @return {*}
 */
export function putSalaryContent(salary, data) {
  return request.put(`company/salary/${salary}`, data)
}
/**
 * 人事管理--最近调薪记录
 * @return {*}
 */
export function latelySalaryContent(card_id) {
  return request.get(`company/salary/last/${card_id}`)
}
/**
 * 合同管理--批量设置转移
 * @return {*}
 */
export function clientContractShiftApi(data) {
  return request.post(`client/contracts/shift`, data)
}

/**
 * 发票管理--批量设置转移
 * @return {*}
 */
export function clientInvoiceShiftApi(data) {
  return request.post(`client/invoice/shift`, data)
}
/**
 * 添加类目分类
 * @return {*}
 */
export function invoiceCategory(data) {
  return request.post(`client/invoice_category`, data)
}

/**
 * 类目分类列表
 * @return {*}
 */
export function invoiceCategoryList(data) {
  return request.get(`client/invoice_category`, data)
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
 * 编辑类目分类
 * @return {*}
 */
export function putInvoiceCategory(id, data) {
  return request.put(`client/invoice_category/${id}`, data)
}

/**
 * 删除类目分类
 * @return {*}
 */
export function deleteInvoiceCategory(id, data) {
  return request.delete(`client/invoice_category/${id}`, data)
}

/**
 * 文件重命名
 * @return {*}
 */
export function putRealName(id, data) {
  return request.put(`client/file/real_name/${id}`, data)
}

/**
 * 获取当前考核周期选中人员
 * @param data {Object}
 * @return {*}
 */
export function assessPlanUserListApi(data) {
  return request.get(`assess/plan/user_list`, data)
}

/**
 * 客户管理--业绩统计--列表
 * @return {*}
 */
export function salesmanDataListApi(data) {
  return request.post(`client/customer/ranking`, data)
}

/**
 * 获取业绩记录统计
 * @param {Object} data
 * @returns {*}
 */
export function performanceStatisticsApi(data) {
  return request.post(`client/customer/statistics`, data)
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
 获取付款提醒详情接口
 * @param {Number} id
 * @returns {*}
 */
export function clientRemindDetailApi(id) {
  return request.get(`client/remind/info/${id}`)
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
  return request.get(`client/contracts/info/${id}`)
}

/**
 * 获取客户名称接口
 * @returns {*}
 */
export function clientNameApi() {
  return request.get(`client/data/select`)
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

/**
 * 客户列表接口
 * @returns {*}
 * @param data
 */
export function customerViewApi(data) {
  return request.post(`client/customer/list`, data)
}
/**
 * 合同列表接口
 * @returns {*}
 * @param data
 */
export function contractViewApi(data) {
  return request.post(`client/contracts/list`, data)
}
/**
 * 合同列表接口
 * @returns {*}
 * @param data
 */
export function clientLiaisonApi(data) {
  return request.get(`client/liaisons`, data)
}

/**
 * 联系人列表接口
 * @returns {*}
 * @param data
 */
export function liaisonViewApi(data) {
  return request.get(`client/liaisons`, data)
}

/**
 * 客户关注接口
 * @return {*}
 */
export function customerSubscribeApi(id, status, data) {
  return request.post(`client/customer/subscribe/${id}/${status}`, data)
}

/**
 * 退回公海接口
 * @return {*}
 */
export function customerReturnApi(data) {
  return request.post(`client/customer/return`, data)
}

/**
 * 客户流失接口
 * @return {*}
 */
export function customerLostApi(data) {
  return request.post(`client/customer/lost`, data)
}

/**
 * 客户取消流失接口
 * @return {*}
 */
export function customerCancelLostApi(id) {
  return request.post(`client/customer/cancel_lost/${id}`)
}

/**
 * 合同关注接口
 * @return {*}
 */
export function contractSubscribeApi(id, status, data) {
  return request.post(`client/contracts/subscribe/${id}/${status}`, data)
}

/**
 * 客户领取接口
 * @return {*}
 */
export function customerClaimApi(data) {
  return request.post(`client/customer/claim`, data)
}

/**
 * 动态记录接口
 * @returns {*}
 * @param data
 */
export function clientRecordApi(data) {
  return request.get(`client/record`, data)
}

/**
 * 客户管理--批量设置转移
 * @return {*} client/contracts/shift
 */
export function customerShiftApi(data) {
  return request.post(`client/customer/shift`, data)
}

/**
 * 合同异常接口
 * @return {*}
 */
export function contractAbnormalApi(id, status) {
  return request.put(`client/contracts/abnormal/${id}/${status}`)
}

/**
 * 云盘模板下载接口
 * @returns {*}
 */
export function tempDownloadApi(fid, data) {
  return request.post(`cloud/file/${fid}/temp_download`, data)
}

/**
 * 客户导入
 * @param {*} types
 * @param {*} data
 * @returns
 */
export function customerImport(types, data) {
  return request.post(`client/customer/import`, { types, data })
}

/**
 * 合同导入
 * @param {*} types
 * @param {*} data
 * @returns
 */
export function contractImport(types, data) {
  return request.post(`client/contracts/import`, { types, data })
}

/**
 * 考勤导入
 * @param {*} data
 * @returns
 */
export function attendanceImport(data) {
  return request.post(`attendance/clock/import_record`, { data })
}
/**
 * 考勤导入
 * @param {*} data
 * @returns
 */
export function attendanceImportFile(data) {
  return request.post(`attendance/clock/import_third`, data)
}
