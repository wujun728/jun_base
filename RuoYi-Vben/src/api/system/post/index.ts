import { defHttp } from '@/utils/http/axios';
import { PageQuery, ID, IDS, commonExport } from '@/api/base';
import { Post } from './model';

enum Api {
  root = '/system/post',
  postSelect = '/system/post/optionselect',
  postList = '/system/post/list',
  postExport = '/system/post/export',
}

export function postList(params?: PageQuery) {
  return defHttp.get<Post[]>({ url: Api.postList, params });
}

export function postExport(data: any) {
  return commonExport(Api.postExport, data);
}

export function postInfo(postId: ID) {
  return defHttp.get<Post>({ url: Api.root + '/' + postId });
}

export function postAdd(params: any) {
  return defHttp.postWithMsg<void>({ url: Api.root, params });
}

export function postUpdate(data: any) {
  return defHttp.putWithMsg<void>({ url: Api.root, data });
}

export function postRemove(postIds: IDS) {
  return defHttp.deleteWithMsg<void>({ url: Api.root + '/' + postIds });
}

/**
 * 这个方法未使用过
 *
 * 2024年4月30日21:38:25  使用
 * @returns
 */
export function postOptionSelect(deptId: string | number) {
  return defHttp.get({ url: Api.postSelect, params: { deptId } });
}
