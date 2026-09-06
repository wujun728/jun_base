import http from "@/api";
import { Post } from "@/api/interface";

// 查询岗位列表
export function listPost(params) {
  return http.get("/system/post/list", params);
}

// 查询岗位详细
export function getPost(postId) {
  return http.get("/system/post/" + postId);
}

// 新增岗位
export function addPost(data) {
  return http.post("/system/post", data);
}

// 修改岗位
export function updatePost(data) {
  return http.put("/system/post", data);
}

// 删除岗位
export function delPost(postId) {
  return http.delete("/system/post/" + postId);
}
// 导出用户数据
export const exportPostInfo = (params: Post.ResPostInfo) => {
  return http.download("system/post/export", params);
};
