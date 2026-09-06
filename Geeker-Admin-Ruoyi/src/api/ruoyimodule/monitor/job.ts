import http from "@/api";
import { addDateRange } from "@/utils";
import { JobLog, ResPage } from "@/api/interface";

// 查询定时任务调度列表
export function listJob(query) {
  return http.get("/monitor/job/list", query);
}

// 查询定时任务调度详细
export function getJob(jobId) {
  return http.get("/monitor/job/" + jobId);
}

// 新增定时任务调度
export function addJob(data) {
  return http.post("/monitor/job", data);
}

// 修改定时任务调度
export function updateJob(data) {
  return http.put("/monitor/job", data);
}

// 删除定时任务调度
export function delJob(jobId) {
  return http.delete("/monitor/job/" + jobId);
}

// 定时任务立即执行一次
export function runJob(jobId, jobGroup) {
  const data = {
    jobId,
    jobGroup
  };
  return http.put("/monitor/job/run", data);
}

// 导出数据
export const exportJobInfo = params => {
  return http.download(`monitor/job/export`, params);
};
// 查询调度日志列表
export const listJobLog = query => {
  query = addDateRange(query, "createTime");
  console.log(query);

  return new Promise((resolve, reject) => {
    http
      .get<ResPage<JobLog.ResJobLogInfo>>(`/monitor/jobLog/list`, query)
      .then(res => {
        resolve(res);
      })
      .catch(err => reject(err));
  });
};

// 删除调度日志
export function delJobLog(jobLogId) {
  return http.delete("/monitor/jobLog/" + jobLogId);
}

// 清空调度日志
export function cleanJobLog() {
  return http.delete("/monitor/jobLog/clean");
}
export const exportJobLogInfo = params => {
  return http.download("monitor/jobLog/export", params);
};
