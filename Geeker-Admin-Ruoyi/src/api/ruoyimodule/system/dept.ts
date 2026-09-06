import http from "@/api";
import { addDateRange } from "@/utils";
import { ResPage, Role } from "@/api/interface";
import { handleTree } from "@/api/ruoyimodule/system/menu";

export function listDept(params?) {
  params = addDateRange(params, "createTime");
  return new Promise((resolve, reject) => {
    http
      .get<ResPage<Role.ResRoleInfo>>(`/system/dept/list`, params)
      .then(res => {
        resolve({
          rows: handleTree(res.data, "deptId")
        });
      })
      .catch(err => reject(err));
  });
}

// 新增菜单
export function addDept(data) {
  return http.post("/system/dept", data);
}

// 修改菜单
export function updateDept(data) {
  return http.put("/system/dept", data);
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(deptId) {
  return http.get("/system/dept/list/exclude/" + deptId);
}
// 查询部门详细
export function getDept(deptId) {
  return http.get("/system/dept/" + deptId);
}

// 删除部门
export function delDept(deptId) {
  return http.delete("/system/dept/" + deptId);
}
