import axios from 'axios';

// 查询登录日志列表
export function listLogininfor(query: any) {
    return axios({
        url: '/monitor/logininfor/list',
        method: 'get',
        params: query
    })
}

// 删除登录日志
export function delLogininfor(infoId: number | string) {
    return axios({
        url: `/monitor/logininfor/${infoId}`,
        method: 'delete'
    })
}

// 解锁用户登录状态
export function unlockLogininfor(userName: string) {
    return axios({
        url: `/monitor/logininfor/unlock/${userName}`,
        method: 'get'
    })
}

// 清空登录日志
export function cleanLogininfor() {
    return axios({
        url: '/monitor/logininfor/clean',
        method: 'delete'
    })
}
