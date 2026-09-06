import http from "@/api";
//import { Notice } from "@/api/interface";

// 查询公告列表

export function listNotice(params) {
  return http.get("/system/notice/list", params);
}
// 查询公告详细
export function getNotice(noticeId) {
  return http.get("/system/notice/" + noticeId);
}

// 新增公告
export function addNotice(data) {
  return http.post("/system/notice", data);
}

// 修改公告
export function updateNotice(data) {
  return http.put("/system/notice", data);
}

// 删除公告
export function delNotice(noticeId) {
  return http.delete("/system/notice/" + noticeId);
}
