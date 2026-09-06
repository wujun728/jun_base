import axios from 'axios';

export interface DeptRecord {
    searchValue?: any;
    createBy?: any;
    createTime?: any;
    updateBy?: any;
    updateTime?: any;
    remark?: any;
    deptId: number | undefined;
    parentId?: any;
    ancestors?: any;
    deptName?: string;
    orderNum?: any;
    leader?: string;
    phone?: any;
    email?: any;
    status?: any;
    delFlag?: any;
    parentName?: any;
    children?: any[];
}

// 查询部门列表
export function listDept(query: any) {
    return axios({
        url: '/system/dept/list',
        method: 'get',
        params: query
    })
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(deptId: number) {
    return axios({
        url: `/system/dept/list/exclude/${deptId}`,
        method: 'get'
    })
}

// 查询部门详细
export function getDept(deptId: number) {
    return axios({
        url: `/system/dept/${deptId}`,
        method: 'get'
    })
}

// 新增部门
export function addDept(data: DeptRecord) {
    return axios({
        url: '/system/dept',
        method: 'post',
        data
    })
}

// 修改部门
export function updateDept(data: DeptRecord) {
    return axios({
        url: '/system/dept',
        method: 'put',
        data
    })
}

// 删除部门
export function delDept(deptId: number) {
    return axios({
        url: `/system/dept/${deptId}`,
        method: 'delete'
    })
}
