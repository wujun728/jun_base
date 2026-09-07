import { ContentTypeEnum } from '@/enums/httpEnum';
import { defHttp } from '@/utils/http/axios';

export type ID = string | number;
export type IDS = (string | number)[];

export interface BaseEntity {
  createBy?: any;
  createDept?: any;
  createTime?: string;
  updateBy?: any;
  updateTime?: any;
}

/**
 * 分页查询参数
 * @param pageNum 当前页
 * @param pageSize 每页大小
 * @param orderByColumn 排序字段
 * @param isAsc 是否升序
 */
export interface PageQuery {
  pageNum?: number;
  pageSize?: number;
  orderByColumn?: string;
  isAsc?: boolean;
}

/**
 * 通用下载接口 封装一层
 * @param url 请求地址
 * @param data  请求参数
 * @returns
 */
export function commonExport(url: string, data: Recordable) {
  return defHttp.post<Blob>(
    {
      url,
      responseType: 'blob',
      data,
      headers: { 'Content-Type': ContentTypeEnum.FORM_URLENCODED },
    },
    { isTransformResponse: false },
  );
}
