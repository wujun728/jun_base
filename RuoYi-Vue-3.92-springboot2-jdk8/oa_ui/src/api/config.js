import request from './request'
/**
 *获取新建角色数据
 */
export function systemRoleCreateApi() {
  return request.get(`system/roles/create`)
}

/**
 *获取角色列表
 */
export function systemRoleListApi() {
  return request.get(`system/roles`)
}

/**
 *获取角色详情
 */
export function systemRoleEditApi(id) {
  return request.get(`system/roles/${id}/edit`)
}

/**
 *企业新增角色
 */
export function systemRoleStoreApi(data) {
  return request.post(`system/roles`, data)
}

/**
 *企业修改角色
 */
export function systemRoleUpdateApi(id, data) {
  return request.put(`system/roles/${id}`, data)
}

/**
 *企业修改角色状态
 */
export function systemRoleStatusApi(id, data) {
  return request.get(`system/roles/${id}`, data)
}

/**
 *企业删除角色
 */
export function systemRoleDeleteApi(id) {
  return request.delete(`system/roles/${id}`)
}

/**
 *获取角色用户列表
 */
export function systemRoleUserListApi(id, data) {
  return request.get(`system/roles/user/${id}`, data)
}

/**
 *角色新增用户
 */
export function systemRoleAddUserApi(data) {
  return request.post(`system/roles/add_user`, data)
}

/**
 *角色删除用户
 */
export function systemRoleDeleteUserApi(data) {
  return request.post(`system/roles/del_user`, data)
}

/**
 *修改角色用户
 */
export function systemRoleShowUserApi(data) {
  return request.post(`system/roles/show_user`, data)
}
/**
 *修改云文件配置
 */
export function cloudFileSetupApi(id, data) {
  return request.put(`config/data/all/${id}`, data)
}
/**
 * 编辑员工培训
 */
export function employeeTrainApi(type, data) {
  return request.put(`company/train/${type}`, data)
}
/**
 * 获取员工培训
 */
export function getEmployeeTrainApi(type) {
  return request.get(`company/train/${type}`)
}
/**
 * 获取职位下拉列表接口
 */
export function jobSelectApi() {
  return request.get(`jobs/select`)
}
/**
 * 获取晋升表类型列表
 */
export function promotionListApi() {
  return request.get(`company/promotions`)
}
/**
 * 保存晋升表类型列表
 */
export function subPromotionApi(data) {
  return request.post(`company/promotions`, data)
}
/**
 * 编辑晋升表类型列表
 */
export function putPromotionApi(id, data) {
  return request.put(`company/promotions/${id}`, data)
}
/**
 * 删除晋升表类型列表
 */
export function deletePromotionApi(id) {
  return request.delete(`company/promotions/${id}`)
}

/**
 * 修改晋升表状态
 */
export function promotionShowApi(id, data) {
  return request.get(`company/promotions/${id}`, data)
}

/**
 * 获取职级下拉数据
 */
export function getrankSelectApi() {
  return request.get(`rank/select`)
}

/**
 * 获取晋升表格数据
 */
export function promotionDataApi(data) {
  return request.get(`company/promotion/data`, data)
}

/**
 * 保存晋升表格数据
 */
export function savePromotionDataApi(data) {
  return request.post(`company/promotion/data`, data)
}
/**
 * 编辑晋升表格数据
 */
export function putPromotionDataApi(id, data) {
  return request.put(`company/promotion/data/${id}`, data)
}
/**
 * 删除晋升表格数据
 */
export function deletePromotionDataApi(id) {
  return request.delete(`company/promotion/data/${id}`)
}
/**
 * 晋升表格数据排序
 */
export function sortPromotionDataApi(pid, data) {
  return request.post(`company/promotion/data/sort/${pid}`, data)
}

/**
 * 编辑晋升
 */
export function standardDataApi(id, data) {
  return request.post(`company/promotion/data/standard/${id}`, data)
}
/**
 * 保存海氏量表
 */
export function hayGroupApi(data) {
  return request.post(`company/evaluate`, data)
}
/**
 * 评估表列表接口
 */
export function getHayGroupListApi(data) {
  return request.get(`company/evaluate`, data)
}
/**
 * 删除评估表列表接口
 */
export function deleteHayGroupApi(id) {
  return request.delete(`company/evaluate/${id}`)
}
/**
 * 获取评估表详情
 */
export function getHayGroupApi(group_id) {
  return request.get(`company/evaluate/data/${group_id}`)
}
/**
 * 编辑保存评估表接口
 */
export function putHayGroupApi(id, data) {
  return request.put(`company/evaluate/${id}`, data)
}
/**
 * 历史记录评估表接口
 */
export function putHayHistoryApi(id, data) {
  return request.get(`company/evaluate/history/${id}`, data)
}
/**
 * 保存考勤班次接口
 */
export function saveAttendanceShiftApi(data) {
  return request.post(`attendance/shift`, data)
}
/**
 * 获取考勤班次列表接口
 */
export function attendanceShiftListApi(data) {
  return request.get(`attendance/shift`, data)
}
/**
 * 删除考勤班次列表接口
 */
export function deleteShiftListApi(id) {
  return request.delete(`attendance/shift/${id}`)
}
/**
 * 获取考勤班次详情
 */
export function detailShiftListApi(id) {
  return request.get(`attendance/shift/info/${id}`)
}
/**
 * 编辑考勤班次详情
 */
export function putShiftListApi(id, data) {
  return request.put(`attendance/shift/${id}`, data)
}
/**
 * 获取考勤日统计数据
 */
export function dailyStatisticsApi(data) {
  return request.get(`attendance/daily_statistics`, data)
}

/**
 * 获取考勤组下拉选择数据
 */
export function attendanceGroupSelectApi() {
  return request.get(`attendance/group/select`)
}

/**
 * 更新打卡接口
 */
export function putStatisticsApi(id, data) {
  return request.put(`attendance/statistics/${id}`, data)
}

/**
 * 获取打卡记录数据
 */
export function clockRecordApi(id, data) {
  return request.get(`attendance/statistics/${id}`, data)
}
/**
 * 获取打卡记录列表
 */
export function clockRecordList(data) {
  return request.get(`attendance/clock_record`, data)
}
/**
 * 获取打卡记录详情
 */
export function clockRecordDetails(id) {
  return request.get(`attendance/clock_record/${id}`)
}
/**
 * 获取月度统计
 */
export function monthlyStatisticsApi(data) {
  return request.get(`attendance/monthly_statistics`, data)
}

/**
 * 修改白名单人员设置
 */
export function putWhitelistApi(data) {
  return request.post(`attendance/group/white`, data)
}
/**
 * 获取白名单人员数据
 */
export function getWhitelistApi() {
  return request.get(`attendance/group/white`)
}
/**
 * 下拉选择班次数据
 */
export function attendanceShiftSelectApi(data) {
  return request.get(`attendance/shift/select`, data)
}
/**
 * 保存周期接口
 */
export function saveRosterCycleApi(data) {
  return request.post(`attendance/cycle`, data)
}
/**
 * 获取排班周期列表接口
 */
export function rosterCycleListApi(group_id) {
  return request.get(`attendance/cycle/list/${group_id}`)
}
/**
 * 修改排班周期接口
 */
export function putCycleListApi(id, data) {
  return request.put(`attendance/cycle/${id}`, data)
}
/**
 * 获取排班周期详情接口
 */
export function rosterCycleDetailApi(group_id, id) {
  return request.get(`attendance/cycle/info/${group_id}/${id}`)
}
/**
 * 获取考勤组列表接口
 */
export function attendanceGroupListApi(data) {
  return request.get(`attendance/group`, data)
}
/**
 * 获取考勤组设置详情接口
 */
export function attendanceGroupDetailsApi(id) {
  return request.get(`attendance/group/info/${id}`)
}
/**
 * 删除考勤组列表接口
 */
export function deleteGroupListApi(id) {
  return request.delete(`attendance/cycle/${id}`)
}
/**
 * 获取考勤组冲突人员接口
 */
export function repeatCheckApi(data) {
  return request.post(`attendance/group/repeat_check`, data)
}
/**
 * 获取考勤组冲突id
 */
export function repeatCheckMemberApi(data) {
  return request.get(`attendance/group/member`, data)
}
/**
 * 保存考勤组接口
 */
export function saveAttendanceGroup(data) {
  return request.post(`attendance/group`, data)
}
/**
 * 编辑考勤组接口
 */
export function putAttendanceGroup(id, data) {
  return request.put(`attendance/group/${id}`, data)
}
/**
 * 删除考勤组接口
 */
export function deleteAttendanceGroup(id) {
  return request.delete(`attendance/group/${id}`)
}
/**
 * 获取未设置考勤组成员的数据接口
 */
export function attendanceUnattendedMember(data) {
  return request.get(`attendance/group/unattended_member`, data)
}
/**
 * 获取12个月日历配置
 */
export function calendarYearApi(year) {
  return request.get(`attendance/calendar/${year}`)
}
/**
 * 修改日历配置
 */
export function putCalendarApi(date, data) {
  return request.put(`attendance/calendar/${date}`, data)
}
/**
 * 保存排班管理
 */
export function attendanceArrangeApi(data) {
  return request.post(`attendance/arrange`, data)
}
/**
 * 排班管理列表
 */
export function attendanceArrangeListApi(data) {
  return request.get(`attendance/arrange`, data)
}
/**
 * 获取排班页面数据
 */
export function getAttendanceArrangeApi(id, data) {
  return request.get(`attendance/arrange/info/${id}`, data)
}
/**
 * 保存排班页面表格
 */
export function saveAttendanceArrangeApi(group_id, data) {
  return request.put(`attendance/arrange/${group_id}`, data)
}

/**
 * 获取客户审批规则接口
 * @returns {*}
 */
export function configRuleApproveApi(isForm = 1) {
  return request.get(`config/client_rule/approve/${isForm}`)
}
/**
 * 规则设置分类列表接口
 * @returns {*}
 */
export function configRuleCatApi(data) {
  return request.get(`config/client_rule/cate`, data)
}

/**
 * 更新规则设置列表接口
 */
export function saveClientRuleApi(cate_id, data) {
  return request.put(`config/client_rule/${cate_id}`, data)
}

/**
 * 规则设置列表接口
 * @returns {*}
 */
export function clientRuleInfoApi(cate_id) {
  return request.get(`config/client_rule/${cate_id}`)
}
