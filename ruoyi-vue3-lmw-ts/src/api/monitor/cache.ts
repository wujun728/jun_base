import { request } from '@/utils/hsj/service/index'

// 查询缓存详细
export function getCache() {
  return request<GetCacheApi>({
    url: '/monitor/cache',
    method: 'get',
  })
}

// 查询缓存名称列表
export function listCacheName() {
  return request<CacheNameApi>({
    url: '/monitor/cache/getNames',
    method: 'get',
  })
}

// 查询缓存键名列表
export function listCacheKey(cacheName: string) {
  return request({
    url: '/monitor/cache/getKeys/' + cacheName,
    method: 'get',
  })
}

// 查询缓存内容
export function getCacheValue(cacheName: string, cacheKey: string) {
  return request({
    url: '/monitor/cache/getValue/' + cacheName + '/' + cacheKey,
    method: 'get',
  })
}

// 清理指定名称缓存
export function clearCacheName(cacheName: string) {
  return request({
    url: '/monitor/cache/clearCacheName/' + cacheName,
    method: 'delete',
  })
}

// 清理指定键名缓存
export function clearCacheKey(cacheKey: string) {
  return request({
    url: '/monitor/cache/clearCacheKey/' + cacheKey,
    method: 'delete',
  })
}

// 清理全部缓存
export function clearCacheAll() {
  return request({
    url: '/monitor/cache/clearCacheAll',
    method: 'delete',
  })
}
