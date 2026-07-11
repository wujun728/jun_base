import request from '@/utils/request'


// 查询文档相关数据
export function statDocumentRelate(docId) {
    return request({
        url: '/document/info/stat/' + docId,
        method: 'get',
    })
}

// 新增文档浏览记录
export function addView(data) {
    return request({
        url: '/document/view',
        method: 'post',
        data: data
    })
}
