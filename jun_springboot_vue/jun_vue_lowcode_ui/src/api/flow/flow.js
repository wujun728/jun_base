import request from '@/utils/request'

// 提交表单(发起流程)
export function start(data) {
    return request({
        url: '/nocode/flow/start',
        method: 'post',
        data: data
    })
}

// 处理流程
export function exec(data) {
    return request({
        url: '/nocode/flow/exec',
        method: 'post',
        data: data
    })
}

// 查询我发起的
export function started(query) {
    return request({
        url: '/nocode/flow/data/list/user',
        method: 'get',
        params: query
    })
}

// 查询审批历史
export function history(businessId) {
    return request({
        url: '/nocode/flow/history/' + businessId,
        method: 'get'
    })
}

// 查询表单详情
export function formData(businessId) {
    return request({
        url: '/nocode/flow/data/' + businessId,
        method: 'get'
    })
}

// 查询表单数据
export function formDataList(formId, query) {
    return request({
        url: '/nocode/flow/data/list/' + formId,
        method: 'get',
        params: query
    })
}