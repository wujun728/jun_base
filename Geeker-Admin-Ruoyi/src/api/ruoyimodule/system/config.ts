import http from "@/api";
//import { Config } from "@/api/interface";

// 查询岗位列表
export function listConfig(params) {
  return http.get("/system/config/list", params);
}

// 导出参数数据
export const exportConfigInfo = params => {
  return http.download(`system/config/export`, params);
};

// 新增字典类型
export function addConfig(data) {
  return http.post("/system/config", data);
}

// 修改字典类型
export function updateConfig(data) {
  return http.put("/system/config", data);
}

// 刷新参数缓存
export function refreshCache() {
  return http.delete("/system/config/refreshCache");
}
// 获取
export function getConfig(configId) {
  return http.get("/system/config/" + configId);
}
// 删除字典类型
export function delConfig(configId) {
  return http.delete("/system/config/" + configId);
}
