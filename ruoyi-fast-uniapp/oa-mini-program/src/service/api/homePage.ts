import type { HomePage } from '../types/homePage';
import http from '@/utils/request';


// 首页消息提醒（温馨提示、内部通知)
export function announcement(data: HomePage) {
  return http.post<HomePage, HomePage>('/homepage/info/announcement', data);
}
// 首页消息提醒（汇报提醒）
export function reportNotice(data: HomePage) {
  return http.post<HomePage, HomePage>('/homepage/info/reportNotice', data);
}
// 首页未读数四合一
export function unreadCountFourInOne(data: any) {
  return http.get<any, any>('/applet/homepage/info/unreadCountFourInOne', data);
}
// 首页数据简报
export function dataBriefing(data: any) {
  return http.get<any, any>('/applet/homepage/info/dataBriefing', data);
}
// 首页今日日程
export function todaySchedule(data: any) {
  return http.get<any, any>('/applet/homepage/info/todaySchedule', data);
}
// 批量阅读消息提醒（温馨提示、内部通知)
export function updateAnnouncementSetReadByIdList(data: any) {
  return http.get<any, any>('/homepage/updateAnnouncementSetReadByIdList', data);
}
// 批量阅读消息提醒（汇报)
export function updateReportNoticeSetReadByIdList(data: any) {
  return http.get<any, any>('/homepage/updateReportNoticeSetReadByIdList', data);
}