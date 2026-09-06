import http from "@/api";
export function getServer() {
  return http.get("/monitor/server");
}
