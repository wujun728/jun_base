import request from "../utils/request";

/**
 * 获取用户加入的企业列表
 * @return {*}
 */
export function userWorkEnterpriseApi() {
  return request.get("user/work/enterprise");
}

/**
 * 企业注册
 * @return {*}
 */
export function registerEntApi(data: object) {
  return request.post("common/registerEnt", data);
}

/**
 * 获取通讯录用户
 * @return {*}
 */
export function enterpriseUsersApi() {
  return request.get("user/tree");
}

/**
 * 企业动态分类列表
 * @param {Object} data
 * @return {*}
 */
export function noticeCategoryApi(data: object) {
  return request.get("news/cate", data);
}

/**
 * 企业动态列表
 *  @return {data}
 * @return {*}
 */
export function noticeListApi(data: object) {
  return request.get("news/list", data);
}

/**
 * 企业动态详情
 * @param {Number} id
 * @return {*}
 */
export function noticeDetailApi(id: number) {
  return request.get(`news/detail/${id}`);
}

/**
 * 获取日报列表接口
 * @param {Object} data
 * @return {*}
 */
export function enterpriseDailyApi(data: object) {
  return request.get(`daily`, data);
}

/**
 * 获取日报人员列表
 * @return {*}
 */
export function enterpriseDailyUsersApi() {
  return request.get(`daily/users`);
}

/**
 * 汇报统计接口接口
 * @return {*}
 */
export function entDailyStatisticsApi(data: object) {
  return request.get(`daily/statistics`, data);
}

/**
 * 未提交汇报人员列表接口
 * @return {*}
 */
export function entDailyNoSubmitListApi(data: object) {
  return request.get(`daily/no/submit/list`, data);
}

/**
 * 汇报提交列表接口
 * @return {*}
 */
export function entDailySubmitListApi(data: object) {
  return request.get(`daily/submit/list`, data);
}

/**
 * 获取查看日报接口
 * @param {Number} id
 * @return {*}
 */
export function enterpriseDailyEditApi(id: number) {
  return request.get(`daily/${id}/edit`);
}

/**
 * 修改日报接口
 * @param {Number} id
 * @param {Object} data
 * @return {*}
 */
export function enterpriseDailyUpdateApi(id: number, data: object) {
  return request.put(`daily/${id}`, data);
}

/**
 * 保存日报接口
 * @param {Object} data
 * @return {*}
 */
export function enterpriseDailyAddApi(data: object) {
  return request.post(`daily`, data);
}

/**
 * 删除日报回复
 * @param {Number} id
 * @param {Number} dailyId
 * @return {*}
 */
export function dailyReplyDeleteApi(id: number, dailyId: number) {
  return request.delete(`daily/reply/${id}/${dailyId}`);
}

/**
 * 日报回复
 * @param {Object} data
 * @return {*}
 */
export function dailyReplyApi(data: object) {
  return request.post(`daily/reply`, data);
}

/**
 * 我的考核列表
 * @param {Object} data
 * @return {*}
 */
export function assessMineApi(data: object) {
  return request.get(`assess/mine`, data);
}

/**
 * 查看考核详情
 * @param {Number} id
 * @return {*}
 */
export function assessInfoApi(id: number) {
  return request.get(`assess/info/${id}`);
}

/**
 * 查看考核详情
 * @param {Number} id
 * @param {Object} data
 * @return {*}
 */
export function assessUpdateApi(id: number, data: object) {
  return request.put(`assess/update/${id}`, data);
}

/**
 * 当前考核列表
 * @returns {*}
 */
export function assessNowApi() {
  return request.get(`assess/now`);
}

/**
 * 获取提醒类型
 * @returns {*}
 */
export function scheduleTypesApi() {
  return request.get(`schedule/types`);
}

/**
 * 获取月提醒数量
 *  @param {Object} data
 * @returns {*}
 */
export function scheduleCountApi(data: object) {
  return request.post(`schedule/count`, data);
}

/**
 * 获取提醒事项列表接口
 *  @param {Object} data
 * @returns {*}
 */
export function scheduleListApi(data: object) {
  return request.post(`schedule/index`, data);
}

/**
 * 获取日程详情
 * @returns {*}
 */
export function scheduleDetailApi(id: number, data: object) {
  return request.get(`schedule/info/${id}`, data);
}

/**
 * 待办保存
 * @returns {*}
 */
export function scheduleSaveApi(data: object) {
  return request.post(`schedule/store`, data);
}

/**
 * 待办修改
 * @returns {*}
 */
export function scheduleEditApi(id: number, data: object) {
  return request.put(`schedule/update/${id}`, data);
}

/**
 * 待办详情
 * @param {Number} id
 * @returns {*}
 */
export function scheduleInfoApi(id: number) {
  return request.get(`user/schedule/${id}/edit`);
}

/**
 * 待办删除
 * @returns {*}
 */
export function scheduleDeleteApi(id: number, data: object) {
  return request.delete(`schedule/delete/${id}`, data);
}

/**
 *提醒状态确认
 * @returns {*}
 */
export function scheduleRecordApi(id: number, data: object) {
  return request.put(`schedule/status/${id}`, data);
}

/**
 *组织架构tree型数据
 * @returns {*}
 */
export function frameTreeApi() {
  return request.get(`frame/tree`);
}

/**
 *获取邀请列表
 * @returns {*}
 */
export function userInvitationApi(data: object) {
  return request.get(`user/enterprise/invitation`, data);
}

/**
 *处理企业邀请加入
 * @returns {*}
 */
export function userInvitationApplyApi(id: number, data: object) {
  return request.put(`user/enterprise/invitation/${id}`, data);
}

/**
 *企业注册列表
 * @returns {*}
 */
export function enterpriseListApi(data: object) {
  return request.get(`enterprise`, data);
}

/**
 *企业注册保存
 * @returns {*}
 */
export function enterpriseSaveApi(data: object) {
  return request.post(`enterprise`, data);
}

/**
 *企业注册编辑
 * @returns {*}
 */
export function enterpriseEditApi(id: number, data: object) {
  return request.put(`enterprise/${id}`, data);
}

/**
 * 修改企业申请表单
 * @returns {*}
 */
export function enterpriseInfoApi(id: number) {
  return request.get(`enterprise/${id}/edit`);
}

/**
 * 删除企业申请表单
 * @returns {*}
 */
export function enterpriseDeleteApi(id: number) {
  return request.delete(`enterprise/${id}`);
}

/**
 * 获取个人信息
 * @returns {*}
 */
export function userUserInfoApi() {
  return request.get(`user/userInfo`);
}

/**
 * 个人信息修改
 * @returns {*}
 */
export function userUserInfoEditApi(data: object) {
  return request.put(`user/userInfo`, data);
}

/**
 * 获取用户社区文章标签
 * @returns {*}
 */
export function articleUserLabelApi() {
  return request.get(`article/user_label`);
}

/**
 * 获取文章分类
 * @returns {*}
 */
export function articleLabelApi(types: number) {
  return request.get(`article/label/${types}`);
}

/**
 * 保存用户文章标签
 * @returns {*}
 */
export function articleLabeSavelApi(data: object) {
  return request.post(`article/label`, data);
}

/**
 * 获取文章列表
 * @returns {*}
 */
export function articleListApi(data: object) {
  return request.post(`article/list`, data);
}

/**
 * 收藏/取消收藏文章
 * @returns {*}
 */
export function articleCollectApi(data: object) {
  return request.post(`article/collect`, data);
}

/**
 * 知识社区/登录
 * @returns {*}
 */
export function articleUserLoginApi(data: object) {
  return request.post(`article/user/login`, data);
}

/**
 * 点赞/取消点赞文章
 * @returns {*}
 */
export function articleSupportApi(data: object) {
  return request.post(`article/support`, data);
}

/**
 * 获取文章详情
 * @returns {*}
 */
export function articleInfoApi(id: number) {
  return request.get(`article/info/${id}`);
}

/**
 * 工作台-主页信息
 * @param data
 * @return {*}
 */
export function workIndexApi() {
  return request.get("user/work/index");
}

/**
 * 获取消息分类列表
 * @param data
 * @return {*}
 */
export function messageCateApi() {
  return request.get("message/cate");
}

/**
 * 获取消息列表
 * @param data
 * @return {*}
 */
export function messageListApi(data: object) {
  return request.get("message/list", data);
}

/**
 * 获取用户信息
 * @return {*}
 */
export function loginInfo() {
  return request.get("user/info");
}

/**
 * 获取菜单列表
 * @return {*}
 */
export function loginMenus() {
  return request.get("user/menus");
}

/**
 * 企业员工详情接口
 * @param id Number
 * @return {*}
 */
export function enterpriseUserInfoApi(id: number) {
  return request.get(`company/user/${id}`);
}

/**
 * 批量修改消息为已读
 * @return {*}
 * @param status
 * @param ids
 */
export function userMessageBatchApi(status: number, ids: object) {
  return request.put(`message/batch/${status}`, ids);
}

/**
 * 修改消息已处理状态
 * @return {*}
 * @param id
 * @param status
 */
export function userMessageHandleApi(id: number, status: number) {
  return request.put(`message/update/${id}/${status}`);
}

/**
 * 按类修改消息处理状态
 * @return {*}
 * @param id
 * @param status
 */
export function userMessageHandleCateApi(id: number, status: number) {
  return request.put(`message/handle/${id}/${status}`);
}

/**
 * 批量删除消息
 * @return {*}
 * @param data
 */
export function userMessageDeleteApi(data: object) {
  return request.delete(`message/batch/delete`, data);
}

/**
 * 获取待处理消息数量
 * @return {*}
 */
export function userMessageCountApi() {
  return request.get(`message/count`);
}

/**
 * 获取当日已完成日程
 * @return {*}
 */
export function enterpriseDailyCompletedApi(type: number) {
  return request.get(`daily/schedule/record/${type}`);
}
/**
 * V1.9日程列表数据
 * @return {*}
 */
export function getScheduleListApi(data: object) {
  return request.post(`schedule/date_lst`, data);
}

/**
 * 保存日程评价
 * @return {*}
 */
export function replySaveApi(obj: object) {
  return request.post(`schedule/reply/save`, obj);
}

/**
 * 日程评论列表
 * @return {*}
 */
export function replyListApi(obj: object) {
  return request.get(`schedule/replys`, obj);
}

/**
 *  删除日程评论
 * @return {*}
 */
export function replyDelApi(id: number) {
  return request.delete(`schedule/reply/del/${id}`);
}

/**
 * 获取用户工作台快捷菜单
 * @return {*}
 */
export function userWorkMenusApi() {
  return request.get(`user/work/menus`);
}

/**
 * 获取工作台快捷菜单
 * @return {*}
 */
export function userWorkFastEntryApi() {
  return request.get(`user/work/fast_entry`);
}

/**
 * 自定义工作台快捷菜单
 * @return {*}
 */
export function userWorkMenusSaveApi(data: object) {
  return request.put(`user/work/menus`, data);
}

/**
 * 获取备忘录最新分组列表接口
 * @return {*}
 */
export function userMemorialGroupApi(data: object) {
  return request.get(`memorials/group`, data);
}

/**
 获取文件夹下备忘录列表接口
 * @return {*}
 */
export function userMemorialListApi(data: object) {
  return request.get(`memorials`, data);
}

/**
 * 获取备忘录详情接口
 * @return {*}
 */
export function userMemorialInfoApi(id: number) {
  return request.get(`memorials/info/${id}`);
}

/**
 * 获取备忘录树状分类接口
 * @return {*}
 */
export function userMemorialCateTreeApi() {
  return request.get(`memorial/cate/tree`);
}

/**
 * 获取备忘录分类列表接口
 * @return {*}
 */
export function userMemorialCateListApi(data: object) {
  return request.get(`memorial/cate/list`, data);
}

/**
 * 删除备忘录分类接口
 * @return {*}
 */
export function userMemorialCateDeleteApi(id: number) {
  return request.delete(`memorial/cate/${id}`);
}

/**
 * 保存备忘录分类接口
 * @return {*}
 */
export function userMemorialCateSaveApi(data: object) {
  return request.post(`memorial/cate`, data);
}

/**
 * 修改备忘录分类接口
 * @return {*}
 */
export function userMemorialCateEditApi(id: number, data: object) {
  return request.put(`memorial/cate/${id}`, data);
}

/**
 * 移动备忘录分类接口
 * @return {*}
 */
export function userMemorialCateMoveApi(id: number, data: object) {
  return request.post(`memorial/cate/move/${id}`, data);
}

/**
 * 可移动备忘录分类列表接口
 * @return {*}
 */
export function userMemorialCateMovebleApi(id: number) {
  return request.get(`memorial/cate/movable/${id}`);
}

/**
 * 修改备忘录接口
 * @return {*}
 */
export function userMemorialEditApi(id: number, data: object) {
  return request.put(`memorials/${id}`, data);
}

/**
 * 删除备忘录接口
 * @return {*}
 */
export function userMemorialDeleteApi(id: number) {
  return request.delete(`memorials/${id}`);
}

/**
 * 保存备忘录接口
 * @return {*}
 */
export function userMemorialSaveApi(data: object) {
  return request.post(`memorials`, data);
}

/**
 * 扫码绑定用户
 * @return {*}
 */
export function userScanUserApi(data: object) {
  return request.post(`user/scan/user`, data);
}

/**
 * 扫码确认登录
 * @return {*}
 */
export function userScanLoginApi(data: object) {
  return request.post(`user/scan/login`, data);
}

/**
 * 汇报人
 * @return {*}
 */
export function reportMemberApi() {
  return request.get(`daily/report/member`);
}
