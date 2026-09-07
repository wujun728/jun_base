import { UploadApiResult } from './model';
import { defHttp } from '@/utils/http/axios';
import { UploadFileParams } from '#/axios';
import { AxiosProgressEvent } from 'axios';

/**
 * @description: Upload interface
 */
export function uploadApi(
  params: UploadFileParams,
  onUploadProgress?: (progressEvent: AxiosProgressEvent) => void,
) {
  return defHttp.uploadFile<UploadApiResult>(
    {
      // 固定url地址
      url: '/common/upload',
      onUploadProgress,
    },
    params,
  );
}
