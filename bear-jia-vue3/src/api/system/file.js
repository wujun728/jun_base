import request from '@/utils/request';

// 查询文件列表
export function listFile(query) {
  return request({
    url: '/system/file/list',
    method: 'get',
    params: query,
  });
}

// 查询文件详细
export function getFile(id) {
  return request({
    url: '/system/file/' + id,
    method: 'get',
  });
}

// 根据业务类型查询文件列表
export function getFilesByBusiness(businessType, businessId) {
  return request({
    url: `/system/file/business/${businessType}`,
    method: 'get',
    params: { businessId },
  });
}

// 查询RAG文档列表
export function getRagDocuments(knowledgeBaseId) {
  return request({
    url: '/system/file/rag',
    method: 'get',
    params: { knowledgeBaseId },
  });
}

// 查询用户头像
export function getUserAvatar(userId) {
  return request({
    url: `/system/file/avatar/${userId}`,
    method: 'get',
  });
}

// 获取文件统计信息
export function getFileStats() {
  return request({
    url: '/system/file/stats',
    method: 'get',
  });
}

// 查询重复文件
export function getDuplicateFiles() {
  return request({
    url: '/system/file/duplicates',
    method: 'get',
  });
}

// 查询孤立文件
export function getOrphanFiles() {
  return request({
    url: '/system/file/orphans',
    method: 'get',
  });
}

// 新增文件
export function addFile(data) {
  return request({
    url: '/system/file',
    method: 'post',
    data: data,
  });
}

// 修改文件
export function updateFile(data) {
  return request({
    url: '/system/file',
    method: 'put',
    data: data,
  });
}

// 更新文件状态
export function updateFileStatus(id, status) {
  return request({
    url: `/system/file/status/${id}/${status}`,
    method: 'put',
  });
}

// 更新处理进度
export function updateFileProgress(id, progress, errorMessage) {
  return request({
    url: `/system/file/progress/${id}`,
    method: 'put',
    params: { progress, errorMessage },
  });
}

// 删除文件
export function delFile(ids) {
  return request({
    url: '/system/file/' + ids,
    method: 'delete',
  });
}

// 物理删除文件
export function delFilePhysically(ids) {
  return request({
    url: '/system/file/physical/' + ids,
    method: 'delete',
  });
}

// 清理已删除文件
export function cleanDeletedFiles(days) {
  return request({
    url: `/system/file/clean/${days}`,
    method: 'delete',
  });
}

// 上传文件
export function uploadFile(formData) {
  return request({
    url: '/system/file/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

// 上传RAG文档
export function uploadRagDocument(formData) {
  return request({
    url: '/system/file/upload/rag',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

// 上传用户头像
export function uploadUserAvatar(formData) {
  return request({
    url: '/system/file/upload/avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

// 上传系统附件
export function uploadSystemAttachment(formData) {
  return request({
    url: '/system/file/upload/attachment',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

// 获取文件下载URL
export function getFileDownloadUrl(id) {
  return request({
    url: `/system/file/download/${id}`,
    method: 'get',
  });
}

// 获取文件预览URL
export function getFilePreviewUrl(id) {
  return request({
    url: `/system/file/preview/${id}`,
    method: 'get',
  });
}

// 检查文件是否存在
export function checkFileExists(contentHash) {
  return request({
    url: '/system/file/exists',
    method: 'post',
    params: { contentHash },
  });
}

// 文件上传工具函数
export function createUploadFormData(file, businessType, businessId) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('businessType', businessType);
  if (businessId) {
    formData.append('businessId', businessId);
  }
  return formData;
}

// RAG文档上传工具函数
export function createRagUploadFormData(file, knowledgeBaseId) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('knowledgeBaseId', knowledgeBaseId);
  return formData;
}

// 用户头像上传工具函数
export function createAvatarUploadFormData(file, userId) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('userId', userId);
  return formData;
}

// 系统附件上传工具函数
export function createAttachmentUploadFormData(file, relatedId) {
  const formData = new FormData();
  formData.append('file', file);
  if (relatedId) {
    formData.append('relatedId', relatedId);
  }
  return formData;
}

// 文件大小格式化
export function formatFileSize(bytes) {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

// 获取文件类型图标
export function getFileTypeIcon(fileType) {
  const iconMap = {
    pdf: 'file-pdf',
    doc: 'file-word',
    docx: 'file-word',
    xls: 'file-excel',
    xlsx: 'file-excel',
    ppt: 'file-powerpoint',
    pptx: 'file-powerpoint',
    txt: 'file-text',
    md: 'file-markdown',
    html: 'file-code',
    css: 'file-code',
    js: 'file-code',
    json: 'file-code',
    xml: 'file-code',
    jpg: 'file-image',
    jpeg: 'file-image',
    png: 'file-image',
    gif: 'file-image',
    bmp: 'file-image',
    svg: 'file-image',
    mp4: 'file-video',
    avi: 'file-video',
    mov: 'file-video',
    wmv: 'file-video',
    mp3: 'file-audio',
    wav: 'file-audio',
    flac: 'file-audio',
    zip: 'file-archive',
    rar: 'file-archive',
    '7z': 'file-archive',
    tar: 'file-archive',
    gz: 'file-archive',
  };
  return iconMap[fileType?.toLowerCase()] || 'file';
}

// 业务类型映射
export const businessTypeMap = {
  rag: 'RAG文档',
  avatar: '用户头像',
  attachment: '系统附件',
  common: '通用文件',
};

// 文件状态映射
export const fileStatusMap = {
  0: '已删除',
  1: '正常',
  2: '处理中',
  3: '处理失败',
};

// 文件状态标签类型
export const fileStatusTagType = {
  0: 'danger',
  1: 'success',
  2: 'warning',
  3: 'danger',
};

// 文件夹相关API

// 查询文件夹树结构
export function getFolderTree(parentId, businessType) {
  return request({
    url: '/system/file/folderTree',
    method: 'get',
    params: { parentId, businessType },
  });
}

// 查询指定文件夹下的内容
export function getFilesByParentId(parentId, businessType) {
  return request({
    url: `/system/file/folder/${parentId}`,
    method: 'get',
    params: { businessType },
  });
}

// 创建文件夹
export function createFolder(data) {
  return request({
    url: '/system/file/createFolder',
    method: 'post',
    params: data,
  });
}

// 移动文件
export function moveFiles(data) {
  return request({
    url: '/system/file/move',
    method: 'post',
    params: data,
  });
}

// 重命名文件或文件夹
export function renameFile(data) {
  return request({
    url: '/system/file/rename',
    method: 'post',
    params: data,
  });
}

// 获取文件夹路径
export function getFolderPath(folderId) {
  return request({
    url: `/system/file/path/${folderId}`,
    method: 'get',
  });
}

// 检查文件夹名称是否存在
export function checkFolderName(parentId, folderName, businessType, excludeId) {
  return request({
    url: '/system/file/checkFolderName',
    method: 'get',
    params: { parentId, folderName, businessType, excludeId },
  });
}
