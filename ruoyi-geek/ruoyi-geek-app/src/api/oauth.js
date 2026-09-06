import request from '@/utils/request'
/**
 * 微信登录
 * @param {*} source pub miniapp
 * @param {*} code 
 * @returns 
 */
export function wxLogin(source,code) {
    return request({
        url: `/oauth/wx/login/${source}/${code}`,
        headers: {
            isToken: false
        },
        method: 'post',
    })
}
/**
 * 微信绑定
 * @param {*} source 微信绑定
 * @param {*} code 
 * @returns 
 */
export function wxRegister(source,code) {
    return request({
        url: `/oauth/wx/register/${source}/${code}`,
        headers: {
            isToken: true
        },
        method: 'post',
    })
}