import request from '@/utils/request'

// 查询消息管理列表
export function listMessageSystem(query) {
  return request({
    url: '/modelMessage/messageSystem/list',
    method: 'get',
    params: query
  })
}

// 查询消息管理详细
export function getMessageSystem(messageId) {
  return request({
    url: '/modelMessage/messageSystem/' + messageId,
    method: 'get'
  })
}

// 修改消息管理
export function updateMessageSystem(data) {
  return request({
    url: '/modelMessage/messageSystem',
    method: 'put',
    data: data
  })
}

// 删除消息管理
export function delMessageSystem(messageId) {
  return request({
    url: '/modelMessage/messageSystem/' + messageId,
    method: 'delete'
  })
}

// 统一查询系统资源信息（角色、部门、用户）
export function getSystemResource(type, id, sendMode) {
  const params = { type };
  if (id !== null && id !== undefined) {
    params.id = id;
  }
  if (sendMode !== null && sendMode !== undefined) {
    params.sendMode = sendMode;
  }

  return request({
    url: '/modelMessage/messageSystem/systemResource',
    method: 'get',
    params: params
  });
}

//点击信息详情状态调整为已读
export function getUpdate(messageId) {
  return request({
    url: '/modelMessage/messageSystem/' + messageId,
    method: 'post'
  })
}

// 批量发送消息
export function batchAddMessage(data) {
  return request({
    url: '/modelMessage/messageSystem',
    method: 'post',
    data: data,
    headers: { 'isRepeatSubmit': false } 
  })
}
