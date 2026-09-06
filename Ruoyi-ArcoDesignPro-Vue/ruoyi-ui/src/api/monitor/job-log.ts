import axios from 'axios';

export interface JobLogRecord {
    createBy?: any;
    createTime?: string;
    updateBy?: any;
    updateTime?: any;
    remark?: any;
    jobLogId?: number;
    jobName?: string;
    jobGroup?: string;
    invokeTarget?: string;
    jobMessage?: string;
    status?: string;
    exceptionInfo?: string;
    startTime?: any;
    stopTime?: any;
}

// 查询调度日志列表
export function listJobLog(query: any) {
    return axios({
        url: '/monitor/jobLog/list',
        method: 'get',
        params: query
    })
}

// 删除调度日志
export function delJobLog(jobLogId: number | string) {
    return axios({
        url: `/monitor/jobLog/${jobLogId}`,
        method: 'delete'
    })
}

// 清空调度日志
export function cleanJobLog() {
    return axios({
        url: '/monitor/jobLog/clean',
        method: 'delete'
    })
}
