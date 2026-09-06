import type { JobQueryParams, SysJob } from '@/types/api/monitor/job'
import type { TableDataInfo } from '@/types/api/system/common'

import response from '@/utils/http'

// 查询定时任务调度列表
export function listJob(query: JobQueryParams & Record<string, any>) {
  return response<TableDataInfo<SysJob>>({
    url: '/monitor/job/list',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度详细
export function getJob(jobId: number) {
  return response<SysJob>({
    url: `/monitor/job/${jobId}`,
    method: 'get'
  })
}

// 新增定时任务调度
export function addJob(data: SysJob) {
  return response({
    url: '/monitor/job',
    method: 'post',
    data
  })
}

// 修改定时任务调度
export function updateJob(data: SysJob) {
  return response({
    url: '/monitor/job',
    method: 'put',
    data
  })
}

// 删除定时任务调度
export function delJob(jobId: number | number[]) {
  return response({
    url: `/monitor/job/${jobId}`,
    method: 'delete'
  })
}

// 任务状态修改
export function changeJobStatus(jobId: number, status: string) {
  return response({
    url: '/monitor/job/changeStatus',
    method: 'put',
    data: { jobId, status }
  })
}

// 定时任务立即执行一次
export function runJob(jobId: number, jobGroup: string) {
  return response({
    url: '/monitor/job/run',
    method: 'put',
    data: { jobId, jobGroup }
  })
}
