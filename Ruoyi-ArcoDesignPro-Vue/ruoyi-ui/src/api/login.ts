import axios from 'axios';

export interface LoginData {
    username: string;
    password: string;
    code: string;
    uuid: string;
}

export interface CodeImg {
    img: string;
    uuid: string;
    captchaEnabled: boolean;
}

// 登录方法
export function login(data: LoginData) {
    return axios({
        url: '/login',
        headers: {
            isToken: false
        },
        method: 'post',
        data
    } as any)
}

// 注册方法
export function register(data: any) {
    return axios({
        url: '/register',
        headers: {
            isToken: false
        },
        method: 'post',
        data
    } as any)
}

// 获取用户详细信息
export function getInfo() {
    return axios({
        url: '/getInfo',
        method: 'get'
    })
}

// 退出方法
export function logout() {
    return axios({
        url: '/logout',
        method: 'post'
    })
}

// 获取验证码
export function getCodeImg() {
    return axios({
        url: '/captchaImage',
        headers: {
            isToken: false
        },
        method: 'get',
        timeout: 20000
    } as any)
}
