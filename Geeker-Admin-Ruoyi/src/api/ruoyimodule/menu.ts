import http from "@/api";
import { ResultData } from "@/api/interface";

// 根据角色ID查询部门树结构
export function deptTreeSelect(roleId) {
  return http.get("/system/role/deptTree/" + roleId);
}

export function menuTreeselect() {
  return http.get<ResultData<any>>("/system/menu/treeselect");
}
// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return http.get<ResultData<any>>("/system/menu/roleMenuTreeselect/" + roleId);
}
