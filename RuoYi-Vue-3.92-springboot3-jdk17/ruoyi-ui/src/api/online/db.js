import request from '@/utils/request'

export function listDbTable(query) {
  return request({
    url: '/online/db/table/list',
    method: 'get',
    params: query
  })
}

export function listDbColumn(tableName) {
  return request({
    url: '/online/db/column/list',
    method: 'get',
    params: { tableName }
  })
}
