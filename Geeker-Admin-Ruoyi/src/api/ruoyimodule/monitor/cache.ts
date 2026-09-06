import http from "@/api";
export function getCache() {
  return http.get("/monitor/cache");
}

// 查询缓存名称列表
export function listCacheName() {
  return http.get("/monitor/cache/getNames");
}

// 查询缓存键名列表
export function listCacheKey(cacheName) {
  return http.get("/monitor/cache/getKeys/" + cacheName);
}

// 查询缓存内容
export function getCacheValue(cacheName, cacheKey) {
  return http.get("/monitor/cache/getValue/" + cacheName + "/" + cacheKey);
}

// 清理指定名称缓存
export function clearCacheName(cacheName) {
  return http.delete("/monitor/cache/clearCacheName/" + cacheName);
}

// 清理指定键名缓存
export function clearCacheKey(cacheKey) {
  return http.delete("/monitor/cache/clearCacheKey/" + cacheKey);
}

// 清理全部缓存
export function clearCacheAll() {
  return http.delete("/monitor/cache/clearCacheAll");
}
