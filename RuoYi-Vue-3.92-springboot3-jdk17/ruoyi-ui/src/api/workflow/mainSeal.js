import request from '@/utils/request'

// 查询正文印章列表
export function listSeal(query) {
  return request({
    url: '/workflow/seal/list',
    method: 'get',
    params: query
  })
}

// 获取所有有效印章
export function findAllSeals() {
  return request({
    url: '/workflow/seal/findAllSeals',
    method: 'get'
  })
}

// 查询正文印章详细
export function getSeal(id) {
  return request({
    url: '/workflow/seal/' + id,
    method: 'get'
  })
}

// 预览印章
export function previewSeal(data) {
  return request({
    url: '/workflow/seal/previewMainSeal',
    method: 'post',
    data: data
  })
}

// 启、停配置
export function changeEnableFlag(id, enableFlag) {
  const data = {
    id,
    enableFlag
  }
  return request({
    url: '/workflow/seal/changeEnableFlag',
    method: 'put',
    data: data
  })
}

// 创建印章
export function createSeal(data) {
  return request({
    url: '/workflow/seal/createMainSeal',
    method: 'post',
    data: data
  })
}

// 修改正文印章
export function updateSeal(data) {
  return request({
    url: '/workflow/seal',
    method: 'put',
    data: data
  })
}

// 删除正文印章
export function delSeal(id) {
  return request({
    url: '/workflow/seal/' + id,
    method: 'delete'
  })
}
