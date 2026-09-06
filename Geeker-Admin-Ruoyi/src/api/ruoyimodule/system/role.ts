import http from "@/api";

import { addDateRange } from "@/utils";
import { ResPage, Role, User } from "@/api/interface";

// 查询角色列表
export function listRole(params) {
  params = addDateRange(params, "createTime");
  console.log(params);

  return http.get<ResPage<Role.ResRoleInfo>>(`/system/role/list`, params);
}

// 查询角色详细
export function getRole(roleId) {
  return http.get("/system/role/" + roleId);
}

// 新增角色
export function addRole(data) {
  return http.post("/system/role", data);
}

// 修改角色
export function updateRole(data) {
  return http.put("/system/role", data);
}

// 删除角色
export function delRole(roleId) {
  return http.delete("/system/role/" + roleId);
}

// 导出用户数据
export const exportRoleInfo = (params: User.ReqUserParams) => {
  return http.download("system/role/export", params);
};

// 角色数据权限
export function dataScope(data) {
  return http.put("/system/role/dataScope", data);
}
