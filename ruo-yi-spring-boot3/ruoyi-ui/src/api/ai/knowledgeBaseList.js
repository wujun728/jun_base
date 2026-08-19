import request from '@/utils/request'

// 查询知识库列表列表
export function listKnowledgeBaseList(query) {
  return request({
    url: '/ai/knowledgeBaseList/list',
    method: 'get',
    params: query
  })
}

// 查询知识库列表详细
export function getKnowledgeBaseList(id) {
  return request({
    url: '/ai/knowledgeBaseList/' + id,
    method: 'get'
  })
}

// 查询知识库单列列表
export function getLineInfo(id) {
  return request({
    url: '/ai/knowledgeBaseList/getLineInfo/' + id,
    method: 'get'
  })
}


// 新增知识库列表
export function addKnowledgeBaseList(data) {
  return request({
    url: '/ai/knowledgeBaseList',
    method: 'post',
    data: data
  })
}

// 修改知识库列表
export function updateKnowledgeBaseList(data) {
  return request({
    url: '/ai/knowledgeBaseList',
    method: 'put',
    data: data
  })
}

// 删除知识库列表
export function delKnowledgeBaseList(id) {
  return request({
    url: '/ai/knowledgeBaseList/' + id,
    method: 'delete'
  })
}
