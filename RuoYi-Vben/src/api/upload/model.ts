/**
 * @description 文件上传返回
 * @param fileName 文件名
 * @param ossId ossId
 * @param url 文件地址
 */
export interface UploadApiResult {
  fileName: string;
  ossId: string;
  url: string;
}
