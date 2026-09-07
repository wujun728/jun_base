import { defHttp } from '@/utils/http/axios';
import { ID, IDS, PageQuery, commonExport } from '@/api/base';
import { DictData } from './dictData.model';

enum Api {
  root = '/system/dict/data',
  dictDataList = '/system/dict/data/list',
  dictDataExport = '/system/dict/data/export',
}

/**
 * 主要是DictTag组件使用
 * ignoreCancelToken 不忽略重复请求  页面肯可能加载多次字典  忽略请求的话会没有数据
 * @param dictType 字典类型
 * @returns
 */
export function dictDataInfo(dictType: string) {
  return defHttp.get<DictData[]>(
    { url: Api.root + '/type/' + dictType },
    { ignoreCancelToken: true },
  );
}

export function dictDataList(params?: PageQuery) {
  return defHttp.get<DictData[]>({ url: Api.dictDataList, params });
}

export function dictDataExport(data: any) {
  return commonExport(Api.dictDataExport, data);
}

export function dictDataRemove(dictIds: IDS) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + dictIds });
}

export function dictDataAdd(data: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, data });
}

export function dictDataUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

// 查询字典数据详细
export function dictDetailInfo(dictCode: ID) {
  return defHttp.get<DictData>({ url: Api.root + '/' + dictCode });
}
