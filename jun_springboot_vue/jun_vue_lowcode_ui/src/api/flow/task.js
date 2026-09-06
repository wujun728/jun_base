import request from '@/utils/request'

// 查询task列表
export function listTask(query) {
  return request({
    url: '/nocode/flow/taskList',
    method: 'get',
    params: query
  })
}





