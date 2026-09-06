import { RouteItem } from '@/types/route'
import { LoginForm, RegisterForm } from '@/types/user'
import request, { getAction, postAction } from '@/utils/request'

export const login = (data: LoginForm) => postAction('/login', data, { isToken: false })
export const register = (data: RegisterForm) => postAction('/register', data, { isToken: false })
export const getInfo = () => getAction('/getInfo')
export const logout = () => postAction('/logout')
export const getRouters = () => getAction<RouteItem[]>('/getRouters')

type CodeType = 'register' | 'login'
type CodeMethod = 'mail' | 'dySms'
type CodeUse = 'send' | 'verify'

export function sendCode(data: LoginForm, type: CodeType, method: CodeMethod, use: CodeUse) {
  return request({
    url: `/auth/${method}/${use}/${type}`,
    headers: {
      isToken: (type == 'register' || type == 'login') ? false : true
    },
    method: 'post',
    timeout: 20000,
    data,
    params: {
      autoRegister: data.autoRegister
    },
  })
}

export const sendEmailCode = (data: LoginForm, type: CodeType) => sendCode(data, type, 'mail', 'send');
export const verifyEmailCode = (data: LoginForm, type: CodeType) => sendCode(data, type, 'mail', 'verify');
export const sendPhoneCode = (data: LoginForm, type: CodeType) => sendCode(data, type, 'dySms', 'send');
export const verifyPhoneCode = (data: LoginForm, type: CodeType) => sendCode(data, type, 'dySms', 'verify');
