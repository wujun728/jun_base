import axios from 'axios';

export interface OperlogRecord {
    createBy?: any;
    createTime?: any;
    updateBy?: any;
    updateTime?: any;
    remark?: any;
    operId: number;
    title: string;
    businessType: number;
    businessTypes?: any;
    method: string;
    requestMethod: string;
    operatorType: number;
    operName: string;
    deptName?: any;
    operUrl: string;
    operIp: string;
    operLocation: string;
    operParam: string;
    jsonResult: string;
    status: number;
    errorMsg?: any;
    operTime: string;
    costTime: number;
}

// 查询操作日志列表
export function listOperlog(query: any) {
    return axios({
        url: '/monitor/operlog/list',
        method: 'get',
        params: query
    })
}

// 删除操作日志
export function delOperlog(operId: number | string) {
    return axios({
        url: `/monitor/operlog/${operId}`,
        method: 'delete'
    })
}

// 清空操作日志
export function cleanOperlog() {
    return axios({
        url: '/monitor/operlog/clean',
        method: 'delete'
    })
}
