import http from "@/api";
import { Dept, ResPage, User } from "@/api/interface";
import { addDateRange, parseStrEmpty } from "@/utils";

// 获取用户部门列表
export const getUserDepartment = () => {
  return http.get<User.ResDepartment[]>("/system/user/deptTree");
};

// 获取用户列表
export const getUserList = (params: User.ReqUserParams) => {
  params = addDateRange(params, "createTime");
  console.log(params);

  return http.get<ResPage<User.ResUserList>>(`/system/user/list`, params);
};
export const changeUserStatus = (params: { userId: string; status: number }) => {
  return http.put(`/system/user/changeStatus`, params);
};
// 查询部门下拉树结构
export function deptTreeSelect() {
  return http.get<Array<Dept.ResDeptInfo>>(`/system/user/deptTree`);
}

export function getUser(userId) {
  return http.get("/system/user/" + parseStrEmpty(userId));
}

// 修改用户
export function updateUser(data: User.ResUserInfo) {
  return http.put(`/system/user`, data);
}
// 新增用户
export function addUser(data) {
  return http.post(`/system/user`, data);
}
// 删除用户
export function delUser(userId) {
  return http.delete("/system/user/" + userId);
}
export function resetUserPwd(data) {
  console.log(data);
  return http.put(`/system/user/resetPwd`, data);
}

// 下载模板
export const userTempDownload = () => {
  return http.download("system/user/importTemplate");
};
export const userUpload = (params: FormData) => {
  console.log(params);
  return http.post("/system/user/importData?updateSupport=" + params.get("isCover"), params);
};
// 导出用户数据
export const exportUserInfo = (params: User.ReqUserParams) => {
  return http.download(`system/user/export`, params);
};
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  };
  return http.put("/system/user/profile/updatePwd", data, {
    params: data
  });
}
