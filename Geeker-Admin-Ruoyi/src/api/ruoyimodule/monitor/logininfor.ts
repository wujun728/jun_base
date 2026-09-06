import http from "@/api";
import { Logininfor } from "@/api/interface";

// 查询登录日志列表
export function listLogininfor(params) {
  return http.get("/monitor/logininfor/list", params);
}

// 删除登录日志
export function delLogininfor(infoId) {
  return http.delete("/monitor/logininfor/" + infoId);
}

// 解锁用户登录状态
export function unlockLogininfor(userName) {
  return http.get("/monitor/logininfor/unlock/" + userName);
}

// 清空登录日志
export function cleanLogininfor() {
  return http.delete("/monitor/logininfor/clean");
}

export const exportLogininforInfo = (params: Logininfor.ResLogininforInfo) => {
  return http.download("monitor/logininfor/export", params);
};
