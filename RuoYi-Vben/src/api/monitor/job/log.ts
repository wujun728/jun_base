import { commonExport } from '@/api/base';
import { defHttp } from '@/utils/http/axios';

export function jobLogList(params?: any) {
  return defHttp.get({ url: '/monitor/jobLog/list', params });
}

export function jobLogExport(data: any) {
  return commonExport('/monitor/jobLog/export', data);
}

export function jobLogInfo(jobLogId: string) {
  return defHttp.get({ url: `/monitor/jobLog/${jobLogId}` });
}

export function jobLogRemove(jobLogIds: string[]) {
  return defHttp.deleteWithMsg({ url: `/monitor/jobLog/${jobLogIds.join(',')}` });
}

export function jobLogClean() {
  return defHttp.deleteWithMsg({ url: '/monitor/jobLog/clean' });
}
