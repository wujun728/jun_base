import http from "@/api";
import { addDateRange } from "@/utils";
import { Operlog } from "@/api/interface";

// 查询操作日志列表
export function listOperlog(params) {
  params = addDateRange(params, "operTime");
  return http.get("/monitor/operlog/list", params);
}

// 删除操作日志
export function delOperlog(operId) {
  return http.delete("/monitor/operlog/" + operId);
}

// 清空操作日志
export function cleanOperlog() {
  return http.delete("/monitor/operlog/clean");
}

export const exportOperlogInfo = (params: Operlog.ResOperlogInfo) => {
  return http.download("monitor/operlog/export", params);
};
