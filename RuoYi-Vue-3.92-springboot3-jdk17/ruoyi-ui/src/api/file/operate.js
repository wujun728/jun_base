import request from '@/utils/request'

// 文件预览
export function previewFile(param) {
  return request({
    url: '/file/operate/preview',
    method: 'get',
    params: param
  })
}

// 文件下载
export function downloadfile(param) {
  return request({
    url: '/file/operate/downloadfile',
    method: 'get',
    params: param
  })
}

// 文件重命名
export function rename(data) {
  return request({
    url: '/file/operate/rename',
    method: 'post',
    data: data
  })
}

// 文件排序
export function sort(data) {
  return request({
    url: '/file/operate/sort',
    method: 'post',
    data: data
  })
}

// 文件排序
export function delFile(data) {
  return request({
    url: '/file/operate/delFile',
    method: 'post',
    data: data
  })
}