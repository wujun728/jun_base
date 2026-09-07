import { defHttp } from '@/utils/http/axios';

export interface CommandStats {
  name: string;
  value: string;
}

export interface Info {
  [key: string]: string;
}

export interface CacheInfo {
  dbSize: number;
  commandStats: CommandStats[];
  info: Info;
}

export function redisCacheInfo() {
  return defHttp.get<CacheInfo>({ url: '/monitor/cache' });
}

export function cacheGetNames() {
  return defHttp.get({ url: '/monitor/cache/getNames' });
}

export function cacheGetKeys(name: string) {
  return defHttp.get<string[]>({ url: `/monitor/cache/getKeys/${name}` });
}

export function cacheGetValue(name: string, key: string) {
  return defHttp.get({ url: `/monitor/cache/getValue/${name}/${key}` });
}

// clearCacheName
export function cacheClearName(name: string) {
  return defHttp.deleteWithMsg({ url: `/monitor/cache/clearCacheName/${name}` });
}

// clearCacheKey
export function cacheClearKey(key: string) {
  return defHttp.deleteWithMsg({ url: `/monitor/cache/clearKey/${key}` });
}

// clearCacheAll
export function cacheClearAll() {
  return defHttp.deleteWithMsg({ url: '/monitor/cache/clearAll' });
}
