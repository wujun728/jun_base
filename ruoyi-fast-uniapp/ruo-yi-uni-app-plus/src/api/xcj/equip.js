import request from '@/utils/request'
// 查询设备列表
export function equipList(data) {
    return request({
        url: '/xcj/equip/listall',
        method: 'GET',
        params: data
    })
}
/**
 * 查询设备信息详情
 * @param id id
 * @returns 设备信息详情
 */
export function equipInfo(id) {
    return request({
        url:`/xcj/equip/${id}`,
        method: 'GET'
    });
}
