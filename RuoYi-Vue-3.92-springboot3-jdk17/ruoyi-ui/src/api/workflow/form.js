import request from '@/utils/request'

// 查询流程表单
export function getForm(params) {
  return request({
    url: '/biz/form/info',
    method: 'get',
    params: params
  })
}

// 新增流程表单
export function addForm(data) {
  return request({
    url: '/biz/form/save',
    method: 'post',
    data: data
  })
}

// 修改流程表单
export function updateForm(data) {
  return request({
    url: '/biz/form/update',
    method: 'post',
    data: data
  })
}

// 业务按钮数据
export function getButtons(params) {
  return request({
    url: '/biz/button/list',
    method: 'get',
    params: params
  })
}

// 盖章文件预览（pdf）
export function preview(params) {
  return request({
    url: '/file/operate/preview',
    method: 'get',
    params: params,
    responseType: "blob"
  })
}