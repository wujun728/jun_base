import http from "@/api";
import { addDateRange } from "@/utils";

// 查询字典类型列表
export function listType(params) {
  params = addDateRange(params, "createTime");
  return http.get("/system/dict/type/list", params);
}

// 查询字典类型详细
export function getType(dictId) {
  return http.get("/system/dict/type/" + dictId);
}

// 新增字典类型
export function addType(data) {
  return http.post("/system/dict/type", data);
}

// 修改字典类型
export function updateType(data) {
  return http.put("/system/dict/type", data);
}

// 删除字典类型
export function delType(dictId) {
  return http.delete("/system/dict/type/" + dictId);
}

// 刷新字典缓存
export function refreshCache() {
  return http.delete("/system/dict/type/refreshCache");
}

// 获取字典选择框列表
export function optionselect() {
  return http.get("/system/dict/type/optionselect");
}

// 导出数据
export const exportDictInfo = params => {
  return http.download(`system/dict/type/export`, params);
};
