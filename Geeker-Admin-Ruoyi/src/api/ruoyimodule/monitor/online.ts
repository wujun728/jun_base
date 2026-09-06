import http from "@/api";

// 查询在线用户列表
export function list(query) {
  return http.get("/monitor/online/list", query);
}

// 强退用户
export function forceLogout(tokenId) {
  return http.delete("/monitor/online/" + tokenId);
}
