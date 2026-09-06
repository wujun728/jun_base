import http from "@/api";

// 查询字典数据列表
export function listData(params) {
  return http.get("/system/dict/data/list", params);
}

// 查询字典数据详细
export function getData(dictCode) {
  return http.get("/system/dict/data/" + dictCode);
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return http.get("/system/dict/data/type/" + dictType);
}

// 新增字典数据
export function addData(data) {
  return http.post("/system/dict/data", data);
}

// 修改字典数据
export function updateData(data) {
  return http.put("/system/dict/data", data);
}

// 删除字典数据
export function delData(dictCode) {
  return http.delete("/system/dict/data/" + dictCode);
}

// 导出数据
export const exportDictDetailInfo = params => {
  return http.download(`system/dict/data/export`, params);
};
