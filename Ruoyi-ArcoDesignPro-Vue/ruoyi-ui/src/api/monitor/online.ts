import axios from 'axios';

export interface OnlineRecord {
    tokenId: string;
    deptName: string;
    userName: string;
    ipaddr: string;
    loginLocation: string;
    browser: string;
    os: string;
    loginTime: number;
}

// 查询在线用户列表
export function listOnline(query: any) {
    return axios({
        url: '/monitor/online/list',
        method: 'get',
        params: query
    })
}

// 强退用户
export function forceLogout(tokenId: string) {
    return axios({
        url: `/monitor/online/${tokenId}`,
        method: 'delete'
    })
}
