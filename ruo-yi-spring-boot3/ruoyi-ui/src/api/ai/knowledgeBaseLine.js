import request from '@/utils/request'

// 查询知识库单栏列表
export function listKnowledgeBaseLine(query) {
  return request({
    url: '/ai/knowledgeBaseLine/list',
    method: 'get',
    params: query
  })
}

// 查询知识库单栏详细
export function getKnowledgeBaseLine(id) {
  return request({
    url: '/ai/knowledgeBaseLine/' + id,
    method: 'get'
  })
}

// 知识库单栏向量化
export function withDocumentStorageByPg(id) {
  return request({
    url: '/ai/knowledgeBaseLine/withDocumentStorageByPg/' + id,
    method: 'get'
  })
}

// 新增知识库单栏
export function addKnowledgeBaseLine(data) {
  return request({
    url: '/ai/knowledgeBaseLine',
    method: 'post',
    data: data
  })
}


// 上传文件
export function upload(formData) {
  return request({
    url: '/ai/knowledgeBaseLine/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
}

// 修改知识库单栏
export function updateKnowledgeBaseLine(data) {
  return request({
    url: '/ai/knowledgeBaseLine',
    method: 'put',
    data: data
  })
}

// 删除知识库单栏
export function delKnowledgeBaseLine(id) {
  return request({
    url: '/ai/knowledgeBaseLine/' + id,
    method: 'delete'
  })
}
