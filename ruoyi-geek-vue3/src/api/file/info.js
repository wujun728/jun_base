import request from '@/utils/request'
import { getToken } from '@/utils/auth';

// 查询文件信息列表
export function listInfo(query) {
  return request({
    url: '/file/info/list',
    method: 'get',
    params: query
  })
}

// 查询文件信息详细
export function getInfo(fileId) {
  return request({
    url: '/file/info/' + fileId,
    method: 'get'
  })
}

// 新增文件信息
export function addInfo(data) {
  return request({
    url: '/file/info',
    method: 'post',
    data: data
  })
}

// 修改文件信息
export function updateInfo(data) {
  return request({
    url: '/file/info',
    method: 'put',
    data: data
  })
}

// 删除文件信息
export function delInfo(fileId) {
  return request({
    url: '/file/info/' + fileId,
    method: 'delete'
  })
}

// 统一上传接口
export function uploadFileUnified({ storageType, clientName, file }) {
  const formData = new FormData();
  formData.append('file', file);
  return request({
    url: `/file/${storageType}/${clientName}/upload`,
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data', Authorization: 'Bearer ' + getToken() },
    data: formData
  });
}

// 统一下载接口（返回文件流）
export function downloadFileUnified({ storageType, clientName, filePath }) {
  return request({
    url: `/file/${storageType}/${clientName}/download`,
    method: 'get',
    params: { filePath },
    responseType: 'blob',
    headers: { Authorization: 'Bearer ' + getToken() }
  });
}

/**
 * 初始化分片上传
 */
export function initMultipartUpload(params) {
  return request({
    url: '/file/initUpload',
    method: 'post',
    params: {
      fileName: params.fileName,
      fileSize: params.fileSize,
    }
  });
}

/**
 * 上传文件分片
 */
export function uploadFileChunk(uploadId, filePath, partNumber, chunk) {
  const formData = new FormData();
  formData.append('chunk', chunk);
  return request({
    url: '/file/uploadChunk',
    method: 'post',
    params: {
      uploadId,
      filePath,
      partNumber
    },
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
      repeatSubmit: false
    }
  });
}

/**
 * 完成分片上传
 */
export function completeMultipartUpload(params) {
  const { uploadId, filePath, fileSize, fileName, partETags } = params;
  return request({
    url: '/file/completeUpload',
    method: 'post',
    params: {
      uploadId,
      filePath,
      fileSize,
      fileName
    },
    data: partETags
  });
}
