import axios from 'axios';

export interface RoleRecord {
    searchValue?: any;
    createBy?: any;
    createTime: string;
    updateBy?: any;
    updateTime?: any;
    remark: string;
    params: any;
    roleId: number;
    roleName: string;
    roleKey: string;
    roleSort: number | string;
    dataScope: string;
    menuCheckStrictly: boolean;
    deptCheckStrictly: boolean;
    status: string;
    delFlag: string;
    flag: boolean;
    menuIds?: any;
    deptIds?: any;
    permissions?: any;
    admin: boolean;
}

// 查询角色列表
export function listRole(query: any) {
    return axios({
        url: '/system/role/list',
        method: 'get',
        params: query
    })
}

// 查询角色详细
export function getRole(roleId: number | string) {
    return axios({
        url: `/system/role/${roleId}`,
        method: 'get'
    })
}

// 新增角色
export function addRole(data: any) {
    return axios({
        url: '/system/role',
        method: 'post',
        data
    })
}

// 修改角色
export function updateRole(data: any) {
    return axios({
        url: '/system/role',
        method: 'put',
        data
    })
}

// 角色数据权限
export function dataScope(data: any) {
    return axios({
        url: '/system/role/dataScope',
        method: 'put',
        data
    })
}

// 角色状态修改
export function changeRoleStatus(roleId: number | string, status: number | string) {
    const data = {
        roleId,
        status
    }
    return axios({
        url: '/system/role/changeStatus',
        method: 'put',
        data
    })
}

// 删除角色
export function delRole(roleId: string | number) {
    return axios({
        url: `/system/role/${roleId}`,
        method: 'delete'
    })
}

// 查询角色已授权用户列表
export function allocatedUserList(query: any) {
    return axios({
        url: '/system/role/authUser/allocatedList',
        method: 'get',
        params: query
    })
}

// 查询角色未授权用户列表
export function unallocatedUserList(query: any) {
    return axios({
        url: '/system/role/authUser/unallocatedList',
        method: 'get',
        params: query
    })
}

// 取消用户授权角色
export function authUserCancel(data: any) {
    return axios({
        url: '/system/role/authUser/cancel',
        method: 'put',
        data
    })
}

// 批量取消用户授权角色
export function authUserCancelAll(data: any) {
    return axios({
        url: '/system/role/authUser/cancelAll',
        method: 'put',
        params: data
    })
}

// 授权用户选择
export function authUserSelectAll(data: any) {
    return axios({
        url: '/system/role/authUser/selectAll',
        method: 'put',
        params: data
    })
}

// 根据角色ID查询部门树结构
export function deptTreeSelect(roleId: number) {
    return axios({
        url: `/system/role/deptTree/${roleId}`,
        method: 'get'
    })
}
