import request from '@/config/request.js';

// 获取用户信息
export const getInfo = () => request.get('/getInfo')