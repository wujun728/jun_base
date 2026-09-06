import request from '@/config/request.js';

// 获取通知公告列表
export const noticeList = (params) => request.get('/system/notice/list', params)

// 获取通知公告详情信息
export const noticeById = (id) => request.get('/system/notice/' + id)

// 新增通知公告详情信息
export const noticeAdd = (params) => request.post('/system/notice', params)

// 修改通知公告详情信息
export const noticeModify = (params) => request.put('/system/notice', params)

// 删除通知公告详情信息
export const noticeDelete = (id) => request.delete('/system/notice/' + id)