import request from '@/utils/request'


export function listDbTable(params) {
    return request({
        url: '/online/db/table/list',
        method: 'get',
        params: params
    })
}

export function listDbColumn(tableName) {
    return request({
        url: '/online/db/column/list',
        method: 'get',
        params: { tableName }
    })
}