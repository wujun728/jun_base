import axios from 'axios';

export interface JobRecord {
    createBy?: string;
    createTime?: string;
    updateBy?: any;
    updateTime?: any;
    remark?: string;
    jobId?: number;
    jobName?: string;
    jobGroup?: string;
    invokeTarget?: string;
    cronExpression?: string;
    misfirePolicy?: string;
    concurrent?: string;
    status?: string;
    nextValidTime?: string;
}

// 查询定时任务调度列表
export function listJob(query: any) {
    return axios({
        url: '/monitor/job/list',
        method: 'get',
        params: query
    })
}

// 查询定时任务调度详细
export function getJob(jobId: number | string) {
    return axios({
        url: `/monitor/job/${jobId}`,
        method: 'get'
    })
}

// 新增定时任务调度
export function addJob(data: JobRecord) {
    return axios({
        url: '/monitor/job',
        method: 'post',
        data
    })
}

// 修改定时任务调度
export function updateJob(data: JobRecord) {
    return axios({
        url: '/monitor/job',
        method: 'put',
        data
    })
}

// 删除定时任务调度
export function delJob(jobId: number) {
    return axios({
        url: `/monitor/job/${jobId}`,
        method: 'delete'
    })
}

// 任务状态修改
export function changeJobStatus(jobId: number, status: string) {
    const data = {
        jobId,
        status
    }
    return axios({
        url: '/monitor/job/changeStatus',
        method: 'put',
        data
    })
}

// 定时任务立即执行一次
export function runJob(jobId: number, jobGroup: string) {
    const data = {
        jobId,
        jobGroup
    }
    return axios({
        url: '/monitor/job/run',
        method: 'put',
        data
    })
}
