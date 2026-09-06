import axios from 'axios';

export interface DictDataRecord {
    createBy: string;
    createTime: string;
    updateBy?: any;
    updateTime?: any;
    remark: string;
    dictCode: number;
    dictSort: number;
    dictLabel: string;
    dictValue: string;
    dictType: string;
    cssClass: string;
    listClass: string;
    isDefault: string;
    status: string;
    default: boolean;
}

// 查询字典数据列表
export function listData(query: any) {
    return axios({
        url: '/system/dict/data/list',
        method: 'get',
        params: query
    })
}

// 查询字典数据详细
export function getData(dictCode: string) {
    return axios({
        url: `/system/dict/data/${dictCode}`,
        method: 'get'
    })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType: string) {
    return axios({
        url: `/system/dict/data/type/${dictType}`,
        method: 'get'
    })
}

// 新增字典数据
export function addData(data: any) {
    return axios({
        url: '/system/dict/data',
        method: 'post',
        data
    })
}

// 修改字典数据
export function updateData(data: any) {
    return axios({
        url: '/system/dict/data',
        method: 'put',
        data
    })
}

// 删除字典数据
export function delData(dictCode: string) {
    return axios({
        url: `/system/dict/data/${dictCode}`,
        method: 'delete'
    })
}

// 导出字典数据
export function exportData(query: any) {
    return axios({
        url: '/system/dict/data/export',
        method: 'get',
        params: query
    })
}
