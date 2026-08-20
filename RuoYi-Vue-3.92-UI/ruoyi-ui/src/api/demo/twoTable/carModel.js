import request from '@/utils/request'

// 查询车辆型号列表
export function listCarModel(query) {
  return request({
    url: '/demo/twotable/carmodel/list',
    method: 'get',
    params: query
  })
}

// 查询车辆型号详细
export function getCarModel(id) {
  return request({
    url: '/demo/twotable/carmodel/' + id,
    method: 'get'
  })
}

// 新增车辆型号
export function addCarModel(data) {
  return request({
    url: '/demo/twotable/carmodel',
    method: 'post',
    data: data
  })
}

// 修改车辆型号
export function updateCarModel(data) {
  return request({
    url: '/demo/twotable/carmodel',
    method: 'put',
    data: data
  })
}

// 删除车辆型号
export function delCarModel(id) {
  return request({
    url: '/demo/twotable/carmodel/' + id,
    method: 'delete'
  })
}

// 导出车辆型号
export function exportCarModel(query) {
  return request({
    url: '/demo/twotable/carmodel/export',
    method: 'get',
    params: query
  })
}
