import { defHttp } from '@/utils/http/axios';
import { IDS, PageQuery, commonExport } from '@/api/base';
import { OperateLog } from './model';

enum Api {
  root = '/monitor/operlog',
  operLogList = '/monitor/operlog/list',
  operLogExport = '/monitor/operlog/export',
  operLogClean = '/monitor/operlog/clean',
}

export function operLogList(params?: PageQuery) {
  return defHttp.get<OperateLog>({ url: Api.operLogList, params });
}

export function operLogDelete(operIds: IDS) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + operIds });
}

export function operLogClean() {
  return defHttp.deleteWithMsg<void>({ url: Api.operLogClean });
}

export function operLogExport(data: any) {
  return commonExport(Api.operLogExport, data);
}
