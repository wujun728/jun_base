import request from './request'
/**
 * @description 获取项目列表
 */
export function getProgramListApi(data) {
  return request.get('program', data)
}

/**
 * @description 保存项目列表
 */
export function saveProgramApi(data) {
  return request.post(`program`, data)
}

/**
 * @description 修改项目列表
 */
export function putProgramApi(id, data) {
  return request.put(`program/${id}`, data)
}

/**
 * @description 获取项目详情
 */
export function getProgramInfoApi(id) {
  return request.get(`program/info/${id}`)
}

/**
 * @description 删除项目详情列表
 */
export function deleteProgramApi(id) {
  return request.delete(`program/${id}`)
}

/**
 * @description 获取任务列表
 */
export function getProgramTaskApi(data) {
  return request.get('program_task', data)
}

/**
 * @description 保存任务列表
 */
export function saveProgramTaskApi(data) {
  return request.post(`program_task`, data)
}

/**
 * @description 获取任务详情
 */
export function getProgramTaskInfoApi(id) {
  return request.get(`program_task/info/${id}`)
}

/**
 * @description 修改任务列表
 */
export function putProgramTaskApi(id, data) {
  return request.put(`program_task/${id}`, data)
}

/**
 * @description 获取任务下拉列表
 */
export function getProgramTaskSelectApi(data) {
  return request.get('program_task/select', data)
}

/**
 * @description 获取任项目下拉列表
 */
export function getProgramSelectApi(data) {
  return request.get('program/select', data)
}

/**
 * @description 获取项目版本下拉列表
 */
export function getProgramVersionSelectApi(data) {
  return request.get('program_version/select', data)
}

/**
 * @description 获取项目成员下拉列表
 */
export function getProgramMemberApi(data) {
  return request.get('program/members', data)
}

/**
 * @description 批量修改任务列表
 */
export function putProgramTaskBatchApi(data) {
  return request.post(`program_task/batch`, data)
}

/**
 * @description 批量删除任务列表
 */
export function deleteProgramTaskBatchApi(data) {
  return request.post(`program_task/batch_del`, data)
}

/**
 * @description 保存任务评论
 */
export function saveTaskCommentApi(data) {
  return request.post(`task_comment`, data)
}

/**
 * @description 修改任务评论
 */
export function putTaskCommentApi(id, data) {
  return request.put(`task_comment/${id}`, data)
}

/**
 * @description 删除任务
 */
export function deleteProgramTaskApi(id) {
  return request.delete(`program_task/${id}`)
}

/**
 * @description 删除评论
 */
export function deleteTaskCommentApi(id) {
  return request.delete(`task_comment/${id}`)
}

/**
 * @description 任务评论列表
 */
export function getTaskCommentApi(data) {
  return request.get('task_comment', data)
}

/**
 * @description 任务操作日志列表
 */
export function getDynamicTaskApi(data) {
  return request.get('program_dynamic/task', data)
}

/**
 * @description 项目动态列表
 */
export function getDynamicApi(data) {
  return request.get('program_dynamic/program', data)
}

/**
 * @description 获取项目版本
 */
export function getProgramVersionApi(data) {
  return request.get('program_version', data)
}

/**
 * @description 设置项目版本
 */
export function setProgramVersionApi(id, data) {
  return request.post(`program_version/${id}`, data)
}

/**
 * @description 任务排序
 */
export function putTaskSortApi(data) {
  return request.post(`program_task/sort`, data)
}

/**
 * @description 保存下级任务列表
 */
export function saveSubordinateApi(data) {
  return request.post(`program_task/subordinate`, data)
}

/**
 * @description 获取项目资料列表
 */
export function programFileListApi(data) {
  return request.get(`program_file/index`, data)
}

/**
 * @description 项目资料重命名
 */
export function programrealNameApi(id, data) {
  return request.put(`program_file/real_name/${id}`, data)
}
/**
 * @description 删除项目资料
 */
export function programFileDelApi(id) {
  return request.delete(`program_file/${id}`)
}
