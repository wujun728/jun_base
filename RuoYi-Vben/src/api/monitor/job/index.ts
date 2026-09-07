import { IDS, commonExport } from '@/api/base';
import { defHttp } from '@/utils/http/axios';

export function jobList(params: any) {
  return defHttp.get({ url: '/monitor/job/list', params });
}

export function jobExport(data: any) {
  return commonExport('/monitor/job/export', data);
}

export function jobInfo(id: string) {
  return defHttp.get({ url: `/monitor/job/${id}` });
}

export function jobAdd(data: any) {
  return defHttp.postWithMsg({ url: '/monitor/job', data });
}

export function jobUpdate(data: any) {
  return defHttp.putWithMsg({ url: '/monitor/job', data });
}

export function changeStatus(data: any) {
  return defHttp.putWithMsg({ url: `/monitor/job/changeStatus`, data });
}

export function jobRunOnce(data: any) {
  return defHttp.putWithMsg({ url: `/monitor/job/run`, data });
}

export function jobDelete(ids: IDS) {
  return defHttp.deleteWithMsg({ url: `/monitor/job/${ids.join(',')}` });
}
