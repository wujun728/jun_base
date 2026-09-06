import request from '@/utils/request'

// 查询已办任务列表
export function finishedList(query) {
  return request({
    url: '/flowable/task/finishedList',
    method: 'get',
    params: query
  })
}

// 任务流转记录
export function flowRecord(query) {
  return request({
    url: '/flowable/task/flowRecord',
    method: 'get',
    params: query
  })
}

// 部署流程实例
export function deployStart(deployId) {
  return request({
    url: '/flowable/process/startFlow/' + deployId,
    method: 'get',
  })
}

// 删除流程定义
export function delDeployment(id) {
  return request({
    url: '/flowable/instance/delete/' + id,
    method: 'delete'
  })
}

// 撤回流程
export function revokeProcess(data) {
  return request({
    url: '/flowable/task/revokeProcess',
    method: 'post',
    data: data
  })
}

// 导入流程文件
export function importDeployment(name, category, file) {
  const formData = new FormData();
  if (name) formData.append('name', name);
  if (category) formData.append('category', category);
  formData.append('file', file);
  
  return request({
    url: '/flowable/definition/import',
    method: 'post',
    data: formData
  });
}
