import request from '@/utils/request'

// 查询车辆品牌列表
export function listCar(query) {
  return request({
    url: '/demo/twotable/car/list',
    method: 'get',
    params: query
  })
}

// 查询车辆品牌详细
export function getCar(id) {
  return request({
    url: '/demo/twotable/car/' + id,
    method: 'get'
  })
}

// 新增车辆品牌
export function addCar(data) {
  return request({
    url: '/demo/twotable/car',
    method: 'post',
    data: data
  })
}

// 修改车辆品牌
export function updateCar(data) {
  return request({
    url: '/demo/twotable/car',
    method: 'put',
    data: data
  })
}

// 删除车辆品牌
export function delCar(id) {
  return request({
    url: '/demo/twotable/car/' + id,
    method: 'delete'
  })
}

// 导出车辆品牌
export function exportCar(query) {
  return request({
    url: '/demo/twotable/car/export',
    method: 'get',
    params: query
  })
}
