import request from '@/config/request.js';

// 用户登录(账号+密码+验证码)
export const login = (params) => request.post('/login', params, { custom: { auth: false } })

// 用户退出
export const logout = () => request.get('/logout')