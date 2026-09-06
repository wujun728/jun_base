import request from '@/config/request.js';

// 获取用户列表
export const userList = (params) => request.get('/system/user/list', params)

// 获取用户信息
export const userById = (id) => request.get('/system/user/' + id)