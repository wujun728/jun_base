import request from './request'

/**
 * @description 短信验证码Key
 */
export function getCmsKeyApi() {
  return request.get(`common/verify/key`)
}

/**
 * @description 短信验证码
 */
export function getCmsApi(data) {
  return request.post(`common/verify`, data)
}

/**
 * @description 验证码（若依 /captchaImage）
 */
export function captchaApi() {
  return request.get(`/captchaImage`)
}

/**
 * @description 账号密码登录（若依 /login）
 */
export function login(data) {
  return request.post(`/login`, {
    username: data.account,
    password: data.password,
    code: data.captcha,
    uuid: data.key
  })
}

/**
 * @description 获取用户基本信息（若依 /getInfo）
 */
export function loginInfo(data) {
  return request.get(`/getInfo`, data)
}

/**
 * @description 退出登录（若依 /logout）
 */
export function logout() {
  return request.post(`/logout`)
}

/**
 * @description 账号密码登录
 */
export function phoneLogin(data) {
  return request.post(`/user/phone_login`, data)
}

/**
 * @description 用户注册
 */
export function registerUserApi(data) {
  return request.post(`/registerUser`, data)
}

/**
 * @description 重置密码
 */
export function savePasswordApi(data) {
  return request.put(`/user/savePassword`, data)
}
/**
 * @description 获取个人简历信息
 */
export function getResume(data) {
  return request.get(`/user/resume`, data)
}
/**
 * @description 保存个人简历信息
 */
export function saveResume(data) {
  return request.put(`user/resume_save`, data)
}
/**
 * @description 验证密码
 */
export function checkPasswordApi(data) {
  return request.post(`user/checkpwd`, data)
}
/**
 * @description 获取工作经历列表
 */
export function userWorkList(data) {
  return request.get(`user/work`, data)
}
/**
 * @description 获取教育列表
 */
export function userEducationList(data) {
  return request.get(`user/education`, data)
}
/**
 * @description 创建完善资料邀请记录
 */
export function getPerfectList(uniqued) {
  return request.get(`user/perfect/${uniqued}`)
}
/**
 * @description 完善资料邀请记录
 */
export function getPerfectIndex(data) {
  return request.get(`user/perfect/index`, data)
}
/**
 * @description 同意发送资料
 */
export function putPerfectAgree(id) {
  return request.put(`user/perfect/agree/${id}`)
}
/**
 * @description 拒绝发送资料
 */
export function putPerfectRefuse(id) {
  return request.put(`user/perfect/refuse/${id}`)
}

/**
 * @description 获取创建工作经历表单
 */
export function userCreateWork(data) {
  return request.get(`user/work/create`, data)
}
/**
 * @description 删除个人简历工作经历表单
 */
export function userDeleteWork(work) {
  return request.delete(`user/work/${work}`)
}

/**
 * @description 编辑创建个人简历工作经历表单
 */
export function userPutWork(work) {
  return request.get(`user/work/${work}/edit`)
}

/**
 * @description 工作分析列表
 */
export function jobAnalysis(data) {
  return request.get(`company/job_analysis`, data)
}

/**
 * @description 获取工作分析
 */
export function jobDetails(id) {
  return request.get(`company/job_analysis/info/${id}`)
}

/**
 * @description 修改工作分析
 */
export function putAnalysis(id, data) {
  return request.put(`company/job_analysis/${id}`, data)
}

/**
 * @description 获取我的工作分析
 */
export function mineAnalysis() {
  return request.get(`company/job_analysis/mine`)
}

/**
 * @description 获取创建教育经历表单
 */
export function userCreateEducation(data) {
  return request.get(`user/education/create`, data)
}
/**
 * @description 删除个人简历教育经历表单
 */
export function userDeleteEducation(education) {
  return request.delete(`user/education/${education}`)
}
/**
 * @description 编辑创建个人简历教育经历表单
 */
export function userPutEducation(education) {
  return request.get(`user/education/${education}/edit`)
}
/**
 * @description 当前企业邀请的用户 -- 列表
 */
export function getApplyApi(data) {
  return request.get(`user/ent/getApply`, data)
}

/**
 * @description 当前企业邀请的用户 -- 删除
 */
export function applyDeleteApi(id, data) {
  return request.delete(`user/ent/apply/${id}`, data)
}

/**
 * @description 当前企业邀请的用户 -- 同意加入企业 user/refuse/{id}
 */
export function applyAgreeApi(id) {
  return request.put(`user/agree/${id}`)
}

/**
 * @description 当前企业邀请的用户 -- 拒绝加入企业
 */
export function applyRefuseApi(id) {
  return request.put(`user/refuse/${id}`)
}

/**
 * @description 企业成员 -- 列表

 */
export function userListApi(data) {
  return request.get(`user/list`, data)
}

/**
 * @description 获取组织架构成员修改信息

 */
export function userEntCardApi(id) {
  return request.get(`system/roles/role/${id}`)
}

/**
 * @description 获取人事管理--组织架构成员修改信息

 */
export function userCardApi(id) {
  return request.get(`user/card/${id}`)
}
/**
 * @description 获取出勤统计接口

 */
export function attendanceStatistics(data) {
  return request.get(`attendance/attendance_statistics`, data)
}
/**
 * @description 获取出勤表格数据接口

 */
export function individualStatistics(data) {
  return request.get(`attendance/individual_statistics`, data)
}
/**
 * 修改用户角色
 */
export function systemUserRoleApi(data) {
  return request.post(`system/roles/user`, data)
}

/**
 * @description 修改人事管理--组织架构成员
 */
export function userHrUpdateApi(id, data) {
  return request.put(`user/card/${id}`, data)
}
/**
 * @description 办公中心-企业通讯录-组织架构
 */
export function userAddBookTree() {
  return request.get('user/add_book/tree')
}

/**
 * 办公中心-企业通讯录-列表
 * @param data
 * @return {*}
 */
export function userAddBookeList(data) {
  return request.get('user/add_book/list', data)
}
/**
 * 获取企业文档分类
 * @return {*}
 */
export function fileTemplateFolder() {
  return request.get('enterprise/template/folder')
}

/**
 * 工作台-获取企业文档列表
 * @param data
 * @return {*}
 */
export function fileList(data) {
  return request.get('enterprise/template/lst', data)
}

/**
 * 工作台-获取模板支付二维码
 * @return {*}
 * @param id
 */
export function templatePayCode(id) {
  return request.get(`enterprise/template/pay/code/${id}`)
}

/**
 * 云文件-获取模板预览地址
 * @param id
 * @return {*}
 */
export function templateViewApi(id) {
  return request.get(`enterprise/folder/template_view/${id}`)
}

/**
 * 个人中心-获取当前用户信息
 * @return {*}
 */
export function userInfo() {
  return request.get('user/userInfo')
}

/**
 * 个人中心-修改当前用户信息
 * @param data
 * @return {*}
 */
export function editUserInfo(data) {
  return request.put('user/userInfo', data)
}

/**
 * 获取用户加入的企业列表
 * @param data
 * @return {*}
 */
export function userWorkInfo(data) {
  return request.get('user/work/enterprise', data)
}

/**
 * 批量邀请用户加入企业
 * @param data
 * @return {*}
 */
export function userBatchCreate(data) {
  return request.post('user/batch/create', data)
}

/**
 * @description 站点配置（若依无此接口，保留兼容）
 */
export function site() {
  return request.get(`common/site`).catch(() => Promise.resolve({ data: {}, status: 200 }))
}

/**
 * 个人办公-我的考核-我的自评与下级考核
 * 类型:0=我的自评;1=下级评估
 * @param data
 * @returns {*} user/assess/deletes
 */
export function userAssessList(data) {
  return request.get(`assess/list`, data)
}

/**
 * 绩效考核--员工绩效--删除记录列表
 * @param data
 * @returns {*}
 */
export function userAssessDeletesList(data) {
  return request.get(`assess/del_record`, data)
}
/**
 * 绩效考核--V1.4未创建考核记录列表
 * @param data
 * @returns {*}
 */
export function userAssessAbnormalList(data) {
  return request.get(`assess/abnormal`, data)
}
/**
 * 绩效考核--V1.4未创建考核记录提示显示
 * @param data
 * @returns {*}
 */
export function userAssessAbnormal(data) {
  return request.get(`assess/is_abnormal`, data)
}

/**
 * 人事管理-员工绩效-提醒考核人
 * @param id
 * @returns {*}
 */
export function userAssessRemindApi(id) {
  return request.get(`user/assess/remind/${id}`)
}

/**
 * 个人办公-V1.4我的考核-下级考核-列表
 * @param data
 * @returns {*}
 */
export function userAssessSubord(data) {
  return request.get(`assess/index`, data)
}

/**
 * 个人办公-我的考核-下级考核-状态设置
 *
 */
export function userAssessSetShow(id, data) {
  return request.get(`assess/show/${id}`, data)
}
/**
 * 获取绩效考核详情
 * @param id
 * @returns {*}

 */
export function userAssessInfo(id) {
  return request.get(`assess/info/${id}`)
}

/**
 * 个人办公-V1.4我的考核-目标定制-修改
 * @param id
 * @param data
 * @returns {*}
 */
export function userAssessEditApi(id, data) {
  return request.post(`assess/update/${id}`, data)
}

/**
 * v1.4 创建绩效考核
 * @returns {*}
 * @param data
 */
export function userAssessCreateApi(data) {
  return request.post(`assess/create`, data)
}

/**
 * v1.4 绩效考核自评
 * @param id
 * @param data
 * @returns {*}
 */
export function userAssessSelfSaveApi(id, data) {
  return request.put(`assess/self_eval/${id}`, data)
}

/**
 * v1.4 绩效考核上级评价
 * @param id
 * @param data
 * @returns {*}
 */
export function userSuperiorEvalSaveApi(id, data) {
  return request.put(`assess/superior_eval/${id}`, data)
}

/**
 * v1.4 绩效考核上上级评价
 * @param id
 * @param data
 * @returns {*}
 */
export function userExamineEvalSaveApi(id, data) {
  return request.put(`assess/examine_eval/${id}`, data)
}
/**
 * 个人办公-企业邀请-加入企业
 * @param data
 * @returns {*}
 */
export function enterpriseUserJoinApi(data) {
  return request.put(`user/user/join`, data)
}

/**
 * 个人办公-我的考核-当前考核考核-目标定制-保存
 * @param data
 * @returns {*}
 */
export function userAssessTarget(data) {
  return request.post(`assess/target`, data)
}
/**
 * 个人办公-我的考核-考核评分记录
 * @param id
 * @param data
 * @returns {*}
 */
export function userAssessRecord(id, data) {
  return request.get(`assess/score/${id}`, data)
}

/**
 * 个人办公-我的考核-删除考核记录
 * @param id
 * @returns {*}
 */
export function userAssessDelete(id) {
  return request.get(`assess/del_form/${id}`)
}

/**
 * 个人办公-我的考核-执行期-保存上级备注
 * @param id
 * @param data
 * @returns {*}
 */
export function userAssessMark(id, data) {
  return request.put(`user/assess/mark/${id}`, data)
}

/**
 * 个人办公-我的考核-当前考核考核-考核相关-评论-标题
 * @param id
 * @returns {*}
 */
export function userAssessExplain(id) {
  return request.get(`assess/explain/${id}`)
}
/**
 * 记事本-最近使用
 * @param id
 * @returns {*}
 */
export function memorialGroupApi(data) {
  return request.get(`user/memorial/group`, data)
}

/**
 * @description 刷新个人简历工作经历表单
 */
export function getUserWorkList(data) {
  return request.get(`user/work`, data)
}

/**
 * @description 刷新个人简历工作经历表单
 */
export function getUserEducationList(data) {
  return request.get(`user/education`, data)
}

/**
 * 日程类型列表
 * @returns {*}
 */
export function scheduleTypesApi() {
  return request.get(`/schedule/types`)
}

/**
 * 日程评论
 * @returns {*}
 */
export function scheduleReplySaveApi(data) {
  return request.post(`/schedule/reply/save`, data)
}

/**
 * 日程评论列表
 * @returns {*}
 */
export function scheduleReplyListApi(data) {
  return request.get(`/schedule/replys`, data)
}
/**
 * 日程评论删除
 * @returns {*}
 */
export function scheduleReplyDelApi(id) {
  return request.delete(`/schedule/reply/del/${id}`)
}

/**
 * 业绩统计
 * @returns {*}
 */
export function workStatisticsApi(types) {
  return request.get(`user/work/statistics/${types}`)
}
/**
 * 管理业绩统计数据
 * @returns {*}
 */
export function workStatisticsApiAll() {
  return request.get(`user/work/statistics_type`)
}
/**
 * 修改业绩统计数据
 * @returns {*}
 */
export function putStatisticsApiAll(data) {
  return request.post(`user/work/statistics_type`, data)
}

/**
 * 下载模板文件
 * @returns {*}
 */
export function templateExportApi(id) {
  return request.get(`enterprise/template/export/${id}`)
}

/**
 * 日程详情
 * @returns {*}
 */
export function scheduleInfoApi(id, data) {
  return request.get(`/schedule/info/${id}`, data)
}

/**
 * 日程删除
 * @returns {*}
 */
export function scheduleDeleteApi(id, data) {
  return request.delete(`/schedule/delete/${id}`, data)
}

/**
 * 日程编辑
 * @returns {*}
 */
export function scheduleEditApi(id, data) {
  return request.put(`/schedule/update/${id}`, data)
}

/**
 * 新建日程类型
 * @returns {*}
 */
export function scheduleTypesCreateApi() {
  return request.get(`/schedule/type/create`)
}
/**
 * 编辑日程类型
 * @returns {*}
 */
export function scheduleTypesEditApi(id) {
  return request.get(`/schedule/type/edit/${id}`)
}
/**
 * 新增日程表单
 * @returns {*}
 */
export function scheduleStoreApi(data) {
  return request.post(`/schedule/store`, data)
}
/**
 * 获取日程列表
 * @returns {*}
 */
export function scheduleListApi(data) {
  return request.post(`/schedule/index`, data)
}

/**
 * 删除日程类型
 * @returns {*}
 */
export function scheduleTypesDeleteApi(id) {
  return request.delete(`/schedule/type/delete/${id}`)
}
/**
 * 修改日程状态
 * @returns {*}
 */
export function scheduleStatusApi(id, data) {
  return request.put(`/schedule/status/${id}`, data)
}

/**
 * 帮助中心结果页搜索
 * @returns {*}
 */
export function helpCenterApi(data) {
  return request.get(`helps/aggregate`, data)
}
/**
 * 备忘录--分类--列表
 * @returns {*}
 */
export function memorialCateListApi(data) {
  return request.get(`user/memorial_cate`, data)
}

/**
 * 备忘录--分类--添加分类
 * @returns {*}
 */
export function memorialCateCreateApi(pid) {
  return request.get(`user/memorial_cate/create/${pid}`)
}

/**
 * 备忘录--分类--编辑分类
 * @returns {*}
 */
export function memorialCateEditApi(id) {
  return request.get(`user/memorial_cate/${id}/edit`)
}

/**
 * 备忘录--分类--删除
 * @returns {*}user/memorial
 */
export function memorialCateDeleteApi(id) {
  return request.delete(`user/memorial_cate/${id}`)
}

/**
 * 备忘录--列表
 * @returns {*}
 */
export function memorialListApi(data) {
  return request.get(`user/memorial`, data)
}

/**
 * 备忘录--保存
 * @returns {*}
 */
export function memorialSaveApi(data) {
  return request.post(`user/memorial`, data)
}

/**
 * 备忘录--修改
 * @returns {*}
 */
export function memorialEditApi(id, data) {
  return request.put(`user/memorial/${id}`, data)
}

/**
 * 备忘录--删除
 * @returns {*}
 */
export function memorialDeleteApi(id) {
  return request.delete(`user/memorial/${id}`)
}

/**
 * 我的待办--待办添加
 * @returns {*}
 */
export function dealtScheduleAddApi(data) {
  return request.post(`user/schedule`, data)
}

/**
 * 我的待办--列表
 * @returns {*} user/schedule/count
 */
export function dealtScheduleListApi(data) {
  return request.get(`user/schedule`, data)
}

/**
 * 我的待办--日历标记提醒
 * @returns {*}
 */
export function dealtScheduleCountApi(data) {
  return request.post(`/schedule/count`, data)
}

/**
 * 我的待办--待办类型获取
 * @returns {*}
 */
export function dealtScheduleTypesApi() {
  return request.get(`user/schedule/types`)
}

/**
 * 我的待办--删除
 * @returns {*}
 */
export function dealtScheduleDeleteApi(id) {
  return request.delete(`user/schedule/${id}`)
}

/**
 * 我的待办--待办编辑
 * @returns {*}
 */
export function dealtScheduleEditApi(id, data) {
  return request.put(`user/schedule/${id}`, data)
}

/**
 * 我的待办--提醒状态确认
 * @returns {*}
 */
export function dealtScheduleRecordApi(id, data) {
  return request.post(`user/schedule/record/${id}`, data)
}

/**
 * 初始值密码修改
 * @param data {Object}
 * @returns {*}
 */
export function userEntSavePasswordApi(data) {
  return request.put(`user/savePassword`, data)
}

/**
 * 获取消息中心列表
 * @param data {Object}
 * @returns {*}
 */
export function noticeMessageListApi(data) {
  return request.get(`company/message`, data)
}

/**
 * 批量修改消息为已读
 * @param isRead {Number}
 * @param data {Object}
 * @returns {*}
 */
export function noticeMessageReadApi(isRead, data) {
  return request.put(`company/message/batch/${isRead}`, data)
}

/**
 * 批量删除消息
 * @param data {Object}
 * @returns {*}
 */
export function noticeMessageDeleteApi(data) {
  return request.delete(`company/message/batch`, data)
}
/**
 * 我的汇报 - 未提交汇报统计
 * @param data {Object}
 * @returns {*}
 */
export function submitStatisticsApi(data) {
  return request.get(`daily/submit_statistics`, data)
}

/**
 * 我的汇报 - 汇报统计
 * @param data {Object}
 * @returns {*}
 */
export function statisticsApi(data) {
  return request.get(`daily/statistics`, data)
}
/**
 * 我的汇报 - 已提交汇报的表格数据
 * @param data {Object}
 * @returns {*}
 */
export function submitListApi(data) {
  return request.get(`daily/submit_list`, data)
}

/**
 * 我的汇报 - 未提交汇报的表格数据
 * @param data {Object}
 * @returns {*}
 */
export function noSubmitListApi(data) {
  return request.get(`daily/no_submit_list`, data)
}

/**
 * 我的汇报 - 删除当天的汇报
 * @returns {*}
 * @param id
 */
export function deleteDailyApi(id) {
  return request.delete(`daily/${id}`)
}
/**
 * 获取用户主部门信息
 * @returns {*}
 */
export function userInfoApi() {
  return request.get(`user/frame/info`)
}

/**
 * 获取工作台快捷菜单
 * @returns {*}
 */
export function userWorkMenusApi() {
  return request.get(`user/work/menus`)
}

/**
 * 绩效考核指标自评
 PUT /ent/user/assess/eval
 * @returns {*}
 */
export function userAssessEvalApi(data) {
  return request.put(`assess/eval`, data)
}

/**
 * 获取工作台待办数量
 * @returns {*}
 */
export function userWorkCountApi() {
  return request.get(`user/work/count`)
}

/**
 * 获取工作台快捷菜单
 * user/work/fast_entry
 * @returns {*}
 */
export function userWorkFastEntryApi() {
  return request.get(`user/work/fast_entry`)
}

/**
 * 自定义工作台快捷菜单
 * user/work/menus
 * @returns {*}
 */
export function userWorkFastMenusApi(data) {
  return request.post(`user/work/menus`, data)
}

/**
 * 获取订阅消息列表
 * company/message/subscribe
 * @returns {*}
 */
export function userNoticeSubscribeApi(data) {
  return request.get(`company/message/subscribe`, data)
}

/**
 * 订阅/取消订阅消息
 * company/message/subscribe/{id}
 * @returns {*}
 */
export function userNoticeSubscribeShowApi(id, data) {
  return request.put(`company/message/subscribe/${id}`, data)
}

/**
 获取扫码登录参数
 * @returns {*}
 */
export function userScanKeyApi() {
  return request.get(`user/scan_key`)
}

/**
 * 获取扫码状态
 * @param data {Object}
 * @returns {*}
 */
export function userScanStatusApi(data) {
  return request.post(`user/scan_status`, data)
}
/**
 * @description 下级岗位职责列表
 */
export function subordinateApi(data) {
  return request.get(`jobs/subordinate`, data)
}

/**
 * 下级岗位职责详情
 * @returns {*}
 */
export function subordinateInfoApi(id) {
  return request.get(`jobs/subordinate/${id}`)
}

/**
 * 修改岗位职责接口
 * @returns {*}
 */
export function putSubordinateApi(id, data) {
  return request.put(`jobs/subordinate/${id}`, data)
}
