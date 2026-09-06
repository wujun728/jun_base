import response from '@/utils/http'

export function getCache() {
  return response<any>({
    url: '/monitor/cache',
    method: 'get'
  })
}

export function listCacheName() {
  return response<any>({
    url: '/monitor/cache/getNames',
    method: 'get'
  })
}

export function listCacheKey(cacheName: string) {
  return response<any>({
    url: `/monitor/cache/getKeys/${cacheName}`,
    method: 'get'
  })
}

export function getCacheValue(cacheName: string, cacheKey: string) {
  return response<any>({
    url: `/monitor/cache/getValue/${cacheName}/${cacheKey}`,
    method: 'get'
  })
}

export function clearCacheName(cacheName: string) {
  return response<any>({
    url: `/monitor/cache/clearCacheName/${cacheName}`,
    method: 'delete'
  })
}

export function clearCacheKey(cacheKey: string) {
  return response<any>({
    url: `/monitor/cache/clearCacheKey/${cacheKey}`,
    method: 'delete'
  })
}

export function clearCacheAll() {
  return response<any>({
    url: '/monitor/cache/clearCacheAll',
    method: 'delete'
  })
}
