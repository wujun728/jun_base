import { defHttp } from '@/utils/http/axios';
import { ID, IDS, PageQuery } from '@/api/base';
import { Notice } from './model';

enum Api {
  root = '/system/notice',
  noticeList = '/system/notice/list',
}

export function noticeList(params?: PageQuery) {
  return defHttp.get<Notice[]>({ url: Api.noticeList, params });
}

export function noticeInfo(noticeId: ID) {
  return defHttp.get<Notice>({ url: Api.root + '/' + noticeId });
}

export function noticeAdd(data: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, data });
}

export function noticeUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

export function noticeRemove(noticeIds: IDS) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + noticeIds });
}
