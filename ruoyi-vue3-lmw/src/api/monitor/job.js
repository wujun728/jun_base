import { request } from '@/utils/hsj/service/index'

// 任务状态修改
export function changeJobStatus(jobId, status) {
  const data = {
    jobId,
    status,
  }
  return request({
    url: '/monitor/job/changeStatus',
    method: 'put',
    data: data,
  })
}

// 定时任务立即执行一次
export function runJob(jobId, jobGroup) {
  const data = {
    jobId,
    jobGroup,
  }
  return request({
    url: '/monitor/job/run',
    method: 'put',
    data: data,
  })
}
