import axios from 'axios';

export interface TreeMenuSelect {
    id: number;
    label: string;
    children?: TreeMenuSelect[];
}

export interface MenuRecord {
    createBy?: any;
    createTime?: string;
    updateBy?: any;
    updateTime?: any;
    remark?: any;
    menuId?: number;
    menuName?: string;
    parentName?: any;
    parentId?: number;
    orderNum?: number;
    path?: string;
    component?: string;
    query?: string;
    isFrame?: string;
    isCache?: string;
    menuType?: string;
    visible?: string;
    status?: string;
    perms?: string;
    icon?: string;
    children?: any[];
}

// 查询菜单列表
export function listMenu(query: any) {
    return axios({
        url: '/system/menu/list',
        method: 'get',
        params: query
    })
}

// 查询菜单详细
export function getMenu(menuId: number | string) {
    return axios({
        url: `/system/menu/${menuId}`,
        method: 'get'
    })
}

// 查询菜单下拉树结构
export function treeSelect() {
    return axios({
        url: '/system/menu/treeselect',
        method: 'get'
    })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeSelect(roleId: number) {
    return axios({
        url: `/system/menu/roleMenuTreeselect/${roleId}`,
        method: 'get'
    })
}

// 新增菜单
export function addMenu(data: MenuRecord) {
    return axios({
        url: '/system/menu',
        method: 'post',
        data
    })
}

// 修改菜单
export function updateMenu(data: MenuRecord) {
    return axios({
        url: '/system/menu',
        method: 'put',
        data
    })
}

// 删除菜单
export function delMenu(menuId: number | undefined) {
    return axios({
        url: `/system/menu/${menuId}`,
        method: 'delete'
    })
}
