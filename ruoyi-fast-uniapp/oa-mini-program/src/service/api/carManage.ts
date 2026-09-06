
import http from '@/utils/request';

// 查询车辆列表
export function selectVehicleList(data: any) {
  return http.post<any, any>('/vehicle/selectVehicleList', data);
}
// 查询车辆使用记录列表
export function selectVehicleRecordList(data: any) {
  return http.post<any, any>('/vehicle/selectVehicleRecordList', data);
}
// 查询我的车辆使用记录列表
export function selectVehicleRecordListOfMine(data: any) {
  return http.post<any, any>('/vehicle/selectVehicleRecordListOfMine', data);
}
// 预约用车
export function bookVehicle(data: any) {
  return http.post<any, any>('/vehicle/bookVehicle', data);
}
// 取消用车
export function deleteVehicleRecord(data: any) {
  return http.get<any, any>('/vehicle/deleteVehicleRecord', data);
}
// 查询所有项目列表
export function selectAllProject(data: any) {
  return http.get<any, any>('/base/selectAllProject', data);
}
// 修改车辆使用记录
export function updateVehicleRecord(data: any) {
  return http.post<any, any>('/vehicle/updateVehicleRecord', data);
}
// 借出车辆
export function borrowVehicle(data: any) {
  return http.post<any, any>('/vehicle/borrowVehicle', data);
}
// 归还车辆
export function returnVehicle(data: any) {
  return http.post<any, any>('/vehicle/returnVehicle', data);
}
// 车辆管理页面，待还车带出车数量
export function vehicleStatistics(data: any) {
  return http.get<any, any>('/vehicle/info/vehicleStatistics', data);
}
// 首页车辆统计
export function vehicleBriefing(data: any) {
  return http.get<any, any>('/applet/homepage/info/vehicleBriefing', data);
}