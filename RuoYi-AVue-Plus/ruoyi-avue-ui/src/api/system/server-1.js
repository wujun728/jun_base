import request from '@/utils/request'

// 查询用户列表
export function list(query) {
  return request({
    url: '/system/server/type/list',
    method: 'post',
    data: query
  })
}

// 查询用户列表
export function update(query) {
  return request({
    url: '/system/server/type/update',
    method: 'post',
    data: query
  })
}

// 保存服务列表
export function add(query) {
  return request({
    url: '/system/server/type/save',
    method: 'post',
    data: query
  })
}

// 删除服务
export function del(query) {
  return request({
    url: '/system/server/type/del',
    method: 'post',
    data: query
  })
}

/**
 * 获取主键编号
 * @param id
 * @returns {AxiosPromise}
 */
export function getServerObjectApi(id) {
  return request({
    url: '/system/server/type/object',
    method: 'post',
    data: {"id": id}
  })
}
