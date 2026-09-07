import { defHttp } from '@/utils/http/axios';
import { ID, IDS, PageQuery, commonExport } from '@/api/base';
import { DictType } from './dictType.model';

enum Api {
  root = '/system/dict/type',
  dictTypeList = '/system/dict/type/list',
  dictTypeExport = '/system/dict/type/export',
  dictTypeRefreshCache = '/system/dict/type/refreshCache',
  dictOptionSelectList = '/system/dict/type/optionselect',
}

export function dictList(params?: PageQuery) {
  return defHttp.get<DictType[]>({ url: Api.dictTypeList, params });
}

export function dictExport(data: any) {
  return commonExport(Api.dictTypeExport, data);
}

export function dictTypeRemove(dictIds: IDS) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + dictIds });
}

/**
 * 刷新缓存
 * @returns
 */
export function refreshDictTypeCache() {
  return defHttp.deleteWithMsg<void>({ url: Api.dictTypeRefreshCache });
}

export function dictTypeAdd(data: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, data });
}

export function dictTypeUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

export function dictTypeInfo(dictId: ID) {
  return defHttp.get<DictType>({ url: Api.root + '/' + dictId });
}

/**
 * 下拉框  返回值和list一样
 * @returns
 */
export function dictOptionSelectList() {
  return defHttp.get<DictType[]>({ url: Api.dictOptionSelectList });
}
