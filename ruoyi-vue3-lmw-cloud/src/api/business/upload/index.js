import request from '@/utils/hsj/service'
import { businessBase } from '@/api/config/base'
export function selectFileList(params) {
  return request.get({
    url: '/file/uploader/selectFileList',
    params,
  })
}
export function resetFile(id) {
  return request.post({
    url: '/file/uploader/reprocess/' + id,
  })
}

// 询问文件是否上传过
export function getCurrentChunk(params) {
  return request.get({
    url: '/file/uploader/chunk',
    params,
  })
}

export function mergeSimpleUpload(data) {
  return request.post({
    url: '/file/uploader/mergeFile',
    data,
  })
}

export function uploadCell(data) {
  request.post({
    url: '/admin/source/uploadCellImg',
    data,
  })
}

// 是否可以上传
export function getCheck(data) {
  return request.post({
    url: '/file/uploader/check',
    data,
  })
}

// 删除文件
export function deleteFile(data) {
  return request.post({
    url: '/file/uploader/deleteFile',
    data,
  })
}

export function enclosureUpload(data) {
  return request.post({
    url: '/file/enclosureUpload',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

export function changeSection(data) {
  return request.put({
    url: businessBase + '/section',
    data,
  })
}

export function changeFileName(data) {
  return request.put({
    url: businessBase + '/section',
    data,
  })
}

export function aiPushSend(data) {
  return request.post({
    url: '/file/uploader/pushUpdateSection',
    data,
  })
}
