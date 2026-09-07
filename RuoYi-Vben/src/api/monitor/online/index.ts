import { defHttp } from '@/utils/http/axios';
import { PageQuery } from '@/api/base';
import { OnlineUser } from './model';

enum Api {
  root = '/monitor/online',
  onlineList = '/monitor/online/list',
}

/**
 * 当前账号的在线设备
 * @returns
 */
export function onlineDeviceList() {
  return defHttp.get<OnlineUser[]>({ url: Api.root });
}

export function onlineList(params?: PageQuery) {
  return defHttp.get<OnlineUser[]>({ url: Api.onlineList, params });
}

export function forceLogout(tokenId: string) {
  return defHttp.delete<void>({ url: Api.root + '/' + tokenId });
}

/**
 * 个人中心用的 跟上面的不同是用的Post
 * @param tokenId
 * @returns
 */
export function forceLogout2(tokenId: string) {
  return defHttp.post<void>({ url: Api.root + '/' + tokenId });
}
