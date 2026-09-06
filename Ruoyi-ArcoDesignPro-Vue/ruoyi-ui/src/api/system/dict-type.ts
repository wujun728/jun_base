import axios from 'axios';

export interface DictTypeRecord {
    createBy: string;
    createTime: string;
    updateBy?: any;
    updateTime?: any;
    remark?: any;
    dictId: number;
    dictName: string;
    dictType: string;
    status: string;
}

// 查询字典类型列表
export function listType(query: any) {
    return axios({
        url: '/system/dict/type/list',
        method: 'get',
        params: query
    })
}

// 查询字典类型详细
export function getType(dictId: number) {
    return axios({
        url: `/system/dict/type/${dictId}`,
        method: 'get'
    })
}

// 新增字典类型
export function addType(data: any) {
    return axios({
        url: '/system/dict/type',
        method: 'post',
        data
    })
}

// 修改字典类型
export function updateType(data: any) {
    return axios({
        url: '/system/dict/type',
        method: 'put',
        data
    })
}

// 删除字典类型
export function delType(dictId: number) {
    return axios({
        url: `/system/dict/type/${dictId}`,
        method: 'delete'
    })
}

// 清理参数缓存
export function clearCache() {
    return axios({
        url: '/system/dict/type/clearCache',
        method: 'delete'
    })
}

// 导出字典类型
export function exportType(query: any) {
    return axios({
        url: '/system/dict/type/export',
        method: 'get',
        params: query
    })
}

// 获取字典选择框列表
export function optionSelect() {
    return axios({
        url: '/system/dict/type/optionselect',
        method: 'get'
    })
}
