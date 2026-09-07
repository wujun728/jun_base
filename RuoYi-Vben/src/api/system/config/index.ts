import { defHttp } from '@/utils/http/axios';
import { ID, IDS, PageQuery, commonExport } from '@/api/base';
import { Config } from './model';
// import { Result } from '/#/axios';

enum Api {
  root = '/system/config',
  configList = '/system/config/list',
  configExport = '/system/config/export',
  configRefreshCache = '/system/config/refreshCache',
  configInfoByKey = '/system/config/configKey',
}

export function configList(params?: PageQuery) {
  return defHttp.get<Config>({ url: Api.configList, params });
}

export function configInfo(configId: ID) {
  return defHttp.get<Config>({ url: Api.root + '/' + configId });
}

export function configExport(data: any) {
  return commonExport(Api.configExport, data);
}

export function configRefreshCache() {
  return defHttp.deleteWithMsg<void>({ url: Api.configRefreshCache });
}

export function configUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

export function configAdd(data: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, data });
}

export function configRemove(configIds: IDS) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + configIds });
}

/**
 * 返回结果是在msg里???
 * @param configKey configKey
 * @returns
 */
export function configInfoByKey(configKey: string) {
  return defHttp.get<Config>({ url: Api.configInfoByKey + '/' + configKey });
}
