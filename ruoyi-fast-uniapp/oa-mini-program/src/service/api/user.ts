import type { User,Login,UserInfo,UpdateUserInfo,Password } from '../types/user';
import http from '@/utils/request';

export function getUser(data: User) {
  return http.get<User, User>('/user/list', data);
}

// 登录
export function handelLogin(data: Login) {
  return http.post<Login, Login>('/login', data);
}
// 修改密码
export function changePassword(data: Password) {
  return http.get<Password, Password>('/applet/sys/user/changePassword', data);
}
// 退出登录
export function logout(data: Login) {
  return http.post<Login, Login>('/logout', data);
}
// 获取登录人信息
export function getUserInfo(data: UserInfo) {
  return http.get<UserInfo, UserInfo>('/getUserInfo', data);
}
// 所有人员接口
export function usersList(data: any) {
  return http.get<any, any>('/base/selectUserByName', data);
}
// 编辑用户信息
export function updateUserInfo(data: UpdateUserInfo) {
  return http.post<UpdateUserInfo, UpdateUserInfo>('/sys/user/update', data);
}
// 查看我提交的汇报
export function submitReportListOfMine(data: any) {
  return http.post<any, any>('/report/submitReportListOfMine', data);
}
// 查看我提交的汇报
export function receiveReportListOfMine(data: any) {
  return http.post<any, any>('/report/receiveReportListOfMine', data);
}
// 全部模板列表
export function reportListOfAll(data: any) {
  return http.get<any, any>('/report/reportListOfAll', data);
}
// 提交的全部模板列表
export function reportListToSubmit(data: any) {
  return http.get<any, any>('/report/reportListToSubmit', data);
}
// 汇报未读转已读
export function readReport(data: any) {
  return http.get<any, any>('/report/readReport', data);
}
// 根据模板id查询对应数据
export function getTemplateInfoByTemplateId(data: any) {
  return http.get<any, any>('/report/getTemplateInfoByTemplateId', data);
}
// 获取汇报暂存信息
export function getReportDetailDraftByTemplateId(data: any) {
  return http.get<any, any>('/report/getReportDetailDraftByTemplateId', data);
}
// 暂存汇报，自动保存
export function provisionalReport(data: any) {
  return http.post<any, any>('/report/provisionalReport', data);
}
// 新增汇报
export function saveReport(data: any) {
  return http.post<any, any>('/report/saveReport', data);
}
// 部门列表带用户
export function getDeptListWithUsers(data: any) {
  return http.post<any, any>('/applet/base/getDeptListWithUsers', data);
}
// 所有人员
export function selectAllUser(data: any) {
  return http.get<any, any>('/base/selectAllUser', data);
}
// 汇报已读未读人员
export function reportStatusListByTwoIds(data: any) {
  return http.get<any, any>('/report/reportStatusListByTwoIds', data);
}
// 根据id查汇报详情
export function reportByTwoIds(data: any) {
  return http.get<any, any>('/report/reportByTwoIds', data);
}
// 查询徽章列表/单条
export function badgeListOfMine(data: any) {
  return http.get<any, any>('/applet/sys/user/badgeListOfMine', data);
}
// 佩戴徽章
export function wearBadge(data: any) {
  return http.get<any, any>('/applet/sys/user/wearBadge', data);
}