import request from '@/config/request.js';

// 获取操作日志
export const operLog = (params) => request.get('/monitor/operlog/list', params)