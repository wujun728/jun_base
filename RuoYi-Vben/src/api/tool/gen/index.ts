import { defHttp } from '@/utils/http/axios';
import { ID, IDS } from '@/api/base';
import { ContentTypeEnum } from '@/enums/httpEnum';

enum Api {
  root = '/tool/gen',
  generatedList = '/tool/gen/list',
  readyToGenList = '/tool/gen/db/list',
  columnList = '/tool/gen/column',
  importTable = '/tool/gen/importTable',
  preview = '/tool/gen/preview',
  download = '/tool/gen/download',
  genCode = '/tool/gen/genCode',
  syncDb = '/tool/gen/synchDb',
  batchGenCode = '/tool/gen/batchGenCode',
}
// 查询代码生成列表
export function generatedList(params: any) {
  return defHttp.get({ url: Api.generatedList, params });
}

// 修改代码生成业务
export function genInfo(tableId: ID) {
  return defHttp.get({ url: Api.root + '/' + tableId });
}

// 查询数据库列表
export function readyToGenList(params: any) {
  return defHttp.get({ url: Api.readyToGenList, params });
}

// 查询数据表字段列表
export function columnList(tableId: ID) {
  return defHttp.get({ url: Api.columnList + '/' + tableId });
}

/**
 * 导入表结构（保存）
 * @param tables table名称数组 如sys_a, sys_b
 * @param dataName 数据源名称
 * @returns ret
 */
export function importTable(tables: string[] | string, dataName: string, successMsg = true) {
  return defHttp.post(
    {
      url: Api.importTable,
      data: { tables, dataName },
      headers: { 'Content-Type': ContentTypeEnum.FORM_URLENCODED },
    },
    { successMessageMode: successMsg ? 'message' : 'none' },
  );
}

// 修改保存代码生成业务
export function editSave(data: any, successMsg = true) {
  return defHttp.put(
    { url: Api.root, data },
    { successMessageMode: successMsg ? 'message' : 'none' },
  );
}

// 删除代码生成
export function genRemove(tableIds: IDS, successMsg = true) {
  return defHttp.delete(
    { url: Api.root + '/' + tableIds },
    { successMessageMode: successMsg ? 'message' : 'none' },
  );
}
// 预览代码
export function preview(tableId: ID) {
  return defHttp.get<{ [key: string]: string }>({ url: Api.preview + '/' + tableId });
}

// 生成代码（下载方式）
export function genDownload(tableId: ID) {
  return defHttp.get({ url: Api.download + '/' + tableId });
}

// 生成代码（自定义路径）
export function genDownloadWithPath(tableId: ID) {
  return defHttp.get({ url: Api.download + '/' + tableId });
}

// 同步数据库
export function syncDb(tableId: ID, successMsg = true) {
  return defHttp.get(
    { url: Api.syncDb + '/' + tableId },
    { successMessageMode: successMsg ? 'message' : 'none' },
  );
}

// 批量生成代码
export function batchGenCode(tableIdStr: ID | IDS) {
  return defHttp.get<Blob>(
    { url: Api.batchGenCode, params: { tableIdStr }, responseType: 'blob' },
    { isTransformResponse: false },
  );
}
