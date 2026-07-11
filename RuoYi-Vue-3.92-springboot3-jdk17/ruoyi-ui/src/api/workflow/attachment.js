import request from '@/utils/request'

// 查询流程业务附件列表
export function listAttachment(businessId) {
  return request({
    url: '/workfile/attachment/list/' + businessId,
    method: 'get'
  })
}

// 新增流程业务附件
export function addAttachment(data) {
  return request({
    url: '/workfile/attachment',
    method: 'post',
    data: data
  })
}

// 删除流程业务附件
export function remove(id) {
  return request({
    url: '/workfile/attachment/' + id,
    method: 'delete'
  })
}
